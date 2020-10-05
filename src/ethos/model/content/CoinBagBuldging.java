package ethos.model.content;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.CoinBagLarge.Rarity;
import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.util.Misc;

import java.util.*;

/**
 * Revamped a simple means of receiving a random item based on chance.
 * 
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class CoinBagBuldging extends CycleEvent {

	/**
	 * The item id of the PvM Casket required to trigger the event
	 */
	public static final int MYSTERY_BOX = 10835; //Casket

	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static Map<Rarity, List<GameItem>> items = new HashMap<>();

	/**
	 * Stores an array of items into each map with the corresponding rarity to the list
	 */
	static {
		items.put(Rarity.COMMON,
			Arrays.asList(
					new GameItem(995, 31000))
	);

		items.put(Rarity.UNCOMMON,
				Arrays.asList(
						new GameItem(995, 5000))
	);

			items.put(Rarity.RARE,
					Arrays.asList(
							new GameItem(995, 70000))
	);


	}

	/**
	 * The player object that will be triggering this event
	 */
	private Player player;

	/**
	 * Constructs a new PvM Casket to handle item receiving for this player and this player alone
	 *
	 * @param player the player
	 */
	public CoinBagBuldging(Player player) {
		this.player = player;
	}

	/**
	 * Opens a PvM Casket if possible, and ultimately triggers and event, if possible.
	 *
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 1200) {
			return;
		}
		if (player.getItems().freeSlots() < 1 && !player.getItems().playerHasItem(995)) {
			player.sendMessage("You need at least one free slots to open a Coin Bag.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You need Coin Bag to do this.");
			return;
		}
		player.getItems().deleteItem(MYSTERY_BOX, 1);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}

	/**
	 * Executes the event for receiving the mystery box
	 */
	public void openall() {
		if (player.disconnected || Objects.isNull(player)) {
			return;
		}
		int coins = 90000 + Misc.random(150000);
		int coinsDouble = 3000000 + Misc.random(700000);
		int random = Misc.random(100);
		List<GameItem> itemList = random < 55 ? items.get(Rarity.COMMON) : random >= 55 && random <= 80 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);
		int amount = player.getItems().getItemAmount(10835);
		if (Misc.random(1000) == 10) {
			player.getItems().addItem(995, coins + coinsDouble* amount);
			player.getItems().addItem(item.getId(), item.getAmount()* amount);
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount() * amount);
			player.sendMessage("@red@You dig deeper and find a hidden magical abyss of coins!");
			player.getItems().deleteItem(10835, amount);
		} else {
			player.getItems().deleteItem(10835, amount);
			player.getItems().addItem(995, coins * amount);
			player.getItems().addItem(item.getId(), item.getAmount() * amount);
			player.sendMessage("You receive some coins!.");
		}
	}
	
	
	
	@Override
	public void execute(CycleEventContainer container) {
		if (player.disconnected || Objects.isNull(player)) {
			container.stop();
			return;
		}
		int coins = 100000 + Misc.random(150000);
		int coinsDouble = 1800000 + Misc.random(570000);
		int random = Misc.random(100);
		List<GameItem> itemList = random < 55 ? items.get(Rarity.COMMON) : random >= 55 && random <= 80 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);

		if (Misc.random(1000) == 10) {
			player.getItems().addItem(995, coins + coinsDouble);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
			player.sendMessage("@red@You dig deeper and find a hidden magical abyss of coins!");
		} else {
			player.getItems().addItem(995, coins);
			player.getItems().addItem(item.getId(), item.getAmount());
			player.sendMessage("You receive some coins!.");
		}
		container.stop();
	}

	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}

}