package ethos.model.players.combat.specials;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Special;

/**
 * @author Jason MacKeigan
 * @date Apr 8, 2015, 2015, 10:45:54 AM
 */
public class ArcLight extends Special {

	public ArcLight() {
		super(20.0, 1.10, 1.0, new int[] { 19675 });
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

