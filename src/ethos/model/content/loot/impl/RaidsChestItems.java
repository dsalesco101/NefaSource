package ethos.model.content.loot.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethos.model.content.loot.LootRarity;
import ethos.model.items.GameItem;
import ethos.util.Misc;

public class RaidsChestItems {

    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    public static Map<LootRarity, List<GameItem>> getItems() {
        return items;
    }

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(11733, 1),  //overload (1)
                new GameItem(11733, 1),  //overload (1)
                new GameItem(566, 300),  //soul rune   6
                new GameItem(560, 300),  //death rune
                new GameItem(565, 300), //blood rune
                new GameItem(892, 300), //rune arrow
                new GameItem(11212, 72), //dragon arrow
                new GameItem(3050, 39), //grimy toadflax
                new GameItem(210, 32), //grimy irit
                new GameItem(212, 36), //grimy avantoe
                new GameItem(214, 40), //grimy kwuarm
                new GameItem(216, 31), //grimy candatine
                new GameItem(2486, 33), //grimy landatyme
                new GameItem(218, 32), //dwarf weed
                new GameItem(220, 20), //torstol
                new GameItem(454, 200), //coal
                new GameItem(13440, 80), //anglerfish
                new GameItem(448, 95), //mith ore
                new GameItem(450, 48), //addy ore
                new GameItem(19484, 88), //dragon javelin
                new GameItem(452, 25), //runite ore
                new GameItem(1776, 120), //molten glass
                new GameItem(1624, 90), //uncut saphire
                new GameItem(1374, 12), //rune b axe
                new GameItem(1080, 10), //rune platelegs
                new GameItem(1128, 8), //rune platebody
                new GameItem(3139, 67), //potato cactus
                new GameItem(1392, 13), //battle staff
                new GameItem(1622, 68), //uncut emerald
                new GameItem(1620, 42), //uncut ruby
                new GameItem(384, 90), //raw shark
                new GameItem(7937, 400), //pure essence
                new GameItem(1618, 35), //uncut diamonds
                new GameItem(19835, 1), //clue scroll master
                new GameItem(11733, 1),  //overload (1)
                new GameItem(11733, 1),  //overload (1)
                new GameItem(566, 300),  //soul rune   6
                new GameItem(560, 300),  //death rune
                new GameItem(565, 300), //blood rune
                new GameItem(892, 300), //rune arrow
                new GameItem(11212, 72), //dragon arrow
                new GameItem(3050, 39), //grimy toadflax
                new GameItem(210, 42), //grimy irit
                new GameItem(212, 46), //grimy avantoe
                new GameItem(214, 50), //grimy kwuarm
                new GameItem(216, 41), //grimy candatine
                new GameItem(2486, 43), //grimy landatyme
                new GameItem(218, 42), //dwarf weed
                new GameItem(220, 20), //torstol
                new GameItem(454, 200), //coal
                new GameItem(13440, 80), //anglerfish
                new GameItem(448, 85), //mith ore
                new GameItem(450, 58), //addy ore
                new GameItem(19484, 98), //dragon javelin
                new GameItem(452, 35), //runite ore
                new GameItem(1776, 140), //molten glass
                new GameItem(1624, 90), //uncut saphire
                new GameItem(1374, 12), //rune b axe
                new GameItem(1080, 10), //rune platelegs
                new GameItem(1128, 8), //rune platebody
                new GameItem(3139, 167), //potato cactus
                new GameItem(1392, 13), //battle staff
                new GameItem(1622, 78), //uncut emerald
                new GameItem(1620, 72), //uncut ruby
                new GameItem(384, 120), //raw shark
                new GameItem(7937, 500), //pure essence
                new GameItem(1618, 95), //uncut diamonds
                new GameItem(19835, 1), //clue scroll master
                new GameItem(21027, 1), //dark relic
                new GameItem(224, 10 + Misc.random(15)), //snape grass
                new GameItem(222, 10 + Misc.random(15)), //eye of newt
                new GameItem(240, 10 + Misc.random(15)), //white berries
                new GameItem(226, 10 + Misc.random(15)), //limp roots
                new GameItem(232, 10 + Misc.random(15)), //snape grass
                new GameItem(6694, 10 + Misc.random(15)), //crushed nests grass
                new GameItem(2971, 10 + Misc.random(15)), //mort myre fungus
                new GameItem(246, 10 + Misc.random(15)), //wine of zamoraks
                new GameItem(3055, 1 + Misc.random(3)), //lava battlestaff
                new GameItem(3052, 23))); //grimy snap dragons


        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll   		        COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll    		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll       		   COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll      		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll     		   COMMON
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll   		        COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll    		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll       		   COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll      		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll     		   COMMON
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll   		        COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll    		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll       		   COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll      		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll     		   COMMON
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll   		        COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll    		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll       		   COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll      		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll     		   COMMON
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll   		        COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll    		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll       		   COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll      		    COMMON
                new GameItem(22477, 1),  //avernic defender
                new GameItem(21000, 1),  //twisted buckler
                new GameItem(21015, 1),  //dinhs bulwark
                new GameItem(21009, 1),  //dragon sword
                new GameItem(21012, 1),  //dragon hunter crossbow
                new GameItem(21902, 1),  //dragon c'bow
                new GameItem(21006, 1),  //koadi wand
                new GameItem(21034, 1), //arcane scroll
                new GameItem(21079, 1), //dex scroll     		   COMMON
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(21018, 1),//ancestral hat
                new GameItem(21021, 1),//ancestral top
                new GameItem(21024, 1),//ancestral bottom
                new GameItem(22326, 1),  //justiciar helm
                new GameItem(22327, 1),  //justiciar top
                new GameItem(22328, 1),  //justiciar legs		UNCOMMON
                new GameItem(21003, 1), //elder maul      		  UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(21018, 1),//ancestral hat
                new GameItem(21021, 1),//ancestral top
                new GameItem(21024, 1),//ancestral bottom
                new GameItem(22326, 1),  //justiciar helm
                new GameItem(22327, 1),  //justiciar top
                new GameItem(22328, 1),  //justiciar legs
                new GameItem(21079, 1), //dex scroll
                new GameItem(21003, 1), //elder maul      	 	 UNCOMMON
                new GameItem(20784, 1),  //D claws
                new GameItem(21018, 1),//ancestral hat
                new GameItem(21021, 1),//ancestral top
                new GameItem(21024, 1),//ancestral bottom
                new GameItem(22326, 1),  //justiciar helm
                new GameItem(22327, 1),  //justiciar top
                new GameItem(22328, 1),  //justiciar legs		UNCOMMON
                new GameItem(20997, 1),  //twisted bow			RARE
                new GameItem(20851, 1), //olmlet pet			RARE
        		new GameItem(22325, 1)));  //olmlet pet			RARE


        //new GameItem(22325, 1)));//sythe of vitur
        //new GameItem(22323, 1),  //sanguinesti staff
    }

}
