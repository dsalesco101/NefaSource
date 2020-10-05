package ethos.model.players.packets;

import ethos.Config;
import ethos.event.impl.RandomEvent;
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
        if (buttonId == 10227) {
			c.forcedChat("I curently have a total of "+c.pkp+" pkp!");
        }
        if (buttonId == 10408) {
        	 if (Config.BONUS_XP_WOGW == true) {
        		 c.sendMessage("@blu@Wogw is currently active");
        	 } else {
        		 c.sendMessage("@blu@Wogw is currently not active");
 
        	 }
        }

        if (c.getQuestTab().handleActionButton(buttonId)) {
            return;
        }
 
    }
}
