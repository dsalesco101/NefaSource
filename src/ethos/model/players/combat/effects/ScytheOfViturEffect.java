package ethos.model.players.combat.effects;

import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

public class ScytheOfViturEffect implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		int halvedHit = damage.getAmount() == 0 ? 0 : damage.getAmount() / 2;
		int finalHit = halvedHit == 0 ? 0 : halvedHit / 2;
		attacker.getDamageQueue().add(new Damage(defender, halvedHit, attacker.hitDelay, attacker.playerEquipment, halvedHit > 0 ? Hitmark.HIT : Hitmark.MISS, CombatType.MELEE));
		attacker.getDamageQueue().add(new Damage(defender, finalHit, attacker.hitDelay + 1, attacker.playerEquipment, finalHit > 0 ? Hitmark.HIT : Hitmark.MISS, CombatType.MELEE));
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		int halvedHit = damage.getAmount() == 0 ? 0 : damage.getAmount() / 2;
		int finalHit = halvedHit == 0 ? 0 : halvedHit / 2;
		attacker.getDamageQueue().add(new Damage(defender, halvedHit, attacker.hitDelay, attacker.playerEquipment, halvedHit > 0 ? Hitmark.HIT : Hitmark.MISS, CombatType.MELEE));
		attacker.getDamageQueue().add(new Damage(defender, finalHit, attacker.hitDelay + 1, attacker.playerEquipment, finalHit > 0 ? Hitmark.HIT : Hitmark.MISS, CombatType.MELEE));
	}

	@Override
	public boolean isExecutable(Player operator) {
		return operator.getItems().isWearingItem(22325) && Misc.random(3) == 0;
	}

}
