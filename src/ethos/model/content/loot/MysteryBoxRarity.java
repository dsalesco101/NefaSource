package ethos.model.content.loot;

import java.util.Arrays;
import java.util.Optional;

import com.google.common.base.Preconditions;

/**
 * Represents the rarity of a certain list of items
 */
public enum MysteryBoxRarity {
    COMMON(LootRarity.COMMON, "<col=336600>"),
    UNCOMMON(LootRarity.UNCOMMON, "<col=ffff00>"),
    RARE(LootRarity.RARE, "<col=B80000>");

    private LootRarity lootRarity;
    private String color;

    MysteryBoxRarity(LootRarity lootRarity, String color) {
        this.lootRarity = lootRarity;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static MysteryBoxRarity forId(int id) {
        Optional<MysteryBoxRarity> rarity = Arrays.stream(MysteryBoxRarity.values()).filter(r -> r.ordinal() == id).findFirst();
        Preconditions.checkState(rarity.isPresent(), "No rarity: " + id);
        return rarity.get();
    }

    public LootRarity getLootRarity() {
        return lootRarity;
    }
}
