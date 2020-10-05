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

public class LarransChest implements Lootable {

	private static final int KEY = 23490;
	private static final int ANIMATION = 881;

	private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(LootRarity.COMMON, Arrays.asList(
				new GameItem(995, 300000), //coins
				new GameItem(1618, 150 + Misc.random(50)), //uncut diamonds
				new GameItem(1620, 150 + Misc.random(50)), //uncut rubys
				new GameItem(454, 450 + Misc.random(200)), //coal
				new GameItem(444, 150 + Misc.random(100)), //gold ore
				new GameItem(11237, 100 + Misc.random(65)), //dragon tips
				new GameItem(441, 500 + Misc.random(150)), //iron ore
				new GameItem(1164, 10 + Misc.random(5)), //rune full helm
				new GameItem(1128, 10 + Misc.random(5)), //rune body
				new GameItem(1080, 10 + Misc.random(5)), //rune legs
				new GameItem(7937, 2500 + Misc.random(2000)), //pure essence
				new GameItem(360, 100 + Misc.random(350)), //raw tuna
				new GameItem(378, 180 + Misc.random(130)), //raw lobster
				new GameItem(372, 180 + Misc.random(100)), //raw swordfish
				new GameItem(384, 180 + Misc.random(50)), //raw shark
				new GameItem(995, 300000), //coins
				new GameItem(1618, 150 + Misc.random(50)), //uncut diamonds
				new GameItem(1620, 150 + Misc.random(50)), //uncut rubys
				new GameItem(454, 450 + Misc.random(200)), //coal
				new GameItem(444, 150 + Misc.random(100)), //gold ore
				new GameItem(11237, 100 + Misc.random(65)), //dragon tips
				new GameItem(441, 500 + Misc.random(150)), //iron ore
				new GameItem(1164, 10 + Misc.random(5)), //rune full helm
				new GameItem(1128, 10 + Misc.random(5)), //rune body
				new GameItem(1080, 10 + Misc.random(5)), //rune legs
				new GameItem(7937, 2500 + Misc.random(2000)), //pure essence
				new GameItem(360, 100 + Misc.random(350)), //raw tuna
				new GameItem(378, 180 + Misc.random(130)), //raw lobster
				new GameItem(372, 180 + Misc.random(100)), //raw swordfish
				new GameItem(384, 180 + Misc.random(50)), //raw shark
				//uncommon section
				new GameItem(452, 50 + Misc.random(75)), //runite ore
				new GameItem(2354, 350 + Misc.random(150)), //steel bars
				new GameItem(1514, 200 + Misc.random(35)), //magic logs
				new GameItem(11230, 50 + Misc.random(75)), //dragon darts
				new GameItem(5316, 3 + Misc.random(2)), //magic seed
				new GameItem(5295, 6 + Misc.random(9)), //ranarr seed
				new GameItem(5300, 6 + Misc.random(9)), //snapdragon seed

				new GameItem(0000, 1))); //rune boots

		items.put(LootRarity.RARE, Arrays.asList(
				new GameItem(1618, 150 + Misc.random(50)), //uncut diamonds
				new GameItem(1620, 150 + Misc.random(50)), //uncut rubys
				new GameItem(454, 450 + Misc.random(200)), //coal
				new GameItem(444, 150 + Misc.random(100)), //gold ore
				new GameItem(11237, 100 + Misc.random(65)), //dragon tips
				new GameItem(441, 500 + Misc.random(150)), //iron ore
				new GameItem(1164, 10 + Misc.random(5)), //rune full helm
				new GameItem(1128, 10 + Misc.random(5)), //rune body
				new GameItem(1080, 10 + Misc.random(5)), //rune legs
				new GameItem(7937, 2500 + Misc.random(2000)), //pure essence
				new GameItem(360, 100 + Misc.random(350)), //raw tuna
				new GameItem(378, 180 + Misc.random(130)), //raw lobster
				new GameItem(372, 180 + Misc.random(100)), //raw swordfish
				new GameItem(384, 180 + Misc.random(50)), //raw shark
				//uncommon section
				new GameItem(452, 50 + Misc.random(75)), //runite ore
				new GameItem(2354, 350 + Misc.random(150)), //steel bars
				new GameItem(1514, 200 + Misc.random(35)), //magic logs
				new GameItem(11230, 50 + Misc.random(75)), //dragon darts
				new GameItem(5316, 3 + Misc.random(2)), //magic seed
				new GameItem(5295, 6 + Misc.random(9)), //ranarr seed
				new GameItem(5300, 6 + Misc.random(9)), //snapdragon seed

				new GameItem(6792, 1), //seren key
				new GameItem(20080, 1), //mummy peices
				new GameItem(20083, 1), //mummy peices
				new GameItem(20086, 1), //mummy peices
				new GameItem(20089, 1), //mummy peices

				new GameItem(20520, 1), //elder chaos top
				new GameItem(20520, 1),//elder chaos robe
				new GameItem(20595, 1)));//elder chaos
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
			  if (c.amDonated >= 10 && c.amDonated < 201) { //donator, super, ultra  	
					c.sendMessage("@blu@Due to your Donator rank you have received a @red@1% @blu@Chest Rate Boost.");
			  } else if (c.getMode().isOsrs()) {
					c.sendMessage("@blu@Due to your Game Mode you have received a @red@1% @blu@Chest Rate Boost.");
		     } else if (c.amDonated >= 200 && c.amDonated <=300) { //donator
					c.sendMessage("@blu@Due to your Game Mode you have received a @red@2% @blu@Chest Rate Boost.");
		    } else if (c.amDonated > 300 && c.amDonated <= 500) {
				c.sendMessage("@blu@Due to your Game Mode you have received a @red@3% @blu@Chest Rate Boost.");
			} else if (c.amDonated > 500 && c.amDonated <= 1000) {
				c.sendMessage("@blu@Due to your Game Mode you have received a @red@4% @blu@Chest Rate Boost.");
			} else if (c.amDonated > 2500) {
				c.sendMessage("@blu@Due to your Game Mode you have received a @red@5% @blu@Chest Rate Boost.");
			}
		} else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
	}

}