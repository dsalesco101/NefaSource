package ethos.model.content.eventcalendar;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.google.common.base.Preconditions;
import ethos.Server;
import ethos.ServerState;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.DatabaseTable;
import ethos.sql.eventcalendar.queries.AddToBlacklistQuery;
import ethos.sql.eventcalendar.queries.RemoveFromBlacklistQuery;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;
import ethos.sql.eventcalendar.tables.WinnersTable;

public class EventCalendarHelper {

    public static void blacklistCommand(Player c, String playerCommand, boolean blacklist) {
        String[] split = playerCommand.split(" ", 2);
        if (split.length < 2) {
            c.sendMessage("Invalid command format: [::calblacklist username with spaces]");
        } else {
            String username = split[1];
            Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(username);
            if (optionalPlayer.isPresent()) {
                Player other = optionalPlayer.get();
                final ChallengeParticipant participant = new ChallengeParticipant(other, EventCalendar.getDateProvider());
                Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, (context, configuration) -> {
                    if (blacklist) {
                        if (new AddToBlacklistQuery(participant).execute(context, configuration) != null) {
                            synchronized (c) {
                                c.sendMessage("Successfully blacklisted player: " + username);
                            }
                            synchronized (other) {
                                other.sendMessage("You've been prevented from competing in the rest of the " + EventCalendar.EVENT_NAME + ".");
                            }
                        } else {
                            synchronized (c) {
                                c.sendMessage("An unexpected error occurred while attempting to blacklist player: " + username);
                            }
                        }
                    } else {
                        if (new RemoveFromBlacklistQuery(participant).execute(context, configuration) != null) {
                            synchronized (c) {
                                c.sendMessage("Successfully unblacklisted player: " + username);
                            }
                            synchronized (other) {
                                other.sendMessage("You are now allowed to compete in the " + EventCalendar.EVENT_NAME + ".");
                            }
                        } else {
                            synchronized (c) {
                                c.sendMessage("An unexpected error occurred while attempting to unblacklist player: " + username);
                            }
                        }
                    }
                    return null;
                });
            } else {
                c.sendMessage("Player cannot be found: " + username);
            }
        }
    }

    private static void checkTestingConditions() {
        Preconditions.checkState(Server.getConfiguration().getServerState() != ServerState.PUBLIC_PRIMARY,
                "You cannot test the calendar while the server is in public primary state.");
    }

    public static boolean doTestingCommand(Player c, String command) throws ExecutionException, InterruptedException {
        if (!c.isManagement()) {
            return false;
        }

        String[] split = command.split(" ");
        switch (split[0]) {
            case "calvotetest":
                c.setLastVote(LocalDate.now());
                c.sendMessage("Set voted");
                return true;
            case "calreset":
                resetTables();
                resetCalendarProgress(c);
                c.sendMessage("Reset calendar tables and local player progress");
                return true;
            case "calday":
                int day = Integer.parseInt(split[1]);
                if (day < 1) {
                    c.sendMessage("Invalid day: " + day);
                    return true;
                } else {
                    c.sendMessage("If you haven't set day to 1 to winner selection won't work, do that first!");
                    setCalendarDay(c, day);
                    c.getEventCalendar().openCalendar();
                }
                return true;
            case "caltick":
                int ticks = -1;
                if (split.length == 2)
                    ticks = Integer.parseInt(split[1]);
                tickCalendarProgress(c, ticks);
                c.getEventCalendar().openCalendar();
                return true;
            case "caltestall":
                testAllDays(c);
                return true;
            case "calprogress":
                day = Integer.parseInt(split[1]);
                if (day < 1) {
                    c.sendMessage("Invalid day: " + day);
                    return true;
                } else {
                    c.sendMessage("If you haven't set day to 1 to winner selection won't work, do that first!");
                    setCalendarDay(c, day);
                    tickCalendarProgress(c, -1);
                    c.getEventCalendar().openCalendar();
                }
                return true;
        }
        return false;
    }

    private static void resetCalendarProgress(Player c) {
        for (EventCalendarDay day : EventCalendarDay.values()) {
            c.getEventCalendar().set(new EventChallengeKey(day, day.getChallenge()), 0);
        }
    }

    private static void resetTables() throws ExecutionException, InterruptedException {
        Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, (context, connection) -> {
            Statement statement = connection.createStatement();
            final DatabaseTable PARTICIPANTS_TABLE = new CurrentParticipantsTable();
            final DatabaseTable WINNERS_TABLE = new WinnersTable();
            if (context.isTablePresent(PARTICIPANTS_TABLE, connection)) {
                statement.executeUpdate("DELETE FROM " + PARTICIPANTS_TABLE.getName());
            }
            if (context.isTablePresent(WINNERS_TABLE, connection)) {
                statement.executeUpdate("DELETE FROM " + WINNERS_TABLE.getName());
            }
            return null;
        }).get();
    }

    private static void testAllDays(Player c) throws ExecutionException, InterruptedException {
        resetTables();
        resetCalendarProgress(c);

        for (EventCalendarDay day : EventCalendarDay.values()) {
            setCalendarDay(c, day.getDay());
            Future<?> tickFuture = tickCalendarProgress(c, -1);
            if (tickFuture != null) {
                tickFuture.get();
            }
            Future<?> winnerFuture = EventCalendarWinnerSelection.getSingleton().tick();
            if (winnerFuture != null) {
                winnerFuture.get();
            }
        }
        setCalendarDay(c, 999);
    }

    private static Future<?> tickCalendarProgress(Player c, int ticks) {
        EventChallenge challenge = EventCalendar.getChallenge();
        if (challenge == null) {
            c.sendMessage("No challenge for today, use ::calday day_number");
        } else {
            ticks = ticks == -1 ? challenge.getTicks() : ticks;
            return c.getEventCalendar().progress(challenge, ticks);
        }
        return null;
    }

    private static void setCalendarDay(Player c, int day) {
        c.sendMessage("Setting event calendar day to " + day);
        if ((day - 1) >= EventCalendarDay.values().length) {
            EventCalendar.setDateProvider(new DateProviderEventEnded());
            c.sendMessage("You set a day after the event ended, event will now end.");
        } else {
            EventCalendar.setDateProvider(new DateProviderEventStarted(day));
            if (day == 1) {
                EventCalendarWinnerSelection.setSingleton(EventCalendarWinnerSelection.getStandard());
                c.sendMessage("Setup winner selection.");
            }
        }
    }

}
