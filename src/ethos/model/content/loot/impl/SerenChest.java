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
import ethos.model.players.Right;
import ethos.util.Misc;

public class SerenChest implements Lootable {

    private static final int KEY = 6792;
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
                new GameItem(12696, 10 + Misc.random(10)),//super combats 
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
        	    new GameItem(12785, 1), //dex scroll
                new GameItem(20784, 1), //dragon claws
                new GameItem(21034, 1), //dex scroll
                new GameItem(22622, 1), //statius warhammer
                new GameItem(22610, 1), //vesta spear
                new GameItem(22613, 1))); //vesta longsword
    }

    private static GameItem randomChestRewardsYoutube(int chance) {
        int random = Misc.random(chance);
        List<GameItem> itemList = random <= 950 ? items.get(LootRarity.COMMON) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }

    private static GameItem randomChestRewards(int chance) {
        int random = Misc.random(chance);
        List<GameItem> itemList = random <= 978 ? items.get(LootRarity.COMMON) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }
    
    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    @Override
    public void roll(Player c) {
        int pkpbonus = Misc.random(19) + 10;
        if (c.getItems().playerHasItem(KEY)) {
            c.getItems().deleteItem(KEY, 1);
            c.startAnimation(ANIMATION);
            c.pkp += pkpbonus;
            if (c.getRights().isOrInherits(Right.YOUTUBER)) {
                GameItem reward = randomChestRewardsYoutube(1000);
                if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
                    Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
                }
                c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
                c.sendMessage("@blu@You also receive @red@" + pkpbonus + " @blu@pkp as a bonus for killing a wildy boss.");
            } else {
                c.sendMessage("@blu@The chest is locked, it won't budge!");
            }
            if (!(c.getRights().isOrInherits(Right.YOUTUBER))) {
                GameItem reward = randomChestRewards(1000);
                if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
                    Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
                }
                c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
                c.sendMessage("@blu@You also receive @red@" + pkpbonus + " @blu@pkp as a bonus for killing a wildy boss.");
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
            }}
    }
}
