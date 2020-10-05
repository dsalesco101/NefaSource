package ethos.model.content;

import ethos.model.players.Player;

public class DonatorBenefit {

	public void moneyPerk (Player player) {
		player.moneyPerk = true;
		player.sendMessage("The coin donator perk has been activated.");
	}
		
}