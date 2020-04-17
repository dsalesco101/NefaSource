package ethos.model.npcs;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ethos.Config;
import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.godwars.God;
import ethos.model.content.godwars.GodwarsNPCs;
import ethos.model.entity.Entity;
import ethos.model.minigames.inferno.AncestralGlyph;
import ethos.model.minigames.inferno.InfernoWave;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.lighthouse.DisposeType;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.npcs.bosses.CorporealBeast;
import ethos.model.npcs.bosses.Kraken;
import ethos.model.npcs.bosses.Scorpia;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.npcs.bosses.hydra.AlchemicalHydra;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.wildypursuit.FragmentOfSeren;
import ethos.model.npcs.bosses.wildypursuit.Sotetseg;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

import static ethos.model.npcs.NPCHandler.deregister;
import static ethos.model.npcs.NPCHandler.getHydraInstance;
import static ethos.model.npcs.NPCHandler.getNpc;
import static ethos.model.npcs.NPCHandler.isFightCaveNpc;
import static ethos.model.npcs.NPCHandler.npcs;
import static ethos.model.npcs.NPCHandler.spawnNpc;

public class NPCProcess {

    private final NPCHandler npcHandler;

    NPCProcess(NPCHandler npcHandler) {
        this.npcHandler = npcHandler;
    }

    public void process(int i) {
        if (npcs[i].getInstance() != null) {
            npcs[i].getInstance().tick(npcs[i]);
            if (npcs[i] == null) {
                return;
            }
            if (npcs[i].getInstance().isDisposed()) {
                NPCHandler.deregister(npcs[i]);
                return;
            }
        }
        int index = npcs[i].getIndex();
        NPC npc = npcs[i];
        int type = npcs[i].npcType;
        Optional<AlchemicalHydra> hydraInstance = getHydraInstance(i);
        Player slaveOwner = (PlayerHandler.players[npcs[i].summonedBy]);
        if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
            npcs[i].absX = 0;
            npcs[i].absY = 0;
        }
        if (npcs[i] != null && slaveOwner != null && slaveOwner.hasFollower
                && (!slaveOwner.goodDistance(npcs[i].getX(), npcs[i].getY(), slaveOwner.absX, slaveOwner.absY,
                15) || slaveOwner.heightLevel != npcs[i].heightLevel)
                && npcs[i].summoner) {
            npcs[i].absX = slaveOwner.absX;
            npcs[i].absY = slaveOwner.absY;
            npcs[i].heightLevel = slaveOwner.heightLevel;

        }
        if (npcs[i].actionTimer > 0) {
            npcs[i].actionTimer--;
        }

        if (npcs[i].freezeTimer > 0) {
            npcs[i].freezeTimer--;
        }
        if (npcs[i].hitDelayTimer > 0) {
            npcs[i].hitDelayTimer--;
        }
        if (npcs[i].hitDelayTimer == 1) {
            npcs[i].hitDelayTimer = 0;
            npcHandler.applyDamage(i);
        }
        if (npcs[i].attackTimer > 0) {
            npcs[i].attackTimer--;
        }
        if (npcs[i].npcType == 7553) {
            npc.walkingHome = true;
        }
        if (npcs[i].npcType == 7555) {
            npc.walkingHome = true;
        }
        if (npcs[i].npcType == 1143) {
            if (Misc.random(30) == 3) { //how often he says it
                npcs[i].updateRequired = true; //update to the players view
                npcs[i].forceChat("Sell your PvM items here for a limited time!"); //npc forced text example
            }
        }

        if (npcs[i].npcType == 306) {
            if (Misc.random(50) == 3) {
                npcs[i].forceChat("Speak to me if you wish to learn more about this land!");
            }
        }


        if (npcs[i].getHealth().getCurrentHealth() > 0 && !npcs[i].isDead) {
            if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
                if (npcs[i].getHealth().getCurrentHealth() < (npcs[i].getHealth().getMaximumHealth() / 2)
                        && !npcs[i].spawnedMinions) {
                    NPCHandler.spawnNpc(5054, npcs[i].getX() - 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
                    NPCHandler.spawnNpc(5054, npcs[i].getX() + 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
                    npcs[i].spawnedMinions = true;
                }
            }
        }
        if (npcs[i].npcType == 6600 && !npcs[i].isDead) {
            NPC runiteGolem = getNpc(6600);
            if (runiteGolem != null && !runiteGolem.isDead) {
                npcs[i].isDead = true;
                npcs[i].needRespawn = false;
                npcs[i].actionTimer = 0;
            }
        }
        if (npcs[i].spawnedBy > 0) { // delete summons npc
            Player spawnedBy = PlayerHandler.players[npcs[i].spawnedBy];
            if (spawnedBy == null || spawnedBy.heightLevel != npcs[i].heightLevel || spawnedBy.respawnTimer > 0
                    || !spawnedBy.goodDistance(npcs[i].getX(), npcs[i].getY(), spawnedBy.getX(),
                    spawnedBy.getY(),
                    NPCHandler.isFightCaveNpc(i) ? 60 : NPCHandler.isSkotizoNpc(i) ? 60 : 20)) {

                npcs[i] = null;
            }
        }
        if (npcs[i] == null)
            return;
        if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
            npcs[i].lastX = npcs[i].getX();
            npcs[i].lastY = npcs[i].getY();
        }
        hydraInstance.ifPresent(AlchemicalHydra::onTick);
        if (npcs[i] == null)
            return;

        // Inferno glyph movement
        if (npc.npcType == InfernoWave.ANCESTRAL_GLYPH) {
            if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
                AncestralGlyph.handleMovement(PlayerHandler.players[npcs[i].spawnedBy], npcs[i]);
            }
        }

        if (type == 6615) {
            if (npcs[i].walkingHome) {
                npcs[i].getHealth().setCurrentHealth(200);
            }
            Scorpia.spawnHealer();
        }

        if (type == 319) {
            if (npcs[i].walkingHome) {
                npcs[i].getHealth().setCurrentHealth(2000);
            }
            CorporealBeast.checkCore();
        }

        if (type == 8026 || type == 8027) {
            npcs[i].setFacePlayer(false);
        }
        if (type == 8028) {
            npcs[i].setFacePlayer(true);
        }
        if (type >= 2042 && type <= 2044 && npcs[i].getHealth().getCurrentHealth() > 0) {
            Player player = PlayerHandler.players[npcs[i].spawnedBy];
            if (player != null && player.getZulrahEvent().getNpc() != null
                    && npcs[i].equals(player.getZulrahEvent().getNpc())) {
                int stage = player.getZulrahEvent().getStage();
                if (type == 2042) {
                    if (stage == 0 || stage == 1 || stage == 4 || stage == 9 && npcs[i].totalAttacks >= 20
                            || stage == 11 && npcs[i].totalAttacks >= 5) {
                        return;
                    }
                }
                if (type == 2044) {
                    if ((stage == 5 || stage == 8) && npcs[i].totalAttacks >= 5) {
                        return;
                    }
                }
            }
        }

        /**
         * Attacking player
         **/
        if (npcHandler.isAggressive(i, false)
                && !npc.underAttack
                && npc.killerId <= 0
                && !npc.isDead
                && !npcHandler.switchesAttackers(i)
                && npc.inMulti()
                && !Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS)
                && !Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
            Player closestPlayer = null;
            double closestDistance = Integer.MAX_VALUE;
            God god = GodwarsNPCs.NPCS.get(npc.npcType);
            for (Player player : PlayerHandler.players) {
                if (player == null) {
                    continue;
                }
                if (player.isIdle)
                    continue;

                if (god != null && player.inGodwars() && player.getEquippedGodItems() != null
                        && player.getEquippedGodItems().contains(god)) {
                    continue;
                }
                int[] revs = {7931, 7932, 7933, 7934, 7935, 7936, 7937, 7938, 7939, 7940};
                if(player.playerEquipment[player.playerHands] == 21816) {
                    for(int b = 0; b <= revs.length; b++) {
                        if(npc.getDefinition().getNpcId() == b)
                            npcHandler.isAggressive(npc.getDefinition().getNpcId(), false);
                    }
                    continue;
                }

                /**
                 * Skips attacking a player if mode set to invisible
                 */
                if (player.isInvisible()) {
                    continue;
                }

                if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) <= 26) {		// Optimization
                    int distanceRequired = npc.getSize() + 1;
                    double distance = npcs[i].getDistance(player.absX, player.absY);
                    if (distance < closestDistance && distance <= distanceRequired) {
                        //if (distance < closestDistance && distance <= distanceRequired(i) + followDistance(i)) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }
            }
            if (closestPlayer != null) {
                npc.killerId = closestPlayer.getIndex();
                closestPlayer.underAttackByNpc = npc.getIndex();
            }
        } else if (npcHandler.isAggressive(i, false)
                && !npcs[i].underAttack
                && !npcs[i].isDead
                && (npcHandler.switchesAttackers(i) || Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS))) {

            if (System.currentTimeMillis() - npcs[i].lastRandomlySelectedPlayer > 10000) {
                int player = npcHandler.getCloseRandomPlayer(i);

                if (player > 0) {
                    npcs[i].killerId = player;
                    PlayerHandler.players[player].underAttackByPlayer = i;
                    PlayerHandler.players[player].underAttackByNpc = i;
                    npcs[i].lastRandomlySelectedPlayer = System.currentTimeMillis();
                }
            }
        }

        if (npcs[i].killerId <= 0) {
            if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000 && !npcs[i].underAttack) {
                npcs[i].underAttackBy = 0;
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
            }
            if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 10000) {
                npcs[i].underAttackBy = 0;
                npcs[i].underAttack = false;
                npcs[i].randomWalk = true;
            }
        }

        if ((npcs[i].killerId > 0 || npcs[i].underAttack)
                && !npcs[i].walkingHome
                && npcHandler.retaliates(npcs[i].npcType)) {
            if (!npcs[i].isDead) {
                int p = npcs[i].killerId;
                if (PlayerHandler.players[p] != null) {
                    if (!npcs[i].summoner) {
                        Player c = PlayerHandler.players[p];
                        if (c.getInferno() != null && c.getInferno().kill.contains(npcs[i])) {
                            return;
                        }
                        npcHandler.followPlayer(i, c.getIndex());
                        if (npcs[i] == null)
                            return;
                        if (npcs[i].attackTimer == 0) {
                            npcHandler.attackPlayer(c, i);
                        }
                    } else {
                        Player c = PlayerHandler.players[p];
                        if (c.absX == npcs[i].absX && c.absY == npcs[i].absY) {
                            npcHandler.stepAway(i);
                            npcs[i].randomWalk = false;
                            if (npcs[i].npcType == InfernoWave.JAL_NIB) {
                                return;
                            }
                            npcs[i].facePlayer(c.getIndex());
                        } else {
                            if (c.getInferno() != null && c.getInferno().kill.contains(npcs[i])) {
                                return;
                            }
                            npcHandler.followPlayer(i, c.getIndex());
                        }
                    }
                } else {
                    npcs[i].killerId = 0;
                    npcs[i].underAttack = false;
                    npcs[i].facePlayer(0);
                }
            }
        }

        /**
         *
         * Random walking and walking home
         **/
        if (npcs[i] == null)
            return;
        if ((!npcs[i].underAttack) && !isFightCaveNpc(i) && npcs[i].randomWalk && !npcs[i].isDead) {
            npcs[i].facePlayer(0);
            npcs[i].killerId = 0;
            // handleClipping(i);
            if (npcs[i].spawnedBy == 0) {
                if ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE)
                        || (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE)
                        || (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE)
                        || (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)
                        && npcs[i].npcType != 1635 && npcs[i].npcType != 1636 && npcs[i].npcType != 1637
                        && npcs[i].npcType != 1638 && npcs[i].npcType != 1639 && npcs[i].npcType != 1640
                        && npcs[i].npcType != 1641 && npcs[i].npcType != 1642 && npcs[i].npcType != 1643
                        && npcs[i].npcType != 1654 && npcs[i].npcType != 7302) {
                    npcs[i].walkingHome = true;
                }
            }

            if (npcs[i].walkingType >= 0) {
                switch (npcs[i].walkingType) {
                    case 5:
                        npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
                        break;
                    case 4:
                        npcs[i].turnNpc(npcs[i].absX + 1, npcs[i].absY);
                        break;
                    case 3:
                        npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
                        break;
                    case 2:
                        npcs[i].turnNpc(npcs[i].absX, npcs[i].absY + 1);
                        break;
                }
            }

            if (npcs[i].walkingType == 1 && (!npcs[i].underAttack) && !npcs[i].walkingHome) {
                if (System.currentTimeMillis() - npcs[i].getLastRandomWalk() > npcs[i].getRandomWalkDelay()) {
                    int direction = Misc.random3(8);
                    int movingToX = npcs[i].getX() + NPCClipping.DIR[direction][0];
                    int movingToY = npcs[i].getY() + NPCClipping.DIR[direction][1];
                    if (npcs[i].npcType >= 1635 && npcs[i].npcType <= 1643 || npcs[i].npcType == 1654
                            || npcs[i].npcType == 7302) {
                        NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() - 1 + Misc.random(8),
                                npcs[i].getY() - 1 + Misc.random(8));
                    } else {
                        if (Math.abs(npcs[i].makeX - movingToX) <= 1 && Math.abs(npcs[i].makeY - movingToY) <= 1
                                && NPCDumbPathFinder.canMoveTo(npcs[i], direction)) {
                            NPCDumbPathFinder.walkTowards(npcs[i], movingToX, movingToY);
                        }
                    }
                    npcs[i].setRandomWalkDelay(TimeUnit.SECONDS.toMillis(1 + Misc.random(2)));
                    npcs[i].setLastRandomWalk(System.currentTimeMillis());
                }
            }
        }
        if (npcs[i].walkingHome) {
            if (!npcs[i].isDead) {
                NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].makeX, npcs[i].makeY);
                if (npcs[i].moveX == 0 && npcs[i].moveY == 0) {
                    npcs[i].teleport(npcs[i].makeX, npcs[i].makeY, npcs[i].heightLevel);
                    if (npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
                        npcs[i].walkingHome = false;
                    }
                    return;
                }
                if (npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
                    npcs[i].walkingHome = false;
                }
            } else {
                npcs[i].walkingHome = false;
            }
        }
        /**
         * Npc death
         */

        if (npcs[i].isDead) {
            Player player = PlayerHandler.players[npcs[i].spawnedBy];

            if (npcs[i].actionTimer == 0 && !npcs[i].applyDead && !npcs[i].needRespawn) {
                if (npcs[i].npcType == 8028) {
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                            Vorkath.drop(player);
                            Vorkath.drop(player);
                            int dropHead = Misc.random(50);
                            //player.sendMessage(dropHead + " <- that");
                            if (dropHead == 1 || player.getNpcDeathTracker().getTracker().get("vorkath") == 50) {
                                Server.itemHandler.createGroundItem(player, 2425, Vorkath.lootCoordinates[0],
                                        Vorkath.lootCoordinates[1], player.heightLevel, 1, player.getIndex());
                            }
                            Server.npcHandler.spawnNpc(player, 8026, 2272, 4065, player.getHeight(), 0, 750,
                                    player.antifireDelay > 0 ? 0 : 61, 560, 114, true, false);
                            container.stop();
                        }

                        @Override
                        public void onStopped() {
                        }
                    }, 9);
                }

                if (npcs[i].npcType == 9021 || npcs[i].npcType == 9022 || npcs[i].npcType == 9023) {
                    npcs[i].startAnimation(8421);
                    npcs[i].requestTransform(9024);
                    npcs[i].getHealth().reset();
                    npcs[i].isDead = true;
                    npcs[i].forceChat("RAAAAARGHHHHH!");
                    npcs[i].applyDead = true;
                }
                if (npcs[i].npcType == 6618) {
                    npcs[i].forceChat("Ow!");
                }
                if (npcs[i].npcType == 8781) {
                    npcs[i].forceChat("You will pay for this!");
                    npcs[i].startAnimation(-1);
                    npcs[i].gfx0(1005);
                }
                if (npcs[i].npcType == 6611) {
                    npcs[i].requestTransform(6612);
                    npcs[i].getHealth().reset();
                    npcs[i].isDead = false;
                    npcs[i].spawnedMinions = false;
                    npcs[i].forceChat("Do it again!!");
                } else {
                    if (npcs[i].npcType == 6612) {
                        npcs[i].npcType = 6611;
                        npcs[i].spawnedMinions = false;
                    }
                    if (npcs[i].npcType == 9024) {
                        npcs[i].npcType = 9021;
                    }


                    if (npcs[i].npcType == 1605) {
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                npcHandler.spawnNpc(player, 1606, 3106, 3934, 0, 1, 30, 24, 70, 60, true, true);
                                container.stop();
                            }

                            @Override
                            public void onStopped() {
                            }
                        }, 5);
                    }
                    Player killer1 = PlayerHandler.players[npcs[i].spawnedBy];
                    if (npcs[i].npcType == 1606) {
                        killer1.roundNpc=3;
                        killer1.spawned = false;
                        npcHandler.checkMa(killer1,i);
                    }
                    if (npcs[i].npcType == 1607) {
                        killer1.spawned = false;
                        npcHandler.checkMa(killer1,i);
                    }
                    if (npcs[i].npcType == 1608) {
                        killer1.spawned = false;
                        npcHandler.checkMa(killer1,i);
                    }

                    if (npcs[i].npcType == 1609) {
                        if (killer1 != null) {
                            killer1.spawned = false;
                            npcHandler.checkMa(killer1,i);
                        }
                    }
                    /*
                     * if (npcs[i].npcType == 7554) { //TODO animate objec
                     * player.getRaids().finishRaids(); }
                     */
                    npcs[i].updateRequired = true;
                    npcs[i].facePlayer(0);
                    Entity killer = npcs[i].calculateKiller();

                    if (killer != null) {
                        npcs[i].killedBy = killer.getIndex();
                    }
                    if (npcs[i].npcType == 963) {
                        npcs[i].isDead = false;
                        npcs[i].npcType = 965;
                        npcs[i].requestTransform(965);
                        npcs[i].getHealth().reset();
                    } else if(!hydraInstance.isPresent()){
                        npcs[i].startAnimation(npcHandler.getDeadEmote(i));
                    }

                    npcs[i].freezeTimer = 0;
                    npcs[i].applyDead = true;

                    if (npcs[i].npcType == 3118) {
                        spawnNpc(3120, npcs[i].absX, npcs[i].absY, player.heightLevel, 10, 2, 15, 15, 0);
                        spawnNpc(3120, npcs[i].absX, npcs[i].absY + 1, player.heightLevel, 10, 2, 15, 15, 0);
                    }
                    if (npcs[i].npcType == InfernoWave.JAL_AK && player.getInferno() != null) {
                        Server.npcHandler.spawnNpc(player, InfernoWave.JAL_AKREK_KET, npcs[i].absX,
                                npcs[i].absY, player.heightLevel, 1,
                                InfernoWave.getHp(InfernoWave.JAL_AKREK_KET),
                                InfernoWave.getMax(InfernoWave.JAL_AKREK_KET),
                                InfernoWave.getAtk(InfernoWave.JAL_AKREK_KET),
                                InfernoWave.getDef(InfernoWave.JAL_AKREK_KET), true, false);
                        Server.npcHandler.spawnNpc(player, InfernoWave.JAL_AKREK_XIL, npcs[i].absX,
                                npcs[i].absY + 1, player.heightLevel, 1,
                                InfernoWave.getHp(InfernoWave.JAL_AKREK_XIL),
                                InfernoWave.getMax(InfernoWave.JAL_AKREK_XIL),
                                InfernoWave.getAtk(InfernoWave.JAL_AKREK_XIL),
                                InfernoWave.getDef(InfernoWave.JAL_AKREK_XIL), true, false);
                        Server.npcHandler.spawnNpc(player, InfernoWave.JAL_AKREK_MEJ,
                                npcs[i].absX + 1, npcs[i].absY + 1, player.heightLevel, 1,
                                InfernoWave.getHp(InfernoWave.JAL_AKREK_MEJ),
                                InfernoWave.getMax(InfernoWave.JAL_AKREK_MEJ),
                                InfernoWave.getAtk(InfernoWave.JAL_AKREK_MEJ),
                                InfernoWave.getDef(InfernoWave.JAL_AKREK_MEJ), true, false);
                        player.getInferno().setKillsRemaining(
                                player.getInferno().getKillsRemaining() + 3);
                    }

                    if (player != null) {
                        npcHandler.tzhaarDeathHandler(player, i);
                        npcHandler.infernoDeathHandler(player, i);
                    }

                    npcs[i].actionTimer = npcHandler.getDeathDelay(i);
                    npcHandler.resetPlayersInCombat(i);
                    npcHandler.killedBarrow(i);
                }
            } else if (npcs[i].actionTimer == 0 && npcs[i].applyDead && !npcs[i].needRespawn) {

                int killerIndex = npcs[i].killedBy;
                npcs[i].needRespawn = true;
                npcs[i].actionTimer = npcHandler.getRespawnTime(i); // respawn time
                npcHandler.dropItems(i);
                if (killerIndex < PlayerHandler.players.length - 1) {
                    Player target = PlayerHandler.players[npcs[i].killedBy];

                    if (target != null) {
                        target.getSlayer().killTaskMonster(npcs[i]);
                        /*
                         * if (target.getSlayer().isSuperiorNpc()) {
                         * target.getSlayer().handleSuperiorExp(npcs[i]); }
                         */
                    }
                }
                if(npc.getRaidsInstance() != null){
                    Optional<Player> plrOpt = PlayerHandler.getPlayerByIndex(npc.killedBy);
                    npc.getRaidsInstance().handleMobDeath(plrOpt.orElse(null), type);
                }
                if(npc.inXeric()){
                    Player killer = PlayerHandler.players[npc.killedBy];


                    npcHandler.xericDeathHandler(killer, index);

                }
                npcHandler.appendBossKC(i);
                npcHandler.appendKillCount(i);
                npcHandler.handleGodwarsDeath(npc);
                npcHandler.handleDiaryKills(npc);
                npcHandler.handleDailyKills(npc);
                npcs[i].absX = npcs[i].makeX;
                npcs[i].absY = npcs[i].makeY;
                npcs[i].getHealth().reset();
                npcs[i].startAnimation(0x328);

                /**
                 * Actions on certain npc deaths
                 */
                switch (npcs[i].npcType) {

                    case 965:
                        npcs[i].npcType = 963;
                        break;

                    case Sotetseg.NPC_ID:
                        PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ the wildy boss [@red@Sotetseg@blu@] has been defeated!");
                        Sotetseg.rewardPlayers(player);
                        Sotetseg.specialAmount = 0;
                        break;
                    case FragmentOfSeren.NPC_ID:
                        PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ the wildy boss [@red@Seren@blu@] has been defeated!");
                        FragmentOfSeren.rewardPlayers(player);
                        FragmentOfSeren.specialAmount = 0;
                        break;
                    case FragmentOfSeren.CRYSTAL_WHIRLWIND:
                        FragmentOfSeren.activePillars.remove(npcs[i]);
                        if (FragmentOfSeren.activePillars.size() == 0) {
                            FragmentOfSeren.isAttackable = true;
                        }
                        npcs[i] = null;
                        break;
                    case 3127:
                        player.getFightCave().stop();
                        break;
                    /*
                     * case 5762: //Not in use case 5744: player.raidPoints += 1;
                     * player.sendMessage("You currently have @red@" + player.raidPoints +
                     * "@bla@ Assault Points."); break; case 7595: player.raidPoints += 2;
                     * player.sendMessage("You currently have @red@" + player.raidPoints +
                     * "@bla@ Assault Points."); break;
                     */
                    case 6367:
                    case 6369:
                    case 6370:
                    case 6371:
                    case 6372:
                    case 6373:
                    case 6374:
                    case 6375:
                    case 6376:
                    case 6377:
                    case 6378:
                        RecipeForDisaster rfd = player.getrecipeForDisaster();
                        if (player.rfdWave < 5) {
                            rfd.wave();
                        }
                        player.rfdWave++;
                        player.rfdGloves++;

                        switch (player.rfdWave) {
                            case 0:
                                player.getDH().sendNpcChat1("You DARE come HERE?!?!", 6368, "Culinaromancer");
                                player.nextChat = -1;
                                break;

                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                player.getDH().sendNpcChat1("NOOOooo...", 6368, "Culinaromancer");
                                player.nextChat = -1;
                                break;

                            case 6:
                                player.getDH().sendNpcChat2("You have caused me enough grief!",
                                        "I guess I'll have to finish you off myself!", 6368, "Culinaromancer");
                                player.nextChat = -1;
                                break;
                        }
                        break;
                    case 6368:
                        RecipeForDisaster rfdd = player.getrecipeForDisaster();
                        if (rfdd != null) {
                            rfdd.end(DisposeTypes.COMPLETE);
                        }
                        break;

                    case 5862:
                        Cerberus cerb = player.getCerberus();
                        if (cerb != null) {
                            cerb.end(DisposeTypes.COMPLETE);
                        }
                        break;

                    case Skotizo.SKOTIZO_ID:
                        if (player.getSkotizo() != null) {
                            player.getSkotizo().end(DisposeTypes.COMPLETE);
                        }
                        break;

                    case InfernoWave.TZKAL_ZUK:
                        if (player.getInferno() != null) {
                            player.getInferno().end(DisposeTypes.COMPLETE);
                        }
                        break;

                    case Skotizo.AWAKENED_ALTAR_NORTH:
                        Server.getGlobalObjects().remove(28923, 1694, 9904, player.getSkotizo().getHeight()); // Remove
                        // North
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904,
                                player.getSkotizo().getHeight(), 2, 10, -1, -1)); // North - Empty Altar
                        player.getPA().sendChangeSprite(29232, (byte) 0);
                        player.getSkotizo().altarCount--;
                        player.getSkotizo().northAltar = false;
                        player.getSkotizo().altarMap.remove(1);
                        break;
                    case Skotizo.AWAKENED_ALTAR_SOUTH:
                        Server.getGlobalObjects().remove(28923, 1696, 9871, player.getSkotizo().getHeight()); // Remove
                        // South
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871,
                                player.getSkotizo().getHeight(), 0, 10, -1, -1)); // South - Empty Altar
                        player.getPA().sendChangeSprite(29233, (byte) 0);
                        player.getSkotizo().altarCount--;
                        player.getSkotizo().southAltar = false;
                        player.getSkotizo().altarMap.remove(2);
                        break;
                    case Skotizo.AWAKENED_ALTAR_WEST:
                        Server.getGlobalObjects().remove(28923, 1678, 9888, player.getSkotizo().getHeight()); // Remove
                        // West
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888,
                                player.getSkotizo().getHeight(), 1, 10, -1, -1)); // West - Empty Altar
                        player.getPA().sendChangeSprite(29234, (byte) 0);
                        player.getSkotizo().altarCount--;
                        player.getSkotizo().westAltar = false;
                        player.getSkotizo().altarMap.remove(3);
                        break;
                    case Skotizo.AWAKENED_ALTAR_EAST:
                        Server.getGlobalObjects().remove(28923, 1714, 9888, player.getSkotizo().getHeight()); // Remove
                        // East
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888,
                                player.getSkotizo().getHeight(), 3, 10, -1, -1)); // East - Empty Altar
                        player.getPA().sendChangeSprite(29235, (byte) 0);
                        player.getSkotizo().altarCount--;
                        player.getSkotizo().eastAltar = false;
                        player.getSkotizo().altarMap.remove(4);
                        break;
                    case Skotizo.DARK_ANKOU:
                        player.getSkotizo().ankouSpawned = false;
                        break;

                    case 6615:
                        Scorpia.stage = 0;
                        break;

                    case 319:
                        CorporealBeast.stage = 0;
                        break;


                    case 6600:
                        spawnNpc(6601, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
                        break;

                    case 6601:
                        spawnNpc(6600, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
                        npcs[i] = null;
                        NPC golem = getNpc(6600);
                        if (golem != null) {
                            golem.actionTimer = 150;
                        }
                        break;
                }

                if (npcs[i] != null) {
                    if (DagannothMother.RANGE_OF_IDS.contains(npcs[i].npcType)) {
                        DagannothMother dm = player.getDagannothMother();

                        if (dm != null) {
                            dm.end(DisposeType.COMPLETE);
                        }
                    }
                }
            } else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn && npcs[i].npcType != 1739
                    && npcs[i].npcType != 1740 && npcs[i].npcType != 1741 && npcs[i].npcType != 1742) {
                if (player != null) {
                    if (npcs[i].npcType == Kraken.KRAKEN_ID && player.getKraken().getInstance() != null) {
                        player.getKraken().spawnKraken();
                    }
                    npcs[i] = null;
                } else {
                    if(hydraInstance.isPresent()) {
                        hydraInstance.get().respawn();
                        deregister(npcs[i]);
                        return;
                    }

                    // Child respawns with parent
                    if (npcs[i].getParent() != null) {
                        return;
                    }

                    // Parent won't respawn until children are dead
                    if (npcs[i].getChildren().stream().allMatch(child -> child.needRespawn)) {
                        npcs[i].getChildren().forEach(child -> npcHandler.respawn(child.getIndex()));
                        npcHandler.respawn(i);
                    }
                }
            }
        }
    }

}
