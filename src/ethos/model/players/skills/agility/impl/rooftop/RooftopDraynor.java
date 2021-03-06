package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.players.Player;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;
import ethos.util.Misc;

/**
 * Rooftop Agility Draynor
 * 
 * @author Robbie
 */

public class RooftopDraynor {

	public static final int ROUGH_WALL = 11404, 
			TIGHTROPE = 11405, TIGHT_ROPE = 11406, 
			NARROW_WALL = 11430, UP_WALL = 11630, 
			GAP = 11631, CRATE = 11632;
	
	public static int[] DRAYNOR_OBJECTS = { ROUGH_WALL, TIGHTROPE, TIGHT_ROPE, NARROW_WALL, UP_WALL, GAP, CRATE };

	public boolean execute(final Player c, final int objectId) {
		
		for (int id : DRAYNOR_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "DRAYNOR");
			}
			c.runEnergy += Misc.random(3) + 2;

		}
		
		switch (objectId) {
		case ROUGH_WALL:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 3102, 3279, 3, 2);
			c.getAgilityHandler().agilityProgress[0] = true;
		return true;
			
		case TIGHTROPE:
			if (AgilityHandler.failObstacle(c, 3094, 3274, 0)) {
		return false;
			}
			c.setForceMovement(3090, 3277, 0, 200, "WEST", 762);
			c.getAgilityHandler().agilityProgress[1] = true;
		return true;
			
		case TIGHT_ROPE:
			 	c.getPA().movePlayer(3092, 3276, c.heightLevel);
				c.setForceMovement(3092, 3266, 0, 225, "SOUTH", 762);
				c.getAgilityHandler().agilityProgress[2] = true;
		return true;
			
		case NARROW_WALL:
			c.setForceMovement(3088, 3261, 0, 170, "SOUTH", 762);
			c.getAgilityHandler().agilityProgress[3] = true;
		return true;
			
		case UP_WALL:
			if (c.getAgilityHandler().agilityProgress[3] == true) {
				AgilityHandler.delayEmote(c, "JUMP", c.absX, 3255, 3, 2);
				c.getAgilityHandler().agilityProgress[4] = true;
			} else {
				c.appendDamage(1, Hitmark.HIT);
				c.sendMessage("Apperantly I skipped a gap, ouch..");
			}
		return true;
			
		case GAP:
			AgilityHandler.delayEmote(c, "JUMP", 3096, 3256, 3, 2);
			c.getAgilityHandler().agilityProgress[5] = true;
		return true;
			
		case CRATE:
			c.getAgilityHandler().roofTopFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 120 : 2000, 8000);
			AgilityHandler.delayEmote(c, "JUMP", 3103, 3261, 0, 2);
			 Achievements.increase(c, AchievementType.AGIL, 1);
			 int graceEffect = 1 + Misc.random(20);
				if (c.wearingGrace() && graceEffect > 18) {
					c.getItems().addItemUnderAnyCircumstance(11849, 1);
				}
				int marks = 1 + Misc.random(3);
				c.getItems().addItemUnderAnyCircumstance(11849, marks);
		return true;
		}
		return false;
	}

}
