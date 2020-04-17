package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;

/**
 * Since button clicking is messed up and sends a weird id some new things
 * were colliding with old things. This is an attempt to fix that with a cheap hack.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class ClickingButtonsNew implements PacketType {

    public static final int CLICKING_BUTTONS_NEW = 184;

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        int buttonId = c.getInStream().readUnsignedWord();

        if (c.debugMessage) {
            c.sendMessage("ClickingButtonsNew: " + buttonId + ", DialogueID: " + c.dialogueAction);
        }

        if (c.getQuestTab().handleActionButton(buttonId)) {
            return;
        }

        if (c.getEventCalendar().handleButton(buttonId)) {
            return;
        }
    }
}
