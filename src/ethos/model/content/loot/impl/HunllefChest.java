package ethos.model.content.loot.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.model.content.loot.LootRarity;
import ethos.model.content.loot.Lootable;
import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class HunllefChest implements Lootable {

    private static final int KEY = 23776;
    private static final int ANIMATION = 881;

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(6739),//dragon axe
                new GameItem(6733),//archers ring
                new GameItem(6731),//seers ring
                new GameItem(6735),//warrior ring
                new GameItem(995, 240000), //coins
                new GameItem(2996, 35),//pkp tickets
                new GameItem(11230, 128),//dragon darts
                new GameItem(537, 50 + Misc.random(100)),//dragon bones
                new GameItem(1306, 3),//dragon longsword
                new GameItem(11840),//dragon boots
                new GameItem(11836),//bandos boots
                new GameItem(6585),//amulet of fury
                new GameItem(6737),//berserker ring
                new GameItem(6889),//mages book
                new GameItem(4724),//guthans helm
                new GameItem(4726),//guthans warspear
                new GameItem(4728),//guthans platebody
                new GameItem(4730),//guthans chainskirt
                new GameItem(4753),//veracs helm
                new GameItem(4757),//veracs brassard
                new GameItem(4759),//veracs plateskirt
                new GameItem(4716),//dharoks helm
                new GameItem(4718),//dharok axe
                new GameItem(4720),//dhark top
                new GameItem(4722),//dharok legs
                new GameItem(4736),//karil top
                new GameItem(4738),//karil bottom
                new GameItem(4734),//karils crossbow
                new GameItem(4732),//karils coif
                new GameItem(4708),//ahrims hood	
                new GameItem(4712),///ahrims ropetop
                new GameItem(4714),//ahrims robeskirt
                new GameItem(4745),//torags helm
                new GameItem(4749),//torags platebody
                new GameItem(4751),//torags platelegs
                new GameItem(537, 50 + Misc.random(100)),//dragon bones
                new GameItem(2364, 300),//runite bar
                new GameItem(1514, 500),// magic logs
                new GameItem(1632, 250),//uncut dragonstone
                new GameItem(2577),//ranger boots
                new GameItem(12596),//rangers tunic
                new GameItem(11920),//dragon pickaxe
                new GameItem(6739),//dragon axe
                new GameItem(6733),//archers ring
                new GameItem(6731),//seers ring
                new GameItem(6735),//warrior ring
                new GameItem(995, 240000), //coins
                new GameItem(2996, 35),//pkp tickets
                new GameItem(11230, 128),//dragon darts
                new GameItem(537, 50 + Misc.random(100)),//dragon bones
                new GameItem(1306, 3),//dragon longsword
                new GameItem(1080, 6),//rune platelegs
                new GameItem(1128, 6),//rune platebody
                new GameItem(4087, 1),//dragon platelegs
                new GameItem(4585, 1),//dragon plateskirt
                new GameItem(6585, 1),//fury
                new GameItem(4151, 1),//whip
                new GameItem(23804, 1),//imbuedifier
                new GameItem(11232, 50 + Misc.random(10)),//dragon darts
                new GameItem(11840),//dragon boots
                new GameItem(11836),//bandos boots
                new GameItem(6585),//amulet of fury
                new GameItem(6737),//berserker ring
                new GameItem(6889),//mages book
                new GameItem(4724),//guthans helm
                new GameItem(4726),//guthans warspear
                new GameItem(4728),//guthans platebody
                new GameItem(4730),//guthans chainskirt
                new GameItem(4753),//veracs helm
                new GameItem(4757),//veracs brassard
                new GameItem(4759),//veracs plateskirt
                new GameItem(4716),//dharoks helm
                new GameItem(4718),//dharok axe
                new GameItem(4720),//dhark top
                new GameItem(4722),//dharok legs
                new GameItem(4736),//karil top
                new GameItem(4738),//karil bottom
                new GameItem(4734),//karils crossbow
                new GameItem(4732),//karils coif
                new GameItem(4708),//ahrims hood	
                new GameItem(4712),///ahrims ropetop
                new GameItem(4714),//ahrims robeskirt
                new GameItem(4745),//torags helm
                new GameItem(4749),//torags platebody
                new GameItem(4751),//torags platelegs
                new GameItem(537, 250),//dragon bones
                new GameItem(2364, 300),//runite bar
                new GameItem(1514, 500),// magic logs
                new GameItem(1632, 250),//uncut dragonstone
                new GameItem(2577),//ranger boots
                new GameItem(12596),//rangers tunic
                new GameItem(11920),//dragon pickaxe
                new GameItem(6739),//dragon axe
                new GameItem(6733),//archers ring
                new GameItem(6731),//seers ring
                new GameItem(6735),//warrior ring
                new GameItem(995, 240000), //coins
                new GameItem(2996, 35),//pkp tickets
                new GameItem(12695, 10 + Misc.random(10)),//super combats 
                new GameItem(11230, 128),//dragon darts
                new GameItem(537, 92),//dragon bones
                new GameItem(1306, 3),//dragon longsword
                new GameItem(6739),//dragon axe
                new GameItem(6733),//archers ring
                new GameItem(6731),//seers ring
                new GameItem(6735),//warrior ring
                new GameItem(995, 240000), //coins
                new GameItem(2996, 35),//pkp tickets
                new GameItem(11230, 128),//dragon darts
                new GameItem(537, 92),//dragon bones
                new GameItem(1306, 3),//dragon longsword
                new GameItem(1080, 6),//rune platelegs
                new GameItem(1128, 6),//rune platebody
                new GameItem(4087, 1),//dragon platelegs
                new GameItem(4585, 1),//dragon plateskirt
                new GameItem(6585, 1),//fury
                new GameItem(4151, 1),//whip
                new GameItem(23804, 1),//imbuedifier
                new GameItem(11232, 50 + Misc.random(10)),//dragon darts
                new GameItem(11840, 1))); //dragon boots

        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(23975, 1), //crystal body-----------------------------2
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23757, 1), //younglleff pet 	 RARES//////////////////////////////////
                new GameItem(23995, 1), //blade of saeldor
                new GameItem(23848, 1), //corrupt legs
                new GameItem(23842, 1), //corrupt helm
                new GameItem(23845, 1), //corrupt body
                new GameItem(23848, 1), //corrupt legs
                new GameItem(23842, 1), //corrupt helm
                new GameItem(23845, 1), //corrupt body  	 RARES//////////////////////////////////
                new GameItem(23975, 1), //crystal body-----------------------------1
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------2
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------3
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------4
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------5
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------6
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------7
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------8
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------9
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1), //crystal legs
                new GameItem(23975, 1), //crystal body-----------------------------10
                new GameItem(23971, 1), //crystal helm
                new GameItem(23979, 1) //crystal legs
        ));
    }

    private static GameItem commonChestRewards() {

        List<GameItem> itemList = randomNumber() < 800 ? items.get(LootRarity.COMMON) : items.get(LootRarity.COMMON);
        return Misc.getRandomItem(itemList);

    }

    private static GameItem rareChestRewards() {

        List<GameItem> itemList = randomNumber() >= 800 ? items.get(LootRarity.RARE) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);

    }



    public final static int randomNumber() {
        final int random = Misc.random(1000);
        return random;
    }
    private static void randomChestRewards() {
        if (randomNumber() < 800) {
            commonChestRewards();
        }
        if (randomNumber() >= 800) {
            rareChestRewards();
        }

    }
    public static void rolledCommon(Player c) {
        int crystalshardbonus = Misc.random(29) + 10;
        int randompvmp = Misc.random(10);
        if (randomNumber() < 800) { //not a rare
            if (c.getItems().playerHasItem(KEY)) {
                c.getItems().deleteItem(KEY, 1);
                c.startAnimation(ANIMATION);
                GameItem commonreward = commonChestRewards();
                GameItem commonreward2 = commonChestRewards();
                GameItem commonreward3 = commonChestRewards();
                c.getItems().addItem(commonreward.getId(), commonreward.getAmount() * 1); //potentially gives the loot 3 times.	
                c.getItems().addItem(commonreward2.getId(), commonreward2.getAmount() * 1); //potentially gives the loot 3 times.	
                c.getItems().addItem(commonreward3.getId(), commonreward3.getAmount()* 1); //potentially gives the loot 3 times.	
                c.getItems().addItem(23877, crystalshardbonus);
                c.pvmp += 10 + randompvmp;
            } else if (!(c.getItems().playerHasItem(KEY))) {
                c.sendMessage("@blu@The chest is locked, it won't budge!");
            }
        }
    }
    public static void rolledRare(Player c) {
        int randompvmp = Misc.random(10);
        int crystalshardbonus = Misc.random(29) + 10;
        if (c.getItems().playerHasItem(KEY)) {
            c.getItems().deleteItem(KEY, 1);
            c.startAnimation(ANIMATION);
            GameItem rarereward = rareChestRewards();
            c.getItems().addItem(rarereward.getId(), rarereward.getAmount() * 1); //potentially gives the loot 3 times.	
            c.getItems().addItem(23877, crystalshardbonus);
            c.pvmp += 10 + randompvmp;
            PlayerHandler.executeGlobalMessage("@red@[Hunllef] @pur@" + c.playerName + " has just received a rare item from Hunllef's chest.");
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
        } else if (!(c.getItems().playerHasItem(KEY))) {
            c.sendMessage("@blu@The chest is locked, it won't budge!");
        }
    }
    
    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    @Override
    public void roll(Player c) {
        final int random = randomNumber();
        if (random < 800) { //not a rare
            rolledCommon(c);
        } else {
            rolledRare(c);
        }
    }
}
