package ethos.model.items;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ethos.model.items.item_combinations.*;

public enum ItemCombinations {
	
	SARADOMINS_BLESSED_SWORD(new SaradominsBlessedSword(new GameItem(12809), Optional.of(Arrays.asList(new GameItem(12804))), new GameItem[] { new GameItem(12804), new GameItem(11838) })),

	AMULET_OF_FURY(new AmuletOfFury(new GameItem(12436), Optional.of(Arrays.asList(new GameItem(6585), new GameItem(12526))), new GameItem[] { new GameItem(6585), new GameItem(12526) })),
	
	AMULET_OF_TORTURE(new AmuletOfTorture(new GameItem(20366), Optional.of(Arrays.asList(new GameItem(19553), new GameItem(20062))), new GameItem[] { new GameItem(19553), new GameItem(20062) })),

	AMULET_OF_ANGUISH(new AmuletOfAnguish(new GameItem(22249), Optional.of(Arrays.asList(new GameItem(19547), new GameItem(22246))), new GameItem[] { new GameItem(19547), new GameItem(22246) })),

	ARMADYL_GODSWORD(new ArmadylGodsword(new GameItem(20368), Optional.of(Arrays.asList(new GameItem(11802), new GameItem(20068))), new GameItem[] { new GameItem(11802), new GameItem(20068) })),
	
	SARADOMIN_GODSWORD(new SaradominGodsword(new GameItem(20372), Optional.of(Arrays.asList(new GameItem(11806), new GameItem(20074))), new GameItem[] { new GameItem(11806), new GameItem(20074) })),
	
	BANDOS_GODSWORD(new BandosGodsword(new GameItem(20370), Optional.of(Arrays.asList(new GameItem(11804), new GameItem(20071))), new GameItem[] { new GameItem(11804), new GameItem(20071) })),

	ZAMORAK_GODSWORD(new ZamorakGodsword(new GameItem(20374), Optional.of(Arrays.asList(new GameItem(11808), new GameItem(20077))), new GameItem[] { new GameItem(11808), new GameItem(20077) })),
	
	OCCULT_NECKLACE(new OccultNecklace(new GameItem(19720), Optional.of(Arrays.asList(new GameItem(12002), new GameItem(20065))), new GameItem[] { new GameItem(12002), new GameItem(20065) })),

	INFERNAL_MAX_CAPE(new InfernalMaxCape(new GameItem(21285), Optional.of(Arrays.asList(new GameItem(21295), new GameItem(13342))), new GameItem[] { new GameItem(21295), new GameItem(13342) })),

	BLUE_DARK_BOW(new BlueDarkBow(new GameItem(12765), Optional.empty(), new GameItem[] { new GameItem(11235), new GameItem(12757) })),

	GREEN_DARK_BOW(new GreenDarkBow(new GameItem(12766), Optional.empty(), new GameItem[] { new GameItem(11235), new GameItem(12759) })),

	YELLOW_DARK_BOW(new YellowDarkBow(new GameItem(12767), Optional.empty(), new GameItem[] { new GameItem(11235), new GameItem(12761) })),

	WHITE_DARK_BOW(new WhiteDarkBow(new GameItem(12768), Optional.empty(), new GameItem[] { new GameItem(11235), new GameItem(12763) })),

	MALEDICTION_WARD(new MaledictionWard(new GameItem(12806), Optional.of(Arrays.asList(new GameItem(11924))), new GameItem[] { new GameItem(11924), new GameItem(12802) })),

	ODIUM_WARD(new OdiumWard(new GameItem(12807), Optional.of(Arrays.asList(new GameItem(11926))), new GameItem[] { new GameItem(11926), new GameItem(12802) })), 
	
	STEAM_STAFF(new SteamStaff(new GameItem(12796), Optional.of(Arrays.asList(new GameItem(11789))), new GameItem[] { new GameItem(11789), new GameItem(12798) })),

	GRANITE_MAUL(new GraniteMaul(new GameItem(12848), Optional.of(Arrays.asList(new GameItem(4153))), new GameItem[] { new GameItem(4153), new GameItem(12849) })),

	DRAGON_PICKAXE(new DragonPickaxe(new GameItem(12797), Optional.of(Arrays.asList(new GameItem(11920))), new GameItem[] { new GameItem(12800), new GameItem(11920) })),

	BLESSED_SPIRIT_SHIELD(new BlessedSpiritShield(new GameItem(12831), Optional.empty(), new GameItem[] { new GameItem(12829), new GameItem(12833) })),

	ARCANE_SPIRIT_SHIELD(new ArcaneSpiritShield(new GameItem(12825), Optional.empty(), new GameItem[] { new GameItem(12827), new GameItem(12831) })),

	ELYSIAN_SPIRIT_SHIELD(new ElysianSpiritShield(new GameItem(12817), Optional.empty(), new GameItem[] { new GameItem(12819), new GameItem(12831) })),

	SPECTRAL_SPIRIT_SHIELD(new SpectralSpiritShield(new GameItem(12821), Optional.empty(), new GameItem[] { new GameItem(12823), new GameItem(12831) })),

	TENTACLE_WHIP(new TentacleWhip(new GameItem(12006), Optional.of(Arrays.asList(new GameItem(12004))), new GameItem[] { new GameItem(12004), new GameItem(4151) })),

	HOLY_BOOK(new HolyBook(new GameItem(3840), Optional.empty(), 
			new GameItem[] { new GameItem(3839), new GameItem(3827), new GameItem(3828), new GameItem(3829), new GameItem(3830) })),

	UNHOLY_BOOK(new UnholyBook(new GameItem(3842), Optional.empty(),
			new GameItem[] { new GameItem(3841), new GameItem(3831), new GameItem(3832), new GameItem(3833), new GameItem(3834) })),

	BALANCE_BOOK(new BalanceBook(new GameItem(3844), Optional.empty(),
			new GameItem[] { new GameItem(3843), new GameItem(3835), new GameItem(3836), new GameItem(3837), new GameItem(3838) })),

	RING_OF_WEALTH_IMBUED(new RingOfWealthImbued(new GameItem(12785), Optional.empty(), new GameItem[] { new GameItem(2572), new GameItem(12783) })),

	ETERNAL_BOOTS(new EternalBoots(new GameItem(13235), Optional.empty(), new GameItem[] { new GameItem(13227), new GameItem(6920) })),

	PEGASIAN_BOOTS(new PegasianBoots(new GameItem(13237), Optional.empty(), new GameItem[] { new GameItem(13229), new GameItem(2577) })),

	PRIMORDIAL_BOOTS(new PrimordialBoots(new GameItem(13239), Optional.empty(), new GameItem[] { new GameItem(13231), new GameItem(11840) })),

	INFERNAL_AXE(new InfernalAxe(new GameItem(13241), Optional.empty(), new GameItem[] { new GameItem(13233), new GameItem(6739) })),

	INFERNAL_PICKAXE(new InfernalPickaxe(new GameItem(13243), Optional.empty(), new GameItem[] { new GameItem(13233), new GameItem(11920) })),

	FROZEN_ABYSSAL_WHIP(new FrozenAbyssalWhip(new GameItem(12774), Optional.empty(), new GameItem[] { new GameItem(12769), new GameItem(4151) })),

	DRAGON_DEFENDER(new DragonDefender(new GameItem(19722), Optional.empty(), new GameItem[] { new GameItem(12954), new GameItem(20143) })),

	VOLCANIC_ABYSSAL_WHIP(new VolcanicAbyssalWhip(new GameItem(12773), Optional.empty(), new GameItem[] { new GameItem(12771), new GameItem(4151) }));

	private ItemCombination itemCombination;

	private ItemCombinations(ItemCombination itemCombination) {
		this.itemCombination = itemCombination;
	}

	public ItemCombination getItemCombination() {
		return itemCombination;
	}

	static final Set<ItemCombinations> COMBINATIONS = Collections.unmodifiableSet(EnumSet.allOf(ItemCombinations.class));

	public static List<ItemCombinations> getCombinations(GameItem item1, GameItem item2) {
		return COMBINATIONS.stream().filter(combos -> combos.getItemCombination().itemsMatch(item1, item2)).collect(Collectors.toList());
	}

	public static Optional<ItemCombination> isRevertable(GameItem item) {
		Predicate<ItemCombinations> itemMatches = ic -> ic.getItemCombination().getRevertItems().isPresent() && ic.getItemCombination().getOutcome().getId() == item.getId();
		Optional<ItemCombinations> revertable = COMBINATIONS.stream().filter(itemMatches).findFirst();
		if (revertable.isPresent() && revertable.get().getItemCombination().isRevertable()) {
			return Optional.of(revertable.get().getItemCombination());
		}
		return Optional.empty();
	}

}
