package ethos.model.players.combat.melee;

import ethos.model.items.ItemAssistant;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 *
 * Weapon Specials
 *
 * Class MeleeData
 * 
 * @author 2012
 *
 */

public class MeleeSpecial {

	public static boolean checkSpecAmount(Player c, int weapon) {
		switch (weapon) {
		case 1215:
		case 1231:
		case 1249:
		case 1263:
		case 1305:
		case 1434:
		case 5680:
		case 5698:
		case 5716:
		case 5730:
		case 22613:
		case 22610:
		case 22622:
		case 11824:
		case 11889:
			if (c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 13902:
			if (c.specAmount >= 3.5) {
				c.specAmount -= 3.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 19675:
		case 20368:
		case 20370:
		case 20374:
		case 20372:
		case 11802:
		case 12006:
		case 11806:
		case 12848:
		case 4153:
		case 14484:
		case 12788:
		case 10887:
		case 13263:
		case 20622:
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 6736:
			if (c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 3204:
		case 12926:
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11804:
		case 11838:
		case 21028:
		case 11061:
			if (c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 11785:
			if (c.specAmount > 4) {
				c.specAmount -= 4;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 12809:
			if (c.specAmount > 6.5) {
				c.specAmount -= 6.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 4587:
		case 859:
		case 861:
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
		case 11808:
			if (c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public static void activateSpecial(Player c, int weapon, int i) {
		if (NPCHandler.npcs[i] == null && c.npcAttackingIndex > 0) {
			return;
		}
		if (i > PlayerHandler.players.length - 1) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		c.logoutDelay = System.currentTimeMillis();
		if (c.npcAttackingIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerAttackingIndex > 0) {
			c.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackByPlayer = c.getIndex();
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = c.getIndex();
		}
		if (c.playerAttackingIndex > 0) {
			c.getPA().followPlayer();
		} else if (c.npcAttackingIndex > 0) {
			c.getPA().followNpc();
		}
		switch (weapon) {

		case 12848:
		case 4153: // maul
			if (c.isDead)
				return;
			c.startAnimation(1667);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.gfx100(337);
			break;

		}
		c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
	}
}