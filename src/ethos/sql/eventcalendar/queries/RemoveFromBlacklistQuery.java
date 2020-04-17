package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.DatabaseTable;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.BlacklistTable;

public class RemoveFromBlacklistQuery implements SqlQuery<Boolean> {

    private static final DatabaseTable TABLE = new BlacklistTable();

    private final ChallengeParticipant participant;

    public RemoveFromBlacklistQuery(ChallengeParticipant participant) {
        this.participant = participant;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        connection.createStatement().execute("delete from " + TABLE.getName() + " where "
                + BlacklistTable.MAC_ADDRESS + "='" + participant.getMacAddress() + "' and " +
                BlacklistTable.IP_ADDRESS + "='" + participant.getIpAddress() + "'");
        return true;
    }

}
