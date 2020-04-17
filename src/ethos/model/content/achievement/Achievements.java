package ethos.model.content.achievement;

import java.util.EnumSet;
import java.util.Set;

import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * @author Jason http://www.rune-server.org/members/jason
 * @date Mar 26, 2014
 */
public class Achievements {

    public enum Achievement {
        /**
         * Tier 1 Achievement Start
         */
       Crab_Killer(0, AchievementTier.TIER_1, AchievementType.SLAY_ROCK_CRABS, null, "Kill 100 Rock Crabs", 100, 1, new GameItem(995, 100000), new GameItem(4153), new GameItem(405)),
       Slaughter_House(1, AchievementTier.TIER_1, AchievementType.SLAY_CHICKENS, null, "Kill 200 Chickens", 200, 1, new GameItem(995, 115000), new GameItem(405), new GameItem(1945, 100)),
      Bby_Dragon_Hunter_I(2, AchievementTier.TIER_1, AchievementType.SLAY_BABY_DRAGONS, null, "Kill 50 Baby Dragons", 50, 1, new GameItem(995, 75000), new GameItem(535, 100), new GameItem(405)),
       DRAGON_SLAYER_I(3, AchievementTier.TIER_1, AchievementType.SLAY_DRAGONS, null, "Kill 25 Dragons", 25, 1, new GameItem(995, 100000), new GameItem(537, 40), new GameItem(405)),
       PvMer_I(4, AchievementTier.TIER_1, AchievementType.SLAY_ANY_NPCS, null, "Kill 1000 Npcs", 1000, 1, new GameItem(995, 500000), new GameItem(989, 2)),
       Boss_Hunter_I(5, AchievementTier.TIER_1, AchievementType.SLAY_BOSSES, null, "Kill 100 Bosses", 100, 1, new GameItem(995, 250000), new GameItem(405)),
       Fishing_Task_I(6, AchievementTier.TIER_1, AchievementType.FISH, null, "Catch 1000 Fish", 1000, 1, new GameItem(995, 100000), new GameItem(13258, 1), new GameItem(13261, 1)),
        Cooking_Task_I(7, AchievementTier.TIER_1, AchievementType.COOK, null, "Cook 1000 Fish", 1000, 1, new GameItem(995, 50000), new GameItem(3145, 100)),
        Mining_Task_I(8, AchievementTier.TIER_1, AchievementType.MINE, null, "Mine 1000 Rocks", 1000, 1, new GameItem(995, 100000), new GameItem(12013), new GameItem(12016)),
        Smithing_Task_I(9, AchievementTier.TIER_1, AchievementType.SMITH, null, "Smith 1000 Bars", 1000, 1, new GameItem(995, 350000), new GameItem(2360, 300)),
        Farming_Task_I(10, AchievementTier.TIER_1, AchievementType.FARM, null, "Pick Herbs 100 Times", 100, 1, new GameItem(995, 250000), new GameItem(208, 100), new GameItem(13646), new GameItem(13644)),
        Herblore_Task_I(11, AchievementTier.TIER_1, AchievementType.HERB, null, "Mix 500 Potions", 500, 1, new GameItem(995, 100000), new GameItem(100, 25)),
        Woodcutting_Task_I(12, AchievementTier.TIER_1, AchievementType.WOODCUT, null, "Cut 1000 Trees", 1000, 1, new GameItem(995, 100000), new GameItem(10941, 1), new GameItem(10933, 1)),
        Fletching_Task_I(13, AchievementTier.TIER_1, AchievementType.FLETCH, null, "Fletch 1000 Logs", 1000, 1, new GameItem(995, 100000), new GameItem(63, 150),  new GameItem(1778, 100)),
        Firemaking_Task_I(14, AchievementTier.TIER_1, AchievementType.FIRE, null, "Light 500 Logs", 500, 1, new GameItem(995, 10000), new GameItem(20710)),
        Theiving_Task_I(15, AchievementTier.TIER_1, AchievementType.THIEV, null, "Steal 500 Times", 500, 1, new GameItem(995, 100000), new GameItem(5557), new GameItem(5556)),
        Agility_Task_I(16, AchievementTier.TIER_1, AchievementType.AGIL, null, "Complete 100 Course Laps", 100, 1, new GameItem(995, 50000), new GameItem(11849, 5), new GameItem(12792, 1)),
        Slayer_Task_I(17, AchievementTier.TIER_1, AchievementType.SLAY, null, "Complete 50 Slayer Tasks", 50, 1, new GameItem(995, 450000), new GameItem(405)),
        CKey_Task(18, AchievementTier.TIER_1, AchievementType.LOOT_CRYSTAL_CHEST, null, "Loot Crystal Chest 50 Times", 50, 1, new GameItem(995, 500000), new GameItem(990, 4)),
        Pc_Task(19, AchievementTier.TIER_1, AchievementType.PEST_CONTROL_ROUNDS, null, "Complete Pest Control 50 Times", 50, 1, new GameItem(995, 500000), new GameItem(405), new GameItem(8841)),
        Jad_Task(20, AchievementTier.TIER_1, AchievementType.FIGHT_CAVES_ROUNDS, null, "Complete Fightcaves 3 Times", 3, 1, new GameItem(995, 100000), new GameItem(405), new GameItem(6570)),
        Barrows_Task_I(21, AchievementTier.TIER_1, AchievementType.BARROWS_KILLS, null, "Kill 100 barrows npcs", 100, 1, new GameItem(995, 150000), new GameItem(405)),
        ClueScroll_Task(22, AchievementTier.TIER_1, AchievementType.CLUES, null, "Loot 50 Clue Caskets", 50, 1, new GameItem(995, 250000)),
        NEWB_VOTER(23, AchievementTier.TIER_1, AchievementType.VOTE_CHEST_UNLOCK, null, "Open 1 Vote Chest", 1, 1, new GameItem(995, 2500000), new GameItem(23933, 5)),
        DAGGANOTH_MOTHER_I(24, AchievementTier.TIER_1, AchievementType.SLAY_DAGGANOTH_MOTHER, null, "Get 25 kills at dagganoth mother mini-game", 25, 1, new GameItem(810, 300), new GameItem(890, 300), new GameItem(562, 300)),
        COMPETITOR(25, AchievementTier.TIER_1, AchievementType.OTHER, null, "Enter 5 Tournaments", 5, 1, new GameItem(989, 1), new GameItem(11739, 1)),

        /**
         * Tier 2 Achievement Start
         */
        Bby_Dragon_Hunter_II(0, AchievementTier.TIER_2, AchievementType.SLAY_BABY_DRAGONS, null, "Kill 400 Baby Dragons", 400, 2, new GameItem(995, 400000), new GameItem(535, 250), new GameItem(405)),
        Dragon_Hunter_II(1, AchievementTier.TIER_2, AchievementType.SLAY_DRAGONS, null, "Kill 350 Dragons", 350, 2, new GameItem(995, 500000), new GameItem(537, 60), new GameItem(405)),
        PvMer_II(2, AchievementTier.TIER_2, AchievementType.SLAY_ANY_NPCS, null, "Kill 3000 Npcs", 3000, 2, new GameItem(995, 1000000), new GameItem(405)),
        Boss_Hunter_II(3, AchievementTier.TIER_2, AchievementType.SLAY_BOSSES, null, "Kill 700 Bosses", 700, 2, new GameItem(995, 1000000), new GameItem(405, 2)),
        INTERMEDIATE_FISHER(4, AchievementTier.TIER_2, AchievementType.FISH, null, "Catch 2500 Fish", 2500, 2, new GameItem(995, 500000), new GameItem(13259, 1)),
        INTERMEDIATE_CHEF(5, AchievementTier.TIER_2, AchievementType.COOK, null, "Cook 2500 Fish", 2500, 2, new GameItem(995, 350000), new GameItem(13441, 75)),
        INTERMEDIATE_MINER(6, AchievementTier.TIER_2, AchievementType.MINE, null, "Mine 2500 Rocks", 2500, 2, new GameItem(995, 400000), new GameItem(12015)),
        INTERMEDIATE_SMITH(7, AchievementTier.TIER_2, AchievementType.SMITH, null, "Smelt/Smith 2500 Bars", 2500, 2, new GameItem(995, 1250000), new GameItem(2362, 300)),
        INTERMEDIATE_FARMER(8, AchievementTier.TIER_2, AchievementType.FARM, null, "Pick Herbs 300 Times", 300, 2, new GameItem(995, 500000), new GameItem(2486, 100), new GameItem(13640)),
        INTERMEDIATE_MIXER(9, AchievementTier.TIER_2, AchievementType.HERB, null, "Mix 1000 Potions", 1000, 2, new GameItem(995, 300000), new GameItem(3005, 30)),
        INTERMEDIATE_CHOPPER(10, AchievementTier.TIER_2, AchievementType.WOODCUT, null, "Cut 2500 Trees", 2500, 2, new GameItem(10939, 1), new GameItem(995, 150000)),
        INTERMEDIATE_FLETCHER(11, AchievementTier.TIER_2, AchievementType.FLETCH, null, "Fletch 2500 Logs", 2500, 2, new GameItem(995, 500000), new GameItem(67, 225)),
        INTERMEDIATE_PYRO(12, AchievementTier.TIER_2, AchievementType.FIRE, null, "Light 1000 Logs", 1000, 2, new GameItem(995, 500000), new GameItem(20706), new GameItem(20704)),
        INTERMEDIATE_THIEF(13, AchievementTier.TIER_2, AchievementType.THIEV, null, "Steal 1000 Times", 1000, 2, new GameItem(995, 450000), new GameItem(5555), new GameItem(5553)),
        INTERMEDIATE_RUNNER(14, AchievementTier.TIER_2, AchievementType.AGIL, null, "Complete 250 Course Laps", 250, 2, new GameItem(995, 400000), new GameItem(989, 2)),
        SLAYER_DESTROYER(15, AchievementTier.TIER_2, AchievementType.SLAY, null, "Complete 80 Slayer Tasks", 80, 2, new GameItem(995, 1500000), new GameItem(405)),
        CHEST_LOOTER(16, AchievementTier.TIER_2, AchievementType.LOOT_CRYSTAL_CHEST, null, "Loot Crystal Chest 100 Times", 100, 2, new GameItem(995, 2000000), new GameItem(990, 8)),
        MR_PORTAL(17, AchievementTier.TIER_2, AchievementType.PEST_CONTROL_ROUNDS, null, "Complete Pest Control 120 Times", 120, 2, new GameItem(995, 750000), new GameItem(405, 2)),
        RED_OF_FURY(18, AchievementTier.TIER_2, AchievementType.FIGHT_CAVES_ROUNDS, null, "Complete Fightcaves 5 Times", 5, 2, new GameItem(995, 500000), new GameItem(405), new GameItem(6570, 2)),
        DHAROKER(19, AchievementTier.TIER_2, AchievementType.DH_KILLS, null, "Kill 300 dharok npcs", 300, 2, new GameItem(995, 1250000), new GameItem(405)),
        CLUE_SCROLLER(20, AchievementTier.TIER_2, AchievementType.CLUES, null, "Loot 120 Clue Caskets", 120, 2, new GameItem(995, 1700000)),
        DAGGANOTH_MOTHER_II(21, AchievementTier.TIER_2, AchievementType.SLAY_DAGGANOTH_MOTHER, null, "Get 50 kills at dagganoth mother mini-game", 50, 2, new GameItem(811, 300), new GameItem(892, 300), new GameItem(560, 300)),
        ADVANCED_VOTER(22, AchievementTier.TIER_2, AchievementType.VOTE_CHEST_UNLOCK, null, "Open 5 Vote Chests", 5, 2, new GameItem(6199, 1), new GameItem(11739, 1)),
        /**
         * Tier 3 Achievement Start
         */
        EXPERT_BABY_DRAGON_SLAYER(0, AchievementTier.TIER_3, AchievementType.SLAY_BABY_DRAGONS, null, "Kill 1000 Baby Dragons", 1000, 3, new GameItem(995, 1000000), new GameItem(405), new GameItem(535, 1100)),
        EXPERT_DRAGON_SLAYER(1, AchievementTier.TIER_3, AchievementType.SLAY_DRAGONS, null, "Kill 950 Dragons", 950, 3, new GameItem(995, 1500000), new GameItem(405), new GameItem(537, 150)),
        SLAUGHTERER(2, AchievementTier.TIER_3, AchievementType.SLAY_ANY_NPCS, null, "Kill 10000 Npcs", 10000, 3, new GameItem(995, 2000000), new GameItem(405, 1)),
        BOSS_SLAUGHTERER(3, AchievementTier.TIER_3, AchievementType.SLAY_BOSSES, null, "Kill 1500 Bosses", 1500, 3, new GameItem(995, 3000000), new GameItem(405, 6)),
        EXPERT_FISHER(4, AchievementTier.TIER_3, AchievementType.FISH, null, "Catch 5000 Fish", 5000, 3, new GameItem(995, 1000000), new GameItem(13260, 1)),
        EXPERT_CHEF(5, AchievementTier.TIER_3, AchievementType.COOK, null, "Cook 5000 Fish", 5000, 3, new GameItem(995, 1000000), new GameItem(13441, 350)),
        EXPERT_MINER(6, AchievementTier.TIER_3, AchievementType.MINE, null, "Mine 5000 Rocks", 5000, 3, new GameItem(995, 2500000), new GameItem(12014)),
        EXPERT_SMITH(7, AchievementTier.TIER_3, AchievementType.SMITH, null, "Smelt/Smith 5000 Bars", 5000, 3, new GameItem(995, 1000000), new GameItem(2364, 300)),
        EXPERT_FARMER(8, AchievementTier.TIER_3, AchievementType.FARM, null, "Pick Herbs 600 Times", 600, 3, new GameItem(995, 2500000), new GameItem(220, 150), new GameItem(13642)),
        EXPERT_MIXER(9, AchievementTier.TIER_3, AchievementType.HERB, null, "Mix 2500 Potions", 2500, 3, new GameItem(995, 1000000), new GameItem(11731, 3)),
        EXPERT_CHOPPER(10, AchievementTier.TIER_3, AchievementType.WOODCUT, null, "Cut 5000 Trees", 5000, 3, new GameItem(995, 2000000), new GameItem(10940, 1)),
        EXPERT_FLETCHER(11, AchievementTier.TIER_3, AchievementType.FLETCH, null, "Fletch 5000 Logs", 5000, 3, new GameItem(995, 1000000), new GameItem(71, 750),  new GameItem(1778, 200)), 
        EXPERT_PYRO(12, AchievementTier.TIER_3, AchievementType.FIRE, null, "Light 2500 Logs", 2500, 3, new GameItem(995, 1000000), new GameItem(20708), new GameItem(20712)),
        EXPERT_THIEF(13, AchievementTier.TIER_3, AchievementType.THIEV, null, "Steal 3000 Times", 3000, 3, new GameItem(995, 1000000), new GameItem(5554)),
        EXPERT_RUNNER(14, AchievementTier.TIER_3, AchievementType.ROOFTOP, null, "Complete 300 Rooftop Course Laps", 300, 3, new GameItem(995, 2000000), new GameItem(989, 3)),
        SLAYER_EXPERT(15, AchievementTier.TIER_3, AchievementType.SLAY, null, "Complete 150 Slayer Tasks", 150, 3, new GameItem(995, 5000000), new GameItem(405, 1)),
        DIG_FOR_GOLD(16, AchievementTier.TIER_3, AchievementType.LOOT_CRYSTAL_CHEST, null, "Loot Crystal Chest 200 Times", 200, 3, new GameItem(995, 3500000), new GameItem(990, 14)),
        KNIGHT(17, AchievementTier.TIER_3, AchievementType.PEST_CONTROL_ROUNDS, null, "Complete Pest Control 200 Times", 200, 3, new GameItem(995, 3000000), new GameItem(405, 2)),
        TZHAAR(18, AchievementTier.TIER_3, AchievementType.FIGHT_CAVES_ROUNDS, null, "Complete Fightcaves 10 Times", 10, 3, new GameItem(995, 2500000), new GameItem(6570, 3), new GameItem(405)),
        BARROWS_GOD(19, AchievementTier.TIER_3, AchievementType.BARROWS_KILLS, null, "Kill 750 npcs at barrows", 750, 3, new GameItem(995, 10000000), new GameItem(405, 3)),
        DAGGANOTH_MOTHER_III(20, AchievementTier.TIER_3, AchievementType.SLAY_DAGGANOTH_MOTHER, null, "Get 100 kills at dagganoth mother mini-game", 100, 3, new GameItem(11230, 300), new GameItem(11212, 300), new GameItem(565, 300)),
        CLUE_CHAMP(21, AchievementTier.TIER_3, AchievementType.CLUES, null, "Loot 180 Clue Caskets", 180, 3, new GameItem(995, 2000000), new GameItem(20164), new GameItem(6666)),
        EXTREME_VOTER(23, AchievementTier.TIER_3, AchievementType.VOTE_CHEST_UNLOCK, null, "Open 10 Vote Chests", 10, 3, new GameItem(995, 10000000), new GameItem(11739, 2), new GameItem(6199, 2));
        
        private AchievementTier tier;
        private AchievementRequirement requirement;
        private AchievementType type;
        private String description;
        private int amount, identification, points;
        private final GameItem[] rewards;

        Achievement(int identification, AchievementTier tier, AchievementType type, AchievementRequirement requirement, String description, int amount, int points, GameItem... rewards) {
            this.identification = identification;
            this.tier = tier;
            this.type = type;
            this.requirement = requirement;
            this.description = description;
            this.amount = amount;
            this.points = points;
            this.rewards = rewards;

            //format the items
            for (GameItem b : rewards) if (b.getAmount() == 0) b.setAmount(1);

        }

        public int getId() {
            return identification;
        }

        public AchievementTier getTier() {
            return tier;
        }

        public AchievementType getType() {
            return type;
        }

        public AchievementRequirement getRequirement() {
            return requirement;
        }

        public String getDescription() {
            return description;
        }

        public int getAmount() {
            return amount;
        }

        public int getPoints() {
            return points;
        }

        public GameItem[] getRewards() {
            return rewards;
        }

        public static final Set<Achievement> ACHIEVEMENTS = EnumSet.allOf(Achievement.class);

        public static Achievement getAchievement(AchievementTier tier, int ordinal) {
            for (Achievement achievement : ACHIEVEMENTS)
                if (achievement.getTier() == tier && achievement.ordinal() == ordinal)
                    return achievement;
            return null;
        }

        public static boolean hasRequirement(Player player, AchievementTier tier, int ordinal) {
            for (Achievement achievement : ACHIEVEMENTS) {
                if (achievement.getTier() == tier && achievement.ordinal() == ordinal) {
                    if (achievement.getRequirement() == null)
                        return true;
                    if (achievement.getRequirement().isAble(player))
                        return true;
                }
            }
            return false;
        }
    }

    public static void increase(Player player, AchievementType type, int amount) {
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (achievement.getType() == type) {
                if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
                    int currentAmount = player.getAchievements().getAmountRemaining(achievement.getTier().ordinal(), achievement.getId());
                    int tier = achievement.getTier().ordinal();
                    if (currentAmount < achievement.getAmount() && !player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
                        player.getAchievements().setAmountRemaining(tier, achievement.getId(), currentAmount + amount);
                        if ((currentAmount + amount) >= achievement.getAmount()) {
                            String name = achievement.name().replaceAll("_", " ");
                            player.getAchievements().setComplete(tier, achievement.getId(), true);
                            player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
                            player.sendMessage("@red@Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).", 255);
                            if (achievement.getTier().ordinal() > 0) {
                                for (Player p : PlayerHandler.players) {
                                    if (p == null)
                                        continue;
                                    Player c = p;
                                    //c.sendMessage("@red@" + Misc.capitalizeJustFirst(player.playerName) + " has completed the achievement: " + name + " on tier " + (tier + 1) + ".");
                                }
                            }
                            //add reward inventory if spots free
                            if (achievement.getRewards() != null && player.getItems().freeSlots() > 3) {
                                for (GameItem b : achievement.getRewards())  
                                    player.getItems().addItem(b.getId(), b.getAmount());
                                    player.sendMessage("@red@Your achievement reward(s) has been added to your inventory."); 
                            }else {
                                for (GameItem b : achievement.getRewards())  
                                player.getItems().addItemToBank(b.getId(), b.getAmount());
                                player.sendMessage("@red@Your achievement reward(s) has been added to your bank.");   
                }
              }
            }
         }
      }
   }
 }
    public static void reset(Player player, AchievementType type) {
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (achievement.getType() == type) {
                if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
                    if (!player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
                        player.getAchievements().setAmountRemaining(achievement.getTier().ordinal(), achievement.getId(),
                                0);
                    }
                }
            }
        }
    }
    /**
     * @author - Grant_ | www.rune-server.ee/members/grant_ | 2/20/20
     * @param tier
     * @return - The number of achievements completed in the specific tier
     */

    public static void complete(Player player, AchievementType type) {
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (achievement.getType() == type) {
                if (achievement.getRequirement() != null && achievement.getRequirement().isAble(player)
                        && !player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
                    int tier = achievement.getTier().ordinal();
                    //String name = achievement.name().replaceAll("_", " ");
                    player.getAchievements().setAmountRemaining(tier, achievement.getId(), achievement.getAmount());
                    player.getAchievements().setComplete(tier, achievement.getId(), true);
                    player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
                    player.sendMessage("Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).", 255);
                }
            }
        }
    }

    public static void checkIfFinished(Player player) {
        //complete(player, AchievementType.LEARNING_THE_ROPES);
    }

    public static int getMaximumAchievements() {
        return Achievement.ACHIEVEMENTS.size();
    }
}
