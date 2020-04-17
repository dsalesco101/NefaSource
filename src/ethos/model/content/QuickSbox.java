package ethos.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.model.items.GameItem;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class QuickSbox {
	private static int mysteryPrize;
	private static final Map<Rarity, List<GameItem>> items = new HashMap<>();
	static {
		items.put(Rarity.COMMON, //50% chance
			Arrays.asList(
					new GameItem(12873),//guthan set
            		new GameItem(12875),//verac set
            		new GameItem(12877),//dharok set
            		new GameItem(12881),//ahrim set
            		new GameItem(12883),//karil set
            		new GameItem(12873),//guthan set
            		new GameItem(12875),//verac set
            		new GameItem(12877),//dharok set
            		new GameItem(12879),//torags set
            		new GameItem(12881),//ahrim set
            		new GameItem(12883),//karil set
                    new GameItem(11804),//BGS
            		new GameItem(11806),//SGS
            		new GameItem(11808),//ZGS
                    new GameItem(11770),//seers (i)
                    new GameItem(11771),//archers (i)
                    new GameItem(11772),//warrior (i)
                    new GameItem(11773),//berserker (i)
                    new GameItem(10346),//3rd age platelegs
                    new GameItem(10348),//3rd age platebody
                    new GameItem(10350),//3rd age helm
                    new GameItem(10352),//3rd age kite
                    new GameItem(10330),//3rd age range top
                    new GameItem(10332),//3rd age range bottom
                    new GameItem(10338),//3rd age robe top
                    new GameItem(10340),//3rd age robe bottom
                    new GameItem(11804),//BGS
            		new GameItem(11806),//SGS
            		new GameItem(11808),//ZGS
                    new GameItem(11770),//seers (i)
                    new GameItem(11771),//archers (i)
                    new GameItem(11772),//warrior (i)
                    new GameItem(11773),//berserker (i)
                    new GameItem(10346),//3rd age platelegs
                    new GameItem(10348),//3rd age platebody
                    new GameItem(10350),//3rd age helm
                    new GameItem(10352),//3rd age kite
                    new GameItem(10330),//3rd age range top
                    new GameItem(10332),//3rd age range bottom
                    new GameItem(10338),//3rd age robe top
                    new GameItem(10340)//3rd age robe bottom
					)
			
				 );

			        items.put(Rarity.UNCOMMON,//40% Chance
			                Arrays.asList(
			                		
			                		new GameItem(12873),//guthan set
			                		new GameItem(12875),//verac set
			                		new GameItem(12877),//dharok set
			                		new GameItem(12881),//ahrim set
			                		new GameItem(12883),//karil set
			                		new GameItem(2572),//ring of wealth
			                		new GameItem(6585),//fury
			                		new GameItem(4151),//whip
			                        new GameItem(11838),//saradomin sword
			                		new GameItem(12873),//guthan set
			                		new GameItem(12875),//verac set
			                		new GameItem(12877),//dharok set
			                		new GameItem(12879),//torags set
			                		new GameItem(12881),//ahrim set
			                		new GameItem(12883),//karil set
			                        new GameItem(11804),//BGS
			                		new GameItem(11806),//SGS
			                		new GameItem(11808),//ZGS
			                        new GameItem(11770),//seers (i)
			                        new GameItem(11771),//archers (i)
			                        new GameItem(11772),//warrior (i)
			                        new GameItem(11773),//berserker (i)
			                        new GameItem(10346),//3rd age platelegs
			                        new GameItem(10348),//3rd age platebody
			                        new GameItem(10350),//3rd age helm
			                        new GameItem(10352),//3rd age kite
			                        new GameItem(10330),//3rd age range top
			                        new GameItem(10332),//3rd age range bottom
			                        new GameItem(10338),//3rd age robe top
			                        new GameItem(10340),//3rd age robe bottom
			                        new GameItem(11804),//BGS
			                		new GameItem(11806),//SGS
			                		new GameItem(11808),//ZGS
			                        new GameItem(11770),//seers (i)
			                        new GameItem(11771),//archers (i)
			                        new GameItem(11772),//warrior (i)
			                        new GameItem(11773),//berserker (i)
			                        new GameItem(10346),//3rd age platelegs
			                        new GameItem(10348),//3rd age platebody
			                        new GameItem(10350),//3rd age helm
			                        new GameItem(10352),//3rd age kite
			                        new GameItem(10330),//3rd age range top
			                        new GameItem(10332),//3rd age range bottom
			                        new GameItem(10338),//3rd age robe top
			                        new GameItem(10340)//3rd age robe bottom
			                        )
			        );
		
		items.put(Rarity.RARE,//8% chance
				Arrays.asList(
						new GameItem(11802),//AGS
                		new GameItem(11826),//armadyl helm
                		new GameItem(11828),//armadyl body
                		new GameItem(11830),//armadyls legs
                        new GameItem(11832),//bandos chestplate
                        new GameItem(11834),//bandos tassets
                		new GameItem(4084),//sled
                		new GameItem(13346),//ultra mystery box
                		new GameItem(11785),//armadyl crossbow
                        new GameItem(12437, 1),//3rd age cape
                        new GameItem(12424, 1),//3rd age bow
                        new GameItem(12426, 1),//3rd age longsword
                        new GameItem(12422, 1),//3rd age wand
                        new GameItem(20014, 1),//3rd age pickaxe
                        new GameItem(2403, 1),//10 dollar scroll
                        new GameItem(13239, 1),//primordial boots
                        new GameItem(13235, 1),//eternal boots
                        new GameItem(13237, 1),//pegasian boots
                        new GameItem(12785)));//ring of wealth (i)
	};			
	private static GameItem randomChestRewards(Player c) {
		String name = ItemDefinition.forId(mysteryPrize).getName();
		int random = Misc.random(1000);
		if (random > 890) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>Super Mystery Box</col>] <col=255>" + Misc.formatPlayerName(c.playerName)
					+ "</col> hit the jackpot!");
			
			List<GameItem> itemList = random > 890 ? items.get(Rarity.RARE) : items.get(Rarity.RARE);
			return Misc.getRandomItem(itemList);
		} else if (random <= 880 && random > 450) {
				
				List<GameItem> itemList = (random <= 890 && random > 450) ? items.get(Rarity.UNCOMMON) : items.get(Rarity.UNCOMMON);
				return Misc.getRandomItem(itemList);
			} else {
		List<GameItem> itemList = random <= 450 ? items.get(Rarity.COMMON) : items.get(Rarity.COMMON);
		return Misc.getRandomItem(itemList);
	   }
   }


	public static void QuickOpen(Player c) {
		if (c.getItems().playerHasItem(6828, 1)) {
			c.getItems().deleteItem(6828, 1);
			GameItem reward =  randomChestRewards(c);
				c.getItems().addItem(reward.getId(), reward.getAmount() * 1); 
		} else {
			c.sendMessage("@blu@You have just used your last mystery box.");
		}
	}

	enum Rarity {
		RARE, COMMON, UNCOMMON
	}

}