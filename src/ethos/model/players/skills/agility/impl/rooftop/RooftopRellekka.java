package ethos.model.players.skills.agility.impl.rooftop;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.agility.MarkOfGrace;
import ethos.util.Misc;

/**
 * Rooftop Agility Rellekka
 * 
 * @author Robbie
 */

public class RooftopRellekka {

	public static final int ROUGH_WALL = 14964, GAP1 = 14947, TIGHTROPE = 14987, GAP2 = 14990,
							GAP3 = 14991, TIGHTROPE2 = 14992, FISH = 14994;

	public static int[] RELLEKKA_OBJECTS = { ROUGH_WALL, GAP1, TIGHTROPE, GAP2, GAP3, TIGHTROPE2,
											FISH };

	public boolean execute(final Player c, final int objectId) {
		
		for (int id : RELLEKKA_OBJECTS) {
			if (System.currentTimeMillis() - c.lastObstacleFail < 3000) {
				return false;
			}
			if (c.getAgilityHandler().checkLevel(c, objectId)) {
				return false;
			}
			if (id == objectId) {
				MarkOfGrace.spawnMarks(c, "RELLEKKA");
			}
			c.runEnergy += Misc.random(3) + 2;
		}
		
		switch (objectId) {
		case ROUGH_WALL:
			AgilityHandler.delayEmote(c, "CLIMB_UP", 2626, 3676, 3, 2);
			c.getAgilityHandler().agilityProgress[0] = true;
		return true;
			
		case GAP1:
			if (AgilityHandler.failObstacle(c, 2622, 3670, 0)) {
				return false;
			}
				AgilityHandler.delayEmote(c, "JUMP", 2622, 3668, 3, 2);
				c.getAgilityHandler().agilityProgress[1] = true;
		return true;
			
		case TIGHTROPE:
			c.getPA().movePlayer(2622, 3658, c.heightLevel);
			c.turnPlayerTo(2626, 3654);
			c.setForceMovement(2626, 3654, 0, 250, "SOUTH", 762);
			c.getAgilityHandler().agilityProgress[2] = true;
		return true;
			
		case GAP2:
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                int ticks = 0;
                @Override
                public void execute(CycleEventContainer container) {
                    if (c.disconnected) {
                        onStopped();
                        return;
                    }
                    switch (ticks++) {
                    case 0:
                    	c.startAnimation(3067);
                        AgilityHandler.delayEmote(c, "JUMP", 2629, 3658, 3, 1);
                    break;
                    case 1:
                    	c.turnPlayerTo(2631, 3662);
                    	AgilityHandler.delayEmote(c, "HANG_ON_POST", 2635, 3658, 3, 1);
                    break;
                    case 2:
                        c.setForceMovement(2640, 3653, 0, 250, "SOUTH", 762);
                        c.getAgilityHandler().agilityProgress[3] = true;
                        c.stopAnimation();
                        container.stop();
                    break;
                    }
                }
                @Override
                public void onStopped() {
                }
            }, 3);
		return true;
			
		case GAP3:
			if (AgilityHandler.failObstacle(c, 3048, 3359, 0)) {
				return false;
			}
				AgilityHandler.delayEmote(c, "JUMP", 2643, 3657, 3, 2);
				c.getAgilityHandler().agilityProgress[4] = true;
		return true;
			
		case TIGHTROPE2:
			c.getPA().movePlayer(2647, 3663, c.heightLevel);
			c.setForceMovement(2655, 3671, 0, 350, "NORTH-EAST", 762);
			c.getAgilityHandler().agilityProgress[5] = true;
		return true;
			
		case FISH:
			c.getAgilityHandler().roofTopFinished(c, 5, c.getMode().getType().equals(ModeType.OSRS) ? 780 : 23000, 25000);
			AgilityHandler.delayEmote(c, "JUMP", 2653, 3676, 0, 2);
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
