package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;

public class HasPlayerAlreadyParticipatedQuery implements SqlQuery<Boolean> {

    private static final CurrentParticipantsTable TABLE = new CurrentParticipantsTable();

    private final ChallengeParticipant participant;

    public HasPlayerAlreadyParticipatedQuery(ChallengeParticipant participant) {
        this.participant = participant;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        String dayCondition = CurrentParticipantsTable.ENTRY_DAY + "=" + participant.getEntryDay();
        ResultSet rs = connection.createStatement().executeQuery("select * from " + TABLE.getName() + " where "
                + dayCondition +  " and " + CurrentParticipantsTable.USERNAME + "='" + participant.getUsername() + "'"
                + " or " + dayCondition + " and " + CurrentParticipantsTable.MAC_ADDRESS + "='" + participant.getMacAddress() + "'"
                + " or " + dayCondition + " and " + CurrentParticipantsTable.IP_ADDRESS + "='" + participant.getIpAddress() + "'");
        return rs.next();
    }

}
