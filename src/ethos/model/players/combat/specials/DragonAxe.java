package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DragonAxe extends Special {

	public DragonAxe() {
		super(10.0, 1.0, 1.0, new int[] { 6739 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(2876);
		player.specBarId = 7812;
		player.specAmount = 0.0;
		player.usingSpecial = false;
		player.getItems().updateSpecialBar();
		player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
		player.gfx0(479);
		player.playerLevel[8] += 3;
		player.getPA().refreshSkill(8);
		player.forcedChat("Chop Chop!");
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}