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
		npcIds = { 7531 }
	)
public class Vespula extends CombatScript {

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#attack(ethos.model.entity.npc.NPC, ethos.model.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		
		int randomAttack = Misc.random(1, 100);
		
		if (randomAttack <= 50) { // range
			int damage = getRandomMaxHit(npc, target, CombatType.RANGE, 35);
			npc.startAnimation(7455);
			handleHit(npc, target, CombatType.RANGE, new Projectile(328, 120, 40, 0, 100, 0, 50), new Graphic (331, 0), new Hit(Hitmark.HIT, damage, 4));			
		} else { //mage
			int damage = getRandomMaxHit(npc, target, CombatType.MAGE, 20);
			npc.startAnimation(7455);
			handleHit(npc, target, CombatType.MAGE, new Projectile(1465, 120, 40, 0, 100, 0, 50), new Graphic (1028, 0), new Hit(Hitmark.HIT, damage, 4));	
			
		}
		return 5;
	}

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#getAttackDistance(ethos.model.entity.npc.NPC)
	 */
	@Override
	public int getAttackDistance(NPC npc) {
		// TODO Auto-generated method stub
		return 8;
	}

	/* (non-Javadoc)
	 * @see ethos.model.entity.npc.combat.CombatScript#ignoreProjectileClipping()
	 */
	@Override
	public boolean ignoreProjectileClipping() {
		// TODO Auto-generated method stub
		return true;
	}

}
