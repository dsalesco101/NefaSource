package ethos.model.npcs.bosses;

import ethos.Server;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;
import ethos.model.players.Right;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 12/5/19
 *
 */
public class Kraken {

	private Player player;
	private KrakenInstance instance;
	public static final int KRAKEN_ID = 494;
	private static final int HITPOINTS = 255;
	private static final int MAX_DAMAGE = 30;
	private static final int ATTACK_STAT = 250;
	private static final int DEFENCE_STAT = 250;
	private NPC npc;
	
	public Kraken(Player player) {
		this.player = player;
		this.instance = null;
		this.setNpc(null);
	}
	
	public void init() {
		if (player == null) {
			return;
		}
		
		if (!player.getSlayer().getTask().isPresent() ||
				!player.getSlayer().getTask().get().getPrimaryName().contains("kraken") && player.getRights().getPrimary() != Right.OWNER) {
			player.sendMessage("You must have an active kraken task in order to do this.");
			return;
		}
		
		if (instance != null) {
			instance.dispose();
			InstancedAreaManager.getSingleton().disposeOf(instance);
		}
		
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(Boundary.KRAKEN_CAVE);
		instance = new KrakenInstance(player, Boundary.KRAKEN_CAVE, height);
		if (instance != null) {
			InstancedAreaManager.getSingleton().add(height, instance);
			navigatePlayer();
			spawnKraken();
		}
	}
	
	public void navigatePlayer() {
		player.getPA().movePlayer(new Coordinate(2280, 10022, instance.getHeight()));
		player.sendMessage("Goodluck!");
	}
	
	public void spawnKraken() {
		setNpc(Server.npcHandler.spawnNpc(player, KRAKEN_ID, 2279, 10035, instance.getHeight(), -1, HITPOINTS, MAX_DAMAGE, ATTACK_STAT, DEFENCE_STAT, true, false));
	}
	
	public void handleKill() {
	
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}
	
	public KrakenInstance getInstance() {
		return instance;
	}
}
