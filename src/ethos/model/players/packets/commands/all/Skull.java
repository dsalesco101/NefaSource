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
public class Skull extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inWild()) {
			c.sendMessage("You cannot use this command in the wilderness.");
			return;
		}
		c.headIconPk = (0);
		c.getPA().requestUpdates();
		c.sendMessage("You are now skulled.");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Puts a skull above your head..");
	}
}
