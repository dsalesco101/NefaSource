package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.DatabaseTable;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;

public class AddParticipantEntryOnVoteQuery implements SqlQuery<Boolean> {

    private static final DatabaseTable TABLE = new CurrentParticipantsTable();

    private final ChallengeParticipant participant;

    public AddParticipantEntryOnVoteQuery(ChallengeParticipant participant) {
        this.participant = participant;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + TABLE.getName() + " WHERE "
                + CurrentParticipantsTable.USERNAME + "='" + participant.getUsername() + "'"
                + " AND " + CurrentParticipantsTable.ENTRY_DAY + "=" + participant.getEntryDay());
        int count = 0;
        while (rs.next()) {
            count++;
        }

        if (count == 1) {
            new AddParticipantQuery(participant, 1).execute(context, connection);
            return true;
        } else {
            return false;
        }
    }

}
