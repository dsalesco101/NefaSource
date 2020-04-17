package ethos.model.content.collection_log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import ethos.Server;
import ethos.model.content.loot.impl.RaidsChestRare;
import ethos.model.content.trails.CasketRewards;
import ethos.model.content.trails.RewardLevel;
import ethos.model.items.GameItem;
import ethos.model.npcs.NPCDefinitions;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/7/19
 *
 */
public class CollectionLog {
	
	/**
	 * Different tabs within interface
	 *
	 */
	public enum CollectionTabType {
		BOSSES, WILDERNESS, RAIDS, MINIGAMES, CLUE_SCROLL;
	}
	
	/* Variables */
	private static HashMap<CollectionTabType, ArrayList<Integer>> collectionNPCS;
	private static final int INTERFACE_ID = 23110;
	
	private final Player player;
	private HashMap<String, ArrayList<GameItem>> collections;
	
	/**
	 * Constructor
	 * @param player
	 */
	public CollectionLog(Player player) {
		this.player = player;
		this.collections = new HashMap<>();
	}

	public Player getPlayer() {
		return player;
	}
	
	public HashMap<String, ArrayList<GameItem>> getCollections() {
		return collections;
	}
	
	/**
	 * Initializes the default npcs to be collecting for
	 */
	public static void init() {
		try {
			Path path = Paths.get("./data/json/collections/collection_npcs.json");
			File file = path.toFile();

			JsonParser parser = new JsonParser();
			if (!file.exists()) {
				return;
			}
			Object obj = parser.parse(new FileReader(file));
			JsonObject jsonUpdates = (JsonObject) obj;

			Type listType = new TypeToken<HashMap<CollectionTabType, ArrayList<Integer>>>() {
			}.getType();

			collectionNPCS = new Gson().fromJson(jsonUpdates, listType);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No default NPCs found!");
			collectionNPCS = new HashMap<>();
		}
	}
	
	/**
	 * Opens the interface for a player
	 */
	public void openInterface() {
		if (player == null) {
			return;
		}
		
		resetInterface();
		selectTab(CollectionTabType.BOSSES);
		//selectCell(0, CollectionTabType.BOSSES);
	}
	
	/**
	 * Clears the interface
	 */
	public void resetInterface() {
		for(int i = 0; i < 50; i++) {
			player.getPA().sendFrame126("", 23123 + (i * 2));
			player.getPA().sendConfig(520 + i, 0);
		}
		player.getPA().sendConfig(519, 0);
		for(int i = 0; i < 3; i++) {
			player.getPA().sendConfig(571 + i, 0);
		}
	}
	
	/**
	 * Selects a tab within the interface
	 * @param type
	 */
	public void selectTab(CollectionTabType type) {
		if (collectionNPCS == null || collectionNPCS.isEmpty()) {
			return;
		}
		
		ArrayList<Integer> npcs = collectionNPCS.get(type);
		if (npcs != null) {
			resetInterface();
			player.collectionLogTab = type;
			player.previousSelectedCell = 0;
			player.getPA().sendConfig(player.previousSelectedTab == 0 ? 519 : 570 + player.previousSelectedTab, 0);
			player.previousSelectedTab = type.ordinal();
			player.getPA().sendConfig(type.ordinal() == 0 ? 519 : 570 + type.ordinal(), 1);
			for(int i = 0; i < npcs.size(); i++) {
				boolean found = false;
				if (getCollections().containsKey(npcs.get(i) + "")) {
					ArrayList<GameItem> itemsObtained = getCollections().get(npcs.get(i) + "");
					if (itemsObtained != null) {
						List<GameItem> drops = Server.getDropManager().getNPCdrops(npcs.get(i));
						if (drops != null && drops.size() == itemsObtained.size()) {
							found = true;
							player.getPA().sendFrame126("@gre@" + (type == CollectionTabType.CLUE_SCROLL ? Misc.optimizeText(RewardLevel.VALUES.get(npcs.get(i)).name().toLowerCase()) : Misc.optimizeText(NPCDefinitions.get(npcs.get(i)).getNpcName())), 23123 + (i * 2));
						}
					}
				}
				if (!found) {
					player.getPA().sendFrame126(type == CollectionTabType.CLUE_SCROLL ? Misc.optimizeText(RewardLevel.VALUES.get(npcs.get(i)).name().toLowerCase()) : Misc.optimizeText(NPCDefinitions.get(npcs.get(i)).getNpcName()), 23123 + (i * 2));
				}
			}
			selectCell(0, type);
		} else {
			player.sendMessage("There are no collection logs for this type yet.");
		}
	}
	
	/**
	 * Selects a cell from a tab type
	 * @param index
	 * @param type
	 */
	public void selectCell(int index, CollectionTabType type) {
		if (collectionNPCS == null || collectionNPCS.isEmpty()) {
			return;
		}
		
		ArrayList<Integer> npcs = collectionNPCS.get(type);
		if (npcs != null) {
			if (index >= npcs.size()) {
				return;
			}
			
			player.getPA().sendConfig(520 + player.previousSelectedCell, 0);
			player.previousSelectedCell = index;
			player.getPA().sendConfig(520 + index , 1);
			
			populateInterface(npcs.get(index));
		}
	}
	
	/**
	 * Populates the interface with data
	 * @param npcId
	 */
	public void populateInterface(int npcId) {
		if (!getCollections().containsKey("" + npcId)) { //If they've never looked at that NPC before, initialize a blank arraylist
			getCollections().put("" + npcId, new ArrayList<>());
			saveToJSON();
		}
		
		String npcName = NPCDefinitions.get(npcId).getNpcName();
		if (npcId >= 1 && npcId <= 4) {
			npcName = Misc.optimizeText(RewardLevel.VALUES.get(npcId).name().toLowerCase());
		}
		player.getPA().sendFrame126(Misc.optimizeText(npcName), 23118);
		if (player.getNpcDeathTracker().getTracker().containsKey(npcName)) {
			player.getPA().sendFrame126(Misc.optimizeText(npcName) + ": @whi@" + player.getNpcDeathTracker().getTracker().get(npcName), 23120);
		} else {
			player.getPA().sendFrame126(Misc.optimizeText(npcName) + ": @whi@" + 0, 23120);
		}
		
		//Clear items
		for(int i = 0; i < 198; i++) {
			player.getPA().sendItemToSlotWithOpacity(23231, -1, i, 0, false);
		}
		
		ArrayList<GameItem> items = getCollections().get(npcId + "");
		Server.getDropManager().getDrops(player, npcId);
		if (npcId == 8028) {
			player.dropItems = Vorkath.getVeryRareDrops();
		}
		if (npcId == 7554) {
			player.dropItems = RaidsChestRare.getRareDrops();
		}
		if (npcId >= 1 && npcId <= 4) {
			player.dropItems = CasketRewards.getRewardsForType(npcId);
		}
		int foundCount = 0;
		for(int i = 0; i < player.dropItems.size(); i++) {
			boolean found = false;
			for(int j = 0; j < items.size(); j++) {
				if (items.get(j).getId() == player.dropItems.get(i).getId()) {
					player.getPA().sendItemToSlotWithOpacity(23231, items.get(j).getId(), i, items.get(j).getAmount(), false);
					foundCount++;
					found = true;
					break;
				}
			}
			if (!found) {
				player.getPA().sendItemToSlotWithOpacity(23231, player.dropItems.get(i).getId(), i, 0, true);
			}
		}
		player.getPA().sendFrame126("Obtained: " + (foundCount == player.dropItems.size() ? "@gre@" : "@red@") + foundCount + "/" + player.dropItems.size(), 23119);
		player.getPA().showInterface(INTERFACE_ID);
	}
	
	/**
	 * Saves users collection to a JSON file
	 */
	public void saveToJSON() {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = prettyGson.toJson(getCollections());
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File("./data/json/collections/" + player.playerName + ".json")));
            bw.write(prettyJson);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Loads a users collection data
	 */
	public void loadCollections() {
		try {
			Path path = Paths.get("./data/json/collections/" + player.playerName + ".json");
			File file = path.toFile();

			JsonParser parser = new JsonParser();
			if (!file.exists()) {
				return;
			}
			Object obj = parser.parse(new FileReader(file));
			JsonObject jsonUpdates = (JsonObject) obj;

			Type listType = new TypeToken<HashMap<String, ArrayList<GameItem>>>() {
			}.getType();

			collections = new Gson().fromJson(jsonUpdates, listType);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No collections found!");
			collections = new HashMap<>();
		}
	}
	
	/**
	 * Handles and NPC dropping an item
	 * @param npcId
	 * @param dropId
	 * @param dropAmount
	 */
	public void handleDrop(int npcId, int dropId, int dropAmount) {
		if (npcId == 2043 || npcId == 2044) { //All zulrahs
			npcId = 2042;
		}
		
		if (npcId == 7144  || npcId == 7146) {
			npcId = 7145;
		}
		
		if (npcId == 8615 || npcId == 8619 || npcId == 8620 || npcId == 8622) {
			npcId = 8621;
		}
		
		if (!isCollectionNPC(npcId)) {
			return;
		}
		
		ArrayList<GameItem> currentItems = getCollections().get("" + npcId);
		if (currentItems == null) {
			currentItems = new ArrayList<>();
			currentItems.add(new GameItem(dropId, dropAmount));
			player.sendMessage("You have unlocked another item in your collection log!");
		} else {
			boolean found = false;
			for(int i = 0; i < currentItems.size(); i++) {
				if (currentItems.get(i).getId() == dropId) {
					currentItems.get(i).setAmount(currentItems.get(i).getAmount() + dropAmount);
					found = true;
					break;
				}
			}
			
			if (!found) {
				currentItems.add(new GameItem(dropId, dropAmount));
				player.sendMessage("You have unlocked another item in your collection log!");
			}
		}
		getCollections().put("" + npcId, currentItems);
		//As soon as it gets a drop it saves Kraken has been getting the most complaints
		saveToJSON();
	}
	
	/**
	 * Checks if an NPC is in fact a collection NPC
	 * @param npcId
	 * @return
	 */
	public boolean isCollectionNPC(int npcId) {
		for (Map.Entry<CollectionTabType, ArrayList<Integer>> entry : collectionNPCS.entrySet()) {
		    for(int i = 0; i < entry.getValue().size(); i++) {
		    	if (entry.getValue().get(i) == npcId) {
		    		return true;
		    	}
		    }
		}
		return false;
	}
	
	/**
	 * Handles all buttons on the interface
	 * @param buttonId
	 * @return
	 */
	public boolean handleActionButtons(int buttonId) {
		if (buttonId >= 90082 && buttonId <= 90180) {
			int index = (buttonId - 90082) / 2;
			selectCell(index, player.collectionLogTab);
			return true;
		}
		switch(buttonId) {
		case 90076:
			selectTab(CollectionTabType.BOSSES);
			return true;
		case 90182:
			selectTab(CollectionTabType.WILDERNESS);
			return true;
		case 90184:
			selectTab(CollectionTabType.RAIDS);
			return true;
		case 90186:
			selectTab(CollectionTabType.MINIGAMES);
			return true;
		case 90188:
			selectTab(CollectionTabType.CLUE_SCROLL);
			return true;
		case 90073:
			player.getPA().closeAllWindows();
			return true;
		}
		return false;
	}
}
