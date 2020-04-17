package ethos.model.players.packets.commands.owner;

import java.util.Arrays;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Spawn a specific Object.
 * 
 * @author Emiel
 *
 */
public class Object extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		int objId = Integer.parseInt(args[0]);
		int type = args.length > 1 ? Integer.parseInt(args[1]) : 10;
		int face = args.length > 2 ? Integer.parseInt(args[2]) : 0;
		
		Arrays.stream(PlayerHandler.players).forEach(p -> {
			if (p != null) {
				p.getPA().object(objId, c.absX, c.absY, face, type);
			}
		});
		
		c.sendMessage(String.format("Object spawned [Id: %s] [Type: %s] [Face: %s]", objId, type, face));
	}
}
