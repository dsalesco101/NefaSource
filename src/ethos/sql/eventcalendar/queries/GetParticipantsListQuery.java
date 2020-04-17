package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.List;

import com.google.common.collect.Lists;
import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;

public class GetParticipantsListQuery implements SqlQuery<List<ChallengeParticipant>> {

    private static final CurrentParticipantsTable TABLE = new CurrentParticipantsTable();

    private final int day;

    public GetParticipantsListQuery(int day) {
        this.day = day;
    }

    @Override
    public List<ChallengeParticipant> execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        List<ChallengeParticipant> list = Lists.newArrayList();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + TABLE.getName()
                + " WHERE " + CurrentParticipantsTable.ENTRY_DAY + "=" + day);
        while (rs.next()) {
            list.add(new ChallengeParticipant(rs.getString(CurrentParticipantsTable.USERNAME),
                    rs.getString(CurrentParticipantsTable.IP_ADDRESS),
                    rs.getString(CurrentParticipantsTable.MAC_ADDRESS),
                    rs.getInt(CurrentParticipantsTable.ENTRY_DAY)));
        }
        list.sort((p1, p2) -> Collator.getInstance().compare(p1.getUsername(), p2.getUsername()));
        return list;
    }
}
