package ethos.model.content.tradingpost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ethos.Config;
import ethos.Server;
import ethos.model.content.CheatEngine.CheatEngineBlock;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.items.ItemList;
import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.util.Misc;

/**
*
* @author Nighel
* @credits Nicholas
*
*/

public class Listing {

	private static final String DIRECTORY = "./data/tradingpost/";
	private static final String SALE_DIRECTORY = DIRECTORY + "sales/";
	private static final String PLAYERS_DIRECTORY = DIRECTORY + "players/";
	private static final String ITEMS_DIRECTORY = DIRECTORY + "items/";

	// make true to preload all sales and keep them all in the cache
	// make true to preload all sales and keep them all in the cache
	private static final boolean PRELOAD_ALL = false;

	// the next ID to be assigned to a sale, increment every time someone makes a new sale
	private static int NEXT_SALE_ID = 1;

	// Number of sales to keep in memory, irrelevant if PRELOAD_SALES is true
	private static int CACHE_SIZE = 100;

	// recently read sales kept in memory for faster access
	private static LinkedList<Sale> cache = new LinkedList<Sale>();

	/**
	 * Loads the total sales on load of server
	 */
	public static void init() {
		Misc.createDirectory(SALE_DIRECTORY, PLAYERS_DIRECTORY, ITEMS_DIRECTORY);
		getFile(SALE_DIRECTORY);
		//System.out.println("NEXT_SALE_ID: " + NEXT_SALE_ID);
	}
	
	/**
	 * Counts how much sales there are
	 * @param dirPath
	 */
	
	private static void getFile(String dirPath) {
	    File f = new File(dirPath);
	    File[] files = f.listFiles();

	    if (files != null)
	    for (int i = 0; i < files.length; i++) {
	    	NEXT_SALE_ID = files.length+1;
	        File file = files[i];

	        if (file.isDirectory()) {   
	             getFile(file.getAbsolutePath()); 
	        }
	    }
	}
	
	/**
	 * Loads the sales via player name
	 * @param playerName - player his username
	 * @return
	 */
	
	public static List<Sale> getSales(String playerName) {
		String line = "";
		LinkedList<Sale> sales = new LinkedList<Sale>();
		// read text file at /players/playerName.txt
		try {
			BufferedReader br = new BufferedReader(new FileReader(PLAYERS_DIRECTORY+playerName+".txt"));

			while((line = br.readLine()) != null) {
				int id = Integer.parseInt(line);
				if(sales != null)
					sales.add(getSale(id));
			}
			
			br.close();

			return sales;
		} catch(Exception e) {
			e.printStackTrace();
			return new LinkedList<Sale>();
		}
	}
	
	/**
	 * Loads the sales via item id
	 * @param itemId
	 * @return
	 */
	
	public static List<Sale> getSales(int itemId) {
		String line = "";
		LinkedList<Sale> sales = new LinkedList<Sale>();
		// read text file at /players/playerName.txt
		try {
			BufferedReader br = new BufferedReader(new FileReader(ITEMS_DIRECTORY+itemId+".txt"));

			while((line = br.readLine()) != null) {
				int id = Integer.parseInt(line);
				if(sales != null)
					sales.add(getSale(id));
			}
			
			br.close();

			return sales;
		} catch(Exception e) {
			e.printStackTrace();
			return new LinkedList<Sale>();
		}
	}

	/**
	 * Gets the sale via the id
	 * @param saleId - id of the sale
	 * @return
	 */
	
	public static Sale getSale(int saleId) {
		String[] split;
		// Check cache for this sale
		for(Sale sale : cache)
			if(sale.getSaleId() == saleId)
				return sale;

		// read text file at /sale/saleId.txt
		try {
			BufferedReader br = new BufferedReader(new FileReader(SALE_DIRECTORY+saleId+".txt"));

			// read information
			split = br.readLine().split("\t");
			Sale sale = new Sale(saleId, split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Boolean.parseBoolean(split[6]));
				
			// If the cache is full, remove the last Sale. Add this one to the beginning either way.
				if(!PRELOAD_ALL && cache.size() == CACHE_SIZE)
			cache.removeLast();
			cache.addFirst(sale);

			br.close();
				
			return sale;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Opens up the first interface for the trading post.
	 * And then loading all the data thats needed.
	 * @param c
	 */

	public static void openPost(Player c, boolean soldItem, boolean openFirst) {
		if (!c.getMode().isTradingPermitted()) {
			c.sendMessage("You are not permitted to make use of this.");
			return;
		}
		c.setInTradingPost(true);
		resetEverything(c);
		c.getPA().showInterface(48600);
		c.getPA().sendFrame106(3);
		if(soldItem) {
			String each = c.quantity > 1 ? "each" : "";
			c.sendMessage("[@red@Trading Post@bla@] You successfully list "+c.quantity+"x "+ItemAssistant.getItemName(c.item)+" for "+Misc.format(c.price)+" GP " + each);
			c.item = -1;
			c.uneditItem = -1;
			c.quantity = -1;
			c.price = -1;
		}
		loadPlayersListings(c);
		c.insidePost = true;
		loadHistory(c);
	}
	
	/**
	 * Makes all the listings show up for the player.
	 * @param c
	 */
	
	public static void loadPlayersListings(Player c) {
		int start = 48788, id = 0, moneyCollectable = 0;

		LinkedList<Sale> sales = (LinkedList<Sale>) getSales(c.playerName);
		
		for(Sale sale : sales) {
			c.getPA().sendTradingPost(48847, sale.getId(), id, sale.getQuantity());
			id++;
			c.getPA().sendFrame126(ItemAssistant.getItemName(sale.getId()), start);
			start++;
			c.getPA().sendFrame126("" + zerosintomills(sale.getPrice()), start);
			start++;
			c.getPA().sendFrame126(sale.getTotalSold() + " / " + sale.getQuantity() , start);
			start += 2;
			moneyCollectable += (sale.getPrice() * sale.getLastCollectedAmount());
		}
		c.getPA().sendFrame126(Misc.format(moneyCollectable) + " GP", 48610);
		for (int k = id; k < 15; k++) {
			c.getPA().sendTradingPost(48847, -1, k, -1);
		}
		for(int i = start; i < 48850; i++) {
			c.getPA().sendFrame126("", i);
		}
	}
	
	/**
	 * Shows the last 10 latest sales you have done.
	 * @param c
	 */
	
	public static void loadHistory(Player c) {
		for(int i = 0, start1 = 48636, start2 = 48637; i < c.saleItems.size(); i++) {
			//System.out.println("salesItems - " + c.saleItems.get(i).intValue());
			//System.out.println("saleAmount - " + c.saleAmount.get(i).intValue());
			//System.out.println("salePrice - " + c.salePrice.get(i).intValue());
			if(c.saleItems.get(i).intValue() > 0 && c.saleAmount.get(i).intValue() > 0 && c.salePrice.get(i).intValue() > 0) {
				String each = c.saleAmount.get(i).intValue() > 1 ? "each" : "coins";
				c.getPA().sendFrame126(c.saleAmount.get(i).intValue() + " x " + ItemAssistant.getItemName(c.saleItems.get(i).intValue()), start1);
				c.getPA().sendFrame126("sold for "+zerosintomills(c.salePrice.get(i).intValue())+" " + each, start2);
				start1 += 2;
				start2 += 2;
			}
		}
	}
	
	/**
	 * Opens up the selected item using offer 1/5/10/all/x
	 * @param c
	 * @param itemId
	 * @param amount
	 * @param p
	 */
	
	@SuppressWarnings("unused")
	public static void openSelectedItem(Player c, int itemId, int amount, int p) {
		//System.out.println("");
		if (!c.getItems().playerHasItem(itemId, amount)) {
			c.sendMessage("[@red@Trading Post@bla@] You don't have that many "+ItemAssistant.getItemName(itemId) + (amount > 1 ? "s" : "")+".");
			return;
		}
		if (ItemDefinition.forId(itemId) != null) {
			if (!ItemDefinition.forId(itemId).isTradable()) {
				c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
				return;
			}
		}
		for(int item : Config.NOT_SHAREABLE) {
			if(item == itemId) {
				c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
				return;
			}
		}
		if(itemId == 995) {
			c.sendMessage("[@red@Trading Post@bla@] You can't sell that item");
			return;
		}

		if (ItemDefinition.forId(itemId) == null) {
			c.sendMessage("Item definition is null for " + itemId + ", please report this error.");
			return;
		}

		//if(c.uneditItem <= 0) - Caused a dupe if you changed items
			c.uneditItem = itemId;
		//Config.trade
		c.item = -1;
		
		c.inSelecting = false;
		c.isListing = true;
		//boolean noted = Item.itemIsNote[itemId];
		//boolean noted = ItemDefinition.forId(itemId).isNoteable();
		//if(noted)
		//	itemId--;
		c.item = itemId;
		c.quantity = amount;
		ItemList itemList = Server.itemHandler.ItemList[c.item];
		//c.price = p >= 1 ? p : (int) itemList.ShopValue; 
		//c.getInventory().getItemshopValue(c.item);
		if (p > 0) {
		    c.price = p;
		} else if (ItemDefinition.forId(itemId).getValue() < Integer.MAX_VALUE) {
		    c.price = (int) ItemDefinition.forId(itemId).getValue();
		} else {
		    c.price = Integer.MAX_VALUE;
		}
		//c.price = p >= 1 ? p : (int) ItemDefinition.forId(itemId).getValue();
		c.getPA().showInterface(48598);
		c.getPA().sendTradingPost(48962, itemId, 0, amount);
		c.getPA().sendFrame126(ItemAssistant.getItemName(itemId), 48963); //item name
		c.getPA().sendFrame126("Price (each): "+Misc.format(c.price)+"", 48964); //price each
		c.getPA().sendFrame126("Quantity: " + amount, 48965); //quantity
		//c.getPA().sendFrame(s, 48966); //guide
		//c.getPA().sendFrame(s, 48967); //listings
	}
	
	/**
	 * Writes every thing the the proper files.
	 * @param c
	 */
	
	public static void confirmListing(Player c) {
		int itemId = c.item;
		int amount = c.quantity;
		if (c.quantity <= 0) {
			c.sendMessage("[@red@Trading Post@bla@] You cant have 0 quantity listed.");
			return;
		}
		if (!c.getItems().playerHasItem(itemId, amount)) {
			c.sendMessage("[@red@Trading Post@bla@] You no longer have that many"+ItemAssistant.getItemName(itemId) + (amount > 1 ? "s" : "")+".");
			return;
		}
		if (c.uneditItem == -1) {
			if (c.debugMessage)
				c.sendMessage("Stopped");
			return;
		}
		/*int Max_Value = 2147000000;
		if (c.price > Max_Value)
			return;*/
		
		BufferedWriter sale_id;
		BufferedWriter item_id;
		BufferedWriter name;
		try {
			sale_id = new BufferedWriter(new FileWriter(SALE_DIRECTORY+NEXT_SALE_ID+".txt", true));
			item_id = new BufferedWriter(new FileWriter(ITEMS_DIRECTORY+c.item+".txt", true));
			name = new BufferedWriter(new FileWriter(PLAYERS_DIRECTORY+c.playerName+".txt", true));
			
			sale_id.write(c.playerName + "\t" + c.item + "\t" + c.quantity + "\t0\t" + c.price + "\t0\t" + "false");
			sale_id.newLine();
			
			item_id.write("" + NEXT_SALE_ID);
			item_id.newLine();
			
			name.write("" + NEXT_SALE_ID);
			name.newLine();
			
			//try {
				//CreateListing.getSingleton().createListing(NEXT_SALE_ID, c.item, c.getPA().getItemName(c.item), c.quantity, c.price, c.playerName, 0);
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}
			Sale sale = new Sale(NEXT_SALE_ID, c.playerName, c.item, c.quantity, 0, c.price, 0, false);
			
			++NEXT_SALE_ID;
			
			cache.addFirst(sale);
			
			if(!PRELOAD_ALL && cache.size() == CACHE_SIZE)
				cache.removeLast();
			sale_id.close();
			item_id.close();
			name.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (c.debugMessage)
			c.sendMessage("uneditItem "+c.uneditItem+" - c.item "+c.item+" - quanity: "+c.quantity);
		c.getItems().deleteItem2(c.uneditItem, c.quantity);
		openPost(c, true, false);
		PlayerSave.save(c);
	}
	
	/**
	 * Cancel a listing via its sale id
	 * @param c
	 * @param saleId
	 */
	
	public static void cancelListing(Player c, int id, int itemId) {
		if (id < 0 || itemId < 0)
			return;
		
		Sale sales = getSales(c.playerName).get(id); 
		int quantity = sales.getQuantity() - sales.getTotalSold(), saleItem = sales.getId();
		boolean stackable = Item.itemStackable[saleItem];
		boolean isNoted = Item.itemIsNote[saleItem];
		if(!stackable && !isNoted && quantity > 1) {
			saleItem++;
		}
		if(sales.getId() == itemId) { //needed to put 
			if(quantity > 0) {
				sales.setHasSold(true); //this code
				save(sales); //
				updateHistory(c, sales.getId(), sales.getTotalSold(), sales.getPrice()); //
				
				if ((((c.getItems().freeSlots() >= 1) || c.getItems().playerHasItem(itemId, 1)) && Item.itemIsNote[itemId]) || ((c.getItems().freeSlots() > 0) && !Item.itemStackable[itemId])) {
					c.getItems().addItem(itemId, 1 * quantity);
					c.sendMessage("[@red@Trading Post@bla@] You succesfully cancel the offer for "+quantity+"x "+ItemAssistant.getItemName(sales.getId())+".");
			} else {// If inventory is full!
					c.getItems().addItemToBank(itemId, 1 * quantity);
					c.sendMessage("[@red@Trading Post@bla@] You succesfully cancel the offer for "+quantity+"x "+ItemAssistant.getItemName(sales.getId())+".");
					c.sendMessage("[@red@Trading Post@bla@] Your "+quantity+"x "+ItemAssistant.getItemName(sales.getId())+" was sent to your bank.");
				}
				
				loadPlayersListings(c); //
				PlayerSave.save(c); //
				//into the leftOver>0 if statement 
			} else {
				c.sendMessage("This item has already been sold/cancelled.");
				sales.setHasSold(true); //this code
				save(sales); //
			}
		}
	}
	/**
	 * Collecting your money via the button
	 * @param c
	 */
	
	public static void collectMoney(Player c) {
		LinkedList<Sale> sales = (LinkedList<Sale>) getSales(c.playerName);
		long moneyCollectable = 0;
		for(Sale sale : sales) {
			moneyCollectable += (sale.getPrice() * sale.getLastCollectedAmount());
			sale.setLastCollectedSold(0);
			save(sale);
		}
		if (moneyCollectable <= 0) {
			c.sendMessage("[@red@Trading Post@bla@] You collected no coins due to your coffer being empty.");
			moneyCollectable = 0;
			c.getPA().sendFrame126(Misc.format(moneyCollectable) + " GP", 48610);
			PlayerSave.save(c);
			return;
		}
		c.getItems().addItem(995, moneyCollectable); 
		c.sendMessage("[@red@Trading Post@bla@] You successfully collect "+Misc.format(moneyCollectable)+" coins from your coffer.");
		moneyCollectable = 0;
		c.getPA().sendFrame126(Misc.format(moneyCollectable) + " GP", 48610);
		PlayerSave.save(c);
	}
	
	
	
	
	public static void save(Sale sale) {
		String line;
		String newLine = "";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(SALE_DIRECTORY+sale.getSaleId()+".txt"));
			writer.write(sale.getName() + "\t" + sale.getId() + "\t" + sale.getQuantity() + "\t" + sale.getTotalSold() + "\t" + sale.getPrice() + "\t" + sale.getLastCollectedAmount() + "\t" + sale.hasSold());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(sale.hasSold()) {
			if(sale.getLastCollectedAmount() > 0) {
				Player c = (Player) PlayerHandler.players[PlayerHandler.getPlayerID(sale.getName())];
				c.getItems().addItem(995, sale.getLastCollectedAmount() * sale.getPrice());
				sale.setLastCollectedSold(0);
			}
			try {
				
				BufferedReader read = new BufferedReader(new FileReader(PLAYERS_DIRECTORY+sale.getName()+".txt"));
				while((line = read.readLine()) != null) {
					if(line.equals(Integer.toString(sale.getSaleId()))) continue;
					newLine += line + System.getProperty("line.separator");
				}
				read.close(); 
				BufferedWriter write = new BufferedWriter(new FileWriter(PLAYERS_DIRECTORY+sale.getName()+".txt"));
				write.write(newLine);
				write.close();
				newLine = "";
				read = new BufferedReader(new FileReader(ITEMS_DIRECTORY+sale.getId()+".txt"));
				while((line = read.readLine()) != null) {
					if(line.equals(Integer.toString(sale.getSaleId()))) continue;
					newLine += line + System.getProperty("line.separator");
				}
				read.close();
				write = new BufferedWriter(new FileWriter(ITEMS_DIRECTORY+sale.getId()+".txt"));
				write.write(newLine);
				write.close();
				newLine = "";
				write = new BufferedWriter(new FileWriter(SALE_DIRECTORY+sale.getSaleId()+".txt"));
				newLine = sale.getName() + "\t" + sale.getId() + "\t" + sale.getQuantity() + "\t" + sale.getTotalSold() + "\t" + sale.getPrice() + "\t" + sale.getLastCollectedAmount() + "\t" + sale.hasSold();
				write.write(newLine);
				write.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Displays the 6 sales based on pages and item name/player name and recent
	 * @param sales
	 * @param c
	 */
	
	public static void displayResults(List<Sale> sales, Player c) {
		c.sendMessage(":resetpost:");
		List<GameItem> result = new ArrayList<GameItem>();
		List<Integer> idk = new ArrayList<Integer>();
		int total = 0, skipped = 0, start = 26023;
		for(Sale sale : sales) {
			if(sale.hasSold() || sale.getTotalSold() == sale.getQuantity()) continue;
			//if(skipped < (c.pageId - 1) * 50) { skipped++; continue; }
			
			result.add(new GameItem(sale.getId(), sale.getQuantity() - sale.getTotalSold()));
			idk.add(sale.getSaleId());
			c.getPA().sendFrame126(ItemAssistant.getItemName(sale.getId()), start);
			start++;
			String each = sale.getQuantity() - sale.getTotalSold() > 1 ? " each" : "";
			c.getPA().sendFrame126(Misc.format(sale.getPrice()) + each, start);
			start++;
			c.getPA().sendFrame126(sale.getName(), start);
			start++;
			c.getPA().sendFrame126(Integer.toString(sale.getTotalSold()), start);
			start++;
			total++;
			if(total == 250) {
				//System.out.println("Reached 50 recent sales");
				break;
			}
		}
		PlayerAssistant.sendItems(c, 26022, result, 250);
		for(int i = start; i < 27023; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.saleResults = idk;
	}
	
	/**
	 * Loads the recent sales
	 * @param c
	 */
	
	public static void loadRecent(Player c) {
		if (System.currentTimeMillis() - c.lastTradingPostView <= 5000) {
			c.sendMessage("You must wait before doing this again.");
			return;
		}
		c.lastTradingPostView = System.currentTimeMillis();
		c.pageId = 0;
		c.searchId = 3;
		c.getPA().sendFrame126("Trading Post - Recent listings", 48019);
		c.getPA().showInterface(48000);
		List<Sale> sales = new LinkedList<Sale>();
		int total = 0;

		for(int i = NEXT_SALE_ID - 1; i > 0; i--) {
			Sale sale = getSale(i);
			if(sale.hasSold()) continue;
			total++;
			sales.add(sale);
			if(total == 250)
				break;
		}
		displayResults(sales, c);
	}
	
	public static void buyListing(Player c, int slot, int amount) {
		if (c.inTradingPost == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
			return;
		}
		if (c.inTradingPost == false) {
			CheatEngineBlock.tradingPostAlert(c);
			return;
		}
		if (!c.getMode().isTradingPermitted()) {
			c.sendMessage("You are not permitted to make use of this.");
			return;
		}
		
		Sale sales = getSale(c.saleResults.get(slot));
		
		
		int totalQuantity = sales.getQuantity();
		int alreadySold = sales.getTotalSold();
		int totalStock = (totalQuantity)-alreadySold;
		if (sales.hasSold() == true) {
				c.sendMessage("This item has already been sold/cancelled.");
				return;
		}
		if(sales.getQuantity() == sales.getTotalSold()) {
			return;
		}
		if(totalStock < amount) {
			amount = totalStock;
		}
		if (totalStock < 1) {
			c.sendMessage("Seems to be no more stock, try again.");
			c.sendMessage("Total Stock = "+totalStock+"");
			c.sendMessage("Purchase Amount = "+amount+"");
			return;
		}
		
		if(sales.getQuantity() == sales.getTotalSold()) {
			return;
		}
		
		if(sales.getName().equalsIgnoreCase(c.playerName)) {
			c.sendMessage("[@red@Trading Post@bla@] You cannot buy your own listings.");
			return;
		}
		
		if(amount > sales.getQuantity()) {
			amount = sales.getQuantity();
		}
		
		if(!c.getItems().playerHasItem(995, sales.getPrice() * amount)) {
			c.sendMessage("[@red@Trading Post@bla@] You need atleast "+Misc.format(sales.getPrice() * amount)+" coins to buy the "+amount+"x "+ItemAssistant.getItemName(sales.getId())+".");
			return;
		}
		int slotsNeeded = amount;
		
		int saleItem = sales.getId();
		
		if(amount > 1 && Item.itemIsNote[sales.getId()+1]) {
			saleItem++;
		}
		
		if(c.getItems().freeSlots() < slotsNeeded && (!Item.itemIsNote[sales.getId()+1] && !Item.itemStackable[sales.getId()])) {
			c.sendMessage("[@red@Trading Post@bla@] You need atleast "+ slotsNeeded +" free slots to buy this.");
			return;
		}
		
		c.getItems().deleteItem(995, sales.getPrice() * amount);
		c.getItems().addItem(saleItem, amount);
		c.sendMessage("[@red@Trading Post@bla@] You succesfully purchase "+ amount +"x "+ItemAssistant.getItemName(sales.getId())+".");
		c.getItems().resetItems(3214);
		PlayerSave.save(c);
		
		sales.setLastCollectedSold(sales.getLastCollectedAmount() + amount);
		sales.setTotalSold(sales.getTotalSold() + amount);
		save(sales);
		if(PlayerHandler.getPlayerID(sales.getName()) != -1) {
			Player seller = (Player) PlayerHandler.players[PlayerHandler.getPlayerID(sales.getName())];
			if(seller != null) {
				if(seller.playerName.equalsIgnoreCase(sales.getName())) {
					if(sales.getTotalSold() < sales.getQuantity()) {
						seller.sendMessage("[@red@Trading Post@bla@] You succesfully sold "+ amount +"x of your "+ItemAssistant.getItemName(sales.getId())+".");
					}
					else {
						seller.sendMessage("[@red@Trading Post@bla@] Finished selling your "+ItemAssistant.getItemName(sales.getId())+".");
						emptyInterface(seller, false);
					}					
					PlayerSave.save(seller);
					if(seller.isListing) {
						loadPlayersListings(seller);
					}
				}
			}
		}
    }
	/**
	 * Loads the sales via playerName
	 * @param c
	 * @param playerName
	 */
	
	public static void loadPlayerName(Player c, String playerName) {
		if (System.currentTimeMillis() - c.lastTradingPostView <= 5000) {
			c.sendMessage("You must wait before doing this again.");
			return;
		}
		c.lastTradingPostView = System.currentTimeMillis();
		c.lookup = playerName;
		playerName = playerName.replace("_"," ");
		c.searchId = 2;
		c.getPA().showInterface(48000);
		c.getPA().sendFrame126("Trading Post - Searching for player: " + playerName, 48019);

		List<Sale> sales = new LinkedList<Sale>();

		for(String s : new File(PLAYERS_DIRECTORY).list()) {
			s = s.substring(0, s.indexOf(".")).toLowerCase();
			if(s.contains(playerName.toLowerCase()))
				sales.addAll(getSales(s));
			}

		 displayResults(sales, c);
	}
	
	/**
	 * Loads the sales via itemName
	 * @param c
	 * @param itemName
	 */
	
	public static void loadItemName(Player c, String itemName) {
		if (System.currentTimeMillis() - c.lastTradingPostView <= 5000) {
			c.sendMessage("You must wait before doing this again.");
			return;
		}
		c.lastTradingPostView = System.currentTimeMillis();
		c.lookup = itemName;
		itemName = itemName.replace("_"," ");
		c.searchId = 1;
		c.getPA().showInterface(48000);
		c.getPA().sendFrame126("Trading Post - Searching for item: " + itemName, 48019);
		  
		List<Integer> items = new LinkedList<Integer>();
		List<Sale> sales = new LinkedList<Sale>();

		for(String s : new File(ITEMS_DIRECTORY).list())
			items.add(Integer.parseInt(s.substring(0, s.indexOf("."))));

		for(int item : items) {
			//System.out.println("item: "+ItemAssistant.getItemName(item)+", itemName: " + itemName);
			if(ItemAssistant.getItemName(item).toLowerCase().contains(itemName.toLowerCase())) {
				sales.addAll(getSales(item));
			}
		}
		  
		displayResults(sales, c);
	}
	
	/**
	 * Resets all the necessary stuff;
	 * @param c
	 */
	
	public static void resetEverything(Player c) {
		c.inSelecting = false;
		c.isListing = true;
		c.insidePost = false;
		c.setSidebarInterface(3, 3213);
	}
	
	/**
	 * Handles the opening of the interface for offering an item
	 * @param c
	 */
	
	public static void openNewListing(Player c) {
		c.getPA().showInterface(48599);
		c.setSidebarInterface(3, 48500); // backpack tab
		for (int k = 0; k < 28; k++) {
			c.getPA().sendTradingPost(48501, c.playerItems[k]-1, k, c.playerItemsN[k]);
		}
	}
	
	/*
	 * 
	 * Handles the buttons of the interfaces
	 * 
	 */
	
	public static void postButtons(Player c, int button) {
		switch(button) {
			case 189237:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				int total = 0;
				LinkedList<Sale> sales = (LinkedList<Sale>) getSales(c.playerName);
				
				for(@SuppressWarnings("unused") Sale sale : sales)
					total++;
				if(c.amDonated <= 9 && total >= 6) {
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 6 listings as a regular player.");
					return;
				} else if(c.amDonated >= 10 && c.amDonated <= 49 && total >= 7) { //regular donator
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 7 listings as a regular donator rank.");
					return;
				} else if(c.amDonated >= 50 && c.amDonated <= 99 && total >= 8) { //super donator
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 8 listings as a super donator rank.");
					return;
				} else if(c.amDonated >= 100 && c.amDonated <= 199 && total >= 9) { //extreme donator
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 9 listings as a extreme donator rank.");
					return;
				} else if(c.amDonated >= 200 && c.amDonated <= 299 && total >= 10) { //ultra donator
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 10 listings as a ultra donator rank.");
					return;
				} else if(c.amDonated >= 300 && c.amDonated <= 499 && total >= 11) { //legendary donator
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 11 listings as a legendary donator rank.");
					return;
				} else if(c.amDonated >= 500 && c.amDonated <= 999 && total >= 12) { //diamond club
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 12 listings as a diamond club rank.");
					return;
				} else if(c.amDonated >= 1000 && c.amDonated <= 2500 && total >= 13) { //onyx club
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 13 listings as a onyx club rank.");
					return;
				} else if(c.amDonated >= 1000 && c.amDonated <= 2500 && total >= 14) { //platinum
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 14 listings as a platinum rank.");
					return;
				} else if(c.amDonated >= 2500 && c.amDonated <= 5000 && total >= 15) { //divine
					c.sendMessage("[@red@Trading Post@bla@] You cannot have more then 15 listings as a divine rank.");
					return;
				}
				if(!c.inSelecting) {
					openNewListing(c);
					c.inSelecting = true;
					c.getPA().sendFrame106(3);
				} else {
					resetEverything(c);
					c.getPA().showInterface(48600);
					c.getPA().sendFrame106(3);
				}
			break;
			
			case 59229: //Close select item
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				c.inTradingPost = false;
				c.getPA().closeAllWindows();
				resetEverything(c);
			break;
			
			case 191072:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				synchronized (c) {
					c.outStream.createFrame(191);
				}
				c.xInterfaceId = 191072;
			break;
			
			case 191075: // Removed quantity button
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				synchronized (c) {
					c.outStream.createFrame(192);
				}
				c.xInterfaceId = 191075;
			break;
			
			case 191078:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				confirmListing(c);
			break;
			
			case 189223:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				if (c.getItems().freeSlots() < 2) {
					c.sendMessage("Please clear up your inventory before collecting.");
					return;
				}
				collectMoney(c);
			break;
			
			case 189234:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				loadRecent(c);
			break;
			
			case 187133:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				openPost(c, false, false);
			break;
			
			case 187136:
				if (c.inTradingPost == false &&System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				if(c.pageId > 1)
					c.pageId--;
				c.sendMessage("You are already on the previous page.");
				switch(c.searchId) {
					case 1:
						loadItemName(c, c.lookup);
					break;
					case 2:
						loadPlayerName(c, c.lookup);
					break;
					case 3:
						loadRecent(c);
					break;
				}
			break;

			case 187139:
				if (c.inTradingPost == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inTradingPost == false) {
					CheatEngineBlock.tradingPostAlert(c);
					return;
				}
				if (!c.getMode().isTradingPermitted()) {
					c.sendMessage("You are not permitted to make use of this.");
					return;
				}
				c.pageId+=1;
				c.sendMessage("You can only view recents on this page for now.");
				switch(c.searchId) {
					case 1:
						loadItemName(c, c.lookup);
					break;
					case 2:
						loadPlayerName(c, c.lookup);
					break;
					case 3:
						loadRecent(c);
					break;
				}
			break;
		}
	}
	
	/*
	 * 
	 * This method makes it so it cleans out the history and my offers.
	 * Incase you had a diffrent account with more listings.
	 * 
	 */
	
	public static void emptyInterface(Player c, boolean b) {
		for(int i = 0; i < 15; i++) {
			c.getPA().sendTradingPost(48847, -1, i, -1);
		}
		if(b) {
			for(int i = 48636; i < 48656; i++) {
				c.getPA().sendFrame126("", i);
			}
		}
		for(int i = 48787; i < 48847; i++) {
			c.getPA().sendFrame126("", i);
		}
	}
	
	/*
	 * 
	 * Turns the 100,000,000 into 100m etc.
	 * 
	 */
	
	private static String zerosintomills(int j) {
		if(j >= 0 && j < 1000)
			return String.valueOf(j);
		if(j >= 1000 && j < 10000000)
			return j / 1000 + "K";
		if(j >= 10000000 && j  < 2147483647)
			return j / 1000000 + "M";
		return String.valueOf(j);
	}
	
	private static void updateHistory(Player c, int itemId, int amount, int price) {
		//System.out.println("itemId - " + itemId);
		//System.out.println("amount - " + amount);
		//System.out.println("price - " + price);
		c.saleItems.add(0, itemId);
		c.saleItems.remove(c.saleItems.size()-1);
		c.saleAmount.add(0, amount);
		c.saleAmount.remove(c.saleAmount.size()-1);
		c.salePrice.add(0, price);
		c.salePrice.remove(c.salePrice.size()-1);
		loadHistory(c);
	}
	
}