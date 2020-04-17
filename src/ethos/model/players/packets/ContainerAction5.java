package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;

/**
 * Bank X Items
 **/
public class ContainerAction5 implements PacketType {

	public static final int PART1 = 135;
	public static final int PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int xRemoveSlot = c.getInStream().readSignedWordBigEndian();
		int xInterfaceId = c.getInStream().readUnsignedWordA();
		int xRemoveId = c.getInStream().readSignedWordBigEndian();

		if (packetType == 135) {
			c.xRemoveSlot = xRemoveSlot;
			c.xInterfaceId = xInterfaceId;
			c.xRemoveId = xRemoveId;
		}

		if (c.getLootingBag().handleClickItem(xRemoveId, -1)) {
			return;
		}

		if (c.viewingRunePouch) {
			c.getRunePouch().setEnterAmountVariables(c.xRemoveId, c.xInterfaceId);
		}
		if (c.debugMessage)
			c.sendMessage("ContainerAction5: interfaceid: "+c.xInterfaceId+", removeSlot: "+c.xRemoveSlot+", removeID: " + c.xRemoveId);

/* 		if (c.xInterfaceId == 3823) {
			c.getShops().sellItem(c.xRemoveId, c.xRemoveSlot, 100);// buy 100
			c.xRemoveSlot = 0;
			c.xInterfaceId = 0;
			c.xRemoveId = 0;
			return;
		} */
		/**
		 * Buy 500
		 */
		 if (c.xInterfaceId == 64016) {
			c.buyingX = true;
			c.getOutStream().createFrame(27);
			//return;
        }
		
		 if (c.xInterfaceId == 3823) {
				c.getShops().sellItem(c.xRemoveId, c.xRemoveSlot, 2000000000);//sell all
				c.xRemoveSlot = 0;
				c.xInterfaceId = 0;
				c.xRemoveId = 0;
				return;
			}


		if (packetType == PART1) {
			c.getOutStream().createFrame(27);
			c.flushOutStream();
		}

	}
}
