package ethos.model.players.packets.itemoptions;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ethos.Config;
import ethos.Server;
import ethos.model.content.AlchemyPouch;
import ethos.model.content.loot.LootableInterface;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.content.teleportation.TeleportTablets;
import ethos.model.items.ItemDefinition;
import ethos.model.items.item_combinations.Godswords;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.skills.hunter.impling.Impling;
import ethos.model.players.skills.runecrafting.Pouches;
import ethos.model.players.skills.runecrafting.Pouches.Pouch;
import ethos.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemOptionTwo implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemId = player.getInStream().readSignedWordA();

		if (!player.getItems().playerHasItem(itemId, 1))
			return;
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return;
		}
		if (LootingBag.isLootingBag(player, itemId)) {
			player.getLootingBag().openDepositMode();
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			player.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		TeleportTablets.operate(player, itemId);
		ItemDefinition def = ItemDefinition.forId(itemId);
		switch (itemId) {
		 case 977:
	        	AlchemyPouch.checkCharges(player);
	        	break;
		case 12007:
		case 22106:
		case 12936:
			player.getDH().sendDialogues(361, 8819);
			break;
		case 20714:
			player.sendMessage("@blu:You currently have @red@"+player.pages+"@blu@ in your tome of fire.");
			break;
		 case 21259:
			 if(System.currentTimeMillis() - player.clickDelay < 120000) {
	             player.sendMessage("Trade this to aaron to change your name.");
                 return;
			 	}
			   List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> p.getRights().isOrInherits(Right.OWNER)).collect(Collectors.toList());
               player.sendMessage("Trade this to aaron to change your name.", 13611);
               player.sendMessage("@red@You have alerted aaron about the request if online.");
               player.startAnimation(3141);
			   PlayerHandler.sendMessage("[@blu@NAME CHANGE REQUESTED]@red@"+player.playerName+" @blu@has just requeseted a name change", staff);	 
	           player.clickDelay = System.currentTimeMillis();
	         break;
		case 21817:
			 player.getItems().deleteItem(21817, 1);
			 player.getItems().addItem(21820, 250);
			 player.getItems().addItem(995, 50000);
			break;
		case 21816:
		 if (player.getItems().freeSlots() < 1) {
			 player.sendMessage("You need atleast one free slots to use this command.");
             return;
         }
		player.getItems().addItem(21820, player.ether);
		player.braceletDecrease(player.ether);
		player.sendMessage("@blu@You have removed @red@"+player.ether+"@blu@ ether into your inventory");
		 if (player.ether <= 0) {
				player.getItems().deleteItem(21816, 1);
				player.getItems().addItem(21817, 1);
			 return;
		 }
		break;
		case 7509:
            if (player.inDuelArena() || Boundary.isIn(player, Boundary.DUEL_ARENA)) {
            	player.sendMessage("You cannot do this here.");
                return;
            }
            if (player.getHealth().getStatus().isPoisoned() || player.getHealth().getStatus().isVenomed()) {
            	player.sendMessage("You are effected by venom or poison, you should cure this first.");
                return;
            }
            if (player.getHealth().getCurrentHealth() <= 10 && player.getHealth().getCurrentHealth() > 1 ) {
                player.appendDamage(1, Hitmark.HIT);
                player.forcedChat("URGHHHHH!");
                return;
            }
            if (player.getHealth().getCurrentHealth() <= 1) {
            	player.sendMessage("I better not do that.");
                return;
            }
            player.forcedChat("URGHHHHH!");
            player.startAnimation(829);        
            int currentHealth = player.getHealth().getCurrentHealth() / 10;
            player.appendDamage(currentHealth, Hitmark.HIT);
            break;
		case 9762:
			if (!Boundary.isIn(player, Boundary.EDGEVILLE_PERIMETER)) {
				player.sendMessage("This cape can only be operated within the edgeville perimeter.");
				return;
			}
			if (player.inWild()) {
				return;
			}
				if (player.playerMagicBook == 0) {
					player.playerMagicBook = 1;
					player.setSidebarInterface(6, 838);
					player.autocasting = false;
					player.sendMessage("An ancient wisdomin fills your mind.");
					player.getPA().resetAutocast();
				} else if (player.playerMagicBook == 1) {
					player.sendMessage("You switch to the lunar spellbook.");
					player.setSidebarInterface(6, 29999);
					player.playerMagicBook = 2;
					player.autocasting = false;
					player.autocastId = -1;
					player.getPA().resetAutocast();
				} else if (player.playerMagicBook == 2) {
					player.setSidebarInterface(6, 938);
					player.playerMagicBook = 0;
					player.autocasting = false;
					player.sendMessage("You feel a drain on your memory.");
					player.autocastId = -1;
					player.getPA().resetAutocast();
				}
				break;
		case 9780:
			player.getPA().spellTeleport(3810, 3550, 0, false);
			player.sendMessage("You have teleported to the Crafting Shop.");
			break;
		case 22550:
            int stored = player.crawsbowCharge - 1000;
			player.sendMessage("Your Craw's bow  bow current has "+stored+" charges.");
			break;
		case 22555:
            int stored1 = player.thammaronCharge - 1000;
			player.sendMessage("Your thammaron sceptre current has "+stored1+" charges.");
			break;
		case 22545:
		    int stored2 = player.viggoraCharge - 1000;
			player.sendMessage("Your Viggora's chainmace current has "+stored2+" charges.");
			break;
		case 6199:
            LootableInterface.openMysteryView(player);
			break;
		case 6828:
            LootableInterface.openSuperMboxView(player);
			break;
		case 13346:
            LootableInterface.openUltraMboxView(player);
			break;
		case 405:
            LootableInterface.openPvmCasketView(player);
			break;
		case 11739:
			LootableInterface.openVoteMboxView(player);
			break;
		case 21347:
			player.boltTips = false;
			player.arrowTips = true;
			player.javelinHeads = false;
			player.sendMessage("Your Amethyst method is now Arrowtips!");
			break;

			case 13660:
				if(player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
					player.sendMessage("You can't teleport above " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					return;
				}
				player.getPA().showInterface(51000);
				player.getTeleport().selection(player, 0);
				return;
		case 11238:
		case 11240:
		case 11242:
		case 11244:
		case 11246:
		case 11248:
		case 11250:
		case 11252:
		case 11254:
		case 11256:
		case 19732:
			Impling.getReward(player, itemId);
			break;
		case 20164: //Spade
			int x = player.absX;
			int y = player.absY;
	
		case 13190:
			player.getItems().addItemToBank(13190, 1);
				break;
				
		case 20243:
				if (System.currentTimeMillis() - player.lastPerformedEmote < 2500)
					return;			
				player.startAnimation(7268);
				player.lastPerformedEmote = System.currentTimeMillis();
			break;
		
		case 13136:
			if (player.inClanWars() || player.inClanWarsSafe()) {
				player.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			if (Server.getMultiplayerSessionListener().inAnySession(player)) {
				player.sendMessage("You cannot do that right now.");
				return;
			}
			if (player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
				player.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return;
			}
			player.getPA().spellTeleport(3426, 2927, 0, false);
			break;
		
		case 13117:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(4);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13118:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(2);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13119:
		case 13120:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(1);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
			
		case 13111:
			if (player.getRechargeItems().useItem(itemId)) {
				player.getPA().spellTeleport(3236, 3946, 0, false);
			}
			break;
		
		case 13241:
		case 13243:
			Degrade.checkRemaining(player, itemId);
			break;
		
		case 13280:
		case 13337:
		case 21898:
			player.getDH().sendDialogues(76, 1);
			break;

			case 11802:
			case 11804:
			case 11806:
			case 11808:
				Godswords.dismantle(player,itemId);
				break;

		case 13226:
			player.getHerbSack().withdrawAll();
			break;
			
		case 12020:
			player.getGemBag().check();
			break;
			
		case 5509:
			Pouches.check(player, Pouch.forId(itemId), itemId, 0);
			break;
		case 5510:
			Pouches.check(player, Pouch.forId(itemId), itemId, 1);
			break;
		case 5512:
			Pouches.check(player, Pouch.forId(itemId), itemId, 2);
			break;
		case 12904:
			player.sendMessage("The toxic staff of the dead has " + player.getToxicStaffOfTheDeadCharge() + " charges remaining.");
			break;
		case 13199:
		case 13197:
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charges remaining.");
			break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? player.getTridentCharge() : player.getToxicTridentCharge();
			player.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
			break;
		case 12926:
			def = ItemDefinition.forId(player.getToxicBlowpipeAmmo());
			player.sendMessage("The blowpipe has " + player.getToxicBlowpipeAmmoAmount() + " " + def.getName() + " and " + player.getToxicBlowpipeCharge() + " charge remaining.");
			break;

		case 12931:
			def = ItemDefinition.forId(itemId);
			if (def == null) {
				return;
			}
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charge remaining.");
			break;
		case 8901:
			player.getPA().assembleSlayerHelmet();
			break;
		case 19675:
			//if (player.getArcLightCharge() >= 1000) {
			//	player.getItems().addItem(19677, 3);
			//	player.getDH().sendStatement("You lost some of the ancient shards in the process!");
			//} else {
			//	player.getDH().sendStatement("Your Arclight's charge was too low to refund shards.");
			//}
		//	player.getItems().deleteItem(19675, 1);
		//	player.getItems().addItem(6746, 1);
		//	player.setArcLightCharge(0);
			player.getDH().sendItemStatement("Your arc light has "+player.getArcLightCharge()+ " charges.",19675);
			break;
		case 11283:
		case 11285:
		case 11284:
			player.sendMessage("Your dragonfire shield currently has " + player.getDragonfireShieldCharge() + " charges.");
			break;
		case 4155:
			player.getDH().sendDialogues(950, 6797);
			break;
		default:
			if (player.getRights().isOrInherits(Right.OWNER)) {
				Misc.println("[DEBUG] Item Option #2-> Item id: " + itemId);
			}
			break;
		}

	}

}
