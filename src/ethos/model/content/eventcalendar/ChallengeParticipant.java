package ethos.model.content.eventcalendar;

import ethos.model.players.Player;

public class ChallengeParticipant {

    private final String username;
    private final String ipAddress;
    private final String macAddress;
    private final int entryDay;

    public ChallengeParticipant(Player player, DateProvider dateProvider) {
        this(player.playerName, player.getIpAddress(), player.getMacAddress(), dateProvider.getDay());
    }

    public ChallengeParticipant(String username, String ipAddress, String macAddress, int entryDay) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.entryDay = entryDay;
    }

    @Override
    public String toString() {
        return "ChallengeParticipant{" +
                "username='" + username + '\'' +
                ", entryDay=" + entryDay +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public int getEntryDay() {
        return entryDay;
    }
}
