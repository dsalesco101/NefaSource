package ethos.sql.eventcalendar.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.sql.DatabaseManager;
import ethos.sql.SqlQuery;
import ethos.util.Misc;

public class SelectWinnerQuery implements SqlQuery<ChallengeParticipant> {

    private final int day;

    public SelectWinnerQuery(int day) {
        this.day = day;
    }

    @Override
    public ChallengeParticipant execute(DatabaseManager context, Connection connection) throws SQLException {
        List<ChallengeParticipant> currentParticpants = new GetParticipantsListQuery(day).execute(context, connection);

        if (currentParticpants.isEmpty()) {
            return null;
        } else {
            ChallengeParticipant winner = currentParticpants.get(Misc.random3(currentParticpants.size()));
            new AddWinnerQuery(winner).execute(context, connection);
            return winner;
        }
    }
}
