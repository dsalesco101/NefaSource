package ethos.model.content.eventcalendar;

public class ChallengeWinner {

    private final String username;
    private final int day;

    public ChallengeWinner(String username, int day) {
        this.username = username;
        this.day = day;
    }

    @Override
    public String toString() {
        return "ChallengeWinner{" +
                "username='" + username + '\'' +
                ", day=" + day +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public int getDay() {
        return day;
    }
}
