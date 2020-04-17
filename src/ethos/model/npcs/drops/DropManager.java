package ethos.model.npcs.drops;

import ethos.model.content.lootbag.LootingBag;
import ethos.model.items.ItemList;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ethos.Server;
import ethos.model.content.godwars.Godwars;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDefinitions;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.skills.slayer.SlayerMaster;
import ethos.model.players.skills.slayer.Task;
import ethos.util.Location3D;
import ethos.util.Misc;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DropManager {

	public static int amountOfTables = 0;

	private static final int DEFAULT_NPC = 2662; // Vorkath
	private static final int NPC_RESULTS_CONTAINER_INTERFACE_ID = 39507;
	private static final int DROP_TABLE__CONTAINER_INTERFACE_ID = 34000;
	private static final int DROP_AMOUNT_CONFIG_ID = 1356;
	private static final String LAST_OPENED_TABLE_KEY = "drop_manager_last_opened";

	private static final Comparator<Integer> COMPARE_NAMES =(o1, o2) -> {
		String name1 = NPCDefinitions.get(o1).getNpcName();
		String name2 = NPCDefinitions.get(o2).getNpcName();
		return name1.compareToIgnoreCase(name2);
	};

	private Map<List<Integer>, TableGroup> groups = new HashMap<>();

	private List<Integer> ordered = new ArrayList<>();

	public void openDefault(Player player) {
		if (player.getAttributes().getInt(LAST_OPENED_TABLE_KEY) == -1) {
			openForNpcId(player, DEFAULT_NPC);
			player.getPA().showInterface(39500);
		} else {
			player.getPA().showInterface(39500);
		}
	}

	@SuppressWarnings("unchecked")
	public void read() {
		JSONParser parser = new JSONParser();
		try {
			fileReader = new FileReader("./Data/json/npc_droptable.json");
			JSONArray data = (JSONArray) parser.parse(fileReader);
			for (Object aData : data) {
				JSONObject drop=(JSONObject) aData;
				List<Integer> npcIds=new ArrayList<>();
				if (drop.get("npc_id") instanceof JSONArray) {
					JSONArray idArray=(JSONArray) drop.get("npc_id");
					idArray.forEach(id -> npcIds.add(((Long) id).intValue()));
				} else {
					npcIds.add(((Long) drop.get("npc_id")).intValue());
				}
				TableGroup group=new TableGroup(npcIds);
				for (TablePolicy policy : TablePolicy.POLICIES) {
					if (!drop.containsKey(policy.name().toLowerCase())) {
						continue;
					}
					JSONObject dropTable=(JSONObject) drop.get(policy.name().toLowerCase());
					Table table=new Table(policy, ((Long) dropTable.get("accessibility")).intValue());
					JSONArray tableItems=(JSONArray) dropTable.get("items");
					for (Object tableItem : tableItems) {
						JSONObject item=(JSONObject) tableItem;
						int id=((Long) item.get("item")).intValue();
						int minimumAmount=((Long) item.get("minimum")).intValue();
						int maximumAmount=((Long) item.get("maximum")).intValue();
						table.add(new Drop(npcIds, id, minimumAmount, maximumAmount));
					}
					group.add(table);
				}
				groups.put(npcIds, group);
			}
			ordered.clear();

			for (TableGroup group : groups.values()) {
				if (group.getNpcIds().size() == 1) {
					ordered.add(group.getNpcIds().get(0));
					continue;
				}
				for (int id : group.getNpcIds()) {
					String name = NPCDefinitions.get(id).getNpcName();
					if (ordered.stream().noneMatch(i -> NPCDefinitions.get(i).getNpcName().equals(name))) {
						ordered.add(id);
					}
				}
			}

			ordered.sort(COMPARE_NAMES);
			Misc.println("Loaded " + ordered.size() + " drop tables.");
			amountOfTables = ordered.size();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	static int[] wildybosses = { 6611, 6609, 6615, 6609, 6615, 6610, 6619, 2054, 6618, 6607};
	public static int[] wildybossesforgiveaway = { 6611, 6609, 6615, 6609, 6615, 6610, 6619, 2054, 6618, 6607};
	static int[] revs = {7930, 7931, 7932, 7933, 7934, 7935, 7936, 7937, 7938, 7939, 7940};

	public void create(Player player, NPC npc, Location3D location, int repeats) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npc.npcType)).findFirst();
		
		group.ifPresent(g -> {
			double modifier = getModifier(player);
			List<GameItem> drops = g.access(player, modifier, repeats);

			for (GameItem item : drops) {
				if (item.getId() == 536) {
					if (player.getRechargeItems().hasItem(13111) && player.inWild()) {
						item.changeDrop(537, item.getAmount());
					}
				}			 
				if (player.getItems().isWearingItem(21816) && player.absorption == true && item.getId() == 21820 && (IntStream.of(revs).anyMatch(id -> id == npc.npcType))) {
						 item.changeDrop(995, 50); //basically just changes coins for ether 
						 int amount = Misc.random(12);	 // 1/12			  
						 player.braceletIncrease(amount);
						 player.sendMessage("@red@The bracelet has collected some ether for you.");
					 } 
				if (player.getItems().isWearingItem(21816) && player.absorption == false && item.getId() == 21820 && (IntStream.of(revs).anyMatch(id -> id == npc.npcType))) {
					int ether = 6 + Misc.random(6);
					item.changeDrop(21820, ether); //basically just changes coins for ether 
				} 
				if (item.getId() == 995 && player.collectCoins == true) {
					if ((player.getItems().isWearingItem(12785)) || (player.getItems().isWearingItem(2572))) {
							 player.getItems().addItem(995, item.getAmount());
							 item.changeDrop(-1, item.getAmount());
							 player.sendMessage("@red@The ring of wealth has collected some coins for you.");
						} else if (player.getItems().freeSlots() < 1) {
							 player.sendMessage("@blu@You had no inventory space left for the effect.");
						 } 
					}
					if (item.getId() == 6529 && player.collectCoins == true) {
					if ((player.getItems().isWearingItem(12785)) || (player.getItems().isWearingItem(2572))) {
							 player.getItems().addItem(6529, item.getAmount());
							 item.changeDrop(-1, item.getAmount());
							 player.sendMessage("@red@The ring of wealth has collected some tokkul for you.");
					} else if (player.getItems().freeSlots() < 1) {
							 player.sendMessage("@blu@You had no inventory space left for the effect.");
						 } 
					}
				if (player.getDiaryManager().getFremennikDiary().hasDoneAll() && item.getId() == 6729) {
					item.changeDrop(6730, item.getAmount());
				}
				if (player.wildLevel > 0 && player.getDiaryManager().getVarrockDiary().hasDoneElite() && player.getDiaryManager().getArdougneDiary().hasDoneElite() &&
					 player.getDiaryManager().getFaladorDiary().hasDoneElite() && player.getDiaryManager().getLumbridgeDraynorDiary().hasDoneElite() &&
					 player.getDiaryManager().getLumbridgeDraynorDiary().hasDoneElite() && player.getDiaryManager().getWildernessDiary().hasDoneElite() &&
					 player.getDiaryManager().getMorytaniaDiary().hasDoneElite() && player.getDiaryManager().getKandarinDiary().hasDoneElite() && 
					 player.getDiaryManager().getFremennikDiary().hasDoneElite() && player.getDiaryManager().getWesternDiary().hasDoneElite() && 
					 player.getDiaryManager().getDesertDiary().hasDoneElite() && item.getId() == 536) {
					item.changeDrop(537, item.getAmount());
				}
				if (item.getId() == 6529) {
					if (player.getRechargeItems().hasItem(11136)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.20));
					}
					if (player.getRechargeItems().hasItem(11138)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.50));
					}
					if (player.getRechargeItems().hasItem(11140)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.70));
					}
					if (player.getRechargeItems().hasItem(13103)) {
						item.changeDrop(6529, (int) (item.getAmount() * 1.90));
					}
				}
				if (item.getId() == 6729 && player.getRechargeItems().hasItem(13132)) {
					item.changeDrop(6730, item.getAmount());
				}
				// i keep these here just so i dont need to keep getting the item definiton.		
				//tail = 22988
				//fang =  22971
				//eye =  22973
				//heart = 22969
				if(player.playerEquipment[player.playerHands] == 22975) {
							if (item.getId() == 22988 || item.getId() == 22971 || item.getId() == 22973 && 
									player.getItems().getItemCount(22988, true) > 0 || player.getItems().getItemCount(22971, true) > 0
							|| player.getItems().getItemCount(22973, true) > 0 || player.getItems().getItemCount(22969, true) > 0) { //if they got all of the items, it just give them there usual item id
							item.changeDrop(item.getId(), item.getAmount());
					}
					if (item.getId() == 22988 && player.getItems().getItemCount(22988, true) > 0 &&
							player.getItems().getItemCount(22971, false) > 0 && player.getItems().getItemCount(22969, true) > 0 && player.getItems().getItemCount(22973, true) > 0) {	
							item.changeDrop(22971, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra tail for the Hydra eye..");
						} else if (item.getId() == 22988 && player.getItems().getItemCount(22988, true) > 0 &&
							player.getItems().getItemCount(22971, true) > 0 && player.getItems().getItemCount(22969, true) > 0 && player.getItems().getItemCount(22973, false) > 0) {	
							item.changeDrop(22973, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra tail for the Hydra fang."); //thats the hydra tail drop done
						} else if (item.getId() == 22988 && player.getItems().getItemCount(22988, true) > 0 &&
							player.getItems().getItemCount(22971, true) > 0 && player.getItems().getItemCount(22969, false) > 0 && player.getItems().getItemCount(22973, true) > 0) {	
							item.changeDrop(22969, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra tail for the Hydra heart."); //thats the hydra tail drop done
						}
					if (item.getId() == 22971 && player.getItems().getItemCount(22971, true) > 0 &&
							player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22969, true) > 0 && player.getItems().getItemCount(22973, false) > 0) {	
							item.changeDrop(22973, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra fang for the Hydra eye."); 
						 } else if (item.getId() == 22971 && player.getItems().getItemCount(22971, true) > 0 &&
							player.getItems().getItemCount(22988, false) > 0 && player.getItems().getItemCount(22969, true) > 0 && player.getItems().getItemCount(22973, true) > 0) {	
							item.changeDrop(22988, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra fang for the Hydra tail."); //thats the hydra fang drop done
			 			} else if (item.getId() == 22971 && player.getItems().getItemCount(22971, true) > 0 &&
			 				player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22969, false) > 0 && player.getItems().getItemCount(22973, true) > 0) {	
			 				item.changeDrop(22969, item.getAmount());
			 				player.sendMessage("@red@The Brimstone ring has swapped the Hydra fang for the Hydra heart."); //thats the hydra fang drop done
			 			}
					if (item.getId() == 22973 && player.getItems().getItemCount(22973, true) > 0 && player.getItems().getItemCount(22969, true) > 0 &&
						    player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22971, false) > 0) {	
							item.changeDrop(22971, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra eye for the Hydra fang."); 
						 } else if (item.getId() == 22973 && player.getItems().getItemCount(22973, true) > 0 && player.getItems().getItemCount(22969, true) > 0 &&
							player.getItems().getItemCount(22988, false) > 0 && player.getItems().getItemCount(22971, true) > 0) {	
							item.changeDrop(22988, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra eye for the Hydra tail."); //thats the hydra eye drop done
						 } else if (item.getId() == 22973 && player.getItems().getItemCount(22973, true) > 0 && player.getItems().getItemCount(22969, false) > 0 &&
							player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22971, true) > 0) {	
							item.changeDrop(22969, item.getAmount());
							player.sendMessage("@red@The Brimstone ring has swapped the Hydra eye for the Hydra heart."); //thats the hydra eye drop done
						}
					 if (item.getId() == 22969 && player.getItems().getItemCount(22973, true) > 0 && player.getItems().getItemCount(22969, true) > 0 &&
								    player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22971, false) > 0) {	
									item.changeDrop(22971, item.getAmount());
									player.sendMessage("@red@The Brimstone ring has swapped the Hydra heart for the Hydra fang."); 
						 } else if (item.getId() == 22969 && player.getItems().getItemCount(22971, true) > 0 && player.getItems().getItemCount(22969, true) > 0 &&
									player.getItems().getItemCount(22988, false) > 0 && player.getItems().getItemCount(22971, true) > 0) {	
									item.changeDrop(22988, item.getAmount());
									player.sendMessage("@red@The Brimstone ring has swapped the Hydra heart for the Hydra tail."); //thats the hydra eye drop done
						 } else if (item.getId() == 22969 && player.getItems().getItemCount(22971, true) > 0 && player.getItems().getItemCount(22969, true) > 0 &&
									player.getItems().getItemCount(22988, true) > 0 && player.getItems().getItemCount(22973, false) > 0) {	
									item.changeDrop(22973, item.getAmount());
									player.sendMessage("@red@The Brimstone ring has swapped the Hydra heart for the Hydra eye."); //thats the hydra eye drop done
								}
					}
				if (item.getId() == 13233 && !Boundary.isIn(player, Boundary.CERBERUS_BOSSROOMS)) {
					player.sendMessage("@red@Something hot drops from the body of your vanquished foe");
				}
				Server.itemHandler.createGroundItem(player, item.getId(), location.getX(), location.getY(),
						location.getZ(), item.getAmount(), player.getIndex());
			}

			/**
			 * Looting bag and rune pouch
			 */
			if (npc.inWild()) {
				switch (Misc.random(40)) {
				case 2:
					if (!player.getItems().playerHasItem(LootingBag.LOOTING_BAG_OPEN) && !player.getItems().playerHasItem(LootingBag.LOOTING_BAG)) {
						Server.itemHandler.createGroundItem(player, LootingBag.LOOTING_BAG, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					}
					break;
					
				case 8:
					if (player.getItems().getItemCount(12791, true) < 1) {
						Server.itemHandler.createGroundItem(player, 12791, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					}
					break;
				}
			}
			/**
			 * Slayer's staff enchantment and Emblems
			 */
			Optional<Task> task = player.getSlayer().getTask();
			Optional<SlayerMaster> myMaster = SlayerMaster.get(player.getSlayer().getMaster());
			task.ifPresent(t -> {
			String name = npc.getDefinition().getNpcName().toLowerCase().replaceAll("_", " ");
			
				if (name.equals(t.getPrimaryName()) || ArrayUtils.contains(t.getNames(), name)) {
					myMaster.ifPresent(m -> {
						if (npc.inWild() && m.getId() == 7663) {
							int slayerChance = 650;
							int emblemChance = 100;
							if (Misc.random(emblemChance) == 1) {
								Server.itemHandler.createGroundItem(player, 12746, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
								player.sendMessage("@red@You receive a mysterious emblem!");
							}
							if (Misc.random(slayerChance) == 1) {
								Server.itemHandler.createGroundItem(player, 21257, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
								//player.sendMessage("@red@A slayer's enchantment has dropped from your foe!");
								//PlayerHandler.executeGlobalMessage(
										//"@red@" + Misc.capitalize(player.playerName) + " received a drop: Slayer's Enchantment.</col>.");
								//PlayerHandler.executeGlobalMessage("<col=FF0000>[Lootations] @cr19@ </col><col=255>"+ Misc.capitalize(player.playerName) + "</col> received a <col=255>Slayer's Enchantment</col>.");
							}
						}
					});
				}
			});
			
			/**
			 * Clue scrolls
			 */
			int chance = player.getRechargeItems().hasItem(13118) ? 142 : player.getRechargeItems().hasItem(13119) ? 135 : player.getRechargeItems().hasItem(13120) ? 120 : 150;
			if (Misc.random(chance) == 1) {
				player.sendMessage("@blu@You notice a clue scroll on the ground.");
				if (npc.getDefinition().getNpcCombat() > 0 && npc.getDefinition().getNpcCombat() <= 40) {
					Server.itemHandler.createGroundItem(player, 2677, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				} 
				if (npc.getDefinition().getNpcCombat() > 40 && npc.getDefinition().getNpcCombat() <= 100) {
					Server.itemHandler.createGroundItem(player, 2801, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				} 
				if (npc.getDefinition().getNpcCombat() > 100) {
					Server.itemHandler.createGroundItem(player, 2722, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
			}
			
			/**
			* Coin Bags
			*/
			if (Misc.random(13) == 5) {
				player.sendMessage("@bla@You notice a @yel@Coin Bag@bla@ on the ground.");
				if (npc.getDefinition().getNpcCombat() > 0 && npc.getDefinition().getNpcCombat() <= 70) {
					Server.itemHandler.createGroundItem(player, 10832, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				} 
				if (npc.getDefinition().getNpcCombat() > 70 && npc.getDefinition().getNpcCombat() <= 110) {
					Server.itemHandler.createGroundItem(player, 10833, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
				if (npc.getDefinition().getNpcCombat() > 110 && npc.getDefinition().getNpcCombat() <= 200) {
					Server.itemHandler.createGroundItem(player, 10834, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
				if (npc.getDefinition().getNpcCombat() > 200) {
					Server.itemHandler.createGroundItem(player, 10835, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
			}
			/**
			 * Runecrafting pouches
			 */
			if (Misc.random(100) == 10) {
				if (npc.getDefinition().getNpcCombat() >= 70 && npc.getDefinition().getNpcCombat() <= 100 && player.getItems().getItemCount(5509, true) == 1 && player.getItems().getItemCount(5510, true) != 1) {
					Server.itemHandler.createGroundItem(player, 5510, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					//player.sendMessage("@pur@You sense an upgraded Runecrafting Pouch!");
				} else if (npc.getDefinition().getNpcCombat() > 100 && player.getItems().getItemCount(5510, true) == 1 && player.getItems().getItemCount(5512, true) != 1) {
					Server.itemHandler.createGroundItem(player, 5512, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
					//player.sendMessage("@pur@You sense an upgraded Runecrafting Pouch!");
				}
			}

			/**
			 * Crystal keys
			 */
			if (Misc.random(115) == 1) {
				player.sendMessage("@bla@You notice a @blu@crystal key@bla@ on the floor.");
				Server.itemHandler.createGroundItem(player, 989, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
			}
			/**
			 * Ecumenical Keys
			 */
			if (Boundary.isIn(npc, Boundary.WILDERNESS_GOD_WARS_BOUNDARY)) {
				if (Misc.random(60 + 10 * player.getItems().getItemCount(Godwars.KEY_ID, true)) == 1) {
					/**
					 * Key will not drop if player owns more than 3 keys already
					 */
					int key_amount = player.getDiaryManager().getWildernessDiary().hasCompleted("ELITE") ? 6 : 3;
					if (player.getItems().getItemCount(Godwars.KEY_ID, true) > key_amount) {
						return;
					}
					Server.itemHandler.createGroundItem(player, Godwars.KEY_ID, npc.getX(), npc.getY(), player.heightLevel, 1, player.getIndex());
					//player.sendMessage("@pur@An Ecumenical Key drops from your foe.");
				}
			}
			int random = Misc.random(1200);
			if (IntStream.of(wildybosses).anyMatch(id -> id == npc.npcType) && player.inWild())
			if (random == 1) {
				Server.itemHandler.createGroundItem(player, 12746, player.absX,
						player.absY, player.heightLevel, 1, player.getIndex());
				if (chance == 1 && player.getItems().playerHasItem(12746, 1)) {
					player.getItems().deleteItem(12746, 1);
					player.getItems().addItem(12748, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 1 EMBLEM]@blu@ to @red@[TIER 2 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12748, 1)) {
					player.getItems().deleteItem(12748, 1);
					player.getItems().addItem(12749, 1);
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 2 EMBLEM]@blu@ to @red@[TIER 3 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12749, 1)) {
					player.getItems().deleteItem(12749, 1);
					player.getItems().addItem(12750, 1);
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 3 EMBLEM]@blu@ to @red@[TIER 4 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12750, 1)) {
					player.getItems().deleteItem(12750, 1);
					player.getItems().addItem(12751, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 4 EMBLEM]@blu@ to @red@[TIER 5 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12751, 1)) {
					player.getItems().deleteItem(12751, 1);
					player.getItems().addItem(12752, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 5 EMBLEM]@blu@ to @red@[TIER 6 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12752, 1)) {
					player.getItems().deleteItem(12752, 1);
					player.getItems().addItem(12753, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 6 EMBLEM]@blu@ to @red@[TIER 7 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12753, 1)) {
					player.getItems().deleteItem(12753, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.getItems().addItem(12754, 1);
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 7 EMBLEM]@blu@ to @red@[TIER 8 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12754, 1)) {
					player.getItems().deleteItem(12754, 1);
					player.getItems().addItem(12755, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 8 EMBLEM]@blu@ to @red@[TIER 9 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12755, 1)) {
					player.getItems().deleteItem(12755, 1);
					player.getItems().addItem(12756, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You have upgraded your @red@ [TIER 9 EMBLEM]@blu@ to @red@[TIER 10 EMBLEM]@blu@.");
				} else if (chance == 1 && player.getItems().playerHasItem(12756, 1)) {
					player.getItems().deleteItem(12756, 1);
					player.getItems().addItem(12746, 1);
					player.sendMessage("@blu@You have received a mysterious emblem.");
					player.sendMessage("@blu@You are max tier so you was rewarded with just the Mysterious emblem added..");
				}
			}
			
			/**
			 * Dark Light
			 */
			if (Boundary.isIn(npc, Boundary.CATACOMBS)) {
				if (Misc.random(1000) == 1) {
					
					//PlayerHandler.executeGlobalMessage("<col=FF0000>[Lootations] @cr19@ </col><col=255>"+ Misc.capitalize(player.playerName) + "</col> received a <col=255>Darklight!</col>.");
					Server.itemHandler.createGroundItem(player, 6746, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
				}
			}
			
			/**
			 * Dark totem Pieces
			 */
			if (Boundary.isIn(npc, Boundary.CATACOMBS)) {
				switch (Misc.random(25)) {
				case 1:
					if (player.getItems().getItemCount(19679, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19679, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
					
				case 2:
					if (player.getItems().getItemCount(19681, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19681, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
				
				
				case 3:
					if (player.getItems().getItemCount(19683, false) < 1) {
						Server.itemHandler.createGroundItem(player, 19683, location.getX(), location.getY(), location.getZ(), 1, player.getIndex());
						player.sendMessage("@red@A surge of dark energy fills your body as you notice something on the ground.");
					}
					break;
				}
			}
		});
	}

	private double getModifier(Player player) {
		double modifier = 1.0;
		if(player.getMode().isOsrs()){
			modifier -=.100;
		}
		if (player.getItems().isWearingItem(2572)) {
			modifier -= .03;
		} else if (player.getItems().isWearingItem(12785)) {
			modifier -= .08;
		}
		if (player.isSkulled == true && Boundary.isIn(player, Boundary.REV_CAVE)) {
			modifier -= .100;
	 	}
		if (player.getRights().contains(Right.DIVINE)) {
			modifier -= 0.140;
		} else if (player.getRights().contains(Right.PLATINUM)) {
			modifier -= 0.120;
		} else if (player.getRights().contains(Right.ONYX_CLUB)) {
			modifier -= 0.100;
		} else if (player.getRights().contains(Right.DIAMOND_CLUB)) {
			modifier -= 0.080;
		} else if (player.getRights().contains(Right.LEGENDARY_DONATOR)) {
			modifier -= 0.070;
		} else if (player.getRights().contains(Right.ULTRA_DONATOR)) {//needs to be fixed
			modifier -= 0.050;
		} else if (player.getRights().contains(Right.EXTREME_DONOR)) {
			modifier -= 0.040;
		} else if (player.getRights().contains(Right.SUPER_DONOR)) {
			modifier -= 0.030;
		} else if (player.getRights().contains(Right.REGULAR_DONATOR)) {
			modifier -= 0.000;
		}
		return modifier;
	}
	public static double getModifier1(Player player) {
		int modifier = 0;
		if (player.getItems().isWearingItem(2572)) {
			modifier += 3;
		} else if (player.getItems().isWearingItem(12785)) {
			modifier += 8;
		}
		if (player.isSkulled == true && Boundary.isIn(player, Boundary.REV_CAVE)) {
			modifier += 2;
	 	}
		if (player.getMode().isOsrs()) {
			modifier += 10;
		} else if (player.getMode().isIronman() || player.getMode().isRegular() || player.getMode().isHCIronman()) {
			modifier += 0;
		}
		if (player.getRights().contains(Right.DIVINE)) {
			modifier += 14;
		} else if (player.getRights().contains(Right.PLATINUM)) {
			modifier += 12;
		} else if (player.getRights().contains(Right.ONYX_CLUB)) {
			modifier += 10;
		} else if (player.getRights().contains(Right.DIAMOND_CLUB)) {
			modifier += 8;
		} else if (player.getRights().contains(Right.LEGENDARY_DONATOR)) {
			modifier += 7;
		} else if (player.getRights().contains(Right.ULTRA_DONATOR)) {
			modifier += 5;
		} else if (player.getRights().contains(Right.EXTREME_DONOR)) {
			modifier += 4;
		} else if (player.getRights().contains(Right.SUPER_DONOR)) {
			modifier += 3;
		} else if (player.getRights().contains(Right.REGULAR_DONATOR)) {
			modifier += 0;
		}
		return modifier;
	}

	public void clearSearch(Player player) {
		for(int i = 0; i < 150; i++) {
			player.getPA().sendFrame126("", 33008 + i);
		}
		player.searchList.clear();
	}

	private void clearDrops(Player player) {
		player.getPA().sendFrame126("", 43110);
		player.getPA().sendFrame126("", 43111);
		player.getPA().sendFrame126("", 43112);
		player.getPA().sendFrame126("", 43113);

		for(int i = 0;i<80;i++){
			player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
			player.getPA().sendString("", 34200+i);
			player.getPA().sendString("", 34300+i);
			player.getPA().sendString("", 34100+i);
			player.getPA().sendString("", 34400+i);
		}
	}

	/**
	 * Clears the interface of all parts.
	 * 
	 * Used on searching and initial load.
	 * @param player
	 */
	public void clear(Player player) {
		clearSearch(player);
		clearDrops(player);
	}

	public void open2(Player player) {
		clear(player);

		for (int index = 0; index < ordered.size(); index++) {
			player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(ordered.get(index)).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.getPA().showInterface(33000);
	}

	public List<GameItem> getNPCdrops(int id) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(id)).findFirst();
		try {
			return group.map(g -> {
				List<GameItem> items = new ArrayList<>();
				for (TablePolicy policy : TablePolicy.POLICIES) {
					if (policy == TablePolicy.RARE || policy == TablePolicy.VERY_RARE) {
						Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
						if (table.isPresent()) {
							for (Drop d : table.get()) {
								items.add(new GameItem(d.getItemId(), d.getMaximumAmount()));
							}
						}
					}
				}
				return items;
			}).orElse(new ArrayList<>());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getDrops(Player player, int id) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(id)).findFirst();
		group.ifPresent(g -> {
			List<GameItem> items = new ArrayList<>();
			for (TablePolicy policy : TablePolicy.POLICIES) {
				if (policy == TablePolicy.RARE || policy == TablePolicy.VERY_RARE) {
					Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
					if (table.isPresent()) {
						for(Drop d : table.get()) {
							items.add(new GameItem(d.getItemId(), d.getMaximumAmount()));
						}
					}
				}
			}
			player.dropItems = items;
		});
	}

	/**
	 * Searchers after the player inputs a npc name
	 * @param player
	 * @param name
	 */
	public void search(Player player, String name) {
		if(name.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
			player.sendMessage("You may not search for alphabetical and numerical combinations.");
			return;
		}
		if (System.currentTimeMillis() - player.lastDropTableSearch < TimeUnit.SECONDS.toMillis(1)
				&& !player.getRights().contains(Right.GAME_DEVELOPER)) {
			player.sendMessage("You can only do this once every few seconds.");
			return;
		}
		player.lastDropTableSearch = System.currentTimeMillis();
		
		clearSearch(player);

		List<Integer> definitions = ordered.stream().filter(Objects::nonNull).filter(def -> NPCDefinitions.get(def).getNpcName() != null).filter(def -> NPCDefinitions.get(def).getNpcName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());

		if(definitions.isEmpty()) {
			definitions = ordered.stream().filter(Objects::nonNull).collect(Collectors.toList());
			List<Integer> npcs = new ArrayList<>();
			int count = 0;

			player.getPA().setScrollableMaxHeight(NPC_RESULTS_CONTAINER_INTERFACE_ID, 250 + (definitions.size() > 16 ? (definitions.size() - 16) * 14 : 0));

			for(Integer index : definitions) {
				Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(NPCDefinitions.get(index).getNpcId())).findFirst();
				if(group.isPresent()) {
					TableGroup g = group.get();
					
					for(TablePolicy policy : TablePolicy.values()) {
						Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
						if(table.isPresent()) {
							for(Drop drop : table.get()) {
								if(drop == null) {
									continue;
								}
								
								if(ItemAssistant.getItemName(drop.getItemId()).toLowerCase().contains(name.toLowerCase())) {
									npcs.add(index);
									player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(NPCDefinitions.get(index).getNpcId()).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + count);
									count++;
								}
							}
						}
					}
				}

			}
			
			player.searchList = npcs;
			return;
			
		}

		player.getPA().setScrollableMaxHeight(NPC_RESULTS_CONTAINER_INTERFACE_ID, 251 + (definitions.size() > 17 ? (definitions.size() - 17) * 13 : 0));

		for(int index = 0; index < definitions.size(); index++) {
			if(index >= 150) {
				break;
			}
			player.getPA().sendFrame126(StringUtils.capitalize(NPCDefinitions.get(definitions.get(index)).getNpcName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.searchList = definitions;
	}

	/**
	 * Loads the selected npc choosen by the player to view their drops
	 * @param player
	 * @param button
	 */
	public void select(Player player, int button) {
		int listIndex;
		
		//So the idiot client dev didn't organize the buttons in a singulatiry order. So i had to shift around the id's
		//so if you have 50 npcs in the search you can click them all fine
		if(button <= 128255) {
			listIndex = button - 128240;
		} else {
			listIndex = (128255 - 128240) + 1 + button - 129000;
		}
		
		if (listIndex < 0) {
			return;
		}

		//Finding NPC ID
		if (player.searchList.isEmpty() && ordered.size() > listIndex) {
			openForNpcId(player, ordered.get(listIndex));
		} else if (player.searchList.size() > listIndex) {
			openForNpcId(player, player.searchList.get(listIndex));
		}
	}

	public void openForNpcId(Player player, int npcId) {
		player.getAttributes().setInt(LAST_OPENED_TABLE_KEY, npcId);
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		//If the group in the search area contains this NPC
		group.ifPresent(g -> {
			if (System.currentTimeMillis() - player.lastDropTableSelected < TimeUnit.SECONDS.toMillis(5)
					&& !player.getRights().contains(Right.GAME_DEVELOPER)) {
				player.sendMessage("You can only do this once every 5 seconds.");
				return;
			}

			//Loads the definition and maxhit/aggressiveness to display
			NPCDefinitions npcDef = NPCDefinitions.get(npcId);

			if (player.getRights().contains(Right.GAME_DEVELOPER)) {
				player.getPA().sendString(npcDef.getNpcId() + ", " + npcDef.getNpcName(), 43005);
			} else {
				player.getPA().sendString("Monster Drop Viewer", 43005);
			}

			player.getPA().sendFrame126("Health: @whi@" + npcDef.getNpcHealth(), 43110);
			player.getPA().sendFrame126("Combat Level: @whi@" + npcDef.getNpcCombat(), 43111);
			if(NPCHandler.getNpc(npcId) != null){
				player.getPA().sendFrame126("Max Hit: @whi@" + NPCHandler.getNpc(npcId).maxHit, 43112);
			} else {
				player.getPA().sendFrame126("Max Hit: @whi@?", 43112);
			}
			player.getPA().sendFrame126("Aggressive: @whi@" + (Server.npcHandler.isAggressive(npcId, true) ? "true" : "false"), 43113);

			player.lastDropTableSelected = System.currentTimeMillis();

			double modifier = getModifier(player);

			player.getPA().resetScrollBar(DROP_TABLE__CONTAINER_INTERFACE_ID);

			//Iterates through all 5 drop table's (Found in TablePolicy -> Enum)
			for (TablePolicy policy : TablePolicy.POLICIES) {
				Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
				if (table.isPresent()) {
					int chance = (int) Math.ceil((table.get().getAccessibility() * modifier)) * table.get().size();

					//Updates the interface with all new information
					updateAmounts(player, policy, table.get(), chance);
				} else {
					updateAmounts(player, policy, new ArrayList<>(), -10);
				}
			}

			player.getPA().setScrollableMaxHeight(DROP_TABLE__CONTAINER_INTERFACE_ID, 225 + (player.dropSize > 7 ? (player.dropSize - 7) * 32 : 26));
			player.getPA().sendConfig(DROP_AMOUNT_CONFIG_ID, player.dropSize);

			//If the game has displayed all drops and there are empty slots that haven't been filled, clear them
			if(player.dropSize < 80) {
				for(int i = player.dropSize; i<80; i++){
					player.getPA().sendString("", 34200+i);
					player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
					player.getPA().sendString("", 34300+i);
					player.getPA().sendString("", 34100+i);
					player.getPA().sendString("", 34400+i);
				}
			}
			player.dropSize = 0;
		});
	}

	/**
	 * Updates the interface for the selected NPC
	 * @param player
	 * @param policy
	 * @param drops
	 * @param kills
	 */
	private void updateAmounts(Player player, TablePolicy policy, List<Drop> drops, int kills) {
		drops = drops.stream().distinct().collect(Collectors.toList());

		//Iterates through all drops in that catagory
		for (int index = 0; index < drops.size(); index++) {
			Drop drop = drops.get(index);
			int minimum = drop.getMinimumAmount();
			int maximum = drop.getMaximumAmount();
			int frame = (34200 + player.dropSize + index);//collumnOffset + (index * 2);
			
			//if max = min, just send the max
			if (minimum == maximum) {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMaximumAmount()), frame);
			} else {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMinimumAmount()) + " - " + Misc.getValueWithoutRepresentation(drop.getMaximumAmount()), frame);
			}
			ItemList itemList = Server.itemHandler.getItemList(drop.getItemId());

			String itemName = null;
			if (itemList != null && itemList.itemName != null) {
				if (itemList.itemName.length() > 17) {
					itemName = itemList.itemName.substring(0, 17) + "..";
				} else {
					itemName = itemList.itemName;
				}
			}

			player.getPA().itemOnInterface(drop.getItemId(), 1, 34010+player.dropSize + index, 0);
			player.getPA().sendString(Misc.optimizeText(policy.name().toLowerCase().replaceAll("_", " ")), 34300+player.dropSize + index);
			player.getPA().sendString(itemName == null ? "Unknown" : itemName, 34100 + player.dropSize + index);

			if(kills == -10){
				player.getPA().sendString(1 + "/?", 34400 + player.dropSize + index);
			} else {
				player.getPA().sendString(1 + "/"+kills, 34400 + player.dropSize + index);
			}
		}
		
		player.dropSize += drops.size();
	}

	static int amountt = 0;

	private FileReader fileReader;

	/**
	 * Testing droptables of chosen npcId
	 * @param player		The player who is testing the droptable
	 * @param npcId			The npc who of which the player is testing the droptable from
	 * @param amount		The amount of times the player want to grab a drop from the npc droptable
	 */
	
	public void test(Player player, int npcId, int amount) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		amountt = amount;

		while (amount-- > 0) {
			group.ifPresent(g -> {
				List<GameItem> drops = g.access(player, 1.0, 1);

				for (GameItem item : drops) {
					player.getItems().addItemToBank(item.getId(), item.getAmount());
				}
			});
		}
		player.sendMessage("Completed " + amountt + " drops from " + Server.npcHandler.getNpcName(npcId) + ".");
	}


}
