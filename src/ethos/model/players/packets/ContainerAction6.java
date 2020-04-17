package ethos.model.players.packets;

import ethos.model.players.PacketType;
import ethos.model.players.Player;

public class ContainerAction6 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int slot = player.getInStream().readUnsignedWordA();
		int component = player.getInStream().readUnsignedWord();
		int item = player.getInStream().readUnsignedWordA();
		int amount = player.getInStream().readInteger();
		if (player.debugMessage)
			player.sendMessage("ContainerAction6: interfaceid: "+component+", removeSlot: "+slot+", removeID: " + item);
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return;
		}
		if (amount <= 0)
			return;
		if (player.getBank().withdraw(component, item, amount)) {
			return;
		}
	}

}
