package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.common.base.Preconditions;
import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;

public class AddParticipantQuery implements SqlQuery<Object> {

    private static final CurrentParticipantsTable TABLE = new CurrentParticipantsTable();

    private final ChallengeParticipant participant;
    private final int amount;

    public AddParticipantQuery(ChallengeParticipant participant, int amount) {
        this.participant = participant;
        this.amount = amount;
    }

    @Override
    public Object execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        Preconditions.checkState(amount > 0);
        Statement statement = connection.createStatement();
        for (int count = 0; count < amount; count++) {
            statement.execute(
                    "INSERT INTO " + TABLE.getName()
                            + " VALUES ("
                            + "'" + participant.getUsername() + "',"
                            + "'" + participant.getIpAddress() + "',"
                            + "'" + participant.getMacAddress() + "',"
                            + participant.getEntryDay()
                            + ")"
            );
        }
        return null;
    }
}
