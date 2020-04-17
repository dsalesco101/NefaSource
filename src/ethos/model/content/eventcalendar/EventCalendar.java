package ethos.model.content.eventcalendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import ethos.Server;
import ethos.ServerState;
import ethos.model.content.achievement.AchievementHandler;
import ethos.model.content.achievement.AchievementTier;
import ethos.model.content.achievement.Achievements;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.eventcalendar.queries.AddParticipantQuery;
import ethos.sql.eventcalendar.queries.CheckForBlacklistQuery;
import ethos.sql.eventcalendar.queries.GetParticipantsListQuery;
import ethos.sql.eventcalendar.queries.GetWinnersListQuery;
import ethos.sql.eventcalendar.queries.HasPlayerAlreadyParticipatedQuery;
import ethos.util.Misc;

/**
 * Event calendar.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class EventCalendar {

    /**
     * The month the event will take place.
     */
    public static final Month MONTH = Month.FEBRUARY;

    /**
     * The year the event will take place.
     */
    public static final int YEAR = 2020;

    /**
     * The name of the event.
     */
    public static final String EVENT_NAME = "February Event";

    /**
     * The message colour for messages involving the Event Calendar.
     */
    public static final String MESSAGE_COLOUR = "<col=06629c>";

    /**
     * The {@link DateProvider}.
     * You can change this to {@link DateProviderEventStarted} to test the start date,
     * or {@link DateProviderEventEnded} to test the day after the event ended.
     */
    private static DateProvider dateProvider = new DateProviderStandard();

    private static final int INTERFACE_ID = 23332;
    private static final int DAY_CONFIG_ID = 1358;
    private static final int WINNERS_CONTAINER_INTERFACE_ID = 23348;
    private static final int TODAYS_PARTICIPANTS_CONTAINER_INTERFACE_ID = 23352;
    private static final int WINNERS_NUMBER_CONTAINER_INTERFACE_ID = 23_349;
    private static final int RULES_BUTTON_ID = 23_344;
    private static final int CURRENT_STATE_STRING_INTERFACE_ID = 23341;
    private static final String RULES_BUTTON_URL = "";
    private static final boolean LEAP_YEAR = Misc.isLeapYear();

    /**
     * Verify the calendar will worth every day for the month selected.
     */
    public static void verifyCalendar() {
        Preconditions.checkState(MONTH.length(LEAP_YEAR) <= EventCalendarDay.values().length,
                "Event calendar days do not match the month length ["
                            + "Month length is " + MONTH.length(LEAP_YEAR) + " days, "
                            + "EventCalendarDays enum length is " + EventCalendarDay.values().length + "]");
    }

    /**
     * Check if the event is running.
     * @return <code>true</code> if the event is running
     */
    public static boolean isEventRunning() {
        return isEventRunning(dateProvider.getLocalDate());
    }

    /**
     * Check if the event is running on the specified day.
     * @param localDate the date to check
     * @return <code>true</code> if the event is running
     */
    public static boolean isEventRunning(LocalDate localDate) {
        return localDate.getMonth() == MONTH && getChallenge(localDate) != null;
    }

    /**
     * @return today's challenge
     */
    public static EventChallenge getChallenge() {
        return getChallenge(getDateProvider().getLocalDate());
    }

    /**
     * @return today's challenge
     * @param localDate the local date
     */
    private static EventChallenge getChallenge(LocalDate localDate) {
        if (localDate.getMonth() == MONTH) {
            EventCalendarDay eventCalendarDay = EventCalendarDay.forDayOfTheMonth(localDate.getDayOfMonth());
            if (eventCalendarDay != null) {
                return eventCalendarDay.getChallenge();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Generates a key for the {@link EventCalendar#challengeProgress} map
     * @param eventChallenge the event challenge
     * @return the generated {@link EventChallengeKey}
     */
    public static EventChallengeKey generateKey(EventChallenge eventChallenge) {
        return generateKey(eventChallenge, dateProvider.getDay());
    }

    /**
     * Generates a key for the {@link EventCalendar#challengeProgress} map
     * @param eventChallenge the event challenge
     * @param day the event day
     * @return the generated {@link EventChallengeKey}
     */
    public static EventChallengeKey generateKey(EventChallenge eventChallenge, int day) {
        EventCalendarDay eventCalendarDay = EventCalendarDay.forDayOfTheMonth(day);
        return new EventChallengeKey(eventCalendarDay, eventChallenge);
    }

    public static DateProvider getDateProvider() {
        return dateProvider;
    }

    /**
     * Sets the date provider.
     * @param dateProvider the {@link DateProvider}
     */
    public static void setDateProvider(DateProvider dateProvider) {
        Preconditions.checkState(Server.getConfiguration().getServerState() != ServerState.PUBLIC_PRIMARY,
                "You cannot set the date provider while it's in the " + Server.getConfiguration().getServerState() + " state.");
        EventCalendar.dateProvider = dateProvider;
    }

    private final Player player;
    private final Map<EventChallengeKey, Integer> challengeProgress = new HashMap<>();

    public EventCalendar(Player player) {
        this.player = player;
    }

    public void openCalendar() {
        if (player.combatLevel >= 126) {
            player.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
        }
        if ( player.combatLevel >= 100){

            player.getEventCalendar().progress(EventChallenge.COMPLETE_X_HARD_ACHIEVEMENTS);
            player.getEventCalendar().progress(EventChallenge.COMPLETE_X_HARD_ACHIEVEMENTS);
            player.getEventCalendar().progress(EventChallenge.COMPLETE_X_HARD_ACHIEVEMENTS);
        }

        if (dateProvider.getMonth() == Month.FEBRUARY && dateProvider.getYear() == YEAR) {
            openCalendar(dateProvider.getDay());
        } else {
            if (dateProvider.isBefore(MONTH, YEAR)) {
                player.sendMessage(MESSAGE_COLOUR + "The " + MONTH.getDisplayName(TextStyle.FULL, Locale.US) + " event hasn't started yet.");
            } else {
                player.sendMessage(MESSAGE_COLOUR + "The " + MONTH.getDisplayName(TextStyle.FULL, Locale.US) + " has ended, thanks for playing.");
            }

            if (player.getRights().contains(Right.GAME_DEVELOPER)) {
                player.sendMessage(MESSAGE_COLOUR + "You're a developer, so even though the event "
                        + (dateProvider.isBefore(MONTH, YEAR) ? "hasn't started" : "has ended")
                        + " you see the interface.");
                openCalendar(1);
            }
        }
    }

    private void openCalendar(final int day) {
        Preconditions.checkArgument(day >= 0 && day <= MONTH.length(LEAP_YEAR));
        EventCalendarDay eventCalendarDay = EventCalendarDay.forDayOfTheMonth(day);
        if (player.combatLevel >= 126) {
            player.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
        }

        Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, (context, connection) -> {
            List<ChallengeParticipant> participantList = new GetParticipantsListQuery(day).execute(context, connection);
            List<ChallengeWinner> winnerList = new GetWinnersListQuery().execute(context, connection);
            player.addQueuedAction(player -> {
                if (isCompleted(eventCalendarDay.getChallenge())) {
                    player.getPA().sendString("@gre@Completed", CURRENT_STATE_STRING_INTERFACE_ID);
                } else {
                    int ticksCompleted = challengeProgress.getOrDefault(new EventChallengeKey(eventCalendarDay, eventCalendarDay.getChallenge()), 0);
                    int totalForCompletion = eventCalendarDay.getChallenge().getTicks();
                    player.getPA().sendString("@red@" + ticksCompleted + "/" + totalForCompletion, CURRENT_STATE_STRING_INTERFACE_ID);
                }
                player.getPA().sendStringContainer(WINNERS_NUMBER_CONTAINER_INTERFACE_ID, winnerList.stream().map(participant ->
                        participant.getDay() + ".").collect(Collectors.toList()));
                player.getPA().sendStringContainer(WINNERS_CONTAINER_INTERFACE_ID, winnerList.stream().map(participant ->
                        Misc.formatPlayerName(participant.getUsername())).collect(Collectors.toList()));
                player.getPA().sendStringContainer(TODAYS_PARTICIPANTS_CONTAINER_INTERFACE_ID, participantList.stream().map(participant ->
                        Misc.formatPlayerName(participant.getUsername())).distinct().limit(100).collect(Collectors.toList()));
                player.getPA().sendConfig(DAY_CONFIG_ID, day);
                player.getPA().showInterface(INTERFACE_ID);
            });
            return null;
        });

    }

    public Future<?> progress(EventChallenge eventChallenge) {
        return progress(eventChallenge, 1);
    }

    public Future<?> progress(EventChallenge eventChallenge, int amount) {
        Objects.requireNonNull(eventChallenge);
        if (eventChallenge == getChallenge() && !isCompleted(eventChallenge)) {
            EventChallengeKey key = generateKey(eventChallenge);
            int previousAmount = challengeProgress.getOrDefault(key, 0);
            challengeProgress.put(key, amount + challengeProgress.getOrDefault(key, 0));
            int currentAmount = challengeProgress.get(key);

            if (player.getRights().contains(Right.GAME_DEVELOPER)) {
                player.sendMessage("Ticked " + eventChallenge + " x" + amount + ", current progress is "
                        + currentAmount + ", day " + getDateProvider().getDay());
            }

            if (currentAmount >= eventChallenge.getTicks()) {
                return completed(eventChallenge);
            } else {
                int halfway = eventChallenge.getTicks() / 2;
                if (currentAmount >= halfway && previousAmount < halfway) {
                    player.sendMessage(MESSAGE_COLOUR + "You're halfway to completing the challenge '" + eventChallenge.getFormattedString() + "'!");
                }
            }
        }

        return null;
    }

    private Future<?> completed(EventChallenge eventChallenge) {
        final boolean voted = player.getLastVote().toEpochDay() == LocalDate.now().toEpochDay();
        final ChallengeParticipant participant = new ChallengeParticipant(player, dateProvider);
        return Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, (context, connection) -> {
            if (new HasPlayerAlreadyParticipatedQuery(participant).execute(context, connection)) {
                player.sendMessage(MESSAGE_COLOUR + "You've already participated in the " + EVENT_NAME + " today.");
            } else if (new CheckForBlacklistQuery(participant).execute(context, connection)) {
                player.sendMessage(MESSAGE_COLOUR + "You've been blacklisted and can no longer compete in the event.");
            } else {
                new AddParticipantQuery(participant, voted ? 2 : 1).execute(context, connection);
                synchronized (player) {
                    player.sendMessage(MESSAGE_COLOUR + "You've completed the challenge '" + eventChallenge.getFormattedString() + "'!");
                    player.sendMessage(MESSAGE_COLOUR + "You've been entered " + (voted ? "twice" : "once") + " into the drawing for today's reward drawing.");
                    if (!voted) {
                        player.sendMessage(MESSAGE_COLOUR + "You haven't voted, if you vote you will be entered into the drawing a second time.");
                    }
                }
            }
            return null;
        });
    }

    private boolean isCompleted(EventChallenge eventChallenge) {
        return challengeProgress.getOrDefault(generateKey(eventChallenge), 0) >= eventChallenge.getTicks();
    }

    public boolean handleButton(int buttonId) {
        if (buttonId == RULES_BUTTON_ID) {
            player.getPA().openWebAddress(RULES_BUTTON_URL);
            return true;
        }
        return false;
    }

    /**
     * Sets the challenge progress on a certain {@link EventChallenge}
     * @param eventChallengeKey the event challenge key
     * @param progress the amount of progress to set
     */
    public void set(EventChallengeKey eventChallengeKey, int progress) {
        Objects.requireNonNull(eventChallengeKey);
        challengeProgress.put(eventChallengeKey, progress);
    }

    /**
     * @return an unmodifiable set of the challenge progress entries.
     */
    public Set<Map.Entry<EventChallengeKey, Integer>> getEntries() {
        return Collections.unmodifiableSet(challengeProgress.entrySet());
    }
}
