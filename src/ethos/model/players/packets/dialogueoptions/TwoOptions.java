package ethos.model.players.packets.dialogueoptions;

import java.util.Map.Entry;
import java.util.List;
import java.util.Optional;

import ethos.model.content.Referrals;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.npcs.bosses.Hunllef;
import org.apache.commons.lang3.text.WordUtils;

import ethos.Config;
import ethos.Server;
import ethos.database.impl.Donation;
import ethos.model.content.LootValue;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.barrows.RoomLocation;
import ethos.model.content.dailytasks.TaskTypes;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.content.wogw.Wogw;
import ethos.model.content.wogw.Wogwitems;
import ethos.model.items.GameItem;
import ethos.model.items.ItemCombination;
import ethos.model.minigames.bounty_hunter.BountyHunterEmblem;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PrayerScrolls;
import ethos.model.players.PlayerAssistant.PointExchange;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.pkdistrict.District;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.crafting.SpinMaterial;
import ethos.model.players.skills.herblore.UnfCreator;
import ethos.util.Misc;

/*
 * @author Matt
 * Two Option Dialogue actions
 */

public class TwoOptions {
	/*
	 * Handles all first options on 'Two option' dialogues.
	 */
	public static void handleOption1(Player c) {  //see This handles option 1 if u scroll down ull see the section two
		Player other = c.getItemOnPlayer();

		switch (c.dialogueAction) {
		case 565:
			if (!c.getItems().playerHasItem(995, 5000000)) {
	         	c.getDH().sendStatement("@red@You must have atleast 5M cash to do this.");	
	         	return;
			}
			if (c.insurance == false) {
				c.getItems().deleteItem(995, 5000000);
				c.insurance = true;
				c.getPA().closeAllWindows();
			} else if (c.insurance == true) {
	         	c.getDH().sendStatement("@red@Your pet insurance is still active.");	
			}
			break;
		case 481:
			List<String> CompletedChristmasEvent = Referrals.getUsedReferrals();
			long CompleteldChristmasEvent = CompletedChristmasEvent.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (CompleteldChristmasEvent >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already completed the christmas event.");
				return;
				}
			if (c.getItems().playerHasItem(2209) && (c.getItems().playerHasItem(22719) && (c.getItems().playerHasItem(2520)
					&& (c.getItems().playerHasItem(4083))))) {
				c.getDH().sendDialogues(477, 8906);
				c.sendMessage("You have received from special items from santa's little helper.");
				c.getItems().deleteItem(22719, 1);
				c.getItems().deleteItem(20834, 1);
				c.getItems().deleteItem(2520, 1);
				c.getItems().deleteItem(4083, 1);
				c.getItems().addItem(6199, 2);
				PlayerHandler.executeGlobalMessage("@blu@[EVENT]@pur@"+c.playerName+" has just completed the 2019 @red@christmas event");
				c.christmasEvent = true;
				return;
			}
			c.getDH().sendDialogues(478, 8906);
			break;
		case 1004:
			c.getPA().startTeleport(2715, 10207, 0, "modern", false);
			c.sendMessage("@red@The Icelords have stolen santa`s presents.");
			c.sendMessage("@red@Retrieve the items stolen from the presents and return them.");
			c.sendMessage("@red@Toy horse, Candy Cane, Chocchip crunchies and santa's sled .");
			c.nextChat = -1;
			break;
		case 354:
		if (c.getItems().playerHasItem(12932, 1)) {
			c.getItems().deleteItem(12932, 1);
			c.getItems().addItem(12934, 20000);
			c.sendMessage("You have dismantled your Magic Fang for 20,000 zulrah scales.");
			c.getPA().closeAllWindows();
		} else if (c.getItems().playerHasItem(12922, 1)) {
			c.getItems().deleteItem(12922, 1);
			c.getItems().addItem(12934, 20000);
			c.sendMessage("You have dismantled your Tanzanite Fang for 20,000 zulrah scales.");
			c.getPA().closeAllWindows();
		} else if (c.getItems().playerHasItem(12929, 1)) {
			c.getItems().deleteItem(12929, 1);
			c.getItems().addItem(12934, 20000);
			c.sendMessage("You have dismantled your Serpentine Helm for 20,000 zulrah scales.");
			c.getPA().closeAllWindows();
		} else if (c.getItems().playerHasItem(12927, 1)) {
			c.getItems().deleteItem(12927, 1);
			c.getItems().addItem(12934, 20000);
			c.sendMessage("You have dismantled your Serpentine visage for 20,000 zulrah scales.");
			c.getPA().closeAllWindows();
		} else if (c.getItems().playerHasItem(12924, 1)) {
			c.getItems().deleteItem(12924, 1);
			c.getItems().addItem(12934, 20000);
			c.sendMessage("You have dismantled your Toxic Blowpipe for 20,000 zulrah scales.");
			c.getPA().closeAllWindows();
			}	
			break;
		case 793:
			c.sendMessage("@blu@This cave is currenetly in developement.");
			break;
		case 381:
			Hunllef.start(c);
			break;
		case 362:
			if (c.getItems().playerHasItem(12007)) {
				c.getItems().deleteItem(12007, 1);
				c.pvmp+=500;
				c.getQuestTab().updateInformationTab();
				c.sendMessage("@blu@You have received @red@500 @blu@Pvm Points for your jar of dirt.");
				c.getPA().closeAllWindows();
			} else if (c.getItems().playerHasItem(22106)) {
				c.getItems().deleteItem(22106, 1);
				c.pvmp+=500;
				c.getQuestTab().updateInformationTab();
				c.sendMessage("@blu@You have received @red@500 @blu@Pvm Points for your jar of decay.");
				c.getPA().closeAllWindows();
			} else if (c.getItems().playerHasItem(12936)) {
				c.getItems().deleteItem(12936, 1);
				c.pvmp+=500;
				c.getQuestTab().updateInformationTab();
				c.sendMessage("@blu@You have received @red@500 @blu@Pvm Points for your jar of swamp.");
				c.getPA().closeAllWindows();
			}
			break;
		case 346:
					if (c.rigour == false && c.getItems().playerHasItem(21034)) {
							c.getItems().deleteItem(21034, 1);
							PrayerScrolls.giveRigourPrayer(c);
							c.getPA().closeAllWindows();
					} else {
						if (c.rigour == true) {
							c.sendMessage("You already have unlocked the rigour prayer");
							c.getPA().closeAllWindows();
					}
		}
					break;
		case 348:
			if (c.augury == false && c.getItems().playerHasItem(21079)) {
					c.getItems().deleteItem(21079, 1);
					PrayerScrolls.giveAuguryPrayer(c);
					c.getPA().closeAllWindows();
			} else {
				if (c.rigour == c.augury) {
					c.sendMessage("You already have unlocked the augury prayer");
					c.getPA().closeAllWindows();
				}
			}
			break;
		case 783:
			if (c.getItems().playerHasItem(995, 1000000)) {
				c.getItems().deleteItem(995, 1000000);
				c.getItems().addItem(10498, 1);
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.ATTRACTOR);
				c.getDH().sendDialogues(781, 386);
				return;
			}
			c.getDH().sendDialogues(780, 386);
			break;
		case 335:
			if (c.getItems().playerHasItem(22322, 1)) {
				c.getItems().deleteItem(22322, 1);
				c.getItems().addItem(12954, 1);
                c.sendMessage("@red@You have dismantled your avernic defender.");
                c.getPA().closeAllWindows();

                return;
			}
			c.sendMessage("You need to have an avernic defender hilt to do this.");
			break;
		case 459:
			AgilityHandler.delayFade(c, "NONE", 2421, 3781, 0, "You board the boat...", "And end up in Jatizso",
					3);
			c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TRAVEL_JATIZSOT);
            c.getPA().closeAllWindows();
			break;
		case 983:
			c.sendMessage("You have total of @red@"+c.tWin+" Tournament Wins");
			c.sendMessage("Your current winstreak is @red@"+c.streak+"");
            c.getPA().closeAllWindows();
			break;
		case 859:
			switch (c.destroyingItemId) {
			case 12791:
				c.getItems().deleteItem(12791, 1);
                c.sendMessage("@red@You have destroyed your Rune Pouch.");
                c.getPA().closeAllWindows();
				break;
			case LootingBag.LOOTING_BAG_OPEN:
				c.getItems().deleteItem(LootingBag.LOOTING_BAG_OPEN, 1);
				c.sendMessage("@red@You have destroyed your Looting Bag.");
				c.getPA().closeAllWindows();
			case LootingBag.LOOTING_BAG:
    			c.getItems().deleteItem(LootingBag.LOOTING_BAG, 1);
                c.sendMessage("@red@You have destroyed your Looting Bag.");
                c.getPA().closeAllWindows();
                 break;
			case 5509:
				c.getItems().deleteItem(5509, 1);
	            c.sendMessage("@red@You have destroyed your pouch.");
	            c.getPA().closeAllWindows();
	            break;
			case 5510:
				c.getItems().deleteItem(5510, 1);
	            c.sendMessage("@red@You have destroyed your pouch.");
	            c.getPA().closeAllWindows();
	            break;
			case 5512:
				c.getItems().deleteItem(5512, 1);
	            c.sendMessage("@red@You have destroyed your pouch.");
	            c.getPA().closeAllWindows();
	            break;
			case 5514:
				c.getItems().deleteItem(5514, 1);
	            c.sendMessage("@red@You have destroyed your pouch.");
	            c.getPA().closeAllWindows();
	            break;
			case 6819:
				c.getItems().deleteItem(6819, 1);
	            c.sendMessage("@red@You have destroyed your pouch.");
	            c.getPA().closeAllWindows();
	            break;
			case 13199:
				c.setSerpentineHelmCharge(0); //this just sets it for all helms strangely.
				c.getItems().deleteItem(13199, 1);
	            c.sendMessage("@red@You have destroyed your Magma helm.");
	            c.getPA().closeAllWindows();
	            break;
			case 12931:
				c.setSerpentineHelmCharge(0); //this just sets it for all helms strangely.
				c.getItems().deleteItem(12931, 1);
	            c.sendMessage("@red@You have destroyed your Serpentine helm.");
	            c.getPA().closeAllWindows();
	            break;
			case 13197:
				c.setSerpentineHelmCharge(0); //this just sets it for all helms strangely.
				c.getItems().deleteItem(13197, 1);
	            c.sendMessage("@red@You have destroyed your Serpentine helm.");
	            c.getPA().closeAllWindows();
	            break;
			}
			c.destroyingItemId = 0;
			break;
		case 977:
			 if (c.getItems().freeSlots() < 1) {
                 c.sendMessage("You need atleast one free slots to use this command.");
                 c.getPA().closeAllWindows();
                 return;
             }
			 new Thread(new Donation(c)).start();
			 c.sendMessage("@red@Checking your reward...");
			 c.sendMessage("@red@We have succesfully scanned your named through the database.");
             c.getPA().closeAllWindows();
			break;
		case 943:
    		c.getPA().startTeleport(3080, 3495, 0, "modern", false);
    		c.sendMessage("@red@You have teleported to Nieve");
			break;
		case 689:
			  if (c.getItems().playerHasItem(2403, 1)) {
                  c.getItems().deleteItem(2403, 1);
                  c.gfx100(263);
                  c.donatorPoints += 10;
                  c.amDonated += 10;
                  c.sendMessage("Thank you for donating! 10$ has been added to your total credit.");
                  c.updateRank();
                  c.getPA().closeAllWindows();
			  } else if (c.getItems().playerHasItem(2396, 1)) {
                    c.getItems().deleteItem(2396, 1);
                    c.gfx100(263);
                    c.donatorPoints += 25;
                    c.amDonated += 25;
                    c.sendMessage("Thank you for donating! 25$ has been added to your total credit.");
                    c.updateRank();
                    c.getPA().closeAllWindows();
			  } else if (c.getItems().playerHasItem(786, 1)) {
                  c.getItems().deleteItem(786, 1);
                  c.gfx100(263);
                  c.donatorPoints += 50;
                  c.amDonated += 50;
                  c.sendMessage("Thank you for donating! 50$ has been added to your total credit.");
                  c.updateRank();
                  c.getPA().closeAllWindows();
			  } else if (c.getItems().playerHasItem(761, 1)) {
                  c.getItems().deleteItem(761, 1);
                  c.gfx100(263);
                  c.donatorPoints += 100;
                  c.amDonated += 100;
                  c.sendMessage("Thank you for donating! 100$ has been added to your total credit.");
                  c.updateRank();
                  c.getPA().closeAllWindows();
			  } else if (c.getItems().playerHasItem(607, 1)) {
                      c.getItems().deleteItem(607, 1);
                      c.gfx100(263);
                      c.donatorPoints += 250;
                      c.amDonated += 250;
                      c.sendMessage("Thank you for donating! 250$ has been added to your total credit.");
                      c.updateRank();
                      c.getPA().closeAllWindows();
			  } else if (c.getItems().playerHasItem(608, 1)) {
                  c.getItems().deleteItem(608, 1);
                  c.gfx100(263);
                  c.donatorPoints += 500;
                  c.amDonated += 500;
                  c.sendMessage("Thank you for donating! 500$ has been added to your total credit.");
                  c.updateRank();
                  c.getPA().closeAllWindows();
                }
			  if (c.amDonated >= 10 && c.amDonated <= 49) {
					c.sendMessage("@blu@Your next donator rank is @red@Super Donator");	
			 } else if (c.amDonated >= 50 && c.amDonated <= 99) {
					c.sendMessage("@blu@Your next donator rank is @red@Extreme Donator");	
			 } else if (c.amDonated >= 100 && c.amDonated <= 199) {
					c.sendMessage("@blu@Your next donator rank is @red@Ultra Donator");	
			 } else if (c.amDonated >= 200 && c.amDonated <= 299) {
					c.sendMessage("@blu@Your next donator rank is @red@Legendary Donator");	
			 } else if (c.amDonated >= 300 && c.amDonated <= 499) {
					c.sendMessage("@blu@Your next donator rank is @red@Diamond Club");	
			 } else if (c.amDonated >= 500 && c.amDonated <= 999) {
					c.sendMessage("@blu@Your next donator rank is @red@Oynx Club");	
			 } else if (c.amDonated >= 100 && c.amDonated <= 2499) {
					c.sendMessage("@blu@Your next donator rank is @red@Platinum Donator");	
			 } else if (c.amDonated >=2500 && c.amDonated <= 4999) {
					c.sendMessage("@blu@Your next donator rank is @red@Divine Donator");	
			 }
			break;
			case 450:
				c.getPA().removeAllItems();
				c.sendMessage("You empty your inventory.");
				c.getPA().closeAllWindows();
				break;
		case 161: //this id is the dialogue
			c.getDH().sendDialogues(162, 1603);
			break;
		case 156:
			c.getDH().sendDialogues(157, 1603);
			break;
		case 901:
			c.dailyEnabled = true;
			c.getDH().sendDialogues(904, 1909);
			break;
		case 903:
			if (c.dailyEnabled == true) {
				c.playerChoice = TaskTypes.PVM;
				// c.dailyChoice = 1;
				c.getDH().sendDialogues(906, 1909);
			} else {
				c.sendMessage("You must enable Daily Tasks first!");
				c.getPA().closeAllWindows();
			}
			break;
		case 784:
			c.getDH().sendDialogues(785, -1);
			break;
		case 786:
			for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
				if (c.getItems().playerHasItem(t.getItemId(), 1)) {
					c.wogwOption = "experience";
					c.wogwDonationAmount = c.wellItemPrice;
					Wogw.donateItems(c, (int) c.wogwDonationAmount);
					c.getItems().deleteItem(c.wellItem, 1);
					c.wellItemPrice = -1;
					c.wellItem = -1;
					c.getPA().closeAllWindows();
				}
			}
			break;
		case 4006:
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (other.inWild() || other.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(other)) {
				return;
			}
			if (c.getItems().playerHasItem(2697, 1)) {
				c.getItems().deleteItem(2697, 1);
				c.gfx100(263);
				other.gfx100(263);
				other.amDonated += 10;
				other.donatorPoints += 10;
				c.amDonated -= 10;
				other.sendMessage("$10 has been added to your total amount donated.");
				c.sendMessage("$10 has been removed from your amount donated and given to the other player.");
				c.updateRank();
				other.updateRank();
				c.getPA().closeAllWindows();
			}
			break;
		case 4007:
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (other.inWild() || other.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(other)) {
				return;
			}
			if (c.getItems().playerHasItem(2698, 1)) {
				c.getItems().deleteItem(2698, 1);
				c.gfx100(263);
				other.gfx100(263);
				other.amDonated += 50;
				other.donatorPoints += 50;
				c.amDonated -= 50;
				other.sendMessage("$50 has been added to your total amount donated.");
				c.sendMessage("$50 has been removed from your amount donated and given to the other player.");
				c.updateRank();
				other.updateRank();
				c.getPA().closeAllWindows();
			}
			break;
		case 4008:
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (other.inWild() || other.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(other)) {
				return;
			}
			if (c.getItems().playerHasItem(2699, 1)) {
				c.getItems().deleteItem(2699, 1);
				c.gfx100(263);
				other.gfx100(263);
				other.amDonated += 150;
				other.donatorPoints += 150;
				c.amDonated -= 150;
				other.sendMessage("$150 has been added to your total amount donated.");
				c.sendMessage("$150 has been removed from your amount donated and given to the other player.");
				c.updateRank();
				other.updateRank();
				c.getPA().closeAllWindows();
			}
			break;
		case 4009:
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (other.inWild() || other.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(other)) {
				return;
			}
			if (c.getItems().playerHasItem(2700, 1)) {
				c.getItems().deleteItem(2700, 1);
				c.gfx100(263);
				other.gfx100(263);
				other.amDonated += 300;
				other.donatorPoints += 300;
				c.amDonated -= 300;
				other.sendMessage("$300 has been added to your total amount donated.");
				c.sendMessage("$300 has been removed from your amount donated and given to the other player.");
				c.updateRank();
				other.updateRank();
				c.getPA().closeAllWindows();
			}
			break;
		case 4005:
			c.getShops().openShop(9);
			break;
		case 811:
			GameItem item = new GameItem(c.unfPotHerb);
			UnfCreator.makeUnfinishedPotion(c, item);
			break;

		case 813:
			District.stage(c, c.inClanWars() || c.inClanWarsSafe() ? "end" : "start");
			c.getPA().closeAllWindows();
			break;

		case 819:
			c.getDH().sendDialogues(820, 822);
			break;
		case 2647:
			c.getDH().sendDialogues(3647, 311);
			break;
		case 673:
			c.getDH().sendDialogues(673, 311);
			break;

		case 70300:
			c.getPA().movePlayer(1664, 10050, 0);
			c.sendMessage("Welcome to the Catacombs of Kourend.");
			c.getPA().closeAllWindows();
			break;

		case 703:
			c.getDH().sendDialogues(704, 822);
			break;
		case 705:
			c.getDH().sendDialogues(706, 822);
			break;
		case 550:
			c.getDH().sendDialogues(539, c.npcType);
			break;
		case 64: // Buy a kittem
			if (c.getDiaryManager().getVarrockDiary().hasDone(VarrockDiaryEntry.PURCHASE_KITTEN)) {
				c.sendMessage("You cannot purchase another kitten.");
				return;
			}
			int[] kittens = { 1555, 1556, 1557, 1558, 1559, 1560 };
			int kitten = Misc.random(kittens.length - 1);
			if (c.getItems().playerHasItem(995, 15000)) {
				c.getItems().deleteItem(995, 15000);
				c.getItems().addItem(kittens[kitten], 1);
				c.sendMessage("You've successfully purchased a kitten!");
				c.getPA().removeAllWindows();
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PURCHASE_KITTEN);
			}
			break;

		case 65:
			c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.GRAND_TREE_TELEPORT);
			switch (c.getDH().tree) {
			case "village":
				c.getPA().startTeleport(2461, 3444, 0, "modern", false); // Stronghold
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.SPIRIT_TREE_WEST);
				break;

			case "stronghold":
				c.getPA().startTeleport(2542, 3169, 0, "modern", false); // Village
				break;

			case "grand_exchange":
				c.getPA().startTeleport(2542, 3169, 0, "modern", false); // Village
				break;
			}
			break;

		case 72: // Attempt for pet
			c.getFightCave().gamble();
			break;

		case 75: // Remove bigger boss tasks
			c.getSlayer().setBiggerBossTasks(false);
			c.sendMessage("You will no longer get extended boss tasks.");
			c.getPA().removeAllWindows();
			break;

		case 10:
			c.getOutStream().createFrame(27);
			c.tablet = 1;
			break;

		case 40:
			// c.sendMessage("Bowstring");
			c.getPA().removeAllWindows();
			SpinMaterial.getInstance().spin(c, SpinMaterial.Material.FLAX.getRequiredItem());
			break;

		case 80:
			if (c.getItems().freeSlots() == 0) {
				c.sendMessage("You need at least one free slot to purchase this item reward.");
				c.getPA().closeAllWindows();
				return;
			}
			if (!c.getItems().playerHasItem(8839) && !c.getItems().playerHasItem(8840)) {	
				c.sendMessage("You need atleast one void peice to upgrade.");
				c.getPA().closeAllWindows();
				return;
			}
		 if (c.getItems().playerHasItem(8839) && !c.getItems().playerHasItem(8840)) {	
				if (c.pcPoints < 200) {
					c.sendMessage("You need a total of 200 pest control points to complete this upgrade.");
					c.getPA().closeAllWindows();
					return;
				}
				c.pcPoints -= 200;
				c.refreshQuestTab(3);
				c.getItems().replaceItem(c, 8839, 13072);
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.UPGRADE_VOID);
				c.sendMessage("You have received a Elite Void Top in exchange for 200 pc points.");
				c.getPA().closeAllWindows();
			} else if (c.getItems().playerHasItem(8840) && !c.getItems().playerHasItem(8839)) {	
				if (c.pcPoints < 200) {
					c.sendMessage("You need a total of 200 pest control points to complete this upgrade.");
					c.getPA().closeAllWindows();
					return;
				}
				c.pcPoints -= 200;
				c.refreshQuestTab(3);
				c.getItems().replaceItem(c, 8840, 13073);
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.UPGRADE_VOID);
				c.sendMessage("You have received a Elite Void robe in exchange for 200 pc points.");
				c.getPA().closeAllWindows();
		} else if (c.getItems().playerHasItem(8840) && c.getItems().playerHasItem(8839)) {	
			if (c.pcPoints < 400) {
				c.sendMessage("You need a total of 400 pest control points to complete this entire set upgrade.");
				c.getPA().closeAllWindows();
				return;
			}
			c.pcPoints -= 400;
			c.refreshQuestTab(3);
			c.getItems().replaceItem(c, 8840, 13073);
			c.getItems().replaceItem(c, 8839, 13072);
			c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.UPGRADE_VOID);
			c.sendMessage("You have received an elite void set in exchange for 400 pc points.");
			c.getPA().closeAllWindows();
			}
			break;

		case 11824:
			if (!c.getItems().playerHasItem(11824))
				return;
			if (!c.getItems().playerHasItem(995, 10_000_000)) {
				c.sendMessage("You do not have 10M Coins to do this.");
				c.getDH().sendStatement("You need 10m gp in order to do this.");
				c.nextChat = -1;
				return;
			}
			c.getItems().deleteItem2(11824, 1);
			c.getItems().deleteItem2(995, 10_000_000);
			c.getItems().addItem(11889, 1);
			c.getDH().sendItemStatement("Otto creates a Zamorakian Hasta.", 11889);
			c.nextChat = -1;
			break;
		case 11156: 
			if (c.getSlayer().getTask().isPresent()) {
				c.getDH().sendStatement("Please finish your current task first.");		
				return;
			}
				if (!c.getItems().playerHasItem(995, 15_000_000)) {
					c.getDH().sendStatement("Come back when you've got the 15m ready.");
					return;
				}
				c.getItems().deleteItem2(995, 15_000_000);
				c.getSlayer().createNewTask(8623);
				c.getDH().sendNpcChat("You have been assigned "+ c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName());
		        c.nextChat = -1;
		        break;//hydranpc
		case 11966: 
			if (c.playerLevel[18] < 87) {
				c.sendMessage("You need a Slayer level of 87 to kill these.");
				return;
			}
			if (c.getSlayer().getTask().isPresent()) {
				c.getDH().sendStatement("Please finish your current task first.");		
				return;
			}
				if (!c.getItems().playerHasItem(995, 5_000_000)) {
					c.getDH().sendStatement("Come back when you've got the 5m ready.");
					return;
				}
				c.getItems().deleteItem2(995, 5_000_000);
				c.getSlayer().createNewTask(603);
				c.getDH().sendNpcChat("You have been assigned "+ c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName());
		        c.nextChat = -1;
		        break;
		case 10956: 
			if (c.playerLevel[18] < 70) {
				c.getDH().sendStatement("You need a Slayer level of 70 to use this slayer master.");
				return;
			}
			if (c.getSlayer().getTask().isPresent()) {
				c.getDH().sendStatement("Please finish your current task first.");		
				return;
			}

			if (!c.getItems().playerHasItem(995, 1_000_000)) {
				c.getDH().sendStatement("Come back when you've got the 1m ready.");
				return;
				}
				c.getItems().deleteItem2(995, 1_000_000);

				c.getSlayer().createNewTask(8761);
				c.getDH().sendNpcChat("You have been assigned "+ c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName());

		        c.nextChat = -1;
		        break;

		case 11889:
			if (!c.getItems().playerHasItem(11889))
				return;
			if (!c.getItems().playerHasItem(995, 5_000_000)) {
				c.sendMessage("You do not have 5M Coins to do this.");
				c.getDH().sendStatement("You need 5m gp in order to do this.");
				c.nextChat = -1;
				return;
			}
			c.getItems().deleteItem2(11889, 1);
			c.getItems().deleteItem2(995, 5_000_000);
			c.getItems().addItem(11824, 1);
			c.getDH().sendItemStatement("Otto creates a Zamorakian Spear.", 11824);
			c.nextChat = -1;
			break;

		case 94:
			c.getDH().sendDialogues(97, c.npcType);
			break;

		case 103:
			SkillcapePerks.purchaseMaxCape(c);
			break;

		case 1391:
			c.getDH().sendDialogues(1400, c.npcType);
			break;

		case 1392:
			c.getDH().sendDialogues(1393, c.npcType);
			break;

		case 66:
			Degrade.repairCrystalBow(c, 4207);
			c.getPA().closeAllWindows();
			break;

		case 68:
			c.dropRateInKills = true;
			Server.getDropManager().open2(c);
			c.sendMessage("Now viewing drop-rates in 1/kills form");
			break;
		}
		if (c.dialogueAction == 7286) {

		}
		if (c.dialogueAction == 7286) {
			if (System.currentTimeMillis() - c.cerbDelay > 5000) {
				Skotizo skotizo = c.createSkotizoInstance();

				/*
				 * if (c.getSkotizoLostItems().size() > 0) { c.getDH().sendDialogues(642, 5870);
				 * c.nextChat = -1; return; }
				 */

				if (skotizo == null) {
					c.sendMessage("We are unable to allow you in at the moment.");
					c.sendMessage("Too many players.");
					return;
				}

				if (Server.getEventHandler().isRunning(c, "skotizo")) {
					c.sendMessage("You're about to fight start the fight, please wait.");
					return;
				}
				c.getSkotizo().init();
				c.getItems().deleteItem(19685, 1);
				c.getPA().closeAllWindows();
				c.nextChat = -1;
				c.cerbDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("Please wait a few seconds between clicks.");
			}
		}
		if (c.dialogueAction == 29) {
			c.dialogueAction = -1;
			c.getPA().movePlayer(RoomLocation.getRandomSpawn());
			c.getPA().removeAllWindows();
			return;
		}
		if (c.dialogueAction == 132) {
			c.getDH().sendDialogues(655, 311);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 133) {
			c.getDH().sendDialogues(656, 311);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 130) {
			if (c.talkingNpc == 5870 && c.getCerberusLostItems().size() > 0) {
				c.getCerberusLostItems().retain();
			} else if (c.talkingNpc == 7283 && c.getSkotizoLostItems().size() > 0) {
				c.getSkotizoLostItems().retain();
			} else {
				if (c.getZulrahLostItems().size() > 0) {
					c.getZulrahLostItems().retain();
				}
			}
			return;
		}
		if (c.dialogueAction == 127) {
			int price = c.getRechargeItems().hasItem(13109) ? 40_000
					: c.getRechargeItems().hasItem(13110) ? 25_000 : c.getRechargeItems().hasItem(13111) ? -1 : 50_000;
			if (c.absX == 3184 && c.absY == 3945) {
				if (c.getItems().playerHasItem(995, price)) {
					c.getPA().movePlayer(3184, 3944, 0);
					c.getItems().deleteItem2(995, price);
					c.getPA().removeAllWindows();
				} else {
					c.getDH().sendStatement("You need at least 50,000 gp to enter this area.");
				}
			}
			c.dialogueAction = -1;
			c.nextChat = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 12700) {
			int price = c.getRechargeItems().hasItem(13109) ? 40_000
					: c.getRechargeItems().hasItem(13110) ? 25_000 : c.getRechargeItems().hasItem(13111) ? -1 : 50_000;
			if (c.absX == 1502 && c.absY == 3838) {
				if (c.getItems().playerHasItem(995, price)) {
					c.getPA().movePlayer(1502, 3840, 0);
					c.getItems().deleteItem2(995, price);
					c.getPA().removeAllWindows();
				} else {
					c.getDH().sendStatement("You need at least 50,000 gp to enter this area.");
				}
			}
			c.dialogueAction = -1;
			c.nextChat = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 947) {
			c.getShops().openShop(113);
			c.dialogueAction = -1;
		}
		if (c.teleAction == 1337) {
			c.getPA().startTeleport(1504, 3419, 0, "modern", false);
		}
		if (c.dialogueAction == 125) {
			if (c.getItems().playerHasItem(8851, 200) || SkillcapePerks.ATTACK.isWearing(c)
					|| SkillcapePerks.isWearingMaxCape(c)) {
				c.getPA().movePlayer(2847, 3540, 2);
				c.getPA().removeAllWindows();
				c.getWarriorsGuild().cycle();
			} else {
				c.getDH().sendNpcChat2("You need atleast 200 warrior guild tokens.",
						"You can get some by operating the armour animator.", 4289, "Kamfreena");
				c.nextChat = 0;
			}
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 124) {
			if (!c.getPA().canTeleport("")) {
				c.getPA().removeAllWindows();
				return;
			}
			c.getZulrahEvent().initialize();
			return;
		}
		if (c.dialogueAction == -1 && c.getCurrentCombination().isPresent()) {
			ItemCombination combination = c.getCurrentCombination().get();
			if (combination.isCombinable(c)) {
				combination.combine(c);
			} else {
				c.getDH().sendStatement("You don't have all the items you need for this combination.");
				c.nextChat = -1;
				c.setCurrentCombination(Optional.empty());
			}
			return;
		}
		if (c.dialogueAction == 555) {
			return;
		}
		if (c.dialogueAction == 3308) {
			c.getSlayer().setTask(Optional.empty());
			c.getSlayer().setTaskAmount(0);


	        c.getSlayer().createNewTask(401); //changed this line
	        c.getSlayer().setConsecutiveTasks(0);
	        c.sendMessage("@red@Your task has been canceled, check your new easy task with ::task");
	        c.getDH().sendNpcChat("Your task and streak has been reset,","check your new easy task with ::task");
	        return;
				}
		if (c.dialogueAction == 3306) {
			c.getSlayer().setTask(Optional.empty());
			c.getSlayer().setTaskAmount(0);

	        c.getSlayer().createNewTask(401); //changed this line
	        c.getSlayer().setConsecutiveTasks(0);
	        c.sendMessage("@red@Your task has been canceled, check your new easy task with ::task");
	        c.getDH().sendNpcChat("Your task and streak has been reset,","check your new easy task with ::task");
	        return;
				}

		if (c.dialogueAction == 115) {
			if (c.getItems().playerHasItem(12526) && c.getItems().playerHasItem(6585)) {
				c.getItems().deleteItem2(12526, 1);
				c.getItems().deleteItem2(6585, 1);
				c.getItems().addItem(12436, 1);
				c.getDH().sendDialogues(582, -1);
			} else if (c.getItems().playerHasItem(20062) && c.getItems().playerHasItem(19553)) {
				c.getItems().deleteItem2(20062, 1);
				c.getItems().deleteItem2(19553, 1);
				c.getItems().addItem(20366, 1);
				c.getDH().sendDialogues(582, -1);
			} else if (c.getItems().playerHasItem(22246) && c.getItems().playerHasItem(19547)) {
				c.getItems().deleteItem2(22246, 1);
				c.getItems().deleteItem2(19547, 1);
				c.getItems().addItem(22249, 1);
				c.getDH().sendDialogues(582, -1);
			} else if (c.getItems().playerHasItem(20065) && c.getItems().playerHasItem(12002)) {
				c.getItems().deleteItem2(20065, 1);
				c.getItems().deleteItem2(12002, 1);
				c.getItems().addItem(19720, 1);
				c.getDH().sendDialogues(582, -1);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 114) {
			c.getDH().sendDialogues(579, -1);
			return;
		}
		if (c.dialogueAction == 110) {
			if (c.getItems().playerHasItem(11235) && c.getItems().playerHasItem(12757)) {
				c.getItems().deleteItem2(11235, 1);
				c.getItems().deleteItem2(12757, 1);
				c.getItems().addItem(12766, 1);
				c.getDH().sendDialogues(568, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 111) {
			if (c.getItems().playerHasItem(11235) && c.getItems().playerHasItem(12759)) {
				c.getItems().deleteItem2(11235, 1);
				c.getItems().deleteItem2(12759, 1);
				c.getItems().addItem(12765, 1);
				c.getDH().sendDialogues(571, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 112) {
			if (c.getItems().playerHasItem(11235) && c.getItems().playerHasItem(12761)) {
				c.getItems().deleteItem2(11235, 1);
				c.getItems().deleteItem2(12761, 1);
				c.getItems().addItem(12767, 1);
				c.getDH().sendDialogues(574, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 113) {
			if (c.getItems().playerHasItem(11235) && c.getItems().playerHasItem(12763)) {
				c.getItems().deleteItem2(11235, 1);
				c.getItems().deleteItem2(12763, 1);
				c.getItems().addItem(12768, 1);
				c.getDH().sendDialogues(577, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 109) {
			if (c.getItems().playerHasItem(4153) && c.getItems().playerHasItem(12849)) {
				c.getItems().deleteItem2(4153, 1);
				c.getItems().deleteItem2(12849, 1);
				c.getItems().addItem(12848, 1);
				c.getDH().sendDialogues(565, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 108) {
			if (c.getItems().playerHasItem(11924) && c.getItems().playerHasItem(12802)) {
				c.getItems().deleteItem2(11924, 1);
				c.getItems().deleteItem2(12802, 1);
				c.getItems().addItem(12806, 1);
				c.getDH().sendDialogues(560, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 107) {
			if (c.getItems().playerHasItem(11926) && c.getItems().playerHasItem(12802)) {
				c.getItems().deleteItem2(11926, 1);
				c.getItems().deleteItem2(12802, 1);
				c.getItems().addItem(12807, 1);
				c.getDH().sendDialogues(560, 315);
			} else {
				c.getPA().removeAllWindows();
			}
		}
		if (c.dialogueAction == 106) {
			int worth = c.getBH().getNetworthForEmblems();
			long total = (long) worth + c.getBH().getBounties();
			if (total > Integer.MAX_VALUE) {
				c.sendMessage("You have to spend some bounties before obtaining any more.");
				c.getPA().removeAllWindows();
				c.nextChat = -1;
				return;
			}
			if (worth > 0) {
				BountyHunterEmblem.EMBLEMS.forEach(emblem -> c.getItems().deleteItem2(emblem.getItemId(),
						c.getItems().getItemAmount(emblem.getItemId())));
				c.pkp += worth;
				c.sendMessage("You sold all of the emblems in your inventory for "
						+ Misc.insertCommas(Integer.toString(worth)) + " PKP points.");
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.MYSTERIOUS_EMBLEM);
				c.getDH().sendDialogues(557, 315);
			} else {
				c.nextChat = -1;
				c.getPA().closeAllWindows();
			}
			return;
		}
		if (c.dialogueAction == 105) {
			if (c.getItems().playerHasItem(12804) && c.getItems().playerHasItem(11838)) {
				c.getItems().deleteItem2(12804, 1);
				c.getItems().deleteItem2(11838, 1);
				c.getItems().addItem(12809, 1);
				c.getDH().sendDialogues(552, -1);
			} else {
				c.getPA().removeAllWindows();
			}
			c.dialogueAction = -1;
			c.nextChat = -1;
			return;
		}
		if (c.dialogueAction == 104) {
			c.getDH().sendDialogues(549, 315);
			c.dialogueAction = -1;
			return;
		}
		if (c.dialogueAction == 101) {
			c.getDH().sendDialogues(546, 315);
			c.dialogueAction = -1;
			return;
		}
		if (c.dialogueAction == 102) {
			c.getDH().sendDialogues(547, 315);
			c.dialogueAction = -1;
			return;
		}
		if (c.dialogueAction == 200) {
			c.getPA().exchangeItems(PointExchange.PK_POINTS, 2996, 1);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		} else if (c.dialogueAction == 201) {
			c.getDH().sendDialogues(503, -1);
			return;
		} else if (c.dialogueAction == 202 && c.getItems().playerHasItem(23933)) {
			c.getPA().exchangeItems(PointExchange.VOTE_POINTS, 23933, 1);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		} else if (c.dialogueAction == 202 && c.getItems().playerHasItem(1464)) { //old vote tickets
			c.getPA().exchangeItems(PointExchange.VOTE_POINTS, 1464, 1);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
			}
		if (c.dialogueAction == 2258) {
			c.getPA().startTeleport(3039, 4834, 0, "modern", false); // first click
			// teleports
			// second
			// click
			// open
			// shops
		}
		// if (c.dialogueAction == 12000) {
		// for (int i = 8144; i < 8195; i++) {
		// c.getPA().sendFrame126("", i);
		// }
		// long milliseconds = (long) c.playTime * 600;
		// long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
		// long hours = TimeUnit.MILLISECONDS.toHours(milliseconds -
		// TimeUnit.DAYS.toMillis(days));
		// long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds -
		// TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours));
		// long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds -
		// TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours) -
		// TimeUnit.MINUTES.toMillis(minutes));
		// String time = days + " days, " + hours + " hours, " + minutes + " minutes, "
		// + seconds + " seconds.";
		//
		// c.getPA().sendFrame126("@dre@Account Information for @blu@" + c.playerName +
		// "", 8144);
		// c.getPA().sendFrame126("", 8145);
		// c.getPA().sendFrame126("@blu@Donator Points@bla@ - " + c.donatorPoints + "",
		// 8150);
		// c.getPA().sendFrame126("@blu@Vote Points@bla@ - " + c.votePoints + "", 8149);
		// c.getPA().sendFrame126("@blu@Amount Donated@bla@ - " + c.amDonated + "",
		// 8151);
		// c.getPA().sendFrame126("@blu@PC Points@bla@ - " + c.pcPoints + "", 8152);
		// c.getPA().sendFrame126("@blu@Time Played: @bla@" + time, 8153);
		// c.getPA().sendFrame126("@blu@Slayer points: @bla@" +
		// c.getSlayer().getPoints(), 8154);
		// c.getPA().sendFrame126("@blu@Mage arena points: @bla@" + c.getArenaPoints(),
		// 8155);
		// c.getPA().sendFrame126("@blu@Consecutive tasks: @bla@" +
		// c.getSlayer().getConsecutiveTasks(), 8156);
		// c.getPA().showInterface(8134);
		// }
		if (c.dialogueAction == 4000) {
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2697, 1)) {
				c.getItems().deleteItem(2697, 1);
				c.gfx100(263);
				c.amDonated += 10;
				c.sendMessage("$10 has been added to your total amount donated.");
				c.updateRank();
				c.getPA().closeAllWindows();
			}
		}
		if (c.dialogueAction == 4001) {
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2698, 1)) {
				c.getItems().deleteItem(2698, 1);
				c.gfx100(263);
				c.amDonated += 50;
				c.sendMessage("$50 has been added to your total amount donated.");
				c.updateRank();
				c.getPA().closeAllWindows();
			}
		}
		if (c.dialogueAction == 4002) {
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2699, 1)) {
				c.getItems().deleteItem(2699, 1);
				c.gfx100(263);
				c.amDonated += 150;
				c.sendMessage("$150 has been added to your total amount donated.");
				c.updateRank();
				c.getPA().closeAllWindows();
			}
		}
		if (c.dialogueAction == 4003) {
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2700, 1)) {
				c.getItems().deleteItem(2700, 1);
				c.gfx100(263);
				c.amDonated += 300;
				c.sendMessage("$300 has been added to your total amount donated.");
				c.updateRank();
				c.getPA().closeAllWindows();
			}
		}
		if (c.dialogueAction == 4004) {
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getMode().isIronman() || c.getMode().isUltimateIronman() || c.getMode().isHCIronman()) {
				c.sendMessage("You are not allowed to do this on your game mode.");
				return;
			}
			if (c.getItems().playerHasItem(2701, 1)) {
				c.getItems().deleteItem(2701, 1);
				c.gfx100(263);
				c.playerTitle = "Gambler";
				c.getItems().addItemUnderAnyCircumstance(15098, 1);
				c.sendMessage("You are now a Gambler. A dice has been added to your bank!");
				c.getPA().closeAllWindows();
			}
		}
		if (c.dialogueAction == 206) {
			c.getItems().resetItems(3214);
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 2109) {
			if (c.absX >= 2438 && c.absX <= 2439 && c.absY >= 5168 && c.absY <= 5169) {
				c.getFightCave().create(1);
			}
		}
		if (c.dialogueAction == 113239) {
			if (c.inDuelArena()) {
				return;
			}
			c.getItems().addItem(557, 1000);
			c.getItems().addItem(560, 1000);
			c.getItems().addItem(9075, 1000);
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.newPlayerAct == 1) {
			// c.isNewPlayer = false;
			c.newPlayerAct = 0;
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern", false);
			c.getPA().removeAllWindows();
		}
		if (c.dialogueAction == 6) {
			c.sendMessage("Slayer will be enabled in some minutes.");
			// c.getSlayer().generateTask();
			// c.getPA().sendFrame126("@whi@Task:
			// @gre@"+Server.npcHandler.getNpcListName(c.slayerTask)+
			// " ", 7383);
			// c.getPA().closeAllWindows();
		}
		// if (c.dialogueAction == 29) {
		// if (c.isInBarrows() || c.isInBarrows2()) {
		// c.getBarrows().checkCoffins();
		// c.getPA().removeAllWindows();
		// return;
		// } else {
		// c.getPA().removeAllWindows();
		// c.sendMessage("@blu@You can only do this while you're at barrows,
		// fool.");
		// }
		// } else if (c.dialogueAction == 27) {
		// c.getBarrows().cantWalk = false;
		// c.getPA().removeAllWindows();
		// // c.getBarrowsChallenge().start();
		// return;
		if (c.dialogueAction == 25) {
			c.getDH().sendDialogues(26, 0);
			return;
		}
		if (c.dialogueAction == 162) {
			c.sendMessage("You successfully emptied your inventory.");
			c.getPA().removeAllItems();
			c.dialogueAction = 0;
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 508) {
			c.getDH().sendDialogues(1030, 925);
			return;
		}

		if (c.caOption2) {
			c.getDH().sendDialogues(106, c.npcType);
			c.caOption2 = false;
		}
		if (c.caOption2a) {
			c.getDH().sendDialogues(102, c.npcType);
			c.caOption2a = false;
		}

		if (c.dialogueAction == 1) {
			c.getDH().sendDialogues(38, -1);
		}
	}

	/*
	 * Handles all the 2nd options on 'Two option' dialogues.
	 */
	public static void handleOption2(Player c) { //under this code is the dialogues OpTion two codIng ooh okay, and its the same id

		switch (c.dialogueAction) {
		case 1004:
		case 565:
			c.getPA().closeAllWindows();
			break;
		case 354:
		case 356:
			c.getPA().closeAllWindows();
			break;
		case 80:
		case 793:
			c.getPA().closeAllWindows();
			break;
		case 381:
			c.getPA().closeAllWindows();
			break;
		case 346:
		case 362:
		case 348:
            c.getPA().closeAllWindows();
			break;
		case 459:
			c.getPA().movePlayer(2310, 3780, 0);
			c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TRAVEL_NEITIZNOT);
            c.getPA().closeAllWindows();
			break;
		case 983:
			c.sendMessage("Last winner was "+TourneyManager.WINNER+" and their current streak is "+c.streak+"!");
            c.getPA().closeAllWindows();
			break;
		case 859:
            c.getPA().closeAllWindows();
			break;
		case 977:
            c.getPA().closeAllWindows();
			break;
		case 943:
    		c.getPA().startTeleport(3109, 3514, 0, "modern", false);
			c.sendMessage("@red@You have teleported to Krystilia");
    		break;
		case 689:
			c.getPA().closeAllWindows();
			break;
		case 903:
			if (c.dailyEnabled == true) {
				c.playerChoice = TaskTypes.SKILLING;
				// c.dailyChoice = 2;
				c.getDH().sendDialogues(907, 1909);
			} else {
				c.sendMessage("You must enable Daily Tasks first!");
				c.getPA().closeAllWindows();
			}
			break;
			case 450:
				c.getPA().closeAllWindows();
			break;
		case 901:
			c.dailyEnabled = false;
			c.getDH().sendDialogues(905, 1909);
			break;
		case 263://lookback
			c.getPA().closeAllWindows();
			break;
		case 784:
			c.getDH().sendItemStatement("Your item is worth " + Misc.format((int) c.wellItemPrice) + " gp.",
					c.wellItem);
			break;
		case 11966:
			c.getPA().closeAllWindows();
			break;
		case 10956:
			c.getPA().closeAllWindows();
			break;
		case 11156:
			c.getPA().closeAllWindows();//hydra npc
			break;
		case 786:
			c.getPA().closeAllWindows();
			break;
		case 4005:
			c.updateRank();
			c.getPA().closeAllWindows();
			break;
		case 819:
			c.getDH().sendDialogues(821, 822);
			break;
		case 550:
			c.getDH().sendDialogues(551, c.npcType);
			break;
		case 703:
			c.getPA().closeAllWindows();
			break;
		case 705:
			c.getPA().closeAllWindows();
			break;
		case 65:
			switch (c.getDH().tree) {
			case "village":
				c.getPA().startTeleport(3183, 3508, 0, "modern", false); // Grand exchange
				break;

			case "stronghold":
				c.getPA().startTeleport(3183, 3508, 0, "modern", false); // Grand exchange
				break;

			case "grand_exchange":
				c.getPA().startTeleport(2461, 3444, 0, "modern", false); // Stronghold
				c.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.SPIRIT_TREE_WEST);
				break;
			}
			break;

		case 673:
			c.getDH().sendDialogues(679, 311);
			break;
		case 2647:
			c.getDH().sendDialogues(4647, 311);
			break;
		case 72: // Attempt for pet
			c.getPA().closeAllWindows();
			break;

		case 70300: // Attempt for pet
			c.getPA().closeAllWindows();
			break;

		case 40:
			c.getPA().removeAllWindows();
			SpinMaterial.getInstance().spin(c, SpinMaterial.Material.CROSSBOW.getRequiredItem());
			break;

		case 10:
			// TabletCreation.createTablet(c, 1, 1);
			c.getOutStream().createFrame(27);
			c.tablet = 2;
			break;

		case 94:
			c.getDH().sendDialogues(95, c.npcType);
			break;

		case 103:
			c.getPA().closeAllWindows();
			break;

		case 1391:
			c.getDH().sendDialogues(1392, c.npcType);
			break;

		case 1392:
			LootValue.configureValue(c, "resetvalue", -1);
			break;

		case 66:
				if (!c.getItems().playerHasItem(995, 10_000_000) || !c.getItems().playerHasItem(4207)) {
					c.sendMessage("You need at least 10m coins and a crystal seed to do this.");
					c.getPA().closeAllWindows();
					return;
				}
				c.getItems().deleteItem(995, 10_000_000);
				c.getItems().deleteItem(4207, 1);
				c.getItems().addItem(13092, 1);
				c.sendMessage("The weird old man successfully created a crystal halberd for you.");
			break;

		case 68:
			c.dropRateInKills = false;
			Server.getDropManager().open2(c);
			c.sendMessage("Now viewing drop-rates in percent form");
			break;
		}

		if (c.dialogueAction == 132 || c.dialogueAction == 134) {
			c.getPA().closeAllWindows();
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 132 || c.dialogueAction == 134) {
			c.getPA().closeAllWindows();
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 133) {
			c.getPA().closeAllWindows();
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 149) {
			c.getShops().openShop(9);
			c.dialogueAction = -1;
			return;
		}
		if (c.teleAction == 1337) {
			c.getPA().startTeleport(1721, 3464, 0, "modern", false);
		}
		if (c.dialogueAction == 126 || c.dialogueAction == 130) {
			c.getPA().removeAllWindows();
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		if (c.dialogueAction == 947) {
			c.getShops().openShop(111);
			c.dialogueAction = -1;
		}
		if (c.dialogueAction == -1 && c.getCurrentCombination().isPresent()) {
			c.setCurrentCombination(Optional.empty());
			c.getPA().removeAllWindows();
			return;
		}
		if (c.dialogueAction == 29) {
			c.dialogueAction = -1;
			c.getPA().removeAllWindows();
			return;
		}
		if (c.dialogueAction == 3308) {
			c.getPA().removeAllWindows();
		}
		if (c.dialogueAction == 100 || c.dialogueAction == 120) {
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 200 || c.dialogueAction == 202 || c.dialogueAction >= 101 && c.dialogueAction <= 103
				|| c.dialogueAction == 106 || c.dialogueAction >= 109 && c.dialogueAction <= 114) {
			c.getPA().removeAllWindows();
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		} else if (c.dialogueAction == 201) {
			c.getDH().sendDialogues(501, -1);
			return;
		}
		if (c.dialogueAction == 162) {
			c.dialogueAction = 0;
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 12001) {
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 12000) {
			for (int i = 8144; i < 8195; i++) {
				c.getPA().sendFrame126("", i);
			}
			int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
					8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175 };
			c.getPA().sendFrame126("@dre@Kill Tracker for @blu@" + c.playerName + "", 8144);
			c.getPA().sendFrame126("", 8145);
			c.getPA().sendFrame126("@blu@Total kills@bla@ - " + c.getNpcDeathTracker().getTotal() + "", 8147);
			c.getPA().sendFrame126("", 8148);
			int index = 0;
			for (Entry<String, Integer> entry : c.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (index > frames.length - 1) {
					break;
				}
				if (entry.getValue() > 0) {
					c.getPA().sendFrame126(
							"@blu@" + WordUtils.capitalize(entry.getKey().toLowerCase()) + ": @red@" + entry.getValue(),
							frames[index]);
					index++;
				}
			}
			c.getPA().showInterface(8134);
		}
		if (c.dialogueAction == 109) {
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 113239) {
			if (c.inDuelArena()) {
				return;
			}
			c.getItems().addItem(555, 1000);
			c.getItems().addItem(560, 1000);
			c.getItems().addItem(565, 1000);
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 2301) {
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.newPlayerAct == 1) {
			c.newPlayerAct = 0;
			c.getPA().removeAllWindows();
		}
		if (c.doricOption2) {
			c.getDH().sendDialogues(309, 284);
			c.doricOption2 = false;
		}
		/*
		 * if (c.dialogueAction == 8) { c.getPA().fixAllBarrows(); } else {
		 * c.dialogueAction = 0; c.getPA().removeAllWindows(); }
		 */
		if (c.dialogueAction == 27) {
			c.getPA().removeAllWindows();
		}
		if (c.caOption2a) {
			c.getDH().sendDialogues(136, c.npcType);
			c.caOption2a = false;
		}
	}

}
