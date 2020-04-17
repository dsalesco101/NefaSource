package ethos.model.players.packets.commands.admin;

import ethos.Server;
import ethos.model.content.instances.InstancedArea;
import ethos.model.items.GroundItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.world.objects.GlobalObject;

public class Instancetest extends Command {

	@Override
	public void execute(Player player, String input) {
		InstancedArea instance = new InstancedArea(new Boundary(player.absX - 5, player.absY - 5, player.absX + 5, player.absY + 5), 0) {
			@Override
			public void onDispose() {
				System.out.println("Disposed instance.");
			}
		};
		
		instance.add(player);
		Server.getGlobalObjects().add(new GlobalObject(1, player.absX + 1, player.absY + 1, player.heightLevel, 10, 10).setInstance(instance));
		Server.itemHandler.createGroundItem(player, 4151, player.absX + 2, player.absY + 1, player.heightLevel, 1);
	}

}
