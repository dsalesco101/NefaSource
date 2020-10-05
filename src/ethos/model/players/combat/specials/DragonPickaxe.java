package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DragonPickaxe extends Special {

	public DragonPickaxe() {
		super(10.0, 1.0, 1.0, new int[] { 11920 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(7138);
		//player.specBarId = 7812;
		player.specAmount = 0.0;
		player.usingSpecial = false;
		player.getItems().updateSpecialBar();
		player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
		//player.gfx0(479);
		player.playerLevel[14] += 3;
		player.getPA().refreshSkill(14);
		player.forcedChat("Smashing!");
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}