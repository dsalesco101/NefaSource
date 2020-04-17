package ethos.model.minigames.xeric;

import java.util.ArrayList;
import java.util.List;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * 
 * @author Patrity, Arithium, Fox News
 * 
 */
public class Xeric {

	private int killsRemaining;
	private int xericWaveId;
	public static boolean xericEnabled;

	public Xeric() {
	}
	
	private int index;

	
	private List <Player> xericTeam = new ArrayList<Player>();
	private List<NPC> spawns = new ArrayList<>();
	
	public void spawn() {
		final int[][] type = XericWave.LEVEL;
		if (xericWaveId >= type.length) {
			stop();
			return;
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				
				if (getXericTeam().size() == 0) {
					System.err.println("Size of team is 0.");
					event.stop();
					return;
				}
				if (xericWaveId != 0 && xericWaveId < type.length) {
					for (Player xericTeam : xericTeam) 
						xericTeam.sendMessage("You are now on wave " + (xericWaveId + 1) + " of " + type.length + ". - Total damage done: " + xericTeam.xericDamage + ".", 255);
				}
				
				
				setKillsRemaining(type[xericWaveId].length);
				
				System.out.print("Kills Remaining: " + killsRemaining);
				for (int i = 0; i < killsRemaining; i++) {
					int npcType = type[xericWaveId][i];
				
					/* OLD SPAWNING (SPAWNS ON A RANDOM PLAYER)
					Player p = getXericTeam().get(Misc.random(getXericTeam().size() - 1));
					int x = p.absX + Misc.random(-3, 3);
					int y = p.absY + Misc.random(-3, 3);*/
					
					/*
					 * NEW SPAWNING METHOD (USING SET SPAWN POINTS)
					 */
					int index = Misc.random(XericWave.SPAWN_DATA.length - 1);
					int x = (XericWave.SPAWN_DATA[index][0] + (Misc.random(-3,3)));
					int y = (XericWave.SPAWN_DATA[index][1] + (Misc.random(-3,3)));
					NPC npc = NPCHandler.spawn(npcType, x,y, getIndex() * 4, 1, XericWave.getHp(npcType), XericWave.getMax(npcType), XericWave.getAtk(npcType), XericWave.getDef(npcType), true);
					npc.setNoRespawn(true);
					spawns.add(npc);
				}
				event.stop();
			}

			@Override
			public void onStopped() {

			}
		}, 16);
	}
	
	public void stop() {
		for (Player xericTeam : xericTeam) {
			Player p = PlayerHandler.getPlayer(xericTeam.getName());
			XericRewards.giveReward(p.xericDamage, p);
			p.getPA().movePlayer(3050, 9950, 0);
			p.getDH().sendStatement("Congratulations on finishing the Trials of Xeric!");
			p.nextChat = 0;
			p.setRunEnergy(100);
			killAllSpawns();
			XericLobby.removeGame(this);
		}
	}

	public void leaveGame(Player player, boolean death) {
		player.getPA().movePlayer(3050, 9950, 0);
		player.getDH().sendStatement((death ? "Unfortunately you died" : "You've left the game") + " on wave " + (player.getXeric().xericWaveId + 1) + ". Better luck next time.");
		player.nextChat = 0;
		removePlayer(player);
	}

	public void killAllSpawns() {

		for (NPC npc : getSpawns()) {
			if (npc != null) {
				NPCHandler.npcs[npc.getIndex()] = null;
			}
		}
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (Boundary.isIn(NPCHandler.npcs[i], Boundary.XERIC) && NPCHandler.npcs[i].getHeight() == index) {
					NPCHandler.npcs[i] = null;

				}
			}
		}
	}

	public int getKillsRemaining() {
		return killsRemaining;
	}	
	
	public void setKillsRemaining(int remaining) {
		this.killsRemaining = remaining;
	}

	
	public static void drawInterface(Player p) {//draws the interface for how much time is left in lobby
		int seconds = XericLobby.xericLobbyTimer;
		p.getPA().sendFrame126("Raid begins in: @gre@"+(seconds - (XericLobby.timeLeft %seconds)), 6570);
		p.getPA().sendFrame126("", 6571);
		p.getPA().sendFrame126("", 6572);
		p.getPA().sendFrame126("", 6664);
	}

	public List<Player> getXericTeam() {
		return xericTeam;
	}
	
	public List<NPC> getSpawns() {
		return spawns;
	}

	public void setXericTeam(List<Player> list) {
		this.xericTeam = new ArrayList<>(list);
	}
	public void removePlayer(Player player) {
		
		xericTeam.remove(player);
		if (xericTeam.size() == 0) {
			stop();
		}
		player.setXeric(null);
		
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int z) {
		this.index = z;
	}

	public int getWaveId() {
		return xericWaveId;
	}
	public void incWaveID() {
		xericWaveId ++;
	}

}
