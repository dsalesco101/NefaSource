package ethos.model.content.limitedItems;

import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class PetGoblin {
	public static int GOBLIN = 2269;
	static GameItem item;
	static int[] allowedItems = {11824, 11836, 405, 995, 11826, 11828, 11830, 23951, 13576, 2996, 11920, 7158, 12601, 12603, 11926, 12605, 11888,
			11924, 9194, 11990, 21817, 21807, 22552, 22547, 22542, 21257, 20517, 20519, 20595, 3122, 10564, 10589, 6809, 10887, 11823, 405, 11824,
			11834, 11832, 11818, 11820, 11822, 11838, 11785, 11791, 11810, 11812, 11814, 11816, 12833, 11829, 12819, 12823, 12827, 6739,
			6733, 6735, 6737, 6731, 23804, 12007, 19701, 22106, 11905, 12004, 12922, 12932, 12927, 6571, 13200, 13201, 13231, 13227, 13229, 13233,
			12002, 3140, 13273, 13263, 19592, 19601, 19586, 19589, 19610, 19553, 19547, 19550, 19544, 13577, 22006, 11826, 22966, 22969, 22971, 22973, 22983,
			22988, 
			};
	
	public static void npcSpeaking(Player player) {
		if (player.petGoblin == false) {
        	player.sendMessage("You do not own this pet.");
        	return;
        }
		player.getDH().sendDialogues(1070, 2269);
	}
	
	public static void npcFilter(Player player) {
			if (player.summonId == 2269) {
	        	player.sendMessage("You do not own this pet.");
	        	return;
	        }
			if (player.goblinFilter == true) {
			    player.goblinFilter = false;
			    player.sendMessage("@blu@You have @red@Disabled@blu@ your pet.");
			  } else {
			    player.goblinFilter = true;
			    player.sendMessage("@blu@You have @gre@Enabled@blu@ your pet.");
			}
	}
	public static void npcDropManagement(Player player) {
		if (player.summonId == PetGoblin.GOBLIN && player.goblinFilter == true) {
			if (Misc.linearSearch(PetGoblin.allowedItems, item.getId()) != -1) {
				player.getItems().addItemToBank(item.getId(), item.getAmount());
				item.changeDrop(-1, item.getAmount());
        		player.sendMessage("@yel@The pet goblin has picked up your @red@" + Item.getItemName(item.getId()) + "@yel@.");
        		NPC GOBLIN = NPCHandler.getNpc(2269);
        		NPCHandler.npcs[GOBLIN.getIndex()].forceChat("oo some juicy loot!");
					NPCHandler.npcs[GOBLIN.getIndex()].gfx100(626);
		}
	}

	} }
