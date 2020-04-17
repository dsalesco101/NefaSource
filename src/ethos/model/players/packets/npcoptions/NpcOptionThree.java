package ethos.model.players.packets.npcoptions;

import ethos.Config;
import ethos.Server;
import ethos.model.content.PotionMixing;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.desert.DesertDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;

/*
 * @author Matt
 * Handles all 3rd options on non playable characters.
 */

public class NpcOptionThree {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.rememberNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;

		if (PetHandler.isPet(npcType)) {
			if (PetHandler.getOptionForNpcId(npcType) == "three") {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}

		switch (npcType) {
		case 6637:
            if (player.getItems().freeSlots() < 1) {
                player.sendMessage("Your inventory is full.");
                return;
            }
            NPCHandler.npcs[player.rememberNpcIndex].absX = 0;
            NPCHandler.npcs[player.rememberNpcIndex].absY = 0;
            NPCHandler.npcs[player.rememberNpcIndex] = null;
            player.summonId = -1;
            player.hasFollower = false;
           player.getItems().addItem(12654, 1);
           break;
       case 6638:
            if (player.getItems().freeSlots() < 1) {
                player.sendMessage("Your inventory is full.");
                return;
            }
            NPCHandler.npcs[player.rememberNpcIndex].absX = 0;
            NPCHandler.npcs[player.rememberNpcIndex].absY = 0;
            NPCHandler.npcs[player.rememberNpcIndex] = null;
            player.summonId = -1;
            player.hasFollower = false;
           player.getItems().addItem(12647, 1);
           break;
		case 8906:
			if (player.christmasEvent == true) {
         		player.getDH().sendStatement("@red@You have already completed the christmas event.");
				return;
			}
			player.getDH().sendDialogues(480, 8906);
			break;
		case 8781:
			Server.getDropManager().search(player, "Donator Boss");
			break;
		case 1428:
			player.getPrestige().openShop();
			break;
		case 1909:
			player.getDH().sendDialogues(903, 1909);
			break;
		case 2897:
			player.getPA().openUpBank();
			break;
		case 2989:
			player.getPrestige().openShop();
			break;
		case 4321:
			player.getShops().openShop(119);
			player.sendMessage("You currently have @red@"+player.bloodPoints+" @bla@Blood Money Points!");
			break;
		case 7520:
			player.getShops().openShop(118);
			player.sendMessage("You currently have @red@"+player.getRaidPoints()+" @bla@Raid Points!");
			break;
		case 6773:
			player.isSkulled = true;
			player.skullTimer = Config.EXTENDED_SKULL_TIMER;
			player.headIconPk = 0;
			player.getPA().requestUpdates();
			player.sendMessage("@cr10@@blu@You are now skulled.");
			break;
		case 2200:
			player.getPA().openUpBank();
			break;
		case 1306:
			if (player.getItems().isWearingItems()) {
				player.sendMessage("You must remove your equipment before changing your appearance.");
				player.canChangeAppearance = false;
			} else {
				player.getPA().showInterface(3559);
				player.canChangeAppearance = true;
			}
			break;
		case 17: //Rug merchant - Nardah
			player.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.TRAVEL_NARDAH);
			player.startAnimation(2262);
			AgilityHandler.delayFade(player, "NONE", 3402, 2916, 0, "You step on the carpet and take off...", "at last you end up in nardah.", 3);
			break;
		
		case 3936:
			AgilityHandler.delayFade(player, "NONE", 2310, 3782, 0, "You board the boat...", "And end up in Neitiznot", 3);
			player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TRAVEL_NEITIZNOT);
			break;
			
		case 402:
		case 401:
		case 405:
		case 6797:
		case 7663:
		case 8761:
		case 5870:
			player.getShops().openShop(10);
			player.sendMessage("I currently have @blu@" + player.getSlayer().getPoints() + " @bla@slayer points.");
			break;
		case 315:
			player.getDH().sendDialogues(548, 315);
			break;
		case 403:
			player.getDH().sendDialogues(12001, -1);
			break;
		case 1599:
			player.getShops().openShop(10);
			player.sendMessage("You currently have @red@" + player.slayerPoints + " @bla@slayer points.");
			break;
		case 836:
			player.getShops().openShop(103);
			break;
		case 5449:
			PotionMixing.decantInventory(player);
			player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.POTION_DECANT);
			break;
		case 2580:
			if (Boundary.isIn(player, Boundary.VARROCK_BOUNDARY)) {
				player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.TELEPORT_ESSENCE_VAR);
			}
			if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
				player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ESSENCE_ARD);
			}
			if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
				player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_ESSENCE_FAL);
			}
			player.getPA().startTeleport(2929, 4813, 0, "modern", false);
			break;
		}
	}

}
