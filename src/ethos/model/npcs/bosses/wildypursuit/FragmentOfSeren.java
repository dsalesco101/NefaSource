package ethos.model.npcs.bosses.wildypursuit;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ethos.clip.PathChecker;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.PathFinder;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/18/19
 * Keep in mind this is built ontop a poor system and architecture. Did the best with what I was given.
 */
public class FragmentOfSeren {
	
	/**
	 * Variables
	 */
	public static final int FRAGMENT_ID = 8920;
	public static final int NPC_ID = 8918;
	public static final int CRYSTAL_WHIRLWIND = 8921;

	//Can Seren be attacked or not
	public static boolean isAttackable = false;
	
	public static final int SPAWN_ANIMATION = 8370;
	public static final int TOUCHED_ANIMATED = 8371;
	public static final int DEATH_ANIMATION = 8373;
	public static final int SPAWN_MINIONS_ANIMATION = 8380;
	public static final int MULTI_ATTACK_ANIMATION = 8376;
	public static final int CRYSTAL_DEATH_ANIMATION = 8424;
	
	//Three active crystals spawned in to give health
	public static final ArrayList<NPC> activePillars = new ArrayList<>();
	
	public static int specialAmount = 0;
	
	public static NPC currentSeren;
	
	/**
	 * Handles Seren spawning in the crystals
	 * @param player
	 */
	public static void handleSpecialAttack(Player player) {
		int[][] coords = {{2, 0}, {-2, 0}, {0, 2}};
		
		if (currentSeren.isDead) {
			return;
		}
		
		if (currentSeren.getHealth().getCurrentHealth() <= 600 && specialAmount == 0) {
			currentSeren.forceChat("I will ruin you all!");
			currentSeren.startAnimation(8380);
			currentSeren.underAttackBy = -1;
			currentSeren.underAttack = false;
			isAttackable = false;
			NPCHandler.queenAttack = "SPECIAL";
			specialAmount++;
			activePillars.clear();
			for(int i = 0; i < 3; i++) {
				if (!PathFinder.getPathFinder().accessable(player, currentSeren.getX() + coords[i][0], currentSeren.getY() + coords[i][1])) {
					activePillars.add(NPCHandler.spawnNpc(CRYSTAL_WHIRLWIND, currentSeren.getX(), currentSeren.getY(), currentSeren.getHeight(), 0, 200, 0, 0, 200));
				} else {
					activePillars.add(NPCHandler.spawnNpc(CRYSTAL_WHIRLWIND, currentSeren.getX() + coords[i][0], currentSeren.getY() + coords[i][1], currentSeren.getHeight(), 0, 200, 0, 0, 200));
				}
			}
			createWhirlWindEvent();
		}
	}
	
	/**
	 * Handles the on-going event of healing Seren until all crystals are dead
	 */
	public static void createWhirlWindEvent() {
		CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				NPC seren = NPCHandler.getNpc(NPC_ID);

				boolean gaveHealth = false;
				for(NPC pillar : activePillars) {
					if (pillar != null && !pillar.isDead && pillar.getHealth().getCurrentHealth() > 0) {
						if (seren != null) {
							gaveHealth = true;
							seren.getHealth().increase(10);
						}
					}
				}

				if (gaveHealth) {
					seren.forceChat("Yes... feed me!");
				} else {
					isAttackable = true;
					container.stop();
				}
			}
			
		}, Misc.toCycles(10, TimeUnit.SECONDS));
	}
	
	/**
	 * This was here before, but assuming rewards all fighters within the area
	 * @param player
	 */
	public static void rewardPlayers(Player player) {
		MonsterHunt.monsterKilled = System.currentTimeMillis();
		MonsterHunt.spawned = false;
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.WILDERNESS))
		.forEach(p -> {
				//int reward = (p.getIceQueenDamageCounter() >= 50 ? Misc.random(5) + 3 : 0);
				if (p.getIceQueenDamageCounter() >= 80) {
					p.sendMessage("@blu@The Wildy Boss has been killed!");
					p.sendMessage("@blu@You receive a @red@key@blu@ for doing enough damage to the boss!");
					p.getItems().addItemUnderAnyCircumstance(6792, 1);

					p.getEventCalendar().progress(EventChallenge.OBTAIN_X_WILDY_EVENT_KEYS);
					p.setIceQueenDamageCounter(0);
				} else {
					p.sendMessage("@blu@You didn't do enough damage to the boss to receive a reward.");
					p.setIceQueenDamageCounter(0);
				}

		});
	}
	
	public static void removePillar(NPC npc) {
		int removeIndex = -1;
		for(int i = 0; i < activePillars.size(); i++) {
			if (activePillars.get(i).absX == npc.absX && activePillars.get(i).absY == npc.absY) {
				removeIndex = i;
				break;
			}
		}
		if (removeIndex != -1) {
			activePillars.remove(removeIndex);
		}
	}
}
