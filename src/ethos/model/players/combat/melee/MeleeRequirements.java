package ethos.model.players.combat.melee;

import ethos.Config;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class MeleeRequirements {

	public static int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return (combat1 - combat2);
		}
		if (combat2 > combat1) {
			return (combat2 - combat1);
		}
		return 0;
	}

	public static boolean checkReqs(Player c) {
		if (PlayerHandler.players[c.playerAttackingIndex] == null) {
			return false;
		}
		if (c.playerAttackingIndex == c.getIndex())
			return false;
		if (c.inPits && PlayerHandler.players[c.playerAttackingIndex].inPits)
			return true;
		if (PlayerHandler.players[c.playerAttackingIndex].inDuelArena()) {
			if (!Boundary.isIn(c, Boundary.DUEL_ARENA)) {
				if (!Config.NEW_DUEL_ARENA_ACTIVE) {
					c.getDH().sendStatement("@red@Dueling Temporarily Disabled", "The duel arena minigame is currently being rewritten.",
							"No player has access to this minigame during this time.", "", "Thank you for your patience, Developer J.");
					c.nextChat = -1;
					return false;
				}
				Player other = PlayerHandler.players[c.playerAttackingIndex];
				if (c.getDuel().requestable(other)) {
					c.getDuel().request(other);
				}
				c.getCombat().resetPlayerAttack();
				return false;
			}
			return true;
		}
		// if(Dicing.inDiceArea(c)) {
		// Dicing.setUpDice(c, c.playerAttackingIndex);
		// return false;
		// }
		/**
		 * Throwing snowballs
		 */
		if (c.getItems().isWearingItem(10501, 3)) {
			if (System.currentTimeMillis() - c.getLastContainerSearch() < 2000) {
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return false;
			}
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			c.setLastContainerSearch(System.currentTimeMillis());
			return false;
		}
		if (!PlayerHandler.players[c.playerAttackingIndex].inWild() && !c.getItems().isWearingItem(10501, 3) && !TourneyManager.getSingleton().isInArena(c)) {
			//System.out.println("ID: " + c.spellId +", Old id: " + c.oldSpellId + ", Mage? " + c.usingMagic);
			c.sendMessage("That player is not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!c.inWild() && !c.getItems().isWearingItem(10501, 3) && !TourneyManager.getSingleton().isInArena(c)) {
			c.sendMessage("You are not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}

		if (Config.COMBAT_LEVEL_DIFFERENCE && !c.getItems().isWearingItem(10501, 3)) {
			if (c.inWild()) {
				int combatDif1 = getCombatDifference(c.combatLevel, PlayerHandler.players[c.playerAttackingIndex].combatLevel);
				if ((combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[c.playerAttackingIndex].wildLevel)) {
					c.sendMessage("Your combat level difference is too great to attack that player here.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			} else {
				int myCB = c.combatLevel;
				int pCB = PlayerHandler.players[c.playerAttackingIndex].combatLevel;
				if ((myCB > pCB + 12) || (myCB < pCB - 12)) {
					c.sendMessage("You can only fight players in your combat range!");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}

		if (Config.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[c.playerAttackingIndex].inMulti()) { // single combat zones
				if (PlayerHandler.players[c.playerAttackingIndex].underAttackByPlayer != c.getIndex()
						&& PlayerHandler.players[c.playerAttackingIndex].underAttackByPlayer != 0
						&& !c.getBH().isTarget(c.playerAttackingIndex)) {
					c.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
				if (PlayerHandler.players[c.playerAttackingIndex].getIndex() != c.underAttackByPlayer && c.underAttackByPlayer != 0
						|| c.underAttackByNpc > 0) {
					c.sendMessage("You are already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public static int getRequiredDistance(Player c) {
		if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving)
			return 2;
		else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
			return 4;
		} else {
			return 1;
		}
	}
}