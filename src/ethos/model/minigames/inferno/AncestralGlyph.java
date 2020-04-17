package ethos.model.minigames.inferno;

import com.google.common.base.Preconditions;
import ethos.Server;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;

public class AncestralGlyph {

    /**
     * Movement, <code>false</code> is left, <code>true</code> is right
     */
    private static String MOVE_ATTRIBUTE_KEY = "ancestral_glyph_move";

    private static void flip(NPC npc) {
        npc.getAttributes().flipBoolean(MOVE_ATTRIBUTE_KEY);
    }

    private static boolean left(NPC npc) {
        return !npc.getAttributes().getBoolean(MOVE_ATTRIBUTE_KEY);
    }

    public static void handleMovement(Player player, NPC npc) {
        if (player != null && player.getInferno() != null) {
            npc.walkingHome = false;

            if (player.getInferno().glyphCanMove) {
                if (npc.absX == 2270 && npc.absY > 5361) { // Starting position
                    npc.moveTowards(2270, 5361);
                    Preconditions.checkState(npc.moveX != 0 || npc.moveY != 0);
                } else if (!left(npc)) {                     // Move left
                    npc.moveTowards(2283, 5361);
                    Preconditions.checkState(npc.moveX != 0 || npc.moveY != 0);
                    if (npc.getPosition().getX() == 2283) {
                        flip(npc);
                    }
                } else {                                    // Move right
                    npc.moveTowards(2257, 5361);
                    Preconditions.checkState(npc.moveX != 0 || npc.moveY != 0);
                    if (npc.getPosition().getX() == 2257) {
                        flip(npc);
                    }
                }

                player.getInferno().glyphCurrentX = npc.absX;
                player.getInferno().glyphCurrentY = npc.absY;
            }
        }
    }

}
