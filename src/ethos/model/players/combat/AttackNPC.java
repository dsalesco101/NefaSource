package ethos.model.players.combat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ethos.model.npcs.bosses.hydra.AlchemicalHydra;
import org.apache.commons.lang3.RandomUtils;

import ethos.Config;
import ethos.Server;
import ethos.clip.PathChecker;
import ethos.model.entity.HealthStatus;

import ethos.model.items.EquipmentSet;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.minigames.raids.Raids;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.minigames.warriors_guild.WarriorsGuild;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.CorporealBeast;
import ethos.model.npcs.bosses.Scorpia;
import ethos.model.npcs.bosses.raids.Tekton;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.wildypursuit.FragmentOfSeren;
import ethos.model.npcs.bosses.wildypursuit.Sotetseg;
//import ethos.model.npcs.bosses.wildypursuit.Seren;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.combat.effects.ToxicBlowpipeEffect;
import ethos.model.players.combat.effects.ToxicStaffOfTheDeadEffect;
import ethos.model.players.combat.effects.TridentOfTheSwampEffect;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.range.RangeData;
import ethos.model.players.combat.range.RangeExtras;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.herblore.PoisonedWeapon;
import ethos.model.players.skills.herblore.PoisonedWeapon.PoisonLevel;
import ethos.model.players.skills.mining.Pickaxe;
import ethos.model.players.skills.slayer.SlayerMaster;
import ethos.model.players.skills.slayer.Task;
import ethos.util.Misc;

public class AttackNPC {

	private static double distanceToNpc;

	public static void calculateCombatDamage(Player attacker, NPC defender, CombatType combatType, Special special) {
		int maximumAccuracy = 0;
		int maximumDamage = 0;
		int damage = 0;
		int damage2 = -1;
		int damage3 = -1;
		int maximumDefence = Misc.random(defender.defence + getBonusDefence(attacker, defender, combatType));
		Hitmark hitmark1 = null;
		Hitmark hitmark2 = null;
		Hitmark hitmark3 = null;
		int accuracy = 0;

		if (Objects.nonNull(attacker) && Objects.nonNull(defender)) {
			if (combatType.equals(CombatType.MELEE)) {
				maximumDamage = attacker.getCombat().calculateMeleeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateMeleeAttack();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Melee hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				if (attacker.summonId == 2269) {
					NPC GOBLIN = NPCHandler.getNpc(2269);
				switch (Misc.random(10)) {
				case 1:
					NPCHandler.npcs[GOBLIN.getIndex()].forceChat("KILL HIM MASTER! KILl HIM.");
					break;
				case 2:
					NPCHandler.npcs[GOBLIN.getIndex()].forceChat("DIE!!!!!!!!!!!!!!!!!!!!!!.");
					break;
				case 3:
					NPCHandler.npcs[GOBLIN.getIndex()].forceChat("COMON! DO MORE DAMAGE!!.");
					break;
				case 4:
					NPCHandler.npcs[GOBLIN.getIndex()].forceChat("IM HUNGRY FOR LOOOT!.");
					break;
				case 5:
					NPCHandler.npcs[GOBLIN.getIndex()].forceChat("IM FEELING SOME LOOT COMING ON HERE!!.");
					break;
				}
			}
				damage = Misc.random(maximumDamage);
				accuracy = Misc.random(maximumAccuracy);
				if (defender.npcType == 6600) {
					Pickaxe pickaxe = Pickaxe.getBestPickaxe(attacker);
					if (pickaxe != null && attacker.getItems().isWearingItem(pickaxe.getItemId())) {
						damage += Misc.random(pickaxe.getPriority() * 4);
					}
				}
				switch (defender.npcType) {
			
				case 7930:
				case 7931:
				case 7932:
				case 7933:
				case 7934:
				case 7935:
				case 7936:
				case 7937:
				case 7938:
				case 7939:
				case 7940:
					if (attacker.playerEquipment[attacker.playerHands] == 21816 && attacker.ether <=0) {
						attacker.getItems().wearItem(21817, 1, 9);
					 }
					 if (attacker.playerEquipment[attacker.playerHands] == 21816) {
							attacker.braceletDecrease(1);
						 }
						break;
				case 7413:
					accuracy = maximumAccuracy;
					damage = maximumDamage;
					break;

					
				case 7144:
				case 2266:
				case 2267:
				case 9021:
					damage = 0;
					break;
					
				case 965:
					if (!EquipmentSet.VERAC.isWearingBarrows(attacker)) {
						damage = 0;
					}
					break;
				}
				if (defender.npcType == 5666) {
					damage = damage / 4;
					if (damage < 0) {
						damage = 0;
					}
				}
				if (!(EquipmentSet.VERAC.isWearingBarrows(attacker) && Misc.random(3) == 0)) {
					if (Misc.random(maximumDefence) > accuracy) {
						damage = 0;
					}
				}
				if (damage > 0 && EquipmentSet.GUTHAN.isWearingBarrows(attacker) && Misc.random(3) == 0) {
					attacker.getHealth().increase(damage);
					defender.gfx0(398);
				}
				if (attacker.doubleHit) {
					int maxHit = attacker.getCombat().calculateMeleeMaxHit();
					damage2 = Misc.random(maxHit);
				}
				/*if (brother.isPresent() && defender.npcType == brother.get().getId()) {
					damage *= brother.get().getEffectiveness(CombatType.MELEE);
					if (damage2 > 0) {
						damage2 *= brother.get().getEffectiveness(CombatType.MELEE);
					}
				}*/
				if (defender.npcType == Skotizo.SKOTIZO_ID) {
					damage = attacker.getSkotizo().calculateSkotizoHit(attacker, damage);
				}
				if (defender.getHealth().getCurrentHealth() - damage < 0) {
					damage = defender.getHealth().getCurrentHealth();
				}
				if (damage2 > 0) {
					if (damage == defender.getHealth().getCurrentHealth() && defender.getHealth().getCurrentHealth() - damage2 > 0) {
						damage2 = 0;
					}
				}
				if (defender.getHealth().getCurrentHealth() - damage - damage2 < 0) {
					damage2 = defender.getHealth().getCurrentHealth() - damage;
				}
				if (damage < 0) {
					damage = 0;
				}
				if (damage2 < 0 && damage2 != -1) {
					damage2 = 0;
				}
				if (damage3 < 0 && damage3 != -1) {
					damage3 = 0;
				}
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark3 = damage3 > 0 ? Hitmark.HIT : Hitmark.MISS;
				if (defender.npcType != 7413) {
					AttackPlayer.addCombatXP(attacker, CombatType.MELEE, damage + (damage2 > 0 ? damage2 : 0));
				}
				
				/**
				 * Ranged attack style
				 */
			} else if (combatType.equals(CombatType.RANGE)) {
				maximumDamage = attacker.getCombat().rangeMaxHit();
				maximumAccuracy = attacker.getCombat().calculateRangeAttack();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Range hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}
				damage = Misc.random(maximumDamage);
				accuracy = Misc.random(maximumAccuracy);
				if (Misc.random(maximumDefence) > accuracy && !attacker.ignoreDefence) {
					damage = 0;
				}
				if (attacker.lastWeaponUsed == 11235 || attacker.lastWeaponUsed == 12765 || attacker.lastWeaponUsed == 12766 || attacker.lastWeaponUsed == 12767
						|| attacker.lastWeaponUsed == 12768 || attacker.bowSpecShot == 1) {
					damage2 = Misc.random(maximumDamage);
					if (Misc.random(maximumDefence) > accuracy) {
						damage2 = 0;
					}
				}
				if (attacker.playerEquipment[attacker.playerShield] == 20714 && attacker.pages <= 0) { // tome of fire	
					attacker.sendMessage("Your tome of fire has run out of pages.");
					attacker.getCombat().resetPlayerAttack();
					return;
				}
				if (attacker.playerEquipment[attacker.playerWeapon] == 22550 && attacker.crawsbowCharge <= 1000) { // new		
					attacker.sendMessage("Your weapon has ran out of ether and can no longer function.");
					attacker.getCombat().resetPlayerAttack();
					return;
				}
				if (attacker.playerEquipment[attacker.playerWeapon] == 22545 && attacker.viggoraCharge <= 1000) { // new		
						attacker.sendMessage("Your weapon has ran out of ether and can no longer function."); 
						attacker.getCombat().resetPlayerAttack();
						return;
		             }
				if (attacker.playerEquipment[attacker.playerWeapon] == 22555 && attacker.thammaronCharge <= 1000) { // new		
						attacker.sendMessage("Your weapon has ran out of ether and can no longer function."); 
						attacker.getCombat().resetPlayerAttack();
						return;
				}
				 if (attacker.playerEquipment[attacker.playerWeapon] == 22550) {
						attacker.decreaseCrawsBowCharge(1);
					 }
				 if (attacker.playerEquipment[attacker.playerWeapon] == 22545) {
						attacker.decreaseViggoraCharge(1);
					 }
				 if (attacker.playerEquipment[attacker.playerWeapon] == 22555) {
						attacker.decreaseThammaronCharge(1);
					 }
				 if (attacker.playerEquipment[attacker.playerShield] == 20714 && !(attacker.spellId == 1181) || !(attacker.spellId == 1169) || !(attacker.spellId == 1158)
						 || !(attacker.spellId == 1539) || !(attacker.spellId == 1192)
							|| !(attacker.spellId == 12939) || !(attacker.spellId == 12963) || !(attacker.spellId == 12951) || !(attacker.spellId == 12975)
							|| !(attacker.spellId == 12975)) { //tome of fire
						attacker.decreasePages(1);
						attacker.decreasePages(1);
					 }
				switch (defender.npcType) {
				case 7413:
					damage = maximumDamage;
					accuracy = maximumAccuracy;
					break;
					
				case 7145:
				case 2265:
				case 2267:
				case 9022:
					damage = 0;
					break;
				case 8781:
					int health1 = defender.getHealth().getCurrentHealth();
					if (health1 > 500 && health1 < 600) {
						defender.forceChat("YOU'LL NEVER TAKE ME DOWN!!");
						defender.gfx0(1028);
						defender.gfx0(1009);
						attacker.getCombat().resetPlayerAttack();
					}
					break;
				case 5890:
					if (!attacker.getSlayer().getTask().isPresent()) {
						return;
					}
					if (!attacker.getSlayer().getTask().isPresent() && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal demon") && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal sire")) {
							attacker.sendMessage("The sire does not seem interested.");
							attacker.getCombat().resetPlayerAttack();
							return;
					}
					int health = defender.getHealth().getCurrentHealth();
					if (health > 329) {
						if (!attacker.getCombat().shadowSpells()) {
							attacker.sendMessage("This would not be effective, I should try shadow spells.");
							attacker.getCombat().resetPlayerAttack();
						}
					}
					break;
				
				}
				
				if (RangeExtras.wearingCrossbow(attacker) && RangeExtras.wearingBolt(attacker)) {
					damage = RangeExtras.executeBoltSpecial(attacker, defender, new Damage(damage));
				}
				DamageEffect venomEffect = new ToxicBlowpipeEffect();
				if (venomEffect.isExecutable(attacker)) {
					venomEffect.execute(attacker, defender, new Damage(6));
				}
				/*Optional<Brother> brother = attacker.getBarrows().getActive();
				if (brother.isPresent() && defender.npcType == brother.get().getId()) {
					damage *= brother.get().getEffectiveness(CombatType.RANGE);
					if (damage2 > 0) {
						damage2 *= brother.get().getEffectiveness(CombatType.RANGE);
					}
				}*/
				if (defender.npcType == Skotizo.SKOTIZO_ID) {
					damage = attacker.getSkotizo().calculateSkotizoHit(attacker, damage);
				}
				if (defender.getHealth().getCurrentHealth() - damage < 0) {
					damage = defender.getHealth().getCurrentHealth();
				}
				if (damage2 > 0) {
					if (damage == defender.getHealth().getCurrentHealth() && defender.getHealth().getCurrentHealth() - damage2 > 0) {
						damage2 = 0;
					}
				}
				if (defender.getHealth().getCurrentHealth() - damage - damage2 < 0) {
					damage2 = defender.getHealth().getCurrentHealth() - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				if (damage3 < 0 && damage3 != -1)
					damage3 = 0;
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark3 = damage3 > 0 ? Hitmark.HIT : Hitmark.MISS;
				if (defender.npcType != 7413) {
					AttackPlayer.addCombatXP(attacker, CombatType.RANGE, damage + (damage2 > 0 ? damage2 : 0));
				}
				
				/**
				 * Magic attack style
				 */
			} else if (combatType.equals(CombatType.MAGE)) {
				maximumDamage = attacker.getCombat().magicMaxHit();
				maximumAccuracy = attacker.getCombat().mageAtk();
				if (attacker.debugMessage)
					attacker.sendMessage("Max Magic hit: "+maximumDamage);
				if (special != null) {
					maximumAccuracy *= special.getAccuracy();
					maximumDamage *= special.getDamageModifier();
				}

				switch (defender.npcType) {

				case 7413:
					damage = maximumDamage;
					accuracy = maximumAccuracy;
					break;
					
				case 5890:
					if (!attacker.getSlayer().getTask().isPresent()) {
						return;
					}
					if (!attacker.getSlayer().getTask().isPresent() && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal demon") && !attacker.getSlayer().getTask().get().getPrimaryName().equals("abyssal sire")) {
							attacker.sendMessage("The sire does not seem interested.");
							attacker.getCombat().resetPlayerAttack();
							return;
					}
					int health = defender.getHealth().getCurrentHealth();
					if (health > 329) {
						if (!attacker.getCombat().shadowSpells()) {
							attacker.sendMessage("This would not be effective, I should try shadow spells.");
							attacker.getCombat().resetPlayerAttack();
						}
					}
					break;
				}
				damage = Misc.random(maximumDamage);
				accuracy = Misc.random(maximumAccuracy);
				if (damage > 0 && EquipmentSet.AHRIM.isWearing(attacker) && attacker.getItems().isWearingItem(12853) && Misc.random(100) < 30) {
					damage = Misc.random((int) (attacker.getCombat().magicMaxHit() * 1.3));
				}
				if (attacker.getCombat().godSpells()) {
					if (System.currentTimeMillis() - attacker.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (Misc.random(maximumDefence) > accuracy) {
					damage = 0;
					attacker.magicFailed = true;
				} else if (defender.npcType == 2881 || defender.npcType == 2882) {
					damage = 0;
					attacker.magicFailed = true;
				} else {
					attacker.magicFailed = false;
				}
				if (attacker.magicFailed) {
					damage = 0;
				}
				/*Optional<Brother> brother = attacker.getBarrows().getActive();
				if (brother.isPresent() && defender.npcType == brother.get().getId()) {
					damage *= brother.get().getEffectiveness(CombatType.MAGE);
					if (damage2 > 0) {
						damage2 *= brother.get().getEffectiveness(CombatType.MAGE);
					}
				}*/
				if (defender.npcType == Skotizo.SKOTIZO_ID) {
					damage = attacker.getSkotizo().calculateSkotizoHit(attacker, damage);
				}
				if (defender.getHealth().getCurrentHealth() - damage < 0) {
					damage = defender.getHealth().getCurrentHealth();
				}
				if (attacker.magicDef) {
					if (defender.npcType != 7413) {
						attacker.getPA().addSkillXP((damage * (attacker.getMode().getType().equals(ModeType.OSRS) ? 2 : Config.MELEE_EXP_RATE) / 3), 1, true);
						attacker.getPA().refreshSkill(1);
					}
				}
				DamageEffect tsotdEffect = new ToxicStaffOfTheDeadEffect();
				if (tsotdEffect.isExecutable(attacker)) {
					tsotdEffect.execute(attacker, defender, new Damage(6));
				}
				if (defender.npcType == 7413) {
					damage = attacker.getCombat().magicMaxHit();
					accuracy = attacker.getCombat().mageAtk();
				}
				hitmark1 = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark2 = damage2 > 0 ? Hitmark.HIT : Hitmark.MISS;
				hitmark3 = damage3 > 0 ? Hitmark.HIT : Hitmark.MISS;
				
				switch (defender.npcType) { //The position of where it sets the damage does matter. Make sure its after the damage calculation
					case 7146:
					case 2265:
					case 2266:
					case 9023:
						damage = 0;
						break;
				}
				
				if (defender.npcType != 7413) {
					AttackPlayer.addCombatXP(attacker, CombatType.MAGE, damage + (damage2 > 0 ? damage2 : 0));
					attacker.getPA().refreshSkill(6);
				}
			}
		}
		if (Boundary.isIn(attacker, PestControl.GAME_BOUNDARY)) {
			attacker.pestControlDamage += damage;
			if (damage2 > 0) {
				attacker.pestControlDamage += damage2;
			}
		}
		attacker.attackTimer = attacker.getCombat().getAttackDelay(ItemAssistant.getItemName(attacker.playerEquipment[attacker.playerWeapon]).toLowerCase());
		Optional<Integer> optional = PoisonedWeapon.getOriginal(attacker.playerEquipment[attacker.playerWeapon]);
		if ((optional.isPresent() && optional.get() == 1249 || attacker.getItems().isWearingItem(1249, attacker.playerWeapon)) && attacker.usingSpecial) {
			return;
		}
		int delay = attacker.hitDelay;
		Damage hit1 = new Damage(defender, damage, delay, attacker.playerEquipment, hitmark1, combatType, special);
		attacker.getDamageQueue().add(hit1);
		if (special != null) {
			special.activate(attacker, defender, hit1);
		}
		if (damage2 > -1) {
			attacker.getDamageQueue().add(new Damage(defender, damage2, delay, attacker.playerEquipment, hitmark2, combatType));
		}
		if (damage3 > -1) {
			attacker.getDamageQueue().add(new Damage(defender, damage3, delay, attacker.playerEquipment, hitmark3, combatType));
		}
		if (Boundary.isIn(attacker, Boundary.XERIC)) {
			attacker.xericDamage += damage;
		}
		
	}

	private static Object attacker() {
		// TODO Auto-generated method stub
		return null;
	}

	public static int salveDamage(Player c) {
		int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		return damage *= 1.15;
	}

	private static int getBonusDefence(Player player, NPC npc, CombatType type) {
		if (type.equals(CombatType.MELEE)) {
			switch (npc.npcType) {
			case 965:
				return EquipmentSet.VERAC.isWearing(player) ? +500 : 5000;
			case 5890:
			case 7144:
			case 7145:
			case 7146:
			case 7604:
			case 7605:
			case 7606:
				return 500;
			case 9021:
			case 9022:
			case 9023:
				return 200;
			case 7544:
				return 400;
			case 5129:
				return 500;
			case FragmentOfSeren.NPC_ID:
				return 500;
			}
		} else if (type.equals(CombatType.MAGE)) {
			switch (npc.npcType) {
			case 2042:
				return -150;
			case 319:
				return +80;
			case 2044:
			case 7544:
				return 1550;
			case 963:
				return +7000;
			case 965:
			case 5129:
				return 300;
			case FragmentOfSeren.NPC_ID:
				return 300;
			case 7144:
			case 7145:
			case 7146:
			case 5890:
			case 7604:
			case 7605:
			case 7606:
				return 500;
			}
		} else if (type.equals(CombatType.RANGE)) {
			switch (npc.npcType) {
			case 492:
				return 50;
			case 2042:
			case 2043:
			case 5890:
			case 7544:
				return 1500;
			case 5129:
				return 500;
			case FragmentOfSeren.NPC_ID:
				return 300;
			case 6766:
				return 280;
			case 319:
				return 80;
			case 2044:
				return -150;
			case 963:
				return +7000;
			case 965:
				return 300;
			case 7144:
			case 7145:
			case 5862:
			case 7146:
			case 7604:
			case 7605:
			case 7606:
				return 500;

			}
		}
		return 0;
	}

	public static void delayedHit(final Player c, final int i, Damage damage) {
		if (i > NPCHandler.npcs.length) {
			return;
		}
		NPC npc = NPCHandler.npcs[i];
		if (npc == null || npc.isDead) {
			return;
		}
		if (c.playerAttackingIndex <= 0 && c.npcAttackingIndex <= 0) {
			if (c.autoRet == 1) {
				c.playerAttackingIndex = c.getIndex();
				c.followId = c.getIndex();
			}
		}
		if (npc.getHealth().getCurrentHealth() <= 0) {
			if (npc.getHealth().getCurrentHealth() <= 0) {
				npc.isDead = true;
			}
			return;
		}
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead) {
				c.npcAttackingIndex = 0;
				return;
			}

			AlchemicalHydra.negateDamage(c, npc, damage);

			/**
			 * Damage applied and maybe changed
			 */
			switch (npc.npcType) {
			case 7413:
				NPCHandler.npcs[i].getHealth().setCurrentHealth(500000);
				break;
			/**
			 * Corporeal Beast
			 */
			case 320:
				if (!Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
					c.getCombat().resetPlayerAttack();
					c.sendMessage("You cannot do this from here.");
					return;
				}
				break;

			/**
			 * No melee
			 */
			case 2042: // Zulrah
			case 2043:
			case 2044:
				if (c.usingMelee) {
					damage.setAmount(0);
				}
				break;
//			/**
//			 * No range
//			 */
//			case 7145:
//				if (c.usingBow || c.usingCross || c.usingOtherRangeWeapons) {
//					damage.setAmount(0);
//				}
//				break;
//				
//			/**
//			 * No mage
//			 */
//			case 7146:
//				if (c.usingMagic) {
//					damage.setAmount(0);
//				}
//				break;

			/**
			 * Melee only
			 */
			case 986:
			case 988:
			case 6374:
				if (!c.usingMelee) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Range only
			 */
			case 987:
			case 6377:
				if (!c.usingBow && !c.usingCross && !c.usingOtherRangeWeapons && !c.usingBallista) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Magic only
			 */
			case 1610:
			case 1611:
			case 1612:
				if (!c.usingMagic) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Air based spells only
			 */
			case 6373:
			case 983:
				if (!c.getCombat().airSpells()) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Water based spells only
			 */
			case 984:
			case 6375:
				if (!c.getCombat().waterSpells()) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Fire based spells only
			 */
			case 985:
			case 6376:
				if (!c.getCombat().fireSpells()) {
					damage.setAmount(0);
				}
				break;

			/**
			 * Earth based spells only
			 */
			case 6378:
				if (!c.getCombat().earthSpells()) {
					damage.setAmount(0);
				}
				break;
			
			
			case 319:
				if (!Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
					c.getCombat().resetPlayerAttack();
					c.sendMessage("You cannot do this from here.");
					return;
				}
				CorporealBeast.attack(c, damage);
				c.corpDamage += damage.getAmount();
				break;
			case 7584:
			case 7604: //Skeletal mystic
			case 7605: //Skeletal mystic
			case 7606: //Skeletal mystic
				c.setSkeletalMysticDamageCounter(c.getSkeletalMysticDamageCounter() + damage.getAmount());
				break;
				
			case 7544: //Tekton
				c.setTektonDamageCounter(c.getTektonDamageCounter() + damage.getAmount());
				Tekton.tektonSpecial(c);
				break;
				
			
			case Sotetseg.NPC_ID: //Sotetseg
				c.setGlodDamageCounter(c.getGlodDamageCounter() + damage.getAmount());
				NPCHandler.glodAttack = "MAGIC";
				break;
			case FragmentOfSeren.NPC_ID: //Fragement of Seren
				c.setIceQueenDamageCounter(c.getIceQueenDamageCounter() + damage.getAmount());
				FragmentOfSeren.handleSpecialAttack(c);
				break;
				
			case 6617:
			case 6616:
			case 6615:
				List<NPC> healer = Arrays.asList(NPCHandler.npcs);
				if (Scorpia.stage > 0 && healer.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 6617 && !n.isDead && n.getHealth().getCurrentHealth() > 0)) {
					NPC scorpia = NPCHandler.getNpc(6615);
					Damage heal = new Damage(Misc.random(20 + 5)); //was 45 + 5 but heals to much for now
					if (scorpia != null && scorpia.getHealth().getCurrentHealth() < 150) {
						scorpia.getHealth().increase(heal.getAmount());
					}
				}
				break;
				
			case 3118: //Tz-kek small
				c.appendDamage(1, Hitmark.HIT);
				break;
				
			case 5862:
				if (Boundary.isIn(c, Boundary.WITHIN_BOUNDARY_CERB)) {
					c.getCerberus().cerberusSpecials();
				} else {
					damage.setAmount(0);
					c.sendMessage("@red@You should keep yourself in the middle so you don't get burned.");
				}
				break;
				
			case Skotizo.SKOTIZO_ID:
					c.getSkotizo().skotizoSpecials();
				break;
				
			case Skotizo.AWAKENED_ALTAR_NORTH:
			case Skotizo.AWAKENED_ALTAR_SOUTH:
			case Skotizo.AWAKENED_ALTAR_WEST:
			case Skotizo.AWAKENED_ALTAR_EAST:
					if (c.playerEquipment[c.playerWeapon] == 19675) {
						c.getSkotizo().arclightEffect(npc);
						return;
					}
				break;
				
			case 7144:
			case 7145:
			case 7146:
				int getTransformation = 0;
				c.totalGorillaDamage += damage.getAmount();
				if (c.totalGorillaDamage > 49) {
					if (c.usingMelee) {
						getTransformation = 7144;
					} else if (c.usingBow || c.usingCross || c.usingOtherRangeWeapons || c.usingBallista) {
						getTransformation = 7145;
					} else if (c.usingMagic || c.autocasting) {
						getTransformation = 7146;
					} else {
						getTransformation = 7144;
					}
					if (damage.getAmount() > 0) { //reset
						npc.requestTransform(getTransformation);
						c.totalGorillaDamage = 0;
					}
				}
				break;
			
		case 9021: //melee
		case 9022: //range
		case 9023: //mage
			int getTransformation2 = 0;
			c.totalHunllefDamage += damage.getAmount();

			if (c.totalHunllefDamage > 70) {
				if (c.usingMelee) {
					getTransformation2 = 9021;
				} else if (c.usingBow || c.usingCross || c.usingOtherRangeWeapons || c.usingBallista) {
					getTransformation2 = 9022;
				} else if (c.usingMagic || c.autocasting) {
					getTransformation2 = 9023;
				} else {
					getTransformation2 = 9021;
				}
				if (damage.getAmount() > 0) { //reset
					npc.requestTransform(getTransformation2);
					c.totalHunllefDamage = 0;
				}
			}
			break;
		}
			if (DagannothMother.RANGE_OF_IDS.contains(npc.npcType)) {
				DagannothMother mother = c.getDagannothMother();

				if (mother != null) {
					c.getDagannothMother().transformation(npc);
				}
			}
			if (RecipeForDisaster.RANGE_OF_GELATINNOTHS.contains(npc.npcType)) {
				RecipeForDisaster rfd = c.getrecipeForDisaster();

				if (rfd != null) {
					c.getrecipeForDisaster().transformation(npc);
				}
			}

			boolean rejectsFaceUpdate = false;
			if (npc.npcType >= 2042 && npc.npcType <= 2044 || npc.npcType == 6720) {
				if (c.getZulrahEvent().getNpc() != null && c.getZulrahEvent().getNpc().equals(npc)) {
					if (c.getZulrahEvent().getStage() == 1) {
						rejectsFaceUpdate = true;
					}
				}
				if (c.getZulrahEvent().isTransforming()) {
					return;
				}
			}
			if (!rejectsFaceUpdate) {
				NPCHandler.npcs[i].facePlayer(c.getIndex());
			}
			if (NPCHandler.npcs[i].underAttackBy > 0 && Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.getIndex();
			} else if (NPCHandler.npcs[i].underAttackBy < 0 && !Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.getIndex();
			}

			if (damage.getSpecial() != null) {
				damage.getSpecial().hit(c, NPCHandler.npcs[i], damage);
			}

			c.lastNpcAttacked = i;

			if (damage.getAmount() > 0) {
				if (!npc.getHealth().isNotSusceptibleTo(HealthStatus.POISON)) {
					damage.getEquipment().ifPresent(equipment -> {
						Optional<PoisonLevel> poison = Optional.empty();
						for (int equipmentItem : equipment) {
							if (equipmentItem == c.playerEquipment[c.playerWeapon] || equipmentItem == c.playerEquipment[c.playerArrows]) {
								poison = PoisonedWeapon.getPoisonLevel(equipmentItem);
								if (poison.isPresent()) {
									break;
								}
							}
						}
						poison.ifPresent(pl -> {
							if (RandomUtils.nextInt(0, pl.getPoisonProbability()) == 1) {
								npc.getHealth().proposeStatus(HealthStatus.POISON, pl.getPoisonDamage(), Optional.of(c));
							}
						});
					});
				}
			}
			if (damage.getCombatType() != null) {
				switch (damage.getCombatType()) {
				case MELEE:
					NPCHandler.npcs[i].appendDamage(c, damage.getAmount(), damage.getHitmark());
					break;

				case RANGE:
					if (c.dbowSpec) {
						c.dbowSpec = false;
					}
					boolean dropArrows = true;
					if (c.lastWeaponUsed >= 4212 || c.lastWeaponUsed <= 4223 || Item.getItemName(c.playerEquipment[3]).contains("crystal bow") || c.lastWeaponUsed == 22550) {
						dropArrows = false;
					}
					for (int noArrowId : c.NO_ARROW_DROP) {
						if (c.lastWeaponUsed == noArrowId) {
							dropArrows = false;
							break;
						}
					}
					if (dropArrows) {
						c.getItems().dropArrowNpc(NPCHandler.npcs[i]);
						if (c.playerEquipment[3] == 11235 || c.playerEquipment[3] == 12765 || c.playerEquipment[3] == 12766 || c.playerEquipment[3] == 12767
								|| c.playerEquipment[3] == 12768) {
							c.getItems().dropArrowNpc(NPCHandler.npcs[i]);
						}
					}
					if (Server.npcHandler.getNPCs()[i].attackTimer > 3) {
						if (npc.npcType != 2042 && npc.npcType != 2043 & npc.npcType != 2044 && npc.npcType != 3127 && npc.npcType != 319) {
							NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
						}
					}
					c.rangeEndGFX = RangeData.getRangeEndGFX(c);
					c.ignoreDefence = false;
					c.multiAttacking = false;

					if (c.playerEquipment[3] == 10034 || c.playerEquipment[3] == 10033
							|| c.playerEquipment[3] == 11959) {
						if (multiAttackRange(c, i)) {
							return;
						}
					}
					if (c.rangeEndGFX > 0) {
						if (c.rangeEndGFXHeight) {
							NPCHandler.npcs[i].gfx100(c.rangeEndGFX);
						} else {
							NPCHandler.npcs[i].gfx0(c.rangeEndGFX);
						}
					}
					if (c.killingNpcIndex != c.oldNpcIndex) {
						c.totalDamageDealt = 0;
					}
					NPCHandler.npcs[i].appendDamage(c, damage.getAmount(), damage.getHitmark());
					break;

				case MAGE:
					if (c.spellSwap) {
						c.spellSwap = false;
						c.setSidebarInterface(6, 16640);
						c.playerMagicBook = 2;
						c.gfx0(-1);
					}
					c.usingMagic = true;
					if (c.getCombat().getEndGfxHeight() == 100 && damage.getAmount() > 0) { // end
						// GFX
						NPCHandler.npcs[i].gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
						if (Server.npcHandler.getNPCs()[i].attackTimer > 3) {
							if (npc.npcType != 2042 && npc.npcType != 2043 & npc.npcType != 2044 && npc.npcType != 3127 && npc.npcType != 7413) {
								NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
							}
						}
					} else if (damage.getAmount() > 0) {
						NPCHandler.npcs[i].gfx0(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					if (damage.getAmount() == 0) {
						if (Server.npcHandler.getNPCs()[i].attackTimer > 3) {
							if (npc.npcType != 2042 && npc.npcType != 2043 & npc.npcType != 2044) {
								NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
							}
						}
						NPCHandler.npcs[i].gfx100(85);
					}
					if (multiAttackMagic(c, i)) {
						return;
					}
					if (c.playerEquipment[c.playerWeapon] == 11907) {
						c.setTridentCharge(c.getTridentCharge() - 1);
					} else if (c.playerEquipment[c.playerWeapon] == 12899) {
						c.setToxicTridentCharge(c.getToxicTridentCharge() - 1);
					}
					DamageEffect tridentOfTheSwampEffect = new TridentOfTheSwampEffect();
					if (tridentOfTheSwampEffect.isExecutable(c)) {
						tridentOfTheSwampEffect.execute(c, NPCHandler.npcs[i], new Damage(6));
					}
					if (damage.getAmount() > 0) {
						int freezeDelay = c.getCombat().getFreezeTime();// freeze
						if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0 && isFreezable(NPCHandler.npcs[i])) {
							NPCHandler.npcs[i].freezeTimer = freezeDelay;
						}
						switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12901:
						case 12919: // blood spells
						case 12911:
						case 12929:
							int heal = Misc.random(damage.getAmount() / 2);
							c.getHealth().increase(heal);
							c.getPA().refreshSkill(3);
							break;
						}
							
						/*case 12891:
							if (Boundary.isIn(c, Boundary.DESERT_BOUNDARY)) {
								c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.CAST_BARRAGE);
							}
						}*/
						if (damage.getAmount() > 0) {
							NPCHandler.npcs[i].appendDamage(c, damage.getAmount(), damage.getHitmark());
						}
					}
					break;

				default:
					break;
				}
			}
		}
		
		c.multiAttacking = false;
		c.killingNpcIndex = c.oldNpcIndex;
		NPCHandler.npcs[i].updateRequired = true;
		c.usingMagic = false;
		c.oldSpellId = 0;
		c.getCombat().checkVenomousItems();
		c.getCombat().checkDemonItems();
		Degrade.degrade(c);
		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot == 1) {
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}
		c.specAccuracy = 1.0;
		c.specDamage = 1.0;
	}

	private static boolean isFreezable(NPC npc) {
		switch (npc.npcType) {
		case 2042:
		case 2043:
		case 2044:
		case 7544:
		case 5129:
		case FragmentOfSeren.NPC_ID:
		case 2205:
		case 3129:
		case 2215:
		case 3162:
			return false;
		}
		return true;
	}

	private static boolean multiAttackMagic(Player player, int i) {
		boolean found = false;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null && NPCHandler.npcs[j].getHealth().getMaximumHealth() > 0) {
				if (NPCHandler.npcs[j].heightLevel != player.heightLevel) {
					continue;
				}
				int nX = NPCHandler.npcs[j].getX();
				int nY = NPCHandler.npcs[j].getY();
				int pX = NPCHandler.npcs[i].getX();
				int pY = NPCHandler.npcs[i].getY();
				if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1) && (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
					if (player.getCombat().multis() && NPCHandler.npcs[i].inMulti() && NPCHandler.npcs[j].heightLevel == NPCHandler.npcs[i].heightLevel) {
						player.getCombat().appendMultiBarrageNPC(j, player.magicFailed);
						found = true;
					}
				}
			}
		}
		return found;
	}
	

	private static boolean multiAttackRange(Player player, int i) {
		boolean found = false;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null && NPCHandler.npcs[j].getHealth().getMaximumHealth() > 0) {
				if (NPCHandler.npcs[j].heightLevel != player.heightLevel) {
					continue;
				}
				int nX = NPCHandler.npcs[j].getX();
				int nY = NPCHandler.npcs[j].getY();
				int pX = NPCHandler.npcs[i].getX();
				int pY = NPCHandler.npcs[i].getY();
				if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1) && (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
					if (NPCHandler.npcs[i].inMulti() && NPCHandler.npcs[j].heightLevel == NPCHandler.npcs[i].heightLevel) {
						player.getCombat().appendMultiChinchompa(j);
						found = true;
					}
				}
			}
		}
		return found;
	}

	public static boolean armaNpc(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6222:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 3166:
		case 3167:
		case 3168:
		case 3169:
		case 3174:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return true;
		}
		return false;
	}
	public static boolean xericRanged(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 7531:
		case 7538:
			return true;
		}
		return false;
	}

	public static void resetSpells(Player c) {
		if (c.playerMagicBook == 0) {
			c.setSidebarInterface(6, 938); // modern
		}
		if (c.playerMagicBook == 1) {
			c.setSidebarInterface(6, 838); // ancient
		}
		if (c.playerMagicBook == 2) {
			c.setSidebarInterface(6, 29999); // lunar
		}
	}

	public static boolean isAttackable(Player player, int i) {
		if (NPCHandler.npcs[i] == null)
			return false;
		if (!NPCHandler.npcs[i].inMulti() && NPCHandler.npcs[i].npcType != 7563 && NPCHandler.npcs[i].npcType != 5890 && NPCHandler.npcs[i].npcType != 7563) {
			if (!Boundary.isIn(player, Boundary.OLM) && !Boundary.isIn(player, Boundary.RAIDS)) {
				if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != player.getIndex()) {
					player.npcAttackingIndex = 0;
					player.sendMessage("This monster is already in combat.");
					return false;
				}
			}
			
		}
		if (NPCHandler.npcs[i].npcType != 5890 && NPCHandler.npcs[i].npcType != 7563 && NPCHandler.npcs[i].npcType != 5916 && NPCHandler.npcs[i].npcType != 7554 && NPCHandler.npcs[i].npcType != 7555 && NPCHandler.npcs[i].npcType != 7553) {
			if ((player.underAttackByPlayer > 0 || player.underAttackByNpc > 0) && player.underAttackByNpc != i && !player.inMulti()) {
				player.getCombat().resetPlayerAttack();
				player.sendMessage("I am already under attack.");
				return false;
			}
		}
		return true;
	}

	public static void attackNpc(Player c, int i) {
		if(AttackNPC.isAttackable(c, i) == false) {
			return;
		}
			
		if (NPCHandler.npcs[i] == null) {
			return;
		}
		if (c.playerEquipment[c.playerWeapon] == 12904 && c.usingSpecial) {
			c.usingSpecial=false;
			c.getItems().updateSpecialBar();
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (NPCHandler.npcs[i].npcType == FragmentOfSeren.NPC_ID && !FragmentOfSeren.isAttackable) {
			c.sendMessage("You can't attack her right now.");
			return;
		}
		NPC npc = NPCHandler.npcs[i];
		Optional<Task> task = SlayerMaster.get(npc.getName().replaceAll("_", " "));
		if (task.isPresent()) {
			int level = task.get().getLevel();
			if (c.playerLevel[Skill.SLAYER.getId()] < task.get().getLevel()) {
				c.sendMessage("You need a slayer level of " + level + " to attack this npc.");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 7544) {
			if (!Boundary.isIn(c, Boundary.TEKTON_ATTACK_BOUNDARY) && !Boundary.isIn(c, Boundary.XERIC)) {
				c.sendMessage("You must be within tektons territory to attack him.");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 7573) {
			if (!Boundary.isIn(c, Boundary.SHAMAN_BOUNDARY) && !Boundary.isIn(c, Boundary.XERIC)) {
				c.sendMessage("You must be within the shaman attack boundries");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 7554) {
			Raids raidInstance = c.getRaidsInstance();
			if(raidInstance != null)
			if(!raidInstance.rightHand || !raidInstance.leftHand) {
				c.sendMessage("@red@Please destroy both hands before attacking The Great Olm.");
				return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 4922 || NPCHandler.npcs[i].npcType == 5129 || NPCHandler.npcs[i].npcType == 8918 || NPCHandler.npcs[i].npcType == 8388) {
			if (!Boundary.isIn(c, Boundary.WILDERNESS)) {
				c.sendMessage("You must be within this npc's original spawn location!");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (!Boundary.isIn(c, Boundary.RAIDS) && NPCHandler.npcs[i].npcType == 7573 || NPCHandler.npcs[i].npcType == 7566 || NPCHandler.npcs[i].npcType == 7527 || 
				NPCHandler.npcs[i].npcType == 7528 || NPCHandler.npcs[i].npcType == 7529 || NPCHandler.npcs[i].npcType == 7585 || NPCHandler.npcs[i].npcType == 7604 ||
				NPCHandler.npcs[i].npcType == 7605 || NPCHandler.npcs[i].npcType == 7506 || NPCHandler.npcs[i].npcType == 7544 || NPCHandler.npcs[i].npcType == 7563 ||
				NPCHandler.npcs[i].npcType == 7553 || NPCHandler.npcs[i].npcType == 7554 || NPCHandler.npcs[i].npcType == 7555) {
			Raids.HITS += 1;
		}
		if (NPCHandler.npcs[i].npcType == 494) {
			if (!c.getSlayer().getTask().isPresent()) {
				c.sendMessage("You do not have a cave kraken task.");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (!c.getSlayer().getTask().isPresent() || !c.getSlayer().getTask().get().getPrimaryName().contains("kraken")) {
					c.sendMessage("You do not have a cave kraken task.");
					c.getCombat().resetPlayerAttack();
					return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 8609) {
			if(c.playerLevel[18] < 95) {
				c.sendMessage("You must have a slayer level of at least 95 to wear these boots");
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		if (NPCHandler.npcs[i].npcType == 9026) {//rat
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline rat")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9027) {//spider
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline spider")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9028) {//bat
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline bat")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9029) {//unicorn
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline unicorn")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9030) {//scorpion
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline scorpion")) {
				c.sendMessage("The creature does not seem interested.");
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9031) {//wolf
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline wolf")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9032) {//bear
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline bear")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (NPCHandler.npcs[i].npcType == 9033) {//dragon
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline dragon")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
	
		}
		if (NPCHandler.npcs[i].npcType == 9034) {//darkbeast
			if (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline dark beast")) {
				c.sendMessage("The creature does not seem interested.");
				c.resetDamageTaken();
				c.getCombat().resetPlayerAttack();
				return;
		}
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (npc.npcType >= 2042 && npc.npcType <= 2044 || npc.npcType == 6720) {
			if (c.getZulrahEvent().isTransforming()) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.getZulrahEvent().getStage() == 0) {
				c.getCombat().resetPlayerAttack();
				return;
			}
		}
		resetSpells(c);
		if (NPCHandler.npcs[i] != null) {
			c.getCombat().strBonus = c.playerBonus[10];
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].getHealth().getMaximumHealth() <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcAttackingIndex = 0;
				return;
			}
			if (c.teleTimer > 0) {
				return;
			}
			if (c.respawnTimer > 0) {
				c.npcAttackingIndex = 0;
				return;
			}
			
			if (c.playerEquipment[c.playerWeapon] == 4734 && c.playerEquipment[c.playerArrows] != 4740) {
				c.sendMessage("You must use bolt racks with the karil's crossbow.");
				c.npcAttackingIndex = 0;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (NPCHandler.npcs[i].npcType == 6611 || NPCHandler.npcs[i].npcType == 6612) {
				List<NPC> minion = Arrays.asList(NPCHandler.npcs);
				if (minion.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 5054 && !n.isDead && n.getHealth().getCurrentHealth() > 0)) {
					c.sendMessage("You must kill Vet'ions minions before attacking him.");
					c.npcAttackingIndex = 0;
					return;
				}
			}

			if (NPCHandler.npcs[i].npcType != 5890 && NPCHandler.npcs[i].npcType != 5916) {
				if ((c.underAttackByPlayer > 0 || c.underAttackByNpc > 0) && c.underAttackByNpc != i && !c.inMulti()) {
						c.getCombat().resetPlayerAttack();
						c.sendMessage("I am already under attack.");
						return;
				}
			}
			if (NPCHandler.npcs[i].spawnedBy != c.getIndex() && NPCHandler.npcs[i].spawnedBy > 0 && !Boundary.isIn(c, Boundary.XERIC)) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}
			if (c.getX() == NPCHandler.npcs[i].getX() && c.getY() == NPCHandler.npcs[i].getY()) {
				c.getPA().walkTo(0, 1);
			}
			if (c.isInvisible() && !c.getRights().isOrInherits(Right.OWNER)) {
				c.sendMessage("You cannot attack npcs while being invisible.");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (Boundary.isIn(NPCHandler.npcs[i], Boundary.GODWARS_BOSSROOMS) && !Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("You cannot attack that npc from outside the room.");
				return;
			}
			int npcType = NPCHandler.npcs[i].npcType;
			if (npcType == 2463 || npcType == 2464) {
				if (Boundary.isIn(c, WarriorsGuild.CYCLOPS_BOUNDARY)) {
					if (!c.getWarriorsGuild().isActive()) {
						c.sendMessage("You cannot attack a cyclops without talking to kamfreena.");
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
			}
			c.followId2 = i;
			c.followId = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.usingArrows = false;
				c.usingOtherRangeWeapons = false;
				c.usingCross = c.playerEquipment[c.playerWeapon] == 4734 || c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 21012 || c.playerEquipment[c.playerWeapon] == 21902;
				c.usingBallista = c.playerEquipment[c.playerWeapon] == 19481 || c.playerEquipment[c.playerWeapon] == 19478;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				c.usingRangeWeapon = false;
				c.usingMelee = false;
				c.usingMagic = false;
				CombatType combatType;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
					c.usingRangeWeapon = false;
					c.usingArrows = false;
					c.usingOtherRangeWeapons = false;
					c.usingCross = false;
					c.usingBallista = false;
					c.usingBow = false;
				}
				/**
				 * Cancel out any other style operation
				 */
				if (c.usingMagic) {
					c.usingCross = false;
					c.usingBallista = false;
				}
				switch (c.playerEquipment[c.playerWeapon]) {
				case 11907:
					if (c.autocasting && c.getTridentCharge() <= 0) {
							c.sendMessage("Your trident of the seas has no more charges.");
							c.getCombat().resetPlayerAttack();
							return;
						}
						c.usingMagic = true;
						c.autocasting = true;
						c.spellId = 52;
						c.attackTimer = 4;
					break;

				case 12899:
					if (c.autocasting) {
						if (c.getToxicTridentCharge() <= 0) {
							c.sendMessage("Your trident of the swamp has no more charges.");
							c.getCombat().resetPlayerAttack();
							return;
						}
						c.usingMagic = true;
						c.autocasting = true;
						c.spellId = 53;
						c.attackTimer = 4;
					}
					break;
				}
				c.attackTimer = c.getCombat().getAttackDelay(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				if (c.getItems().isWearingItem(12931) || c.getItems().isWearingItem(13197) || c.getItems().isWearingItem(13199)) {
					if (c.getSerpentineHelmCharge() <= 0) {
						c.sendMessage("Your serpentine helm has no charge, you need to recharge it.");
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.playerEquipment[c.playerWeapon] == bowId && System.currentTimeMillis() - c.switchDelay >= 600) {
							c.usingBow = true;
							if (bowId == 19481 || bowId == 19478) {
								c.usingBow = false;
								c.usingBallista = true;
							}
							c.rangeDelay = 3;
							for (int arrowId : c.ARROWS) {
								if (c.playerEquipment[c.playerArrows] == arrowId) {
									c.usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
							c.usingOtherRangeWeapons = true;
						}
					}
				}
				if (c.getItems().isWearingItem(12926)) {
					if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0 || c.getToxicBlowpipeCharge() == 0) {
						c.sendMessage("Your blowpipe is out of ammo or charge.");
						c.getCombat().resetPlayerAttack();
						return;
					}
					c.usingBow = true;
					c.usingArrows = true;
				}
				if (!c.usingMagic && !c.usingCross && !c.usingBallista && !c.usingBow && !c.usingOtherRangeWeapons) {
					c.usingMelee = true;
				}
				if (armaNpc(i) && !c.usingBallista && !c.usingCross && !c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) {
					c.getCombat().resetPlayerAttack();
					c.sendMessage("You need to range attack this monster!");
					return;
				}
				
				NPC theNPC = NPCHandler.npcs[i];
				if (!c.checkNpcAttackDistance(theNPC)) {
					c.attackTimer = 1;
					return;
				}

				boolean projectile = c.usingOtherRangeWeapons || c.usingBow || c.usingMagic || c.autocasting
						|| c.getCombat().usingCrystalBow() || c.playerEquipment[c.playerWeapon] == 22550;
				if (projectile && !PathChecker.isProjectilePathClear(c, theNPC, c.absX, c.absY, c.heightLevel, theNPC.absX, theNPC.absY)) {
					c.attackTimer = 1;
					return;
				}
				
				if (!c.usingBallista && !c.usingCross && !c.usingArrows && c.usingBow && (((c.playerEquipment[c.playerWeapon] < 4212) || (c.playerEquipment[c.playerWeapon] > 4223)))
						&& !(c.playerEquipment[c.playerWeapon] == 22550)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcAttackingIndex = 0;
					return;
				}
				if ((c.getCombat().usingCrystalBow() || c.playerEquipment[c.playerWeapon] == 22550) && c.usingArrows && c.getCombat().properBolts()) {
					c.sendMessage("You cannot use ammo with this bow.");
					return;
				}
				
				if (!c.getCombat().correctBowAndArrows() && Config.CORRECT_ARROWS && c.usingBow && !c.getCombat().usingCrystalBow() &&  !(c.playerEquipment[c.playerWeapon] == 22550) && c.playerEquipment[c.playerWeapon] != 9185
						&& c.playerEquipment[c.playerWeapon] != 4734 && c.playerEquipment[c.playerWeapon] != 11785 && c.playerEquipment[c.playerWeapon] != 21012 && c.playerEquipment[c.playerWeapon] != 12926 && c.playerEquipment[c.playerWeapon] != 19478 && c.playerEquipment[c.playerWeapon] != 19481 && c.playerEquipment[c.playerWeapon] != 21902) {
					c.sendMessage("You can't use " + ItemAssistant.getItemName(c.playerEquipment[c.playerArrows]).toLowerCase() + "'s with a "
							+ ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					c.npcAttackingIndex = 0;
					return;
				}
				if (c.playerEquipment[c.playerWeapon] == 9185 && !c.getCombat().properBolts() || c.playerEquipment[c.playerWeapon] == 11785 && !c.getCombat().properBolts() || c.playerEquipment[c.playerWeapon] == 21012 && !c.getCombat().properBolts() || c.playerEquipment[c.playerWeapon] == 21902 && !c.getCombat().properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.playerEquipment[c.playerWeapon] == 19478 && !c.getCombat().properJavelins() || c.playerEquipment[c.playerWeapon] == 19481 && !c.getCombat().properJavelins()) {
					c.sendMessage("You must use javelins with a ballista.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.usingBow || c.usingMagic || c.usingOtherRangeWeapons || c.usingBallista
						|| (c.getCombat().usingHally() || c.playerEquipment[c.playerWeapon] == 11907 || c.playerEquipment[c.playerWeapon] == 12899 && distanceToNpc <= 2)) {
					c.stopMovement();
				}
				if (!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcAttackingIndex = 0;
					return;
				}
				if (c.usingBow || c.usingOtherRangeWeapons || c.usingCross || c.usingBallista) {
					combatType = CombatType.RANGE;
				} else if (c.usingMagic) {
					combatType = CombatType.MAGE;
				} else {
					combatType = CombatType.MELEE;
				}
				
				if (c.usingMagic && !c.autocasting) {
					c.followId2 = 0;
					c.stopMovement();
				}
				
				c.faceUpdate(i);
				NPCHandler.npcs[i].underAttackBy = c.getIndex();
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				NPCHandler.npcs[i].underAttack = true;
				if (NPCHandler.transformOnAttack(NPCHandler.npcs[i])) {
					return;
				}
				if (c.getTargeted() == null || !c.getTargeted().equals(npc)) {
					c.setTargeted(NPCHandler.npcs[i]);
					c.getPA().sendEntityTarget(1, npc);
				}

				if (c.usingSpecial && !c.usingMagic) {
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					Special special = Specials.forWeaponId(c.playerEquipment[c.playerWeapon]);
					if (special == null) {
						return;
					}
					if (special.getRequiredCost() > c.specAmount) {
						c.sendMessage("You don't have enough power left.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
						c.npcAttackingIndex = 0;
						return;
					}
					c.doubleHit = false;
					c.specEffect = 0;
					c.projectileStage = 0;
					c.specMaxHitIncrease = 2;
					c.logoutDelay = System.currentTimeMillis();
					c.oldNpcIndex = i;
					c.specAmount -= special.getRequiredCost();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					calculateCombatDamage(c, NPCHandler.npcs[i], combatType, special);
					if (c.usingOtherRangeWeapons || c.usingBow) {
						if (c.fightMode == 2) {
							c.attackTimer--;
						}
					}
					c.usingSpecial = false;
					c.getItems().updateSpecialBar();
					c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					return;
				}
				c.specMaxHitIncrease = 0;
				if (c.getHealth().getCurrentHealth() > 0 && !c.isDead && NPCHandler.npcs[i].getHealth().getMaximumHealth() > 0) {
					if (!c.usingMagic) {
						c.startAnimation(c.getCombat().getWepAnim(ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()));
						if (Server.npcHandler.getNPCs()[i].attackTimer > 3) {
							if (npcType != 2042 && npcType != 2043 & npcType != 2044 && npcType != 3127 && npcType != 319) {
								NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
							}
						}
					} else {
						c.startAnimation(MagicData.MAGIC_SPELLS[c.spellId][2]);
					}
				}
				c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
				c.lastArrowUsed = c.playerEquipment[c.playerArrows];
				if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons && !c.usingBallista) { // melee															// delay
					c.followId2 = NPCHandler.npcs[i].getIndex();
					c.getPA().followNpc();
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}

				if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic || c.usingCross || c.usingBallista) { // range
																								// hit
																								// delay
					if (c.usingCross)
						c.usingBow = true;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.followId2 = NPCHandler.npcs[i].getIndex();
					c.getPA().followNpc();
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
						c.getCombat().fireProjectileNpc(0);
					} else if (c.playerEquipment[c.playerWeapon] == 12926) {
						c.getCombat().fireProjectileNpc(0);
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
						if (c.playerEquipment[3] == 11235 || c.playerEquipment[3] == 12765 || c.playerEquipment[3] == 12766 || c.playerEquipment[3] == 12767
								|| c.playerEquipment[3] == 12768) {
							c.getItems().deleteArrow();
						}
						c.getCombat().fireProjectileNpc(0);
					}
				}

				if (c.usingOtherRangeWeapons && !c.usingMagic && !c.usingBow) {
					c.usingRangeWeapon = true;
					c.followId2 = NPCHandler.npcs[i].getIndex();
					c.getPA().followNpc();
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteEquipment();
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.lastArrowUsed = 0;
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					c.getCombat().fireProjectileNpc(0);
				}
				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = NPCHandler.npcs[i].getX();
					int nY = NPCHandler.npcs[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.projectileStage = 2;
					c.stopMovement();
					if (MagicData.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(MagicData.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(MagicData.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (MagicData.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, MagicData.MAGIC_SPELLS[c.spellId][4], c.getCombat().getStartHeight(), c.getCombat().getEndHeight(),
								i + 1, 50);
					}
					c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					if (!c.autocasting)
						c.npcAttackingIndex = 0;
				}
				if (System.currentTimeMillis() - c.lastDamageCalculation > 1000) {
					calculateCombatDamage(c, NPCHandler.npcs[i], combatType, null);
					c.lastDamageCalculation = System.currentTimeMillis();
				}
				if (c.usingOtherRangeWeapons || c.usingBow) {
					if (c.fightMode == 2)
						c.attackTimer--;
				}
				if (c.playerEquipment[c.playerWeapon] == 22550 && c.crawsbowCharge <= 0) { // new								
					c.getItems().wearItem(22547, 1, 3);
				}
				if (c.playerEquipment[c.playerWeapon] == 22545 && c.viggoraCharge <= 0) { // new								
					c.getItems().wearItem(22542, 1, 3);
				}
				if (c.playerEquipment[c.playerWeapon] == 22555 && c.thammaronCharge <= 0) { // new								
					c.getItems().wearItem(22552, 1, 3);
				}
				if (c.usingBow && c.usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (c.playerEquipment[c.playerWeapon] == 4212) { // new								
						c.getItems().wearItem(4214, 1, 3);
					}
					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), c.heightLevel, 1);
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}

	public static CombatType checkCombatType(Player c) {
		boolean usingBow = false;
		boolean usingOtherRangeWeapons = false;
		boolean usingCross = c.playerEquipment[c.playerWeapon] == 4734 || c.playerEquipment[c.playerWeapon] == 21902  || c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 33124 || c.playerEquipment[c.playerWeapon] == 33094 || c.playerEquipment[c.playerWeapon] == 21012 || c.playerEquipment[c.playerWeapon] == 33114;
		boolean usingBallista = c.playerEquipment[c.playerWeapon] == 19481 || c.playerEquipment[c.playerWeapon] == 19478;
		
		boolean usingRangeWeapon = false;
		boolean usingMelee = false;
		boolean usingMagic = false;
		
		if (c.autocasting) {
			usingMagic = true;
		}
		if (c.spellId > 0) {
			usingMagic = true;
			usingRangeWeapon = false;
			usingOtherRangeWeapons = false;
			usingCross = false;
			usingBallista = false;
			usingBow = false;
		}
		/**
		 * Cancel out any other style operation
		 */
		if (usingMagic) {
			usingCross = false;
			usingBallista = false;
		}
		switch (c.playerEquipment[c.playerWeapon]) {
		case 11907:
			if (c.autocasting) {
				if (c.getTridentCharge() <= 0) {
					return CombatType.MELEE;
				}
				usingMagic = true;
			}
			break;

		case 12899:
			if (c.autocasting) {
				if (c.getToxicTridentCharge() <= 0) {
					return CombatType.MELEE;
				}
				usingMagic = true;
			}
			break;
		}
		if (usingMagic) {
			for (int bowId : c.BOWS) {
				if (c.playerEquipment[c.playerWeapon] == bowId && System.currentTimeMillis() - c.switchDelay >= 600) {
					usingBow = true;
					if (bowId == 19481 || bowId == 19478) {
						usingBow = false;
						usingBallista = true;
					}
				}
			}

			for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
				if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
		}
		if (c.getItems().isWearingItem(12926)) {
			if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0 || c.getToxicBlowpipeCharge() == 0) {
				return CombatType.MELEE;
			}
			usingBow = true;
		}
		if (!usingMagic && !usingCross && !usingBallista && !usingBow && !usingOtherRangeWeapons) {
			usingMelee = true;
		}
		
		if(usingMagic) {
			return CombatType.MAGE;
		} else if(usingRangeWeapon || usingBow || usingCross || usingBallista) {
			return CombatType.RANGE;
		} else {
			return CombatType.MELEE;
		}
	}
}