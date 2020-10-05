package ethos.model.npcs.bosses;


import java.util.function.Consumer;

import ethos.model.content.instances.InstancedArea;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDefinitions;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;


public class Hunllef extends InstancedArea {

	public static void rewardPlayers(Player player) {
		if (Boundary.isIn(player, Boundary.HUNLLEF_CAVE)) {
			if (player.getInstance() instanceof Hunllef) {
				player.getInstance().dispose();
			}
			player.getPA().spellTeleport(3278, 6050, 0, false);
    			player.getItems().addItem(23776, 1);
          }
			player.hunllefDead = false;	
	}
	public static void start(Player c) {
		c.getPA().closeAllWindows();
		Hunllef instance = new Hunllef(c);
		Consumer<Player> start = (player) -> {
			c.resetDamageTaken();
			c.getItems().deleteItem(23951, 1);
			c.setInstance(instance);
			c.getPA().closeAllWindows();
			respawn(instance);
		};

		AgilityHandler.delayFade(c, "CRAWL", 1172, 9943, instance.height, "You crawl into the cave.",
				"and end up at the Hunllef's lair", 1, start);
	}

	public static void respawn(InstancedArea instance) {
		Hunllef hunllef = (Hunllef) instance;
		hunllef.npc = NPCHandler.spawn(9021, 1170, 9934, hunllef.height, 1, 750, 30, 600,500, true);
		hunllef.npc.setInstance(instance);
	}

	private Player player;
	private int height;
	private NPC npc;

	public Hunllef(final Player player) {
		super(Boundary.HUNLLEF_BOSS_ROOM, player.getIndex() * 4);
		this.player = player;
		this.height = player.getIndex() * 4;
	}

	@Override
	public void tick(Entity entity) {
		if (npc.isDead)
			return;
		if (player != null && player.getInstance() != this) {
			dispose();
		}
	}

	@Override
	public void onDispose() {
		if (npc != null) {
			if (npc.isDead)
				return;
			NPCHandler.deregister(npc);
		}
		if (player != null && player.getInstance() == this) {
			player.setInstance(null);
		}
		player = null;
	}
}
