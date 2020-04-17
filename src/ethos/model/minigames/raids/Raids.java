package ethos.model.minigames.raids;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.clip.doors.Location;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Player;
import ethos.model.players.Position;
import ethos.util.Misc;
import ethos.Config;
import ethos.Server;
import ethos.world.objects.GlobalObject;
import lombok.Getter;

/**
 * Author @ Goon_
 * www.rune-server.com
 */

public class Raids {
	public static int COMMON_KEY = 3456;
	public static int RARE_KEY = 3464;

	@Getter
	private long lastActivity = -1;
	private Map<String, Long> playerLeftAt = Maps.newConcurrentMap();
	private Map<String, Integer> raidPlayers = Maps.newConcurrentMap();
	private Map<String, Integer> activeRoom = Maps.newConcurrentMap();
	
	private int groupPoints;

	public void filterPlayers() {
		raidPlayers.entrySet()
		.stream()
		.filter(entry -> !PlayerHandler.getOptionalPlayer(entry.getKey()).isPresent())
		.forEach(entry -> raidPlayers.remove(entry.getKey()));
	}
	
	public void removePlayer(Player player) {
		//player.setInstance(null);
		raidPlayers.remove(player.getName().toLowerCase());
		groupPoints = raidPlayers.entrySet().stream().mapToInt((val) -> val.getValue()).sum();
		if(raidPlayers.isEmpty()) {
			lastActivity = System.currentTimeMillis();
		}
	}
	
	public List<Player> getPlayers(){
		List<Player> activePlayers = Lists.newArrayList();
		filterPlayers();
		raidPlayers.keySet().stream().forEach(playerName -> {
			PlayerHandler.getOptionalPlayer(playerName).ifPresent(player -> activePlayers.add(player));
		});
		return activePlayers;
	}

	/**
	 * Add points
	 */
	public int addPoints(Player player, int points) {
		if(!raidPlayers.containsKey(player.getName().toLowerCase()))
			return 0;
		int currentPoints = raidPlayers.getOrDefault(player.getName().toLowerCase(), 0);
		raidPlayers.put(player.getName().toLowerCase(), currentPoints + points);
		groupPoints = raidPlayers.entrySet().stream().mapToInt((val) -> val.getValue()).sum();
		return currentPoints + points;
	}
	
	public int getGroupPoints() {
		return groupPoints;
	}


	public int currentHeight;

	/**
	 * The current path
	 */
	private int path;

	/**
	 * The current way
	 */
	private int way;

	/**
	 * Current room
	 */
	public int currentRoom;

	private boolean chestRoomDoorOpen = true;
	private int chestToOpenTheDoor = 5 + Misc.random(20);
	private HashSet<Integer> chestRoomChestsSearched = new HashSet<>();
	
//	/**
//	 * Instance;
//	 */
	//private InstancedArea instance = null;
//
	/**
	 * Monster spawns (No Double Spawning)
	 */
	public boolean lizards = false;
	public boolean vasa = false;
	public boolean vanguard = false;
	public boolean ice = false;
	public boolean chest = false;
	public boolean mystic = false;
	public boolean tekton = false;
	public boolean mutta = false;
	public boolean archers = false;
	public boolean olm = false;
	public boolean olmDead = false;
	public boolean rightHand = false;
	public boolean leftHand = false;
	


	/**
	 * The door location of the current paths
	 */
	private ArrayList<Location> roomPaths= new ArrayList<Location>();

	/**
	 * The names of the current rooms in path
	 */
	private  ArrayList<String> roomNames = new ArrayList<String>();


	/**
	 * Current monsters needed to kill
	 */
	private int mobAmount = 0;


	/**
	 * Gets the start location for the path
	 * @return path
	 */
	public Location getStartLocation() {
		switch(path) {
		case 0:
			return RaidRooms.STARTING_ROOM_2.doorLocation;
		}
		return RaidRooms.STARTING_ROOM.doorLocation;
	}
	
	public Location getOlmWaitLocation() {
		switch(path) {
		case 0:
			return RaidRooms.ENERGY_ROOM.doorLocation;
		}
		return RaidRooms.ENERGY_ROOM_2.doorLocation;
	}
	/**
	 * Handles raid rooms
	 * @author Goon
	 *
	 */
	public enum RaidRooms{
		STARTING_ROOM("start_room",1,0,new Location(3299,5189)),
		LIZARDMEN_SHAMANS("lizardmen",1,0,new Location(3308,5208)),
		SKELETAL_MYSTIC("skeletal",1,0,new Location(3312,5217,1)),
		VASA_NISTIRIO("vasa",1,0,new Location(3312,5279)),
		VANGUARDS("vanguard",1,0,new Location(3312,5311)),
		ICE_DEMON("ice",1,0,new Location(3313,5346)),
		CHEST_ROOM("chest",1,0,new Location(3311,5374)),
		//SCAVENGER_ROOM_2("scavenger",1,new Location(3343,5217,1)),
		//ARCHERS_AND_MAGERS("archer",1,0,new Location(3309,5340,1)),
		MUTTADILE("muttadile",1,0,new Location(3311,5309,1)),
		TEKTON("tekton",1,0,new Location(3310,5277,1)),
		ENERGY_ROOM("energy",1,0,new Location(3275,5159)),
		OLM_ROOM_WAIT("olm_wait",1,0,new Location(3232,5721)),
		OLM_ROOM("olm",1,0,new Location(3232,5730)),

		STARTING_ROOM_2("start_room",1,1,new Location(3299,5189)),
		MUTTADILE_2("muttadile",1,1,new Location(3311,5309,1)),
		VASA_NISTIRIO_2("vasa",1,1,new Location(3312,5279)),
		VANGUARDS_2("vanguard",1,1,new Location(3312,5311)),
		ICE_DEMON_2("ice",1,1,new Location(3313,5346)),
		//ARCHERS_AND_MAGERS_2("archer",1,1,new Location(3309,5340,1)),
		CHEST_ROOM_2("chest",1,1,new Location(3311,5374)),
		//SCAVENGER_ROOM_2("scavenger",1,new Location(3343,5217,1)),
		SKELETAL_MYSTIC_2("skeletal",1,1,new Location(3312,5217,1)),
		TEKTON_2("tekton",1,1,new Location(3310,5277,1)),
		LIZARDMEN_SHAMANS_2("lizardmen",1,1,new Location(3308,5208)),
		ENERGY_ROOM_2("energy",1,1,new Location(3275,5159)),
		OLM_ROOM_WAIT_2("olm_wait",1,1,new Location(3232,5721)),
		OLM_ROOM_2("olm",1,1,new Location(3232,5730));

		private Location doorLocation;
		private int path;
		private int way;
		private String roomName;

		private RaidRooms(String name,int path1,int way1,Location door) {
			doorLocation=door;
			roomName=name;
			path=path1;
			way=way1;

		}

		public Location getDoor() {
			return doorLocation;
		}

		public int getPath() {
			return path;
		}
		public int getWay() {
			return way;
		}
		public String getRoomName() {
			return roomName;
		}


	}

	/**
	 * Starts the raid.
	 */
	public void startRaid(List<Player> players) {//Initializes the raid

		currentHeight = RaidConstants.currentRaidHeight;
		RaidConstants.currentRaidHeight += 4;
		
		//instance = new InstancedArea(Boundary.FULL_RAIDS, this.currentHeight) {
		//	@Override
		//	public void onDispose() {
		//	}
		//};
		
		path = 1;
		way= Misc.random(1);
		for(RaidRooms room : RaidRooms.values()) {
			if(room.getWay() == way) {
				roomNames.add(room.getRoomName());
				roomPaths.add(room.getDoor());
			}
		}
		for (Player lobbyPlayer : players) {//gets all players in lobby
			
			if(lobbyPlayer == null)
				continue;
			if(!lobbyPlayer.inRaidLobby()) {
				lobbyPlayer.sendMessage("You were not in the lobby you have been removed from the raid queue.");
				continue;
			}
			raidPlayers.put(lobbyPlayer.getName().toLowerCase(), 0);
			activeRoom.put(lobbyPlayer.getName().toLowerCase(), 0);
			lobbyPlayer.setRaidsInstance(this);
			//lobbyPlayer.setInstance(instance);
			
			lobbyPlayer.getPA().movePlayer(getStartLocation().getX(),getStartLocation().getY(), currentHeight);
			lobbyPlayer.sendMessage("@red@The raid has now started! Good Luck! type ::leaveraid to leave!");
			lobbyPlayer.sendMessage("[TEMP] @blu@If you get stuck in a wall, type ::stuckraids to be sent back to room 1!");


		}
		RaidConstants.raidGames.add(this);
	}
	


	public boolean hadPlayer(Player player) {
		long leftAt = playerLeftAt.getOrDefault(player.getName().toLowerCase(), (long) -1);
		return leftAt > 0;
	}
	
	public boolean login(Player player) {
		long leftAt = playerLeftAt.getOrDefault(player.getName().toLowerCase(), (long) -1);
		if(leftAt > 0) {
			playerLeftAt.remove(player.getName().toLowerCase());
			if(System.currentTimeMillis() - leftAt <= 60000) {
				raidPlayers.put(player.getName().toLowerCase(), 0);
				player.setRaidsInstance(this);
				player.sendMessage("@red@You rejoin the raid!");
				lastActivity = -1;
				return true;
			}
		}
		
		return false;
	}

	public void logout(Player player) {
		player.setRaidsInstance(null);
		removePlayer(player);
		playerLeftAt.put(player.getName().toLowerCase(), System.currentTimeMillis());
	}
	
	public void resetOlmRoom(Player player) {
		this.activeRoom.put(player.getName().toLowerCase(), 9);
	}
	
	public void resetRoom(Player player) {
		this.activeRoom.put(player.getName().toLowerCase(), 0);
	}

	/**
	 * Kill all spawns for the raid leader if left
	 * @param player
	 */
	public void killAllSpawns() {
		NPCHandler.kill(currentHeight, currentHeight + 3, 
					394, 3341, 7563, 7566, 7585, 7560,
					7544, 7573, 7604, 7606, 7605, 7559,
					7527, 7528, 7529, 7553, 7554, 7555
				);
	}

	/**
	 * Leaves the raid.
	 * @param player
	 */
	public void leaveGame(Player player) {
		if (System.currentTimeMillis() - player.infernoLeaveTimer < 15000) {
			player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
			return;
		}
		player.sendMessage("@red@You have left the Chambers of Xeric.");
		player.getPA().movePlayer(3034, 6067, 0);
		player.setRaidsInstance(null);
		//player.setInstance(null);
		removePlayer(player);
		player.specRestore = 120;
		player.specAmount = 10.0;
		player.setRunEnergy(100);
		player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
		player.getPA().refreshSkill(Config.PRAYER);
		player.getHealth().removeAllStatuses();
		player.getHealth().reset();
		player.getPA().refreshSkill(5);
	}

	/**
	 * Handles giving the raid reward
	 */
	public void giveReward(Player player) {

		int chance = Misc.random(1000);
		if (chance >= 0 && chance < 975) {
			player.getItems().addItemUnderAnyCircumstance(COMMON_KEY, 1);
			player.getEventCalendar().progress(EventChallenge.COMPLETE_X_RAIDS);
			player.sendMessage("@red@You have just recieved a @bla@Common Key." );
		} else if (chance >= 975) {
			player.getItems().addItemUnderAnyCircumstance(RARE_KEY, 1);
			player.getEventCalendar().progress(EventChallenge.COMPLETE_X_RAIDS);
			player.sendMessage("@red@You have just recieved a @pur@Rare Key." );
			PlayerHandler.executeGlobalMessage("@bla@[@blu@RAIDS@bla@] " + player.playerName + "@pur@ has just received a @bla@Rare Raids Key!");
		}
		
		if (player.raidCount == 25) {
			player.getItems().addItemUnderAnyCircumstance(22388, 1);
			PlayerHandler.executeGlobalMessage("@blu@[@pur@"+player.playerName + "@blu@] has completed 25 Raids and obtained the Xeric's Guard Cape!");
		}
		if (player.raidCount == 50) {
			player.getItems().addItemUnderAnyCircumstance(22390, 1);
			PlayerHandler.executeGlobalMessage("@blu@[@pur@"+player.playerName + "@blu@] has completed 50 Raids and obtained the Xeric's Warrior Cape!");
		}
		if (player.raidCount == 100) {
			player.getItems().addItemUnderAnyCircumstance(22392, 1);
			PlayerHandler.executeGlobalMessage("@blu@[@pur@"+player.playerName + "@blu@] has completed 100 Raids and obtained the Xeric's Sentinel Cape!");
		}
		if (player.raidCount == 250) {
			player.getItems().addItemUnderAnyCircumstance(22394, 1);
			PlayerHandler.executeGlobalMessage("@blu@[@pur@"+player.playerName + "@blu@] has completed 250 Raids and obtained the Xeric's General Cape!");
		}
		if (player.raidCount == 500) {
			player.getItems().addItemUnderAnyCircumstance(22396, 1);
			PlayerHandler.executeGlobalMessage("@blu@[@pur@"+player.playerName + "@blu@] has completed 500 Raids and obtained the Xeric's Champions Cape!");

		}


	}

	final int OLM = 7554;
	final int OLM_RIGHT_HAND= 7553;
	final int OLM_LEFT_HAND = 7555;

	public void handleMobDeath(Player killer, int npcType) {

		int height = currentHeight;
		
		mobAmount -= 1;
		switch(npcType) {
		case OLM:
			/*
			 * Crystal & Olm removal after olm's death
			 */
			olmDead = true;
			//idk
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3233, 5751, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3232, 5749, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3232, 5750, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3233, 5749, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3233, 5750, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3233, 5750, currentHeight, 3, 10).setInstance(instance));
//			Server.getGlobalObjects().remove(new GlobalObject(29881, 3220, 5738, currentHeight, 3, 10).setInstance(instance));
			

			getPlayers().stream().forEach(player -> {
				player.getPA().sendPlayerObjectAnimation(player, 3220, 5738, 7348, 10, 3, currentHeight);
				player.sendMessage("@red@Congratulations you have defeated The Great Olm and completed the raid!");
				player.sendMessage("@red@Please go up the stairs beyond the Crystals to get your reward " );
			});
			return;

		case OLM_RIGHT_HAND:
			rightHand = true;
			if(leftHand == true) {
				getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable."));
				Server.getGlobalObjects().add(new GlobalObject(29888, 3220, 5733, currentHeight, 3, 10));
			}else {
				getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!"));
			}
			//Server.getGlobalObjects().remove(new GlobalObject(29887, 3220, 5733, currentHeight, 3, 10).setInstance(instance));

			//Server.getGlobalObjects().add(new GlobalObject(29888, 3220, 5733, currentHeight, 3, 10).setInstance(instance));
			getPlayers().stream()
			.forEach(otherPlr -> {
				otherPlr.getPA().sendPlayerObjectAnimation(otherPlr, 3220, 5733, 7352, 10, 3, currentHeight);
				if(leftHand) {
					otherPlr.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable.");
				} else {
					otherPlr.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!");
				}
			});
		
			return;
		case OLM_LEFT_HAND:
			leftHand = true;
			Server.getGlobalObjects().remove(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
			Server.getGlobalObjects().add(new GlobalObject(29885, 3220, 5743, currentHeight, 3, 10));
			getPlayers().stream()
			.forEach(otherPlr -> {
				otherPlr.getPA().sendPlayerObjectAnimation(otherPlr, 3220, 5743, 7360, 10, 3, currentHeight);
				if(rightHand) {
					otherPlr.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable.");
				} else {
					otherPlr.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!");
				}
			
			});
			if(rightHand == true) {
				Server.getGlobalObjects().remove(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
				Server.getGlobalObjects().add(new GlobalObject(29885, 3220, 5743, currentHeight, 3, 10));
				getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable."));
			}else {
				getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!"));
			}
			return;
		}
		if(killer != null) {
			int randomPoints = Misc.random(500);
			int newPoints = addPoints(killer, randomPoints);
		
			killer.sendMessage("@red@You receive "+ randomPoints +" points from killing this monster.");
			killer.sendMessage("@red@You now have "+ newPoints +" points.");
		}
		if(mobAmount <= 0) {
			getPlayers().stream().forEach(player ->	player.sendMessage("@red@The room has been cleared and you are free to pass."));
			roomSpawned = false;
		}else {
			getPlayers().stream().forEach(player ->	player.sendMessage("@red@There are "+ mobAmount+" enemies remaining."));
		}
	}
	/**
	 * Spawns npc for the current room
	 * @param currentRoom The room
	 */
	public void spawnNpcs(int currentRoom) {

		int height = currentHeight;
		
		switch(roomNames.get(currentRoom)) {
		case "lizardmen":
			if(lizards) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7573, 3274, 5262, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
				NPCHandler.spawn(7573, 3282, 5266, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
				NPCHandler.spawn(7573, 3275, 5269, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7573, 3307,5265, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
				NPCHandler.spawn(7573, 3314,5265, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
				NPCHandler.spawn(7573, 3314,5261, height, 1, 350, 25, 300, 250,true).setRaidsInstance(this);
			}
			lizards = true;
			mobAmount+=3;
			break;
		case "vasa":
			if(vasa) {
				return;
			}
			
			if(path == 0) {
				NPCHandler.spawn(7566, 3280,5295, height, -1, 1000, 25, 250, 350,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7566, 3311,5295, height, -1, 1000, 25, 250, 350,true).setRaidsInstance(this);
			}
			vasa = true;
			mobAmount+=1;
			break;
		case "vanguard":
			if(vanguard) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7527, 3277,5326, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this);// melee vanguard
				NPCHandler.spawn(7528, 3277,5332, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this); // range vanguard
				NPCHandler.spawn(7529, 3285,5329, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this); // magic vanguard
			}else {
				NPCHandler.spawn(7527, 3310,5324, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this); // melee vanguard
				NPCHandler.spawn(7528, 3310,5331, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this); // range vanguard
				NPCHandler.spawn(7529, 3316,5331, height, -1, 400, 25, 140, 300,true).setRaidsInstance(this);// magic vanguard
			}
			vanguard = true;
			mobAmount+=3;
			break;
		case "ice":
			if(ice) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7585, 3273,5365, height, -1, 1250, 45, 350, 400,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7585, 3310,5367, height, -1, 1250, 45, 350, 400,true).setRaidsInstance(this);
			}
			ice = true;
			mobAmount+=1;
			break;
		case "skeletal":
			if(mystic) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7604, 3279,5271, height+1, -1, 550, 25, 400, 300,true).setRaidsInstance(this);
				NPCHandler.spawn(7605, 3290,5268, height+1, -1, 550, 25, 500, 300,true).setRaidsInstance(this);
				NPCHandler.spawn(7606, 3279,5264, height+1, -1, 550, 25, 400, 300,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7604, 3318,5262,height+1, -1, 550, 25, 400, 300,true).setRaidsInstance(this);
				NPCHandler.spawn(7605, 3307,5258, height+1, -1, 550, 25, 500, 300,true).setRaidsInstance(this);
				NPCHandler.spawn(7606, 3301,5262, height+1, -1, 550, 25, 400, 300,true).setRaidsInstance(this);
			}
			mobAmount+=3;
			mystic = true;
			break;
		case "tekton":
			if(tekton) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7544, 3280,5295, height+1, -1, 1500, 45, 450, 450,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7544, 3310, 5293, height+1, -1, 1500, 45, 450, 450,true).setRaidsInstance(this);
			}
			mobAmount+=1;
			tekton = true;
			break;
		case "muttadile":
			if(mutta) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7563, 3276,5331, height + 1, 1, 900, 25, 400, 600,true).setRaidsInstance(this);
			}else {
				NPCHandler.spawn(7563, 3308,5331, height + 1, 1, 900, 25, 400, 600,true).setRaidsInstance(this);
			}
			mobAmount+=1;
			mutta = true;
			break;
		case "archer":
			if(archers) {
				return;
			}
			if(path == 0) {
				NPCHandler.spawn(7559, 3287,5364, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3287,5363, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3285,5363, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3285,5364, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly ranger

				NPCHandler.spawn(7560, 3286,5369, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3284,5369, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3286,5370, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3284,5370, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly mager
			}else {
				NPCHandler.spawn(7559, 3319,5363, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3317,5363, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3317,5364, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly ranger
				NPCHandler.spawn(7559, 3319,5364, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly ranger

				NPCHandler.spawn(7560, 3318,5370, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3318,5369, height + 1, -1, 150, 25, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3316,5369, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly mager
				NPCHandler.spawn(7560, 3316,5370, height + 1, -1, 150, 30, 100, 100,true).setRaidsInstance(this); // deathly mager
			}
			archers = true;
			mobAmount+=8;
			break;
		case "olm":
			if(olm) {
				return;
			}

			// TODO custom region object clipping for instances like this
			Server.getGlobalObjects().add(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
			Server.getGlobalObjects().add(new GlobalObject(29887, 3220, 5733, currentHeight, 3, 10));
			Server.getGlobalObjects().add(new GlobalObject(29881, 3220, 5738, currentHeight, 3, 10));
			getPlayers().stream()
			.forEach(otherPlr -> {
				otherPlr.getPA().sendPlayerObjectAnimation(otherPlr, 3220, 5733, 7350, 10, 3, currentHeight);
				otherPlr.getPA().sendPlayerObjectAnimation(otherPlr, 3220, 5743, 7354, 10, 3, currentHeight);
				otherPlr.getPA().sendPlayerObjectAnimation(otherPlr, 3220, 5738, 7335, 10, 3, currentHeight);
			});
			NPCHandler.spawn(7553, 3223, 5733, height, -1, 500, 33, 272, 272,false).setRaidsInstance(this); // left claw
			NPCHandler.spawn(7554, 3223, 5738, height, -1, 1500, 33, 272, 372,true).setRaidsInstance(this); // olm head
			NPCHandler.spawn(7555, 3223, 5742, height, -1, 500, 33, 272, 272,false).setRaidsInstance(this); // right claw

			olm = true;
			mobAmount+=3;
			break;
			default:
				roomSpawned = false;
				
				break;
		}
		
	}
	/**
	 * Handles object clicking for raid objects
	 * @param player The player
	 * @param objectId The object id
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean handleObjectClick(Player player, int objectId, int x, int y) {
		player.objectDistance = 3;
		switch(objectId) {
			// Searching chest to
			case 29742:
				if (chestRoomDoorOpen) {
					player.sendMessage("The room is already opened, no need for more searching.");
				} else {
					if (chestRoomChestsSearched.contains(Objects.hash(x, y))) {
						player.sendMessage("This chest has already been searched.");
					} else {
						player.startAnimation(6387);
						chestRoomChestsSearched.add(Objects.hash(x, y));
						player.sendMessage("You search the chest..");

						CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								if (chestRoomChestsSearched.size() == chestToOpenTheDoor) {
									player.sendMessage("You find a lever to open the door..");
									player.forcedChat("I found the lever!");
									getPlayers().forEach(plr -> plr.sendMessage("@red@The door has been opened."));
									chestRoomDoorOpen = true;
								} else {
									player.sendMessage("You find nothing.");
									player.startAnimation(65535);
								}

								container.stop();
							}
						}, 2);
					}
				}
				return true;

		case 29789://First entrance
			player.objectDistance = 3;
		case 29734:
			player.objectDistance = 3;
		case 29879:
			if (roomNames.get(getRoomForPlayer(player)).equalsIgnoreCase("chest") && !chestRoomDoorOpen) {
				player.sendMessage("This passage way is blocked, you must search the boxes to find the lever to open it.");
			} else {
				player.objectDistance = 3;
				nextRoom(player);
			}
			return true;
		case 30066:
			player.objectDistance = 3;
			return true;
		case 29777:
			player.objectDistance = 3;
		case 29778:
			player.objectDistance = 3;
			if(!olmDead) {
				if(player.objectX == 3298 && player.objectY == 5185) {
					player.getDH().sendDialogues(10000, -1);
					return true;
				}
				player.sendMessage("You need to complete the raid!");
				return true;
			}
			if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
				return true;
			}
			player.objectDistance = 3;
			player.lastMysteryBox = System.currentTimeMillis();
			player.raidCount+=1;
			giveReward(player);
			DailyTasks.increase(player, PossibleTasks.SKELETAL_MYSTICS_RAID);
			DailyTasks.increase(player, PossibleTasks.TEKTON_RAID);
			player.sendMessage("@red@You receive your reward." );
			player.sendMessage("@red@You have completed "+player.raidCount+" raids." );
			leaveGame(player);
			break;

		case 30028:
			player.objectDistance = 3;
			player.getPA().showInterface(57000);

			return true;
		}
		return false;
	}
	
	private boolean roomSpawned;

	private int getRoomForPlayer(Player player) {
		return activeRoom.getOrDefault(player.getName().toLowerCase(), 0);
	}

	/**
	 * Goes to the next room, Handles spawning etc.
	 */
	public void nextRoom(Player player) {
		player.objectDistance = 3;
		if(activeRoom.getOrDefault(player.getName().toLowerCase(), 0) == currentRoom && mobAmount > 0) {
			player.objectDistance = 3;
			player.sendMessage("You need to defeat the current room before moving on!");
			return;
		}
		if(!roomSpawned) {
			player.objectDistance = 3;
			currentRoom+=1;
			roomSpawned = true;
			spawnNpcs(currentRoom);
		}

		int playerRoom = activeRoom.getOrDefault(player.getName().toLowerCase(), 0) + 1;
		player.getPA().movePlayer(roomPaths.get(playerRoom).getX(),
				roomPaths.get(playerRoom).getY(),
				roomPaths.get(playerRoom).getZ() == 1 ? currentHeight + 1 :currentHeight);
		activeRoom.put(player.getName().toLowerCase(), playerRoom);

	}
	
//	public InstancedArea getInstance() {
//		return instance;
//	}
}

