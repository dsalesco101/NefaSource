package ethos.model.npcs;

import java.util.Arrays;
import java.util.Optional;

import lombok.extern.java.Log;

/**
 * Establishes the relationships between npcs.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
@Log
public class NPCRelationship {

    /**
     * Establish relationships between the npcs after the spawn_config is loaded.
     */
    public static void setup() {
        int count = 0;
        for (NPC parent : NPCHandler.npcs) {
            if (parent != null) {
                Optional<Relationship> relationshipOptional = forParent(parent.npcType);
                if (relationshipOptional.isPresent()) {
                    Relationship relationship = relationshipOptional.get();
                    for (NPC child : NPCHandler.npcs) {
                        if (child != null && Arrays.stream(relationship.childrenIds).anyMatch(id -> id == child.npcType)
                                && child.heightLevel == parent.heightLevel && child.getInstance() == parent.getInstance()) {
                            parent.addChild(child);
                            count++;
                        }
                    }
                }
            }
        }

        log.info("Initialized " + count + " npc relationships.");
    }

    public static Optional<Relationship> forParent(int parentId) {
        return Arrays.stream(Relationship.values()).filter(rel -> rel.parentId == parentId).findFirst();
    }

    public enum Relationship {
        GENERAL_GRAADOR(2215, 2216, 2217, 2218),
        COMMANDER_ZILYANA(2205, 2206, 2207, 2208),
        KRIL_TSUTANOTH(3129, 3130, 3131, 3132),
        KREEARRA(3162, 3163, 3164, 3165)
        ;

        private final int parentId;
        private final int[] childrenIds;

        Relationship(int parentId, int...childrenIds) {
            this.parentId = parentId;
            this.childrenIds = childrenIds;
        }
    }

}
