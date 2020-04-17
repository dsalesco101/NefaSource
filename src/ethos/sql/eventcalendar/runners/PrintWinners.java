package ethos.sql.eventcalendar.runners;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.EventCalendarDay;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.queries.GetParticipantsListQuery;
import ethos.sql.eventcalendar.queries.GetWinnersListQuery;

public class PrintWinners<Object> implements SqlQuery<Object> {

    @Override
    public Object execute(DatabaseManager context, Connection connection) throws SQLException {
        new GetWinnersListQuery().execute(context, connection).forEach(
                winner -> System.out.println("Winner for day " + winner.getDay() + ": "
                        + winner.getUsername()));
        return null;
    }
}
