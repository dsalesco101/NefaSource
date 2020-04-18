package ethos.sql.eventcalendar.queries;

import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.google.common.collect.Lists;
import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.model.content.eventcalendar.ChallengeWinner;
import ethos.model.content.eventcalendar.EventCalendarDay;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.DatabaseManager;
import ethos.sql.DatabaseTable;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;
import ethos.sql.eventcalendar.tables.WinnersTable;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log
class EventCalendarTablesTest {

    private static final String[] USERNAMES = {""};
    private static final String ip = "127.0.0.1";
    private static final String mac = "unknown";
    private static final int amount = 2;

    private static final DatabaseTable PARTICIPANTS_TABLE = new CurrentParticipantsTable();
    private static final DatabaseTable WINNERS_TABLE = new WinnersTable();

    private static void clearDatabases(DatabaseManager manager, DatabaseConfiguration configuration) throws ExecutionException, InterruptedException {
        manager.execute(configuration, (context, connection) -> {
            Statement statement = connection.createStatement();
            if (context.isTablePresent(PARTICIPANTS_TABLE, connection)) {
                statement.executeUpdate("DELETE FROM " + PARTICIPANTS_TABLE.getName());
                log.info("Cleared " + PARTICIPANTS_TABLE.getName());
            }
            if (context.isTablePresent(WINNERS_TABLE, connection)) {
                statement.executeUpdate("DELETE FROM " + WINNERS_TABLE.getName());
                log.info("Cleared " + WINNERS_TABLE.getName());
            }
            return null;
        }).get();
    }

    @Test
    public void test() {
        test_blacklist();
        test_add_another_entry_on_vote();
        test_queries();
        test_player_already_participated();
    }

    @Test
    public void test_blacklist() {
        DatabaseConfiguration configuration = DatabaseConfiguration.EVENT_CALENDAR;
        DatabaseManager manager = new DatabaseManager();

        try {
            clearDatabases(manager, configuration);
            manager.execute(configuration, (context, connection) -> {
                ChallengeParticipant computer_a1 = new ChallengeParticipant(USERNAMES[0], ip, mac, 1);
                ChallengeParticipant computer_a2 = new ChallengeParticipant(USERNAMES[1], ip, mac + "1", 1);
                ChallengeParticipant computer_a3 = new ChallengeParticipant(USERNAMES[2], ip + "1", mac, 1);
                ChallengeParticipant computer_b1 = new ChallengeParticipant(USERNAMES[3], ip + "1", mac + "1", 1);
                new AddToBlacklistQuery(computer_a1).execute(context, connection);
                assertTrue(new CheckForBlacklistQuery(computer_a1).execute(context, connection));
                assertTrue(new CheckForBlacklistQuery(computer_a2).execute(context, connection));
                assertTrue(new CheckForBlacklistQuery(computer_a3).execute(context, connection));
                assertFalse(new CheckForBlacklistQuery(computer_b1).execute(context, connection));
                new RemoveFromBlacklistQuery(computer_a1).execute(context, connection);
                assertFalse(new CheckForBlacklistQuery(computer_a1).execute(context, connection));
                assertFalse(new CheckForBlacklistQuery(computer_a2).execute(context, connection));
                assertFalse(new CheckForBlacklistQuery(computer_a3).execute(context, connection));
                assertFalse(new CheckForBlacklistQuery(computer_b1).execute(context, connection));
                return null;
            });

            clearDatabases(manager, configuration);
            manager.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test_player_already_participated() {
        DatabaseConfiguration configuration = DatabaseConfiguration.EVENT_CALENDAR;
        DatabaseManager manager = new DatabaseManager();

        try {
            clearDatabases(manager, configuration);
            manager.execute(configuration, (context, connection) -> {
                ChallengeParticipant computer_a1 = new ChallengeParticipant(USERNAMES[0], ip, mac, 1);
                ChallengeParticipant computer_a2 = new ChallengeParticipant(USERNAMES[1], ip, mac + "1", 1);
                ChallengeParticipant computer_a3 = new ChallengeParticipant(USERNAMES[2], ip + "1", mac, 1);
                ChallengeParticipant computer_b1 = new ChallengeParticipant(USERNAMES[3], ip + "1", mac + "1", 1);
                new AddParticipantQuery(new ChallengeParticipant(USERNAMES[0], ip, mac, 1), 1).execute(context, connection);
                assertEquals(true, new HasPlayerAlreadyParticipatedQuery(computer_a1).execute(context, connection));
                assertEquals(true, new HasPlayerAlreadyParticipatedQuery(computer_a2).execute(context, connection));
                assertEquals(true, new HasPlayerAlreadyParticipatedQuery(computer_a3).execute(context, connection));
                assertEquals(false, new HasPlayerAlreadyParticipatedQuery(computer_b1).execute(context, connection));
                return null;
            });

            clearDatabases(manager, configuration);
            manager.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test_add_another_entry_on_vote() {
        DatabaseConfiguration configuration = DatabaseConfiguration.EVENT_CALENDAR;
        DatabaseManager manager = new DatabaseManager();

        try {
            clearDatabases(manager, configuration);
            manager.execute(configuration, ((context, connection) -> {
                ChallengeParticipant participant = new ChallengeParticipant(USERNAMES[0], ip, mac, 1);
                new AddParticipantQuery(participant, 1);
                assertEquals(new AddParticipantEntryOnVoteQuery(participant).execute(context, connection), true);
                return null;
            }));

            clearDatabases(manager, configuration);
            manager.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_queries() {
        DatabaseConfiguration configuration = DatabaseConfiguration.EVENT_CALENDAR;
        DatabaseManager manager = new DatabaseManager();

        try {
            clearDatabases(manager, configuration);

            for (EventCalendarDay day : EventCalendarDay.values()) {
                testCalendarSystem(configuration, manager, day.getDay());
            }

            log.info("Printing all winners..");
            for (ChallengeWinner winner : manager.execute(configuration, new GetWinnersListQuery()).get()) {
                log.info(winner.toString());
            }

            log.info("Printing participants..");
            for (EventCalendarDay day : EventCalendarDay.values()) {
                for (ChallengeParticipant participant : manager.execute(configuration, new GetParticipantsListQuery(day.getDay())).get()) {
                    log.info("Iterating over day [" + day + "], partipant: " + participant.toString());
                }
            }

            clearDatabases(manager, configuration);
            manager.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCalendarSystem(DatabaseConfiguration configuration, DatabaseManager manager, int day) throws ExecutionException, InterruptedException {
        List<Future<?>> futureList = Lists.newArrayList();

        for (String username : USERNAMES) {
            futureList.add(manager.execute(configuration, new AddParticipantQuery(new ChallengeParticipant(username, ip, mac, day), amount)));
        }

        for (Future future : futureList) {
            future.get(); // wait
        }

        ChallengeParticipant winner = manager.execute(configuration, new SelectWinnerQuery(day)).get();
        log.info("Winner selected: " + winner + " for day " + day);
    }

}