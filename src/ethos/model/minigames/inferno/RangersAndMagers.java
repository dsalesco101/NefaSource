package ethos.model.minigames.inferno;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

class RangersAndMagers {

    private static boolean verifyRangerState(Player player, Tzkalzuk ctx) {
        return verifyState(player, ctx) && ctx.ranger != null && !ctx.ranger.isDead;
    }

    private static boolean verifyMagerState(Player player, Tzkalzuk ctx) {
        return verifyState(player, ctx) && ctx.mager != null && !ctx.mager.isDead;
    }

    private static boolean verifyState(Player player, Tzkalzuk ctx) {
        return player.getInferno() != null && !player.isDead && !player.getInferno().zukDead && ctx.glyph != null && !ctx.glyph.isDead
                && Boundary.isIn(player, Boundary.INFERNO);
    }

    static void spawnRanger(Player player, Tzkalzuk ctx) {
        ctx.ranger = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_XIL, 2278, 5351, ctx.getHeight(), 0, InfernoWave.getHp(InfernoWave.JAL_XIL), InfernoWave.getMax(InfernoWave.JAL_XIL), InfernoWave.getAtk(InfernoWave.JAL_XIL), InfernoWave.getDef(InfernoWave.JAL_XIL), false, false);
        player.getInferno().kill.add(ctx.ranger);

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (!verifyRangerState(player, ctx)) {
                    container.stop();
                    return;
                }

                if (ctx.ranger.lastDamageTaken > 0 || player.getInferno().glyph.isDead) {
                    ctx.ranger.facePlayer(player.getIndex());
                } else {
                    ctx.ranger.turnNpc(player.getInferno().glyph.getX(), player.getInferno().glyph.getY());
                }

                if (tick % 6 == 0) {
                    ctx.ranger.startAnimation(7605);

                    CycleEventHandler.getSingleton().addEvent(player, rangerHit(player, ctx), 1);
                }

                tick++;
            }
        }, 1);
    }

    static void spawnMager(Player player, Tzkalzuk ctx) {
        ctx.mager = Server.npcHandler.spawnNpc(player, InfernoWave.JAL_ZEK, 2264, 5351, ctx.getHeight(), 0, InfernoWave.getHp(InfernoWave.JAL_ZEK), InfernoWave.getMax(InfernoWave.JAL_ZEK), InfernoWave.getAtk(InfernoWave.JAL_ZEK), InfernoWave.getDef(InfernoWave.JAL_ZEK), false, false);
        player.getInferno().kill.add(ctx.mager);

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            int tick = 0;

            @Override
            public void execute(CycleEventContainer container) {
                if (!verifyMagerState(player, ctx)) {
                    container.stop();
                    return;
                }

                // Face
                if (ctx.mager.lastDamageTaken > 0 || player.getInferno().glyph.isDead) {
                    ctx.mager.facePlayer(player.getIndex());
                } else {
                    ctx.mager.turnNpc(player.getInferno().glyph.getX(), player.getInferno().glyph.getY());
                }

                // Attack
                if (tick % 8 == 0) {
                    ctx.mager.startAnimation(7610);
                    CycleEventHandler.getSingleton().addEvent(player, getMageHit(player, ctx), 1);
                }

                tick++;
            }
        }, 1);
    }

    private static CycleEvent rangerHit(Player player, Tzkalzuk ctx) {
        return new CycleEvent() {
            int tick = 0;
            int amount = Misc.random(0, 46);

            @Override
            public void execute(CycleEventContainer container) {
                if (tick == 0) {

                    if (ctx.ranger.lastDamageTaken > 0 || player.getInferno().glyph.isDead) {
                        int nX = ctx.ranger.getX();
                        int nY = ctx.ranger.getY();
                        int pX = player.getX();
                        int pY = player.getY();
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + ctx.ranger.getSize() / 2;
                        int centerY = nY + ctx.ranger.getSize() / 2;

                        int speed = 85;
                        int startHeight = 43;
                        int endHeight = 31;
                        int delay = 0;
                        if (player.protectingRange()) {
                            amount = 0;
                        }
                        player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1376, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                    } else {
                        int nX = ctx.ranger.getX();
                        int nY = ctx.ranger.getY();
                        int pX = player.getInferno().glyph.getX();
                        int pY = player.getInferno().glyph.getY();
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + ctx.ranger.getSize() / 2;
                        int centerY = nY + ctx.ranger.getSize() / 2;

                        int speed = 85;
                        int startHeight = 43;
                        int endHeight = 31;
                        int delay = 0;
                        player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1376, startHeight, endHeight, player.getInferno().glyph.getIndex() + 1,
                                65, delay);
                    }
                }

                if (tick == 1) {
                    if (player.protectingRange()) {
                        amount = 0;
                    }
                }

                if (tick == 2) {
                    if (ctx.ranger.lastDamageTaken > 0 || player.getInferno().glyph.isDead) {
                        if (player.protectingRange()) {
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
                    } else {
                        amount = Misc.random(0, 70);
                        if (amount == 0) {
                            player.getInferno().glyph.appendDamage(amount, Hitmark.MISS);
                        } else {
                            player.getInferno().glyph.appendDamage(amount, Hitmark.HIT);
                        }
                    }
                    container.stop();
                }

                tick++;
            }
        };
    }

    private static CycleEvent getMageHit(Player player, Tzkalzuk ctx) {
        return new CycleEvent() {
            int tick = 0;
            int amount = Misc.random(0, 70);

            @Override
            public void execute(CycleEventContainer container) {
                if (!verifyMagerState(player, ctx)) {
                    container.stop();
                    return;
                }

                if (tick == 0) {
                    if (ctx.mager.lastDamageTaken > 0) {
                        int nX = ctx.mager.getX();
                        int nY = ctx.mager.getY();
                        int pX = player.getX();
                        int pY = player.getY();
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + ctx.mager.getSize() / 2;
                        int centerY = nY + ctx.mager.getSize() / 2;

                        int speed = 85;
                        int startHeight = 43;
                        int endHeight = 31;
                        int delay = 0;
                        if (player.protectingMagic()) {
                            amount = 0;
                        }
                        player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1378, startHeight, endHeight, -player.getIndex() - 1, 65, delay);
                    } else {
                        int nX = ctx.mager.getX();
                        int nY = ctx.mager.getY();
                        int pX = player.getInferno().glyph.getX();
                        int pY = player.getInferno().glyph.getY();
                        int offX = (nX - pX) * -1;
                        int offY = (nY - pY) * -1;
                        int centerX = nX + ctx.mager.getSize() / 2;
                        int centerY = nY + ctx.mager.getSize() / 2;

                        int speed = 85;
                        int startHeight = 43;
                        int endHeight = 31;
                        int delay = 0;
                        player.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, speed, 1378, startHeight, endHeight, player.getInferno().glyph.getIndex() + 1,
                                65, delay);
                    }
                }

                if (tick == 2) {
                    if (ctx.mager.lastDamageTaken > 0 || player.getInferno().glyph.isDead) {
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
                    } else {
                        amount = Misc.random(0, 70);
                        if (amount == 0) {
                            player.getInferno().glyph.appendDamage(amount, Hitmark.MISS);
                        } else {
                            player.getInferno().glyph.appendDamage(amount, Hitmark.HIT);
                        }
                    }
                    container.stop();
                }

                tick++;
            }
        };
    }

}
