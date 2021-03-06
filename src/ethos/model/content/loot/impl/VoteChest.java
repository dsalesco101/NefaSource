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
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class VoteChest implements Lootable {

    public static final int KEY = 22093; //vote key heree
    private static final int ANIMATION = 881;

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(6666), //flippers15
                new GameItem(4151), //whip20
                new GameItem(6739), //d axe25
                new GameItem(2577), //ranger boots35
                new GameItem(12596), //ranger tunic25
                new GameItem(12600), //wreath30
                new GameItem(20214), //team cape x25
                new GameItem(6585), //fury30
                new GameItem(20217), //team cape i25
                new GameItem(20211))); //team cape zero25
        items.put(LootRarity.UNCOMMON, Arrays.asList(
                new GameItem(12639), //guthix halo45
                new GameItem(12637), //sara halo45
                new GameItem(12638), //zammy halo45
                new GameItem(2572), //row50
                new GameItem(12797), //d pickaxe60
                new GameItem(12526), //fury orn50
                new GameItem(10507), //reindeer hat
                new GameItem(23312), //sandwich outfit
                new GameItem(23315), //sandwich outfit
                new GameItem(23318), //sandwich outfit
                new GameItem(23357), //rain bpw
                new GameItem(23410), //wolf cloak
                new GameItem(23300), //parrot
                new GameItem(23407), //wolf head
                new GameItem(23413), //rock climbing boots g
                new GameItem(21028))); //harpoon


        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(977), //Alchemy Bag
                new GameItem(23360), //ham joint
                new GameItem(23303), //monk robe t
                new GameItem(23306), //monk robe t
                new GameItem(23925), //crystal crowns
                new GameItem(23923), //crystal crowns
                new GameItem(23921), //crystal crowns
                new GameItem(23919), //crystal crowns
                new GameItem(23917), //crystal crowns
                new GameItem(23915), //crystal crowns
                new GameItem(23913), //crystal crowns
                new GameItem(23911), //crystal crowns
                new GameItem(21439), //champ cape65
                new GameItem(20836), //giant presetn85
                new GameItem(10723), //jack lantern120
                new GameItem(5608), //fox150
                new GameItem(19941), //heavy casket85
                new GameItem(1037), //bunny ears
                new GameItem(13243), //infernal pickaxe150
                new GameItem(13241), //infernal axe150
                new GameItem(11235)));//dbow80
    }

    private static GameItem randomChestRewards(int chance) {
        int random = Misc.random(100);
        List<GameItem> itemList = random < 50 ? items.get(LootRarity.COMMON) : random >= 50 && random <= 90 ? items.get(LootRarity.UNCOMMON) : items.get(LootRarity.RARE);
        return Misc.getRandomItem(itemList);
    }

    private static void votePet(Player c) {
        int petchance = Misc.random(1500);
        if (petchance >= 1499) {
            c.getItems().addItem(21262, 1);
            PlayerHandler.executeGlobalMessage("@red@- "+c.playerName+"@blu@ has just received the @red@Vote Genie Pet");
            c.sendMessage("@red@@cr10@You pet genie is waiting in your bank, waiting to serve you as his master.");
            c.gfx100(1028);
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
            Achievements.increase(c, AchievementType.VOTE_CHEST_UNLOCK, 1);
            c.startAnimation(ANIMATION);
            GameItem reward = randomChestRewards(100);
            String name = ItemDefinition.forId(reward.getId()).getName();
            if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
                Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
            }
            c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
            PlayerHandler.executeGlobalMessage("@pur@["+c.playerName+"]@blu@ has just opened the vote chest and received a " + name + "!");
            int random = 1 + Misc.random(5);
            c.votePoints+= random;
            c.sendMessage("You have received an extra "+random+" vote points from the chest.");
            votePet(c);
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
            c.sendMessage("@red@You currently have only voted "+c.dayv+"/30 days so far.");
        }
    }
}
