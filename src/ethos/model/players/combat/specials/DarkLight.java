package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class DarkLight extends Special {

	public DarkLight() {
		super(20.0, 1.10, 1.0, new int[] { 6746 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx0(1222);
		player.startAnimation(2890);
		player.specAccuracy = 4.55;
		player.specDamage = 1.30;
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
