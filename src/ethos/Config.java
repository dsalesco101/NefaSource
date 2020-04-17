package ethos;

import ethos.model.content.lootbag.LootingBag;

public class Config {

	public static String SERVER_NAME = "Wisdom";

	public static int PLAYERMODIFIER = 0;

	// Make sure this matches client version in configuration.java and the cache version.txt
	public static final int CLIENT_VERSION = 142;

	/**
	 * Decides max incoming packets per cyclebank
	 */
	public static final int MAX_INCOMING_PACKETS_PER_CYCLE = 120;

	/**
	 * Combat applications
	 */
	public static boolean BOUNTY_HUNTER_ACTIVE = true;
	public static boolean NEW_DUEL_ARENA_ACTIVE = true;
	
	/**
	 * Controls where characters will be saved                                          
	 */                                                                                      
	public static String CHARACTER_SAVE_DIRECTORY = "./Data/Characters/";
	public static String ERROR_LOG_FILE = "error_log.txt";
	public static String ATTRIBUTES_FILE = "./data/server_attributes.json";
	public static String USER_FOLDER = System.getProperty("user.home");
	
	public static boolean sendServerPackets = false;

	public static final int[] CHEATPACKETS =
			/** P1****P2***P3***P4****P5***P6**P7**P8 ***/
			{  };

	/**
	 * The highest amount ID. Change is not needed here unless loading items higher
	 * than the 667 revision.
	 */
	public static final int ITEM_LIMIT = 40000;

	/**
	 * An integer needed for the above code.
	 */
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	/**
	 * The size of a players bank.
	 */
	public static final int BANK_CAPACITY = 550;

	/**
	 * The size of a bank tab.
	 */
	public static final int BANK_TAB_SIZE = 350;

	/**
	 * The max amount of players until your server is full.
	 */
	public static final int MAX_PLAYERS = 150;

	/**
	 * The maximum number of npcs allowed in the local player's list.
	 */
	public static final int MAX_NPCS_IN_LOCAL_LIST = 150;

	/**
	 * The delay of logging in from connections. Do not change this.
	 */
	public static final int CONNECTION_DELAY = 100;

	/**
	 * How many IP addresses can connect from the same network until the server
	 * rejects another.
	 */
	public static final int IPS_ALLOWED = 3;
	
	/**
	 * Determines whether or not the server is in a beta state.
	 */
	public static final boolean BETA_MODE = false;

	/**
	 * Items that cannot be sold in any stores.
	 */

	/**
	 * Items that cannot be sold in any stores.
	 */
	public static final int[] ITEM_SELLABLE = {22545, 22550, 22555, 21898, 33033, 33034, 33035, 33036, 33037, 33038, 33039, 33040, 33041, 33042,
			33043, 33044, 33045, 33046, 33047, 33048, 33049, 33050, 33051, 33052, 33053, 33054, 33055, 4083, 2677, 2722, 2801, 2714, 2775, 2802,
			12899, 21816, 11730, 11731, 11732, 11733, 22322, 13190, 9927, 2841, 13072, 19730, 12020, 6713, 13307, 12013, 12014, 12015, 12016, 20008, 776, 10071, 13258, 132259,
			13260, 13261, 10933, 10939, 10940, 10941, 10945, 13640, 13642, 13644, 13646, 5553, 5554, 5555, 5556, 5557,
			20704, 20706, 20708, 20710, 13667, 13669, 13671, 13673, 13675, 13677, 21061, 21064, 21067, 21070, 21073, 21076, 20760, 20659, 20659, 20661, 20663,
			20665, 20667, 20669, 20671, 20673, 20675, 20677, 20679, 20681, 20683, 20685, 20687, 20689, 20691, 13133,
			13134, 13135, 13136, 13274, 13275, 13276, 13262, 13092, 20005, 20017, 19841, 13323, 13324, 13325, 13326,
			19722, 12647, 19835, 1409, 1410, 13121, 13122, 13123, 13124, 13141, 13142, 13143, 13144, 13117, 13118,
			13119, 13120, 13129, 13130, 13131, 13132, 13125, 13126, 13127, 13128, 13137, 13138, 13139, 13140, 11136,
			11138, 11140, 13103, 13112, 13113, 13114, 13115, 13104, 13105, 13106, 13107, 13141, 13142, 13143, 13144,
			13108, 13109, 13110, 13111, 1555, 1556, 10551, 1557, 1558, 1559, 1560, 12646, 13177, 13178, 11995, 13579, 13580,
			13581, 13582, 13583, 13584, 13585, 13586, 13587, 13588, 13589, 13590, 13591, 13592, 13593, 13594, 13595,
			13596, 13597, 13598, 13599, 13600, 13601, 13602, 13603, 13604, 13605, 13606, 13607, 13609, 13610, 13611,
			13612, 13613, 13614, 13615, 13616, 13617, 13618, 13619, 13620, 13621, 13622, 13623, 13624, 13625, 13626,
			13627, 13628, 13629, 13630, 13631, 13632, 13633, 13634, 13635, 13636, 13648, 13649, 13650, 12773, 12774,
			 2415, 2416, 2417, 13199, 13197, 12810, 12811, 12812, 12813, 12814, 12815, 11919, 12956, 12957, 12958,
			12959, 12899, 12926, 12931, 12006, 12853, 611, 13320, 13321, 13322, 13226, 12816, 12791, 5733,
			13225, 13247, 8014, 8015, 9958, 9959, 5509, 5510, 5512, 11849, 12840, 775, 12648, 13116, 13069, 13070,
			13072, 13073, 12921, 12954, 11865, 11864, 15573, 8135, 1050, 1051, 1044, 1045, 1046, 1047, 1048, 1049, 1052,
			3839, 3840, 3841, 3842, 3843, 3844, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 6570, 7462,
			7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784,
			9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764,
			9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768,
			9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 2639, 2641, 2643, 10802,
			10804, 10806, 12321, 12323, 12325, 6746, 19675, 21003, 21004, 21012, 21013, 21000, 21001, 21009, 21010, 21006, 21007,
			21015, 21016, 21018, 21019, 21021, 21022, 21024, 21025,12650, 12649, 12651, 12652, 12644, 12645,
			12643, 11995, 15568, 12653, 12655, 13178, 12646, 13179, 13177, 12921, 13181, 12816, 12647,299,21992, 3144, 13442, 13441,
			LootingBag.LOOTING_BAG, LootingBag.LOOTING_BAG_OPEN,
	};
	/**
	 * Items that cannot be traded or staked.
	 */
	public static final int[] NOT_SHAREABLE = {22545, 22550, 22555, 21898, 33033, 33034, 33035, 33036, 33037, 33038, 33039, 33040, 33041, 33042,
			33043, 33044, 33045, 33046, 33047, 33048, 33049, 33050, 33051, 33052, 33053, 33054, 33055, 4083, 20834, 22719, 2520, 2677, 2722, 2801, 2714, 2775, 2802, 23943, 12899, 13197, 12931,13199, 7979, 7980, 13489, 13490, 2425, 21907, 7978, 7976, 7977, 7984,
			21816, 20374, 20370, 20372, 20368, 19720, 12436, 20366, 11730, 11731, 11732, 11733, 21027, 22322, 13190, 10508, 10553, 10555, 11021, 2841, 11019, 11020, 11022, 4566, 1961, 1989, 6863, 6862,
			1959, 13072, 9927,11899,22109,13258, 13259, 13260, 13261, 19730, 12020, 6713, 12013, 10071,
			12014, 12015, 12016, 20008, 776, 13258, 132259, 13260, 13261, 10933, 10939, 10940, 10941, 10945, 13640,
			13642, 13644, 13646, 5553, 5554, 5555, 5556, 5557, 20704, 20706, 20708, 20710, 13667, 13669, 13671, 13673,
			13675, 13677, 20760, 20659, 20659, 20661, 20663, 20665, 20667, 20669, 20671, 20673, 20675, 20677, 20679,
			20681, 20683, 20685, 20687, 20689, 20691, 13133, 13134, 13135, 13136, 21061, 21064, 21067, 21070, 21073, 21076, 13274, 13275, 13276, 13262, 13092,
			19841, 13323, 13324, 13325, 13326, 19722, 12647, 19835, 19639, 19641, 19643, 19645, 19647, 19649, 1409,
			1410, 13121, 13122, 13123, 13124, 13141, 13142, 13143, 13144, 13117, 13118, 13119, 13120, 13129, 13130,
			13131, 13132, 13125, 13126, 13127, 13128, 13137, 13138, 13139, 13140, 11136, 11138, 11140, 13103, 13112,
			13113, 13114, 13115, 13104, 13105, 13106, 13107, 13141, 13142, 13143, 13144, 13108, 13109, 13110, 13111,
			1555, 1556, 1557, 1558, 1559, 1560, 12646, 13177, 13178, 11995, 13579, 13580, 13581, 13582, 13583, 13584,
			13585, 13586, 13587, 13588, 13589, 13590, 13591, 13592, 13593, 13594, 13595, 13596, 13597, 13598, 13599,
			13600, 13601, 13602, 13603, 13604, 13605, 13606, 13607, 13609, 13610, 13611, 13612, 13613, 13614, 13615,
			13616, 13617, 13618, 13619, 13620, 13621, 13622, 13623, 13624, 13625, 13626, 13627, 13628, 13629, 13630,
			13631, 13632, 13633, 13634, 13635, 13636, 13648, 13649, 13650, 12773, 12774, 13320, 13321, 13322, 13226,
			12816, 12791, 13280, 13281, 13329, 13330, 13331, 13332, 13333, 13334, 13335, 13336, 13337, 13338,
			13181, 5733, 13225, 13247, 9958, 9959, 5509, 5510, 5512, 11850, 11852, 11854, 11856, 11858, 11860, 11861,
			11849, 12840, 775, 12648, 13116, 13069, 13070, 13072, 13073, 7509, 8841, 4081, 4202, 2415, 2416, 2528,
			2417, 12921, 13199, 13197, 12810, 12811, 12812, 12813, 12814, 12815, 11919, 12956, 12957, 12958, 12959,
			12926, 12931,  12954, 15573, 8135, 11865, 11864, 12373, 12379, 12369, 12365, 12363, 10507, 15098,
			3839, 3840, 3841, 3842, 3843, 3844, 6822, 6824, 6826, 6830, 6832, 6834, 6836, 6838, 6840, 6842, 6844,
			6846, 6848, 6850, 12853, 1960, 611, 1960, 9920, 9921, 9922, 9923, 9924, 9925, 12845, 3842, 3844, 3840, 8844,
			8845, 8846, 8847, 8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 9748,
			9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787,
			9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776,
			9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780,
			9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 8839, 8840, 8842, 10271, 10273, 10275, 10277, 10279, 10269,
			11663, 11664, 15572, 11665, 10499, 10548, 15098, 12650, 12649, 12651, 12652, 15567, 12644, 12645, 12643,
			15568, 12653, 12655, 15571, 7582, 12848, 12855, 12856, 12808, 12806, 12807, 7449, 12796, 12748,
			12749, 12750, 12751, 12752, 12753, 12754, 12755, 12756, 12211, 12205, 12207, 12213, 12241,
			12243, 12283, 12277, 12279, 12281, 12309, 12311, 12313, 12514, 12299, 12301, 12303, 12305, 12307, 12321,
			12323, 12325, 12516, 19675 ,21273,21197,21295,12638,12639,12637,11889,6529,21791,21793,21795,2425,299,21992,22376,22378,22380,22382,
			LootingBag.LOOTING_BAG, LootingBag.LOOTING_BAG_OPEN
	};
	/**
	 * Items that are kepts on death
	 */

	public static final int[] ITEMS_KEPT_ON_DEATH = { 21898, 33033, 33034, 33035, 33036, 33037, 33038, 33039, 33040, 33041, 33042,
			33043, 33044, 33045, 33046, 33047, 33048, 33049, 33050, 33051, 33052, 33053, 33054, 33055, 22547, 21295, 22542, 22552, 9927,8025,22109,19564, 21285,12791,6713, 12013, 10071, 12014, 12015, 12016, 20008, 776, 13258,
			132259, 13260, 13269, 13261, 10933, 10939, 10940, 10941, 10945, 13640, 13642, 13644, 13646, 5553, 5554, 5555, 5556,
			5557, 20704, 20706, 20708, 20710, 13667, 13669, 13671, 13673, 13675, 13677, 20760, 20659, 20659, 20661,
			20663, 20665, 20667, 20669, 20671, 20673, 20675, 20677, 20679, 20681, 20683, 20685, 20687, 20689, 20691,
			13133, 13134, 13135, 13136, 13262, 13092, 19722, 20005, 20017, 19841, 21061, 21064, 21067, 21070, 21073, 21076, 13323, 13324, 13325, 13326, 12647,
			19639, 19641, 19643, 19645, 19647, 19649, 13226, 1409, 1410, 13121, 13122, 13123, 13124, 13141, 13142,
			13143, 13144, 13117, 13118, 13119, 13120, 13129, 13130, 13131, 13132, 13125, 13126, 13127, 13128, 13137,
			13138, 13139, 13140, 11136, 11138, 11140, 13103, 13112, 13113, 13114, 13115, 13104, 13105, 13106, 13107,
			13141, 13142, 13143, 13144, 13108, 13109, 13110, 13111, 1555, 1556, 1557, 1558, 1559, 1560, 12646, 13177,
			13178, 11995, 13579, 13580, 13581, 13582, 13583, 13584, 13585, 13586, 13587, 13588, 13589, 11861, 13590,
			13591, 13592, 13593, 13594, 13595, 13596, 13597, 13598, 13599, 13600, 13601, 13602, 13603, 13604, 13605,
			13606, 13607, 13609, 13610, 13611, 13612, 13613, 13614, 13615, 13616, 13617, 13618, 13619, 13620, 13621,
			13622, 13623, 13624, 13625, 13626, 13627, 13628, 13629, 13630, 13631, 13632, 13633, 13634, 13635, 13636,
			13320, 13321, 13322, 12816, 13280, 13281, 13329, 13330, 13331, 13332, 13333, 13334, 13335, 13336, 13337,
			13338, 13181, 11850, 11852, 11854, 11856, 11858, 11860, 13181, 5733, 775, 9958, 9959, 12840, 12648, 13116,
			13072, 13073, 7509, 4081, 8841, 4202, 2415, 2416, 2417, 12921, 12810, 12811, 12812, 12813, 12814, 12815,
			11919, 12956, 12957, 12958, 12959, 12954, 15573, 8135, 11864, 11865, 12432, 12373, 12379, 12369,
			12367, 12365, 12363, 3839, 3840, 3841, 3842, 3843, 3844, 7449, 611, 8840, 8839, 8842, 11664, 15098, 12650,
			12649, 12651, 12652, 15567, 12644, 12645, 12643, 15568, 12653, 12655, 15571, 11663, 11665, 6570, 8845, 8846,
			8847, 8848, 8849, 8850, 10551, 10548, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7582, 15572, 12855,
			12856, 13274, 13275, 13276, 19675,21295,12638,12639,12637,11889,21791,21793,21795,21786,21784,21782,21780,21778,21776, 21992};
	/**
	 * Items lost above 20 wilderness
	 */

	public static final int[] LOST_ITEMS_ABOVE_20 = {10548, 8839, 8840, 8841, 8842, 10663, 10664, 10665, 13072, 13073, 21295, 6570};
	/**
	 * dragon ids for the dragon hunter crossbow
	 */
	public static final int[] DRAG_IDS = { 137, 139, 239, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252,
	            253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273,
	            274, 275, 1871, 1872, 2641, 2642, 2918, 2919, 4000, 4385, 5194, 5872, 5873, 5878, 5879, 5880, 5881, 5882,
	            6500, 6501, 6502, 6593, 7039, 7253, 7254, 7255, 7273, 7274, 7275, 8028, 465, 8030, 8031, 7544, 7550, 7551, 7552, 7553,
	            7554, 7555, 8615, 8619, 8620, 8621, 8622, 9033, 8609, };

	 /**
	 * Items that are deleted on death
	 */
	public static final int[] DROP_AND_DELETE_ON_DEATH = { 13190, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784,
			9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764,
			9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768,
			9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 13226,
			5733, 5509, 5510, 5512, 11849, 13069, 13070, 6822, 6824, 6826, 6830, 6832, 6834, 6836, 6838,
			6840, 6842, 6844, 6846, 6848, 6850, 10507, 10499, 5509, 5510, 5512, 5514, 6819 };

	/**
	 * Items that cannot be dropped.
	 */
	public static final int[] TOURNY_PERMITS = { 3144, 385, 11936, 13441 };
	
	/**
	 * undead npcs.
	 */
	public static final int[] UNDEAD_NPCS = {2, 3, 4, 5, 6, 7, 2514, 2515, 2516, 2517, 2518, 2519, 6608,
			7257, 7296, 7864, 414, 120, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 945, 946, 5622,
			5623, 5624, 5625, 5626, 5627, 85, 86, 87, 88, 89, 90, 717, 718, 719, 720, 721, 722,
			723, 724, 725, 726, 727, 728, 7881, 7931, 7932, 7933, 7934, 7935, 7936, 7937, 7938,
			7939, 7940, 5633, 6740, 7258, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 872, 878, 879, 2999,
			2993, 4421, 3969, 3970, 3971, 8026, 8058, 8059 ,8060, 8061, 4500, 4505, 882, 5353, 5354, 5355, 3616, 547, 1163, 
			8359, 6611, 6612, 1672, 1673, 1674, 1675, 1676, 1677, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 
			1049, 1050, 1051, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 8062, 8063, 866, 867, 868,  869, 870, 871, 873, 874, 4312, 4318, 4319};

 /**
 * Items that are deleted on death
 */

	/**
	 * Items that cannot be dropped in a tournament.
	 */

	public static final int[] UNDROPPABLE_ITEMS = { 13190, 6713, 19841, 11919, 12956, 12957, 12958, 12959, 12899, 11907, 12432,
			12369, 12365, 12363, 6822, 6824, 6826, 6830, 6832, 6834, 6836, 6838, 6840, 6842,
			6844, 6846, 6848, 6850, 19675,9927, 2993, 4421 };

	/**
	 * Items that can be dropped but need dialogue
	 */
	public static final int[] CHECK_BEFORE_DROP = { 12791, LootingBag.LOOTING_BAG, LootingBag.LOOTING_BAG_OPEN };

	/**
	 * Items that are listed as fun weapons for duelling.
	 */
	public static final int[] FUN_WEAPONS = { 4151, 5698, 1231, 1215, 5680 };

	/**
	 * Items that are unspawnable
	 */
	public static final String[] UNSPAWNABLE = { "ahrim", "reindeer", "star bauble", "bauble", "torag", "dharok",
			"overload", "tenderiser", "woolly", "bobble", "jester", "gilded", "legend", "hell", "dragon spear", "odium",
			"malediction", "callisto", "gods", "yrannical", "treasonous", "granite maul", "ancient mace",
			"super combat", "dragon halberd", "torstol", "d hally", "dragon hally", "karil", "defender icon",
			"attacker icon", "picture", "collector icon", "collecter icon", "healer icon", "crystal key", "essence",
			"3rd", "third", "bomb", "karamb", "guthan", "verac", "void", "uncut", "Restrict", "onyx amulet",
			"onyx ring", "spirit", "chisel", "statius", "vesta", "morrigan", "zuriel", "occult", "trident",
			"mystic smoke", "mystic steam", "tentacle", "dark bow", "ranger boots", "robin hood hat", "attack cape",
			"defence cape", "strength cape", "prayer cape", "constitution", "range cape", "ranged cape", "ranging cape",
			"magic cape", "herblore", "agility", "fletching", "crafting", "runecrafting", "runecraft", "farming",
			"hunter", "slayer", "summoning", "construction", "woodcutting", "firemaking", "fishing", "cooking",
			"smithing", "mining", "thieving", "arcane", "divine", "spectral", "wealth", "elysian", "spirit", "status",
			"hand cannon", "visage", "raw", "logs", "bar", "ore", "uncut", "dragon leather", "scroll", "hatchet",
			"iron axe", "steel axe", "bronze axe", "rune axe", "adamant axe", "mithril axe", "black axe", "dragon axe",
			"vesta", "pumpkin", "statius's", "zuriel", "morrigan", "dwarven", "statius", "fancy", "rubber", "sled",
			"flippers", "camo", "lederhosen", "mime", "lantern", "santa", "scythe", "bunny", "h'ween", "hween", "clue",
			"casket", "cash", "box", "cracker", "zuriel's", "Statius's", "torso", "fighter", "Statius", "skeleton",
			"chicken", "zamorak platebody", "guthix platebody", "saradomin plate", "grim reaper hood", "armadyl",
			"bandos", "armadyl cross", "graardor", "zilyana", "kree", "tsut", "mole", "kraken", "dagannoth",
			"king black dragon", "chaos ele", "staff of the dead", "staff of dead", "(i)", "ticket", "PK Point",
			"jester", "dragon defender", "fury", "mithril defender", "adamant defender", "rune defender", "elysian",
			"mystery box", "arcane", "chaotic", "Chaotic", "stream", "broken", "deg", "corrupt", "fire cape", "sigil",
			"godsword", "void seal", "lunar", "hilt", "(g)", "mage's book", "berserker ring", "warriors ring",
			"warrior ring", "warrior's ring", "archer", "archer's ring", "archer ring", "archers' ring", "seers' ring",
			"seer's ring", "seers ring", "mages' book", "wand", "(t)", "guthix", "zamorak", "saradomin", "scythe",
			"bunny ears", "zaryte bow", "armadyl battlestaff", "(i)", "infinity", "slayer", "korasi", "staff of light",
			"dice", "ardougne", "unarmed", "gloves", "dragon claws", "party", "santa", "completionist", "null", "coins",
			"Kaodai", "Elder", "tokhaar-kal", "tokhaar", "sanfew", "dragon defender", "zaryte", "coupon", "flippers",
			"dragonfire shield", "sled", };

	/**
	 * NPCs that are of the undead kind
	 */
	public static final int[] UNDEAD_IDS = { 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
			45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 120,
			130, 414, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 717, 720, 721, 722, 723, 724, 725, 726, 727,
			728, 866, 867, 868, 869, 870, 871, 873, 874, 875, 876, 877, 880, 920, 924, 945, 946, 949, 950, 951, 952,
			953, 1538, 1539, 1541, 1685, 1686, 1687, 1688, 1784, 1785, 1786, 2501, 2502, 2503, 2504, 2505, 2506, 2507,
			2508, 2509, 2514, 2515, 2516, 2517, 2518, 2519, 2520, 2521, 2522, 2523, 2524, 2525, 2526, 2527, 2528, 2529,
			2530, 2531, 2532, 2533, 2534, 2992, 2993, 2999, 3516, 3565, 3584, 3617, 3625, 3969, 3970, 3971, 3972, 3973,
			3974, 3975, 3976, 3977, 3978, 3979, 3980, 3981, 4421, 5237, 5342, 5343, 5344, 5345, 5346, 5347, 5348, 5349,
			5350, 5351, 5370, 5506, 5507, 5568, 5571, 5574, 5583, 5622, 5623, 5624, 5625, 5626, 5627, 5633, 5647, 6441,
			6442, 6443, 6444, 6445, 6446, 6447, 6448, 6449, 6450, 6451, 6452, 6453, 6454, 6455, 6456, 6457, 6458, 6459,
			6460, 6461, 6462, 6463, 6464, 6465, 6466, 6467, 6468, 6596, 6597, 6598, 6608, 6611, 6612, 6740, 6741, 4319, 4318, 4312 };

	/**
	 * NPCs that represent demons for the Arclight
	 */
	public static final int[] DEMON_IDS = { 1531, 3134, 2006, 2026, 7244, 1432, 415, 7410, 135, 3133, 484, 1619, 7276,
			3138, 7397, 7398, 11, 7278, 7144, 7145, 7146, 3129, 3132, 3130, 3131, 7286, 5890, 7286 };

	/**
	 * The point in where you spawn in a duel. Do not change this.
	 */
	public static final int RANDOM_DUELING_RESPAWN = 0;

	/**
	 * The level in which you can not teleport in the wild, and higher.
	 */
	public static final int NO_TELEPORT_WILD_LEVEL = 20;
	/**
	 * The level in which you can not teleport above 30 wildy using glory.
	 */
	public static final int glory = 30;

	/**
	 * The time, in game cycles that the skull above a player should exist for.
	 * Every game cycle is 600ms, every minute has 60 seconds. Therefor there are
	 * 100 cycles in 1 minute. .600 * 100 = 60.
	 */
	public static final int SKULL_TIMER = 2000;

	/**
	 * The maximum time for a player skull with an extension in the length.
	 */
	public static final int EXTENDED_SKULL_TIMER = 6000;

	/**
	 * How long the teleport block effect takes.
	 */
	public static final int TELEBLOCK_DELAY = 20000;

	/**
	 * Single and multi player killing zones.
	 */
	public static final boolean SINGLE_AND_MULTI_ZONES = true;

	/**
	 * Wilderness levels and combat level differences. Used when attacking players.
	 */
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;

	/**
	 * Skill names and id's
	 */
	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter" };

	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 16;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	public static final int HUNTER = 22;

	/**
	 * Combat experience rates.
	 */
	public static final int MELEE_EXP_RATE = 250;
	public static final int RANGE_EXP_RATE = 250;
	public static final int MAGIC_EXP_RATE = 250;
	/**
	 * Special server experience bonus rates. (Double experience weekend etc)
	 */
	public static final double SERVER_EXP_BONUS = 1.0;
	/**
	 * XP given when XP is boosted by a voting reward only
	 */
	public static final double SERVER_EXP_BONUS_BOOSTED = 1.0;
	/**
	 * XP given when XP is boosted by a voting reward and bonus weekend
	 */
	public static final double SERVER_EXP_BONUS_WEEKEND_BOOSTED = 1.5; 
	/**
	 * XP given when XP is boosted by bonus mode only
	 */ 
	public static double SERVER_EXP_BONUS_WEEKEND = 1.5;
	public static final double BONUS_EXP_WOGW = 2.0;

	/**
	 * Bonus modes Manually set while server is live
	 */
	public static boolean ONE_YEAR_QUIZ = false;
	public static String QUESTION = "";
	public static String ANSWER = "";
	public static boolean BONUS_WEEKEND = false;
	public static boolean BONUS_XP_WOGW = false;
	public static boolean DOUBLE_SCROLL_EXP = false;
	public static boolean BONUS_PC = true;
	public static boolean CYBER_MONDAY = false;
	public static boolean DOUBLE_DROPS = false;
	public static boolean BONUS_PC_WOGW = false;
	public static int BARROWS_RARE_CHANCE = 1;
	public static boolean DOUBLE_PKP = false;
	public static boolean DOUBLE_VOTE_INCENTIVES = false;
	public static boolean superiorSlayerActivated = true;
	public static boolean wildyPursuit = false;

	public static int AMOUNT_OF_SANTA_MINIONS = 10;
	/**
	 * Looting bag and rune pouch
	 */
	public static boolean BAG_AND_POUCH_PERMITTED = true;

	/**
	 * SQL override, should be true in environments with no SQL setup properly
	 */
	public static boolean BLOCK_SQL = true;

	/**
	 * How fast the special attack bar refills.
	 */
	public static final int INCREASE_SPECIAL_AMOUNT = 31000;

	/**
	 * If you need more than one prayer point to use prayer.
	 */
	public static final boolean PRAYER_POINTS_REQUIRED = true;

	/**
	 * If you need a certain prayer level to use a certain prayer.
	 */
	public static final boolean PRAYER_LEVEL_REQUIRED = true;

	/**
	 * If you need a certain magic level to use a certain spell.
	 */
	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	/**
	 * How long the god charge spell lasts.
	 */
	public static final int GOD_SPELL_CHARGE = 300000;

	/**
	 * If you need runes to use magic spells.
	 */
	public static final boolean RUNES_REQUIRED = true;

	/**
	 * If you need correct arrows to use with bows.
	 */
	public static final boolean CORRECT_ARROWS = true;

	/**
	 * If the crystal bow degrades.
	 */
	public static final boolean CRYSTAL_BOW_DEGRADES = true;

	/**
	 * How often the server saves data.
	 */
	public static final int SAVE_TIMER = 60; // Saves every one minute.

	/**
	 * How far NPCs can walk.
	 */
	public static final int NPC_RANDOM_WALK_DISTANCE = 8; // 5x5 square, NPCs
															// would be able to
															// walk 25 squares
															// around.

	/**
	 * The starting location of your server.
	 */
	public static final int START_LOCATION_X = 3085;
	public static final int START_LOCATION_Y = 3495;

	/**
	 * The re-spawn point of when someone dies.
	 */
	public static final int RESPAWN_X = 3087;
	public static final int RESPAWN_Y = 3500;
	public static final int RESPAWN_Z = 0;

	/**
	 * The re-spawn point of when a duel ends.
	 */
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	/**
	 * Glory locations.
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3493;

	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3176;

	public static final int KARAMJA_X = 2925;
	public static final int KARAMJA_Y = 3173;

	public static final int DRAYNOR_X = 3079;
	public static final int DRAYNOR_Y = 3250;

	public static final int MAGEBANK_X = 2538;
	public static final int MAGEBANK_Y = 4716;

	
	/*
	 * Modern spells
	 */
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;

	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;

	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;

	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;

	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;

	public static final int WATCHTOWER_X = 2549;
	public static final int WATCHTOWER_Y = 3112;

	public static final int TROLLHEIM_X = 2888;
	public static final int TROLLHEIM_Y = 3676;

	/*
	 * Ancient spells
	 */
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 2966;
	public static final int DAREEYAK_Y = 3695;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;

	/**
	 * Lunar spells
	 */
	public static final int MOONCLAN_X = 2111;
	public static final int MOONCLAN_Y = 3915;

	public static final int OURANIA_X = 2469;
	public static final int OURANIA_Y = 3245;

	public static final int WATERBIRTH_X = 2550;
	public static final int WATERBIRTH_Y = 3755;

	public static final int BARBARIAN_X = 2544;
	public static final int BARBARIAN_Y = 3569;

	public static final int KHAZARD_X = 2634;
	public static final int KHAZARD_Y = 3168;

	public static final int FISHING_GUILD_X = 2614;
	public static final int FISHING_GUILD_Y = 3381;

	public static final int CATHERBY_X = 2804;
	public static final int CATHERBY_Y = 3434;

	public static final int ICE_PLATEU_X = 2951;
	public static final int ICE_PLATEU_Y = 3936;

	/**
	 * Timeout time.
	 */
	public static final int TIMEOUT = 20;

	/**
	 * Cycle time.
	 */
	public static final int CYCLE_TIME = 600;

	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 512;

	/**
	 * Skill experience multipliers.
	 */
	public static final int WOODCUTTING_EXPERIENCE = 55;
	public static final int MINING_EXPERIENCE = 55;
	public static final int SMITHING_EXPERIENCE = 60;
	public static final int FARMING_EXPERIENCE = 25;
	public static final int FIREMAKING_EXPERIENCE = 50;
	public static final int HERBLORE_EXPERIENCE = 60;
	public static final int FISHING_EXPERIENCE = 55;
	public static final int AGILITY_EXPERIENCE = 70;
	public static final int PRAYER_EXPERIENCE = 50;
	public static final int RUNECRAFTING_EXPERIENCE = 75;
	public static final int CRAFTING_EXPERIENCE = 45;
	public static final int THIEVING_EXPERIENCE = 80;
	public static final int SLAYER_EXPERIENCE = 70;
	public static final int COOKING_EXPERIENCE = 40;
	public static final int FLETCHING_EXPERIENCE = 40;
	public static final int HUNTER_EXPERIENCE = 60;
	
	public static final String COMBAT_SCRIPT_DIR = "ethos.model.players.combat";
}