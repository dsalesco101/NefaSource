package ethos.model.players.skills.agility;

import ethos.Server;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * Mark of grace
 * 
 * @author Matt
 *
 */
public class MarkOfGrace {

	public static void spawnMarks(Player player, String location) {
		int chance = 0;
		
		switch (location) {
		case "ARDOUGNE":
			int ardougne = 
						  player.getRechargeItems().hasItem(13121) ? 20
						: player.getRechargeItems().hasItem(13122) ? 23
						: player.getRechargeItems().hasItem(13123) ? 25
						: player.getRechargeItems().hasItem(13124) || player.getRechargeItems().hasItem(20760) ? 30 : 17;
			chance = player.playerLevel[player.playerAgility] / ardougne;
			break;
			
		case "SEERS":
			int seers = player.getRechargeItems().hasItem(13137) ? 20 
					  : player.getRechargeItems().hasItem(13138) ? 23
					  : player.getRechargeItems().hasItem(13139) ? 25
					  : player.getRechargeItems().hasItem(13140) ? 30 : 17;
			chance = player.playerLevel[player.playerAgility] / seers;
			break;
			
		case "VARROCK":
			int varrock = player.getRechargeItems().hasItem(13104) ? 20 
					 	: player.getRechargeItems().hasItem(13105) ? 23
					 	: player.getRechargeItems().hasItem(13106) ? 25
					 	: player.getRechargeItems().hasItem(13107) ? 30 : 17;
			chance = player.playerLevel[player.playerAgility] / varrock;
			break;
		case "CANAFIS":
			int canafis = player.getRechargeItems().hasItem(13104) ? 20 
					 	: player.getRechargeItems().hasItem(13105) ? 23
					 	: player.getRechargeItems().hasItem(13106) ? 25
					 	: player.getRechargeItems().hasItem(13107) ? 30 : 17;
			chance = player.playerLevel[player.playerAgility] / canafis;
			break;
		}
	}

}
