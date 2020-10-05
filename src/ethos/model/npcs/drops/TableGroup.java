package ethos.model.npcs.drops;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.util.Misc;

@SuppressWarnings("serial")
public class TableGroup extends ArrayList<Table> {

	/**
	 * The non-playable character that has access to this group of tables
	 */
	private final List<Integer> npcIds;

	/**
	 * Creates a new group of tables
	 * 
	 * @param npcId the npc identification value
	 */
	public TableGroup(List<Integer> npcsIds) {
		this.npcIds = npcsIds;
	}

	/**
	 * Accesses each {@link Table} in this {@link TableGroup} with hopes of retrieving a {@link List} of {@link GameItem} objects.
	 * 
	 * @return
	 */
	public List<GameItem> access(Player player, double modifier, int repeats) {
		int rights = player.getRights().getPrimary().getValue() - 1;
		List<GameItem> items = new ArrayList<>();
		for (Table table : this) {
			TablePolicy policy = table.getPolicy();
			if (policy.equals(TablePolicy.CONSTANT)) {
				for (Drop drop : table) {
					int minimumAmount = drop.getMinimumAmount();

					items.add(new GameItem(drop.getItemId(), minimumAmount + Misc.random(drop.getMaximumAmount() - minimumAmount)));
				}
			} else {
				for (int i = 0; i < repeats; i++) {
					double chance = (1.0 / (double) (table.getAccessibility() * modifier)) * 100D;

					double roll = Misc.preciseRandom(Range.between(0.0, 100.0));

					if (chance > 100.0) {
						chance = 100.0;
					}
					if (roll <= chance) {
						Drop drop = table.fetchRandom();
						int minimumAmount = drop.getMinimumAmount();
						GameItem item = new GameItem(drop.getItemId(),
								minimumAmount + Misc.random(drop.getMaximumAmount() - minimumAmount));
						  if (policy.equals(TablePolicy.VERY_RARE) || policy.equals(TablePolicy.RARE)) {
	                            player.getCollectionLog().handleDrop(drop.getNpcIds().get(0), item.getId(), item.getAmount());
	                        }
	                        items.add(item);              
	                        if (policy.equals(TablePolicy.VERY_RARE) || policy.equals(TablePolicy.RARE)) { // TAKES STUFF OFF THE GLOBAL ANNOUNCEMENTS
	                            if (Item.getItemName(item.getId()).toLowerCase().contains("cowhide")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("feather")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dharok")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("guthan")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("black mask")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("staff of air")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("bronze")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("karil")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("ahrim")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("verac")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("torag")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("neitiznot") 
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("arrow")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("sq shield")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon dagger")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("rune warhammer")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("rock-shell")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("eye of newt")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon spear")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("rune battleaxe")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("casket")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon med helm")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon mace")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("silver ore")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("spined")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("wine of zamorak")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("rune spear")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("grimy")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("skeletal")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("jangerberries")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("goat horn dust")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("yew roots")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("white berries")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("bars")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("blue dragonscales")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("kebab")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("potato")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("shark")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("red")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("spined body")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("prayer")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("anchovy")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("runite")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("adamant")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("magic")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("yew")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("magic roots")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("earth battlestaff")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("torstol")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon battle axe")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("helm of neitiznot")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("mithril")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("sapphire")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("rune")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("toktz")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("steal")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("scale")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("seed")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("ancient")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("monk")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("splitbark")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("pure")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("zamorak robe")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("null")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("coins")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("essence")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("crushed")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("snape")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("unicorn")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("mystic")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("eye patch")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("steel darts")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("steel bar")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("limp")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("darts")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon longsword")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dust battlestaff")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("granite")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("coal")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("leaf-bladed sword")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon plateskirt")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon platelegs")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon scimitar")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("abyssal head")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("cockatrice head")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("dragon chainbody")
	                                    || Item.getItemName(item.getId()).toLowerCase().contains("bones")) {
	                            } else {
	                            	   String icon = "";
	                                   if (player.getRights().contains(Right.PLAYER) && player.amDonated <= 0) {
	                                       icon = "";
	                                   } else if (player.getRights().contains(Right.OSRS) && player.amDonated <= 0) {
	                                       icon = "@cr22@";
	                                   } else if (player.getRights().contains(Right.MED_MODE) && player.amDonated <= 0) {
	                                       icon = "@cr21@";
	                                   } else if (player.getRights().contains(Right.IRONMAN) && player.amDonated <= 0) {
	                                       icon = "@cr12@";
	                                   } else if (player.getRights().contains(Right.ULTIMATE_IRONMAN) && player.amDonated <= 0) {
	                                       icon = "@cr13@";
	                                   } else if (player.getRights().contains(Right.HC_IRONMAN) && player.amDonated <= 0) {
	                                       icon = "@cr9@";
	                                   } else if (player.getRights().contains(Right.HELPER) && player.amDonated >= 0) {
	                                       icon = "@cr10@";
	                                   } else if (player.getRights().contains(Right.MODERATOR) && player.amDonated >= 0) {
	                                       icon = "@cr1@";
	                                   } else if (player.getRights().contains(Right.OWNER) || player.getRights().contains(Right.GAME_DEVELOPER) && player.amDonated >= 0) {
	                                       icon = "@cr3@";
	                                   } else if (player.amDonated >= 10 && player.amDonated <= 49) { //donator
	                                       icon = "@cr4@";
	                                   } else 	if (player.amDonated >= 50 && player.amDonated <= 99) { //super
	                                       icon = "@cr5@";
	                                   } else 	if (player.amDonated >= 100 && player.amDonated <= 199) { //extreme
	                                       icon = "@cr6@";
	                                   } else 	if (player.amDonated >= 200 && player.amDonated <= 299) { //ultra
	                                       icon = "@cr7@";
	                                   } else 	if (player.amDonated >= 300 && player.amDonated <= 499) { //legendary
	                                       icon = "@cr8@";
	                                   } else 	if (player.amDonated >= 500 && player.amDonated <= 999) { // diamond club
	                                       icon = "@cr16@";
	                                   } else 	if (player.amDonated >= 1000 && player.amDonated <= 2499) { //onyx club
	                                       icon = "@cr17@";
	                                   } else 	if (player.amDonated >= 2500 && player.amDonated <= 4999) { //platinum
	                                       icon = "@cr18@";
	                                   } else 	if (player.amDonated >= 5000) { //divine
	                                       icon = "@cr19@";
	                                   } 
	            					PlayerHandler.executeGlobalMessage(
	            								"<col=FF0000>@cr11@[Lootations]"+icon+" </col><col=255>"+ Misc.capitalize(player.playerName) + ""
	            										+ "</col> received a <col=255>"+Item.getItemName(item.getId())+"!</col>.");
	                            }
	                        }
	                    }
	                }
	            }
	        }
	        return items;
	    }
	/**
	 * The non-playable character identification values that have access to this group of tables.
	 * 
	 * @return the non-playable character id values
	 */
	public List<Integer> getNpcIds() {
		return npcIds;
	}
}
