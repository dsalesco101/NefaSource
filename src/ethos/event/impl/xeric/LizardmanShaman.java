/**
 * 
 */
package ethos.event.impl.xeric;

import java.util.Optional;

import ethos.model.entity.Entity;
import ethos.model.entity.HealthStatus;
import ethos.model.npcs.NPC;
import ethos.model.players.combat.CombatScript;
import ethos.model.players.combat.CombatScript.Graphic;
import ethos.model.players.combat.ScriptSettings;
import ethos.model.players.combat.CombatScript.Hit;
import ethos.model.players.combat.CombatScript.Projectile;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

/**
 * @author Patrity
 *
 */
@ScriptSettings(
		npcNames = { },
		npcIds = { 7573 }
	)
public class LizardmanShaman extends CombatScript {

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#attack(ethos.model.entity.npc.NPC, ethos.model.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		int healChance = Misc.random(1, 100);
		int poisonChance = Misc.random(1, 100);
		boolean melee = target.distanceToPoint(npc.getX(), npc.getY()) <= 3;
		int damage = getRandomMaxHit(npc, target, melee ? CombatType.MELEE : CombatType.MAGE, melee ? 28 : 30);
		
		
		if(!melee) {
			npc.startAnimation(7193);
			npc.attackType = CombatType.MAGE;
			handleHit(npc, target, CombatType.MAGE, new Projectile(1293, 120, 40, 0, 100, 0, 50), new Graphic (1294, 0), new Hit(Hitmark.HIT, damage, 4));
			if (healChance >= 80) {
				npc.appendDamage(50, Hitmark.HEAL_PURPLE);
				target.asPlayer().sendMessage("The Shaman leached some of your health!");
			}
			if (poisonChance >= 80) {
				target.asPlayer().getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(npc));
				target.asPlayer().sendMessage("You have been Poisoned!");
			}
		} else {
			npc.startAnimation(7192);
			handleHit(npc, target, CombatType.MELEE, new Hit(Hitmark.HIT, damage, 1));
		}
		return 5;
	}

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#getAttackDistance(ethos.model.entity.npc.NPC)
	 */
	@Override
	public int getAttackDistance(NPC npc) {
		return npc.attackType == CombatType.MELEE ? 1 : 8;
	}

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#ignoreProjectileClipping()
	 */
	@Override
	public boolean ignoreProjectileClipping() {
		return true;
	}
	
	@Override
	public void attackStyleChange(NPC npc, Entity target) {
		int distance = npc.distanceToPoint(target.getX(), target.getY());
		if (distance > 1) {
			npc.attackType = CombatType.MAGE;
		} else {
			npc.attackType = CombatType.MELEE;
		}
	}

}