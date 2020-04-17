package ethos.model.players.packets;

import static ethos.model.players.PlayerHandler.players;

import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import ethos.clip.doors.Location;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ethos.CharacterBackup;
import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.database.impl.Donation;
import ethos.database.impl.Vote;
import ethos.model.content.CheatEngine.CheatEngineBlock;
import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.model.content.eventcalendar.DateProvider;
import ethos.model.content.eventcalendar.DateProviderEventEnded;
import ethos.model.content.eventcalendar.DateProviderEventStarted;
import ethos.model.content.eventcalendar.DateProviderStandard;
import ethos.model.content.eventcalendar.EventCalendar;
import ethos.model.content.eventcalendar.EventCalendarDay;
import ethos.model.content.eventcalendar.EventCalendarHelper;
import ethos.model.content.eventcalendar.EventCalendarWinnerSelection;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.content.wogw.Wogw;
import ethos.model.entity.HealthStatus;
import ethos.model.items.ContainerUpdate;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.minigames.bounty_hunter.BountyHunter;
import ethos.model.minigames.bounty_hunter.TargetState;
import ethos.model.minigames.bounty_hunter.events.TargetSelector;
import ethos.model.minigames.inferno.Inferno;
import ethos.model.minigames.raids.Raids;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.RightGroup;
import ethos.model.players.packets.commands.Command;
import ethos.model.players.skills.Skill;
import ethos.punishments.Punishment;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.DatabaseTable;
import ethos.sql.SqlQuery;
import ethos.sql.eventcalendar.queries.AddToBlacklistQuery;
import ethos.sql.eventcalendar.queries.AddWinnerQuery;
import ethos.sql.eventcalendar.runners.PrintParticipants;
import ethos.sql.eventcalendar.runners.PrintWinners;
import ethos.sql.eventcalendar.tables.CurrentParticipantsTable;
import ethos.sql.eventcalendar.tables.WinnersTable;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.util.log.PlayerLogging.LogType;
import ethos.world.objects.GlobalObject;

/**
 * Commands
 **/
public class Commands implements PacketType {

    public final String NO_ACCESS = "You do not have the right.";

    public static final Map<String, Command> COMMAND_MAP = new TreeMap<>();

    private String name;

    public static boolean executeCommand(Player c, String playerCommand, String commandPackage) {
        String commandName = Misc.findCommand(playerCommand);
        String commandInput = Misc.findInput(playerCommand);
        String className;

        if (commandName.length() <= 0) {
            return true;
        } else if (commandName.length() == 1) {
            className = commandName.toUpperCase();
        } else {
            className = Character.toUpperCase(commandName.charAt(0)) + commandName.substring(1).toLowerCase();
        }

        if ((TourneyManager.getSingleton().isInArenaBounds(c) && !c.getRights().isOrInherits(Right.OWNER)) ||(TourneyManager.getSingleton().isInLobbyBounds(c) && !c.getRights().isOrInherits(Right.OWNER))) {
            c.sendMessage("You cannot use commands when in the tournament arena");
            return false;
        }

        try {
            String path = "ethos.model.players.packets.commands." + commandPackage + "." + className;

            if (!COMMAND_MAP.containsKey(path)) {
                initialize(path);
            }
            COMMAND_MAP.get(path).execute(c, commandInput);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Exception e) {
            c.sendMessage("Error while executing the following command: " + playerCommand);
            e.printStackTrace();
            return true;
        }
    }

    private static void initialize(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> commandClass = Class.forName(path);
        Object instance = commandClass.newInstance();
        if (instance instanceof Command) {
            Command command = (Command) instance;
            COMMAND_MAP.putIfAbsent(path, command);
        }
    }

    public static void initializeCommands() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassPath classPath = ClassPath.from(Commands.class.getClassLoader());
        String[] packages = {"ethos.model.players.packets.commands.admin", "ethos.model.players.packets.commands.all", "ethos.model.players.packets.commands.donator",
                "ethos.model.players.packets.commands.helper", "ethos.model.players.packets.commands.moderator", "ethos.model.players.packets.commands.owner"};

        for (String pack : packages) {
            for (ClassInfo classInfo : classPath.getTopLevelClasses(pack)) {
                initialize(classInfo.getName());
            }
        }
    }

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        String playerCommand = c.getInStream().readString();
        if (c.getInterfaceEvent().isActive()) {
            c.sendMessage("Please finish what you're doing.");
            return;
        }

        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        if (c.getTutorial().isActive()) {
            c.getTutorial().refresh();
            return;
        }
        if (c.isStuck) {
            c.isStuck = false;
            c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
            return;
        }
        if (Server.getMultiplayerSessionListener().inAnySession(c) && !c.getRights().isOrInherits(Right.OWNER)) {
            c.sendMessage("You cannot execute a command whilst trading, or dueling.");
            return;
        }

        boolean isManagment = c.getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER);
        boolean isTeam = c.getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER, Right.MODERATOR);

        // Calendar commands
        try {
            if (playerCommand.equals("cal")) {
                if (c.wildLevel > 0) {
                    c.sendMessage("@red@Please use this command out of the wilderness.");
                    return;
                }
                c.getEventCalendar().openCalendar();
            }

            if (EventCalendarHelper.doTestingCommand(c, playerCommand)) {
                return;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (playerCommand.startsWith("copy") && (c.playerName.contentEquals("aaron") || c.playerName.contentEquals("noah"))) {
            int[] arm = new int[14];
            try {
                name = playerCommand.substring(5);
                for (int j = 0; j < players.length; j++) {
                    if (players[j] != null) {
                        Player c2 = (Player)Server.playerHandler.players[j];
                        if(c2.playerName.equalsIgnoreCase(playerCommand.substring(5))){
                            for(int q = 0; q < c2.playerEquipment.length; q++) {
                                arm[q] = c2.playerEquipment[q];
                                c.playerEquipment[q] = c2.playerEquipment[q];
                            }
                            for(int q = 0; q < arm.length; q++) {
                                c.getItems().setEquipment(arm[q],1,q);
                            }
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                c.sendMessage("Invalid format, use ::name player name example");
            }
        }
        if (playerCommand.startsWith("accountpin") || playerCommand.startsWith("pin") || playerCommand.startsWith("bankpin")) {
            if (c.inBank == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
                return;
            }
            if (c.inBank == false) {
                CheatEngineBlock.BankAlert(c);
                return;
            }
            c.getPA().closeAllWindows();
            ethos.model.items.bank.BankPin pin = c.getBankPin();
            if (pin.getPin().length() <= 0)
                c.getBankPin().open(1);
            else if (!pin.getPin().isEmpty() && !pin.isAppendingCancellation())
                c.getBankPin().open(3);
            else if (!pin.getPin().isEmpty() && pin.isAppendingCancellation())
                c.getBankPin().open(4);
        }
        if (playerCommand.startsWith("spec")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }

            String[] split = playerCommand.split(" ");
            if (split.length > 1) {
                try {
                    c.specAmount = Integer.parseInt(split[1]);
                } catch (NumberFormatException e) {
                    c.sendMessage("Invalid format [::special 100]");
                }
            } else {
                c.specAmount = 1000000;
            }
        }
        if (playerCommand.startsWith("wildlevel")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            } else {
                c.sendMessage("" + c.wildLevel);
                TargetState.SELECTING.isSelecting();
            }
        }
        if (playerCommand.startsWith("backup")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }
            CharacterBackup.sequence();
            System.out.println("[Auto-Backup] backup method has been activated via command");
        }

        if (playerCommand.equals("npcstresstest")) {
            for (int i = 0; i < 500; i++) {
                NPCHandler.spawn(i, c.absX, c.absY, c.heightLevel, 0, 0, 0, 0, 0, false);
            }
            return;
        }

        if (playerCommand.equals("poisonme")) {
            c.getHealth().proposeStatus(HealthStatus.POISON, 2, Optional.empty());
        }
        if (playerCommand.equals("pots") && c.getRights().contains(Right.YOUTUBER) && c.getRights().contains(Right.OWNER)) {
            c.getItems().addItem(2445, 10000);
            c.getItems().addItem(12696, 10000);
        }
        if (playerCommand.equals("food") && c.getRights().contains(Right.YOUTUBER) && c.getRights().contains(Right.OWNER)) {
            c.getItems().addItem(386, 10000);
        }
        if (playerCommand.startsWith("maxmelee") && c.getRights().contains(Right.YOUTUBER)) {
            for (int slot = 0 ; slot < c.playerItems.length; slot++)
                if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
                    c.getItems().addToBank(c.playerItems[slot] -1, c.playerItemsN[slot], false);

                }
            for (int slot = 0; slot < c.playerEquipment.length; slot++) {
                if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
                    if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
                            false)) {
                        c.getItems().wearItem(-1, 0, slot);
                    } else {
                        c.sendMessage("Your bank is full.");
                        break;
                    }
                }
            }
            c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
            c.getItems().resetBank();
            c.getItems().resetTempItems();
            c.getItems().wearItem(6585, 1, c.playerAmulet);
            c.getItems().wearItem(6570, 1, c.playerCape);
            c.getItems().wearItem(13239, 1, c.playerFeet);
            c.getItems().wearItem(12006, 1, c.playerWeapon);
            c.getItems().wearItem(10828, 1, c.playerHat);
            c.getItems().wearItem(11832, 1, c.playerChest);
            c.getItems().wearItem(11834, 1, c.playerLegs);
            c.getItems().wearItem(22322, 1, c.playerShield);
            c.getItems().wearItem(11773, 1, c.playerRing);
            c.getItems().wearItem(7462, 1, c.playerHands);
            c.getItems().addItem(12695, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(385, 20);
        }
        if (playerCommand.startsWith("maxrange") && c.getRights().contains(Right.YOUTUBER)) {
            for (int slot = 0 ; slot < c.playerItems.length; slot++)
                if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
                    c.getItems().addToBank(c.playerItems[slot] -1, c.playerItemsN[slot], false);

                }
            for (int slot = 0; slot < c.playerEquipment.length; slot++) {
                if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
                    if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
                            false)) {
                        c.getItems().wearItem(-1, 0, slot);
                    } else {
                        c.sendMessage("Your bank is full.");
                        break;
                    }
                }
            }
            c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
            c.getItems().resetBank();
            c.getItems().resetTempItems();
            c.getItems().wearItem(6585, 1, c.playerAmulet);
            c.getItems().wearItem(22109, 1, c.playerCape);
            c.getItems().wearItem(13237, 1, c.playerFeet);
            c.getItems().wearItem(11785, 1, c.playerWeapon);
            c.getItems().wearItem(11664, 1, c.playerHat);
            c.getItems().wearItem(13072, 1, c.playerChest);
            c.getItems().wearItem(13073, 1, c.playerLegs);
            c.getItems().wearItem(11284, 1, c.playerShield);
            c.getItems().wearItem(11771, 1, c.playerRing);
            c.getItems().wearItem(8842, 1, c.playerHands);
            c.getItems().addItem(12695, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(9244, 2000);
            c.getItems().addItem(385, 15);
        }
        if (playerCommand.equals("venomme")) {
            c.getHealth().proposeStatus(HealthStatus.VENOM, 2, Optional.empty());
        }

        if (playerCommand.equals("cureme")) {
            c.getHealth().proposeStatus(HealthStatus.NORMAL, 2, Optional.empty());
        }

        if (playerCommand.equals("hitme")) {
            c.getHealth().reduce(10);
        }

        if (playerCommand.equals("resetskills")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            for (int i = 0; i <= 6; i++) {
                if (Skill.forId(i) == Skill.HITPOINTS) {
                    c.playerXP[i] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[i] = 10;
                } else {
                    c.playerXP[i] = c.getPA().getXPForLevel(1) + 5;
                    c.playerLevel[i] = 1;
                }
                c.getPA().refreshSkill(i);
            }
        }

        if (playerCommand.equals("master")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            final int EXP_GOAL = c.getPA().getXPForLevel(99) + 5;
            for (int i = 0; i <= 6; i++) {
                c.playerXP[i] = EXP_GOAL;
                c.playerLevel[i] = 99;
                c.getPA().refreshSkill(i);
            }
        }

        if (playerCommand.equals("max")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            final int EXP_GOAL = c.getPA().getXPForLevel(99) + 5;
            for (int i = 0; i < c.playerXP.length; i++) {
                c.playerXP[i] = EXP_GOAL;
                c.playerLevel[i] = 99;
                c.getPA().refreshSkill(i);
            }
        }

        if (playerCommand.startsWith("/")) {
            if (Server.getPunishments().contains(PunishmentType.MUTE, c.playerName) || Server.getPunishments().contains(PunishmentType.NET_BAN, c.connectedFrom)) {
                c.sendMessage("You are muted for breaking a rule.");
                return;
            }
            if (c.clan != null) {
                c.clan.sendChat(c, playerCommand);
                PlayerLogging.write(LogType.PUBLIC_CHAT, c, "Clan spoke = " + playerCommand);
                return;
            }
            c.sendMessage("You can only do this in a clan chat..");
            return;
        }
        if (playerCommand.startsWith("teletome")) {
            if (!isTeam) {
                c.sendMessage(NO_ACCESS);
                return;
            }



            try {
                String target = playerCommand.replace("teletome ", "");
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    c2.teleportToX = c.absX;
                    c2.teleportToY = c.absY;
                    c2.heightLevel = c.heightLevel;
                    c.sendMessage("You have teleported " + c2.playerName + " to you.");
                    c2.sendMessage("You have been teleported to " + c.playerName + ".");
                }

            } catch (Exception e) {
                c.sendMessage("Player Must Be Offline.");
            }
        }
        if (playerCommand.startsWith("update")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            String[] args = playerCommand.split(" ");
            int seconds = Integer.parseInt(args[1]);
            if (seconds < 15) {
                c.sendMessage("The timer cannot be lower than 15 seconds so other operations can be sorted.");
                seconds = 15;
            }
            PlayerHandler.updateSeconds = seconds;
            PlayerHandler.updateAnnounced = false;
            PlayerHandler.updateRunning = true;
            PlayerHandler.updateStartTime = System.currentTimeMillis();
            Wogw.save();
            for (Player player : PlayerHandler.players) {
                if (player == null) {
                    continue;
                }
                Player client = player;
                if (client.getPA().viewingOtherBank) {
                    client.getPA().resetOtherBank();
                    client.sendMessage("An update is now occuring, you cannot view banks.");
                }
                DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(client, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(duelSession)) {
                    if (duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
                        if (!duelSession.getWinner().isPresent()) {
                            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                            duelSession.getPlayers().forEach(p -> {
                                p.sendMessage("The duel has been cancelled by the server because of an update.");
                                duelSession.moveAndClearAttributes(p);
                            });
                        }
                    } else if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
                        duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                        duelSession.getPlayers().forEach(p -> {
                            p.sendMessage("The duel has been cancelled by the server because of an update.");
                            duelSession.moveAndClearAttributes(p);
                        });
                    }
                }
            }
        }


        if (playerCommand.startsWith("spawns") && c.playerName.equals("Noah") || c.playerName.equals("Aaron")) {
            Server.npcHandler = null;
            Server.npcHandler = new ethos.model.npcs.NPCHandler();
            c.sendMessage("@blu@Reloaded NPCs");
        }

        if (playerCommand.equals("rights")) {
            c.sendMessage("isOwner: "+c.getRights().contains(Right.OWNER));
            c.sendMessage("isAdmin: "+c.getRights().contains(Right.ADMINISTRATOR));
            c.sendMessage("isManagment: "+isManagment);
            c.sendMessage("isMod: "+c.getRights().contains(Right.MODERATOR));
            c.sendMessage("isExreme_Donor: "+c.getRights().contains(Right.EXTREME_DONOR));
            c.sendMessage("isPlayer: "+c.getRights().contains(Right.PLAYER));
        }

        if (playerCommand.startsWith("giverights")) {
            if (!c.getRights().isOrInherits(Right.OWNER)) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            try {

                String[] args = playerCommand.split(" ");
                int right = Integer.parseInt(args[1]);
                String target = playerCommand.substring(args[0].length()+1+args[1].length()).trim();
                boolean found = false;

                for (Player p : PlayerHandler.players) {
                    if (p == null)
                        continue;

                    if (p.playerName.equalsIgnoreCase(target)) {
                        p.getRights().setPrimary(Right.get(right));
                        p.sendMessage("Your rights have changed. Please relog.");
                        found = true;
                        break;
                    }
                }


                if (found) {
                    c.sendMessage("Set "+target+"'s rights to: "+right);
                } else {
                    c.sendMessage("Couldn't change \""+target+"\"'s rights. Player not found.");
                }

            } catch (Exception e) {
                c.sendMessage("Improper usage! ::giverights [id] [target]");
            }

        }

        if (playerCommand.startsWith("infernorock")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            c.getPA().object(30342, 2267, 5366, 1, 10);  // West Wall
            c.getPA().object(30341, 2275, 5366, 3, 10);  // East Wall
            c.getPA().object(30340, 2267, 5364, 1, 10);  // West Corner
            c.getPA().object(30339, 2275, 5364, 3, 10);  // East Corner


            //falling
            c.getPA().object(30344, 2268, 5364, 3, 10);
            c.getPA().object(30343, 2273, 5364, 3, 10);
             c.getPA().sendPlayerObjectAnimation(c, 2268, 5364, 7560, 10, 3, c.height); // Set falling rocks animation - west
            c.getPA().sendPlayerObjectAnimation(c, 2273, 5364, 7559, 10, 3, c.height); // Set falling rocks animation - east

            return;
        }

        if (playerCommand.startsWith("startinferno")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            try {
                int wave = 68;
                String[] split = playerCommand.split(" ");
                if (split.length > 1) {
                    wave = Integer.parseInt(split[1]);
                }
                if (wave > 68 || wave < Inferno.getDefaultWave()) {
                    c.sendMessage("Invalid wave, must be between " + (Inferno.getDefaultWave() - 1) + " and " + 69);
                } else {
                    Inferno.startInferno(c, wave);
                }
            } catch (NumberFormatException e) {
                c.sendMessage("Invalid format, do ::startinferno or ::startinfero wave_number");
            }
        }

        if (playerCommand.startsWith("caladdwinner")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            String[] args = playerCommand.split(" ");
            if (args.length < 3) {
                c.sendMessage("Invalid format, ::caladdwinner usernam_example day_number");
            } else {
                String username = args[1].replaceAll("_", " ");
                int day = Integer.parseInt(args[2]);
                try {
                    AddWinnerQuery.addWinner(Server.getDatabaseManager(), DatabaseConfiguration.EVENT_CALENDAR, username, day);
                    c.sendMessage("Added " + username + " as winner for day " + day);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    c.sendMessage("An error occurred");
                }
            }
        }

        if (playerCommand.startsWith("calparticipants")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, new PrintParticipants());
        }

        if (playerCommand.startsWith("calwinners")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, new PrintWinners<>());
        }

        if (playerCommand.startsWith("item")) {
            if (!isManagment && !Config.BETA_MODE) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            try {
                String[] args = playerCommand.split(" ");
                if (args.length >= 2) {
                    int newItemID = Integer.parseInt(args[1]);
                    int newItemAmount = 1;
                    if (args.length > 2) {
                        newItemAmount = Integer.parseInt(args[2]);
                    }

                    if ((newItemID <= 40000) && (newItemID >= 0)) {
                        c.getItems().addItem(newItemID, newItemAmount);
                    } else {
                        c.sendMessage("No such item.");
                    }
                } else {
                    c.sendMessage("Use as ::item id amount.");
                }
            } catch (Exception e) {

            }
        }
        if (playerCommand.startsWith("movehome")) {

            if (!isTeam) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            try {

                String target = playerCommand.replace("movehome ", "");
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    c2.teleportToX = Config.EDGEVILLE_X;
                    c2.teleportToY = Config.EDGEVILLE_Y;
                    c2.heightLevel = 0;
                    c.sendMessage("You have teleported " + c2.playerName + " to home.");
                    c2.sendMessage("You have been teleported home by " + c.playerName + ".");
                }

            } catch (Exception e) {
                c.sendMessage("Invalid usage! ::movehome [target]");
            }

        }

        if (playerCommand.startsWith("instance")) {
            c.sendMessage("Instance is: " + c.getInstance());
        }

        if (playerCommand.startsWith("config")) {
            String[] args = playerCommand.split(" ");
            if (args.length == 3) {
                c.getPA().sendConfig(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                c.sendMessage(String.format("Send config [%d, %d]", Integer.parseInt(args[1]), Integer.parseInt(args[2])));
            } else {
                c.sendMessage("Incorrect usage! [::config 180 1]");
            }
        }

        if (playerCommand.startsWith("points")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }

            c.getSlayer().setPoints(1_000_000);
            c.donatorPoints += 1_000_000;
            c.pcPoints += 1_000_000;
            c.votePoints += 1_000_000;
            c.achievementPoints += 1_000_000;
        }

        if (playerCommand.startsWith("calunblacklist")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }
            EventCalendarHelper.blacklistCommand(c, playerCommand, false);
            return;
        }

        if (playerCommand.startsWith("calblacklist")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }
            EventCalendarHelper.blacklistCommand(c, playerCommand, true);
            return;
        }

        if (playerCommand.startsWith("hunllef")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }
            c.teleportToX = 3162;
            c.teleportToY = 12428;
            c.heightLevel = 0;
        }

        if (playerCommand.startsWith("qtest")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }
            boolean test = c.getAttributes().getBoolean("qtest");
            c.setSidebarInterface(2, test ? 10220 : 50414);
            c.getAttributes().setBoolean("qtest", !test);
        }

        if (playerCommand.startsWith("caltest")) {
            if (!isManagment) {
                c.getDH().sendStatement(NO_ACCESS);
                return;
            }

            for (int i = 0; i < 100000; i++) {
                c.getEventCalendar().openCalendar();
            }
        }

        if (playerCommand.startsWith("teleto")) {

            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }

            try {

                String target = playerCommand.replace("teleto ", "");
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    c.teleportToX = c2.absX;
                    c.teleportToY = c2.absY;
                    c.heightLevel = c2.heightLevel;
                    c.sendMessage("You have teleported to " + c2.playerName + ".");
                    c2.sendMessage("You have been teleported to by " + c.playerName + ".");
                }

            } catch (Exception e) {
                c.sendMessage("Invalid usage! ::teleto [target]");
            }

        }
        if (playerCommand.startsWith("maxmelee")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            for (int slot = 0 ; slot < c.playerItems.length; slot++)
                if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
                    c.getItems().addToBank(c.playerItems[slot] -1, c.playerItemsN[slot], false);

                }
            for (int slot = 0; slot < c.playerEquipment.length; slot++) {
                if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
                    if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
                            false)) {
                        c.getItems().wearItem(-1, 0, slot);
                    } else {
                        c.sendMessage("Your bank is full.");
                        break;
                    }
                }
            }
            c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
            c.getItems().resetBank();
            c.getItems().resetTempItems();
            c.getItems().wearItem(6585, 1, c.playerAmulet);
            c.getItems().wearItem(6570, 1, c.playerCape);
            c.getItems().wearItem(13239, 1, c.playerFeet);
            c.getItems().wearItem(12006, 1, c.playerWeapon);
            c.getItems().wearItem(10828, 1, c.playerHat);
            c.getItems().wearItem(11832, 1, c.playerChest);
            c.getItems().wearItem(11834, 1, c.playerLegs);
            c.getItems().wearItem(22322, 1, c.playerShield);
            c.getItems().wearItem(11773, 1, c.playerRing);
            c.getItems().wearItem(7462, 1, c.playerHands);
            c.getItems().addItem(12695, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(385, 20);
        }
        if (playerCommand.startsWith("maxrange")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            for (int slot = 0 ; slot < c.playerItems.length; slot++)
                if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
                    c.getItems().addToBank(c.playerItems[slot] -1, c.playerItemsN[slot], false);

                }
            for (int slot = 0; slot < c.playerEquipment.length; slot++) {
                if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
                    if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
                            false)) {
                        c.getItems().wearItem(-1, 0, slot);
                    } else {
                        c.sendMessage("Your bank is full.");
                        break;
                    }
                }
            }
            c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
            c.getItems().resetBank();
            c.getItems().resetTempItems();
            c.getItems().wearItem(6585, 1, c.playerAmulet);
            c.getItems().wearItem(22109, 1, c.playerCape);
            c.getItems().wearItem(13237, 1, c.playerFeet);
            c.getItems().wearItem(11785, 1, c.playerWeapon);
            c.getItems().wearItem(11664, 1, c.playerHat);
            c.getItems().wearItem(13072, 1, c.playerChest);
            c.getItems().wearItem(13073, 1, c.playerLegs);
            c.getItems().wearItem(11284, 1, c.playerShield);
            c.getItems().wearItem(11771, 1, c.playerRing);
            c.getItems().wearItem(8842, 1, c.playerHands);
            c.getItems().addItem(12695, 1);
            c.getItems().addItem(3024, 1);
            c.getItems().addItem(6685, 2);
            c.getItems().addItem(9244, 2000);
            c.getItems().addItem(385, 15);
        }
        if (playerCommand.startsWith("ban") && !playerCommand.startsWith("bank")) {
            if (!isManagment) {
                c.sendMessage(NO_ACCESS);
                return;
            }
            try {
                String[] args = playerCommand.split(" ");
                
                
                /*
                //durations lul?
                int duration = 0;
                
                if (Misc.tryParseInt(args[args.length-1])) {
                	duration = Integer.parseInt(args[args.length-1]);
                };
                */

                //String name = playerCommand.substring(args[0].length()+1+args[1].length()).trim();

                StringBuilder sb = new StringBuilder();
                sb.append(playerCommand);
                Misc.deleteFromSB(sb, args[0]);
                String target = sb.toString().trim();
                System.out.println("target: "+target);



                Punishments punishments = Server.getPunishments();
                if (punishments.contains(PunishmentType.BAN, target)) {
                    c.sendMessage(target+" is already banned.");
                    return;
                }
                Server.getPunishments().add(new Punishment(PunishmentType.BAN, 0, target));
                Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(target);
                if (optionalPlayer.isPresent()) {
                    Player c2 = optionalPlayer.get();
                    if (c2 == null) {
                        return;
                    }
                    
                    /* @TODO FIX THIS TOMORROW
                    if (!c2.getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER) || !c.getRights().isOrInherits(Right.OWNER)) {
                        c.sendMessage("You cannot ban this player.");
                        return;
                    }
                    */

                    if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
                        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
                        session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
                    }
                    c2.properLogout = true;
                    c2.disconnected = true;
                    c.sendMessage(target+" was permenantly banned.");

                    return;
                }

            } catch (Exception e) {
                c.sendMessage("Correct usage. ::ban [target] [duration] (no duration = perm)");
            }
        }

        PlayerLogging.write(LogType.COMMAND, c, c.playerName + " typed command " + playerCommand + " at X: " + c.absX + " Y:" + c.absY);

        if (c.getRights().isOrInherits(Right.OWNER) && executeCommand(c, playerCommand, "owner")) {
            return;
        } else if (c.getRights().isOrInherits(Right.ADMINISTRATOR) && executeCommand(c, playerCommand, "admin")) {
            return;
        } else if (c.getRights().isOrInherits(Right.MODERATOR) && executeCommand(c, playerCommand, "moderator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.HELPER) && executeCommand(c, playerCommand, "helper")) {
            return;
        } else if (c.getRights().isOrInherits(Right.ULTRA_DONATOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.REGULAR_DONATOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.SUPER_DONOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.EXTREME_DONOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.LEGENDARY_DONATOR) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.DIAMOND_CLUB) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.ONYX_CLUB) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.PLATINUM) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (c.getRights().isOrInherits(Right.DIVINE) && executeCommand(c, playerCommand, "donator")) {
            return;
        } else if (executeCommand(c, playerCommand, "all")) {
            return;
        }
    }
}
