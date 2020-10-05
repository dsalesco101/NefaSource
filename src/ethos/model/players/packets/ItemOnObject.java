package ethos.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import java.util.Objects;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.UseItem;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*
		 * a = ? b = ?
		 */

		@SuppressWarnings("unused")
		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readInteger();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		c.xInterfaceId = -1;
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		//UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);

		switch (c.objectId) {

			case 2030: //Allows for items to be used from both sides of the furnace
				c.objectDistance = 4;
				c.objectXOffset = 3;
				c.objectYOffset = 3;
				break;
			case 26782:
				c.objectDistance = 7;
				break;
			case 33320:
				c.objectDistance = 5;
				break;
			case 18818:
			case 409:
				c.objectDistance = 3;
				break;
			case 884:
				c.objectDistance = 5;
				c.objectXOffset = 3;
				c.objectYOffset = 3;
				break;
				
			case 28900:
				c.objectDistance = 3;
				break;
		
	
				
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;

		}
		if (c.goodDistance(objectX + c.objectXOffset, objectY + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
			c.turnPlayerTo(objectX, objectY);
			UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		} else {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.goodDistance(objectX + c.objectXOffset, objectY + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
						c.turnPlayerTo(objectX, objectY);
						UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
						container.stop();
					}
				}

				@Override
				public void onStopped() {
					c.clickObjectType = 0;
				}
			}, 1);
		}

	}

}
