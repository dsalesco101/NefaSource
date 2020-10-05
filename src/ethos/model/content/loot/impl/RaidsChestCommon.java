package ethos.model.content.loot.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.model.content.loot.LootRarity;
import ethos.model.content.loot.Lootable;
import ethos.model.items.GameItem;
import ethos.model.minigames.raids.Raids;
import ethos.model.players.Player;
import ethos.util.Misc;

public class RaidsChestCommon implements Lootable {

    private static final int KEY = Raids.COMMON_KEY;
    private static final int ANIMATION = 881;



    private static GameItem randomChestRewards() {
        int random = Misc.random(1000);
        List<GameItem> itemList = RaidsChestItems.getItems().get(LootRarity.COMMON);
        return Misc.getRandomItem(itemList);
    }
    
    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return RaidsChestItems.getItems();
    }

    @Override
    public void roll(Player c) {
        int randompvmp = Misc.random(10);
        if (c.getItems().playerHasItem(KEY)) {
            c.getItems().deleteItem(KEY, 1);
            c.startAnimation(ANIMATION);
            GameItem reward =  randomChestRewards();
            GameItem reward2 = randomChestRewards();
            GameItem reward3 = randomChestRewards();
            c.getItems().addItem(reward.getId(), reward.getAmount() * 1); //potentially gives the loot 3 times.
            c.getItems().addItem(reward2.getId(), reward2.getAmount() * 1); //potentially gives the loot 3 times.
            c.getItems().addItem(reward3.getId(), reward3.getAmount()* 1); //potentially gives the loot 3 times.
            c.sendMessage("@blu@You recieved a common item out of the storage unit.");
            c.pvmp += 10 + randompvmp;
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
