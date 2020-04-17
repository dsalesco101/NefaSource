package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Raids extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.wildLevel >20) {
			c.sendMessage("@red@You cannot teleport above 20 wilderness.");
			return;
		}
		c.getPA().startTeleport2(3033, 6067, 0);
		c.sendMessage("@red@You have been teleported to the raids lobby.");
		c.getPA().closeAllWindows();
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teleports you to raids.");
	}

}
