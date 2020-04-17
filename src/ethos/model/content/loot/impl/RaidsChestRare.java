package ethos.model.content.loot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ethos.model.content.loot.LootRarity;
import ethos.model.content.loot.Lootable;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.minigames.raids.Raids;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class RaidsChestRare implements Lootable {

    private static final int KEY = Raids.RARE_KEY;
    private static final int ANIMATION = 881;


    private static GameItem randomChestRewards() {
        int random = Misc.random(1000);
        List<GameItem> itemList = RaidsChestItems.getItems().get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }

    public static ArrayList<GameItem> getRareDrops() {
        ArrayList<GameItem> drops = new ArrayList<>();
        List<GameItem> found = RaidsChestItems.getItems().get(LootRarity.RARE);
        for(GameItem f : found) {
            boolean foundItem = false;
            for(GameItem drop : drops) {
                if (drop.getId() == f.getId()) {
                    foundItem = true;
                    break;
                }
            }
            if (!foundItem) {
                drops.add(f);
            }
        }
        return drops;
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
            c.getCollectionLog().handleDrop(7554, reward.getId(), reward.getAmount());
            c.getItems().addItem(reward.getId(), reward.getAmount() * 1); //potentially gives the loot 3 times.
            c.sendMessage("@blu@You have received a rare item out of the storage unit.");
            c.pvmp += 10 + randompvmp;
            PlayerHandler.executeGlobalMessage("@bla@[@blu@RAIDS@bla@] "+c.playerName+"@pur@ has just received a@bla@ "+ Item.getItemName(reward.getId())+".");
        } else {
            c.sendMessage("@blu@The chest is locked, it won't budge!");
        }
    }
}
