package ethos.model.content;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import ethos.Server;
import ethos.model.Area;
import ethos.model.SquareArea;
import ethos.model.content.CheatEngine.CheatEngineBlock;
import ethos.model.content.loot.LootableInterface;
import ethos.model.content.preset.PresetManager;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.npcs.drops.DropManager;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import org.apache.commons.lang3.text.WordUtils;

public class QuestTab {

    public enum Tab {
        INFORMATION(50_417),
        COIN(50_419),
        DIARY(50_421),
        DONATOR(50_423)
        ;

        private final int buttonId;

        Tab(int buttonId) {
            this.buttonId = buttonId;
        }

        public int getConfigValue() {
            return ordinal();
        }
    }

    public enum CoinTab {
        COLLECTION_LOG,
        DROP_TABLE,
        LOOT_TABLES,
        PRESETS,
        DONATOR_BENEFITS,
        ACHIEVEMENTS,
        TITLES,
        COMMUNITY_GUIDES,
        VOTE_PAGE,
        ONLINE_STORE,
        FORUMS,
        RULES,
        CALL_FOR_HELP
    }

    private static final int[] COIN_TAB_BUTTONS = {74107, 74112, 74117, 74122, 74127, 74132, 74137, 74142, 74147, 74152, 74157, 74162};

    public static final int INTERFACE_ID = 50414;
    private static final int CONFIG_ID = 1355;

    public static void updateAllQuestTabs() {
        Arrays.stream(PlayerHandler.players).forEach(player -> {
            if (player != null) {
                player.getQuestTab().updateInformationTab();
            }
        });
    }

    private final Player player;

    public QuestTab(Player player) {
        this.player = player;
    }

    public void openTab(Tab tab) {
        player.getPA().sendConfig(CONFIG_ID, tab.getConfigValue());
    }

    public boolean handleActionButton(int buttonId) {
        for (Tab tab : Tab.values()) {
            if (buttonId == tab.buttonId) {
                openTab(tab);
                return true;
            }
        }

        return false;
    }

    /**
     * Testiong View
     */

    public void updateInformationTab() {

        // Server Information
        boolean bonusWeekend = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
        int id = 10407;
        player.getPA().sendFrame126("@or1@- Players online : @gre@"+ PlayerHandler.getPlayers().size(), id++);
        player.getPA().sendFrame126("@or1@- Wilderness count : @gre@"+(Boundary.entitiesInArea(Boundary.WILDERNESS) + Boundary.entitiesInArea(Boundary.WILDERNESS_UNDERGROUND)),id++);
        player.getPA().sendFrame126("@or1@- " + TourneyManager.getSingleton().getTimeLeft(), id++);
        player.getPA().sendFrame126("@or1@- " + MonsterHunt.getTimeLeft(), id++);
        player.getPA().sendFrame126("@or1@- Bonus Weekend: " + (bonusWeekend ? "@gre@On" : "@red@Off"),  id++);

        // Player Information
        id = 10225;

        long milliseconds = (long) player.playTime * 600;
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
        String time = days + " days, " + hours + " hrs";
        player.getPA().sendFrame126("@or1@- Time Played : @gre@"+time,id++);

        player.getPA().sendFrame126("@or1@- KDR : @gre@"+ (double)(player.deathcount == 0 ? player.killcount + player.deathcount : player.killcount/player.deathcount),id++);

        player.getPA().sendFrame126("@or1@- PK Points : @gre@" +player.pkp,id++);
        player.getPA().sendFrame126("@or1@- Pvm points : @gre@" +player.pvmp, id++);
        player.getPA().sendFrame126("@or1@- Slayer Points : @gre@" +player.getSlayer().getPoints(),id++);
        if (!player.getSlayer().getTask().isPresent()) {
            player.getPA().sendFrame126("@or1@- Slayer Task : @gre@ None",id++);
        }else{
            player.getPA().sendFrame126("@or1@- Slayer Task : @gre@" +player.getSlayer().getTaskAmount()+" "+player.getSlayer().getTask().get().getPrimaryName()+"s",id++);
        }
        player.getPA().sendFrame126("@or1@- Vote points : @gre@" +player.votePoints, id++);
        player.getPA().sendFrame126("@or1@- Vote key : @yel@" +player.dayv/3 + "@gre@/30", id++);
        player.getPA().sendFrame126("@or1@- PC points : @gre@" +player.pcPoints,id++);
        player.getPA().sendFrame126("@or1@- Exchange Points : @gre@" +player.exchangeP, id++);

        // Unknown
        player.getPA().sendFrame126("@whi@@cr11@View the forums",47514);
        player.getPA().sendFrame126("@whi@@cr11@View vote page",47515);
        player.getPA().sendFrame126("@whi@@cr11@View online store",47516);
        player.getPA().sendFrame126("@whi@@cr11@View the rules",47517);
        player.getPA().sendFrame126("@whi@@cr11@View community guides ",47518);

        // Donator tab
        String icon = "";
        if (player.getRights().contains(Right.PLAYER) && player.amDonated <= 0) {
            icon = "";
        } else if (player.getRights().contains(Right.OSRS) && player.amDonated <= 0) {
            icon = "@cr22@";
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

        id = 50616;
        player.getPA().sendFrame126("@or1@- Rank : @gre@"+icon+" "+ player.getRights().getPrimary().toString(), id++);
        player.getPA().sendFrame126("@or1@- Donator points: @gre@" + player.donatorPoints, id++);
        player.getPA().sendFrame126("@or1@- Total donated: @gre@$" + player.amDonated, id++);
        player.getPA().sendFrame126("@or1@- Drop rate bonus: @gre@" + DropManager.getModifier1(player), id++);
    }

    /**
     * Handles all actions within the help tab
     */
    public boolean handleHelpTabActionButton(int button) {
        for (int index = 0; index < COIN_TAB_BUTTONS.length; index++) {
            if (button == COIN_TAB_BUTTONS[index]) {
                CoinTab coinTab = CoinTab.values()[index];
                switch(coinTab) {
                    case COLLECTION_LOG:
                        player.getCollectionLog().openInterface();
                        return true;
                    case DROP_TABLE:
                        Server.getDropManager().openDefault(player);
                        return true;
                    case PRESETS:
                        Area[] areas = {
                            new SquareArea(3066, 3521, 3135, 3456),
                        };
                        if (Arrays.stream(areas).anyMatch(area -> area.inside(player))) {
                            PresetManager.getSingleton().open(player);
                			player.inPresets = true;
                        } else {
                            player.sendMessage("You must be in Edgeville to open presets.");
                        }
                        return true;
                    case DONATOR_BENEFITS:
                    	 player.getPA().sendFrame126("http://www.wisdomrsps.com/forums/index.php?/topic/13-donator-benefits/", 12000);
                        return true;
                    case ACHIEVEMENTS:
                        player.getAchievements().drawInterface(0);
                        return true;
                    case TITLES:
                        player.getTitles().display();
                        return true;
                    case COMMUNITY_GUIDES:
                        player.getPA().sendFrame126("http://www.wisdomrsps.com/forums/index.php?/forum/15-wisdom-guides/", 12000);
                        return true;
                    case VOTE_PAGE:
                        player.getPA().sendFrame126("http://www.wisdomosrs.com/vote", 12000);
                        return true;
                    case ONLINE_STORE:
                        player.getPA().sendFrame126("http://www.wisdomosrs.com/store", 12000);
                        return true;
                    case FORUMS:
                        player.getPA().sendFrame126("http://www.wisdomosrs.com/", 12000);
                        return true;
                    case RULES:
                        player.getPA().sendFrame126("http://www.wisdomrsps.com/forums/index.php?/topic/9-in-game-rules/", 12000);
                        return true;
                    case LOOT_TABLES:
                        LootableInterface.openInterface(player);
                        return true;
                    case CALL_FOR_HELP:
                        List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> p.getRights().isOrInherits(Right.HELPER))
                                .collect(Collectors.toList());
                        player.sendMessage("@red@You can also type ::help to report something.");
                        if (staff.size() > 0) {
                            PlayerHandler.sendMessage("@blu@[Help] " + WordUtils.capitalize(player.playerName)
                                    + " needs help, PM or TELEPORT and help them.", staff);
                            player.getPA().logStuck();
                        } else {
                            player.sendMessage("@red@You've activated the help command but there are no staff-members online.");
                            player.sendMessage("@red@Please try contacting a staff on the forums and discord and they will respond ASAP.");
                            player.sendMessage("@red@You can also type ::help to report something.");
                        }
                        return true;
                    default:
                        return false;
                }
            }
        }

        return false;
    }
}
