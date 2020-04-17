package ethos.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.Server;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.items.GameItem;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.util.Misc;

public class SantaStolenItems {


	private static final Map<Rarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(Rarity.ITEMS, Arrays.asList(
				new GameItem(2209, 1), 
				new GameItem(2520, 1), 
				new GameItem(4083, 1), 
				new GameItem(22719, 1)));
	}

	private static GameItem randomChestRewards() {
		int random = Misc.random(100);
		List<GameItem> itemList = random < 80 ? items.get(Rarity.ITEMS) : items.get(Rarity.ITEMS);
		return Misc.getRandomItem(itemList);
	}


	public static void findStolenItems(Player c) {
			GameItem reward = randomChestRewards();
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
	}

	enum Rarity {
		ITEMS
	}

}