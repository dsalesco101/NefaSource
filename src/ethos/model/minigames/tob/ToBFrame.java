package ethos.model.minigames.tob;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ethos.Config;
import ethos.Server;
import ethos.clip.doors.Location;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Equipment;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;
import lombok.Getter;

public class ToBFrame {
	public static void onLogin(Player player) {
		for(ToBFrame theatre : games) {
			if(theatre.isInTeam(player)) {
				player.setTobInstance(theatre);
				return;
			}
		}
		if(Boundary.isIn(player, Boundary.THEATREROOMS)) {
			player.getPA().movePlayerUnconditionally(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
		}
	}
	private boolean isInTeam(Player player) {
		return team.keySet().stream().anyMatch(name -> name.equalsIgnoreCase(player.getName().toLowerCase()));
	}
	private static List<ToBFrame> games = Lists.newArrayList();
	private Map<String, Integer> team = Maps.newConcurrentMap();
	private Map<String, Integer> bossFightLives = Maps.newConcurrentMap();
	@Getter 
	private int index;
	private boolean finished;
	private long startTime;
	private long endTime;
	
	static int gameIndex = 1;
	
	private ToBFrame(List<Player> players) {
		this.index = gameIndex++;
		spawnShops();
		final ToBFrame instance = this;
		for (Player p : players) {
			p.getPA().movePlayer((3218 + Misc.random(0, 3)), (4459 + Misc.random(0, 1)), getHeight());
			p.sendMessage("Welcome to The Theatre of Blood!");
			team.put(p.getName().toLowerCase(), 0);
			CycleEventHandler.getSingleton().addEvent(-1, p, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					p.setTobInstance(instance);
					container.stop();
				}
				
			}, 2);
		}
		stopwatch = Stopwatch.createStarted();
		spawnBoss(1);			
	}
	
	/*
	 * spawns
	 */
	private NPC maiden, bloat, sotetseg, vasilias, xarpus, verzik;
	
	public boolean lootChest;
	public boolean dawnBringer;

	public static void start(List<Player> lobbyPlayers) {

		ToBFrame instance = new ToBFrame(lobbyPlayers);
		addGame(instance);
		instance.spawnShops();
	}

	/**
	 * Constantly checks if the player is in tob, otherwise removes from team.
	 * @param c
	 */
	public void process(Player c) {
		if (!Boundary.isIn(c, Boundary.THEATRE)) {
			removePlayer(c);
			c.getItems().deleteItem(22516, 28);
			if (c.getItems().isWearingItem(22516)) {
				c.getItems().deleteEquipment(-1, Integer.parseInt(Equipment.Slot.WEAPON.toString()));
			}
		}
	}
	
	public List<Player> getPlayers(){
		return team.keySet().stream().map(PlayerHandler::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	public void removePlayer(Player player) {
		this.team.remove(player.getName().toLowerCase());
		player.setTobInstance(null);
		if(this.team.isEmpty()) {
			this.destroyGame();
		} else {
			getPlayers().forEach(plr -> plr.sendMessage("<col=ff0000>" + player.getName() + " has left the party."));
		}
	}
	
	public void destroyGame() {
		Stream.of(maiden, bloat, vasilias, sotetseg, xarpus, verzik).filter(Objects::nonNull).forEach(NPCHandler::destroy);
		
		removeGame(this);
		System.out.println("Destroyed game index " + getIndex() + ".");
	}
	
	public void setupBossLives() {
		bossFightLives.clear();
		getPlayers().stream().forEach(plr -> bossFightLives.put(plr.getName().toLowerCase(), 1));
	}
	
	public boolean canEnterBossRoom(Player player) {
		return bossFightLives.get(player.getName().toLowerCase()) > 0;
	}
	
	public void spawnLoot() {
		if (!lootChest) {
			Server.getGlobalObjects().add(new GlobalObject(32990, 3233, 4319, getHeight(), 0, 10));
			lootChest = true;
		}
	}
	
	
	public void spawnBoss(int boss) {
		switch (boss) {
		case 1: //maiden
			if (maiden == null) {
				maiden = NPCHandler.spawn(8360, 3162, 4444, getHeight(), 0, 3500, 25, 300, 300,true);
				maiden.setTobInstance(this);
			}
			break;
		
		case 2: //bloat
			if (bloat == null) {
				bloat = NPCHandler.spawn(8359, 3299, 4440, getHeight(), 0, 2000, 25, 300, 300,true);
				bloat.setTobInstance(this);
			}
			break;
			
		case 3: //vasilias
			if (vasilias == null) {
				vasilias = NPCHandler.spawn(8355, 3293, 4246, getHeight(), 0, 2500, 25, 300, 300,true);
				vasilias.setTobInstance(this);
			}
			break;
			
		case 4: //sotetseg
			if (sotetseg == null) {
				sotetseg = NPCHandler.spawn(8388, 3278, 4329, getHeight(), 0, 4000, 25, 300, 300,true);
				sotetseg.setTobInstance(this);
			}
			break;
			
		case 5: //xarpus
			if (xarpus == null) {
				xarpus = NPCHandler.spawn(8338, 3169, 4386, getHeight()+1, 0, 2830, 25, 300, 300,true);
				xarpus.setTobInstance(this);
			}
			break;
			
		case 6: //verzik
			if (verzik == null) {
				verzik = NPCHandler.spawn(8369, 3166, 4323, getHeight(), 0, 2000, 25, 300, 300,true);
				verzik.setTobInstance(this);
			}
		}
		
		setupBossLives();
	}
	
	public boolean onDeath(Player player) {
		bossFightLives.put(player.getName().toLowerCase(), 0);
		if(teamDead()) {
			getPlayers().forEach(plr -> plr.getPA().movePlayerUnconditionally(Config.RESPAWN_X, Config.RESPAWN_Y, 0));
			getPlayers().forEach(plr -> plr.theatrePoints = 0);
			getPlayers().forEach(plr -> plr.sendMessage("Unfortunately your team has failed Theatre of Blood!"));
		} else {
			Location deathLoc = getDeathLocation();
			player.getPA().movePlayerUnconditionally(deathLoc.getX(), deathLoc.getY(), getHeight());
		}
		return true;
	}
	
	public Location getDeathLocation() {
		if (maiden != null && !maiden.isDead) 
			return Location.of2(3190, 4446);
		
		if (bloat != null && !bloat.isDead) 
			return Location.of2(3309, 4447);

		if (vasilias != null && !vasilias.isDead)
			return Location.of2(3295, 4262);
		
		if (sotetseg != null && !sotetseg.isDead)
			return Location.of2(3280, 4302);

		
		if (xarpus != null && !xarpus.isDead) 
			return Location.of2(3280, 4293);

		
		if (verzik != null && !verzik.isDead) {
			int[] xPos = {3179, 3177, 3175, 3161, 3159, 3157};
			int yPos = 4325;
			for(int x : xPos) {
				boolean playerInPos = getPlayers().stream().anyMatch(plr -> plr.getLocation().equalsIgnoreHeight(Location.of2(x, yPos)));
				if(!playerInPos) {
					return Location.of2(x, yPos);
				}
			}
			return Location.of2(3179, yPos);
		}
		return Location.of2(0, 0);//TODO
	}
	
	public boolean teamDead() {
		return bossFightLives.values().stream().allMatch(val -> val == 0);
	}
		
	public void spawnShops() {
		Server.getGlobalObjects().add(new GlobalObject(32758, 3175, 4422, getHeight(), 2, 10)); //maiden
		Server.getGlobalObjects().add(new GlobalObject(32758, 3303, 4277, getHeight(), 5, 10)); //Vasilias
		Server.getGlobalObjects().add(new GlobalObject(32758, 3278, 4293, getHeight(), 2, 10)); //sotetseg
		Server.getGlobalObjects().add(new GlobalObject(32758, 3171, 4399, getHeight() + 1, 5, 10)); //xarpus
	}

	private static void addGame(ToBFrame theatre) {
		if(!games.contains(theatre)) {
			games.add(theatre);
		}
	}
	public static void removeGame(ToBFrame theatre) {
		
		if(games.remove(theatre)) {
			//theatre.onFinish();
		}
		
	}
	public void destroyGame(ToBFrame theatre) {
		if (maiden != null) {
			maiden.kill();
		}
		if (bloat != null) {
			bloat.kill();
		}
		if (vasilias != null) {
			vasilias.kill();
		}
		if (sotetseg != null) {
			sotetseg.kill();
		}
		if (xarpus != null) {
			xarpus.kill();
		}
		if (verzik != null) {
			verzik.kill();
		}
		games.remove(theatre);
	}
	
	@Getter
	private Stopwatch stopwatch;
	
	public String getTimeElapsed() {
        long milliseconds = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        return new String(minutes + ":" + seconds);
    }
	
	public void stop() {
		destroyGame(this);
	}

	public boolean maidenDead() {
		return maiden != null && maiden.isDead;
	}
	

	public boolean bloatDead() {
		return bloat != null && bloat.isDead;
	}
	
	public boolean vasiliasDead() {
		return vasilias != null && vasilias.isDead;
	}
	
	public boolean sotetsegDead() {
		return sotetseg != null && sotetseg.isDead;
	}
	
	public boolean xarpusDead() {
		return xarpus != null && xarpus.isDead;
	}
	
	public boolean verzikDead() {
		return verzik != null && verzik.isDead;
	}
	
	public int getHeight() {
		return index * 4;
	}
}
