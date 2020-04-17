package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;
import ethos.model.content.eventcalendar.ChallengeWinner;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.tables.WinnersTable;

public class GetWinnersListQuery implements SqlQuery<List<ChallengeWinner>> {

    private static final WinnersTable TABLE = new WinnersTable();

    @Override
    public List<ChallengeWinner> execute(DatabaseManager context, Connection connection) throws SQLException {
        context.createTableIfNotPresent(TABLE, connection);
        List<ChallengeWinner> list = Lists.newArrayList();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + TABLE.getName());
        while (rs.next()) {
            list.add(new ChallengeWinner(rs.getString(WinnersTable.USERNAME), rs.getInt(WinnersTable.DAY)));
        }
        return list;
    }
}
