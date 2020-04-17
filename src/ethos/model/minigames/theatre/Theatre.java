package ethos.model.minigames.theatre;

import java.util.ArrayList;
import java.util.List;

import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;

public class Theatre {

	private static Theatre[] games = new Theatre[500];
	private static ArrayList theatreTeam;
	static int index;

	public static void start(List<Player> lobbyPlayers) {
		Theatre theatre = new Theatre();
		addGame(theatre);
		setTheatreTeam(lobbyPlayers);
		for (Player p : lobbyPlayers) {
			p.getPA().movePlayer((3218 + Misc.random(0, 3)), (4459 + Misc.random(0, 1)), getIndex()*4);
			p.sendMessage("Welcome to The Theatre of Blood!");
		}
	}
	public void stop() {
		removeGame(this);
	}

	private static void addGame(Theatre theatre) {
		int freeIndex = getFreeIndex();
		games[freeIndex] = theatre;
		games[freeIndex].setIndex(freeIndex);
		setIndex(freeIndex);

	}
	public static void removeGame(Theatre theatre) {
		if (games[getIndex()] == null)
			return;
		games[getIndex()] = null;
		for (int i = 0; i < games.length; i++) {
			if (games[i] != null && games[i].getTheatreTeam().size() <= 0) {
				games[i] = null;
			}
		}
	}
	public static void setTheatreTeam(List<Player> list) {
		theatreTeam = new ArrayList<>(list);
	}		
	public List<Player> getTheatreTeam() {
		return theatreTeam;
	}
	private static int getFreeIndex() {
		for (int i = 0; i < games.length; i++) {
			if (games[i] == null) {
				return i;
			}
		}
		return -1;
	}
	public static int getIndex() {
		return index;
	}
	public static void setIndex(int z) {
		index = z;
	}
}
