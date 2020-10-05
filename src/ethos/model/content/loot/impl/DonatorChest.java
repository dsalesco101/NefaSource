package ethos.model.content.loot.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.Server;
import ethos.model.content.loot.LootRarity;
import ethos.model.content.loot.Lootable;
import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.util.Misc;

public class DonatorChest implements Lootable {

	private static final int KEY = 2400;
	private static final int ANIMATION = 881;

	private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(LootRarity.COMMON, Arrays.asList(
				new GameItem(6828, 1), 
				new GameItem(6199, 1), 
				new GameItem(13346, 1))); 

		items.put(LootRarity.RARE, Arrays.asList(
				new GameItem(6828, 1), 
				new GameItem(6199, 1), 
				new GameItem(13346, 1))); 
	}

	private static GameItem randomChestRewards() {
		int random = Misc.random(100);
		List<GameItem> itemList = random < 80 ? items.get(LootRarity.COMMON) : items.get(LootRarity.RARE);
		return Misc.getRandomItem(itemList);
	}
	@Override
	public Map<LootRarity, List<GameItem>> getLoot() {
		return items;
	}

	@Override
	public void roll(Player c) {
		if (c.getItems().playerHasItem(KEY)) {
			c.getItems().deleteItem(KEY, 1);
			c.startAnimation(ANIMATION);
			GameItem reward = randomChestRewards();
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
		} else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
	}

}