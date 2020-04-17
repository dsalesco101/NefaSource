package ethos.model.minigames.xeric;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ethos.model.players.Player;
import ethos.util.Misc;
/**
 * 
 * @author Patrity
 * 
 * TODO Deprecate old/unused code
 * 
 */
public class XericLobby {

	public static boolean lobbyActive = false;//players cannot go into other raid rooms while this is true
	
	private static List <Player> xericLobby = new CopyOnWriteArrayList<Player>();//gets players in lobby
	
	public static int xericLobbyTimer = 60;// how many seconds til timer starts

	public static int timeLeft = 0;// how much time is left until raid starts
	
	private static Xeric[] games = new Xeric[500];
	
	private static final int SET_WAIT_TIMER = 60;
	

	/*public static void process() {
		XericLobby.lobbyActive = true;
	
		if (xericLobbyTimer % 5 == 0) {
			if (xericLobby.size() == 0) {
				xericLobbyTimer = SET_WAIT_TIMER;
			}
		}
		
		if (xericLobbyTimer-- <= 0) {
			create();
			xericLobbyTimer = SET_WAIT_TIMER;
		}
	}*/
	
	public static void joinXericLobby(Player player) {//attempt to join raid lobby area
		//conditions
		if (xericLobby.contains(player)) {
			return;
		}
		if (player.combatLevel < 70) {//combat level requirement to join raid
			player.sendMessage("You need a combat level of atleast 70 to join raids.");
			return;
		}
		
		XericLobby.xericLobby.add(player);//adds player to lobby
		player.sendMessage("You are now in the lobby for Trials of Xeric. The Raid will be starting soon.");
	}
	
	public static void removePlayer(Player player) {
		xericLobby.remove(player);
	}
	
	private static void create() {	
		List<Player> joining = new ArrayList<>();
		int added = 0;
		Xeric xeric = new Xeric();
		addGame(xeric);
		
		for (Player p : XericLobby.xericLobby) {
			
			if (added > 7) {// max amount of allowed players in a raid at a time
				p.sendMessage("The lobby is full at the moment, try again in a minute.");
				continue;
			}
			p.xericDamage = 0;
			p.setXeric(xeric);
			p.getPA().removeAllWindows();
			p.getPA().movePlayer((Misc.random(3) + 2715), (Misc.random(3) + 5470), xeric.getIndex() * 4);
			p.sendMessage("Welcome to the Trials of Xeric. Your first wave will start soon.", 255);
			joining.add(p);
			xericLobby.remove(p);
			added++;
			
		}	
		xeric.setXericTeam(joining);
		xeric.spawn();
	}
	public static void start(List<Player> lobbyPlayers) {
		List<Player> joining = new ArrayList<>();
		int added = 0;
		Xeric xeric = new Xeric();
		addGame(xeric);
		
		for (Player p : lobbyPlayers) {
			
			if (added > 7) {// max amount of allowed players in a raid at a time
				p.sendMessage("The lobby is full at the moment, try again in a minute.");
				continue;
			}
			p.xericDamage = 0;
			p.setXeric(xeric);
			p.getPA().removeAllWindows();
			p.getPA().movePlayer((Misc.random(3) + 2715), (Misc.random(3) + 5470), xeric.getIndex() * 4);
			p.sendMessage("Welcome to the Trials of Xeric. Your first wave will start soon.", 255);
			joining.add(p);
			xericLobby.remove(p);
			added++;
			
		}	
		xeric.setXericTeam(joining);
		xeric.spawn();
		
	}
	
	private static int getFreeIndex() {
		for (int i = 0; i < games.length; i++) {
			if (games[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static void addGame(Xeric xeric) {
		int freeIndex = getFreeIndex();
		games[freeIndex] = xeric;
		games[freeIndex].setIndex(freeIndex);
		xeric.setIndex(freeIndex);
	}
	
	public static void removeGame(Xeric xeric) {
		if (games[xeric.getIndex()] == null)
			return;
		games[xeric.getIndex()] = null;
		for (int i = 0; i < games.length; i++) {
			if (games[i] != null && games[i].getXericTeam().size() <= 0) {
				games[i] = null;
			}
		}
	}

	public static List<Player> getRaidLobby() {
		return xericLobby;
	}


}
	
	
	