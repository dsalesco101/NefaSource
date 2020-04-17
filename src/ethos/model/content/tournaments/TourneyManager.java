package ethos.model.content.tournaments;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import ethos.Server;
import ethos.ServerState;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.QuestTab;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.entity.Entity;
import ethos.model.items.GameItem;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;
import lombok.extern.java.Log;

/**
 *
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/3/19
 * A tournament system that allows players to join a lobby and fight until there is only one player left.
 * Top 3 are rewarded.
 */
@Log
public class TourneyManager {
    private static volatile TourneyManager singleton;
    public static String WINNER = "";
    private static final Object ARENA_TASK_OBJECT = new Object();
    private static final int FOG_TIMER = 1;
    private static final int LOBBY_X = 3323;
    private static final int LOBBY_Y = 4949;
    private static final int ARENA_X = 3290;
    private static final int ARENA_Y = 4958;
    private static final int LOBBY_TICK_INTERVAL = 12;

    private static int _minuteBetweenTournaments;
    private static int _secondsBetweenTournaments;
    private static int _playersToStart;

    public static void initialiseSingleton() {
        if (Server.getConfiguration().getServerState() != ServerState.DEBUG) {
            _minuteBetweenTournaments = 60;
            _secondsBetweenTournaments = 5 * 60;
            _playersToStart = 4;
        } else {
            _minuteBetweenTournaments = 3;
            _secondsBetweenTournaments = 60;
            _playersToStart = 1;
        }
        singleton = new TourneyManager();
    }

    public static TourneyManager getSingleton() {
        return singleton;
    }

    private TourneyManager() {
        if (singleton != null) {
            throw new RuntimeException("Use getSingleton() method to get the single instance of this class.");
        }
    }

    private ArrayList<String> currentPlayers = new ArrayList<>();
    private ArrayList<String> victors = new ArrayList<>();
    private final String[] tourneyOrder = {"dharok", "melee", "monk", "dharok", "melee", "monk", "dharok", "nh"};
    private final Object timerObject = new Object();
    private final int[] itemsToRemove = {157, 159, 161, 145, 147, 149, 6687, 6689, 6691, 3026, 3028,
            229, 3030, 3024, 12695, 12697, 12699, 12701, 3024, 6685, 229, 229, 229, 229, 385};
    private final int[] tournamentLevels = new int[] {99, 99, 99, 99, 99, 99, 99};

    private boolean lobbyOpen = false;
    private boolean arenaActive = false;
    private long fogStartedAt = 0;
    private int amountOfPlayers = 0;
    private int tournamentIndex = 0;
    private int minutesUntilAutomatedEvent = _minuteBetweenTournaments;
    private int secondsUntilLobbyEnds = _secondsBetweenTournaments;

    private HashMap<String, TourneySetup> tourneySetups;
    private TourneySetup currentSetup;

    public void init() {
        try {
            Path path = Paths.get("./data/json/default_tourney.json");
            File file = path.toFile();

            JsonParser parser = new JsonParser();
            if (!file.exists()) {
                return;
            }
            Object obj = parser.parse(new FileReader(file));
            JsonObject jsonUpdates = (JsonObject) obj;

            Type listType = new TypeToken<HashMap<String, TourneySetup>>() {
            }.getType();

            tourneySetups = new Gson().fromJson(jsonUpdates, listType);
            System.out.println("Loaded " + tourneySetups.size() + " default tourney setups.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No default presets found!");
            tourneySetups = new HashMap<>();
        }

        initializeAutomaticTournaments();
    }

    /**
     * The server automatically creates tournaments every hour starting at server startup + 1 cycle
     */
    public void initializeAutomaticTournaments() {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!isArenaActive()) {
                    if (--minutesUntilAutomatedEvent <= 0) {
                        // Start the tournament
                        minutesUntilAutomatedEvent = _minuteBetweenTournaments;

                        // Start the lobby
                        initializeLobbyTimer();

                        // Decide next tournament
                        tournamentIndex = (tournamentIndex + 1) % tourneyOrder.length;
                        currentSetup = tourneySetups.get(tourneyOrder[tournamentIndex]);
                        announceNextTournament(tourneyOrder[tournamentIndex]);
                        log.info("Starting automated " + tourneyOrder[tournamentIndex].toLowerCase() + " tournament.");
                    }

                    QuestTab.updateAllQuestTabs();
                } else {
                    // Reset the current execution cycle to delay until the arena is not active
                    minutesUntilAutomatedEvent = _minuteBetweenTournaments;
                    if (Server.getConfiguration().getServerState() != ServerState.PUBLIC_PRIMARY) {
                        log.info("Tournament is running, setting minutes to maximum..");
                    }
                }
            }
        }, Misc.toCycles(1, TimeUnit.MINUTES));
    }

    /**
     * Create the tournament event to the global public
     */
    public void createEvent(Player player, String type) {
        if (!tourneySetups.containsKey(type)) {
            player.sendMessage("That is not a valid setup!");
        } else if (clearCurrentArena(player)) {
            initializeLobbyTimer();
            currentSetup = tourneySetups.get(type);
            announceNextTournament(type);
        }
    }

    private boolean clearCurrentArena(Player player) {
        if (isArenaActive()) {
            if (Boundary.entitiesInArea(Boundary.OUTLAST) == 0) {
                player.sendMessage("Killing arena, 0 entites found inside outlast but outlast @red@WAS@bla@ still active.");
                victors.clear();
                arenaActive = false;
                return true;
            } else {
                player.sendMessage("The arena is already active.");
                return false;
            }
        } else {
            return true;
        }
    }

    private void clearActiveLobby() {
        if (isLobbyOpen()) {
            lobbyOpen = false;
            ArrayList<Player> toLeave = new ArrayList<>();
            for (String name : currentPlayers) {
                Player p = PlayerHandler.getPlayer(name);
                if (p != null) {
                    toLeave.add(p);
                }
            }
            for (Player p : toLeave) {
                if (p != null) {
                    leaveLobby(p);
                }
            }
            toLeave.clear();
            currentPlayers.clear();
        }
    }

    /**
     * Ends the game.
     */
    private void endGame() {
        victors.clear();
        arenaActive = false;
        CycleEventHandler.getSingleton().stopEvents(ARENA_TASK_OBJECT);
        log.info("Tournament ended.");

        for (Iterator<String> it = currentPlayers.iterator(); it.hasNext();) {
            handleDeath(it.next());
        }
    }

    /**
     * Start the lobby timer that waits for players to join
     */
    private void initializeLobbyTimer() {
        // Initalize the variables to control the lobby
        clearActiveLobby();
        CycleEventHandler.getSingleton().stopEvents(timerObject);
        secondsUntilLobbyEnds = _secondsBetweenTournaments;
        lobbyOpen = true;
        QuestTab.updateAllQuestTabs();

        // Start lobby timer
        CycleEventHandler.getSingleton().addEvent(timerObject, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (currentPlayers.size() < _playersToStart) {
                    secondsUntilLobbyEnds = _secondsBetweenTournaments;
                } else if ((secondsUntilLobbyEnds -= LOBBY_TICK_INTERVAL) <= 0) {
                    beginTournament();
                    container.stop();
                }
                updateInterface();
                QuestTab.updateAllQuestTabs();
            }
        }, Misc.toCycles(LOBBY_TICK_INTERVAL, TimeUnit.SECONDS));
    }

    /**
     * Begins the tournament, granted there are more than 1 person in the lobby
     *
     * Stats are set here not in the lobby
     */
    private void beginTournament() {
        amountOfPlayers = currentPlayers.size();
        ArrayList<String> toRemove = new ArrayList<>();
        for(String p : currentPlayers) {
            Player player = PlayerHandler.getPlayer(p);
            if (player == null) {
                toRemove.add(p);
                continue;
            }

            if (!isInLobbyBounds(player)) {
                player.sendMessage("You will no longer compete in the tournament, as you were");
                player.sendMessage("not in the lobby when it began.");
                toRemove.add(p);
                continue;
            }

            player.sendMessage("Welcome to the Tournament. Goodluck!");
            player.getPA().movePlayer(new Coordinate(ARENA_X + Misc.random(6), ARENA_Y + Misc.random(6)));
        }

        for(String r : toRemove) {
            currentPlayers.remove(r);
        }

        if (currentPlayers.size() == 1 && Server.getConfiguration().getServerState() != ServerState.DEBUG) {
            handleDeath(currentPlayers.get(0));
            return;
        } else if (currentPlayers.size() == 0) {
            handleVictors();
            return;
        }

        arenaActive = true;
        lobbyOpen = false;
        QuestTab.updateAllQuestTabs();
        initializeFogTimer();

        // End arena tasks
        CycleEventHandler.getSingleton().stopEvents(ARENA_TASK_OBJECT);

        CycleEventHandler.getSingleton().addEvent(ARENA_TASK_OBJECT, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                if (currentPlayers.isEmpty() || Boundary.entitiesInArea(Boundary.OUTLAST) == 0) {
                    endGame();
                    container.stop();
                }
            }
        }, 10);

        CycleEventHandler.getSingleton().addEvent(ARENA_TASK_OBJECT, new CycleEvent() {
            int tick = 0;
            @Override
            public void execute(CycleEventContainer container) {
                tick++;
                for(String name : currentPlayers) {
                    Player player = PlayerHandler.getPlayer(name);

                    if (player != null) {
                        if (tick == 10) {
                            player.sendMessage("You can now fight!");
                            Achievements.increase(player, AchievementType.OTHER, 1);
                            player.canAttack = true;
                        } else {
                            player.forcedChat(10 - tick + " seconds until you can fight!");
                        }
                    }
                }

                updateInterface();
                if (tick == 10) {
                    container.stop();
                    return;
                }
            }

        }, Misc.toCycles(1, TimeUnit.SECONDS));
    }

    /**
     * Initializes the fog timer that will then trigger the fog to attack
     */
    private void initializeFogTimer() {
        fogStartedAt = System.currentTimeMillis();
        CycleEventHandler.getSingleton().addEvent(ARENA_TASK_OBJECT, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                updateInterface();
            }
        }, Misc.toCycles(1, TimeUnit.MINUTES));

        CycleEventHandler.getSingleton().addEvent(ARENA_TASK_OBJECT, new CycleEvent() {
            int tick = 0;
            @Override
            public void execute(CycleEventContainer container) {
                tick++;
                if (!isArenaActive()) {
                    container.stop();
                    return;
                }

                updateInterface();
                if (tick == 8) {
                    currentPlayers.forEach(p -> {
                        Player player = PlayerHandler.getPlayer(p);
                        if (player != null) {
                            player.sendMessage("@red@[WARNING] The fog will begin to damage you in 60 seconds!");
                        }
                    });
                }
                if (tick == 9) {
                    fogStartedAt = 0;
                    beginFogDamage();
                    container.stop();
                }
            }

        }, Misc.toCycles(FOG_TIMER, TimeUnit.MINUTES));
    }

    /**
     * Begins hitting players for fog damage
     */
    private void beginFogDamage() {
        CycleEventHandler.getSingleton().addEvent(ARENA_TASK_OBJECT, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (!isArenaActive()) {
                    container.stop();
                    return;
                }
                currentPlayers.forEach(p -> {
                    Player player = PlayerHandler.getPlayer(p);
                    if (player == null) {
                        handleDeath(p);
                        return;
                    }

                    if (!player.isDead) {
                        player.appendDamage(Misc.random(1) + 3, Hitmark.HIT);
                    }
                });
            }
        }, Misc.toCycles(2, TimeUnit.SECONDS));
    }

    private void announceNextTournament(String type) {
        Arrays.stream(PlayerHandler.players).forEach(p -> {
            if (p != null) {
                p.getPA().sendBroadCast("An Outlast " + type + " tournament will begin soon! Type ::outlast to join.");
            }
        });
    }

    /**
     * Announces a message across the whole server
     * - Honestly this should be in a more global setting, but it will stay here for now
     * @param message
     */
    private void announce(String message) {
        PlayerHandler.getPlayers().forEach(p -> {
            if (p != null) {
                p.sendMessage("<col=255>" + message);
            }
        });
    }

    /**
     * Allow a player to join the event
     * @param player
     */
    public void join(Player player) {
        if (player == null) {
            return;
        }

        if (isPlayerInTournament(player)) {
            player.sendMessage("You are already a part of the tournament");
            return;
        }

        if (isArenaActive()) {
            player.sendMessage("You are unable to join an active tournament.");
            return;
        }

        if (!isLobbyOpen()) {
            player.sendMessage("The tournament lobby is not currently open.");
            return;
        }

        if (player.getItems().freeSlots() != 28) {
            player.sendMessage("Please empty your inventory before doing this.");
            return;
        }

        if (player.getItems().freeEquipmentSlots() != 14) {
            player.sendMessage("Please take off all equipment before doing this.");
            return;
        }

        if (checkMacAddress(player)) {
            player.sendMessage("You can only play with one account per computer.");
            return;
        }

        currentPlayers.add(player.playerName);
        player.tourneyItemsReceived.clear();
        player.getPA().movePlayer(new Coordinate(LOBBY_X, LOBBY_Y));
        player.sendMessage("You have joined the Tournament lobby.");
        player.sendMessage("Remain in this lobby until the Tournament begins.");
        TourneyManager.getSingleton().outlastEquip(player);
        updateInterface();
    }

    /**
     * Checks a players mac address against all in the current lobby
     * @param player
     * @return
     */
    public boolean checkMacAddress(Player player) {
        for(String playerName : currentPlayers) {
            Player p = PlayerHandler.getPlayer(playerName);
            if (p != null) {
                if (p.getMacAddress().equalsIgnoreCase(player.getMacAddress())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Updates the interface for all active players
     */
    private void updateInterface() {
        currentPlayers.forEach(p -> {
            Player player = PlayerHandler.getPlayer(p);
            if (player != null) {
                player.getPA().sendFrame126(isLobbyOpen() ? "Time Until Tournament: @or1@" + getLobbyTime() : "Time Until Fog: @or1@" + getFogTime(), 266);
                player.getPA().sendFrame126(isLobbyOpen() ? "Current Players In Lobby: @or1@" + currentPlayers.size() : "Players Left In Arena: @or1@" + currentPlayers.size(), 267);
            }
        });
    }

    private void outlastEquip (Player player) {
        player.canAttack = false;
        for(int i = 0; i < 7; i++) {
            player.combatLevelBackUp[i] = player.playerXP[i];
            player.playerXP[i] = player.getPA().getXPForLevel(tournamentLevels[i] + 1);
            player.playerLevel[i] = tournamentLevels[i];
            player.getPA().refreshSkill(i);
            player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
        }

        player.getHealth().setMaximumHealth(tournamentLevels[3]);
        player.getHealth().increase(tournamentLevels[3]);
        player.specAmount = 10.0;
        player.magicBookBackup = player.playerMagicBook;

        switch(currentSetup.getMagicBook()) {
            case "NORMAL":
                player.setSidebarInterface(6, 938);
                player.sendMessage("You feel a drain on your memory.");
                player.playerMagicBook = 0;
                break;
            case "ANCIENT":
                player.setSidebarInterface(6, 838);
                player.sendMessage("An ancient wisdomin fills your mind.");
                player.playerMagicBook = 1;
                break;
            case "LUNAR":
                player.sendMessage("You switch to the lunar spellbook.");
                player.setSidebarInterface(6, 29999);
                player.playerMagicBook = 2;
                break;
        }

        for(int i = 0; i < currentSetup.getTournamentInventory().length; i++) {
            int inventoryItem = currentSetup.getTournamentInventory()[i].getItemID();
            int amount = currentSetup.getTournamentInventory()[i].getItemAmount();
            if (inventoryItem != -1) {
                player.getItems().addItem(inventoryItem, amount);
            }
        }

        //hat, cape, amulet, weapon, chest, shield, EMPTY, legs, EMPTY, hands, feet, EMPTY, ring, arrows
        for(int i = 0; i < currentSetup.getTournamentEquipment().length; i++) {
            if (currentSetup.getTournamentEquipment()[i] != -1) {
                if (i == 13) {
                    player.getItems().setEquipment(currentSetup.getTournamentEquipment()[i], currentSetup.getAmmoAmount(), i);
                } else {
                    player.getItems().setEquipment(currentSetup.getTournamentEquipment()[i], 1, i);
                }

                if (i == 3) {
                    player.spellId = 0;
                    player.usingMagic = false;
                    player.autocasting = false;
                    player.autocastId = 0;
                    player.getPA().sendFrame36(108, 0);
                    player.usingSpecial = false;
                    player.getItems().addSpecialBar(currentSetup.getTournamentEquipment()[i]);
                    player.getItems().updateSpecialBar();
                    player.getPA().resetAutocast();
                    if (currentSetup.getTournamentEquipment()[i] != 4153 && currentSetup.getTournamentEquipment()[i] != 12848) {
                        player.getCombat().resetPlayerAttack();
                    }
                }
            }
        }

        player.getItems().sendWeapon(player.playerEquipment[player.playerWeapon]);
    }

    /**
     * Allows a player to leave the lobby
     * @param player
     */
    public void leaveLobby(Player player) {
        player.getItems().deleteAllItems();
        player.getItems().deleteEquipment();
        currentPlayers.remove(player.playerName);
        player.sendMessage("You have left the tournament lobby.");
        updateInterface();
        for(int i = 0; i < 7; i++) {
            player.playerXP[i] = player.combatLevelBackUp[i];
            player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
            player.getPA().refreshSkill(i);
            player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
        }
        for (int i = 0; i < 20; i++) {
            player.getPA().refreshSkill(i);
        }
        player.getHealth().setMaximumHealth(player.playerLevel[3]);
        player.getHealth().increase(player.playerLevel[3]);
        player.getPlayerAction().canWalk = true;
        player.playerWalkIndex = 0x333;
        player.playerStandIndex = 0x328;
        if (player.magicBookBackup == 0) {
            player.setSidebarInterface(6, 938);
            player.playerMagicBook = 0;
            player.sendMessage("You feel a drain on your memory.");
        } else if (player.magicBookBackup == 1) {
            player.playerMagicBook = 1;
            player.setSidebarInterface(6, 838);
            player.sendMessage("An ancient wisdomin fills your mind.");
        } else if (player.magicBookBackup == 2) {
            player.sendMessage("You switch to the lunar spellbook.");
            player.setSidebarInterface(6, 29999);
            player.playerMagicBook = 2;
        }

        player.getPA().movePlayer(new Coordinate(3108, 3498));
        PlayerSave.save(player);
    }

    /**
     * Handles a player that logs in within the lobby bounds
     * @param player
     */
    public void handleLoginWithinLobby(Player player) {
        TourneyManager.getSingleton().leaveLobby(player);
    }

    /**
     * Handles a player that logs in within the arena bounds
     * @param player
     */
    public void handleLoginWithinArena(Player player) {
        player.getItems().deleteAllItems();
        player.getItems().deleteEquipment();
        player.getCombat().resetPrayers();

        for(int i = 0; i < 7; i++) {
            player.playerXP[i] = player.combatLevelBackUp[i];
            player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
            player.getPA().refreshSkill(i);
            player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
        }

        for (int i = 0; i < 20; i++) {
            player.getPA().refreshSkill(i);
        }

        player.getHealth().setMaximumHealth(player.playerLevel[3]);
        player.getHealth().increase(player.playerLevel[3]);
        player.getPlayerAction().canWalk = true;
        player.playerWalkIndex = 0x333;
        player.playerStandIndex = 0x328;
        player.getPA().movePlayer(new Coordinate(3108, 3498));
        PlayerSave.save(player);
    }

    /**
     * Handles the killer of a player getting rewarded
     * @param player
     */
    public void handleKill(Entity player) {
        if (player == null) {
            return;
        }

        if (currentPlayers.size() == 0 || !isArenaActive()) {
            return;
        }

        if (player instanceof Player) {
            Player killer = (Player) player;
            if (!isInArena(killer)) { return; }

            GameItem rewardItem = new GameItem(0, 1);

            int count = 0;
            boolean contains = false;
            while(!contains) {
                int category = Misc.random(999);
                if (category <= 150) { //Very Rare
                    rewardItem = new GameItem(currentSetup.getVeryRareItems()[Misc.random(currentSetup.getVeryRareItems().length - 1)], 1);
                } else if (category <= 500) {//Common
                    rewardItem = new GameItem(currentSetup.getCommonItems()[Misc.random(currentSetup.getCommonItems().length - 1)], 1);
                } else if (category <= 800) {//Uncommon
                    rewardItem = new GameItem(currentSetup.getUncommonItems()[Misc.random(currentSetup.getUncommonItems().length - 1)], 1);
                } else if (category <= 999) {//Rare
                    rewardItem = new GameItem(currentSetup.getRareItems()[Misc.random(currentSetup.getRareItems().length - 1)], 1);
                }
                count++;

                if (!killer.tourneyItemsReceived.contains(rewardItem.getId())) {
                    contains = true;
                }

                if (count == (currentSetup.getVeryRareItems().length + currentSetup.getRareItems().length + currentSetup.getCommonItems().length + currentSetup.getUncommonItems().length)) {
                    killer.sendMessage("You've already received all possible items.");
                    break;
                }
            }

            killer.tourneyItemsReceived.add(rewardItem.getId());

            if (rewardItem.getId() != 0) {
                if (killer.getItems().freeSlots() >= rewardItem.getAmount()) {
                    killer.getItems().addItem(rewardItem.getId(), rewardItem.getAmount());
                } else {
                    killer.sendMessage("Your inventory was full and your rewarded items were dropped beneath you.");
                    Server.itemHandler.createGroundItem(killer, rewardItem.getId(), killer.absX, killer.absY, killer.getHeightLevel, rewardItem.getAmount(), killer.getIndex());
                }
            }

            for(int i = 0; i < itemsToRemove.length; i++) {
                if (killer.getItems().playerHasItem(itemsToRemove[i])) {
                    killer.getItems().deleteItem(itemsToRemove[i], 1);
                }
            }

            System.out.println("KILLER: " + killer.playerName);
            if (killer.getItems().freeSlots() > 0) {
                killer.getItems().addItem(12695, 1);
                killer.getItems().addItem(6685, 1);
                killer.getItems().addItem(3024, 2);
                killer.getItems().addItem(11936, 6);
                killer.getItems().addItem(3144, 4);
                killer.getItems().addItem(11936, killer.getItems().freeSlots());
            }
            killer.specRestore = 120;
            killer.specAmount = 10.0;
            killer.setRunEnergy(100);
            killer.getItems().addSpecialBar(killer.playerEquipment[killer.playerWeapon]);
            killer.playerLevel[5] = killer.getPA().getLevelForXP(killer.playerXP[5]);
            killer.getHealth().removeAllStatuses();
            killer.getHealth().reset();
            killer.getPA().refreshSkill(5);
        }
    }

    /**
     * Handles a player dying within the arena
     * @param playerName the player name
     */
    public void handleDeath(String playerName) {
        if (!currentPlayers.contains(playerName)) {
            return;
        }

        if (currentPlayers.size() <= 3) {
            victors.add(playerName);
        }

        currentPlayers.remove(playerName);

        updateInterface();

        Player player = PlayerHandler.getPlayer(playerName);

        if (player != null) {
            if (currentPlayers.size() > 0) {
                player.sendMessage("You have been defeated!");
            }
            player.getItems().deleteAllItems();
            player.getItems().deleteEquipment();
            player.getEventCalendar().progress(EventChallenge.PARTICIPATE_IN_X_OUTLAST_TOURNIES);
            player.tPoint+=1;
            player.sendMessage("@blu@You have gained an extra point for participating.");
            player.getCombat().resetPrayers();
            for(int i = 0; i < 7; i++) {
                player.playerXP[i] = player.combatLevelBackUp[i];
                player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
                player.getPA().refreshSkill(i);
                player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
            }
            for (int i = 0; i < 20; i++) {
                player.getPA().refreshSkill(i);
            }
            player.getHealth().setMaximumHealth(player.playerLevel[3]);
            player.getHealth().increase(player.playerLevel[3]);
            player.getPlayerAction().canWalk = true;
            player.playerWalkIndex = 0x333;
            player.playerStandIndex = 0x328;
            if (player.magicBookBackup == 0) {
                player.setSidebarInterface(6, 938);
                player.playerMagicBook = 0;
                player.sendMessage("You feel a drain on your memory.");
            } else if (player.magicBookBackup == 1) {
                player.playerMagicBook = 1;
                player.setSidebarInterface(6, 838);
                player.sendMessage("An ancient wisdomin fills your mind.");
            } else if (player.magicBookBackup == 2) {
                player.sendMessage("You switch to the lunar spellbook.");
                player.setSidebarInterface(6, 29999);
                player.playerMagicBook = 2;
            }

            player.getPA().movePlayer(new Coordinate(3108, 3498));
            PlayerSave.save(player);
        }

        if (currentPlayers.size() == 1) {
            handleDeath(currentPlayers.get(0));
        } else if (currentPlayers.size() == 0) {
            handleVictors();
        }
    }

    /**
     * Handle the victors that won the tournament
     */
    private void handleVictors() {
        int tier = amountOfPlayers /2;

        currentPlayers.clear();
        for(int i = victors.size() - 1; i >= 0; i--) {
            Player player = PlayerHandler.getPlayer(victors.get(i));
            if (player != null) {
                player.sendMessage("Congratulations you placed " + ((2 - i) + 1) + (i == 2 ? "st" : i == 1 ? "nd" : "rd") + " place in the Tournament!");
                if (player!=null) {
                    if (i == 2) {
                        player.getItems().addItemToBank(995, (1000000 * tier) + (6000000));//1st place gets 6m + 1m every 2 people
                    } else if (i != 2) {
                        player.getItems().addItemToBank(995, (500000 * tier) + (2000000));//2nd and 3rd get 2m + 500k ever 2 people
                    }
                    player.tPoint += 1; //winner of tournament gets  a tournament win

                    player.sendMessage("@blu@You gained an extra point for placing.");

                } else {

                }

                if (i == 2) {
                    player.streak += 1;
                    if (player.streak == 5) {
                        player.sendMessage("@red@You receive an extra 5m coins for having a 5 win streak.");
                        announce("@blu@" + victors.get(i) + " has just reached 5 wins in a row in Outlast!");
                        player.getItems().addItemToBank(995, (5000000));

                    } else if (player.streak == 10) {
                        player.sendMessage("@red@You receive an extra 10m coins for having a 10 win streak.");
                        announce("@blu@" + victors.get(i) + " has just reached 10 wins in a row in Outlast!");
                        player.getItems().addItemToBank(995, (10000000));
                    }

                    announce(Misc.optimizeText(victors.get(i)) + " has won the Tournament. Congratulations!");
                    announce(Misc.optimizeText(victors.get(i)) + " Current streak is " + player.streak + "!");
                    player.tWin += 1; //winner of tournament gets  a tournament win
                    player.tPoint += 3; //winner of tournament gets  a tournament win
                    player.getEventCalendar().progress(EventChallenge.WIN_AN_OUTLAST_TOURNAMENT);
                    player.sendMessage("@blu@You now have a total of @bla@" + player.tPoint + " tournament points.");
                    player.sendMessage("@blu@You have won a total of @bla@" + player.tWin + " @blu@tournament wins.");
                    player.sendMessage("@blu@You have gained an extra point for participating.");

                    player.sendMessage("You current winstreak is @red@" + player.streak + ".");
                    WINNER = "" + victors.get(i) + "";
                }

                if (i != 2) {
                    player.streak = 0;
                    player.sendMessage("@blu@You now have a total of @bla@" + player.tPoint + " tournament points.");
                    player.getEventCalendar().progress(EventChallenge.PARTICIPATE_IN_X_OUTLAST_TOURNIES);
                }
            }
        }
        victors.clear();
        lobbyOpen = false;
        arenaActive = false;
    }

    /**
     * Checks if a player is in the list of current players
     * @param player
     * @return
     */
    private boolean isPlayerInTournament(Player player) {
        return currentPlayers.contains(player.playerName);
    }

    /**
     * Checks if a player is standing in the lobby
     * @param player
     * @return
     */
    public boolean isInLobbyBounds(Player player) {
        return player.absX >= 3320 && player.absX <= 3326 && player.absY >= 4939 && player.absY <= 4959;
    }

    /**
     * Checks if a player is standing in the arena
     * @param player
     * @return
     */
    public boolean isInArenaBounds(Player player) {
        return player.absX >= 3266 && player.absX <= 3319 && player.absY >= 4930 && player.absY <= 4989;
    }

    /**
     * (THIS IS BECAUSE ASCEND DOESNT UNDERSTAND LOGIN LOCATION LOGIC...)
     * @param player
     * @return
     */
    public boolean isInLobbyBoundsOnLogin(Player player) {
        return player.tourneyX >= 3320 && player.tourneyX <= 3326 && player.tourneyY >= 4939 && player.tourneyY <= 4959;
    }

    /**
     * (THIS IS BECAUSE ASCEND DOESNT UNDERSTAND LOGIN LOCATION LOGIC...)
     * @param player
     * @return
     */
    public boolean isInArenaBoundsOnLogin(Player player) {
        return player.tourneyX >= 3266 && player.tourneyX <= 3319 && player.tourneyY >= 4930 && player.tourneyY <= 4989;
    }

    /**
     * Checks if a player is in the currently active and fighting arena
     * @param player
     * @return
     */
    public boolean isInArena(Player player) {
        if (!currentPlayers.contains(player.playerName)) {
            return false;
        }
        if (isArenaActive() && isInArenaBounds(player)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Handles force logging out either in the arena or lobby
     * @param player
     */
    public void handleXLog(Player player) {
        if (!currentPlayers.contains(player.playerName)) {
            return;
        }

        if (isArenaActive()) {
            handleDeath(player.playerName);
        } else {
            leaveLobby(player);
        }
    }

    /**
     * Get the time left until the fog begins
     * @return
     */
    private String getFogTime() {
        int minutes = (9) - (int) ((System.currentTimeMillis() - fogStartedAt) / (1000.0 * 60.0));
        int seconds = 59 - (int) (((System.currentTimeMillis() - fogStartedAt) / (1000.0)) - ((double) (minutes - 9) * 60.0)) % 60;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + " @whi@remaining.";
    }

    /**
     * Get the time left until the lobby closes and tournament starts
     * @return
     */
    private String getLobbyTime() {
        int minutes = secondsUntilLobbyEnds / 60;
        int seconds = secondsUntilLobbyEnds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Gets the number of players in the tournament
     * @return
     */
    public int getPlayers() {
        return currentPlayers.size();
    }

    /**
     * Handles all action buttons within the login interface
     * @param player
     * @param button
     * @return
     */
    public boolean handleActionButtons(Player player, int button) {
        switch(button) {
            case 1017:
                if (!Boundary.isIn(player, Boundary.OUTLAST_HUT)) {
                    return false;
                }
                if (player.getTutorial().isActive()) {
                    player.getTutorial().refresh();
                    return false;
                }
                player.getPA().closeAllWindows();
                join(player);
                return true;
            case 1020:
                player.getPA().closeAllWindows();
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets the current prayer option for the current setup
     * @return
     */
    public String getCurrentPrayerBlock() {
        if (currentSetup != null) {
            return currentSetup.getPrayerBlocked();
        }
        return "";
    }

    /**
     * Gets the time left to display on the quest tab
     * @return
     */
    public String getTimeLeft() {
        if (isArenaActive()) {
            return "Outlast: @gre@Active";
        } else  if (isLobbyOpen()) {
            return "Outlast: @whi@Starts in " + getLobbyTime();
        } else {
            return "Outlast: @red@" + minutesUntilAutomatedEvent + " minutes";
        }
    }

    public HashMap<String, TourneySetup> getSetups() {
        return tourneySetups;
    }

    public boolean isLobbyOpen() {
        return lobbyOpen;
    }

    public boolean isArenaActive() {
        return arenaActive;
    }
}
