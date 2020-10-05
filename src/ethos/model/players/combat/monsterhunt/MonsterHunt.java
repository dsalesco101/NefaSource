package ethos.model.players.combat.monsterhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.wildypursuit.FragmentOfSeren;
import ethos.model.npcs.bosses.wildypursuit.Sotetseg;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * MonsterHunt.java
 * 
 * @author Jashy
 *
 * Re-written to make it cleaner - @Emre
 * 
 * A class to spawn NPC's at different locations in the wilderness.
 *
 */

public class MonsterHunt {

	public enum Npcs {
               //NAME(NPC ID, "WHAT YOU WANT NPC TO BE CALLED" ,HP ,MAXHIT, ATTACK, DEFENCE);
		FRAGMENT_OF_SEREN(FragmentOfSeren.FRAGMENT_ID, "Seren", 2000, 30, 250, 400),
		SOTETSEG(Sotetseg.NPC_ID, "Sotetseg", 2000, 55, 400, 900);
		
		private final int npcId;

		private final String monsterName;

		private final int hp;

		private final int maxHit;

		private final int attack;

		private final int defence;

		private Npcs(final int npcId, final String monsterName, final int hp, final int maxHit, final int attack, final int defence) {
			this.npcId = npcId;
			this.monsterName = monsterName;
			this.hp = hp;
			this.maxHit = maxHit;
			this.attack = attack;
			this.defence = defence;
		}

		public int getNpcId() {
			return npcId;
		}

		public String getMonsterName() {
			return monsterName;
		}

		public int getHp() {
			return hp;
		}

		public int getMaxHit() {
			return maxHit;
		}

		public int getAttack() {
			return attack;
		}

		public int getDefence() {
			return defence;
		}
	}

	/**
	 * The spawnNPC method which handles the spawning of the NPC and the global
	 * message sent.
	 * 
	 * @param c
	 */

	public static boolean spawned;
	
	private static int npcType;
	
	public static long monsterKilled = System.currentTimeMillis();
	
	private static MonsterHuntLocation[] locations = new MonsterHuntLocation[]{
			new MonsterHuntLocation(3258, 3878, "Demonic Ruins @red@(45)"),
			new MonsterHuntLocation(3233, 3638, "Chaos Altar @red@(14)"), 
			new MonsterHuntLocation(3199, 3887, "Lava Dragons @red@(46)"), 
			new MonsterHuntLocation(3307, 3933, "Demonic Ruins @red@(45)"), 
			new MonsterHuntLocation(3306, 3668, "Hill Giants @red@(19)")};

	private static MonsterHuntLocation currentLocation;
			
	private static String name;
	
	private static boolean isSeren = true;
	
	public static void spawnNPC() {
		CycleEventHandler.getSingleton().addEvent(spawned, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if(spawned) {
					if (npcType == FragmentOfSeren.FRAGMENT_ID) {
						if (!FragmentOfSeren.activePillars.isEmpty()) {
							for(NPC pillar : FragmentOfSeren.activePillars) {
								if (pillar != null) {
									NPCHandler.kill(pillar.npcType, 0);
									//NPCHandler.deregister(pillar);
								}
							}
							FragmentOfSeren.activePillars.clear();
						}
						FragmentOfSeren.currentSeren.isDead = true;
						//NPCHandler.deregister(NPCHandler.getNpc(FragmentOfSeren.NPC_ID));
					}
					NPCHandler.kill(npcType, 0);
					//NPCHandler.deregister(NPCHandler.getNpc(npcType));
					spawned = false;
					currentLocation = null;
					monsterKilled = System.currentTimeMillis();
					return;
				}
				List<MonsterHuntLocation> locationsList = Arrays.asList(locations);
				MonsterHuntLocation randomLocation = Misc.randomTypeOfList(locationsList);
				currentLocation = randomLocation;
				List<Npcs> npcs = new ArrayList<>(EnumSet.allOf(Npcs.class));
				Npcs randomNpc = isSeren ? Npcs.FRAGMENT_OF_SEREN : Npcs.SOTETSEG;
				isSeren = !isSeren;
				name = randomNpc.getMonsterName();
				npcType = randomNpc.getNpcId();
				if (npcType == FragmentOfSeren.FRAGMENT_ID) {
					FragmentOfSeren.currentSeren = NPCHandler.spawnNpc(randomNpc.getNpcId(), randomLocation.getX(), randomLocation.getY(), 0, 1, randomNpc.getHp(), randomNpc.getMaxHit(), randomNpc.getAttack(), randomNpc.getDefence()/*, false*/);
				} else {
					NPCHandler.spawnNpc(randomNpc.getNpcId(), randomLocation.getX(), randomLocation.getY(), 0, 1, randomNpc.getHp(), randomNpc.getMaxHit(), randomNpc.getAttack(), randomNpc.getDefence()/*, false*/);
				}
				PlayerHandler.executeGlobalMessage("@red@" + randomNpc.getMonsterName() + " has spawned near " + randomLocation.getLocationName() + ", use @bla@::wildyevent@red@ to teleport there.");
				spawned = true;
			}
		}, Misc.toCycles(40, TimeUnit.MINUTES));
	}

	public static MonsterHuntLocation getCurrentLocation() {
		return currentLocation;
	}

	public static void setCurrentLocation(MonsterHuntLocation currentLocation) {
		MonsterHunt.currentLocation = currentLocation;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		MonsterHunt.name = name;
	}
	
	public static String getTimeLeft() {
		if (spawned) {
			return "Wildy Event: @gre@" + (isSeren ? "Sotetseg" : "Seren");
		}
		
		long timeLeft = System.currentTimeMillis() - monsterKilled;
		int minutesPassed = (int) (timeLeft / (1000 * 60));
		return "Wildy Event: @red@" + (60 - minutesPassed) + " minutes";
	}
}