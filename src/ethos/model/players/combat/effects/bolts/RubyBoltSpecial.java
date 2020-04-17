package ethos.model.players.combat.effects.bolts;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.range.RangeExtras;
import ethos.util.Misc;

public class RubyBoltSpecial implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		int change = Misc.random((int) (defender.getHealth().getCurrentHealth() / 5));
		int playerDamage = attacker.getHealth().getCurrentHealth() / 10;
		if (attacker.getHealth().getCurrentHealth() < 10) {
			return;
		}
		attacker.addDamageTaken(attacker, playerDamage);
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 754, false);
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		attacker.rubyspecial = true;
		NPC corp = NPCHandler.getNpc(319);
		if (defender.getDefinition().getNpcName() == null) {
			return;
		}
		int change = Misc.random((int) (defender.getHealth().getCurrentHealth() / 5));
		int playerDamage = attacker.getHealth().getCurrentHealth() / 10;
		if (attacker.getHealth().getCurrentHealth() < 10) {
			return;
		}
		attacker.appendDamage(playerDamage, Hitmark.HIT);
		if (defender == corp) {
			change = 300;
		}
		if (change > 100) {
			change = 100;
		}
		if (Boundary.isIn(attacker, Boundary.CORPOREAL_BEAST_LAIR)) {
			damage.setAmount(100);
			RangeExtras.createCombatGraphic(attacker, defender, 754, false);
			attacker.rubyspecial = true;
		}
		else if (!(Boundary.isIn(attacker, Boundary.CORPOREAL_BEAST_LAIR))) {
		damage.setAmount(change);
		RangeExtras.createCombatGraphic(attacker, defender, 754, false);
		}
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeExtras.boltSpecialAvailable(operator, 9242);
	}

}
