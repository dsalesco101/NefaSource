package ethos.model.minigames.inferno;

import java.io.PrintWriter;
import java.util.Date;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.InstancedArea;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class Inferno extends Tzkalzuk {

    private static final int DEFAULT_WAVE = 59;

    public static void startInferno(Player player, int wave) {
        player.setInferno(new Inferno(player, Boundary.INFERNO, InstancedAreaManager.getSingleton().getNextOpenHeight(Boundary.INFERNO)));
        player.getInferno().create(wave);
    }

    public static void moveToExit(Player player) {
        player.teleportToX = Tzkalzuk.EXIT.getX();
        player.teleportToY = Tzkalzuk.EXIT.getY();
        player.heightLevel = 0;
    }

    public static int getDefaultWave() {
        return DEFAULT_WAVE;
    }

    public static void reset(Player player) {
        player.setInferno(null);
    }

    public static void gamble(Player player) {
        if (!player.getItems().playerHasItem(21295)) {
            player.sendMessage("You do not have an infernal cape.");
            return;
        }

        player.getItems().deleteItem(21295, 1);

        if (Misc.random(25) == 0) {
            PlayerHandler.executeGlobalMessage("[@red@PET@bla@] @cr20@<col=255> " + player.playerName + "</col> received a pet from <col=255>TzKal-Zuk</col>.");
            player.getItems().addItemUnderAnyCircumstance(21291, 1);
            player.getDH().sendDialogues(74, 7690);
        } else {
            player.getDH().sendDialogues(73, 7690);
            return;
        }
    }

    private int infernoWaveId = 50;
    private int infernoWaveType = 50;
    public int wall1Alive;
    public int wall1Hp;
    public int wall2Alive;
    public int wall2Hp;
    public int wall3Alive;
    public int wall3Hp;
    private int killsRemaining;
    private NPC wall1;
    private NPC wall2;
    private NPC wall3;
    private NPC nib1;
    private NPC nib2;
    private NPC nib3;
    boolean started;

    public Inferno(Player player, Boundary boundary, int height) {
        super(player, boundary, height);
    }

    public void create(int wave) {
        player.getPA().removeAllWindows();
        player.getPA().movePlayer(2271, 5329, height);
        add(player);
        setInfernoWaveType(wave);
        setInfernoWaveId(wave);
        wall1Alive = 0;
        wall1Hp = 1000;
        wall2Alive = 0;
        wall2Hp = 1000;
        wall3Alive = 0;
        wall3Hp = 1000;
        player.infernoLeaveTimer = System.currentTimeMillis();
        createWalls();
        spawn();
        player.sendMessage("Welcome to the Inferno. Your first wave will start soon. Please wait...", 255);
    }

    public void leaveGame() {
        killAllSpawns();
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height).setInstance(this));
        Server.getGlobalObjects().remove(30354, 2270, 5333, height, this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2259, 5349, height, this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, height).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2278, 5349, height, this);
        if (wall1 != null) {
            wall1.isDead = true;
        }
        if (wall2 != null) {
            wall2.isDead = true;
        }
        if (wall3 != null) {
            wall3.isDead = true;
        }
        player.sendMessage("You have left the Inferno minigame.");
        player.getPA().movePlayer(2497, 5116, 0);
        setInfernoWaveType(50);
        setInfernoWaveId(50);
        wall1Alive = 0;
        wall1Hp = 0;
        wall2Alive = 0;
        wall2Hp = 0;
        wall3Alive = 0;
        wall3Hp = 0;
        dispose();
    }

    public void stop() {
        player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * getInfernoWaveType()) + Misc.random(5000));
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Congratulations for finishing the Inferno!");
        player.waveInfo[getInfernoWaveType() - 1] += 1;
        reset(player);
        player.nextChat = 0;
        player.setRunEnergy(100);
        killAllSpawns();
        player.getInferno().zukDead = false;
    }

    public void handleDeath() {
        int wave = getInfernoWaveId() + 1;
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Unfortunately you died on wave " + wave + ". Better luck next time.");
        player.nextChat = 0;
        reset(player);
        killAllSpawns();
    }

    /**
     * Disposes of the content by moving the player and finalizing and or removing any left over content.
     *
     * @param dispose the type of dispose
     */
    public final void end(DisposeTypes dispose) {
        if (player == null) {
            return;
        }

        if (dispose == DisposeTypes.COMPLETE) {
            NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
            NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
            for (NPC kill : player.getInferno().kill) {
                if (kill != null) {
                    kill.isDead = true;
                }
            }
            player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * getInfernoWaveType()) + Misc.random(5000));
            player.getItems().addItemUnderAnyCircumstance(21295, 1);
            PlayerHandler.executeGlobalMessage("@cr10@@red@" + Misc.capitalize(player.getName()) + " has defeated the Inferno.");

            try {
                PrintWriter writer = new PrintWriter("./Data/infernowins.txt", "UTF-8");
                writer.println(Misc.capitalize(player.getName()) + " has defeated the Inferno on: " + new Date().toString());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.nextChat = 0;
            player.setRunEnergy(100);
            player.getInferno().zukDead = true;
        } else if (dispose == DisposeTypes.INCOMPLETE) {
            NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
            NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
            for (NPC kill : player.getInferno().kill) {
                if (kill != null) {
                    kill.isDead = true;
                }
            }
        }

        Inferno.reset(player);
        Inferno.moveToExit(player);
    }

    @Override
    public void onDispose() {
        end(DisposeTypes.INCOMPLETE);
    }

    public void spawn() {
        final int[][] type = InfernoWave.LEVEL;
        if (getInfernoWaveId() >= type.length && getInfernoWaveType() > 0 && Boundary.isIn(player, Boundary.INFERNO)) {
            if (player.getInferno().zukDead)
                stop();
            return;
        }

        final InstancedArea instance = this;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer event) {
                if (player == null) {
                    event.stop();
                    return;
                }
                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    reset(player);
                    event.stop();
                    return;
                }
                if (getInfernoWaveId() >= type.length && getInfernoWaveType() > 0) {
                    onStopped();
                    event.stop();
                    return;
                }
                if (getInfernoWaveId() != 66 && getInfernoWaveId() != 67 && getInfernoWaveId() != 68) {
                    createWalls();
                }
                started = true;

                if (getInfernoWaveId() != 0 && getInfernoWaveId() < type.length)
                    player.sendMessage("@red@You are now on wave " + (getInfernoWaveId() + 1) + " of " + type.length + ".", 255);
                if (getInfernoWaveId() == 68) {
                    initiateTzkalzuk();
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, height, instance);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                }
                if (getInfernoWaveId() == 66) {
                    setKillsRemaining(1);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, height, instance);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                    NPC jad = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2271 + Misc.random(-4, 4), 5342 + Misc.random(-4, 4), height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false);
                    jadCombat(jad);
                }
                if (getInfernoWaveId() == 67) {
                    setKillsRemaining(3);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, height, instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, height).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, height, instance);
                    if (wall1 != null) {
                        wall1.isDead = true;
                    }
                    if (wall2 != null) {
                        wall2.isDead = true;
                    }
                    if (wall3 != null) {
                        wall3.isDead = true;
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        NPC jad1;
                        NPC jad2;
                        NPC jad3;
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (ticks == 0) {
                                jad1 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2263, 5341, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false);
                                jadCombat(jad1);
                            }
                            if (ticks == 2) {
                                jad2 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2269, 5347, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false);
                                jadCombat(jad2);
                            }
                            if (ticks == 4) {
                                jad3 = Server.npcHandler.spawnNpc(player, InfernoWave.JALTOK_JAD, 2276, 5341, height, 1, InfernoWave.getHp(InfernoWave.JALTOK_JAD), InfernoWave.getMax(InfernoWave.JALTOK_JAD), InfernoWave.getAtk(InfernoWave.JALTOK_JAD), InfernoWave.getDef(InfernoWave.JALTOK_JAD), false, false);
                                jadCombat(jad3);
                                container.stop();
                            }
                            ticks++;
                        }
                    }, 1);
                }
                if (getInfernoWaveId() < 66) {
                    setKillsRemaining(type[getInfernoWaveId()].length + 3);
                }

                if (getInfernoWaveId() < 66) {
                    for (int i = 0; i < getKillsRemaining() - 3; i++) {
                        final int npcType = type[getInfernoWaveId()][i];
                        final int startX = 2271 + Misc.random(-4, 4);
                        final int startY = 5342 + Misc.random(-4, 4);
                        final int hp = InfernoWave.getHp(npcType);
                        final int maxhit = InfernoWave.getMax(npcType);
                        final int atk = InfernoWave.getAtk(npcType);
                        final int def = InfernoWave.getDef(npcType);
                        Server.npcHandler.spawnNpc(player, npcType, startX, startY, height, 1, hp, maxhit, atk, def, true, false);
                    }
                }
                event.stop();
            }

            @Override
            public void onStopped() {


            }
        }, 16);

        if (getInfernoWaveId() < 66) {
            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                int ticks = 0;

                @Override
                public void execute(CycleEventContainer container) {
                    if (player == null) {
                        container.stop();
                        return;
                    }
                    if (!Boundary.isIn(player, Boundary.INFERNO)) {
                        reset(player);
                        container.stop();
                        return;
                    }
                    if (getInfernoWaveId() >= type.length && getInfernoWaveType() > 0) {
                        onStopped();
                        container.stop();
                        return;
                    }
                    if (started) {
                        if (ticks == 0) {
                            nib1 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2270, 5342, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false);
                            nib2 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2269, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false);
                            nib3 = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_NIB, 2271, 5343, height, 0, InfernoWave.getHp(InfernoWave.JAL_NIB), InfernoWave.getMax(InfernoWave.JAL_NIB), InfernoWave.getAtk(InfernoWave.JAL_NIB), InfernoWave.getDef(InfernoWave.JAL_NIB), false, false);
                        }
                        if (wall1 != null) {
                            if (!wall1.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2270, 5336);
                                NPCDumbPathFinder.walkTowards(nib2, 2271, 5336);
                                NPCDumbPathFinder.walkTowards(nib3, 2272, 5336);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall1.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall1.getX(), wall1.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall1.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall1.getX() + 1, wall1.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall1.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall1.getX() + 2, wall1.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall1Hp -= amount;
                                            if (amount == 0) {
                                                wall1.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall1.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall2 != null) {
                            if (!wall2.isDead && wall1.isDead && !wall3.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2261, 5348);
                                NPCDumbPathFinder.walkTowards(nib2, 2260, 5348);
                                NPCDumbPathFinder.walkTowards(nib3, 2259, 5348);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall2.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall2.getX() + 2, wall2.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall2.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall2.getX() + 1, wall2.getY());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall2.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall2.getX(), wall2.getY());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall2Hp -= amount;
                                            if (amount == 0) {
                                                wall2.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall2.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (wall3 != null) {
                            if (!wall3.isDead && wall1.isDead && wall2.isDead) {
                                NPCDumbPathFinder.walkTowards(nib1, 2277, 5350);
                                NPCDumbPathFinder.walkTowards(nib2, 2277, 5350);
                                NPCDumbPathFinder.walkTowards(nib3, 2277, 5352);
                                if (ticks % 4 == 0) {
                                    if (!nib1.isDead) {
                                        if (wall3.getDistance(nib1.getX(), nib1.getY()) <= 1) {
                                            nib1.turnNpc(wall3.getX(), wall3.getY());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib2.isDead) {
                                        if (wall3.getDistance(nib2.getX(), nib2.getY()) <= 1) {
                                            nib2.turnNpc(wall3.getX(), wall3.getY() + 1);
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                    if (!nib3.isDead) {
                                        if (wall3.getDistance(nib3.getX(), nib3.getY()) <= 1) {
                                            nib3.turnNpc(wall3.getX(), wall3.getY() + 2);
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            wall3Hp -= amount;
                                            if (amount == 0) {
                                                wall3.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                wall3.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        if (wall1 != null) {
                            if (wall1.getHealth().getCurrentHealth() == 0) {
                                wall1.isDead = true;
                            } else {
                                wall1.hp = 1;
                            }
                        }
                        if (wall2 != null) {
                            if (wall2.getHealth().getCurrentHealth() == 0) {
                                wall2.isDead = true;
                            } else {
                                wall2.hp = 1;
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.getHealth().getCurrentHealth() == 0) {
                                wall3.isDead = true;
                            } else {
                                wall3.hp = 1;
                            }
                        }

                        if (wall1 != null) {
                            if (wall1.isDead) {
                                Server.getGlobalObjects().remove(30354, 2270, 5333, height, instance);

                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall2 != null) {
                            if (wall2.isDead) {
                                Server.getGlobalObjects().remove(30355, 2259, 5349, height, instance);

                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall3 != null) {
                            if (wall3.isDead) {
                                Server.getGlobalObjects().remove(30355, 2278, 5349, height, instance);

                                //Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5350, height, 1, 10, -1, -1));
                            }
                        }
                        if (wall1 != null && wall2 != null && wall3 != null) {
                            if (wall1.isDead && wall2.isDead && wall3.isDead) {
                                if (!nib1.isDead) {
                                    NPCDumbPathFinder.follow(nib1, player);
                                    if (ticks % 4 == 0) {
                                        if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib1.facePlayer(player.getIndex());
                                            nib1.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib2.isDead) {
                                    NPCDumbPathFinder.follow(nib2, player);
                                    if (ticks % 4 == 0) {
                                        if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib2.facePlayer(player.getIndex());
                                            nib2.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                                if (!nib3.isDead) {
                                    NPCDumbPathFinder.follow(nib3, player);
                                    if (ticks % 4 == 0) {
                                        if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                            nib3.facePlayer(player.getIndex());
                                            nib3.startAnimation(7574);
                                            int amount = Misc.random(0, 5);
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (amount == 0) {
                                                player.appendDamage(amount, Hitmark.MISS);
                                            } else {
                                                player.appendDamage(amount, Hitmark.HIT);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (wall1 == null && wall2 == null && wall3 == null) {
                            if (!nib1.isDead) {
                                NPCDumbPathFinder.follow(nib1, player);
                                if (ticks % 4 == 0) {
                                    if (nib1.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib1.facePlayer(player.getIndex());
                                        nib1.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib2.isDead) {
                                NPCDumbPathFinder.follow(nib2, player);
                                if (ticks % 4 == 0) {
                                    if (nib2.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib2.facePlayer(player.getIndex());
                                        nib2.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                            if (!nib3.isDead) {
                                NPCDumbPathFinder.follow(nib3, player);
                                if (ticks % 4 == 0) {
                                    if (nib3.getDistance(player.getX(), player.getY()) <= 1) {
                                        nib3.facePlayer(player.getIndex());
                                        nib3.startAnimation(7574);
                                        int amount = Misc.random(0, 5);
                                        if (player.protectingMelee()) {
                                            amount = 0;
                                        }
                                        if (amount == 0) {
                                            player.appendDamage(amount, Hitmark.MISS);
                                        } else {
                                            player.appendDamage(amount, Hitmark.HIT);
                                        }
                                    }
                                }
                            }
                        }
                        if (nib1 != null && nib2 != null && nib3 != null) {
                            if (nib1.isDead && nib2.isDead && nib3.isDead) {
                                started = false;
                                container.stop();
                            }
                        }
                        if (nib1 == null && nib2 == null && nib3 == null) {
                            started = false;
                            container.stop();
                        }
                        ticks++;

                    }
                }
            }, 1);
        }
    }

    private void createWalls() {
        if (wall1Alive == 0 || wall1Hp > 0) {
            if(wall1 == null) {
                //wall1.isDead = true;
                wall1 = Server.npcHandler.spawnNpc(player, 7710, 2270, 5333, height, 0, 1000, 0, 0, 0, false, false);
                Server.getGlobalObjects().add(new GlobalObject(30354, 2270, 5333, height, 1, 10, -1, -1).setInstance(this));
                //Region.addObject(30354, 2270, 5333, height, 10, 1);
                //wall1 = Server.npcHandler.spawnNpc(player, 7710, 2270, 5333, height, 0, 1000, 0, 0, 0, false, false);
                wall1.getHealth().setCurrentHealth(wall1Hp);
                wall1Alive = 1;
            }
        } else {
            //wall1 = Server.npcHandler.spawnNpc(player, 7710, 2270, 5333, height, 0, 0, 0, 0, 0, false, false);
            //wall1.isDead = true;
        }
        if (wall2Alive == 0 || wall2Hp > 0) {
            if(wall2 == null) {
                //wall2.isDead = true;
                wall2 = Server.npcHandler.spawnNpc(player, 7710, 2259, 5349, height, 0, 1000, 0, 0, 0, false, false);
                Server.getGlobalObjects().add(new GlobalObject(30355, 2259, 5349, height, 1, 10, -1, -1).setInstance(this));
                // Region.addObject(30355, 2259, 5349, height, 10, 1);
                //wall2 = Server.npcHandler.spawnNpc(player, 7710, 2259, 5349, height, 0, 1000, 0, 0, 0, false, false);
                wall2.getHealth().setCurrentHealth(wall2Hp);
                wall2Alive = 1;
            }
        } else {
            //wall2 = Server.npcHandler.spawnNpc(player, 7710, 2259, 5349, height, 0, 0, 0, 0, 0, false, false);
            //wall2.isDead = true;
        }
        if (wall3Alive == 0 || wall3Hp > 0) {
            if(wall3 == null) {
                //wall3.isDead = true;
                wall3 = Server.npcHandler.spawnNpc(player, 7710, 2278, 5349, height, 0, 1000, 0, 0, 0, false, false);
                Server.getGlobalObjects().add(new GlobalObject(30355, 2278, 5349, height, 1, 10, -1, -1).setInstance(this));
                //Region.addObject(30555, 2278, 5349, height, 10, 1);
                //wall3 = Server.npcHandler.spawnNpc(player, 7710, 2278, 5350, height, 0, 1000, 0, 0, 0, false, false);
                wall3.getHealth().setCurrentHealth(wall3Hp);
                wall3Alive = 1;
            }
        } else {
            //wall3 = Server.npcHandler.spawnNpc(player, 7710, 2278, 5350, height, 0, 0, 0, 0, 0, false, false);
            //wall3.isDead = true;
        }
    }

    public void killAllSpawns() {
        for (int i = 0; i < NPCHandler.npcs.length; i++) {
            if (NPCHandler.npcs[i] != null) {
                if (NPCHandler.isInfernoNpc(i)) {
                    if (NPCHandler.isSpawnedBy(player, NPCHandler.npcs[i])) {
                        NPCHandler.npcs[i] = null;
                    }
                }
            }
        }
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, height).setInstance(this));
        Server.getGlobalObjects().remove(30354, 2270, 5333, height, this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, height).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2259, 5349, height, this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, height).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2278, 5349, height, this);
        if (wall1 != null) {
            wall1.isDead = true;
        }
        if (wall2 != null) {
            wall2.isDead = true;
        }
        if (wall3 != null) {
            wall3.isDead = true;
        }
        if (nib1 != null) {
            nib1.isDead = true;
        }
        if (nib2 != null) {
            nib2.isDead = true;
        }
        if (nib3 != null) {
            nib3.isDead = true;
        }
    }

    private void jadCombat(NPC jad) {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            int ticks = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (player == null) {
                    container.stop();
                    return;
                }
                if (player.isDead()) {
                    container.stop();
                    return;
                }
                if (jad != null) {
                    if (!jad.isDead) {
                        if (!Boundary.isIn(player, Boundary.INFERNO)) {
                            reset(player);
                            container.stop();
                            return;
                        }
                        if (player == null) {
                            container.stop();
                            return;
                        }
                        if (player.isDead()) {
                            container.stop();
                            return;
                        }
                        jad.facePlayer(player.getIndex());
                    }
                }
                int type;
                if (jad.getDistance(player.getX(), player.getY()) <= 2) {
                    type = Misc.random(0, 2);
                } else {
                    type = Misc.random(0, 1);
                }
                if (ticks % 10 == 0 && ticks != 0) {
                    if (jad != null) {
                        if (!jad.isDead) {
                            if (type == 0) {
                                jad.startAnimation(7593);
                            } else if (type == 1) {
                                jad.startAnimation(7592);
                            } else if (type == 2) {
                                jad.startAnimation(7590);
                            }
                        }
                    }
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        int tick = 0;
                        int amount = Misc.random(0, 113);

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (tick == 0) {
                                if (jad != null) {
                                    if (!jad.isDead) {
                                        if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                            reset(player);
                                            container.stop();
                                            return;
                                        }
                                        if (player == null) {
                                            container.stop();
                                            return;
                                        }
                                        if (player.isDead()) {
                                            container.stop();
                                            return;
                                        }
                                        int nX = jad.getX();
                                        int nY = jad.getY();
                                        int pX = player.getX();
                                        int pY = player.getY();
                                        int offX = (nX - pX) * -1;
                                        int offY = (nY - pY) * -1;
                                        int centerX = nX + jad.getSize() / 2;
                                        int centerY = nY + jad.getSize() / 2;

                                        int speed = 130;
                                        int startHeight = 110;
                                        int endHeight = 31;
                                        int delay = 0;
                                        if (type == 0) {
                                            player.gfx100(451);
                                            if (player.protectingRange()) {
                                                amount = 0;
                                            }
                                        } else if (type == 1) {
                                            if (player.protectingMagic()) {
                                                amount = 0;
                                            }
                                            player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 448, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                                        } else if (type == 2) {
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                        }
                                    }
                                }
                            }

                            if (tick == 1 || tick == 2) {
                                if (type == 0) {
                                    if (player.protectingRange()) {
                                        amount = 0;
                                    }
                                } else if (type == 1) {
                                    if (player.protectingMagic()) {
                                        amount = 0;
                                    }
                                } else if (type == 2) {
                                    if (player.protectingMelee()) {
                                        amount = 0;
                                    }
                                }
                            }

                            if (tick == 3) {
                                if (jad != null) {
                                    if (jad.isDead) {
                                        container.stop();
                                        return;
                                    }
                                }
                                if (jad == null) {
                                    container.stop();
                                    return;
                                }

                                if (player == null) {
                                    container.stop();
                                    return;
                                }
                                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                    reset(player);
                                    container.stop();
                                    return;
                                }
                                if (jad != null) {
                                    if (!jad.isDead) {
                                        if (player == null) {
                                            container.stop();
                                            return;
                                        }
                                        if (player.isDead()) {
                                            container.stop();
                                            return;
                                        }
                                        if (type == 0) {
                                            if (player.protectingRange()) {
                                                amount = 0;
                                            }
                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().calculateRangeDefence())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
                                            }
                                        } else if (type == 1) {
                                            if (player.protectingMagic()) {
                                                amount = 0;
                                            }
                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
                                            }
                                        } else if (type == 2) {
                                            if (player.protectingMelee()) {
                                                amount = 0;
                                            }
                                            if (Misc.random(500) + 200 > Misc.random(player.getCombat().calculateMeleeDefence())) {
                                                if (amount == 0) {
                                                    player.appendDamage(amount, Hitmark.MISS);
                                                } else {
                                                    player.appendDamage(amount, Hitmark.HIT);
                                                }
                                            } else {
                                                player.appendDamage(0, Hitmark.MISS);
                                            }
                                        }
                                    }
                                }
                                container.stop();
                            }

                            if (jad != null) {
                                if (jad.isDead) {
                                    container.stop();
                                    return;
                                }
                            }
                            if (jad == null) {
                                container.stop();
                                return;
                            }

                            if (player == null) {
                                container.stop();
                                return;
                            }

                            if (player.isDead()) {
                                container.stop();
                                return;
                            }
                            if (!Boundary.isIn(player, Boundary.INFERNO)) {
                                reset(player);
                                container.stop();
                                return;
                            }
                            tick++;
                        }
                    }, 1);
                }
                ticks++;
            }
        }, 1);
    }

    public int getKillsRemaining() {
        return killsRemaining;
    }

    public void setKillsRemaining(int remaining) {
        this.killsRemaining = remaining;
    }

    public int getInfernoWaveId() {
        return infernoWaveId;
    }

    public void setInfernoWaveId(int infernoWaveId) {
        this.infernoWaveId = infernoWaveId;
    }

    public int getInfernoWaveType() {
        return infernoWaveType;
    }

    public void setInfernoWaveType(int infernoWaveType) {
        this.infernoWaveType = infernoWaveType;
    }
}