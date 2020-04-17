package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.model.content.eventcalendar.ChallengeWinner;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.WinnersTable;
import lombok.extern.java.Log;

@Log
public class AddWinnerQuery implements SqlQuery {

    public static void addWinner(DatabaseManager databaseManager, DatabaseConfiguration databaseConfiguration, String winner, int day) throws ExecutionException, InterruptedException {
        List<ChallengeWinner> winners = databaseManager.execute(databaseConfiguration, new GetWinnersListQuery()).get();
        Optional<ChallengeWinner> presentWinner = winners.stream().filter(it -> it.getDay() == day).findFirst();
        if (presentWinner.isPresent()) {
            System.out.println("Winner already exists for day " + day + ", name: " + presentWinner.get().getUsername());
        } else {
            databaseManager.execute(databaseConfiguration, new AddWinnerQuery(new ChallengeParticipant(winner, "manual_entry_no_address", "manual_entry_no_address", day))).get();
            log.info("Added winner " + winner + " for day " + day);
        }
    }

    private static final WinnersTable TABLE = new WinnersTable();

    private final ChallengeParticipant participant;

    public AddWinnerQuery(ChallengeParticipant participant) {
        this.participant = participant;
    }

    @Override
    public Object execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        connection.createStatement().execute(
                "INSERT INTO " + TABLE.getName()
                + " VALUES ("
                + "'" + participant.getUsername() + "',"
                + participant.getEntryDay()
                +")"
        );
        return null;
    }

}
