package ethos.model.content;

import java.util.concurrent.TimeUnit;

import ethos.model.content.bonus.DoubleExperience;
import ethos.model.players.Player;


/** 
 * @author Aaron Whittle
 * @date Sep 22th 2019
 */

public class DoubleExpScroll {

	/**
	 * Convert the amount of hours to milliseconds, divide by 600 to get the tick amount.
	 */
	private static final long TIME = TimeUnit.HOURS.toMillis(1) / 600;

	public static void openScroll(Player player) {
		if(DoubleExperience.isDoubleExperience()){
			player.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, use it another day!");
			return;
		} else if (player.xpScroll) {
			player.sendMessage("You already have the xp bonus.");
			return;
		}

		player.xpScroll = true;
		player.xpScrollTicks = TIME;
	}
	

}
	
