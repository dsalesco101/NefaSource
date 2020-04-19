package ethos.model.players.skills.slayer;

import java.util.HashMap;
import java.util.Map;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.SkillHandler;
 
/**
 * 
 * @author JasonRTM - 14/06/2014
 *
 */

public class DuoSlayer {
	
        /**
        * All the checks are in here.
        * if you've passed all the checks, the task is assigned to player 2
        * @param c
        */
	public static void handleTask(Player c, int itemId, int playerId) {
		Player usedOn = PlayerHandler.players[playerId];
		if (c.isDead) {
			c.sendMessage("You can't do this");
			return;
		}
		if (usedOn.hasPartner) {
			c.sendMessage("You already have a partner!");
			return;
		}
		if (usedOn.isDead) {
			c.sendMessage("You can't do this!");
			return;
		}
		if (c.isBanking) {
			c.sendMessage("You can't do this whilst banking");
			return;
		}
		if (usedOn.isBanking) {
			c.sendMessage("The other player is banking");
			return;
		}
		if (c.currentTask == null) {
			c.sendMessage("You must have a task which you want to share!");
			return;
		}
		if (!c.getItems().playerHasItem(itemId))
			return;
		
		c.turnPlayerTo(usedOn.getLocation().getX(), usedOn.getLocation().getY());
		c.getDH().sendDialogues(932, c.npcType);
	}
	/**
	* Assigns the task when a gem is usen on player 2
	* @param c
	*/
	public static void assignTask(Player c, int playerId) {
		Player usedOn = PlayerHandler.players[playerId];
		if(c.currentTask != null) {
			c.currentTask = usedOn.currentTask;
			c.sendMessage("You have succesfully shared your slayer task with "+usedOn.playerName);
			usedOn.sendMessage("You have accepted the slayer task with "+c.playerName);
			c.hasPartner = true;
            usedOn.hasPartner = true;
			return;
		} 
	}
}