package ethos.model.content.teleportation;

import ethos.Config;
import ethos.event.impl.WheatPortalEvent;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.morytania.MorytaniaDiaryEntry;
import ethos.model.content.achievement_diary.western_provinces.WesternDiaryEntry;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;

/**
 * Teleport Handler Class - Used for our new teleporting system Probably should
 * have used enums, oh well..
 * 
 * @author Tyler
 */
public class TeleportHandler {

	private Player c;

	public TeleportHandler(Player player) {
		this.c = player;
	}

	String[] monsterNames = { "Catacombs" ,"Rock Crabs", "Cows", "Bob's Island",
			"Desert Bandits", "Elf Warriors", "Dagannoths", "Mithril Dragons", "Slayer Tower", "Fremennik Slayer Dungeon",
			"Taverley Dungeon", "Stronghold Cave", "Smoke Devils", "Asgarnian Ice Dungeon", "Brimhaven Dungeon", "Lithkren Vault", "Crystal Cave", "", "", "", "", "", "", "", ""};
	String[] minigameNames = {"Raids", "Warriors Guild", "Pest Control",
			"Fight Caves", "Barrows", "Clan Wars", "Outlast", "Mage Arena", "Duel Arena", "Inferno", "Dagannoth Mother", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "" };
	String[] bossNames = { "Barrelchest", "Dagannoth Kings",
			"King Black Dragon@red@ (DANGEROUS!)", "Giant Mole", "Kalphite Queen", "Godwars",
			"Corporeal Beast", "Dagannoth Mother", "Kraken", "Zulrah", "Cerberus", "Thermonuclear Smoke Devils",
			"Abyssal Sire", "Demonic Gorillas", "Lizardman Shaman", "Vorkath", "Alchemical hydra", "", "", "", "", "", "", ""};
	String[] wildernessNames = {"West Dragons @red@(10)", "Mage Bank @yel@(Safe)",
			"Dark Castle @red@(15)", "Hill Giants (Multi) @red@(18)", "Wilderness Agility Course @red@(52)",
			"Vet'ion @red@(40) ", "Callisto @red@(43)", "Scorpia @red@(54)", "Venenatis @red@(28)",
			"Chaos Elemental @red@(50)", "Chaos Fanatic @red@(41)", "Crazy Archaeologist @red@(23)", "Revenant Cave@red@(40)", "Black Chinchompa@red@(32)", "Elder Chaos Druids@red@(16)", "Lava Maze", "",
			"", "", "", "", "", "", "", ""};
	String[] cityNames = {"Varrock", "Yanille", "Edgeville", "Lumbridge", "Ardougne",
			"Neitiznot", "Karamja", "Falador", "Taverley", "Camelot", "Catherby", "Al Kharid", "Draynor", "", "", "", "", "",
			"", "", "", "", "", "", "" };
	String[] donatorNames = { "@cr4@Donator Zone (::dz)", "@cr8@Legendary Zone (::lz)",
			"@cr17@Onyx Zone(::oz)", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	String[] otherNames = {  "Slayer Masters", "Skilling Island", "Agility",
			"Hunter", "Farming Patches", "Puro-Puro", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "" };

	public void loadMonsterTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 24; j++) {
			c.getPA().sendFrame126(monsterNames[j], ids[j]);
		}
	}

	public void loadMinigameTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 23; j++) {
			c.getPA().sendFrame126(minigameNames[j], ids[j]);
		}
	}

	public void loadBossTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 23; j++) {//this line makes the numbers dissappear, i messed around with the <= number and got them to disappear but it gives error sometimes
			c.getPA().sendFrame126(bossNames[j], ids[j]);
		}
	}

	public void loadWildernessTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 24; j++) {
			c.getPA().sendFrame126(wildernessNames[j], ids[j]);
		}
	}

	public void loadCityTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 24; j++) {
			c.getPA().sendFrame126(cityNames[j], ids[j]);
		}
	}

	public void loadDonatorTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 24; j++) {
			c.getPA().sendFrame126(donatorNames[j], ids[j]);
		}
	}

	public void loadOtherTab() {
		int[] ids = { 44745,44749,44753,44757,44761,44765,44769,44773,44777,44781,44785,44789,44793,44797,44801,44805,
				44809, 44813, 44817, 44821, 44825, 44829, 44833, 44837, 44841};
		for (int j = 0; j <= 24; j++) {
			c.getPA().sendFrame126(otherNames[j], ids[j]);
		}
	}

	public void loadTab(Player player, int tab) {
		if (player.teleSelected == 0) {
			loadMonsterTab();
		} else if (player.teleSelected == 1) {
			loadMinigameTab();
		} else if (player.teleSelected == 2) {
			loadBossTab();
		} else if (player.teleSelected == 3) {
			loadWildernessTab();
		} else if (player.teleSelected == 4) {
			loadCityTab();
		} else if (player.teleSelected == 5) {
			loadDonatorTab();
		} else if (player.teleSelected == 6) {
			loadOtherTab();
		}
	}

	public void selection(Player player, int i) {
		player.teleSelected = i;
		loadTab(player, i);
	}

	public boolean teleportCheck(Player player) {
		/*
		 * if (!player.modeTut) { player.
		 * sendMessage("You must finish the tutorial before teleporting anywhere.");
		 * return false; }
		 */
		return true;
	}

	public void handleTeleports(Player player, int Id) {


		if (player.inWild() && player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			return;
		}

		switch (Id) {
		// Handle Magic Book Teleports
		case 4140: // Monsters
		case 50235:
		case 117112:
			// player.getPA().showInterface(65000);
			// selection(player, 0);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4143: // Minigames
		case 50245:
		case 117123:
			// player.getPA().showInterface(65000);
			// selection(player, 1);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4146: // Bosses
		case 50253:
		case 117131:
			// player.getPA().showInterface(65000);
			// selection(player, 2);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 4150: // Wilderness
		case 51005:
		case 117154:
			// player.getPA().showInterface(65000);
			// selection(player, 3);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 6004: // City
		case 51013:
		case 117162:
			// player.getPA().showInterface(65000);
			// selection(player, 4);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 6005: // Donator
		case 51023:
		case 117186:
			// if (player.getRights().isDonator() || player.getRights().isSuperDonator() ||
			// player.getRights().isLegendaryDonator() ||
			// player.getRights().isExtremeDonator() ||
			// c.getRights().isOrInherits(Right.OWNER)) {
			// player.getPA().showInterface(65000);
			// selection( player, 5);
			// } else {
			// player.sendMessage("You must be a donator to use this.");
			// }
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 29031: // Other
		case 51031:
		case 117194:
			// player.getPA().showInterface(65000);
			// selection(player, 6);
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;
		case 51039:
		case 72038:
		case 117210:
			// player.sendMessage("If you wish to teleport, please talk to Wizard Mizgog in
			// Edgeville.");
			break;

		// Start of Tabs
		case 161033: // Monsters Tab
		case 155026:
			selection(player, 0);
			break;
		case 161036: // Minigames Tab
		case 155051:
			selection(player, 1);
			break;
		case 165018: // Bosses Tab
		case 155046:
			selection(player, 2);
			break;
		case 165015: // Wilderness Tab
		case 155041:
			selection(player, 3);
			break;
		case 165024: // City Tab
		case 155036:
			selection(player, 4);
			break;
		case 165027: // Donator Tab
		case 155056:
			if (player.amDonated < 10) {
				player.sendMessage("You must be a donator to use this.");
				return;
			}
			selection(player, 5);
			break;
		case 165021: // Other Tab
		case 155031:
			selection(player, 6);
			break;
		// End of Tabs

		// Start of Buttons
		// Button 1
		case 174198:
			if (Boundary.isIn(player, Boundary.OUTLAST_AREA)) {
				player.sendMessage("You cannot teleport when in the tournament arena.");
				return;
			}
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { 

                player.getPA().movePlayer(1664, 10050);
                player.getPA().showInterface(33900);// AFTER 3 SECONDS THIS INTERFACE COMES UP

            } else if (player.teleSelected == 1) { // Minigames - Raids
				player.getPA().startTeleport(3033, 6067, 0, "modern", false); // change
				// player.sendMessage("Teleporting to "+minigameNames[1]+".");
			} else if (player.teleSelected == 2) { // Bosses - Barrelchest
				player.getPA().startTeleport(1225, 3499, 0, "modern", false);// change
				// player.sendMessage("Teleporting to "+bossNames[1]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Green Dragons
				player.getPA().startTeleport(2976, 3591, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[1]+".");
			} else if (player.teleSelected == 4) { // City - Varrock
				player.getPA().startTeleport(3210, 3424, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[1]+".");
			
			
			} else if (player.teleSelected == 5) { // Donator - Donator Lobby
				 if (player.amDonated >= 5) {
					 player.getPA().startTeleport(3809, 2844, 0, "modern", false); 
				 } else {
				 }
				 player.sendMessage("You need to be a regular donator to teleport here.");
			} else if (player.teleSelected == 6) { // Other - Slayer Masters
				player.getPA().startTeleport(3078, 3494, 0, "modern", false); // change
				// player.sendMessage("Teleporting to "+otherNames[1]+".");
			}
			break;
		// Button 2
		case 174202:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Rock Crabs
				player.getPA().startTeleport(2673, 3710, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[2]+".");
			} else if (player.teleSelected == 1) { // Minigames - Warriors Guild
				player.getPA().startTeleport(2874, 3546, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[2]+".");
			} else if (player.teleSelected == 2) { // Bosses - Daggononoth Kings
				player.getPA().startTeleport(1913, 4367, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[2]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Mage Bank
				player.getPA().startTeleport(2539, 4716, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[2]+".");
			} else if (player.teleSelected == 4) { // City - Yanille
				player.getPA().startTeleport(2606, 3093, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[2]+".");
			} else if (player.teleSelected == 5) { // Donator - Basic Slayer
				 if (player.amDonated > 299) {
					player.getPA().startTeleport(2846, 5089, 0, "modern", false);
					} 
					else {
						 player.sendMessage("You need a donator status of Legendary to tele here.");
					 }
				 //player.sendMessage("The legendary zone is still being developed!");
			} else if (player.teleSelected == 6) { // Other - skilling island
				player.getPA().startTeleport(3803, 3538, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[3]+".");
			}
			break;

		
		case 174206:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monster - Cow
				player.getPA().startTeleport(3260, 3272, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[3]+".");
			} else if (player.teleSelected == 1) { // Minigames - Pest Control
				player.getPA().startTeleport(2660, 2648, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[3]+".");
				player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PEST_CONTROL_TELEPORT);
			} else if (player.teleSelected == 2) { // Bosses - King Black Dragon
				player.getPA().startTeleport(3005, 3849, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[3]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Dark Castle
				player.getPA().startTeleport(3020, 3632, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[3]+".");
			} else if (player.teleSelected == 4) { // City - Edgeville
				player.getPA().startTeleport(3093, 3493, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[3]+".");
			} else if (player.teleSelected == 5) { // Super - Advanced Slayer
				 if (player.amDonated > 999) {
				 player.getPA().startTeleport(2786, 4840, 0, "modern", false);}	
				 else {
					 player.sendMessage("You need a donator status of Onyx to tele here.");
				 }
			} else if (player.teleSelected == 6) { // Other - Agility - Grace
				player.getPA().startTeleport(3080, 3484, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[4]+".");
			}
			break;
		
		case 174210:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Bob's Island
				player.getPA().startTeleport(2524, 4775, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[4]+".");
			} else if (player.teleSelected == 1) { // Minigames - Fight Caves
				player.getPA().startTeleport(2444, 5179, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[4]+".");
				player.sendMessage("The minigame entrance can be found to the south!");
			} else if (player.teleSelected == 2) { // Bosses - Giant Mole
				player.getPA().startTeleport(2993, 3376, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[4]+".");
				// player.sendMessage("Right click and Look-inside mole hills to fight the Giant
				// Mole!");
			} else if (player.teleSelected == 3) { // Wilderness - Hill Giants Multi
				player.getPA().startTeleport(3304, 3657, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[4]+".");
			} else if (player.teleSelected == 4) { // City - Lumbridge
				player.getPA().startTeleport(3222, 3218, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[4]+".");
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.LUMBRIDGE_TELEPORT);
			} else if (player.teleSelected == 5) { // Super - Basic Skilling
				player.getPA().startTeleport(3066, 9544, 0, "modern", false);
			} else if (player.teleSelected == 6) { // City - Lumbridge
				player.getPA().startTeleport(3560, 4010, 0, "modern", false);
			}
			break;
		
		case 174214:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Desert Bandit
				player.getPA().startTeleport(3176, 2987, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[5]+".");
			} else if (player.teleSelected == 1) { // Minigames - Barrows
				c.spawnedbarrows = false;
				player.getPA().startTeleport(3565, 3316, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[5]+".");
			} else if (player.teleSelected == 2) { // Bosses - Kalphite Queen Tunnels
				player.getPA().startTeleport(3510, 9496, 2, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[5]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Wilderness Agility Course
				player.getPA().startTeleport(3003, 3934, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[5]+".");
			} else if (player.teleSelected == 4) { // City - Ardougne
				player.getPA().startTeleport(2662, 3305, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[5]+".");
				c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);
			} else if (player.teleSelected == 5) { // Super - Demons
				player.getPA().startTeleport(3048, 9582, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Other - Farming Patches
				player.getPA().startTeleport(3003, 3376, 0, "modern", false);
				// player.sendMessage("Teleporting to "+otherNames[5]+".");
			}
			break;
		
		
		case 174218:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Elf Warrior
				player.getPA().startTeleport(2897, 2725, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[6]+".");
			} else if (player.teleSelected == 1) { // Minigames - Clan Wars
				player.getPA().startTeleport(3387, 3158, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[6]+".");
			} else if (player.teleSelected == 2) { // Bosses - God Wars Dungeon
				c.getDH().sendDialogues(4487, -1);
			} else if (player.teleSelected == 3) { // Wilderness - Vet'ion
				player.getPA().startTeleport(3200, 3794, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[6]+".");
			} else if (player.teleSelected == 4) { // City - Neitiznot
				player.getPA().startTeleport(2321, 3804, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[6]+".");
			} else if (player.teleSelected == 5) { // Extreme - Advanced Skilling
				player.getPA().startTeleport(2710, 9466, 0, "modern", false);
			} else if (player.teleSelected == 6) { // Extreme - Advanced Skilling
				player.getPA().startTeleport(2594, 4320, 0, "modern", false);
			}
			break;
		
		
		case 174222:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Dagannoths
				player.getPA().startTeleport(2442, 10147, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[7]+".");
			} else if (player.teleSelected == 1) { // Minigames - outlast
				player.getPA().startTeleport(3107, 3498, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[7]+".");
			} else if (player.teleSelected == 2) { // Bosses - Corporeal Beast
				player.getPA().startTeleport(2964, 4382, 2, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[7]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Callisto
				player.getPA().startTeleport(3325, 3845, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[7]+".");
			} else if (player.teleSelected == 4) { // City - Karamja
				player.getPA().startTeleport(2948, 3147, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[7]+".");
			} else if (player.teleSelected == 5) { // Extreme - Dragons(Hides)

			}
			break;
		
		
		case 174226:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Chickens
				player.getPA().startTeleport(1740, 5342, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[8]+".");
			} else if (player.teleSelected == 1) { // Minigames - mage arena
				player.getPA().startTeleport(2541, 4716, 0, "modern", false);
				// player.sendMessage("Teleporting to "+minigameNames[8]+".");
			} else if (player.teleSelected == 2) { // Bosses - Daggonoth Mother
				player.getPA().startTeleport(2508, 3643, 0, "modern", false);
				 player.sendMessage("Enter underground to fight boss. Buy the books with the rusty casket she drops.");
			} else if (player.teleSelected == 3) { // Wilderness - Scorpia
				player.getPA().startTeleport(3233, 3945, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[8]+".");
			} else if (player.teleSelected == 4) { // City - Falador
				player.getPA().startTeleport(2964, 3378, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[8]+".");
				c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_TO_FALADOR);
			}
			break;
		
		case 174230:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Slayer Tower
				player.getPA().startTeleport(3428, 3538, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[9]+".");
			} else if (player.teleSelected == 1) {
				player.getPA().startTeleport(3366, 3266, 0, "modern", false);
			} else if (player.teleSelected == 2) { // Bosses - Kraken
				player.getPA().startTeleport(2280, 10016, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[9]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Venenatis
				player.getPA().startTeleport(3345, 3754, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[9]+".");
			} else if (player.teleSelected == 4) { // City - Taverly
				player.getPA().startTeleport(2928, 3451, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[9]+".");
			} else if (player.teleSelected == 5) { // Extreme - Smoke Devil
				player.getPA().startTeleport(3365, 3266, 0, "modern", false);

			}
			break;
		case 175002:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(1567, 5074, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(2272, 4050, 0, "modern", false);
			} else if (player.teleSelected == 3) { //laza maze
				player.getPA().startTeleport(3199, 3862, 0, "modern", false);
			}
			break;
		case 175006:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(3272, 6052, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(1354, 10259, 0, "modern", false);
			}
			break;
		case 174234:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2807, 10002, 0, "modern", false);
			} else if (player.teleSelected == 1) {
				player.getPA().startTeleport(2437, 5126, 0, "modern", false);
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(3278, 3918, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(2203, 3056, 0, "modern", false);
			} else if (player.teleSelected == 4) {
				player.getPA().startTeleport(2757, 3478, 0, "modern", false);
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
			}
			break;
		
		case 174238:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2883, 9800, 0, "modern", false);
			} else if (player.teleSelected == 1) {
				player.getPA().startTeleport(2508, 3641, 0, "modern", false); 
				c.sendMessage("Climb down the ladder to start the mini-game.");
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(2978, 3833, 0, "modern", false); 
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(2979, 3697, 0, "modern", false);
			} else if (player.teleSelected == 2) {//cerberus
				player.getPA().startTeleport(1310, 1248, 0, "modern", false);
			} else if (player.teleSelected == 4) {
				player.getPA().startTeleport(2804, 3432, 0, "modern", false);
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CATHERY_TELEPORT);
			}
			break;
		
		case 174242:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2452, 9820, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
			} else if (player.teleSelected == 3) {
				player.getPA().startTeleport(2984, 3713, 0, "modern", false);
			}else if (player.teleSelected == 4) {
				player.getPA().startTeleport(3293, 3179, 0, "modern", false);
			}
			break;
		
		case 174246:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
			}else if (player.teleSelected == 2) {//abby sire
				player.getPA().startTeleport(3038, 4767, 0, "modern", false);
			}else if (player.teleSelected == 3) {//revs
				player.getPA().startTeleport(3127, 3835, 0, "modern", false);
			}else if (player.teleSelected == 4) {//revs
				player.getPA().startTeleport(3105, 3249, 0, "modern", false);
			}
			break;
		 //vorkath
		case 174250://boss monkeys
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) {
				player.getPA().startTeleport(3053, 9578, 0, "modern", false);
			} else if (player.teleSelected == 2) {
			player.getPA().startTeleport(2124, 5660, 0, "modern", false);
			}else if (player.teleSelected == 3) {//black chins
				player.getPA().startTeleport(3137, 3767, 0, "modern", false);
			}
			break;
		
		case 174254:
			if (!teleportCheck(player))
				return;
			 if (player.teleSelected == 0) {
				player.getPA().startTeleport(2709, 9476, 0, "modern", false);
			} else if (player.teleSelected == 2) {
				player.getPA().startTeleport(1558, 3696, 0, "modern", false);
			} else if (player.teleSelected == 3) { //elder chaos druids
					player.getPA().startTeleport(3232, 3642, 0, "modern", false);
			}
			break;
		
		case 174258:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Brimhaven Dungasgarnianeon
				player.getPA().startTeleport(2124, 5660, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[10]+".");
			} else if (player.teleSelected == 2) { // Bosses - Zulrah - Instanced
				player.getPA().startTeleport(2202, 3056, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[10]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Elemental
				player.getPA().startTeleport(3281, 3910, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[10]+".");
			} else if (player.teleSelected == 4) { // City - Camelot
				player.getPA().startTeleport(2757, 3477, 0, "modern", false);
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
			} else if (player.teleSelected == 5) { // Legendary - Skeletal Wyverns

			}
			break;
		case 175014:
		case 175018:
		case 175022:
		case 175026:
		case 175030:
		case 175034:
		case 175038:
			player.sendMessage("Please choose a teleport!");
				return;
		case 254046:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Taverley Dungeon
				player.getPA().startTeleport(2884, 9796, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[11]+".");
			} else if (player.teleSelected == 2) { // Bosses - Cerberus
				player.getPA().startTeleport(2873, 9847, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[11]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Fanatic
				player.getPA().startTeleport(2981, 3836, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[11]+".");
			} else if (player.teleSelected == 4) { // City - Catherby
				player.getPA().startTeleport(2813, 3447, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[11]+".");
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CATHERY_TELEPORT);
			} else if (player.teleSelected == 5) { // Legendary - Fanatic/Archaeologist

			}
			break;
		case 254048:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Stronghold Slayer Dungeon
				player.getPA().startTeleport(2432, 3423, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[12]+".");
			} else if (player.teleSelected == 2) { // Bosses - Thermonuclear Smoke Devil
				if (player.playerLevel[18] < 93) {
					player.sendMessage("You need a Slayer level of 93 to kill these.");
					return;
				}
				player.getPA().startTeleport(2376, 9452, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[12]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Crazy Archaeologist
				player.getPA().startTeleport(2984, 3713, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[12]+".");
			} else if (player.teleSelected == 4) { // City - Al Kharid
				player.getPA().startTeleport(3293, 3174, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[12]+".");
			} /*
				 * else if (c.getRights().isOrInherits(Right.LEGENDARY) ||
				 * c.getRights().isOrInherits(Right.OWNER)) { if (player.teleSelected == 5) { //
				 * Legendary - Runecrafting player.getPA().showInterface(26100);
				 * //player.getPA().startTeleport(1992,4530, 3, "modern");
				 * player.sendMessage("Opening "+donatorNames[12]+" Interface."); } }
				 */
			break;
		case 254050:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Relleka Dungeon
				player.getPA().startTeleport(2808, 10002, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[13]+".");
			} else if (player.teleSelected == 2) { // Bosses - Abyssal Sire
				player.getPA().startTeleport(3039, 4788, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[13]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Skeletal Wyverns
				player.getPA().startTeleport(2963, 3916, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[13]+".");
			} else if (player.teleSelected == 4) { // City - Morytania
				player.getPA().startTeleport(3432, 3451, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[13]+".");
			}
			break;
		case 254052:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Mithril Dragons
				player.getPA().startTeleport(1746, 5323, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[14]+".");
			} else if (player.teleSelected == 2) { // Bosses - Demonic Gorillas
				player.getPA().startTeleport(2130, 5647, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[14]+".");
			} else if (player.teleSelected == 3) { // Wilderness - East Dragons
				player.getPA().startTeleport(3351, 3659, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[14]+".");
			} else if (player.teleSelected == 4) { // City - Shilo Village
				player.getPA().startTeleport(2827, 2995, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[14]+".");
			}
			break;
		case 254054:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Asgarnian Ice Cave
				player.getPA().startTeleport(3029, 9582, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[15]+".");
			} else if (player.teleSelected == 2) { // Bosses - Lizardman Shaman
				player.getPA().startTeleport(1469, 3687, 0, "modern", false);
				// player.sendMessage("Teleporting to "+bossNames[15]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Wildy Volcano
				player.getPA().startTeleport(3366, 3935, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[15]+".");
			} else if (player.teleSelected == 4) { // City - Waterbirth Tele
				player.getPA().startTeleport(2508, 3725, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[15]+".");
			}
			break;
		case 254056:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Catacombs
				player.getPA().startTeleport(1630, 3673, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[16]+".");
				player.sendMessage("Click on the statue to enter the Catacombs!");
			} else if (player.teleSelected == 3) { // Wilderness - Chaos Altar
				player.getPA().startTeleport(3236, 3628, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[16]+".");
			} else if (player.teleSelected == 4) { // City - Lletya
				player.getPA().startTeleport(2352, 3162, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[16]+".");
			}
			break;
		case 254058:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Cave Kraken
				player.getPA().startTeleport(2277, 10001, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[17]+".");
			} else if (player.teleSelected == 3) { // Wilderness - Lava Dragons
				player.getPA().startTeleport(3202, 3860, 0, "modern", false);
				// player.sendMessage("Teleporting to "+wildernessNames[17]+".");
			} else if (player.teleSelected == 4) { // City - Brimhaven
				player.getPA().startTeleport(2898, 3546, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[17]+".");
			}
			break;
		case 254060:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 0) { // Monsters - Smoke Devils
				if (player.playerLevel[18] < 93) {
					player.sendMessage("You need a Slayer level of 93 to kill these.");
					return;
				}
				player.getPA().startTeleport(2404, 9415, 0, "modern", false);
				// player.sendMessage("Teleporting to "+monsterNames[18]+".");
			} else if (player.teleSelected == 4) { // City - Entrana
				player.getPA().startTeleport(2827, 3336, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[18]+".");
			}
		case 254062:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 4) { // City - Draynor
				player.getPA().startTeleport(3077, 3252, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[19]+".");
			}
			break;
		case 254064:
			if (!teleportCheck(player))
				return;
			if (player.teleSelected == 4) { // City - Trollheim
				player.getPA().startTeleport(2911, 3612, 0, "modern", false);
				// player.sendMessage("Teleporting to "+cityNames[20]+".");
			}
			break;
		// End of Buttons
		}
	}
}