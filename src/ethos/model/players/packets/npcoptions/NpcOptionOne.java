package ethos.model.players.packets.npcoptions;

import java.util.Objects;
import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.model.content.PotionMixing;
import ethos.model.content.achievement_diary.desert.DesertDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.limitedItems.PetGoblin;
import ethos.model.content.tradingpost.Listing;

import ethos.model.items.UseItem;
import ethos.model.minigames.inferno.Inferno;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.npcs.pets.Probita;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.skills.Fishing;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.crafting.Tanning;
import ethos.model.players.skills.hunter.impling.Impling;
import ethos.model.players.skills.mining.Mineral;
import ethos.model.players.skills.prayer.Bone;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.players.skills.thieving.Thieving.Pickpocket;
import ethos.util.Location3D;
import ethos.util.Misc;

/*
 * @author Matt
 * Handles all first options on non playable characters.
 */

public class NpcOptionOne {

	private static int output;

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.rememberNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;

		/*
		 * if(Fishing.fishingNPC(c, npcType)) { Fishing.fishingNPC(c, 1, npcType);
		 * return; }
		 */
		if (PetHandler.isPet(npcType)) {
			if (Objects.equals(PetHandler.getOptionForNpcId(npcType), "first")) {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}

		switch (npcType) {
		case 3215:
			player.getShops().openShop(183);
			break;
		case 1789:
			player.getShops().openShop(184);
			break;
		case 4065:
			player.getShops().openShop(113);
			player.sendMessage("@red@Any supplies that cost money will be in the blood money shop.");
			break;
		case 5008:
		case 7020:
			player.getDH().sendDialogues(1091, npcType);
			break;
		case 2269:
			PetGoblin.npcSpeaking(player);
			break;
		case 1017:
			if (player.diceBan == 86400 * 3) {
				player.getDH().sendNpcChat("You have already got the max dice ban time.");
				return;
			}
			if (player.diceBan > 100) {
				player.getDH().sendDialogues(1007, npcType);
			}
			player.getDH().sendDialogues(1003, npcType);
			break;
		case 7995:
			player.getDH().sendNpcChat1("Use your bones on me to unnote them.", 7995, "Elder Chaos Druid");
			break;
		case 7954:
			player.getShops().openShop(178);
			break;
		case 8906:
			if (player.christmasEvent == true) {
         		player.getDH().sendStatement("@red@You have already completed the christmas event.");
				return;
			}
			player.getDH().sendDialogues(580, npcType);
			break;
		case 2111:
			player.getShops().openShop(176);
			break;
		case 5513:
			player.getDH().sendDialogues(80, 5513);
			break;
		case 954:
			if (player.getItems().playerHasItem(4207)) {
				player.getDH().sendDialogues(66, 954);
				return;
			}
			player.getDH().sendDialogues(832, 954);
			break;
		case 5293:
			player.getShops().openShop(175);
			break;
		case 386:
			player.getDH().sendDialogues(782, 386);
			break;
		case 2662:
			player.getShops().openShop(131);
			player.sendMessage("You currently have @red@" + player.tPoint + " @bla@Tournament Points!");
			break;
		case 1013:
			player.getDH().sendStatement("You can use blood money to convert into platinum tokens.");
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
		case 5082:
			player.getDH().sendNpcChat("Enter the portal at your own risk!");
			break;
		case 5449: //herblore shop
			player.getShops().openShop(21);
			break;
		case 1603:
			player.getDH().sendDialogues(150, npcType);
			break;
		case 555:
			player.getShops().openShop(122);
			player.sendMessage("@red@You can sell me anything you want!");
			break;
		case 9011:
			player.getShops().openShop(77);
			player.sendMessage("@red@ Please type in ::vote to go to the site. And do ::voted to receive them!");
			player.sendMessage("@red@ Thank you for supporting NefariousPkz!");
			break;
		case 1011:
			player.getDH().sendNpcChat1(
					"@or2@Gamble 10 firecapes at a chance of an infernal cape.", 1011,
					"Item Gambler");
		    	break;
		case 9168:
			player.getShops().openShop(123);
			break;
		case 4921:
			player.getShops().openShop(124);
			break;
		case 9120:
			player.getShops().openShop(9);
			player.sendMessage("@red@ Use the command ::mbox to see possible rewards from the mystery boxes!");
			break;
		
		case 1035:
			player.getDH().sendDialogues(300, npcType);
			break;
		case 7690:
			Inferno.startInferno(player, Inferno.getDefaultWave());
			break;
		case 4407:
			player.getShops().openShop(124);
			break;

		case 5314:
			player.getPA().showInterface(39700);
			player.getTeleport().selection(player, 0);
			// player.getTeleport().loadMonsterTab();
			break;
		case 3248:
			player.getPA().showInterface(39700);
			player.teleSelected = 0;
			player.getTeleport().loadMonsterTab();
			break;
		case 1143:
			player.getShops().openShop(83);
			break;

		case 1909:
			player.getDH().sendDialogues(900, 1909);
			break;
		case 2989:
			player.getDH().sendDialogues(1427, 2989);
			break;
		case 3306:
			player.getDH().sendDialogues(1577, -1);
			break;
		case 7520:
			player.getDH().sendDialogues(850, 7520);
			break;
		/**
		 * Doomsayer
		 */
		case 6773:
			if (!player.pkDistrict) {
				player.sendMessage("You cannot do this right now.");
				return;
			}
			player.getDH().sendDialogues(800, 6773);
			break;
		// Zeah Throw Aways
		case 2200:
			player.getDH().sendDialogues(55873, 2200);
			break;
		case 3189:
			player.getDH().sendDialogues(11929, 3189);
			break;
		case 5998:
			if (player.amDonated <= 1) {
				player.getDH().sendDialogues(5998, 5998);
			} else {
				player.getDH().sendDialogues(5999, 5998);
			}
			break;
		case 4062:
			player.getDH().sendDialogues(55875, 4062);
			break;
		case 4321:
			player.getDH().sendDialogues(145, 4321);
			break;
		case 7041:
			player.getDH().sendDialogues(500, 7041);
			break;
		case 6877:
			player.getDH().sendDialogues(55877, 6877);
			break;
		case 4409:
			player.getDH().sendDialogues(55876, 4409);
			break;
		case 6982:
			player.getDH().sendDialogues(55868, 6982);
			break;
		case 6947:
		case 6948:
		case 6949:
			player.getDH().sendDialogues(55872, 6947);
			break;
		case 6998:
			player.getDH().sendDialogues(55871, 6998);
			break;
		case 7001:
			player.getDH().sendDialogues(55869, 7001);
			break;
		case 6999:
			player.getDH().sendDialogues(55870, 6999);
			break;
		case 6904:
			player.getDH().sendDialogues(55864, 6904);
			break;
		case 6906:
			player.getDH().sendDialogues(55865, 6904);
			break;
		case 6908:
			player.getDH().sendDialogues(55866, 6904);
			break;
		case 6910:
			player.getDH().sendDialogues(55867, 6904);
			break;
		// End Zeah Throw Aways
		case 1503:
			player.getDH().sendDialogues(88393, 1503);
			break;
		case 1504:
			player.getDH().sendDialogues(88394, 1504);
			break;

		case 6774:
			player.getDH().sendDialogues(800, 6773);
			break;


		case 822:
			if (player.getDiaryManager().getWildernessDiary().hasDoneEasy()) {
				player.getDH().sendDialogues(702, 822);
			} else {
				if (player.getItems().playerHasItem(11286) && player.getItems().playerHasItem(1540)
						&& player.getItems().playerHasItem(995, 5_000_000)) {
					player.getItems().deleteItem(11286, 1);
					player.getItems().deleteItem(1540, 1);
					player.getItems().deleteItem(995, 500_000);
					player.getItems().addItem(11283, 1);
					player.votePoints -= 5;
					player.refreshQuestTab(2);
					player.getDH().sendItemStatement("Oziach successfully bound your dragonfire shield.", 11283);
				} else {
					player.getDH().sendNpcChat("Come back with a shield, visage and 5M Gold!");
				}
			}
			break;
		case 306:
			player.getDH().sendDialogues(710, 306);
			break;
		case 2897:
			if (player.getRights().isOrInherits(Right.IRONMAN) || player.getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || player.getRights().isOrInherits(Right.HC_IRONMAN)) {
				player.sendMessage("@red@You are not permitted to make use of this.");	
				return;
			}
			Listing.openPost(player, false, true);	
			break;
		case 3226:
			if (player.playerLevel[Skill.WOODCUTTING.getId()] >= 90) {
				player.getPA().startTeleport(1586, 3488, 0, "modern", false);
				return;
			}
			player.sendMessage("@red@You need a woodcutting level of 90 to teleport to WC Guild.");	
			break;
		case 7303:
			player.sendMessage("I will trade a full set of clue scrolls with a master one.");
			break;

		case 2914:
			player.getDH().sendNpcChat2("Use Zammy Spear on me to get Hasta, cost 10m", "Use Hasta on me get Zammy Spear, cost 5m", 2914,
					"Otto Godblessed");
			break;

		case 1635:
		case 1636:
		case 1637:
		case 1638:
		case 1639:
		case 1640:
		case 1641:
		case 1642:
		case 1643:
		case 1654:
		case 7302:
			Impling.catchImpling(player, npcType, player.rememberNpcIndex);
			break;

		case 17: // Rug merchant 
			player.getDH().sendDialogues(837, 17);
			break;
		case 276:
			if (Config.BONUS_XP_WOGW) {
				player.getDH().sendNpcChat1(
						"Well of Goodwill is currently @red@active@bla@! \\n It is granting 1 hour of @red@Double XP@bla@!",
						276, "NefariousPkz Crier");
			} else if (Config.BONUS_PC_WOGW) {
				player.getDH().sendNpcChat1(
						"Well of Goodwill is currently @red@active@bla@! \\n It is granting 1 hour of @red@Double Pc Points@bla@!",
						276, "NefariousPkz Crier");
			} else if (Config.DOUBLE_DROPS) {
				player.getDH().sendNpcChat1(
						"Well of Goodwill is currently @red@active@bla@! \\n It is granting 1 hour of @red@Double Drops@bla@!",
						276, "NefariousPkz Crier");
			} else {
				player.getDH().sendNpcChat1("Well of Goodwill is currently @red@inactive@bla@!", 276, "NefariousPkz Crier");
			}
			break;
		case 5520:
			player.getDiaryManager().getDesertDiary().claimReward();
			break;
		case 5519:
			player.getDiaryManager().getArdougneDiary().claimReward();
			break;
		case 5790:
			player.getDiaryManager().getKaramjaDiary().claimReward();
			break;
		case 5525:
			player.getDiaryManager().getVarrockDiary().claimReward();
			break;
		case 5523:
			player.getDiaryManager().getLumbridgeDraynorDiary().claimReward();
			break;
		case 5524:
			player.getDiaryManager().getFaladorDiary().claimReward();
			break;
		case 5521:
			player.getDiaryManager().getMorytaniaDiary().claimReward();
			break;
		case 5514:
			player.getDiaryManager().getWildernessDiary().claimReward();
			break;
		case 5517:
			player.getDiaryManager().getKandarinDiary().claimReward();
			break;
		case 5526:
			player.getDiaryManager().getFremennikDiary().claimReward();
			break;
		case 5518:
			player.getDiaryManager().getWesternDiary().claimReward();
			break;

		case 3936:
			player.getDH().sendNpcChat1("Right click on me and i will take you on-board.", 3936, "Sailor");
			break;

		case 1031:
			if (player.getItems().playerHasItem(995, 30)) {
				player.getItems().deleteItem(995, 30);
				player.getItems().addItem(36, 1);
				player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.BUY_CANDLE);
			} else {
				player.sendMessage("You need 30 coins to purchase a candle.");
				return;
			}
			break;

		case 6586:
			player.getDH().sendNpcChat1("No shirt, Sherlock", 6586, "Sherlock");
			player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.SHERLOCK);
			break;

		case 5036:
			if (player.getItems().playerHasItem(225) || player.getItems().playerHasItem(223)) {
				player.sendMessage("The Apothecary takes your ingredients and creates a strength potion.");
				player.getItems().deleteItem(225, 1);
				player.getItems().deleteItem(223, 1);
				player.getItems().addItem(115, 1);
				player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.APOTHECARY_STRENGTH);
			} else {
				player.sendMessage("You must have limpwurt root and red spiders' eggs to do this.");
				return;
			}
			break;

		case 5906:
			player.getDH().sendDialogues(564, npcType);
			break;

		case 3500:
			player.getDH().sendDialogues(64, npcType);
			break;

		case 5870:
			if (player.getCerberusLostItems().size() > 0) {
				player.getDH().sendDialogues(640, 5870);
				return;
			}
			player.getDH().sendDialogues(105, npcType);
			break;

		case 7283:
			if (player.getSkotizoLostItems().size() > 0) {
				player.getDH().sendDialogues(640, 7283);
				return;
			}
			player.getDH().sendDialogues(105, npcType);
			break;

		case 6481: // Max
			if (!player.maxRequirements(player)) {
				player.sendMessage("Max does not even bother speaking to you.");
				return;
			}
			player.getDH().sendDialogues(85, npcType);
			break;
		case 3307: // Combat instructor
			player.getDH().sendDialogues(1390, npcType);
			break;

		case 6071: // Achievement cape
			player.getAchievements().claimCape();
			break;
		case 394:
			player.getDH().sendDialogues(669, npcType);
			break;
		case 311:
			player.getDH().sendDialogues(650, npcType);
			break;

		// Noting Npc At Skill Area
		case 905:
			player.talkingNpc = 905;
			player.getDH().sendNpcChat("Hello there, I can note your resources.",
					"I charge @red@25%@bla@ of the yield, this @red@does not apply to donators@bla@.",
					"Use any resource item obtained in this area on me.");
			player.nextChat = -1;
			break;

		case 2040:
			player.getDH().sendDialogues(637, npcType);
			break;
		case 2184:
			player.getShops().openShop(29);
			break;

		case 6866:
			player.getShops().openShop(82);
			player.sendMessage("You currently have @red@" + player.getShayPoints() + " @bla@Assault Points!");
			break;

		case 6601:
			NPC golem = NPCHandler.npcs[player.rememberNpcIndex];
			if (golem != null) {
				player.getMining().mine(golem, Mineral.RUNE,
						new Location3D(golem.getX(), golem.getY(), golem.heightLevel));
			}
			break;
		case 1850:
			player.getShops().openShop(112);
			break;
		case 2580:
			player.getDH().sendDialogues(629, npcType);
			break;
		case 3257:
			if(player.getMode().isIronman() || player.getMode().isUltimateIronman() || player.getMode().isHCIronman()){
				player.getThieving().steal(Pickpocket.FARMER, NPCHandler.npcs[player.rememberNpcIndex]);
			}else{
				player.getShops().openShop(16);
			}
			break;
		case 3894:
			player.getShops().openShop(26);
			break;
		case 3220:
			player.getShops().openShop(25);
			break;
		case 637:
			player.getShops().openShop(6);
			break;
			case 6875:
				player.setRunEnergy(100);
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getHealth().removeAllStatuses();
				player.getHealth().reset();
				player.getPA().refreshSkill(5);
				player.getDH().sendItemStatement("You restore your health and prayer points.", 4049);
				player.nextChat =  -1;
				break;
			case 732:
			player.getShops().openShop(16);
			break;
		case 8688:
			player.getShops().openShop(113);
			break;
		case 2949:
			player.getPestControlRewards().showInterface();
			break;
		case 2461:
			player.getWarriorsGuild().handleDoor();
			break;
		case 7663:
			player.getDH().sendDialogues(3299, npcType);
			player.sendMessage("@red@ Use the command ::wildymap to see the locations of wildy slayer monsters!");
			break;
		case 402:
			player.getDH().sendDialogues(1298, npcType);
			break;
		case 315:
			player.getShops().openShop(3);
			break;
		case 401:
			if(player.combatLevel<20){
				player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of 20.",402,"Mazchna");
				return;
			}
			player.getDH().sendDialogues(3300, npcType);
			break;
	
		case 405:
			if(player.combatLevel<100){
				player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 100.",402,"Duradel");
				return;
			}
			if (player.playerLevel[18] < 50) {
				player.getDH().sendNpcChat1("You must have a slayer level of at least 50 weakling.", 490, "Duradel");
				return;
			}
			player.getDH().sendDialogues(3300, npcType);
			break;
		case 6797: // Nieve
				player.getDH().sendDialogues(3300, npcType);
			break;
		case 1308:
			player.getDH().sendDialogues(538, npcType);
			break;
		case 8819:
			player.getShops().openShop(121);
			player.sendMessage("You currently have " + player.pvmp + " Pvm points.");
			break;
		case 7456:
			player.getDH().sendDialogues(619, npcType);
			break;
		case 4306:
			player.getShops().openSkillCape();
			break;
		case 1933:
			player.getShops().openMillCape();
			break;
		case 3341:
			player.getDH().sendDialogues(2603, npcType);
			break;
		case 5919:
			player.getDH().sendDialogues(14400, npcType);
			break;
		case 603:
			player.getDH().sendDialogues(11955, npcType);
			break;
		case 8761:
			player.getDH().sendDialogues(10955, npcType);
			break;
		case 8623:
			if (player.playerLevel[18] >= 95) {
			player.getDH().sendDialogues(11155, npcType);
			break;
			}
			player.sendMessage("You need a slayer level of 95 to get a task from me.");
			break;
		case 6599:
			player.getShops().openShop(12);
			break;
		case 2578:
			player.getDH().sendDialogues(2401, npcType);
			break;
		case 3789:
			player.getShops().openShop(75);
			break;
		// FISHING
		case 3913: // NET + BAIT
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 1);
			break;
		case 3317:
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 14);
			break;
		case 4712:
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 15);
			break;
		case 1524:
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 11);
			break;
		case 3417: // TROUT
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 4);
			break;
		case 3657:
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 8);
			break;
		case 635:
			Fishing.attemptdata(player, 13); // DARK CRAB FISHING
			break;
		case 6825: // Anglerfish
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 16);
			break;
		case 1520: // LURE
		case 310:
		case 314:
		case 317:
		case 318:
		case 328:
		case 331:
			Fishing.attemptdata(player, 9);
			break;

		case 944:
			player.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons", "Chaos Dwarf", "-- Next Page --");
			player.teleAction = 7;
			break;

		case 559:
			player.getShops().openShop(16);
			break;
		case 5809:
			Tanning.sendTanningInterface(player);
			break;

		case 2913:
			player.getShops().openShop(22);
			break;
		case 403:
			player.getDH().sendDialogues(2300, npcType);
			break;
		case 1599:
			break;

		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker
		case 494: // Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 567: // Banker
		case 766: // Banker
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
			player.getPA().openUpBank();
			break;
		case 1986:
			player.getDH().sendDialogues(2244, player.npcType);
			break;

		case 5792:
			player.getDH().sendDialogues(4005, player.npcType);
			// player.getShops().openShop(9);
			break;
		case 6747:
			player.getShops().openShop(77);
			break;
		case 8637:// magic supplies
			player.getShops().openShop(6);
			break;
		case 8636:// range supplies
			player.getShops().openShop(5);
			break;
		case 1785:
			player.getShops().openShop(8);
			break;

		case 1860:
			player.getShops().openShop(47);
			break;

		case 519:
			player.getShops().openShop(48);
			break;

		case 548:
			player.getDH().sendDialogues(69, player.npcType);
			break;

		case 2258:
			player.getDH().sendOption2("Teleport me to Runecrafting Abyss.", "I want to stay here, thanks.");
			player.dialogueAction = 2258;
			break;

		case 532:
			player.getShops().openShop(47);
			break;

		case 8635:// melee supplies
			player.getShops().openShop(4);
			break;

		case 7913:
			if (player.getMode().isIronman() || player.getMode().isUltimateIronman() || player.getMode().isHCIronman()
					|| player.getRights().contains(Right.OWNER) || player.getRights().contains(Right.ADMINISTRATOR)) {
				player.getShops().openShop(41);
			} else {
				player.sendMessage("You must be an Iron Man to access this shop.");
			}
			break;
		case 7769:
			player.getShops().openShop(182);
			break;

		

		/*
		 * case 198: c.getShops().openSkillCape(); break;
		 */

		/**
		 * Make over mage.
		 */

		}
	}

}
