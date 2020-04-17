package ethos.model.npcs.bosses.wildypursuit;

import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.monsterhunt.MonsterHunt;

public class Sotetseg {
	
	public static int specialAmount = 0;
	public static final int NPC_ID = 8388;
	
	public static void glodSpecial(Player player) {
		NPC GLOD = NPCHandler.getNpc(8388);
		
		if (GLOD.isDead) {
			return;
		}
		
		if ((GLOD.getHealth().getCurrentHealth() < 1400 && GLOD.getHealth().getCurrentHealth() >1350) ||
			(GLOD.getHealth().getCurrentHealth() < 800 && GLOD.getHealth().getCurrentHealth() >750)||
			(GLOD.getHealth().getCurrentHealth() < 50 && GLOD.getHealth().getCurrentHealth() >0)) {
				NPCHandler.npcs[GLOD.getIndex()].forceChat("RAARRRRGHHHH!");
				};
			}
		
	
	public static void rewardPlayers(Player player) {
		MonsterHunt.monsterKilled = System.currentTimeMillis();
		MonsterHunt.spawned = false;
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.WILDERNESS))
		.forEach(p -> {
				if (p.getGlodDamageCounter() >= 80) {
					p.sendMessage("@blu@The wildy boss has been killed!");
					p.sendMessage("@blu@You receive a key for doing enough damage to the boss!");

					p.getItems().addItemUnderAnyCircumstance(4185, 1);
					p.getEventCalendar().progress(EventChallenge.OBTAIN_X_WILDY_EVENT_KEYS);
					p.setGlodDamageCounter(0);
				} else {
					p.sendMessage("@blu@You didn't do enough damage to the boss to receive a reward.");
					p.setGlodDamageCounter(0);
				}
				

		});
	}
}
