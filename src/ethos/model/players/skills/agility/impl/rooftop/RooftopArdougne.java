package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;
import ethos.util.Misc;

/**
 * Rooftop Agility Ardougne
 * 
 * @author Matt
 */

public class RooftopArdougne {

	public static final int WOODEN_BEAMS = 15608, JUMP_GAP = 15609, PLANK = 26635, JUMP_2ND_GAP = 15610, JUMP_3RD_GAP = 15611, STEEP_ROOF = 28912, JUMP_4TH_GAP = 15612;
	public static int[] ARDOUGNE_OBJECTS = { WOODEN_BEAMS, JUMP_GAP, PLANK, JUMP_2ND_GAP, JUMP_3RD_GAP, STEEP_ROOF, JUMP_4TH_GAP };

	public static boolean execute(final Player c, final int objectId) {
		for (int id : ARDOUGNE_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "ARDOUGNE");
			}
			c.runEnergy += Misc.random(3) + 2;
		}
		
		switch (objectId) {
		case WOODEN_BEAMS:
			c.startAnimation(737);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int ticks = 0;
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						onStopped();
						return;
					}
					switch (ticks++) {
					case 1:
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2673, 3298, 1, 1);
						break;
						
					case 3:
						c.startAnimation(737);
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2673, 3298, 2, 1);
						break;
						
					case 5:
						c.startAnimation(737);
						AgilityHandler.delayEmote(c, "CLIMP_UP_WALL", 2671, 3299, 3, 2);
						c.getAgilityHandler().agilityProgress[0] = true;
						container.stop();
						break;
					}
				}

				@Override
				public void onStopped() {

				}
			}, 1);
			return true;
			
		case JUMP_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 2667, 3311, 1, 1);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int ticks = 0;
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						onStopped();
						return;
					}
					switch (ticks++) {
					case 3:
						AgilityHandler.delayEmote(c, "JUMP", 2665, 3314, 1, 1);
						break;
						
					case 7:
						AgilityHandler.delayEmote(c, "JUMP", 2665, 3318, 3, 1);
						c.getAgilityHandler().agilityProgress[1] = true;
						container.stop();
						break;
					}
				}

				@Override
				public void onStopped() {

				}
			}, 1);
			return true;
			
		case PLANK:
			c.setForceMovement(2656, 3318, 0, 200, "WEST", 762);
			c.getAgilityHandler().agilityProgress[2] = true;
			return true;
			
		case JUMP_2ND_GAP:
			AgilityHandler.delayEmote(c, "JUMP", 2653, 3314, 3, 2);
			c.getAgilityHandler().agilityProgress[3] = true;
			return true;
			
		case JUMP_3RD_GAP:
			AgilityHandler.delayEmote(c, "JUMP_DOWN", 2651, 3309, 3, 2);
			c.getAgilityHandler().agilityProgress[4] = true;
			return true;
			
		case STEEP_ROOF:
			c.setForceMovement(2656, 3297, 0, 200, "SOUTH", 762);
			c.getAgilityHandler().agilityProgress[5] = true;
			return true;
			
		case JUMP_4TH_GAP:
			AgilityHandler.delayEmote(c, "JUMP_DOWN", 2658, 3298, 1, 2);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.disconnected) {
						onStopped();
						return;
					}
					if (c.absX == 2658 && c.absY == 3298) {
						c.setForceMovement(2661, 3298, 0, 80, "EAST", 0x333);
					} else if (c.absX == 2661 && c.absY == 3298) {
						c.setForceMovement(2663, 3297, 0, 80, "EAST", 3067);
					} else if (c.absX == 2663 && c.absY == 3297) {
						c.setForceMovement(2666, 3297, 0, 80, "EAST", 0x333);
					} else if (c.absX == 2666 && c.absY == 3297) {
						AgilityHandler.delayEmote(c, "JUMP_DOWN", 2668, 3297, 0, 1);
						c.getAgilityHandler().roofTopFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 793 : 60000, 4000);
						c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.ARDOUGNE_ROOFTOP);
						 Achievements.increase(c, AchievementType.AGIL, 1);
								int graceEffect = 1 + Misc.random(20);
								if (c.wearingGrace() && graceEffect > 18) {
									c.getItems().addItemUnderAnyCircumstance(11849, 1);
								}
								int marks = 1 + Misc.random(3);
								c.getItems().addItemUnderAnyCircumstance(11849, marks);
						container.stop();
					}
				}
				@Override
				public void onStopped() {

				}
			}, 1);
			return true;
		}
		return false;
	}
}
