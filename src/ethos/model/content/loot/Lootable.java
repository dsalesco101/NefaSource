package ethos.model.content.loot;

import java.util.List;
import java.util.Map;

import ethos.model.items.GameItem;
import ethos.model.players.Player;

public interface Lootable {

    Map<LootRarity, List<GameItem>> getLoot();

    void roll(Player player);

}
