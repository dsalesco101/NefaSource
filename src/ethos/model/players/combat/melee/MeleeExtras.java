package ethos.model.players.combat.melee;

import java.util.Objects;

import ethos.Server;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.combat.AttackNPC;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

public class MeleeExtras {

	public static void applySmite(Player c, int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.playerLevel[5] -= damage / 4;
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}
	}

	public static void appendVengeanceNPC(Player c, int damage, int i) {
		if (damage <= 0)
			return;
		if (!c.vengOn)
			return;
		
		if (NPCHandler.npcs[i] != null) {
			c.forcedText = "Taste vengeance!";
			c.forcedChatUpdateRequired = true;
			c.setUpdateRequired(true);
			c.vengOn = false;
			if ((NPCHandler.npcs[i].getHealth().getCurrentHealth() - damage) > 0) {
				damage = (int) (damage * 0.75);
				if (damage > NPCHandler.npcs[i].getHealth().getCurrentHealth()) {
					damage = NPCHandler.npcs[i].getHealth().getCurrentHealth();
				}
				NPCHandler.npcs[i].appendDamage(c, damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
			}
		}
		c.setUpdateRequired(true);
	}

	public static void appendVengeance(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste vengeance!";
		o.forcedChatUpdateRequired = true;
		o.setUpdateRequired(true);
		o.vengOn = false;
		if ((o.getHealth().getCurrentHealth() - damage) > 0) {
			damage = (int) (damage * 0.75);
			c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageTaken(o, damage);
		}
		c.setUpdateRequired(true);
	}

	public static void applyRecoilNPC(Player c, int damage, int i) {
		if (damage <= 0) 
			return;
		if (damage > 0 && c.playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10;
			if (recDamage < 1 && damage > 0) {
				recDamage = 1;
			}
			if (NPCHandler.npcs[i].getHealth().getCurrentHealth() <= 0 || NPCHandler.npcs[i].isDead) {
				return;
			}
			NPCHandler.npcs[i].appendDamage(c, recDamage, Hitmark.HIT);
			removeRecoil(c);
			c.recoilHits += recDamage;
		}
		if (damage > 0 && c.playerEquipment[c.playerRing] == 19550 || c.playerEquipment[c.playerRing] == 19710) {
			int recDamage = damage / 10;
			if (recDamage < 1 && damage > 0) {
				recDamage = 1;
			}
			if (NPCHandler.npcs[i].getHealth().getCurrentHealth() <= 0 || NPCHandler.npcs[i].isDead) {
				return;
			}
			NPCHandler.npcs[i].appendDamage(c, recDamage, Hitmark.HIT);
			removeRecoil(c);
			c.recoilHits += recDamage;
		}
	}

	public static void applyRecoil(Player c, int damage, int i) {
		if (damage <= 0) 
			return;
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_RINGS)) {
					return;
				}
			}
		}
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			c.appendDamage(recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageTaken(PlayerHandler.players[i], recDamage);
			c.setUpdateRequired(true);
			removeRecoil(c);
			c.recoilHits += damage;
		}
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[c.playerRing] == 19550 || c.playerEquipment[c.playerRing] == 19710) {
			int recDamage = damage / 10 + 1;
			c.appendDamage(recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageTaken(PlayerHandler.players[i], recDamage);
			c.setUpdateRequired(true);
			c.recoilHits += damage;
		}
	}

	public static void removeRecoil(Player c) {
		if (c.recoilHits >= 40) {
			if (c.playerEquipment[c.playerRing] == 2550) {
				c.getItems().removeItem(2550, c.playerRing);
				c.getItems().deleteItem(2550, c.getItems().getItemSlot(2550), 1);
				c.recoilHits = 0;
				c.sendMessage("Your ring of recoil shaters!");
			}
		} else {
			c.recoilHits++;
		}
	}

	public static void graniteMaulSpecial(Player c, boolean queueSpecialOrSetAttacking) {
		int weapon = c.playerEquipment[c.playerWeapon];

		if (c.playerAttackingIndex > 0 && c.getHealth().getCurrentHealth() > 0) {
			Player o = PlayerHandler.players[c.playerAttackingIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
				if (c.getCombat().checkReqs()) {
					boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence());
					int damage = 0;
					if (hit)
						damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
					if (o.prayerActive[18])
						damage *= .6;
					if (o.getHealth().getCurrentHealth() - damage <= 0) {
						damage = o.getHealth().getCurrentHealth();
					}
					if (o.getHealth().getCurrentHealth() > 0 && c.getCombat().checkSpecAmount(weapon)) {
						c.getItems().updateSpecialBar();
						o.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
						c.startAnimation(1667);
						c.gfx100(340);
						o.addDamageTaken(c, damage);
						c.attackTimer += 1;
					}
				}
			}
		} else if (c.npcAttackingIndex > 0) {
			// Attacking npc
			NPC npc = NPCHandler.npcs[c.npcAttackingIndex];
			int x = npc.absX;
			int y = npc.absY;
			if (c.goodDistance(c.getX(), c.getY(), x, y, npc.getSize()) && AttackNPC.isAttackable(c, c.npcAttackingIndex)) {
				if (npc.getHealth().getCurrentHealth() == 0 || npc.needRespawn || npc.isDead || npc.applyDead)
					return;
				int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
				if (npc.getHealth().getCurrentHealth() - damage < 0) {
					damage = npc.getHealth().getCurrentHealth();
				}
				if (c.getCombat().checkSpecAmount(weapon)) {
					c.getItems().updateSpecialBar();
					npc.appendDamage(c, damage, Hitmark.HIT);
					c.startAnimation(1667, 6);
					c.gfx100(340);
					c.attackTimer += 1;
				}
			}
		} else if (queueSpecialOrSetAttacking) {
			if (c.lastPlayerAttackingIndex != 0) {
				Player o = PlayerHandler.players[c.lastPlayerAttackingIndex];
				if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
					c.playerAttackingIndex = c.lastPlayerAttackingIndex;
					c.faceUpdate(c.playerAttackingIndex);
					graniteMaulSpecial(c, false);
					if (c.attackTimer <= 1) {
						c.attackTimer += 1;
					}
					return;
				}
			} else if (c.lastNpcAttackingIndex != 0) {
				NPC o = NPCHandler.npcs[c.lastNpcAttackingIndex];
				if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())
						&& !o.applyDead && !o.isDead && !o.needRespawn && AttackNPC.isAttackable(c, o.getIndex())) {
					c.npcAttackingIndex = c.lastNpcAttackingIndex;
					c.faceNPC(c.npcAttackingIndex);
					graniteMaulSpecial(c, false);
					if (c.attackTimer <= 1) {
						c.attackTimer += 1;
					}
					return;
				}
			}

			if (++c.graniteMaulSpecialCharges > 2) {
				c.graniteMaulSpecialCharges = 0;
			} else if (c.getRights().contains(Right.GAME_DEVELOPER)) {
				c.sendMessage("Granite maul spec queued: " + c.graniteMaulSpecialCharges);
			}
		}
	}
}