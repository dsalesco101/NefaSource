package ethos.model.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ethos.Config;
import ethos.Server;
import ethos.clip.PathChecker;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.event.impl.RandomEvent;
import ethos.model.Animation;
import ethos.model.AnimationPriority;
import ethos.model.content.SantaStolenItems;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.AchievementDiaryKills;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.morytania.MorytaniaDiaryEntry;
import ethos.model.content.barrows.Barrows;
import ethos.model.content.barrows.brothers.Brother;
import ethos.model.content.godwars.GodwarsNPCs;
import ethos.model.content.instances.InstancedArea;
import ethos.model.entity.HealthStatus;
import ethos.model.items.EquipmentSet;
import ethos.model.minigames.Wave;
import ethos.model.minigames.inferno.InfernoWave;
import ethos.model.minigames.warriors_guild.AnimatedArmour;
import ethos.model.minigames.xeric.XericWave;
import ethos.model.npcs.animations.AttackAnimation;
import ethos.model.npcs.animations.DeathAnimation;
import ethos.model.npcs.bosses.Hunllef;
import ethos.model.npcs.bosses.Kraken;
import ethos.model.npcs.bosses.Scorpia;
import ethos.model.npcs.bosses.hydra.AlchemicalHydra;
import ethos.model.npcs.bosses.hydra.HydraStage;
import ethos.model.npcs.bosses.raids.Tekton;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.wildypursuit.FragmentOfSeren;
import ethos.model.npcs.bosses.wildypursuit.Sotetseg;
//import ethos.model.npcs.bosses.wildypursuit.Seren;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.drops.DropManager;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.Special;
import ethos.model.players.combat.Specials;
import ethos.model.players.combat.effects.SerpentineHelmEffect;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.skills.hunter.impling.PuroPuro;
import ethos.util.Location3D;
import ethos.util.Misc;
import ethos.model.players.combat.CombatScript;

public class NPCHandler {

    public void checkMa(Player c, int i) {
        if (c.roundNpc == 2 && !c.spawned) {
            spawnNpc(c, 1606, 3106, 3934, 0, 1, 30, 24, 70, 60, true, true);
            c.roundNpc = 3;
            c.spawned = true;
        } else if (c.roundNpc == 3 && !c.spawned) {
            spawnNpc(c, 1607, 3106, 3934, 0, 1, 60, 24, 70, 60, true, true);
            c.roundNpc = 4;
            c.spawned = true;
        } else if (c.roundNpc == 4 && !c.spawned) {
            spawnNpc(c, 1608, 3106, 3934, 0, 1, 80, 15, 70, 60, true, true);
            c.roundNpc = 5;
            c.spawned = true;
        } else if (c.roundNpc == 5 && !c.spawned) {
            spawnNpc(c, 1609, 3106, 3934, 0, 1, 140, 19, 70, 60, true, true);
            c.roundNpc = 6;
            c.spawned = true;
        } else if (c.roundNpc == 6 && !c.spawned) {
            c.getPA().movePlayer(2541, 4716, 0);
            c.getDH().sendNpcChat3("Congratulations you have proved your self worthy!", "Head back to the arena now to earn points!", "Goodluck wizard!", 1603, "Kolodion");
            c.roundNpc = 0;
            c.maRound = 2;
        }
    }

    public static int maxNPCs = 30000;
    public static int maxListedNPCs = 10000;
    public static int maxNPCDrops = 10000;
    public static NPC npcs[] = new NPC[maxNPCs];
    private static NPCDef[] npcDef = new NPCDef[maxListedNPCs];

    public static boolean projectileClipping = true;

    /**
     * Tekton variables
     */
    public static String tektonAttack = "MELEE";
    public static Boolean tektonWalking = false;
    NPC TEKTON = NPCHandler.getNpc(7544);
    NPC RDRAGON = NPCHandler.getNpc(8031);
    NPC ADRAGON = NPCHandler.getNpc(8030);

    /**
     * Ice demon variables
     */
    NPC ICE_DEMON = NPCHandler.getNpc(7584);

    /**
     * Skeletal mystics
     */
    NPC SKELE_MYSTIC_ONE = NPCHandler.getNpc(7604);
    NPC SKELE_MYSTIC_TWO = NPCHandler.getNpc(7605);
    NPC SKELE_MYSTIC_THREE = NPCHandler.getNpc(7606);

    /**
     * Glod variables
     */
    public static String glodAttack = "MELEE";
    public static Boolean glodWalking = false;
    NPC GLOD = NPCHandler.getNpc(5219);

    /*
     * wilderness bosses (MA2) variables
     */
    NPC JUSTICIAR = NPCHandler.getNpc(7858);
    NPC DERWEN = NPCHandler.getNpc(7859);
    NPC PORAZDIR = NPCHandler.getNpc(7860);


    /**
     * Queen variables
     */
    public static String queenAttack = "MAGIC";
    public static Boolean queenWalking = false;
    NPC QUEEN = NPCHandler.getNpc(4922);

    private NPCProcess npcProcess = new NPCProcess(this);

    public NPCHandler() {
        for (int i = 0; i < maxNPCs; i++) {
            npcs[i] = null;
            NPCDefinitions.getDefinitions()[i] = null;
        }
        loadNPCList("./Data/CFG/npc_config.cfg");
        loadAutoSpawn("./Data/CFG/spawn_config.cfg");
        loadNPCSizes("./Data/cfg/npc_sizes.txt");
        NPCRelationship.setup();
        startGame();
    }

    public void init() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        for (int i = 0; i < maxNPCs; i++) {
            npcs[i] = null;
            NPCDefinitions.getDefinitions()[i] = null;
        }
        loadNPCList("./Data/CFG/npc_config.cfg");

        NPCSpawns.loadNPCSpawns();
        NPCSpawns.getNPCSpawns().forEach(npcSpawn -> {
            newNPC(npcSpawn.getNpcId(), npcSpawn.getXPos(), npcSpawn.getYPos(),
                    npcSpawn.getHeight(), npcSpawn.getWalkType(),
                    getNpcListHP(npcSpawn.getNpcId()), npcSpawn.getMaxHit(),
                    npcSpawn.getAttack(), npcSpawn.getDefence());
        });

        loadNPCSizes("./Data/cfg/npc_sizes.txt");
        startGame();
    }

    public boolean ringOfLife(Player c) {
        boolean defenceCape = SkillcapePerks.DEFENCE.isWearing(c);
        boolean maxCape = SkillcapePerks.isWearingMaxCape(c);
        if (c.getItems().isWearingItem(2570) || defenceCape || (maxCape && c.getRingOfLifeEffect())) {
            if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
                c.sendMessage("The ring of life effect does not work as you are teleblocked.");
                return false;
            }
            if (defenceCape || maxCape) {
                c.sendMessage("Your cape activated the ring of life effect and saved you!");
            } else {
                c.getItems().deleteEquipment(2570, c.playerRing);
                c.sendMessage("Your ring of life saved you!");
            }
            c.getPA().spellTeleport(3087, 3499, 0, false);
            return true;
        }
        return false;
    }

    public void spawnNpc3(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                          int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc3");
            return;
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getIndex();
        newNPC.underAttack = true;
        newNPC.facePlayer(c.getIndex());
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (summonFollow) {
            newNPC.summoner = true;
            newNPC.summonedBy = c.getIndex();
            c.hasFollower = true;
        }
        if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.killerId = c.getIndex();
        }
        npcs[slot] = newNPC;
    }

    public void stepAway(int i) {
        int[][] points = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] k : points) {
            int dir = NPCClipping.getDirection(k[0], k[1]);
            if (NPCDumbPathFinder.canMoveTo(npcs[i], dir)) {
                NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() + NPCClipping.DIR[dir][0],
                        npcs[i].getY() + NPCClipping.DIR[dir][1]);
                break;
            }
        }
    }

    public void multiAttackGfx(int i, int gfx) {
        if (npcs[i].projectileId < 0)
            return;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player c = PlayerHandler.players[j];
                if (c.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
                    int nX = NPCHandler.npcs[i].getX() + offset(i);
                    int nY = NPCHandler.npcs[i].getY() + offset(i);
                    int pX = c.getX();
                    int pY = c.getY();
                    int offX = (nX - pX) * -1;
                    int offY = (nY - pY) * -1;
                    int centerX = nX + npcs[i].getSize() / 2;
                    int centerY = nY + npcs[i].getSize() / 2;
                    c.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, getProjectileSpeed(i),
                            npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
                            getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getIndex() - 1, 65);
                    if (npcs[i].npcType == 7554) {
                        c.getPA().sendPlayerObjectAnimation(c, 3220, 5738, 7371, 10, 3, c.getHeight());
                    }
                }
            }
        }
    }

    public boolean switchesAttackers(int i) {
        switch (npcs[i].npcType) {

            case 963:
            case 965:
            case 3129:
            case 2208:
            case 239:
            case 6611:
            case 6612:
            case 494:
            case 319:
            case 7554:
            case 320:
            case 5535:
            case 2551:
            case 6609:
            case 2552:
            case 2553:
            case 2559:
            case 2560:
            case 2561:
            case 2563:
            case 2564:
            case 2565:
            case 2892:
            case 2894:
            case 1046:
            case 6615:
            case 6616:
            case 7604:
            case 7605:
            case 7606:
            case 7544:
            case 5129:
            case FragmentOfSeren.NPC_ID:
            case 8781:
                return true;

        }

        return false;
    }

    public static boolean isSpawnedBy(Player player, NPC npc) {
        if (player != null && npc != null)
            if (npc.spawnedBy == player.getIndex() || npc.killerId == player.getIndex())
                return true;
        return false;
    }

    public int getCloseRandomPlayer(int i) {
        ArrayList<Integer> players = new ArrayList<>();
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                // Great Olm
                if (npcs[i].npcType == 7554 && !PlayerHandler.players[j].inOlmRoom()) {
                    continue;
                }

                if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
                    if (!Boundary.isIn(PlayerHandler.players[j], Boundary.CORPOREAL_BEAST_LAIR)) {
                        npcs[i].killerId = 0;
                        continue;
                    }
                }
                /**
                 * Skips attacking a player if mode set to invisible
                 */
                if (PlayerHandler.players[j].isInvisible()) {
                    continue;
                }
                if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
                    if (!Boundary.isIn(PlayerHandler.players[j], Boundary.GODWARS_BOSSROOMS)) {
                        npcs[i].killerId = 0;
                        continue;
                    }
                }
                if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
                        npcs[i].absY, distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
                    if ((PlayerHandler.players[j].underAttackByPlayer <= 0 && PlayerHandler.players[j].underAttackByNpc <= 0)
                            || PlayerHandler.players[j].inMulti())
                        if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
                            players.add(j);
                }
            }
        }
        if (players.size() > 0)
            return players.get(Misc.random(players.size() - 1));
        else
            return 0;
    }

    public int culinomancer = 0;

    /**
     * Updated to support the new drop table checker
     *
     * @param i
     * @param searching
     * @return
     */
    public boolean isAggressive(int i, boolean searching) {
        if (HydraStage.stream().anyMatch(stage -> i == stage.getNpcId()))
            return true;
        if (!searching) {
            if (HydraStage.stream().anyMatch(stage -> npcs[i].npcType == stage.getNpcId()))
                return true;
            if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)
                    || Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)
                    || Boundary.isIn(npcs[i], Boundary.XERIC)
                    || Boundary.isIn(npcs[i], Boundary.RAIDROOMS)
                    || (Boundary.isIn(npcs[i], Boundary.CATACOMBS)
                    && !(npcs[i].npcType == 2098) //hill giant
                    && !(npcs[i].npcType == 7283) //npc
                    && !(npcs[i].npcType == 85) //ghost
                    && !(npcs[i].npcType == 7268)//possessed pickaxe
                    && !(npcs[i].npcType == 891)//moss giant
                    && !(npcs[i].npcType == 70)//skeleton
                    && !(npcs[i].npcType == 273)//iron dragon

            )) {
                return true;
            }
        }

        if (searching) {
            switch (i) {
                case 7931:
                case 7932:
                case 7933:
                case 7934:
                case 7935:
                case 7936:
                case 7937:
                case 7938:
                case 7939:
                case 7940:
                case 5916:
                case 690:
                case 5779:
                case 963:
                case 7401:
                case 965:
                case 2098:
                case 70:
                case 4005:
                case 2084:
                case 7273:
                case 7274:
                case 7275:
                case 6607:
                case 270:
                case 273:
                case 415:
                case 7244:
                case 7278:
                case 10:
                case 85:
                case 891:
                case 423:
                case 7277:
                case 7258:
                case 7279:
                case 7272:
                case 2514:
                case 975:
                case 955:
                case 957:
                case 959:
                case 7032:
                case 5867:
                case 5868:
                case 5869:
                case 2042:
                case 239:
                case 7413:
                case 1739:
                case 1740:
                case 1741:
                case 1742:
                case 2044:
                case 2043:
                case 465:
                case 8062:
                case 7706:
                case Zulrah.SNAKELING:
                case 5054:
                case 6611:
                case 6612:
                case 6610:
                case 494:
                case 5535:
                case 2550:
                case 2551:
                case 50:
                case 28:
                case 2552:
                case 6609:
                case 763:
                case 2553:
                case 2558:
                case 2559:
                case 2560:
                case 2561:
                    return true;
                case 2562:
                case 2563:
                case 2564:
                case 2565:
                case 2892:
                case 2894:
                case 2265:
                case 2266:
                case 2267:
                case 2035:
                case 291:
                case 435:
                case 135:
                case 7276:
                case 5944: // Rock lobster
                    // Godwars
                case 3138:
                case 2205:
                case 2206:
                case 2207:
                case 2208:
                case 2209:
                case 2211:
                case 2212:
                case 2215:
                case 2216:
                case 2217:
                case 2218:
                case 2233:
                case 2234:
                case 2235:
                case 2237:
                case 2242:
                case 2243:
                case 2244:
                case 2245:
                case 3129:
                case 3130:
                case 3131:
                case 3132:
                case 3133:
                case 3134:
                case 3135:
                case 3137:
                case 3139:
                case 3140:
                case 3141:
                case 3159:
                case 3160:
                case 3161:
                case 3162:
                    return true;
                case 7037:
                case 3163:
                case 3164:
                case 3165:
                case 3166:
                case 3167:
                case 3168:
                case 3174:

                    // Barrows tunnel monsters
                case 1678:
                case 1679:
                case 1683:
                case 1684:
                case 1685:

                case Skotizo.SKOTIZO_ID:
                case Skotizo.REANIMATED_DEMON:
                case Skotizo.DARK_ANKOU:
                    // GWD
                case 6230:
                case 6231:
                case 6229:
                case 6232:
                case 6240:
                case 6241:
                case 6242:
                case 6233:
                case 6234:
                case 6243:
                case 6244:
                case 6245:
                case 6246:
                case 6238:
                case 6239:
                case 6625:
                case 122:// Npcs That Give BandosKC
                case 6278:
                case 6277:
                case 6276:
                    return true;
                case 6283:
                case 6282:
                case 6281:
                case 6280:
                case 2026:
                case 6279:
                case 6271:
                case 6272:
                case 6273:
                case 6274:
                case 6269:
                case 6270:
                case 6268:
                case 6221:
                case 6219:
                case 6220:
                case 6217:
                case 6216:
                case 6215:
                case 6214:
                case 6213:
                case 6212:
                case 6211:
                case 6218:
                case 6275:
                case 6257:// Npcs That Give SaraKC
                case 6255:
                case 6256:
                case 6259:
                case 6254:
                case 1689:
                case 1694:
                case 1699:
                case 1704:
                case 1709:
                case 1714:
                case 1724:
                case 1734:
                case 6914: // Lizardman, Lizardman brute
                case 6915:
                case 6916:
                    return true;
                case 6917:
                case 6918:
                case 6919:
                case 6766:
                case 7573:
                case 7617: // Tekton magers
                case 7544: // Tekton
                case 7604: // Skeletal mystic
                case 7605: // Skeletal mystic
                case 7606: // Skeletal mystic
                case 7585: //
                case 7554:
                case 7563: // muttadiles
                case 5129:
                case Sotetseg.NPC_ID:
                case FragmentOfSeren.NPC_ID:
                case 7547: //xeric mini raid
                case 7548:
                case 7559:
                case 7560:
                case 7597:
                case 7596:
                case 7577:
                case 7578:
                case 7579:
                case 7586:
                case 7538:
                case 7531://xeric mini raid bosses
                case 7543:
                case 7566:
                case 8030:// Addy dragon
                case 8031:// Rune dragon
                    return true;
                case 1524:
                case 6600:
                case 6601:
                case 7553:
                case 7555:
                case 6602:
                case 1049:
                case 6617:
                case 6620:
                    return false;
            }
        } else {
            switch (npcs[i].npcType) {
                case 5916:
                case 690:
                case 963:
                case 965:
                case 955:
                case 957:
                case 959:
                case 7032:
                case 5867:
                case 5868:
                case 5869:
                case 2042:
                case 239:
                case 7413:
                case 1739:
                case 1740:
                case 1741:
                case 1742:
                case 2044:
                case 2043:
                case 465:
                case Zulrah.SNAKELING:
                case 5054:
                case 6611:
                case 6612:
                case 6610:
                case 494:
                case 5535:
                case 2550:
                case 2551:
                case 50:
                case 28:
                case 2552:
                case 6609:
                case 763:
                case 2553:
                case 2558:
                case 2559:
                case 2560:
                case 2561:
                case 2562:
                case 2563:
                case 2564:
                case 2565:
                case 2892:
                case 2894:
                case 2265:
                case 2266:
                case 2267:
                case 2035:
                case 5779:
                case 291:
                case 435:
                case 135:
                case 484:
                case 7276:
                case 5944: // Rock lobster

                    // Godwars
                case 3138:
                case 2205:
                case 2206:
                case 2207:
                case 2208:
                case 2209:
                case 2211:
                case 2212:
                case 2215:
                case 2216:
                case 2217:
                case 2218:
                case 2233:
                case 2234:
                case 2235:
                case 2237:
                case 2242:
                case 2243:
                case 2244:
                case 2245:
                case 3129:
                case 3130:
                case 3131:
                case 3132:
                case 3133:
                case 3134:
                case 3135:
                case 3137:
                case 3139:
                case 3140:
                case 3141:
                case 3159:
                case 3160:
                case 3161:
                case 3162:
                case 7037:
                case 3163:
                case 3164:
                case 3165:
                case 3166:
                case 3167:
                    return true;
                case 3168:
                case 3174:

                case Skotizo.SKOTIZO_ID:
                case Skotizo.REANIMATED_DEMON:
                case Skotizo.DARK_ANKOU:

                    // Barrows tunnel monsters
                case 1678:
                case 1679:
                case 1683:
                case 1684:
                case 1685:
                    // GWD
                case 6230:
                case 6231:
                case 6229:
                case 6232:
                case 6240:
                case 6241:
                case 6242:
                case 6233:
                case 6234:
                case 6243:
                case 6244:
                case 6245:
                case 6246:
                case 6238:
                case 6239:
                case 6625:
                case 122:// Npcs That Give BandosKC
                case 6278:
                case 6277:
                case 6276:
                case 6283:
                case 6282:
                case 6281:
                case 6280:
                case 6279:
                case 6271:
                case 6272:
                case 6273:
                case 6274:
                case 6269:
                case 6270:
                case 6268:
                case 6221:
                case 6219:
                case 6220:
                case 6217:
                case 6216:
                case 6215:
                case 6214:
                case 6213:
                case 6212:
                case 6211:
                    return true;
                case 6218:
                case 6275:
                case 6257:// Npcs That Give SaraKC
                case 6255:
                case 6256:
                case 6259:
                case 6254:
                case 1689:
                case 1694:
                case 1699:
                case 1704:
                case 1709:
                case 1714:
                case 1724:
                case 1734:
                case 6914: // Lizardman, Lizardman brute
                case 6915:
                case 6916:
                case 6917:
                case 6918:
                case 6919:
                case 6766:
                case 7573:
                case 7617: // Tekton magers
                case 7544: // Tekton
                case 7604: // Skeletal mystic
                case 7605: // Skeletal mystic
                case 7606: // Skeletal mystic
                case 5129:
                case FragmentOfSeren.NPC_ID:
                case 7388: // Start of superior
                case 7389:
                case 7390:
                case 7391:
                case 7392:
                case 7393:
                case 7394:
                case 7395:
                case 7396:
                case 7397:
                case 7398:
                case 7399:
                case 7400:
                case 7401:
                case 7402:
                case 7403:
                case 7404:
                case 7405:
                case 7406:
                case 7407:
                case 7409:
                case 7410:
                case 7411: // end of superior
                    return true;
                case 1524:
                case 6600:
                case 6601:
                case 6602:
                case 1049:
                case 6617:
                case 6620:
                case FragmentOfSeren.FRAGMENT_ID:
                case FragmentOfSeren.CRYSTAL_WHIRLWIND:
                    return false;
                case 8028:
                    return true;
            }
            if (npcs[i].inWild() && npcs[i].getHealth().getMaximumHealth() > 0)
                return true;
            if (npcs[i].inRaids() && npcs[i].getHealth().getMaximumHealth() > 0)
                return true;
            if (npcs[i].inXeric() && npcs[i].getHealth().getMaximumHealth() > 0)
                return true;
            return isFightCaveNpc(i);
        }
        return false;
    }

    public static boolean isDagannothMother(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {
            case 983:
            case 984:
            case 985:
            case 986:
            case 987:
            case 988:
                return true;
        }
        return false;
    }

    public static boolean isFightCaveNpc(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {
            case Wave.TZ_KEK_SPAWN:
            case Wave.TZ_KIH:
            case Wave.TZ_KEK:
            case Wave.TOK_XIL:
            case Wave.YT_MEJKOT:
            case Wave.KET_ZEK:
            case Wave.TZTOK_JAD:
                return true;
        }
        return false;
    }

    public static boolean isInfernoNpc(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {

            case InfernoWave.JAL_NIB:
            case InfernoWave.JAL_MEJRAH:
            case InfernoWave.JAL_AK:
            case InfernoWave.JAL_AKREK_MEJ:
            case InfernoWave.JAL_AKREK_XIL:
            case InfernoWave.JAL_AKREK_KET:
            case InfernoWave.JAL_IMKOT:
            case InfernoWave.JAL_XIL:
            case InfernoWave.JAL_ZEK:
            case InfernoWave.JALTOK_JAD:
            case InfernoWave.YT_HURKOT:
            case InfernoWave.TZKAL_ZUK:
            case InfernoWave.ANCESTRAL_GLYPH:
            case InfernoWave.JAL_MEJJAK:

                return true;
        }
        return false;
    }

    public static boolean isXericNpc(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {

            case XericWave.RUNT:
            case XericWave.BEAST:
            case XericWave.RANGER:
            case XericWave.MAGE:
            case XericWave.SHAMAN:
            case XericWave.LIZARD:
            case XericWave.VESPINE:
            case XericWave.AIR_CRAB:
            case XericWave.FIRE_CRAB:
            case XericWave.EARTH_CRAB:
            case XericWave.WATER_CRAB:
            case XericWave.ICE_FIEND:
            case XericWave.VANGUARD:
            case XericWave.VESPULA:
            case XericWave.TEKTON:
            case XericWave.MUTTADILE:
            case XericWave.VASA:
            case XericWave.ICE_DEMON:

                return true;
        }
        return false;
    }

    public static boolean isSkotizoNpc(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {
            case Skotizo.SKOTIZO_ID:
            case Skotizo.AWAKENED_ALTAR_NORTH:
            case Skotizo.AWAKENED_ALTAR_SOUTH:
            case Skotizo.AWAKENED_ALTAR_WEST:
            case Skotizo.AWAKENED_ALTAR_EAST:
            case Skotizo.REANIMATED_DEMON:
            case Skotizo.DARK_ANKOU:
                return true;
        }
        return false;
    }

    /**
     * Summon npc, barrows, etc
     **/
    public NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                        int attack, int defence, boolean attackPlayer, boolean headIcon) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.err.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc");
            return null;
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        final NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.getSize();
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getIndex();
        newNPC.setInstance(c.getInstance());
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.killerId = c.getIndex();
            c.underAttackByPlayer = slot;
            c.underAttackByNpc = slot;
        }
        npcs[slot] = newNPC;
        if (newNPC.npcType == 1605) {
            newNPC.forceChat("You must prove yourself... now!");
            newNPC.gfx100(86);
        }
        if (newNPC.npcType == 1606) {
            newNPC.forceChat("This is only the beginning, you can't beat me!");
            newNPC.gfx100(86);
        }
        if (newNPC.npcType == 1607) {
            newNPC.forceChat("Foolish mortal, I am unstoppable.");
        }
        if (newNPC.npcType == 1608) {
            newNPC.forceChat("Now you feel it... The dark energy.");
        }
        if (newNPC.npcType == 1609) {
            newNPC.forceChat("Aaaaaaaarrgghhhh! The power!");
        }
        return newNPC;
    }

    /**
     * Attack animations
     *
     * @param i the npc to perform the animation.
     * @return the animation to be performed.
     */
    public static int getAttackEmote(int i) {
        return AttackAnimation.handleEmote(i);
    }

    /**
     * Death animations
     *
     * @param i the npc to perform the animation.
     * @return the animation to be performed.
     */
    public int getDeadEmote(int i) {
        return DeathAnimation.handleEmote(i);
    }

    /**
     * Death delay
     *
     * @param i the npc whom were setting the delay to
     * @return the delay were setting
     */
    public int getDeathDelay(int i) {
        if (npcs[i] == null)
            return 4;
        switch (npcs[i].npcType) {
            case 8028:
                return 8;
            case 8621:
            case 8622:
                return 10;
            case 5548:
            case 5549:
            case 5550:
            case 5551:
            case 5552:
            case 1505:
            case 2910:
            case 2911:
            case 2912:
            case 484:
            case 7276:
            case 3138:
            case 1635:
            case 1636:
            case 1637:
            case 1638:
            case 1639:
            case 1640:
            case 1641:
            case 1642:
            case 1643:
            case 1654:
            case 7302:
                return 1;
            case 2209:
            case 2211:
            case 2212:
            case 2233:
            case 2234:
            case 435:
            case 3137:
            case 3139:
            case 3140:
            case 3159:
            case 3160:
            case 3161:
            case 2241:
                return 2;
            case 3134:
            case 3141:
                return 3;
            case 3166:
            case 3167:
            case 3168:
            case 3174:
                return 5;
            case 3129:
            case 3130:
            case 3131:
            case 3132:
                return 6;
            case 2237:
            case 2242:
            case 2243:
            case 2244:
            case 3135:
                return 7;
            case 9021:
            case 9022:
            case 9023:
            case 9024:
                return 3;
            default:
                return 4;
        }
    }

    /**
     * Attack delay
     *
     * @param i the npc whom were setting the delay to
     * @return the delay were setting
     */
    public int getNpcDelay(int i) {
        switch (npcs[i].npcType) {
            case InfernoWave.JAL_NIB:
                return 4;
            case InfernoWave.JAL_MEJRAH:
                return 7;
            case InfernoWave.JAL_AK:
                return 4;
            case InfernoWave.JAL_AKREK_KET:
                return 4;
            case InfernoWave.JAL_AKREK_MEJ:
                return 4;
            case InfernoWave.JAL_AKREK_XIL:
                return 4;
            case InfernoWave.JAL_IMKOT:
                return 6;
            case InfernoWave.JAL_XIL:
                return 6;
            case InfernoWave.JAL_ZEK:
                return 6;
            case InfernoWave.YT_HURKOT:
                return 6;
            case InfernoWave.JALTOK_JAD:
                return 9;
            case InfernoWave.JAL_MEJJAK:
                return 7;
            case 499:
                return 4;
            case 498:
                return 7;
            case 6611:
            case 6612:
                return npcs[i].attackType == CombatType.MAGE ? 6 : 5;
            case 6607:
                return 5;
            case 319:
                return npcs[i].attackType == CombatType.MAGE ? 7 : 6;
            case 7554:
                return npcs[i].attackType == CombatType.MAGE ? 4 : 6;
            case 2025:
            case 2028:
            case 963:
            case 965:
                return 7;
            case 3127:
                return 8;
            case 8030:
            case 8031:
                return 5;
            case 2205:
                return 4;
            case Brother.AHRIM:
                return 6;
            case Brother.DHAROK:
                return 7;
            case Brother.GUTHAN:
                return 5;
            case Brother.KARIL:
                return 4;
            case Brother.TORAG:
                return 5;
            case Brother.VERAC:
                return 5;
            case 3167:
            case 2558:
            case 2559:
            case 2560:
            case 2561:
            case 2215:
                return 6;
            // saradomin gw boss
            case 2562:
            case 7597:
            case 7547:
                return 2;
            case 3162:
                return 7;
            default:
                return 5;
        }
    }

    /**
     * Projectile start height
     *
     * @param npcType      the npc to perform the projectile
     * @param projectileId the projectile to be performed
     * @return
     */
    private int getProjectileStartHeight(int npcType, int projectileId) {
        switch (npcType) {
            case 2044:
                return 60;
            case 3162:
                return 0;
            case 3127:
            case 3163:
            case InfernoWave.JALTOK_JAD:
            case 3164:
            case 3167:
            case 3174:
                return 110;
            case 6610:
                switch (projectileId) {
                    case 165:
                        return 20;
                }
                break;
        }
        return 43;
    }

    /**
     * Projectile end height
     *
     * @param npcType      the npc to perform the projectile
     * @param projectileId the projectile to be performed
     * @return
     */
    private int getProjectileEndHeight(int npcType, int projectileId) {
        switch (npcType) {
            case 1605:
                return 0;
            case 3162:
                return 15;
            case 6610:
                switch (projectileId) {
                    case 165:
                        return 30;
                    case 1996:
                        return 0;
                }
                break;
        }
        return 31;
    }

    /**
     * Hit delay
     *
     * @param i the npc whom were setting the delay to
     * @return the delay were setting
     */
    public int getHitDelay(int i) {
        switch (npcs[i].npcType) {
            case 7706:
                return 7;
            case 1605:
            case 1606:
            case 1607:
            case 1608:
            case 1609:
            case 499:
                return 4;
            case 498:
                return 4;
            case 6611:
            case 6612:
                return npcs[i].attackType == CombatType.MAGE ? 3 : 2;
            case 1672:
            case 1675:
            case 1046:
            case 1049:
            case 6610:
            case 2265:
            case 2266:
            case 2054:
            case 2892:
            case 2894:
            case 3125:
            case 3121:
            case 2167:
            case 2558:
            case 2559:
            case 2560:
            case 2209:
            case 2211:
            case 2218:
            case 2242:
            case 2244:
            case 3160:
            case 3163:
            case 3167:
            case 3174:
            case 2028:
            case 8781:
                return 3;
            case 2212:
            case 2217:
            case 3161:
            case 3162:
            case 3164:
            case 3168:
            case 6914: // Lizardman, Lizardman brute
            case 6915:
            case 6916:
            case 6917:
            case 6918:
            case 6919:
            case 2025:
                return 4;
            case 3127:
            case InfernoWave.JALTOK_JAD:
                if (npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.MAGE) {
                    return 5;
                } else {
                    return 2;
                }

            default:
                return 2;
        }
    }

    /**
     * Respawn time
     *
     * @param i the npc whom were setting the time to
     * @return the time were setting
     */
    public int getRespawnTime(int i) {
        switch (npcs[i].npcType) {
            case 6600:
            case 6601:
            case 6602:
            case 320:
            case 1049:
            case 6617:
            case 3118:
            case 3120:
            case 6768:
            case 5054:
            case 2402:
            case 2401:
            case 2400:
            case 2399:
            case 5916:
            case 7604:
            case 7605:
            case 7606:
            case 7585:
            case 5129:
            case FragmentOfSeren.FRAGMENT_ID:
            case FragmentOfSeren.NPC_ID:
            case FragmentOfSeren.CRYSTAL_WHIRLWIND:
            case Sotetseg.NPC_ID:
            case 7563:
            case 7573:
            case 7544:
            case 7566:
            case 7559:
            case 7553:
            case 7554:
            case 7555:
            case 7560:
            case 7527:
            case 7528:
            case 7529:
                return -1;
            case 492:
                return 20;
            case 5862: //cerberus
                return 15;
            case 5001://anti-santa
            case 6477:
            case 5462:
            case 7858:
            case 7859:
            case 7860:
                return -1;

            case 963:
            case 965:
                return 10;
            case 8609:
                return 40;
            case 6618:
            case 6619:
            case 319:
            case 5890:
                return 30;

            case 1046:
            case 465:
                return 60;

            case 6609:
            case 2265:
            case 2266:
            case 2267:
                return 70;

            case 6611:
            case 6612:
            case 8781:
                return 90;
            case 2558:
            case 2559:
            case 2560:
            case 2561:
            case 2562:
            case 2563:
            case 2564:
            case 2205:
            case 2215:
            case 3129:
            case 3162:
            case 1641:
            case 1642:
                return 100;

            case 1643:
                return 180;

            case 1654:
                return 250;
            case 2216:
            case 2217:
            case 2218:
            case 3163:
            case 3164:
            case 3165:
            case 2206:
            case 2207:
            case 2208:
            case 3130:
            case 3131:
            case 3132:
                return 40;
            case 3777:
            case 3778:
            case 3779:
            case 3780:
            case 7302:
                return 250;
            default:
                return 25;
        }
    }

    public NPC newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            throw new IllegalStateException();
        }

        return newNPC(slot, npcType, x, y, heightLevel, WalkingType, HP, maxHit, attack, defence);
    }

    /**
     * Spawn a new npc on the world
     *
     * @param npcType     the npcType were spawning
     * @param x           the x coordinate were spawning on
     * @param y           the y coordinate were spawning on
     * @param heightLevel the heightLevel were spawning on
     * @param WalkingType the WalkingType were setting
     * @param HP          the HP were setting
     * @param maxHit      the maxHit were setting
     * @param attack      the attack level were setting
     * @param defence     the defence level were setting
     */
    public NPC newNPC(int npcIndex, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(npcIndex, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.resetDamageTaken();
        npcs[npcIndex] = newNPC;
        return newNPC;
    }

    public boolean olmDead;
    public static boolean rightClawDead;
    public static boolean leftClawDead;

    /**
     * Constructs a new npc list
     *
     * @param npcType npcType to be set
     * @param npcName npcName to be gathered
     * @param combat  combat level to be set
     * @param HP      HP level to be set
     */
    public void newNPCList(int npcType, String npcName, int combat, int HP) {
        NPCDefinitions newNPCList = new NPCDefinitions(npcType);
        newNPCList.setNpcName(npcName.replaceAll("_", " ").toLowerCase());
        newNPCList.setNpcCombat(combat);
        newNPCList.setNpcHealth(HP);
        NPCDefinitions.getDefinitions()[npcType] = newNPCList;
    }

    public int olmStage;

    /**
     * Handles processes for NPCHandler every 600ms
     */
    public void process() {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] == null)
                continue;
            npcs[i].clearUpdateFlags();
        }
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {
                npcProcess.process(i);
            }
        }
    }

    void respawn(int i) {
        Preconditions.checkArgument(npcs[i].needRespawn);
        int newType = npcs[i].npcType;
        int newX = npcs[i].makeX;
        int newY = npcs[i].makeY;
        int newH = npcs[i].heightLevel;
        int newWalkingType = npcs[i].walkingType;
        int newHealth = npcs[i].getHealth().getMaximumHealth();
        int newMaxHit = npcs[i].maxHit;
        int newAttack = npcs[i].attack;
        int newDefence = npcs[i].defence;
        int parent = npcs[i].parentNpc;
        List<Integer> children = npcs[i].children;
        npcs[i] = null;
        NPC newNpc = newNPC(i, newType, newX, newY, newH, newWalkingType, newHealth, newMaxHit, newAttack, newDefence);
        newNpc.parentNpc = parent;
        newNpc.children = children;
    }

    /**
     * Apply damage
     */
    public void applyDamage(int i) {
        if (npcs[i] != null) {
            if (PlayerHandler.players[npcs[i].oldIndex] == null) {
                return;
            }
            if (npcs[i].isDead) {
                return;
            }
            if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 7413) {
                return;
            }
            Player c = PlayerHandler.players[npcs[i].oldIndex];
            if (npcs[i].npcType >= 7930 && npcs[i].npcType <= 7940 && c.getItems().isWearingItem(21816) && c.ether > 0) {
                return;
            }
            if (multiAttacks(i)) {
                multiAttackDamage(i);
                return;
            }
            if (c.playerAttackingIndex <= 0 && c.npcAttackingIndex <= 0)
                if (c.autoRet == 1)
                    c.npcAttackingIndex = i;
            if (c.attackTimer <= 3) {
                if (!NPCHandler.isFightCaveNpc(i) || npcs[i].npcType != 319) {
                    c.startAnimation(c.getCombat().getBlockEmote());
                }
            }
            if (c.getItems().isWearingItem(12931) || c.getItems().isWearingItem(13197)
                    || c.getItems().isWearingItem(13199) && !(npcs[i].npcType == 319)) {
                DamageEffect venom = new SerpentineHelmEffect();
                if (venom.isExecutable(c)) {
                    venom.execute(c, npcs[i], new Damage(6));
                }
            }
            npcs[i].totalAttacks++;
            boolean protectionIgnored = prayerProtectionIgnored(i);
            if (c.respawnTimer <= 0) {
                int damage = 0;
                int secondDamage = -1;

                /**
                 * Handles all the different damage approaches npcs are dealing
                 */
                if (npcs[i].attackType != null) {
                    switch (npcs[i].attackType) {

                        /**
                         * Handles npcs who are dealing melee based attacks
                         */
                        case MELEE:
                            damage = Misc.random(getMaxHit(i));
                            switch (npcs[i].npcType) {
                                case 6374:
                                    secondDamage = Misc.random(getMaxHit(i));
                                    break;
                                case 423:
                                    if (!(c.getItems().isWearingItem(4164) || c.getItems().isWearingAnyItem(c.SLAYER_HELMETS))) {
                                        c.sendMessage("@red@Your stats are reduced since you do not have a face mask or slayer helm.");
                                        int[] toDecrease = {0, 1, 2, 4, 6};
                                        for (int tD : toDecrease) {
                                            c.playerLevel[tD] -= c.playerLevel[tD];
                                            if (c.playerLevel[tD] < 0) {
                                                c.playerLevel[tD] = 1;
                                            }
                                            c.getPA().refreshSkill(tD);
                                            c.getPA().setSkillLevel(tD, c.playerLevel[tD], c.playerXP[tD]);
                                        }
                                    }
                                    break;
                                case 7930:
                                case 7931:
                                case 7932:
                                case 7933:
                                case 7934:
                                case 7935:
                                case 7936:
                                case 3937:
                                case 7938:
                                case 7939:
                                case 7940:
                                    if (c.playerEquipment[c.playerShield] == 12817) {
                                        damage *= .85;
                                    }
                                    if (c.playerEquipment[c.playerHat] == 22326 && c.playerEquipment[c.playerChest] == 22327 && c.playerEquipment[c.playerLegs] == 22328) {
                                        damage *= .75;
                                    }

                                    break;
                                case 3116:
                                    c.playerLevel[5] -= 1;
                                    if (c.playerLevel[5] < 0) {
                                        c.playerLevel[5] = 0;
                                    }
                                    c.getPA().refreshSkill(Config.PRAYER);
                                    break;
                            }

                            /**
                             * Calculate defence
                             */
                            if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
                                    .random(NPCHandler.npcs[i].attack)) {
                                damage = 0;
                            }
                            if (npcs[i].npcType == 5869) {
                                damage = !c.protectingMelee() ? 30 : 0;
                                c.playerLevel[5] -= c.protectingMelee() && !c.getItems().isWearingItem(12821) ? 30
                                        : c.protectingMelee() && c.getItems().isWearingItem(12821) ? 15 : 0;
                                if (c.playerLevel[5] < 0) {
                                    c.playerLevel[5] = 0;
                                }
                            }


                            /**
                             * Zulrah
                             */
                            if (npcs[i].npcType == 2043 && c.getZulrahEvent().getNpc() != null
                                    && c.getZulrahEvent().getNpc().equals(npcs[i])) {
                                Boundary boundary = new Boundary(npcs[i].targetedLocation.getX(),
                                        npcs[i].targetedLocation.getY(), npcs[i].targetedLocation.getX(),
                                        npcs[i].targetedLocation.getY());
                                if (!Boundary.isIn(c, boundary)) {
                                    return;
                                }
                                damage = 20 + Misc.random(25);
                            }

                            /**
                             * Protection prayer
                             */
                            if (c.protectingMelee() && !protectionIgnored) {
                                if (npcs[i].npcType == 5890)
                                    damage /= 3;
                                else if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
                                        || npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
                                        || npcs[i].npcType == 239 || npcs[i].npcType == 998 || npcs[i].npcType == 999 ||
                                        npcs[i].npcType == 1000 || npcs[i].npcType == 7554 || npcs[i].npcType == 319
                                        || npcs[i].npcType == 320 || npcs[i].npcType == 6615 || npcs[i].npcType == 5916 || npcs[i].npcType == 8781
                                        || npcs[i].npcType == 7544 || npcs[i].npcType == 5129 || npcs[i].npcType == 8918 || npcs[i].npcType == 8030 || npcs[i].npcType == 8031)
                                    damage /= 2;
                                else if (npcs[i].npcType == Sotetseg.NPC_ID) {//Here is melee 80%
                                    damage *= .8;
                                } else {
                                    damage = 0;
                                }
                            } else if (c.protectingMelee() && protectionIgnored) {
                                damage /= 2;
                            }
                            if (c.playerEquipment[c.playerCape] == 10556) { //attacker icon
                                damage /= 0.5;
                            }
                            if (c.protectingRange() && !protectionIgnored) {
                                if (npcs[i].npcType == 7554)
                                    damage /= 2;
                            }
                            /**
                             * Specials and defenders
                             */
                            if (Server.getEventHandler().isRunning(c, "staff_of_the_dead")) {
                                Special special = Specials.STAFF_OF_THE_DEAD.getSpecial();
                                Damage d = new Damage(damage);
                                special.hit(c, npcs[i], d);
                                damage = d.getAmount();
                            }
                            if (c.playerEquipment[c.playerShield] == 12817) {
                                if (Misc.random(100) > 30 && damage > 0) {
                                    damage *= .75;
                                }
                            }
                            if (c.playerEquipment[c.playerChest] == 22327 && c.playerEquipment[c.playerHat] == 22326 && c.playerEquipment[c.playerLegs] == 22328) {
                                damage *= .75;
                            }
                            if (c.getHealth().getCurrentHealth() - damage < 0) {
                                damage = c.getHealth().getCurrentHealth();
                            }
                            break;

                        /**
                         * Handles npcs who are dealing range based attacks
                         */
                        case RANGE:
                            damage = Misc.random(getMaxHit(i));

                            if (c.playerEquipment[c.playerShield] == 12817) {
                                damage *= .85;
                            }
                            if (c.playerEquipment[c.playerHat] == 22326 && c.playerEquipment[c.playerChest] == 22327 && c.playerEquipment[c.playerLegs] == 22328) {
                                damage *= .75;
                            }


                            switch (npcs[i].npcType) {
                                case 6377:
                                    secondDamage = Misc.random(getMaxHit(i));
                                    break;
                            }

                            /**
                             * Range defence
                             */
                            if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
                                    .random(NPCHandler.npcs[i].attack)) {
                                damage = 0;
                            }

                            if (npcs[i].npcType == 5867) {
                                damage = !c.protectingRange() ? 30 : 0;
                                c.playerLevel[5] -= c.protectingMelee() && !c.getItems().isWearingItem(12821) ? 30
                                        : c.protectingMelee() && c.getItems().isWearingItem(12821) ? 15 : 0;
                                if (c.playerLevel[5] < 0) {
                                    c.playerLevel[5] = 0;
                                }
                            }
                            /**
                             * Protection prayer
                             */
                            if (c.protectingRange() && !protectionIgnored) {
                                if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
                                        || npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
                                        || npcs[i].npcType == 7554 || npcs[i].npcType == 239 || npcs[i].npcType == 319
                                        || npcs[i].npcType == 499) {
                                    damage /= 2;
                                } else if (npcs[i].npcType == 7931 || npcs[i].npcType == 7932 || npcs[i].npcType == 7933 ||
                                        npcs[i].npcType == 7934 || npcs[i].npcType == 7935 || npcs[i].npcType == 7936 ||
                                        npcs[i].npcType == 7937 || npcs[i].npcType == 7938 || npcs[i].npcType == 7939 ||
                                        npcs[i].npcType == 7940)
                                    damage /= 3;

                                else {
                                    damage = 0;
                                }
                                if (c.getHealth().getCurrentHealth() - damage < 0) {
                                    damage = c.getHealth().getCurrentHealth();
                                }
                            } else if (c.protectingRange() && protectionIgnored) {
                                damage /= 2;
                            }
                            if (npcs[i].npcType == 2042 || npcs[i].npcType == 2044) {
                                c.getHealth().proposeStatus(HealthStatus.VENOM, 6, Optional.of(npcs[i]));
                            }
                            if (npcs[i].endGfx > 0 || npcs[i].npcType == 3127) {
                                c.gfx100(npcs[i].endGfx);
                            }
                            if (npcs[i].endGfx > 0 || npcs[i].npcType == 7700) {
                                c.gfx100(npcs[i].endGfx);
                            }
                            break;

                        /**
                         * Handles npcs who are dealing mage based attacks
                         */
                        case MAGE:
                            damage = Misc.random(getMaxHit(i));
                            boolean magicFailed = false;

                            if (c.playerEquipment[c.playerShield] == 12817) {
                                damage *= .85;
                            }
                            if (c.playerEquipment[c.playerHat] == 22326 && c.playerEquipment[c.playerChest] == 22327 && c.playerEquipment[c.playerLegs] == 22328) {
                                damage *= .75;
                            }

                            /**
                             * Attacks
                             */
                            switch (npcs[i].npcType) {
                                case 6373:
                                case 6375:
                                case 6376:
                                case 6378:
                                    secondDamage = Misc.random(getMaxHit(i));
                                    break;

                                case 6371: // Karamel
                                    c.freezeTimer = 4;
                                    break;

                                case 2205:
                                    secondDamage = Misc.random(27);
                                    break;

                                case 6609:
                                    c.sendMessage("Callisto's fury sends an almighty shockwave through you.");
                                    break;

                                case 8781:
                                    int r11 = Misc.random(10);
                                    if (r11 >= 7) {
                                        secondDamage = Misc.random(5);
                                    }
                                    break;
                            }
                            /**
                             * Magic defence
                             */
                            if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
                                damage = 0;
                                if (secondDamage > -1) {
                                    secondDamage = 0;
                                }
                                magicFailed = true;
                            }

                            if (npcs[i].npcType == 5868) {
                                damage = !c.protectingMagic() ? 30 : 0;
                                c.playerLevel[5] -= c.protectingMagic() && !c.getItems().isWearingItem(12821) ? 30
                                        : c.protectingMagic() && c.getItems().isWearingItem(12821) ? 15 : 0;
                                if (c.playerLevel[5] < 0) {
                                    c.playerLevel[5] = 0;
                                }
                            }

                            /**
                             * Protection prayer
                             */
                            if (c.protectingMagic() && !protectionIgnored) {

                                switch (npcs[i].npcType) {
                                    case 494:
                                    case 492:
                                    case 5535:
                                        int max = npcs[i].npcType == 494 ? 2 : 0;
                                        if (Misc.random(2) == 0) {
                                            damage = 1 + Misc.random(max);
                                        } else {
                                            damage = 0;
                                            if (secondDamage > -1) {
                                                secondDamage = 0;
                                            }
                                        }
                                        break;

                                    case 1677:
                                    case 963:
                                    case 965:
                                    case 8349:
                                    case 8133:
                                    case 6342:
                                    case 2054:
                                    case 239:
                                    case 1046:
                                    case 8028:
                                    case 319:
                                    case 7554:
                                    case 7604: // Skeletal mystic
                                    case 7605: // Skeletal mystic
                                    case 7606: // Skeletal mystic
                                    case 7617:
                                    case 8923:
                                        damage /= 2;
                                        break;

                                    default:
                                        damage = 0;
                                        if (secondDamage > -1) {
                                            secondDamage = 0;
                                        }
                                        magicFailed = true;
                                        break;

                                }

                            } else if (c.protectingMagic() && protectionIgnored) {
                                damage /= 2;
                            }
                            if (c.getHealth().getCurrentHealth() - damage < 0) {
                                damage = c.getHealth().getCurrentHealth();
                            }
                            if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
                                c.gfx100(npcs[i].endGfx);
                            } else {
                                c.gfx100(85);
                            }
                            c.getCombat().appendVengeanceNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
                            break;

                        /**
                         * Handles npcs who are dealing dragon fire based attacks
                         */
                        case DRAGON_FIRE:
                            int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
                                    || c.getItems().isWearingItem(11284) ? 2 : 0;
                            if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
                                resistance++;
                            }

                            if (resistance == 0) {
                                damage = Misc.random(getMaxHit(i));
                                if (npcs[i].npcType == 465) {
                                    c.sendMessage("You are badly burnt by the cold breeze!");
                                } else {
                                    c.sendMessage("You are badly burnt by the dragon fire!");
                                }
                            } else if (resistance == 1) {
                                damage = Misc.random(15);
                            } else if (resistance == 2) {
                                damage = 0;
                            }
                            if (npcs[i].endGfx != 430 && resistance == 2) {
                                damage = 5 + Misc.random(5);
                            }

                            /**
                             * Attacks
                             */
                            switch (npcs[i].endGfx) {
                                case 429:
                                    c.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(npcs[i]));
                                    break;

                                case 163:
                                    c.freezeTimer = 15;
                                    c.sendMessage("You have been frozen to the ground.");
                                    break;

                                case 428:
                                    c.freezeTimer = 10;
                                    break;

                                case 431:
                                    c.lastSpear = System.currentTimeMillis();
                                    break;
                            }
                            if (c.getHealth().getCurrentHealth() - damage < 0)
                                damage = c.getHealth().getCurrentHealth();
                            c.gfx100(npcs[i].endGfx);

                            c.getCombat().appendVengeanceNPC(damage + 0, i);
                            break;

                        /**
                         * Handles npcs who are dealing special attacks
                         */
                        case SPECIAL:
                            damage = Misc.random(getMaxHit(i));

                            /**
                             * Attacks
                             */
                            switch (npcs[i].npcType) {
                                case 3129:
                                    int prayerReduction = c.playerLevel[5] / 2;
                                    if (prayerReduction < 1) {
                                        break;
                                    }
                                    c.playerLevel[5] -= prayerReduction;
                                    if (c.playerLevel[5] < 0) {
                                        c.playerLevel[5] = 0;
                                    }
                                    c.getPA().refreshSkill(5);
                                    c.sendMessage(
                                            "K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
                                    break;
                                case 1046:
                                case 1049:
                                    prayerReduction = c.playerLevel[5] / 10;
                                    if (prayerReduction < 1) {
                                        break;
                                    }
                                    c.playerLevel[5] -= prayerReduction;
                                    if (c.playerLevel[5] < 0) {
                                        c.playerLevel[5] = 0;
                                    }
                                    c.getPA().refreshSkill(5);
                                    c.sendMessage("Your prayer has been drained drastically.");
                                    break;
                                case 6609:
                                    damage = 3;
                                    c.gfx0(80);
                                    c.lastSpear = System.currentTimeMillis();
                                    c.getPA().getSpeared(npcs[i].absX, npcs[i].absY, 3);
                                    c.sendMessage("Callisto's roar sends your backwards.");
                                    break;
                                case 6610:
                                    if (c.protectingMagic()) {
                                        damage *= .7;
                                    }
                                    secondDamage = Misc.random(getMaxHit(i));
                                    if (secondDamage > 0) {
                                        c.gfx0(80);
                                    }
                                    break;

                                case 465:
                                    c.freezeTimer = 15;
                                    c.sendMessage("You have been frozen.");
                                    break;

                                case 8781:
                                    break;
                                case 7144:
                                case 7145:
                                case 7146:
                                    if (gorillaBoulder.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                        return;
                                    }
                                    break;

                                case 5890:
                                    if (damage > 0 && Misc.random(2) == 0) {
                                        if (npcs[i].getHealth().getStatus() == HealthStatus.POISON) {
                                            c.getHealth().proposeStatus(HealthStatus.POISON, 15, Optional.of(npcs[i]));
                                        }
                                    }
                                    if (gorillaBoulder.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                        return;
                                    }
                                    break;
                            }
                            break;
                    }

                    if (npcs[i].npcType == 320) {
                        int distanceFromTarget = c.distanceToPoint(npcs[i].getX(), npcs[i].getY());

                        if (distanceFromTarget <= 1) {
                            NPC corp = NPCHandler.getNpc(319);
                            Damage heal = new Damage(
                                    damage + Misc.random(15 + 5) + (secondDamage > 0 ? secondDamage : 0));
                            if (corp != null && corp.getHealth().getCurrentHealth() < 2000) {
                                corp.getHealth().increase(heal.getAmount());
                            }
                        }
                    }
                    if (npcs[i].npcType == 6617 || npcs[i].npcType == 6616 || npcs[i].npcType == 6615) {
                        int distanceFromTarget = c.distanceToPoint(npcs[i].getX(), npcs[i].getY());

                        List<NPC> healer = Arrays.asList(NPCHandler.npcs);

                        if (distanceFromTarget <= 1 && Scorpia.stage > 0 && healer.stream().filter(Objects::nonNull)
                                .anyMatch(n -> n.npcType == 6617 && !n.isDead && n.getHealth().getCurrentHealth() > 0)) {
                            NPC scorpia = NPCHandler.getNpc(6615);
                            Damage heal = new Damage(
                                    damage + Misc.random(45 + 5) + (secondDamage > 0 ? secondDamage : 0));
                            if (scorpia != null && scorpia.getHealth().getCurrentHealth() < 150) {
                                scorpia.getHealth().increase(heal.getAmount());
                            }
                        }
                    }
                    if (npcs[i].endGfx > 0) {
                        c.gfx100(npcs[i].endGfx);
                    }
                    int poisonDamage = getPoisonDamage(npcs[i]);
                    if (poisonDamage > 0 && Misc.random(10) == 1) {
                        c.getHealth().proposeStatus(HealthStatus.POISON, poisonDamage, Optional.of(npcs[i]));
                    }
                    if (c.getHealth().getCurrentHealth() - damage < 0
                            || secondDamage > -1 && c.getHealth().getCurrentHealth() - secondDamage < 0) {
                        damage = c.getHealth().getCurrentHealth();
                        if (secondDamage > -1) {
                            secondDamage = 0;
                        }
                    }
                    handleSpecialEffects(c, i, damage);
                    c.logoutDelay = System.currentTimeMillis();
                    if (damage > -1) {
                        c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                        c.addDamageTaken(npcs[i], damage);
                    }
                    if (secondDamage > -1) {
                        c.appendDamage(secondDamage, secondDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
                        c.addDamageTaken(npcs[i], secondDamage);
                    }
                    if (damage > 0 || secondDamage > 0) {
                        c.getCombat().appendVengeanceNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
                        c.getCombat().applyRecoilNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
                    }
                    int rol = c.getHealth().getCurrentHealth() - damage;
                    if (rol > 0 && rol < c.getHealth().getMaximumHealth() / 10) {
                        ringOfLife(c);
                    }
                    switch (npcs[i].npcType) {
                        // Abyssal sire
                        case 5890:
                            int health = npcs[i].getHealth().getCurrentHealth();
                            c.sireHits++;
                            int randomAmount = Misc.random(5);
                            switch (c.sireHits) {
                                case 10:
                                case 20:
                                case 30:
                                case 40:
                                    for (int id = 0; id < randomAmount; id++) {
                                        int x = npcs[i].absX + Misc.random(2);
                                        int y = npcs[i].absY - Misc.random(2);
                                        newNPC(5916, x, y, 0, 0, 15, 15, 100, 0);
                                    }
                                    break;

                                case 45:
                                    c.sireHits = 0;
                                    break;

                            }
                            if (health < 400 && health > 329 || health < 100) {
                                npcs[i].attackType = CombatType.MELEE;
                            }
                            if (health < 330 && health > 229) {
                                npcs[i].attackType = CombatType.MAGE;
                            }
                            if (health < 230 && health > 99) {
                                npcs[i].attackType = CombatType.SPECIAL;
                                npcs[i].getHealth().increase(6);
                            }
                            break;

                        case 8028:
                            if (Vorkath.attackStyle == 0) {
                                npcs[i].attackType = CombatType.MAGE;
                            }
                            break;
                        case 6607:
                            if (Misc.random(12) == 4) {
                                c.teleportToX = 3236;
                                c.teleportToY = 3620;
                            }
                            npcs[i].attackType = CombatType.MAGE;

                            break;
                        /**
                         * Hunllef
                         */
                        case 9021: //melee
                        case 9022: //range
                        case 9023: //mage
                            if (damage >= 100) {
                            }
                            if (damage == 0) {
                                if (c.totalMissedHunllefHits >= 6) {
                                    c.totalMissedHunllefHits = 0;
                                }
                                c.totalMissedHunllefHits++;
                            }
                            if (c.totalMissedHunllefHits == 6) {
                                c.totalMissedHunllefHits = 0;

                                switch (npcs[i].attackType) {
                                    case MELEE:
                                        switch (Misc.random(1)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                        }
                                        break;
                                    case MAGE:
                                        switch (Misc.random(1)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                        }
                                        break;
                                    case RANGE:
                                        switch (Misc.random(1)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                        }
                                        break;

                                    default:
                                        break;
                                }
                                break;
                            }
                        case 7144:
                        case 7145:
                        case 7146:
                            if (damage >= 50) {
                            }
                            if (damage == 0) {
                                if (c.totalMissedGorillaHits >= 6) {
                                    c.totalMissedGorillaHits = 0;
                                }
                                c.totalMissedGorillaHits++;
                            }
                            if (c.totalMissedGorillaHits == 6) {
                                c.totalMissedGorillaHits = 0;

                                switch (npcs[i].attackType) {
                                    case MELEE:
                                        switch (Misc.random(2)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.SPECIAL;
                                                break;
                                            case 2:
                                                npcs[i].attackType = CombatType.RANGE;
                                                break;
                                        }
                                        break;
                                    case MAGE:
                                        switch (Misc.random(2)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.SPECIAL;
                                                break;
                                            case 2:
                                                npcs[i].attackType = CombatType.RANGE;
                                                break;
                                        }
                                        break;
                                    case RANGE:
                                        switch (Misc.random(2)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.SPECIAL;
                                                break;
                                            case 2:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                        }
                                        break;
                                    case SPECIAL:
                                        switch (Misc.random(2)) {
                                            case 0:
                                                npcs[i].attackType = CombatType.MAGE;
                                                break;
                                            case 1:
                                                npcs[i].attackType = CombatType.MELEE;
                                                break;
                                            case 2:
                                                npcs[i].attackType = CombatType.RANGE;
                                                break;
                                        }
                                        break;

                                    default:
                                        break;
                                }
                                break;
                            }
                            c.setUpdateRequired(true);
                    }
                }
            }
        }
    }

    /**
     * Poison damage
     *
     * @param npc the npc whom can be poisonous
     * @return the amount of damage the poison will begin on
     */
    private int getPoisonDamage(NPC npc) {
        switch (npc.npcType) {
            case 3129:
                return 16;
            case 319:
                return 0;

            case 3021:
                return 5;

            case 957:
                return 4;

            case 959:
                return 6;

            case 6615:
                return 10;
        }
        return 0;
    }


    public static Optional<AlchemicalHydra> getHydraInstance(int index) {
        if (npcs[index].getInstance() != null && npcs[index].getInstance() instanceof AlchemicalHydra)
            return Optional.ofNullable((AlchemicalHydra) npcs[index].getInstance());
        return Optional.empty();
    }

    /**
     * Multi attacks from a distance
     *
     * @param npc the npc whom can pefrom multiattacks from a distance
     * @return the distance that the npc can reach from
     */
    private int multiAttackDistance(NPC npc) {
        if (npc == null) {
            return 0;
        }
        switch (npc.npcType) {
            case 319:
            case 239:
            case 8031:
            case 8030:
                return 35;
            case 7554:
                return 30;
            case Sotetseg.NPC_ID:
            case FragmentOfSeren.NPC_ID:
                return 25;
        }
        return 15;
    }

    /**
     * Multi attack damage
     *
     * @param i the damage set
     */
    public void multiAttackDamage(int i) {
        int damage = Misc.random(getMaxHit(i));
        Hitmark hitmark = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Player c = PlayerHandler.players[j];
                if (c.isDead || c.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].isInvisible()) {
                    continue;
                }
                if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY,
                        multiAttackDistance(npcs[i]))) {
                    if (npcs[i].attackType == CombatType.SPECIAL) {
                        if (npcs[i].npcType == 5862) {
                            if (cerberusGroundCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 6618) {
                            if (archSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 8609) {
                            if (hydraPoisonCoordinates.stream().noneMatch(p -> p[0] == c.getX() && p[1] == c.getY())) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 7566) {
                            if (vasaRockCoordinates.parallelStream().noneMatch(p -> p[0] == c.getX() && p[1] == c.getY())) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 8030) {
                            if (DragonGroundCoordinates.stream().noneMatch(p -> p[0] == c.getX() && p[1] == c.getY())) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 6766) {
                            if (explosiveSpawnCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }

                        if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
                            if (!Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
                                return;
                            }
                        }
                        if (npcs[i].npcType == 6619) {
                            if (fanaticSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
                            if (!(c.absX > npcs[i].absX - 5 && c.absX < npcs[i].absX + 5 && c.absY > npcs[i].absY - 5
                                    && c.absY < npcs[i].absY + 5)) {
                                continue;
                            }
                            c.sendMessage(
                                    "Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
                            createVetionEarthquake(c);
                        }
                        if (npcs[i].npcType == 319) {
                            if (corpSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }
                        c.appendDamage(damage, hitmark);
                    } else if (npcs[i].attackType == CombatType.DRAGON_FIRE) {
                        int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
                                || c.getItems().isWearingItem(11284) ? 1 : 0;
                        if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
                            resistance++;
                        }
                        //	c.sendMessage("Resistance: " + resistance);
                        if (resistance == 0) {
                            damage = Misc.random(getMaxHit(i));
                            c.sendMessage("You are badly burnt by the dragon fire!");
                        } else if (resistance == 1)
                            damage = Misc.random(15);
                        else if (resistance == 2)
                            damage = 0;
                        if (c.getHealth().getCurrentHealth() - damage < 0)
                            damage = c.getHealth().getCurrentHealth();
                        c.gfx100(npcs[i].endGfx);
                        c.appendDamage(damage, hitmark);
                    } else if (npcs[i].attackType == CombatType.MAGE) {
                        if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
                            if (vetionSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
                                continue;
                            }
                        }
                        if (npcs[i].npcType == 3162) {
                            damage /= 3;
                        }
                        if (!c.protectingMagic()) {
                            if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
                                c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                            } else {
                                c.appendDamage(0, Hitmark.MISS);
                            }
                        } else {
                            switch (npcs[i].npcType) {
                                case 1046:
                                case 3162:
                                case 6610:
                                case 6611:
                                case 6612:
                                case Sotetseg.NPC_ID: //Magic 60%,
                                case FragmentOfSeren.NPC_ID:
                                    damage *= .6;
                                    break;
                                case 7554:
                                    if (c.protectingMagic())
                                        damage /= 2;
                                    break;
                                case 319:
                                    if (c.protectingMagic())
                                        damage /= 2;
                                    break;
                                default:
                                    damage = 0;
                                    break;
                            }
                            c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                        }
                    } else if (npcs[i].attackType == CombatType.RANGE) {
                        if (!c.protectingRange()) {
                            if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
                                c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
                            } else {
                                c.appendDamage(0, Hitmark.MISS);
                            }
                            if (npcs[i].npcType == 2215) {
                                damage /= 1;
                            }
                        } else {
                            switch (npcs[i].npcType) {
                                default:
                                    damage = 0;
                                    break;
                            }
                            c.appendDamage(damage, Hitmark.MISS);
                        }
                    }
                    if (npcs[i].endGfx > 0) {
                        c.gfx0(npcs[i].endGfx);
                    }
                    c.getCombat().appendVengeanceNPC(damage, i);
                }
            }
        }
    }

    /**
     * Gets pulled
     *
     * @param i the npc to be pulled
     * @return return true if it can, false otherwise
     */
    public boolean getsPulled(int i) {
        switch (npcs[i].npcType) {
            case 655:

                if (npcs[i].firstAttacker > 0)
                    return false;
                break;
        }
        return true;
    }

    /**
     * Multi attacks
     *
     * @param i the npc whom can perform multi attacks
     * @return true if it can, false otherwise
     */
    public boolean multiAttacks(int i) {
        switch (npcs[i].npcType) {
            case 7554:
                return npcs[i].attackType == CombatType.SPECIAL || npcs[i].attackType == CombatType.MAGE ||
                        npcs[i].attackType == CombatType.RANGE;
            case FragmentOfSeren.NPC_ID:
                return true;
            case 6611:
            case 6612:
            case 6618:
            case 6619:
            case 319:
            case 6766:
            case 7617:
                return npcs[i].attackType == CombatType.SPECIAL || npcs[i].attackType == CombatType.MAGE;
            case 8609:
                return npcs[i].attackType == CombatType.SPECIAL;
            case Sotetseg.NPC_ID:
                return npcs[i].attackType == CombatType.MAGE ? true : false;
            case 7604:
            case 7605:
            case 7606:
            case 8781:
                return npcs[i].attackType == CombatType.SPECIAL;

            case 1046:
                return npcs[i].attackType == CombatType.MAGE
                        || npcs[i].attackType == CombatType.SPECIAL && Misc.random(3) == 0;
            case 6610:
                return npcs[i].attackType == CombatType.MAGE;
            case 2558:
                return true;
            case 2562:
                if (npcs[i].attackType == CombatType.MAGE)
                    return true;
            case 2215:
                return npcs[i].attackType == CombatType.RANGE;
            case 3162:
                return npcs[i].attackType == CombatType.MAGE;
            case 963:
            case 965:
                return npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE;
            default:
                return false;
        }

    }

    /**
     * Npc killer id
     *
     * @param npcId the npc whom were grabbing the id from
     * @return the killeId to be printed
     */
    public int getNpcKillerId(int npcId) {
        int oldDamage = 0;
        int killerId = 0;
        for (int p = 1; p < Config.MAX_PLAYERS; p++) {
            if (PlayerHandler.players[p] != null) {
                if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
                    if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
                        oldDamage = PlayerHandler.players[p].totalDamageDealt;
                        killerId = p;
                    }
                    PlayerHandler.players[p].totalDamageDealt = 0;
                }
            }
        }
        return killerId;
    }

    /**
     * Barrows kills
     *
     * @param i the barrow brother whom been killed
     */
    void killedBarrow(int i) {
        if (npcs[i] == null)
            return;
        Player player = PlayerHandler.players[npcs[i].killedBy];
        if (player != null && player.getBarrows() != null) {
            Optional<Brother> brother = player.getBarrows().getBrother(npcs[i].npcType);
            if (brother.isPresent()) {
                brother.get().handleDeath();
            } else if (Boundary.isIn(npcs[i], Barrows.TUNNEL)) {
                if (player.getBarrows().getKillCount() < 25) {
                    player.getBarrows().increaseMonsterKilled();
                }
            }
        }
    }

    /**
     * Godwars kill
     *
     * @param npc the godwars npc whom been killed
     */
    void handleGodwarsDeath(NPC npc) {
        Player player = PlayerHandler.players[npc.killedBy];
        if (!GodwarsNPCs.NPCS.containsKey(npc.npcType)) {
            return;
        }
        if (player != null && player.getGodwars() != null) {
            player.getGodwars().increaseKillcount(GodwarsNPCs.NPCS.get(npc.npcType));
            /*
             * if (Misc.random(60 + 10 * player.getItems().getItemCount(Godwars.KEY_ID,
             * true)) == 1) { /** Key will not drop if player owns more than 3 keys already
             *
             * int key_amount =
             * player.getDiaryManager().getWildernessDiary().hasCompleted("ELITE") ? 6 : 3;
             * if (player.getItems().getItemCount(Godwars.KEY_ID, true) > key_amount) {
             * return; } Server.itemHandler.createGroundItem(player, Godwars.KEY_ID,
             * npc.getX(), npc.getY(), player.heightLevel, 1, player.getIndex()); }
             */
        }
    }

    /**
     * Handles kills towards the achievement diaries
     *
     * @param npc The npc killed.
     */
    void handleDiaryKills(NPC npc) {
        Player player = PlayerHandler.players[npc.killedBy];

        if (player != null) {
            AchievementDiaryKills.kills(player, npc.npcType);
        }
    }

    /**
     * Handles kills towards daily tasks
     *
     * @param npc The npc killed.
     */
    void handleDailyKills(NPC npc) {
        Player player = PlayerHandler.players[npc.killedBy];

        if (player != null) {
            Ranked.Ranked.kills(player, npc.npcType);
        }
    }

    /**
     * Tzhaar kill
     *
     * @param player the player who killed a tzhaar
     * @param i      the tzhaar to be killed
     */
    void tzhaarDeathHandler(Player player, int i) {// hold a vit plz
        if (npcs[i] != null) {
            if (player != null) {
                if (player.getFightCave() != null) {
                    if (isFightCaveNpc(i))
                        killedTzhaar(player, i);
                }
            }
        }
    }

    void infernoDeathHandler(Player player, int i) {// hold a vit plz
        if (npcs[i] != null) {
            if (player != null) {
                if (player.getInferno() != null) {
                    if (isInfernoNpc(i))
                        killedInferno(player, i);
                }
            }
        }
    }

    void xericDeathHandler(Player player, int i) {// hold a vit plz
        if (player != null) {
            if (player.getXeric() != null) {
                if (isXericNpc(i))
                    killedXeric(player, i);
            }
        }
    }

    private void killedXeric(Player player, int i) {
        if (Boundary.isIn(player, Boundary.XERIC)) {
            if (player.getXeric() != null) {
                player.getXeric().setKillsRemaining(player.getXeric().getKillsRemaining() - 1);
                player.getXeric().getSpawns().remove(npcs[i]);
                if (player.getXeric().getKillsRemaining() == 0) {
                    player.getXeric().incWaveID();
                    player.getXeric().spawn();
                }
            }
        }
    }

    /**
     * Killed tzhaar
     *
     * @param player the player who killed a tzhaar
     * @param i      the tzhaar to be killed
     */
    private void killedTzhaar(Player player, int i) {
        if (player.getFightCave() != null) {
            player.getFightCave().setKillsRemaining(player.getFightCave().getKillsRemaining() - 1);
            if (player.getFightCave().getKillsRemaining() == 0) {
                player.waveId++;
                player.getFightCave().spawn();
            }
        }
    }

    private void killedInferno(Player player, int i) {
        if (player.getInferno() != null) {
            player.getInferno().setKillsRemaining(player.getInferno().getKillsRemaining() - 1);
            if (player.getInferno().getKillsRemaining() == 0) {
                player.getInferno().setInfernoWaveId(player.getInferno().getInfernoWaveId() + 1);
                System.out.println("Inferno Wave ID: " + player.getInferno().getInfernoWaveId());
                player.getInferno().spawn();
            }
        }
    }

    /**
     * Jad kill
     *
     * @param i
     */
    public void handleJadDeath(int i) {
        Player c = PlayerHandler.players[npcs[i].spawnedBy];
        c.getItems().addItem(6570, 1);
        c.getFightCave();
        // c.getDH().sendDialogues(69, 2617);
        c.getFightCave().stop();
        c.waveId = 300;
    }

    public boolean isBone(int item) {
        switch (item) {
            case 526:
            case 532:
            case 536:
            case 592:
                return true;
            default:
                return false;
        }
    }

    /**
     * Dropping items
     *
     * @param i
     */
    public void dropItems(int i) {
        Player c = PlayerHandler.players[npcs[i].killedBy];
        if (c != null) {
            if (c.getTargeted() != null && npcs[i].equals(c.getTargeted())) {
                c.setTargeted(null);
                c.getPA().sendEntityTarget(0, npcs[i]);
            }
            c.getAchievements().kill(npcs[i]);
            PetHandler.receive(c, npcs[i]);

            boolean dropPkp = Misc.random(2) == 0;
            int combatLevel = npcs[i].getDefinition().getNpcCombat();
            if (dropPkp && combatLevel >= 50) {
                c.getItems().addItemUnderAnyCircumstance(2996, 4);
            }

            if (npcs[i].npcType >= 1610 && npcs[i].npcType <= 1612) {
                //c.setArenaPoints(c.getArenaPoints() + 1);
                c.refreshQuestTab(4);
                //c.sendMessage("@red@You gain 1 point for killing the Mage! You now have " + c.getArenaPoints()
                //+ " Arena Points.");
            }
            if (npcs[i].npcType == 1673 || npcs[i].npcType == 1674 || npcs[i].npcType == 1677 || npcs[i].npcType == 1676 || npcs[i].npcType == 1675
                    || npcs[i].npcType == 1672) { //barrows npcs
                if (c.eventFinished == false && RandomEvent.eventNumber == 11) {
                    c.eventStage += 1;
                }
                if (c.eventStage == 50 && c.eventFinished == false && RandomEvent.eventNumber == 11) {
                    c.sendMessage("@blu@You have completed the event challenge: @red@Kill 50 barrows brothers.");
                    c.sendMessage("@blu@You receive @red@1 @blu@Event Point for completing the Event Challenge.");
                    c.eventPoints += 1;
                    c.eventStage = 0;
                    c.eventFinished = true;
                }
                if (EquipmentSet.AHRIM.isWearingBarrows(c) || EquipmentSet.KARIL.isWearingBarrows(c)
                        || EquipmentSet.DHAROK.isWearingBarrows(c)
                        || EquipmentSet.VERAC.isWearingBarrows(c)
                        || EquipmentSet.GUTHAN.isWearingBarrows(c)
                        || EquipmentSet.TORAG.isWearingBarrows(c)) {
                    c.getDiaryManager().getMorytaniaDiary().progress(MorytaniaDiaryEntry.BARROWS_CHEST);
                }
            }
            if (npcs[i].npcType == 2266 || npcs[i].npcType == 2267 || npcs[i].npcType == 2265) {
                c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.KILL_DAGANNOTH_KINGS);
            }
            if (npcs[i].npcType == 411) {
                c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.KILL_KURASK);
            }
            if (npcs[i].npcType == 9021 || npcs[i].npcType == 9022 || npcs[i].npcType == 9023 || npcs[i].npcType == 9024) {

                c.hunllefDead = true;
                PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ Hunllef has been defeated by @bla@" + c.playerName + "@blu@!");
                Hunllef.rewardPlayers(c);
            }
            if (npcs[i].npcType == 1047) {
                c.getDiaryManager().getMorytaniaDiary().progress(MorytaniaDiaryEntry.KILL_CAVE_HORROR);
            }
            if (npcs[i].npcType == 1673 || npcs[i].npcType == 1674 || npcs[i].npcType == 1677 || npcs[i].npcType == 1676 || npcs[i].npcType == 1675
                    || npcs[i].npcType == 1672) {
                Achievements.increase(c, AchievementType.BARROWS_KILLS, 1);
            }
            if (npcs[i].npcType == 1673) { //barrows npcs
                Achievements.increase(c, AchievementType.DH_KILLS, 1);
            }
            if (npcs[i].npcType == 5744 || npcs[i].npcType == 5762) {
                c.setShayPoints(c.getShayPoints() + 1);
                c.sendMessage("@red@You gain 1 point for killing the Penance! You now have " + c.getShayPoints()
                        + " Assault Points.");
            }
            if (Misc.linearSearch(DropManager.wildybossesforgiveaway, npcs[i].npcType) != -1) {
                if (Misc.random(350) == 1) {
                    if (c.summonId == 2269 && c.goblinFilter == true) {
                        c.getItems().addItemToBank(4185, 1);
                        c.sendMessage("@yel@The pet goblin has picked up your @red@Seren key@yel@.");
                        NPC GOBLIN = NPCHandler.getNpc(2269);
                        NPCHandler.npcs[GOBLIN.getIndex()].forceChat("oo some juicy loot!");
                        NPCHandler.npcs[GOBLIN.getIndex()].gfx100(626);
                    }
                    if (!(c.summonId == 2269)) {
                        c.getItems().addItemUnderAnyCircumstance(4185, 1);
                    }
                }
                if (Misc.random(100) == 1) {
                    if (c.summonId == 2269 && c.goblinFilter == true) {
                        c.getItems().addItemToBank(23490, 1);
                        c.sendMessage("@yel@The pet goblin has picked up your @red@Seren key@yel@.");
                        NPC GOBLIN = NPCHandler.getNpc(2269);
                        NPCHandler.npcs[GOBLIN.getIndex()].forceChat("oo some juicy loot!");
                        NPCHandler.npcs[GOBLIN.getIndex()].gfx100(626);
                    }
                    if (!(c.summonId == 2269)) {
                        c.getItems().addItemUnderAnyCircumstance(23490, 1);
                        c.sendMessage("@bla@You notice a @blu@Larrans rystal key@bla@ on the floor.");
                    }
                }
                if (Misc.random(450) == 1) {
                    if (c.summonId == 2269 && c.goblinFilter == true) {
                        c.getItems().addItemToBank(6792, 1);
                        c.sendMessage("@yel@The pet goblin has picked up your @red@Seren key@yel@.");
                        NPC GOBLIN = NPCHandler.getNpc(2269);
                        NPCHandler.npcs[GOBLIN.getIndex()].forceChat("oo some juicy loot!");
                        NPCHandler.npcs[GOBLIN.getIndex()].gfx100(626);
                    }
                    if (!(c.goblinFilter == true)) {
                        Server.itemHandler.createGroundItem(c, 6792, c.getX(), c.getY(), c.getHeight(), 1, c.getIndex());
                        PlayerHandler.executeGlobalMessage("@red@[SEREN KEY]@blu@" + c.playerName + "@pur@ has just received Seren Key in the wildy!");
                    }

                }
            }
            if (npcs[i].npcType == 494 && c.eventFinished == false && RandomEvent.eventNumber == 5) {
                c.eventStage += 1;
            }
            if (npcs[i].npcType == 494 && c.eventStage == 5 && c.eventFinished == false && RandomEvent.eventNumber == 5) {
                c.sendMessage("@blu@You have completed the event challenge: @red@kill 5 krakens.");
                c.sendMessage("@blu@You receive @red@1 @blu@Event Point for completing the Event Challenge.");
                c.eventPoints += 1;
                c.eventStage = 0;
                c.eventFinished = true;
            }

            int[] revs = {7931, 7932, 7933, 7934, 7935, 7936, 7937, 7938, 7939, 7940};
            if (npcs[i].npcType == 100 || npcs[i].npcType == 2216 || npcs[i].npcType == 5816 || npcs[i].npcType == 1680 || npcs[i].npcType == 2241
                    || npcs[i].npcType == 2218 || npcs[i].npcType == 2217 || npcs[i].npcType == 3428 || npcs[i].npcType == 3429 || npcs[i].npcType == 970
                    || npcs[i].npcType == 448
                    || npcs[i].npcType == 414 || npcs[i].npcType == 423 || npcs[i].npcType == 446 || npcs[i].npcType == 484
                    || npcs[i].npcType == 268 || npcs[i].npcType == 2834 || npcs[i].npcType == 241
                    || npcs[i].npcType == 2006 || npcs[i].npcType == 2844 || npcs[i].npcType == 484
                    || npcs[i].npcType == 2514 || npcs[i].npcType == 6 || npcs[i].npcType == 135 || npcs[i].npcType == 2084
                    || npcs[i].npcType == 264 || npcs[i].npcType == 7931 || npcs[i].npcType == 7932 || npcs[i].npcType == 2026
                    || npcs[i].npcType == 957 || npcs[i].npcType == 492 || npcs[i].npcType == 957 || npcs[i].npcType == 2916
                    || npcs[i].npcType == 6918 || npcs[i].npcType == 6914 || npcs[i].npcType == 2098 || npcs[i].npcType == 7268
                    || npcs[i].npcType == 2235 || npcs[i].npcType == 891 || npcs[i].npcType == 85
                    || npcs[i].npcType == 406 || npcs[i].npcType == 1047 || npcs[i].npcType == 421
                    || npcs[i].npcType == 494 || npcs[i].npcType == 435 || npcs[i].npcType == 417 || npcs[i].npcType == 437 || npcs[i].npcType == 427
                    || npcs[i].npcType == 411 || npcs[i].npcType == 2834 || npcs[i].npcType == 2841
                    || npcs[i].npcType == 2085 || npcs[i].npcType == 7272 || npcs[i].npcType == 7279 || npcs[i].npcType == 7277
                    || npcs[i].npcType == 2844 || npcs[i].npcType == 975 || npcs[i].npcType == 270 || npcs[i].npcType == 7275
                    || npcs[i].npcType == 7274 || npcs[i].npcType == 7273 || npcs[i].npcType == 247
                    || npcs[i].npcType == 419 || npcs[i].npcType == 3169 || npcs[i].npcType == 6604 || npcs[i].npcType == 2212
                    || npcs[i].npcType == 3163 || npcs[i].npcType == 3164 || npcs[i].npcType == 3165 || npcs[i].npcType == 7276
                    || npcs[i].npcType == 3130 || npcs[i].npcType == 2206 || npcs[i].npcType == 2207 || npcs[i].npcType == 2208
                    || npcs[i].npcType == 3131 || npcs[i].npcType == 3132 || npcs[i].npcType == 1673 || npcs[i].npcType == 1672
                    || npcs[i].npcType == 1677 || npcs[i].npcType == 1674 || npcs[i].npcType == 1675 || npcs[i].npcType == 1676
                    || npcs[i].npcType == 2463 || npcs[i].npcType == 2840 || npcs[i].npcType == 7933 || npcs[i].npcType == 1610
                    || npcs[i].npcType == 1611 || npcs[i].npcType == 1612 || npcs[i].npcType > 9026 && npcs[i].npcType < 9031
                    || npcs[i].npcType == 3135 || npcs[i].npcType == 3139 || npcs[i].npcType == 3134 || npcs[i].npcType == 3140
                    || npcs[i].npcType == 3141 || npcs[i].npcType == 3133 || npcs[i].npcType == 3137 || npcs[i].npcType == 2213
                    || npcs[i].npcType == 2209 || npcs[i].npcType == 2211 || npcs[i].npcType == 2210 || npcs[i].npcType == 2212
                    || npcs[i].npcType == 2237 || npcs[i].npcType == 2241 || npcs[i].npcType == 3138 || npcs[i].npcType == 2234
                    || npcs[i].npcType == 2233 || npcs[i].npcType == 2245 || npcs[i].npcType == 3141 || npcs[i].npcType == 3140
                    || npcs[i].npcType == 3135 || npcs[i].npcType == 3139 || npcs[i].npcType == 3133 || npcs[i].npcType == 3135
                    || npcs[i].npcType == 3138 || npcs[i].npcType == 3134 || npcs[i].npcType == 3161 || npcs[i].npcType == 2210
                    || npcs[i].npcType == 2242 || npcs[i].npcType == 2212 || npcs[i].npcType == 3166 || npcs[i].npcType == 3169
                    || npcs[i].npcType == 3167 || npcs[i].npcType == 3168 || npcs[i].npcType == 2244 || npcs[i].npcType == 2234
                    || npcs[i].npcType == 2237 || npcs[i].npcType == 3133 || npcs[i].npcType == 2235 || npcs[i].npcType == 2243) {
                c.pvmp += 1 + Misc.random(1);
                if (c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.eventStage += 1;
                }
                if (Boundary.isIn(c, Boundary.CATACOMBS) && c.getItems().playerHasItem(13116)) {
                    c.prayerPoint += 1;
                }
            }//low tier
            if (npcs[i].npcType == 2919 || npcs[i].npcType == 5944 || npcs[i].npcType == 1543 || npcs[i].npcType == 415 || npcs[i].npcType == 11
                    || npcs[i].npcType == 7145 || npcs[i].npcType == 7144 || npcs[i].npcType == 498 || npcs[i].npcType == 6606
                    || npcs[i].npcType == 259 || npcs[i].npcType == 7935 || npcs[i].npcType == 7939 || npcs[i].npcType == 7940
                    || npcs[i].npcType == 7936 || npcs[i].npcType == 7934 || npcs[i].npcType == 7937 || npcs[i].npcType == 7938
                    || npcs[i].npcType == 1432 || npcs[i].npcType == 7278 || npcs[i].npcType == 7244
                    || npcs[i].npcType == 6342 || npcs[i].npcType == 465 || npcs[i].npcType == 273 || npcs[i].npcType == 274 || npcs[i].npcType == 499
                    || npcs[i].npcType == 4005 || npcs[i].npcType == 7250 || npcs[i].npcType == 959 || npcs[i].npcType == 8030 || npcs[i].npcType == 8031
                    || npcs[i].npcType > 9032 && npcs[i].npcType < 9034) {
                c.pvmp += 2 + Misc.random(2);
                if (c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.eventStage += 1;
                }
                if (c.eventStage == 150 && c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.sendMessage("@blu@You have completed the event challenge: @red@Gain 150 Pvm Points.");
                    c.sendMessage("@blu@You receive @red@1 @blu@Event Point for completing the Event Challenge.");
                    c.eventPoints += 1;
                    c.eventStage = 0;
                    c.eventFinished = true;
                }

            }//medium tier
            if (npcs[i].npcType == 6611 || npcs[i].npcType == 6609 || npcs[i].npcType == 6615 || npcs[i].npcType == 6610 || npcs[i].npcType == 6593
                    || npcs[i].npcType == 2054 || npcs[i].npcType == 6619 || npcs[i].npcType == 6618
                    || npcs[i].npcType == 2265 || npcs[i].npcType == 2266 || npcs[i].npcType == 2267 || npcs[i].npcType == 8781
                    || npcs[i].npcType == 239 || npcs[i].npcType == 5779 || npcs[i].npcType == 494 || npcs[i].npcType == 963
                    || npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044 || npcs[i].npcType == 5862
                    || npcs[i].npcType == 494 || npcs[i].npcType == 2215 || npcs[i].npcType == 5890 || npcs[i].npcType == 319 || npcs[i].npcType == 6766 || npcs[i].npcType == 8026
                    || npcs[i].npcType == 8027 || npcs[i].npcType == 3162 || npcs[i].npcType == 2205
                    || npcs[i].npcType == 8028 || npcs[i].npcType == 8615 || npcs[i].npcType == 8620 || npcs[i].npcType == 8621 || npcs[i].npcType == 8622
                    || npcs[i].npcType == 9021 || npcs[i].npcType == 9022 || npcs[i].npcType == 9023 || npcs[i].npcType > 9021 && npcs[i].npcType < 9023) {
                c.pvmp += 6 + Misc.random(4);
                if (c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.eventStage += 1;
                }
                if (c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.eventStage += 1;
                }
                if (c.eventStage == 150 && c.eventFinished == false && RandomEvent.eventNumber == 7) {
                    c.sendMessage("@blu@You have completed the event challenge: @red@Gain 150 Pvm Points.");
                    c.sendMessage("@blu@You receive @red@1 @blu@Event Point for completing the Event Challenge.");
                    c.eventPoints += 1;
                    c.eventStage = 0;
                    c.eventFinished = true;
                }

                //HIGH TIER
            }
            if (npcs[i].npcType == 1739 || npcs[i].npcType == 1740 || npcs[i].npcType == 1741 || npcs[i].npcType == 1742) {
                c.pcPoints += 7;
            }


            if (npcs[i].npcType == FragmentOfSeren.NPC_ID && npcs[i].getHealth().getCurrentHealth() <= 0) {
                // Player p = PlayerHandler.players[npcs[i].killedBy];
                int randomPkp = Misc.random(15) + 10;
                c.pkp += randomPkp;
                c.refreshQuestTab(0);
                MonsterHunt.setCurrentLocation(null);
                c.sendMessage("Well done! You killed Seren!");
                c.sendMessage("You received: " + randomPkp + " PK Points for killing the wildy boss.");

            }
            if (npcs[i].npcType == Sotetseg.NPC_ID && npcs[i].getHealth().getCurrentHealth() <= 0) {
                // Player p = PlayerHandler.players[npcs[i].killedBy];
                int randomPkp = Misc.random(15) + 10;
                c.pkp += randomPkp;
                c.refreshQuestTab(0);
                MonsterHunt.setCurrentLocation(null);
                c.sendMessage("Well done! You killed Sotetseg!");
                c.sendMessage("You received: " + randomPkp + " PK Points for killing the wildy boss.");
            }
            int dropX = npcs[i].absX;
            int dropY = npcs[i].absY;
            int dropHeight = npcs[i].heightLevel;
            /*
             * if (npcs[i].npcType == 494) { if (Boundary.isIn(c, Boundary.KRAKEN_CAVE)) {
             * dropX = c.absX; dropY = c.absY; } else { dropX = 1770; dropY = 3426; } }
             */
            if (npcs[i].npcType == 492 || npcs[i].npcType == 494) {
                dropX = c.absX;
                dropY = c.absY;
            }
            if (npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044
                    || npcs[i].npcType == 6720) {
                dropX = 2268;
                dropY = 3069;
                c.getZulrahEvent().stop();
            }
            if (npcs[i].npcType == Kraken.KRAKEN_ID) {
                dropX = 2280;
                dropY = 10031;
                c.getKraken().handleKill();
            }
            /**
             * Warriors guild
             */
            c.getWarriorsGuild().dropDefender(npcs[i].absX, npcs[i].absY);
            if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {

                if (npcs[i].getX() == 2851 && npcs[i].getY() == 3536) {
                    dropX = 2851;
                    dropY = 3537;
                    AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY + 1);
                } else if (npcs[i].getX() == 2857 && npcs[i].getY() == 3536) {
                    dropX = 2857;
                    dropY = 3537;
                    AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY + 1);
                } else {
                    AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY);
                }
            }
            Location3D location = new Location3D(dropX, dropY, dropHeight);
            int amountOfDrops = 1;
            if (Config.DOUBLE_DROPS && c.getLastIncentive() > 0
                    && (System.currentTimeMillis() - c.getLastIncentive()) < TimeUnit.DAYS.toMillis(1)) {
                amountOfDrops++;
            }
            Server.getDropManager().create(c, npcs[i], location, amountOfDrops);
            if (NPCDefinitions.get(npcs[i].npcType).getNpcCombat() >= 100) {
                c.getNpcDeathTracker().add(NPCDefinitions.get(npcs[i].npcType).getNpcName());
            }
        }
    }

    public void appendKillCount(int i) {
        if (npcs[i] == null)
            return;
        Optional<Player> plrOpt = PlayerHandler.getPlayerByIndex(npcs[i].killedBy);
        plrOpt.ifPresent(c -> {
            int[] kcMonsters = {122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565};
            for (int j : kcMonsters) {
                if (npcs[i].npcType == j) {
                    if (c.killCount < 20) {
                        // c.killCount++;
                        // c.sendMessage("Killcount: " + c.killCount);
                    } else {
                        // c.sendMessage("You already have 20 kill count");
                    }
                    break;
                }
            }

        });
    }

    public void appendBossKC(int i) {
        NPC npc = npcs[i];
        if (npc == null || npc.killedBy < 0)
            return;
        Player player = PlayerHandler.players[npcs[i].killedBy];

        if (npc == null || player == null) {
            return;
        }
        if (npc.getDefinition().getNpcCombat() >= 170) {
            Achievements.increase(player, AchievementType.SLAY_BOSSES, 1);
        }
    }

    /**
     * Resets players in combat
     */
    public static NPC getNpc(int npcType) {
        for (NPC npc : npcs)
            if (npc != null && npc.npcType == npcType)
                return npc;
        return null;
    }

    public static NPC spawnNpc(InstancedArea instance, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                               int attack, int defence) {
// first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.setInstance(instance);
        npcs[slot] = newNPC;

        return newNPC;
    }

    public static NPC spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                               int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;

        return newNPC;
    }

    public static NPC spawn(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
                            int defence, boolean attackPlayer) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
        return newNPC;
    }


    public void resetPlayersInCombat(int i) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null)
                if (PlayerHandler.players[j].underAttackByNpc == i)
                    PlayerHandler.players[j].underAttackByNpc = 0;
        }
    }

    public static NPC getNpc(int npcType, int x, int y) {
        for (NPC npc : npcs)
            if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y)
                return npc;
        return null;
    }

    public static NPC getNpc(int npcType, int x, int y, int height) {
        for (NPC npc : npcs) {
            if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y && npc.heightLevel == height) {
                return npc;
            }
        }
        return null;
    }

    public static NPC getNpc(int npcType, int height) {
        for (NPC npc : npcs) {
            if (npc != null && npc.npcType == npcType && npc.heightLevel == height) {
                return npc;
            }
        }
        return null;
    }

    /**
     * Npc names
     **/

    public String getNpcName(int npcId) {
        if (npcId <= -1) {
            return "None";
        }
        if (NPCDefinitions.getDefinitions()[npcId] == null) {
            return "None";
        }
        return NPCDefinitions.getDefinitions()[npcId].getNpcName();
    }

    /**
     * Npc Follow Player
     **/
    public int GetMove(int Place1, int Place2) {
        if ((Place1 - Place2) == 0) {
            return 0;
        } else if ((Place1 - Place2) < 0) {
            return 1;
        } else if ((Place1 - Place2) > 0) {
            return -1;
        }
        return 0;
    }

    public boolean followPlayer(int i) {
        switch (npcs[i].npcType) {
            case 2042:
            case 2043:
            case 2044:
            case 492:
            case 494:
            case 5535:
            case 2892:
            case 2894:
            case 1739:
            case 7413:
            case 1740:
            case 1741:
            case 1742:
            case 7288:
            case 7290:
            case 7292:
            case 7294:
            case 5867:
            case FragmentOfSeren.CRYSTAL_WHIRLWIND:
            case 5868:
            case 5869:
            case 7560:
            case 7559:
                return false;
        }
        return true;
    }

    public void stepAway2(Player player, int i) {
        int[][] points = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] k : points) {
            int dir = NPCClipping.getDirection(k[0], k[1]);
            if (NPCDumbPathFinder.canMoveTo(npcs[i], dir)) {
                NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].absX + k[0], npcs[i].absY + k[1]);
                break;
            }
        }
    }

    public void followPlayer(int i, int playerId) {
        if (PlayerHandler.players[playerId] == null) {
            return;
        }

        Player player = PlayerHandler.players[playerId];
        CombatScript script = npcs[i].getCombatScript();
        if (script != null) {
            script.process(npcs[i], player);
            return;
        }

        if (PlayerHandler.players[playerId].respawnTimer > 0) {
            npcs[i].facePlayer(0);
            npcs[i].randomWalk = true;
            npcs[i].underAttack = false;
            return;
        }

        if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
            if (!Boundary.isIn(player, Boundary.CORPOREAL_BEAST_LAIR)) {
                npcs[i].killerId = 0;
                return;
            }
        }

        if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
            if (!Boundary.isIn(player, Boundary.CORPOREAL_BEAST_LAIR)) {
                npcs[i].killerId = 0;
                return;
            }
        }

        if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
            if (!Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)) {
                npcs[i].killerId = 0;
                return;
            }
        }

        if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY)
                && (npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044 || npcs[i].npcType == 6720)) {
            return;
        }

        if (npcs[i].npcType == InfernoWave.JAL_NIB) {
            npcs[i].facePlayer(playerId);
            return;
        }

        if (npcs[i].npcType == FragmentOfSeren.CRYSTAL_WHIRLWIND) {
            return;
        }

        npcs[i].facePlayer(playerId);

        if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 7413
                || npcs[i].npcType >= 7288 && npcs[i].npcType <= 7294) {
            return;
        }

        int playerX = PlayerHandler.players[playerId].absX;
        int playerY = PlayerHandler.players[playerId].absY;
        npcs[i].randomWalk = false;
        int followDistance = followDistance(i);
        double distance = ((double) distanceRequired(i)) + (npcs[i].getSize() > 1 ? 0.5 : 0.0);

        if (npcs[i].insideOf(player.absX, player.absY)) {
            stepAway2(player, i);
            npcs[i].randomWalk = false;
            npcs[i].facePlayer(player.getIndex());
            return;
        }

        if (npcs[i].getDistance(playerX, playerY) <= distance)
            return;

        if ((npcs[i].spawnedBy > 0) || (npcs[i].absX < npcs[i].makeX + followDistance)
                && (npcs[i].absX > npcs[i].makeX - followDistance)
                && (npcs[i].absY < npcs[i].makeY + followDistance)
                && (npcs[i].absY > npcs[i].makeY - followDistance)) {
            if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
                if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
                    NPCDumbPathFinder.follow(npcs[i], player);
                }
            }
        } else {
            npcs[i].facePlayer(0);
            npcs[i].randomWalk = true;
            npcs[i].underAttack = false;
            npcs[i].walkingHome = true;
            npcs[i].killerId = -1;
        }
    }

    public void loadSpell(Player player, int i) {
        int chance = 0;
        switch (npcs[i].npcType) {
            case 7931:
            case 7932:
            case 7933:
            case 7934:
            case 7935:
            case 7936:
            case 7937:
            case 7938:
            case 7939:
            case 7940:

                if (npcs[i].getHealth().getCurrentHealth() < npcs[i].getHealth().getMaximumHealth() / 2) {
                    int healchance = Misc.random(100);
                    if (healchance > 75) {
                        npcs[i].gfx0(1196);
                        npcs[i].getHealth().setCurrentHealth(npcs[i].getHealth().getCurrentHealth() + (npcs[i].getHealth().getMaximumHealth() / 4));
                        player.sendMessage("The revenant drains power from within and heals.");
                        return;
                    }

                    int randomHit = Misc.random(15);
                    boolean distance = !player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 5);
                    if (randomHit <= 5 && !distance) {
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                    } else if (randomHit > 5 && randomHit <= 10 || distance) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].projectileId = 1415;
                        npcs[i].endGfx = -1;
                    } else if (randomHit > 10 && randomHit <= 15 || distance) {
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].projectileId = 1415;
                        npcs[i].endGfx = -1;
                    }
                }

                break;
            case 1605:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 78;
                break;
            case 5890:

                if (npcs[i].attackType != null) {
                    switch (npcs[i].attackType) {
                        case MAGE:
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = 1274;
                            break;
                        case MELEE:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;
                        case SPECIAL:
                            npcs[i].attackType = CombatType.SPECIAL;
                            groundAttack(npcs[i], player, -1, 1284, -1, 5);
                            npcs[i].attackTimer = 8;
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;
                        default:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;

                    }
                }
                break;
            case 8028:
                if (npcs[i].attackType != null) {
                    switch (npcs[i].attackType) {
                        case MAGE:
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = 393;
                            break;
                        case MELEE:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;
                        case SPECIAL:
                            npcs[i].attackType = CombatType.SPECIAL;
                            groundAttack(npcs[i], player, -1, 1284, -1, 5);
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;

                        default:
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = 393;
                            break;

                    }

                }
                break;
            case 7706:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].hitDelayTimer = 5;
                npcs[i].projectileId = 1375;
                npcs[i].endGfx = -1;

                break;
            case 9021:
            case 9022:
            case 9023://gfxstuff
                if (npcs[i].attackType != null)
                    switch (npcs[i].attackType) {
                        case MAGE:
                            npcs[i].attackType = CombatType.MAGE;
                            groundSpell(npcs[i], player, 280, 281, "vetion", 4);
                            break;

                        case MELEE:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;

                        default:
                            break;
                    }
                break;

            /**
             * Demonic Gorillas attack
             */
            case 7144:
            case 7145:
            case 7146:
                if (npcs[i].attackType != null)
                    switch (npcs[i].attackType) {
                        case MAGE:
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].endGfx = 1304;
                            npcs[i].projectileId = 1305;
                            break;

                        case MELEE:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;

                        case RANGE:
                            npcs[i].attackType = CombatType.RANGE;
                            npcs[i].endGfx = 1303;
                            npcs[i].projectileId = 1302;
                            break;

                        case SPECIAL:
                            npcs[i].attackType = CombatType.SPECIAL;
                            groundAttack(npcs[i], player, 304, 303, 305, 5);
                            npcs[i].attackTimer = 8;
                            npcs[i].hitDelayTimer = 5;
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = -1;
                            break;

                        default:
                            break;
                    }
                break;

            case 320:
                if (npcs[i].getHealth().getStatus() == HealthStatus.POISON) {
                    npcs[i].attackTimer *= 2;
                }
                break;
            case 498:
            case 499:
                npcs[i].projectileId = 642;
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].endGfx = -1;
                break;

            // Zilyana
            case 2205:
                if (Misc.random(4) == 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = -1;
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = -1;
                }
                break;
            case 6607:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 156;
                npcs[i].endGfx = 160;
                break;
            // Growler
            case 2207:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1203;
                break;
            // Bree
            case 2208:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 9;
                break;
            // Saradomin priest
            case 2209:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 76;
                break;
            // Saradomin ranger
            case 2211:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 20;
                npcs[i].endGfx = -1;
                break;
            // Saradomin mage
            case 2212:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 162;
                npcs[i].endGfx = 163;
                npcs[i].setProjectileDelay(2);
                break;
            // Graardor
            case 2215:
                if (Misc.random(4) == 1) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 174;
                    npcs[i].endGfx = -1;
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;

            case 3428:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 249;
                npcs[i].endGfx = -1;
                break;
            // Steelwill
            case 2217:
                npcs[i].projectileId = 1217;
                npcs[i].endGfx = 1218;
                npcs[i].attackType = CombatType.MAGE;
                break;
            // Grimspike
            case 2218:
                npcs[i].projectileId = 1193;
                npcs[i].endGfx = -1;
                npcs[i].attackType = CombatType.RANGE;
                break;
            // Bandos ranger
            case 2242:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1197;
                npcs[i].endGfx = -1;
                break;
            // Bandos mage
            case 2244:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 165;
                npcs[i].endGfx = 166;
                break;
            // Saradomin ranger
            case 3160:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 20;
                npcs[i].endGfx = -1;
                break;
            // Zammorak mage
            case 3161:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 156;
                npcs[i].endGfx = 157;
                npcs[i].setProjectileDelay(2);
                break;
            // Armadyl boss
            case 3162:
                if (Misc.random(2) == 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1200;
                } else {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 1199;
                }
                npcs[i].setProjectileDelay(1);
                break;
            // Skree
            case 3163:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1192;
                npcs[i].endGfx = -1;
                break;
            // Geerin
            case 3164:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1192;
                npcs[i].endGfx = -1;
                break;
            // Armadyl ranger
            case 3167:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1192;
                npcs[i].endGfx = -1;
                break;
            // Armadyl mage
            case 3168:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 159;
                npcs[i].endGfx = 160;
                break;
            // Aviansie
            case 3174:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1193;
                npcs[i].endGfx = -1;
                break;

            case 1672: // Ahrim
                npcs[i].attackType = CombatType.MAGE;
                switch (npcs[i].attackType) {

                    case SPECIAL:
                        npcs[i].projectileId = 156;
                        npcs[i].endGfx = 400;
                        break;

                    case MAGE:
                        npcs[i].projectileId = 156;
                        npcs[i].endGfx = 157;
                        break;

                    default:
                        npcs[i].projectileId = 156;
                        npcs[i].endGfx = 157;
                        break;
                }
                break;
            case 1675: // Karil
                npcs[i].projectileId = 27;
                npcs[i].attackType = CombatType.RANGE;
                break;

            case 2042:
                chance = 1;
                if (player != null) {
                    if (player.getZulrahEvent().getStage() == 9) {
                        chance = 2;
                    }
                }
                chance = Misc.random(chance);
                npcs[i].setFacePlayer(true);
                if (chance < 2) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 97;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].attackTimer = 4;
                } else {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 156;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].attackTimer = 4;
                }
                break;

            case 1610:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 78;
                break;

            case 1611:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 76;
                break;

            case 1612:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 77;
                break;

            case 2044:
                npcs[i].setFacePlayer(true);
                if (Misc.random(3) > 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1046;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].attackTimer = 4;
                } else {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 1044;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].attackTimer = 4;
                }
                break;

            case 983: // Dagannoth mother Air
            case 6373:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 159;
                npcs[i].endGfx = 160;
                break;

            case 984: // Dagannoth mother Water
            case 6375:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 162;
                npcs[i].endGfx = 163;
                break;

            case 985: // Dagannoth mother Fire
            case 6376:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 156;
                npcs[i].endGfx = 157;
                break;

            case 6378: // Mother Earth
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 165;
                npcs[i].endGfx = 166;
                break;

            case 987: // Dagannoth mother range
            case 6377:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 996;
                npcs[i].endGfx = -1;
                break;

            case 6371: // Karamel
                if (Misc.random(10) > 6) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].attackTimer = 6;
                    npcs[i].endGfx = 369;
                    npcs[i].forceChat("Semolina-Go!");
                } else {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].attackTimer = 3;
                    npcs[i].endGfx = -1;
                }
                break;

            case 6372: // Dessourt
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                npcs[i].projectileId = 866;
                npcs[i].endGfx = 865;
                if (Misc.random(10) > 6) {
                    npcs[i].forceChat("Hssssssssss");
                }
                break;

            case 6368: // Culinaromancer
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].attackTimer = 4;
                break;

            case 2043:
                npcs[i].setFacePlayer(false);
                npcs[i].turnNpc(player.getX(), player.getY());
                npcs[i].targetedLocation = new Location3D(player.getX(), player.getY(), player.heightLevel);
                npcs[i].attackType = CombatType.MELEE;
                npcs[i].attackTimer = 9;
                npcs[i].hitDelayTimer = 6;
                npcs[i].projectileId = -1;
                npcs[i].endGfx = -1;
                break;

            case 6611:
            case 6612:
                chance = Misc.random(100);
                int distanceToVet = player.distanceToPoint(npcs[i].absX, npcs[i].absY);
                if (distanceToVet < 3) {
                    if (chance < 25) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].attackTimer = 7;
                        npcs[i].hitDelayTimer = 4;
                        groundSpell(npcs[i], player, 280, 281, "vetion", 4);
                    } else if (chance > 90 && System.currentTimeMillis() - npcs[i].lastSpecialAttack > 15_000) {
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].attackTimer = 5;
                        npcs[i].hitDelayTimer = 2;
                        npcs[i].lastSpecialAttack = System.currentTimeMillis();
                    } else {
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].attackTimer = 5;
                        npcs[i].hitDelayTimer = 2;
                    }
                } else {
                    if (chance < 71) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].attackTimer = 7;
                        npcs[i].hitDelayTimer = 4;
                        groundSpell(npcs[i], player, 280, 281, "vetion", 4);
                    } else if (System.currentTimeMillis() - npcs[i].lastSpecialAttack > 15_000) {
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].attackTimer = 5;
                        npcs[i].hitDelayTimer = 2;
                        npcs[i].lastSpecialAttack = System.currentTimeMillis();
                    } else {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].attackTimer = 7;
                        npcs[i].hitDelayTimer = 4;
                        groundSpell(npcs[i], player, 280, 281, "vetion", 4);
                    }
                }
                break;

            case 6914: // Lizardman, Lizardman brute
            case 6915:
            case 6916:
            case 6917:
            case 6918:
            case 6919:
                int randomAtt = Misc.random(1);
                if (randomAtt == 1) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1291;
                    npcs[i].endGfx = -1;
                    if (Misc.random(10) == 5) {
                        player.getHealth().proposeStatus(HealthStatus.POISON, 3, Optional.of(npcs[i]));
                    }
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;

            /**
             * Lizardman Shaman<
             */
            case 6766:
                int randomAttack3 = Misc.random(100);
                if (randomAttack3 > 9 && randomAttack3 < 90) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].attackTimer = 5;
                    npcs[i].hitDelayTimer = 2;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                } else if (randomAttack3 > 89) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].projectileId = 1293;
                    npcs[i].endGfx = 1294;

                    if (Misc.random(5) == 5) {
                        player.getHealth().proposeStatus(HealthStatus.POISON, 10, Optional.of(npcs[i]));
                    }
                } else {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].attackTimer = 10;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 8;
                    groundSpell(npcs[i], player, -1, 1295, "spawns", 10);
                }
                break;

            /**
             * Crazy Archaeologist
             */
            case 6618:
                int randomAttack = Misc.random(10);
                String[] shout = {"I'm Bellock - respect me!", "Get off my site!", "No-one messes with Bellock's dig!",
                        "These ruins are mine!", "Taste my knowledge!", "You belong in a museum!"};
                String randomShout = (shout[new Random().nextInt(shout.length)]);

                if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) < 2) {
                    npcs[i].forceChat(randomShout);
                    if (randomAttack > 2 && randomAttack < 7) {
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].attackTimer = 5;
                        npcs[i].hitDelayTimer = 2;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                    } else if (randomAttack > 6) {
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 1259;
                        npcs[i].endGfx = 140;
                    } else {
                        npcs[i].forceChat("Rain of knowledge!");
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        npcs[i].hitDelayTimer = 3;
                        groundSpell(npcs[i], player, 1260, 131, "archaeologist", 4);
                    }
                } else {
                    if (randomAttack > 3) {
                        npcs[i].forceChat(randomShout);
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].projectileId = 1259;
                        npcs[i].endGfx = 140;
                    } else {
                        npcs[i].forceChat("Rain of knowledge!");
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        npcs[i].hitDelayTimer = 3;
                        groundSpell(npcs[i], player, 1260, 131, "archaeologist", 4);
                    }
                }
                break;

            /**
             * Chaos fanatic
             */
            case 6619:
                int randomAttack2 = Misc.random(10);
                String[] shout_chaos = {"Burn!", "WEUGH!", "Develish Oxen Roll!",
                        "All your wilderness are belong to them!", "AhehHeheuhHhahueHuUEehEahAH",
                        "I shall call him squidgy and he shall be my squidgy!"};
                String randomShoutChaos = (shout_chaos[new Random().nextInt(shout_chaos.length)]);

                npcs[i].forceChat(randomShoutChaos);

                if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) < 2) {
                    if (randomAttack2 > 2) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 1044;
                        npcs[i].endGfx = 140;
                    } else {
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        npcs[i].hitDelayTimer = 3;
                        groundSpell(npcs[i], player, 1045, 131, "fanatic", 4);
                    }
                } else {
                    if (randomAttack2 > 3) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].projectileId = 1044;
                        npcs[i].endGfx = 140;
                    } else {
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        npcs[i].hitDelayTimer = 3;
                        groundSpell(npcs[i], player, 1045, 131, "fanatic", 4);
                    }
                }
                break;

            case 465:
                boolean distanceToWyvern = player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 3);
                int newRandom = Misc.random(10);
                if (newRandom >= 2) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 258;
                    npcs[i].endGfx = -1;
                } else if (distanceToWyvern && newRandom == 1) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                } else {
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                }
                break;

            /*
             * Hydra combat
             */

            case 8609:

                npcs[i].attack = 150;
                npcs[i].defence = 450;


                if (player.hydraAttackCount == 12) {
                    player.hydraAttackCount = 0;
                    //player.sendMessage("Hydra attack count reset to 0");
                }

                if (player.hydraAttackCount <= 6 && player.countUntilPoison != 20) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1663;
                    npcs[i].endGfx = -1;
                    npcs[i].hitDelayTimer = 4;
                    player.hydraAttackCount++;
                    player.countUntilPoison++;
                    //player.sendMessage("count until poison hits: " + player.countUntilPoison + " [MAGE]");
                    //player.sendMessage("hydra attack counter: " + player.hydraAttackCount + "[MAGE]");
                }
                if (player.hydraAttackCount > 6 && player.hydraAttackCount <= 12 && player.countUntilPoison != 20) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 1662;
                    npcs[i].endGfx = -1;
                    npcs[i].maxHit = 22;
                    player.hydraAttackCount++;
                    player.countUntilPoison++;
                    //player.sendMessage("count until poison hits: " + player.countUntilPoison + " [RANGE]");
                    //player.sendMessage("hydra attack counter: " + player.hydraAttackCount + " [RANGE]");
                }
                if (player.countUntilPoison == 20) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                    player.countUntilPoison = 0;
                    groundSpell(npcs[i], player, 1660, 1655, "hydra", 4);
                    player.getHealth().proposeStatus(HealthStatus.POISON, Misc.random(3, 10), Optional.of(npcs[i]));
                    player.sendMessage("You have been poisoned.");
                }
                break;

            /*
             * Gets the npc id for the Rune Dragon & addy dragon
             */
            case 8031:
            case 8030:
                boolean distanceToDragon = player.goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(), player.getY(), 3);

                int randomAttack1 = Misc.random(12);
                int damage = 0;
                damage = Misc.random(getMaxHit(i));
                int hit = damage;
                int hp = npcs[i].getHealth().getCurrentHealth();
                int maxHp = npcs[i].getHealth().getMaximumHealth();

                /*
                 * Handles the main/range attack if any number 4 and above is rolled
                 * for randomAttack
                 */
                if (randomAttack1 >= 5 && randomAttack1 <= 7) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = 258;
                    npcs[i].endGfx = -1;
                    npcs[i].maxHit = 24;

                    /*
                     * Handles the melee attack if the player is within 1 tile and 1
                     * is rolled for randomAttack
                     */
                } else if (distanceToDragon && randomAttack1 == 1) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                    npcs[i].maxHit = 30;

                    /*
                     * Handles the magic attack if 2 is rolled for randomAttack
                     */
                } else if (randomAttack1 >= 8 && randomAttack1 <= 10) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                    npcs[i].maxHit = 38;
                }

                /*
                 * Handles the first special attack if a 2 is rolled for
                 * randomAttack + adds 100% dmg dealt to the Rune Dragons hp
                 */
                else if (randomAttack1 == 3) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = 1183;
                    npcs[i].endGfx = 1363;
                    npcs[i].maxHit = 30;
                    // TODO REAL Special gfx / special 1 / healing isnt working
                    if (hp >= maxHp) {
                        return;
                    } else if (npcs[i] != null && hp < maxHp) {
                        npcs[i].getHealth().increase(hit / 2);
                        player.sendMessage("You feel the dragon leeching your life force.");
                    }

                    /*
                     * Handles the second special attack if a 3 is rolled for the
                     * randomAttack + does dmg per tick if hit
                     * Projectile hits at the players coordinates TODO
                     * 5x5 square radius for tick dmg TODO
                     */
                } else if (randomAttack1 == 4) {

                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].singleCombatDelay = 5;
                    npcs[i].projectileId = 1198;
                    npcs[i].endGfx = 1196;

                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (player.disconnected) {
                                onStopped();
                                return;
                            }
                            switch (ticks++) {
                                case 0:
                                    npcs[i].attackType = CombatType.SPECIAL;
                                    npcs[i].projectileId = -1;
                                    npcs[i].endGfx = -1;
                                    npcs[i].maxHit = 7;
                                    //groundSpell(npcs[i], player, 1198, 1196, "Dragon", 4);
                                    //player.sendMessage("@red@Lightening attack 1/5");
                                    break;

                                case 1:
                                    npcs[i].attackType = CombatType.SPECIAL;
                                    npcs[i].projectileId = -1;
                                    npcs[i].endGfx = -1;
                                    npcs[i].maxHit = 7;
                                    //player.sendMessage("@red@Lightening attack 2/5");
                                    break;

                                case 2:
                                    npcs[i].attackType = CombatType.SPECIAL;
                                    npcs[i].projectileId = -1;
                                    npcs[i].endGfx = -1;
                                    npcs[i].maxHit = 7;
                                    //player.sendMessage("@red@Lightening attack 3/5");
                                    container.stop();
                                    break;
                            }
                        }

                        @Override
                        public void onStopped() {

                        }
                    }, 1); //handles delay between ticks
                    /*
                     * handles the dragonfire breathe
                     */
                } else {
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                }
                break;

            case 1046:
            case 1049:
                if (Misc.random(10) > 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].gfx100(194);
                    npcs[i].projectileId = 195;
                    npcs[i].endGfx = 196;
                } else {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].gfx100(194);
                    npcs[i].projectileId = 195;
                    npcs[i].endGfx = 576;

                }
                break;
            case 6610:
                if (Misc.random(15) > 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].gfx100(164);
                    npcs[i].projectileId = 165;
                    npcs[i].endGfx = 166;
                } else {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].gfx0(164);
                    npcs[i].projectileId = 165;
                    npcs[i].endGfx = 166;
                }
                break;

            case 6609:
                if (player != null) {
                    int randomHit = Misc.random(20);
                    boolean distance = !player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 5);
                    if (randomHit < 15 && !distance) {
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                    } else if (randomHit >= 15 && randomHit < 20 || distance) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].projectileId = 395;
                        npcs[i].endGfx = 431;
                    } else {
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                    }
                }
                break;
            case 5535:
            case 494:
            case 492:
                npcs[i].attackType = CombatType.MAGE;
                if (Misc.random(5) > 0 && npcs[i].npcType == 494 || npcs[i].npcType == 5535) {
                    npcs[i].gfx0(161);
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                } else {
                    npcs[i].gfx0(155);
                    npcs[i].projectileId = 156;
                    npcs[i].endGfx = 157;
                }
                break;
            case 2892:
                npcs[i].projectileId = 94;
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 95;
                break;
            case 2894:
                npcs[i].projectileId = 298;
                npcs[i].attackType = CombatType.RANGE;
                break;
            case 264:
            case 259:
            case 247:
            case 268:
            case 270:
            case 274:
            case 6593:
            case 273:
                int random3 = Misc.random(2);
                if (random3 == 0) {
                    npcs[i].projectileId = 393;
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;
            case 2919:
            case 2918:
            case 9033://dragon
                int random2 = Misc.random(2);
                if (random2 == 0) {
                    npcs[i].projectileId = 393;
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;

            /*
             * brutal black dragons
             */
            case 7275:
                int bbdrandom2 = Misc.random(5);
                int distanceToBrutal = player.distanceToPoint(npcs[i].getX(), npcs[i].getY());

                if (bbdrandom2 <= 4) {
                    npcs[i].projectileId = 396;
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = CombatType.MAGE;
                } else if (distanceToBrutal <= 4) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                } else {
                    npcs[i].projectileId = 393;
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                }
                break;
            /*
             * brutal black dragons
             */
            case 7274:
                int bReddrandom2 = Misc.random(5);
                int distanceToBrutalR = player.distanceToPoint(npcs[i].getX(), npcs[i].getY());

                if (bReddrandom2 <= 4) {
                    npcs[i].projectileId = 396;
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = CombatType.MAGE;
                } else if (distanceToBrutalR <= 4) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                } else {
                    npcs[i].projectileId = 393;
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                }
                break;
            /*
             * brutal blue dragons
             */
            case 7273:
                int bblueDrandom2 = Misc.random(5);
                int distanceToBrutalB = player.distanceToPoint(npcs[i].getX(), npcs[i].getY());

                if (bblueDrandom2 <= 4) {
                    npcs[i].projectileId = 396;
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = CombatType.MAGE;
                } else if (distanceToBrutalB <= 4) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                } else {
                    npcs[i].projectileId = 393;
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                }
                break;
            case 239:
                int random = Misc.random(100);
                int distance = player.distanceToPoint(npcs[i].absX, npcs[i].absY);
                if (random >= 60 && random < 65) {
                    npcs[i].projectileId = 394; // green
                    npcs[i].endGfx = 429;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                } else if (random >= 65 && random < 75) {
                    npcs[i].projectileId = 395; // white
                    npcs[i].endGfx = 431;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                } else if (random >= 75 && random < 80) {
                    npcs[i].projectileId = 396; // blue
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                } else if (random >= 80 && distance <= 4) {
                    npcs[i].projectileId = -1; // melee
                    npcs[i].endGfx = -1;
                    npcs[i].attackType = CombatType.MELEE;
                } else {
                    npcs[i].projectileId = 393; // red
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = CombatType.DRAGON_FIRE;
                }
                break;
            // arma npcs
            case 2561:
                npcs[i].attackType = CombatType.MELEE;
                break;
            case 2560:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1190;
                break;
            case 2559:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1203;
                break;
            case 2558:
                random = Misc.random(1);
                npcs[i].attackType = CombatType.getRandom(CombatType.RANGE, CombatType.MAGE);
                if (npcs[i].attackType == CombatType.RANGE) {
                    npcs[i].projectileId = 1197;
                } else {
                    npcs[i].projectileId = 1198;
                }
                break;
            // sara npcs
            case 2562: // sara
                random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].endGfx = 1224;
                    npcs[i].projectileId = -1;
                } else if (random == 1)
                    npcs[i].attackType = CombatType.MELEE;
                break;
            case 2563: // star
                npcs[i].attackType = CombatType.MELEE;
                break;
            case 2564: // growler
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1203;
                break;
            case 2565: // bree
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 9;
                break;
            case 2551:
                npcs[i].attackType = CombatType.MELEE;
                break;
            case 2552:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1203;
                break;
            case 2553:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1206;
                break;
            case 2025:
                npcs[i].attackType = CombatType.MAGE;
                int r = Misc.random(3);
                if (r == 0) {
                    npcs[i].gfx100(158);
                    npcs[i].projectileId = 159;
                    npcs[i].endGfx = 160;
                }
                if (r == 1) {
                    npcs[i].gfx100(161);
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                }
                if (r == 2) {
                    npcs[i].gfx100(164);
                    npcs[i].projectileId = 165;
                    npcs[i].endGfx = 166;
                }
                if (r == 3) {
                    npcs[i].gfx100(155);
                    npcs[i].projectileId = 156;
                }
                break;
            case 6707:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].gfx100(158);
                npcs[i].projectileId = 159;
                npcs[i].endGfx = 160;
                if (Misc.random(8) == 0) {
                    player.getPA().movePlayer(3239, 3618, 0);
                }
                break;
            case 2265:// supreme
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 298;
                break;

            case 2266:// prime
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 162;
                npcs[i].endGfx = 477;
                break;

            case 2028:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 27;
                break;
            case 8781:
                int r10 = Misc.random(10);
                if (r10 <= 9) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].startAnimation(-1);
                    npcs[i].projectileId = 1380;
                    npcs[i].endGfx = 431;

                } else {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = 395;
                    npcs[i].startAnimation(-1);
                    npcs[i].endGfx = 555;
                    player.gfx0(437);
                    player.lastSpear = System.currentTimeMillis();
                    player.getPA().getSpeared(npcs[i].absX, npcs[i].absY, 3);
                    player.freezeTimer = 3;
                    player.sendMessage("The Queen's magic pushes you out of her way.");
                }
                break;

            case 2054:
                int r2 = Misc.random(1);
                if (r2 == 0) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].gfx100(550);
                    npcs[i].projectileId = 551;
                    npcs[i].endGfx = 552;
                } else {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].gfx100(553);
                    npcs[i].projectileId = 554;
                    npcs[i].endGfx = 555;
                }
                break;
            case 6257:// saradomin strike
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 76;
                break;
            case 6221:// zamorak strike
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].endGfx = 78;
                break;
            case 6231:// arma
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1199;
                break;

            case 7708:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 660;
                break;

            // sara npcs
            case 3129:
                random = Misc.random(15);
                if (random > 0 && random < 7) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                } else if (random >= 7) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1211;
                } else if (random == 0) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = -1;
                }
                break;
            case 3209:// cave horror
                random = Misc.random(3);
                if (random == 0 || random == 1) {
                    npcs[i].attackType = CombatType.MELEE;
                } else {
                    npcs[i].attackType = CombatType.MAGE;
                }
                break;
            case 3127:
                int r3 = 0;
                if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX, PlayerHandler.players[npcs[i].spawnedBy].absY, 1)) {
                    r3 = Misc.random(2);
                } else {
                    r3 = Misc.random(1);
                }
                if (r3 == 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 448;
                    npcs[i].endGfx = 157;
                } else if (r3 == 1) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].endGfx = 451;
                    npcs[i].projectileId = -1;
                    npcs[i].hitDelayTimer = 6;
                    npcs[i].attackTimer = 9;
                } else if (r3 == 2) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;
            case InfernoWave.JALTOK_JAD:
                int r4 = 0;
                if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX, PlayerHandler.players[npcs[i].spawnedBy].absY, 1)) {
                    r4 = Misc.random(2);
                } else {
                    r4 = Misc.random(1);
                }
                if (r4 == 0) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 448;
                    npcs[i].endGfx = 157;
                } else if (r4 == 1) {
                    npcs[i].attackType = CombatType.RANGE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = 451;
                } else if (r4 == 2) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;
            case 3125:
                if (player.distanceToPoint(npcs[i].getX(), npcs[i].getY()) > 2) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 445;
                    npcs[i].endGfx = 446;
                } else {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].endGfx = -1;
                }
                break;
            case 3121:
            case 2167:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 443;
                break;
            case 1678:
            case 1679:
            case 1680:
            case 1683:
            case 1684:
            case 1685:
                npcs[i].attackType = CombatType.MELEE;
                npcs[i].attackTimer = 4;
                break;

            case 319:
                int corpRandom = Misc.random(15);
                if (corpRandom >= 12) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].projectileId = Misc.random(1) == 0 ? 316 : 314;
                    npcs[i].endGfx = -1;
                }
                if (corpRandom >= 3 && corpRandom <= 11) {
                    npcs[i].attackType = CombatType.MELEE;
                    npcs[i].projectileId = -1;
                    npcs[i].hitDelayTimer = 2;
                    npcs[i].endGfx = -1;
                }
                if (corpRandom <= 2) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].hitDelayTimer = 3;
                    groundSpell(npcs[i], player, 315, 317, "corp", 4);
                }
                break;

            case InfernoWave.JAL_XIL:
                if (npcs[i].getDistance(player.getX(), player.getY()) <= 1) {
                    int x = Misc.random(0, 1);
                    if (x == 0) {
                        npcs[i].projectileId = -1;
                        npcs[i].attackType = CombatType.MELEE;
                    } else {
                        npcs[i].projectileId = 1376;
                        npcs[i].attackType = CombatType.RANGE;
                    }
                } else {
                    npcs[i].projectileId = 1376;
                    npcs[i].attackType = CombatType.RANGE;
                }

                break;
            case InfernoWave.JAL_ZEK:
                if (npcs[i].getDistance(player.getX(), player.getY()) <= 1) {
                    int x = Misc.random(0, 1);
                    if (x == 0) {
                        npcs[i].projectileId = -1;
                        npcs[i].attackType = CombatType.MELEE;
                    } else {
                        npcs[i].projectileId = 1378;
                        npcs[i].attackType = CombatType.MAGE;
                    }
                } else {
                    npcs[i].projectileId = 1378;
                    npcs[i].attackType = CombatType.MAGE;
                }
                break;
            case InfernoWave.JAL_IMKOT:
                npcs[i].attackType = CombatType.MELEE;
                break;
            case InfernoWave.JAL_AKREK_KET:
                npcs[i].attackType = CombatType.MELEE;
                break;
            case InfernoWave.JAL_AKREK_MEJ:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1381;
                break;
            case InfernoWave.JAL_AKREK_XIL:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].projectileId = 1379;
                break;
            case InfernoWave.JAL_AK:
                if (npcs[i].getDistance(player.getX(), player.getY()) <= 1) {
                    int x = Misc.random(0, 1);
                    if (x == 0) {
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].projectileId = -1;
                    } else if (x == 1) {
                        if (player.protectingMagic()) {
                            npcs[i].attackType = CombatType.RANGE;
                            npcs[i].hitDelayTimer = 3;
                            npcs[i].projectileId = 1378;
                        } else if (player.protectingRange()) {
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].hitDelayTimer = 3;
                            npcs[i].projectileId = 1380;
                        } else {
                            int y = Misc.random(0, 1);
                            if (y == 0) {
                                npcs[i].attackType = CombatType.RANGE;
                                npcs[i].hitDelayTimer = 3;
                                npcs[i].projectileId = 1378;
                            } else if (y == 1) {
                                npcs[i].attackType = CombatType.MAGE;
                                npcs[i].hitDelayTimer = 3;
                                npcs[i].projectileId = 1380;
                            }
                        }
                    }
                } else {
                    if (player.protectingMagic()) {
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 1378;
                    } else if (player.protectingRange()) {
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 1380;
                    } else {
                        int y = Misc.random(0, 1);
                        if (y == 0) {
                            npcs[i].attackType = CombatType.RANGE;
                            npcs[i].hitDelayTimer = 3;
                            npcs[i].projectileId = 1378;
                        } else if (y == 1) {
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].hitDelayTimer = 3;
                            npcs[i].projectileId = 1380;
                        }
                    }
                }
                break;
            case InfernoWave.JAL_MEJRAH:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].hitDelayTimer = 3;
                npcs[i].projectileId = 1382;
                int[] toDecrease = {0, 2, 4, 6};

                for (int tD : toDecrease) {
                    player.playerLevel[tD] -= 1;
                    if (player.playerLevel[tD] < 0) {
                        player.playerLevel[tD] = 1;
                    }
                    player.getPA().refreshSkill(tD);
                    player.getPA().setSkillLevel(tD, player.playerLevel[tD], player.playerXP[tD]);
                }
                npcs[i].attack += 1;
                break;

            /**
             * Kalphite Queen Stage One
             */
            case 963:
            case 965:
                int kqRandom = Misc.random(2);
                switch (kqRandom) {
                    case 0:
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 280;
                        npcs[i].endGfx = 281;
                        break;
                    case 1:
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].hitDelayTimer = 3;
                        npcs[i].projectileId = 473;
                        npcs[i].endGfx = 281;
                        break;
                    case 2:
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        break;
                }
                break;
            /**
             * Tekton
             */
            case 7544:
                if (Objects.equals(tektonAttack, "MELEE")) {
                    npcs[i].attackType = CombatType.MELEE;
                } else if (Objects.equals(tektonAttack, "SPECIAL")) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    Tekton.tektonSpecial(player);
                    tektonAttack = "MELEE";
                    npcs[i].hitDelayTimer = 4;
                    npcs[i].attackTimer = 8;
                }
                break;
            /**
             * Sotetseg
             */
            case Sotetseg.NPC_ID:
                if (Objects.equals(glodAttack, "MELEE")) {
                    npcs[i].attackType = CombatType.MELEE;
                } else if (Objects.equals(glodAttack, "MAGIC")) {
                    npcs[i].attackType = CombatType.MAGE;
                    glodAttack = "MELEE";
                    npcs[i].projectileId = 195;
                    npcs[i].endGfx = 196;
                    npcs[i].hitDelayTimer = 4;
                    npcs[i].attackTimer = 8;
                }
                break;

            /**
             * Fragment Of Seren
             */
            case FragmentOfSeren.NPC_ID:
                if (Objects.equals(queenAttack, "MAGIC")) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 195;
                    npcs[i].endGfx = 196;
                    npcs[i].hitDelayTimer = 4;
                } else if (Objects.equals(queenAttack, "SPECIAL")) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    FragmentOfSeren.handleSpecialAttack(player);
                    queenAttack = "MAGIC";
                    npcs[i].hitDelayTimer = 4;
                    npcs[i].attackTimer = 8;
                }
                break;

            /**
             * Tekton magers
             */
            case 7617:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1348;
                npcs[i].endGfx = 1345;
                npcs[i].hitDelayTimer = 5;
                npcs[i].attackTimer = 15;
                break;
            case 7529:
                if (Misc.random(10) == 5) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = 1348;
                    npcs[i].endGfx = 1345;
                    npcs[i].hitDelayTimer = 3;
                } else {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1348;
                    npcs[i].endGfx = 1345;
                    npcs[i].hitDelayTimer = 3;
                }
                break;

            case 7566:
                if (Misc.random(10) <= 8) {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1327;
                    npcs[i].endGfx = 1328;
                    npcs[i].hitDelayTimer = 3;
                    npcs[i].maxHit = 35;
                } else {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].projectileId = 1329;
                    npcs[i].endGfx = 1330;
                    npcs[i].maxHit = 48;
                    npcs[i].hitDelayTimer = 3;
                    break;
                }
                break;

            case 7554://great olm
                int randomStyle1 = Misc.random(12);

                switch (randomStyle1) {
                    case 0://mage
                    case 1:
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].projectileId = 1339;
                        npcs[i].endGfx = 1353;
                        npcs[i].maxHit = 33;
                        npcs[i].hitDelayTimer = 2;
                        break;

                    case 2://range
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        npcs[i].attackType = CombatType.RANGE;
                        npcs[i].projectileId = 1340;
                        npcs[i].endGfx = 1353;
                        npcs[i].maxHit = 33;
                        npcs[i].hitDelayTimer = 2;
                        break;

                    case 7://acid
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = 1354;
                        npcs[i].endGfx = 1358;
                        npcs[i].maxHit = 45;
                        npcs[i].hitDelayTimer = 2;
                        player.getHealth().proposeStatus(HealthStatus.POISON, Misc.random(3, 10), Optional.of(npcs[i]));
                        player.sendMessage("You have been poisoned by Olm's acid attack!");
                        break;

                    case 8://dragon fire
                    case 9:
                        npcs[i].attackType = CombatType.DRAGON_FIRE;
                        npcs[i].projectileId = 393;
                        npcs[i].endGfx = 430;
                        npcs[i].maxHit = 62;
                        npcs[i].hitDelayTimer = 3;
                        break;

                    case 10://burn
                    case 11:
                        npcs[i].attackType = CombatType.SPECIAL;
                        npcs[i].projectileId = 1349;
                        npcs[i].endGfx = -1;
                        npcs[i].maxHit = 45;
                        npcs[i].hitDelayTimer = 2;
                        break;
                }
                break;

            case 7604:
            case 7605:
            case 7606:
                if (Misc.random(10) == 5) {
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].forceChat("RAA!");
                    npcs[i].projectileId = 1348;
                    npcs[i].endGfx = 1345;
                    npcs[i].hitDelayTimer = 3;
                } else {
                    npcs[i].attackType = CombatType.MAGE;
                    npcs[i].projectileId = 1348;
                    npcs[i].endGfx = 1345;
                    npcs[i].hitDelayTimer = 3;
                }
                break;

            /**
             * Cerberus
             */
            case 5862:
                if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "GROUND_ATTACK")) {
                    startAnimation(4492, i);
                    npcs[i].forceChat("Grrrrrrrrrrrrrr");
                    npcs[i].attackType = CombatType.SPECIAL;
                    npcs[i].hitDelayTimer = 4;
                    groundSpell(npcs[i], player, -1, 1246, "cerberus", 4);
                    player.CERBERUS_ATTACK_TYPE = "MELEE";
                }
                if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "GHOST_ATTACK")) {
                    startAnimation(4494, i);
                    // npcs[i].forceChat("Aaarrrooooooo");
                    player.CERBERUS_ATTACK_TYPE = "MELEE";
                }
                if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "FIRST_ATTACK")) {
                    startAnimation(4493, i);
                    npcs[i].attackTimer = 5;
                    player.CERBERUS_ATTACK_TYPE = "MELEE";
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (player.disconnected) {
                                onStopped();
                                return;
                            }
                            switch (ticks++) {
                                case 0:
                                    npcs[i].attackType = CombatType.MELEE;
                                    npcs[i].projectileId = -1;
                                    npcs[i].endGfx = -1;
                                    break;

                                case 2:
                                    npcs[i].attackType = CombatType.RANGE;
                                    npcs[i].projectileId = 1245;
                                    npcs[i].endGfx = 1244;
                                    break;

                                case 4:
                                    npcs[i].attackType = CombatType.MAGE;
                                    npcs[i].projectileId = 1242;
                                    npcs[i].endGfx = 1243;
                                    container.stop();
                                    break;
                            }
                            // System.out.println("Ticks - cerb " + ticks);
                        }

                        @Override
                        public void onStopped() {

                        }
                    }, 2);
                } else {
                    int randomStyle = Misc.random(2);

                    switch (randomStyle) {
                        case 0:
                            npcs[i].attackType = CombatType.MELEE;
                            npcs[i].projectileId = -1;
                            npcs[i].endGfx = -1;
                            break;

                        case 1:
                            npcs[i].attackType = CombatType.RANGE;
                            npcs[i].projectileId = 1245;
                            npcs[i].endGfx = 1244;
                            break;

                        case 2:
                            npcs[i].attackType = CombatType.MAGE;
                            npcs[i].projectileId = 1242;
                            npcs[i].endGfx = 1243;
                            break;
                    }
                }
                break;

            case Skotizo.AWAKENED_ALTAR_NORTH:
            case Skotizo.AWAKENED_ALTAR_SOUTH:
            case Skotizo.AWAKENED_ALTAR_WEST:
            case Skotizo.AWAKENED_ALTAR_EAST:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].projectileId = 1242;
                npcs[i].endGfx = 1243;
                break;

            case Skotizo.SKOTIZO_ID:
                int randomStyle;
                if (player.getSkotizo().firstHit) {
                    randomStyle = 1;
                    player.getSkotizo().firstHit = false;
                } else {
                    randomStyle = Misc.random(1);
                }
                switch (randomStyle) {
                    case 0:
                        npcs[i].attackType = CombatType.MELEE;
                        npcs[i].projectileId = -1;
                        npcs[i].endGfx = -1;
                        break;

                    case 1:
                        npcs[i].attackType = CombatType.MAGE;
                        npcs[i].projectileId = 1242;
                        npcs[i].endGfx = 1243;
                        break;
                }
                break;

            case 5867:
                npcs[i].attackType = CombatType.RANGE;
                npcs[i].hitDelayTimer = 3;
                npcs[i].projectileId = 1230;
                npcs[i].attackTimer = 15;
                break;

            case 5868:
                npcs[i].attackType = CombatType.MAGE;
                npcs[i].hitDelayTimer = 3;
                npcs[i].projectileId = 127;
                npcs[i].attackTimer = 15;
                break;

            case 5869:
                npcs[i].attackType = CombatType.MELEE;
                npcs[i].hitDelayTimer = 3;
                npcs[i].projectileId = 1248;
                npcs[i].attackTimer = 15;
                break;
        }
    }

    /**
     * Distanced required to attack
     **/
    public int distanceRequired(int i) {
        switch (npcs[i].npcType) {
            case InfernoWave.TZKAL_ZUK:
                return 30;
            case InfernoWave.JALTOK_JAD:
            case InfernoWave.JAL_XIL:
            case InfernoWave.JAL_ZEK:
                return 8;
            case InfernoWave.JAL_IMKOT:
                return 1;
            case InfernoWave.JAL_AK:
                return 8;
            case InfernoWave.JAL_AKREK_XIL:
            case InfernoWave.JAL_AKREK_MEJ:
            case InfernoWave.JAL_MEJRAH:
                return 4;
            case Sotetseg.NPC_ID:
            case Skotizo.SKOTIZO_ID:
                return npcs[i].attackType == CombatType.MAGE ? 25 : 3;
            case 8028:
                return 10;

            /* Hydra */
            case 8609:
                return 10;

            /*xeric monsters*/
            case 7576://crabs
            case 7577:
            case 7578:
            case 7579:
                return npcs[i].attackType == CombatType.MELEE ? 1 : 2;
            case 7586://ice fiend
                return npcs[i].attackType == CombatType.MELEE ? 1 : 2;
            case 7585://ice demon
                return npcs[i].attackType == CombatType.MELEE ? 1 : 2;
            /**
             * Cerberus
             */
            case 5862:
            case 6766:
            case 7144:
            case 7145:
            case 7146:
                return npcs[i].attackType == CombatType.MELEE ? 1 : 7;
            case 9021: //melee
            case 9022: //range
            case 9023: //mage
            case 9024: //death
                return npcs[i].attackType == CombatType.MELEE ? 2 : 7;

            case 5867:
            case 5868:
            case 5869:
            case 7617:
                return 30;

            case 7559:
            case 7560:
                return 10;

            case 7604: // Skeletal mystic
            case 7605: // Skeletal mystic
            case 7606: // Skeletal mystic
            case FragmentOfSeren.NPC_ID:
                return 8;

            case 319:
                if (npcs[i].attackType == CombatType.MAGE)
                    if (npcs[i].attackType == CombatType.SPECIAL)
                        return 20;
                if (npcs[i].attackType == CombatType.MELEE)
                    return 4;

            case 5890:
            case 7544:
            case 5129:
                return npcs[i].attackType == CombatType.MELEE ? 3 : 7;
            case 6914: // Lizardman, Lizardman brute
            case 6915:
            case 6916:
            case 6917:
            case 6918:
            case 6919:
                return npcs[i].attackType == CombatType.MAGE ? 4 : 1;

            case 6618:
                return npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.SPECIAL ? 4 : 1;

            case 465:
                return npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.SPECIAL ? 6 : 2;

            case 6615: // Scorpia
            case 6619: // Chaos fanatic
                return 4;

            case 6367:
            case 6368:
            case 6369:
            case 6371:
            case 6372:
            case 6373:
            case 6374:
            case 6375:
            case 6376:
            case 6377:
            case 6378:
                if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE)
                    return 8;
                else
                    return 4;

            case 6370:
                return 10;

            case 498:
            case 499:
                return 6;
            case 1672: // Ahrim
            case 1675: // Karil
            case 983: // Dagannoth mother
            case 984:
            case 985:
            case 987:
                return 8;

            case 986:
            case 988:
                return 3;

            case 2209:
            case 2211:
            case 2212:

            case 2242:
            case 2244:
            case 3160:
            case 3161:
            case 3174:
                return 4;
            case 3162:
            case 3167:
            case 3168:
                return 9;

            case 1610:
            case 1611:
            case 1612:
                return 4;

            case 2205:
                return npcs[i].attackType == CombatType.MAGE ? 4 : 1;
            case Zulrah.SNAKELING:
                return 2;
            case 2208:
                return 8;
            case 2215:
                return npcs[i].attackType == CombatType.MELEE ? 3 : 12;
            case 7936://start of revs
            case 7940:
            case 7931:
            case 7932:
            case 7937:
            case 7939:
            case 7935:
            case 7934:
            case 7938:
            case 7933:
                return npcs[i].attackType == CombatType.MELEE ? 2 : 8;
            case 2217:
                return 9;
            case 2218:
                return 6;
            case 2042:
            case 2043:
            case 2044:
            case 7554:
                return 25;
            case 3163:
                return 8;
            case 3164:
            case 1049:
                return 5;
            case 6611:
            case 6612:
                return npcs[i].attackType == CombatType.SPECIAL || npcs[i].attackType == CombatType.MAGE ? 12 : 3;
            case 1046:
            case 6610:
                return 8;
            case 494:
            case 492:
            case 6609:
            case 5535:
                return 10;
            case 2025:
            case 2028:
                return 6;
            case 2562:
            case 3131:
            case 3132:
            case 3130:
            case 2206:
            case 2207:
            case 2267:
                return 2;
            case 2054:// chaos ele
            case 3125:
            case 3121:
            case 2167:
            case 3127:
                return npcs[i].attackType == CombatType.MELEE ? 2 : 8;
            case 3129:
                return 5;
            case 2265:// dag kings
            case 2266:
                return 4;
            case 8781:
                return 6;
            case 239:
                return npcs[i].attackType == CombatType.DRAGON_FIRE ? 18 : 4;
            case 8030:
            case 8031:
                return npcs[i].attackType == CombatType.DRAGON_FIRE ? 12 : 3;
            case 2552:
            case 2553:
            case 2556:
            case 2557:
            case 2558:
            case 2559:
            case 2560:
            case 2564:
            case 2565:
                return 9;
            // things around dags
            case 2892:
            case 2894:
                return 10;
            default:
                return 1;
        }
    }

    public int followDistance(int i) {
        if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS) || Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)
                || Boundary.isIn(npcs[i], Boundary.CERBERUS_BOSSROOMS)) {
            return 20;
        }
        if (Boundary.isIn(npcs[i], Boundary.XERIC)) {
            return 128;
        }
        switch (npcs[i].npcType) {
            case 8622:
            case 8621:
            case 8620:
            case 8619:
            case 8615:
                return 40;
            case 2045:
                return 20;
            case 6615:
                return 30;
            /* Hydra */
            case 8609:
                return 10;
            case 1739:
            case 1740:
            case 1741:
            case 1742:
            case 7413:
            case 7288:
            case 7290:
            case 7292:
            case 7294:
                return -1;
            case 1678: // Barrows tunnel NPCs
            case 1679:
            case 1680:
            case 1683:
            case 1684:
            case 1685:
            case 484:
            case 7276:
            case 135:
            case 6914: // Lizardman, Lizardman brute
            case 6915:
            case 6916:
            case 6917:
            case 6918:
            case 6919:
                return 4;
            case 2209:
            case 2211:
            case 2212:
            case 2233:
            case 2234:
            case 2235:
            case 2237:
            case 2241:
            case 2242:
            case 2243:
            case 2244:
            case 2245:
            case 3133:
            case 3134:
            case 3135:
            case 3137:
            case 3138:
            case 3139:
            case 3140:
            case 3141:
            case 3159:
            case 3160:
            case 3161:
            case 3166:
            case 3167:
            case 3168:
            case 7936://start of revs
            case 7940:
            case 7931:
            case 7932:
            case 7937:
            case 7939:
            case 7935:
            case 7934:
            case 7938:
            case 7933:
                return 4;
            case 2205:
            case 2206:
            case 2207:
            case 2208:
            case 2215:
            case 2216:
            case 2217:
            case 2218:
            case 3129:
            case 3130:
            case 3131:
            case 3132:
            case 3162:
            case 3163:
            case 3164:
            case 3165:
                return 22;
            case 239:
            case 8031:
            case 8030:
                return 40;
            case 6611:
            case 6612:
            case 963:
            case 965:
            case 7544:
                return 15;
            case 5129:
                return 9;
            case Sotetseg.NPC_ID:
            case FragmentOfSeren.NPC_ID:
                return 10;
            case 319:
                return 9;
            case 2551:
            case 2562:
            case 2563:
                return 8;
            case 2054:
            case 5890:
            case 5916:
                return 10;
            case 2265:
            case 2266:
            case 2267:
                return 8;
            case 8781:
                return 3;
            default:
                return 10;

        }

    }

    public int getProjectileSpeed(int i) {
        switch (npcs[i].npcType) {
            case 498:
                return 120;
            case 499:
                return 105;
            case 2265:
            case 2266:
            case 2054:
                return 85;
            case 8781:
                return 75;

            case 3127:
            case InfernoWave.JALTOK_JAD:
            case 7617:
                return 130;
            case 1672:
            case 239:
            case 8030:
            case 8031:
                return 90;
            case 8609://Hydra
                return 110;
            case 2025:
                return 85;
            case 6607:
                return 70;
            case 2028:
                return 80;
            case 3162:
                return 100;

            default:
                return 85;
        }
    }

    /**
     * Npcs who ignores projectile clipping to ensure no safespots
     *
     * @param i
     * @return true is the npc is using range, mage or special
     */
    public static boolean ignoresProjectile(int i) {
        if (npcs[i] == null)
            return false;
        switch (npcs[i].npcType) {
            case 6611:
            case 6612:
            case 319:
            case 6618:
            case 6766:
            case 5862:
            case 963:
            case 965:
            case 7706:
            case 7144:
            case 8028:
            case 7145:
            case 7146:
            case 9021: //melee
            case 9022: //range
            case 9023: //mage
            case 9024: //death
            case 5890:
            case 8609:
            case 7566:
            case 7563:
            case 7585:
            case 7544:
            case 7554:
            case 8781:
            case Sotetseg.NPC_ID:
            case FragmentOfSeren.NPC_ID:
                return true;
        }
        return false;
    }

    /**
     * NPC Attacking Player
     **/
    public void attackPlayer(Player c, int i) {
        NPC npc = npcs[i];
        CombatScript script = npc.getCombatScript();
//		if (c.getInstance() != npc.getInstance())
//			return;

        if (script != null) {
            if (script.handleAttack(npc, c)) ;
            return;
        }
        if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
            return;
        }
        if (npcs[i].npcType == 7555 || npcs[i].npcType == 7553
                || npcs[i].npcType == InfernoWave.TZKAL_ZUK) {
            return;
        }
        if (npcs[i].npcType == FragmentOfSeren.CRYSTAL_WHIRLWIND) {
            return;
        }
        if (c.getInferno() != null && c.getInferno().kill.contains(npcs[i])) {
            return;
        }
        if (npcs[i].npcType == InfernoWave.JALTOK_JAD) {
            return;
        }
        if (npcs[i].npcType == InfernoWave.JAL_NIB) {
            return;
        }
        if (c.isInvisible()) {
            return;
        }
        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        if (npcs[i] != null) {
            if (npcs[i].isDead)
                return;
            if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.getIndex()) {
                npcs[i].killerId = 0;
                npcs[i].facePlayer(0);
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
                return;
            }
            if (!npcs[i].inMulti() && ((c.underAttackByPlayer > 0 && c.underAttackByNpc != i)
                    || (c.underAttackByNpc > 0 && c.underAttackByNpc != i))) {
                npcs[i].killerId = 0;
                npcs[i].facePlayer(0);
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
                return;
            }
            if (npcs[i].heightLevel != c.heightLevel) {
                npcs[i].killerId = 0;
                npcs[i].facePlayer(0);
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
                return;
            }
            switch (npcs[i].npcType) {
                case 8062:// vorkath crab when gets close and attacks player insta kills player
                    npcs[i].gfx100(427);
                    c.appendDamage(150, Hitmark.HIT);
                    npcs[i].isDead = true;
                    break;
                case 1739:
                case 7413:
                case 1740:
                case 1741:
                case 1742:
                case 6600:
                case 7288:
                case 7290:
                case 7292:
                case 7294:
                case 6602:
                    npcs[i].killerId = 0;
                    npcs[i].facePlayer(0);
                    npcs[i].underAttack = false;
                    npcs[i].randomWalk = true;
                    break;

                // case 5862:
                // if (Cerberus.ATTACK_TYPE == "FIRST_ATTACK") {
                // loadSpell(c, i);
                //
                // Cerberus.ATTACK_TYPE = "MELEE";
                // }
                // break;
            }

            if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS) ^ Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
                npcs[i].killerId = 0;
                npcs[i].facePlayer(0);
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
                return;
            }
            if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)
                    ^ Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
                npcs[i].killerId = 0;
                npcs[i].facePlayer(0);
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
                return;
            }
            npcs[i].facePlayer(c.getIndex());

            int distance = c.distanceToPoint(npcs[i].absX, npcs[i].absY);
            boolean hasDistance = npcs[i].getDistance(c.getX(), c.getY()) <= ((double) distanceRequired(i))
                    + (npcs[i].getSize() > 1 ? 0.5 : 0.0);

            /**
             * Npc's who will ignore projectile clipping
             */
            if (ignoresProjectile(i)) {
                if (distance < 10) {
                    c.getPA().removeAllWindows();
                    npcs[i].oldIndex = c.getIndex();
                    c.underAttackByNpc = i;
                    c.singleCombatDelay2 = System.currentTimeMillis();
                    npcs[i].attackTimer = getNpcDelay(i);
                    npcs[i].hitDelayTimer = getHitDelay(i);
                    loadSpell(c, i);
                    startAnimationHighPriority(getAttackEmote(i), i);
                }
            }

            if (hasDistance) {
                if (projectileClipping) {
                    if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE) {
                        int x1 = npcs[i].absX;
                        int y1 = npcs[i].absY;
                        int z = npcs[i].heightLevel;
                        if (!PathChecker.isProjectilePathClear(npcs[i], c, x1, y1, z, c.absX, c.absY)
                                && !PathChecker.isProjectilePathClear(npcs[i], c, c.absX, c.absY, z, x1, y1)) {
                            return;
                        }
                    }
                }

                if (c.respawnTimer <= 0) {
                    Optional<AlchemicalHydra> hydraInstance = getHydraInstance(i);
                    if (hydraInstance.isPresent()) {
                        hydraInstance.get().doAttack();
                        return;
                    }
                    /**
                     * Npcs who follow projectile clipping
                     */
                    npcs[i].attackTimer = getNpcDelay(i);
                    npcs[i].hitDelayTimer = getHitDelay(i);
                    if (npcs[i].attackType == null) {
                        npcs[i].attackType = CombatType.MELEE;
                    }
                    loadSpell(c, i);
                    npcs[i].oldIndex = c.getIndex();
                    c.underAttackByNpc = i;
                    c.singleCombatDelay2 = System.currentTimeMillis();
                    startAnimationHighPriority(getAttackEmote(i), i);
                    c.getPA().removeAllWindows();
                    if (npcs[i].attackType == CombatType.DRAGON_FIRE) {
                        npcs[i].hitDelayTimer += 2;
                        c.getCombat().absorbDragonfireDamage();
                    }
                    if (multiAttacks(i)) {
                        startAnimationHighPriority(getAttackEmote(i), i);
                        multiAttackGfx(i, npcs[i].projectileId);
                        npcs[i].oldIndex = c.getIndex();
                        return;
                    }
                    if (npcs[i].projectileId > 0) {
                        if (npcs[i].npcType == 7706) {
                            NPC glyph = getNpc(7707, c.getHeight());
                            if (glyph == null) {
                                return;
                            }
                        }
                        int nX = NPCHandler.npcs[i].getX() + offset(i);
                        int nY = NPCHandler.npcs[i].getY() + offset(i);
                        int pX = c.getX();
                        int pY = c.getY();
                        //c.sendMessage(pX + " "+ pY);
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + npcs[i].getSize() / 2;
                        int centerY = nY + npcs[i].getSize() / 2;
                        c.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, getProjectileSpeed(i),
                                npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
                                getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getIndex() - 1, 65,
                                npcs[i].getProjectileDelay());

                        if (c.teleporting) {
                            c.startAnimation(65535);
                            c.teleporting = false;
                            c.isRunning = false;
                            c.gfx0(-1);
                            c.startAnimation(-1);
                        }
                    }
                } else {
                    if (npcs[i].npcType == InfernoWave.JAL_IMKOT
                            && npcs[i].getDistance(c.getX(), c.getY()) > 2 && Misc.random(0, 3) == 0) {
                        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                            int ticks = 0;

                            @Override
                            public void execute(CycleEventContainer container) {
                                if (ticks == 0) {
                                    npcs[i].startAnimation(7600);
                                }
                                if (ticks == 2) {
                                    npcs[i].teleport(c.getX(), c.getY(), c.heightLevel);
                                }
                                if (ticks == 4) {
                                    npcs[i].startAnimation(7601);
                                }
                                ticks++;
                            }
                        }, 1);
                    }
                }
            }
        }
    }

    public int offset(int i) {
        switch (npcs[i].npcType) {
            case 2044:
                return 0;
            case 6611:
            case 6612:
                return 3;
            case 6610:
                return 2;
            case 239:
            case 8031:
            case 8030:
                return 2;
            case 2265:
            case 2266:
                return 1;
            case 3127:
            case 3125:
            case InfernoWave.JALTOK_JAD:
                return 1;
        }
        return 0;
    }

    public boolean retaliates(int npcType) {
        return npcType < 3777 || npcType > 3780;
    }

    private boolean prayerProtectionIgnored(int npcId) {
        switch (npcs[npcId].npcType) {
            case 1610:
            case 1611:
            case 1612:
            case 3162:
            case 8028:

            case 3129:
                return true;

            case 1672:
                return false;
            case 6611:
            case 6612:
            case 6609:
                return npcs[npcId].attackType == CombatType.MAGE || npcs[npcId].attackType == CombatType.SPECIAL;
            case 465:
                return npcs[npcId].attackType == CombatType.DRAGON_FIRE;
        }
        return false;
    }

    public void handleSpecialEffects(Player c, int i, int damage) {
        if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
            if (damage > 0) {
                if (c != null) {
                    if (c.playerLevel[5] > 0) {
                        c.playerLevel[5]--;
                        c.getPA().refreshSkill(5);
                    }
                }
            }
        }

    }

    public static void startAnimation(int animId, int i) {
        npcs[i].startAnimation(animId);
    }

    public static void startAnimationHighPriority(int animId, int i) {
        npcs[i].startAnimation(new Animation(animId, 0, AnimationPriority.HIGH));
    }

    public NPC[] getNPCs() {
        return npcs;
    }

    public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        return ((objectX - playerX <= distance && objectX - playerX >= -distance)
                && (objectY - playerY <= distance && objectY - playerY >= -distance));
    }

    public int getMaxHit(int i) {
        if (npcs[i] == null) {
            return 0;
        }
        if (Boundary.isIn(npcs[i], Boundary.XERIC)) {
            return XericWave.getMax(npcs[i].npcType);
        }
        switch (npcs[i].npcType) {
            case Sotetseg.NPC_ID:
                return npcs[i].attackType == CombatType.MELEE ? 55 : 25;
            case 22545:
                return 22;
            case 7706:
                return 120;
            case 3021: // KBD Spiders
                return 7;
            case Skotizo.SKOTIZO_ID:
                return 38;
            case Skotizo.AWAKENED_ALTAR_NORTH:
            case Skotizo.AWAKENED_ALTAR_SOUTH:
            case Skotizo.AWAKENED_ALTAR_WEST:
            case Skotizo.AWAKENED_ALTAR_EAST:
                return 15;
            case Skotizo.REANIMATED_DEMON:
            case Skotizo.DARK_ANKOU:
                return 8;
            case 8028:
                return 40;
            case 6914: // Lizardman, Lizardman brute
            case 6915:
            case 6916:
            case 6917:
                return 7;
            case 7617:
                return 30;
            case 6918:
            case 6919:
                return 11;
            case 8781:
                return npcs[i].attackType == CombatType.MAGE ? 25 : 55;

            case 2042:
            case 2043:
            case 2044:
                return 41;
            case 5862:
                return 23;
            case 499:
                return 21;
            case 498:
                return 12;
            case 5867:
            case 5868:
            case 5869:
                return 30;
            case 273:
                return npcs[i].attackType == CombatType.MELEE ? 20 : 18;
            case 239:
                return npcs[i].attackType == CombatType.DRAGON_FIRE ? 50 : 22;
            case 465:
                return npcs[i].attackType == CombatType.DRAGON_FIRE ? 55 : 13;
            case 8031:
            case 8030:
                return npcs[i].attackType == CombatType.DRAGON_FIRE ? 55 : 38;
            case 2208:
            case 2207:
            case 2206:
                return 16;

            /*Hydra*/
            case 8609:
                return 22;

            case 319:
                return npcs[i].attackType == CombatType.MELEE ? 55 : npcs[i].attackType == CombatType.SPECIAL ? 35 : 49;
            case 320:
                return 10;
            case 3129:
                return npcs[i].attackType == CombatType.MELEE ? 60 : npcs[i].attackType == CombatType.SPECIAL ? 49 : 30;
            case 6611:
            case 6612:
                return npcs[i].attackType == CombatType.MELEE ? 30 : npcs[i].attackType == CombatType.MAGE ? 34 : 46;
            case 1046:
                return npcs[i].attackType == CombatType.MAGE ? 40 : 50;
            case 6610:
            case 7144:
            case 7145:
            case 7146:
                return 30;
            case 9021: //melee
            case 9022: //range
            case 9023: //mage
            case 9024: //death
                return npcs[i].attackType == CombatType.MELEE ? 30 : 20;
            case 6609:
                return npcs[i].attackType == CombatType.SPECIAL ? 3 : npcs[i].attackType == CombatType.MAGE ? 60 : 40;
            case 6618:
                return npcs[i].attackType == CombatType.SPECIAL ? 23 : 15;
            case 6619:
                return npcs[i].attackType == CombatType.SPECIAL ? 31 : 25;
            case 2558:
                return npcs[i].attackType == CombatType.MAGE ? 38 : 68;
            case 2562:
                return 31;
            case 2215:
                return npcs[i].attackType == CombatType.MELEE ? 55 : 31;
            case 3162:
                return npcs[i].attackType == CombatType.RANGE ? 71 : npcs[i].attackType == CombatType.MAGE ? 21 : 15;
            case 963:
                return npcs[i].attackType == CombatType.MAGE ? 30 : 21;
            case 965:
                return npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE ? 30 : 21;
        }
        return npcs[i].maxHit == 0 ? 1 : npcs[i].maxHit;
    }

    @SuppressWarnings("resource")
    public boolean loadAutoSpawn(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            return false;
        }
        while (!EndOfFile && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("spawn")) {
                    newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
                            Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
                            getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]),
                            Integer.parseInt(token3[6]), Integer.parseInt(token3[7]));

                }
            } else {
                if (line.equals("[ENDOFSPAWNLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return true;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }

    public void startGame() {
        for (int i = 0; i < PuroPuro.IMPLINGS.length; i++) {
            newNPC(PuroPuro.IMPLINGS[i][0], PuroPuro.IMPLINGS[i][1], PuroPuro.IMPLINGS[i][2], 0, 1, -1, -1, -1, -1);
        }

        /**
         * Random spawns
         */
        int random_spawn = Misc.random(2);
        int x = 0;
        int y = 0;
        switch (random_spawn) {
            case 0:
                x = 2620;
                y = 4347;
                break;

            case 1:
                x = 2607;
                y = 4321;
                break;

            case 2:
                x = 2589;
                y = 4292;
                break;
        }
        newNPC(7302, x, y, 0, 1, -1, -1, -1, -1);
    }

    public int getNpcListHP(int npcId) {
        if (npcId <= -1) {
            return 0;
        }
        if (NPCDefinitions.getDefinitions()[npcId] == null) {
            return 0;
        }
        return NPCDefinitions.getDefinitions()[npcId].getNpcHealth();

    }

    public String getNpcListName(int npcId) {
        if (npcId <= -1) {
            return "?";
        }
        if (NPCDefinitions.getDefinitions()[npcId] == null) {
            return "?";
        }
        return NPCDefinitions.getDefinitions()[npcId].getNpcName();
    }

    private void loadNPCSizes(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("ERROR: " + fileName + " does not exist.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                int npcId, size;
                try {
                    npcId = Integer.parseInt(line.split("\t")[0]);
                    size = Integer.parseInt(line.split("\t")[1]);
                    if (npcId > -1 && size > -1) {
                        if (NPCDefinitions.getDefinitions()[npcId] == null) {
                            NPCDefinitions.create(npcId, "None", 0, 0, size);
                        } else {
                            NPCDefinitions.getDefinitions()[npcId].setSize(size);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadNPCList(String fileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        File file = new File("./" + fileName);
        if (!file.exists()) {
            throw new RuntimeException("ERROR: NPC Configuration file does not exist.");
        }
        try (BufferedReader characterfile = new BufferedReader(new FileReader("./" + fileName))) {
            while ((line = characterfile.readLine()) != null && !line.equals("[ENDOFNPCLIST]")) {
                line = line.trim();
                int spot = line.indexOf("=");
                if (spot > -1) {
                    token = line.substring(0, spot);
                    token = token.trim();
                    token2 = line.substring(spot + 1);
                    token2 = token2.trim();
                    token2_2 = token2.replaceAll("\t\t", "\t");
                    token2_2 = token2_2.replaceAll("\t\t", "\t");
                    token2_2 = token2_2.replaceAll("\t\t", "\t");
                    token2_2 = token2_2.replaceAll("\t\t", "\t");
                    token2_2 = token2_2.replaceAll("\t\t", "\t");
                    token3 = token2_2.split("\t");
                    if (token.equals("npc")) {
                        newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]),
                                Integer.parseInt(token3[3]));
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public static void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                                 int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            // Misc.println("No Free Slot");
            return; // no free slot found
        }
        NPCDefinitions definition = NPCDefinitions.get(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.getHealth().setMaximumHealth(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
    }

    public static NPCDef[] getNpcDef() {
        return npcDef;
    }

    private ArrayList<int[]> vetionSpellCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> archSpellCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> hydraPoisonCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> fanaticSpellCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> corpSpellCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> olmSpellCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> explosiveSpawnCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> cerberusGroundCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> DragonGroundCoordinates = new ArrayList<>(3);
    private ArrayList<int[]> vasaRockCoordinates = new ArrayList<>(1);

    private void groundSpell(NPC npc, Player player, int startGfx, int endGfx, String coords, int time) {
        if (player == null) {
            return;
        }
        switch (coords) {

            case "hydra":
                player.coordinates = hydraPoisonCoordinates;
                break;

            case "vasa":
                player.coordinates = vasaRockCoordinates;
                break;

            case "vetion":
                player.coordinates = vetionSpellCoordinates;
                break;

            case "Dragon":
                player.coordinates = DragonGroundCoordinates;
                break;

            case "archaeologist":
                player.coordinates = archSpellCoordinates;
                break;

            case "fanatic":
                player.coordinates = fanaticSpellCoordinates;
                break;

            case "corp":
                player.coordinates = corpSpellCoordinates;
                break;

            case "olm":
                player.coordinates = olmSpellCoordinates;
                break;

            case "spawns":
                player.coordinates = explosiveSpawnCoordinates;

                List<NPC> exploader = Arrays.asList(NPCHandler.npcs);
                if (exploader.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 6768 && !n.isDead)) {
                    return;
                }
                break;

            case "cerberus":
                player.coordinates = cerberusGroundCoordinates;
                break;
        }
        int x = player.getX();
        int y = player.getY();
        player.coordinates.add(new int[]{x, y});
        for (int i = 0; i < 2; i++) {
            player.coordinates.add(new int[]{(x - 1) + Misc.random(3), (y - 1) + Misc.random(3)});
        }
        for (int[] point : player.coordinates) {
            int nX = npc.absX + 2;
            int nY = npc.absY + 2;
            int x1 = point[0] + 1;
            int y1 = point[1] + 2;
            int offY = (nX - x1) * -1;
            int offX = (nY - y1) * -1;
            if (startGfx > 0) {
                player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.getIndex()),
                        startGfx, 31, 0, -1, 5);
            }
            if (Objects.equals(coords, "spawns")) {
                spawnNpc(6768, point[0], point[1], 0, 0, -1, -1, -1, -1);
            }

        }
        if (Objects.equals(coords, "spawns")) {
            CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

                @Override
                public void execute(CycleEventContainer container) {
                    kill(6768, 0);
                    container.stop();
                }

            }, 7);
        }
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                for (int[] point : player.coordinates) {
                    int x2 = point[0];
                    int y2 = point[1];
                    if (endGfx > 0) {
                        player.getPA().createPlayersStillGfx(endGfx, x2, y2, player.heightLevel, 5);
                    }
                    if (Objects.equals(coords, "cerberus")) {
                        player.getPA().createPlayersStillGfx(1247, x2, y2, player.heightLevel, 5);
                    }
                }
                player.coordinates.clear();
                container.stop();
            }

        }, time);
    }

    private ArrayList<int[]> gorillaBoulder = new ArrayList<>(1);
    private Player closestPlayer1;
    private int closestDistance1;


    private void groundAttack(NPC npc, Player player, int startGfx, int endGfx, int explosionGfx, int time) {
        if (player == null) {
            return;
        }
        player.totalMissedGorillaHits = 3;
        player.coordinates = gorillaBoulder;
        int x = player.getX();
        int y = player.getY();
        player.coordinates.add(new int[]{x, y});

        for (int[] point : player.coordinates) {
            int nX = npc.absX + 2;
            int nY = npc.absY + 2;
            int x1 = point[0] + 1;
            int y1 = point[1] + 2;
            int offY = (nX - x1) * -1;
            int offX = (nY - y1) * -1;
            if (startGfx > 0)
                player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.getIndex()),
                        startGfx, 31, 0, -1, 5); // 304
        }
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                for (int[] point : player.coordinates) {
                    int x2 = point[0];
                    int y2 = point[1];
                    if (endGfx > 0)
                        player.getPA().createPlayersStillGfx(endGfx, x2, y2, player.heightLevel, 5); // 303
                }
                container.stop();
            }

        }, 3);
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                for (int[] point : player.coordinates) {
                    int x2 = point[0];
                    int y2 = point[1];
                    if (explosionGfx > 0)
                        player.getPA().createPlayersStillGfx(explosionGfx, x2, y2, player.heightLevel, 5); // 305
                }
                npc.attackType = CombatType.getRandom(CombatType.MELEE, CombatType.RANGE, CombatType.MAGE);
                player.coordinates.clear();
                container.stop();
            }

        }, time);
    }

    public static void kill(int minHeight, int maxHeight, int... npcType) {
        nonNullStream()
                .filter(n -> IntStream.of(npcType).anyMatch(type -> type == n.npcType) && n.getHeight() >= minHeight && n.getHeight() <= maxHeight)
                .forEach(npc -> npc.isDead = true);
    }

    public static void kill(int npcType, int height) {
        Arrays.stream(npcs).filter(Objects::nonNull).filter(n -> n.npcType == npcType && n.heightLevel == height)
                .forEach(npc -> npc.isDead = true);
    }

    public static void destroy(NPC npc) { //TOB
        IntStream.range(0, npcs.length).forEach(index -> {
            if (npcs[index] != null && npcs[index] == npc) {
                npcs[index].setInstance(null);
                npcs[index] = null;
            }
        });
    }


    private void createVetionEarthquake(Player player) {
        player.getPA().shakeScreen(3, 2, 3, 2);
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                player.getPA().sendFrame107();
                container.stop();
            }

        }, 4);
    }

    public static void deregister(NPC npc) {
        if (npc == null)
            return;
        npc.isDead = true;
        npc.needRespawn = false;
        npc.setInstance(null);
        if (npcs[npc.getIndex()] != null && npcs[npc.getIndex()] == npc)
            npcs[npc.getIndex()] = null;
    }

    public static Stream<NPC> nonNullStream() {
        return Stream.of(npcs).filter(Objects::nonNull);
    }

    public static boolean anyExists(int x, int y, int height) {
        return nonNullStream().filter(npc -> npc.getHeight() == height).anyMatch(npc -> npc.getX() == x && npc.getY() == y);
    }

    /**
     * Handles transforming npcs when clicked on to attack
     *
     * @param npc
     * @return
     */
    public static boolean transformOnAttack(NPC npc) {
        if (npc == null) {
            return true;
        }
        switch (npc.npcType) {
            case FragmentOfSeren.FRAGMENT_ID:
                npc.requestTransform(FragmentOfSeren.NPC_ID);
                npc.startAnimation(FragmentOfSeren.TOUCHED_ANIMATED);
                FragmentOfSeren.isAttackable = false;
                CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {

                    @Override
                    public void execute(CycleEventContainer container) {
                        FragmentOfSeren.isAttackable = true;
                        npc.forceChat("How dare you awaken me!");
                        npc.startAnimation(65535);
                        container.stop();
                    }

                }, 13);
                //npc.gfx100(1582);
                return true;
            default:
                return false;
        }
    }
}
