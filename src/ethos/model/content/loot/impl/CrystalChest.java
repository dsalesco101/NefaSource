package ethos.model.content.loot.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.Server;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.loot.LootRarity;
import ethos.model.content.loot.Lootable;
import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.util.Misc;

public class CrystalChest implements Lootable {

	private static final int KEY = 989;
	private static final int DRAGONSTONE = 1631;
	private static final int KEY_HALVE1 = 985;
	private static final int KEY_HALVE2 = 987;
	private static final int ANIMATION = 881;

	private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(LootRarity.COMMON, Arrays.asList(
				new GameItem(995, 300000), //coins
				new GameItem(2435, 20),//prayer pots
				new GameItem(1712, 1),//glory (4)
				new GameItem(2677, 1), //clue easy
				new GameItem(454, 200), //coal
				new GameItem(1516, 200), //yew log
				new GameItem(208, 50), //grimy ranarr
				new GameItem(565, 750), //bloods
				new GameItem(560, 750), //deaths
				new GameItem(1632, 10), //uncut d stone
				new GameItem(537, 40), //d bones
				new GameItem(384, 50), //sharks
				new GameItem(4131, 1),
				new GameItem(995, 300000), //coins
				new GameItem(2435, 20),//prayer pots
				new GameItem(1712, 1),//glory (4)
				new GameItem(2677, 1), //clue easy
				new GameItem(454, 200), //coal
				new GameItem(1516, 200), //yew log
				new GameItem(208, 50), //grimy ranarr
				new GameItem(565, 750), //bloods
				new GameItem(560, 750), //deaths
				new GameItem(1632, 10), //uncut d stone
				new GameItem(537, 40), //d bones
				new GameItem(384, 50), //sharks
				new GameItem(4131, 1),
				new GameItem(995, 300000), //coins
				new GameItem(2435, 20),//prayer pots
				new GameItem(1712, 1),//glory (4)
				new GameItem(2677, 1), //clue easy
				new GameItem(454, 200), //coal
				new GameItem(1516, 200), //yew log
				new GameItem(208, 50), //grimy ranarr
				new GameItem(565, 750), //bloods
				new GameItem(560, 750), //deaths
				new GameItem(1632, 10), //uncut d stone
				new GameItem(537, 40), //d bones
				new GameItem(384, 50), //sharks
				new GameItem(4131, 1))); //rune boots

		items.put(LootRarity.RARE, Arrays.asList(
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(4724, 1),//guthan
				new GameItem(4726, 1),//guthan
				new GameItem(4728, 1),//guthan
				new GameItem(4730, 1),//guthan
				new GameItem(4753, 1),//verac
				new GameItem(4755, 1),//verac
				new GameItem(4757, 1),//verac
				new GameItem(4759, 1),
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(4724, 1),//guthan
				new GameItem(4726, 1),//guthan
				new GameItem(4728, 1),//guthan
				new GameItem(4730, 1),//guthan
				new GameItem(4753, 1),//verac
				new GameItem(4755, 1),//verac
				new GameItem(4757, 1),//verac
				new GameItem(4759, 1),
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(4724, 1),//guthan
				new GameItem(4726, 1),//guthan
				new GameItem(4728, 1),//guthan
				new GameItem(4730, 1),//guthan
				new GameItem(4753, 1),//verac
				new GameItem(4755, 1),//verac
				new GameItem(4757, 1),//verac
				new GameItem(4759, 1),
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(4724, 1),//guthan
				new GameItem(4726, 1),//guthan
				new GameItem(4728, 1),//guthan
				new GameItem(4730, 1),//guthan
				new GameItem(4753, 1),//verac
				new GameItem(4755, 1),//verac
				new GameItem(4757, 1),//verac
				new GameItem(4759, 1),
				new GameItem(386, 200), //shark
				new GameItem(990, 3), //crystal key
				new GameItem(995, 500000), //coins
				new GameItem(1377, 1),//dbaxe
				new GameItem(2368, 1), //right shield half
				new GameItem(2801, 1), //clue medium
				new GameItem(3145, 150),//karambwan
				new GameItem(6688, 30), //sara brews
				new GameItem(11840, 1),//d boots
				new GameItem(24034, 1),//dragonstone armour
				new GameItem(24037, 1),//dragonstone armour
				new GameItem(24040, 1),//dragonstone armour
				new GameItem(24043, 1),//dragonstone armour
				new GameItem(24046, 1),//dragonstone armour
				new GameItem(4724, 1),//guthan
				new GameItem(4726, 1),//guthan
				new GameItem(4728, 1),//guthan
				new GameItem(4730, 1),//guthan
				new GameItem(4753, 1),//verac
				new GameItem(4755, 1),//verac
				new GameItem(4757, 1),//verac
				new GameItem(4759, 1)));//verac
	}

	private static GameItem randomChestRewards() {
		int random = Misc.random(100);
		List<GameItem> itemList = random < 80 ? items.get(LootRarity.COMMON) : items.get(LootRarity.RARE);
		return Misc.getRandomItem(itemList);
	}

	public static void makeKey(Player c) {
		if (c.getItems().playerHasItem(KEY_HALVE1, 1) && c.getItems().playerHasItem(KEY_HALVE2, 1)) {
			c.getItems().deleteItem(KEY_HALVE1, 1);
			c.getItems().deleteItem(KEY_HALVE2, 1);
			c.getItems().addItem(KEY, 1);
		}
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
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = randomChestRewards();
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			Achievements.increase(c, AchievementType.LOOT_CRYSTAL_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
		} else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
	}

}