package ethos.model.content.limitedItems;

import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.UseItem;
import ethos.model.players.Player;
import ethos.util.Misc;

public class PetImp {
	
	public static int IMP = 5008;
	
	static GameItem item;
	
	public static int[] FOOD = {};
		
	public static void checkSlot1(Player c) {
		String s1Item = Item.getItemName(c.impS1);
		int s1Quantity = c.getItems().getItemAmount(c.impS1);
		
		if (c.impS1 <= 0) {
			c.getDH().sendStatement("You currently have nothing in slot 1.");
			return;
		}
		c.getDH().sendStatement("You currently have "+s1Quantity+" "+s1Item+" in your slot 1.");
	}
	public static void checkSlot2(Player c) {
		String s2Item = Item.getItemName(c.impS2);
		int s2Quantity = c.getItems().getItemAmount(c.impS2);

		if (c.impS2 <= 0) {
			c.getDH().sendStatement("You currently have nothing in slot 2.");
			return;
		}
		c.getDH().sendStatement("You currently have "+s2Quantity+" "+s2Item+" in your slot 1.");
	}
	public static void removeSlot1(Player c) {
		if (c.impS1 <= 0) {
			c.getDH().sendStatement("You currently have nothing in slot 1.");
			return;
		}
		 if (c.getItems().freeSlots() < c.s1Quantity) {
			 c.getDH().sendStatement("You need the correct amount of space to do this.");
             return;
         }	
		 c.getItems().addItem(c.impS1, c.s1Quantity);
		 c.impS1 = 0;
		 c.s1Quantity = 0;
	     c.getDH().sendStatement("You have succesfully removed your item from slot 1.");
	}
	public static void removeSlot2(Player c) {	
		if (c.impS2 <= 0) {
			c.getDH().sendStatement("You currently have nothing in slot 1.");
			return;
		}
		 if (c.getItems().freeSlots() < c.s2Quantity) {
			 c.getDH().sendStatement("You need the correct amount of space to do this.");
             return;
         }		
		 c.getItems().addItem(c.impS2, c.s2Quantity);
		 c.impS2 = 0;
		 c.s2Quantity = 0;
	     c.getDH().sendStatement("You have succesfully removed your item from slot 1.");
	}
	public static void addingSlots(Player player) {//THIS IS JUST A REFERENCE TO THE CODE in UseItem.java
		int itemId = player.itemId;
		if (Misc.linearSearch(PetImp.FOOD, itemId) != -1 && player.s1Quantity >= 30) {
			player.getDH().sendStatement("Slots 1 are full, please remove to add another.");
			return;
		}
		if (!(Misc.linearSearch(PetImp.FOOD, itemId) != -1) && player.s1Quantity >= 1) {
			player.getDH().sendStatement("Slots 1 are full, please remove to add another.");
			return;
		}
		if (Misc.linearSearch(PetImp.FOOD, itemId) != -1 && player.s2Quantity >= 30) {
			player.getDH().sendStatement("Slots 2 are full, please remove to add another.");
			return;
		}
		if (!(Misc.linearSearch(PetImp.FOOD, itemId) != -1) && player.s2Quantity >= 1) {
			player.getDH().sendStatement("Slots 2 are full, please remove to add another.");
			return;
		}
		if (!(Misc.linearSearch(PetImp.FOOD, itemId) != -1) && player.s2Quantity >= 1 &&  player.s1Quantity >= 1) {
			player.getDH().sendStatement("Both slots are full, please remove to add another.");
			return;
		}
		if (Misc.linearSearch(PetImp.FOOD, itemId) != -1 && player.s2Quantity >= 30 &&  player.s1Quantity >= 30) {
			player.getDH().sendStatement("Both slots are full, please remove to add another.");
			return;
		}
		if(player.impS1 <= 0 && player.s1Quantity <=0) {
			if (player.impS1 > 0 && player.impS1 != itemId) {
				player.getDH().sendStatement("You cant overwrite an item, you must remove first.");
				return;
			}
			int maxAdd = 30 - player.getItems().getItemAmount(player.s2Quantity);
			player.impS1 = itemId;
			player.s1Quantity +=maxAdd;
			String s1Item = Item.getItemName(player.impS1);
			player.getDH().sendStatement("You have succesfully added your "+maxAdd+" "+s1Item+" to slot 1.");
		}
		if (player.impS1 > 0 && player.s1Quantity > 0) {
			if (player.impS2 > 0 && player.impS2 != itemId) {
				player.getDH().sendStatement("You cant overwrite an item, you must remove first.");
				return;
			}
				int maxAdd = 30 - player.getItems().getItemAmount(player.s2Quantity);
				player.impS2 = itemId;
				player.s2Quantity +=maxAdd;
				String s2Item = Item.getItemName(player.impS2);
				player.getDH().sendStatement("You have succesfully added your "+maxAdd+" "+s2Item+" to slot 1.");
		}
	}
}
