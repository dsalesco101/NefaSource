package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.DatabaseTable;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.BlacklistTable;

public class AddToBlacklistQuery implements SqlQuery<Boolean> {

    private static final DatabaseTable TABLE = new BlacklistTable();

    private final ChallengeParticipant participant;

    public AddToBlacklistQuery(ChallengeParticipant participant) {
        this.participant = participant;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        connection.createStatement().execute("insert into " + TABLE.getName()+ " values("
               + "'" + participant.getIpAddress() + "', '" + participant.getMacAddress() + "')");
        return true;
    }

}
