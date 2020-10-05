package ethos.model.items;

import ethos.model.players.skills.hunter.impling.ItemRarity;
import lombok.Data;

@Data
public class LootItem { //TOB
	private final int id, min, max;
	private final ItemRarity rarity;
	public static LootItem of(int id, int min, int max, ItemRarity rarity) {
		return new LootItem(id, min, max, rarity);
	}
}