package ethos.model.players;


import ethos.model.players.PlayerSave;

public class PrayerScrolls {
		

	public Player c;

	public PrayerScrolls(Player c) {
		this.c = c;
	}

	public static void giveRigourPrayer(Player c) {
		c.rigour = true;
		c.sendMessage("You have unlocked the secrets of the Dexterous Prayer scroll.");
		c.sendMessage("You can now use the prayer (rigour) on your spellbook.");
		PlayerSave.saveGame(c);
	}
	public static void giveAuguryPrayer(Player c) {
		c.augury = true;
		c.sendMessage("You have unlocked the secrets of the Arcane Prayer scroll.");
		c.sendMessage("You can now use the prayer (augury) on your spellbook.");
		PlayerSave.saveGame(c);
	}
}