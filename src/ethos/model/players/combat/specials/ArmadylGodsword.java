package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

public class ArmadylGodsword extends Special {

	public ArmadylGodsword() {
		super(5.0, 2.50, 1.375, new int[] { 11802, 20368 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(7644);
		player.gfx0(1211);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
