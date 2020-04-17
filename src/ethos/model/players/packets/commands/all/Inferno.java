package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Inferno extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inWild()) {
			c.sendMessage("You can only use this command outside the wilderness.");
			return;
		}
		c.getPA().startTeleport(2437, 5126, 0, "modern", false);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Takes you near the inferno gates.");
	}
}
