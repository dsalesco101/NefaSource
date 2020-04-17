package ethos.sql.eventcalendar.runners;

import java.sql.Connection;
import java.sql.SQLException;

import ethos.model.content.eventcalendar.EventCalendarDay;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.queries.GetParticipantsListQuery;

public class PrintParticipants implements SqlQuery {

    @Override
    public Object execute(DatabaseManager context, Connection connection) throws SQLException {
        for (EventCalendarDay day : EventCalendarDay.values()) {
            System.out.println("------- Participants for day " + day.getDay()
                    + ", challenge " + day.getChallenge().getFormattedString() + " -------");
            new GetParticipantsListQuery(day.getDay()).execute(context, connection).forEach(
                    participant -> System.out.println(participant.getUsername()));
        }
        return null;
    }
}
