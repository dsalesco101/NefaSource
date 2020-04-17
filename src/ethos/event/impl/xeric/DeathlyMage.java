package ethos.event.impl.xeric;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.combat.CombatScript;
import ethos.model.players.combat.ScriptSettings;
import ethos.model.players.combat.CombatScript.Hit;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;

@ScriptSettings(
	npcNames = { "Deathly Mage" },
	npcIds = { 7560 }
)

public class DeathlyMage extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		System.out.println(npc.npcType+" Attack run!");
		int damage = getRandomMaxHit(npc, target, CombatType.MAGE, 35);
		npc.startAnimation(7855);
		handleHit(npc, target, CombatType.MAGE, new Projectile(1465, 40, 40, 0, 100, 0, 50), new Graphic (1028, 0), new Hit(Hitmark.HIT, damage, 4));
		return 5;
	}

	@Override
	public int getAttackDistance(NPC npc) {
		return 8;
	}
	
	@Override
	public boolean ignoreProjectileClipping() {
		return true;
	}

}
