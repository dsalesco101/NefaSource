package ethos.model.content;

import ethos.model.players.Player;

public class AlchemyPouch {
	
	public int NATURE = 561;
	public int FIRE = 554;
	public int CURRENCY = 995;
	public int COST = 500;
	/*
	 * Author : Rune-server:  AaronW
	 * www.NefariousPkz.com
	 */
	public static void addCharges(Player c) {
		c.getDH().sendDialogues(698, -1);
	}
	
	public static void checkCharges(Player c) {
		c.sendMessage("@blu@You have a total of @red@"+c.alchCharge+"@blu@ charges in your alchemy bag.");
	}
	
}