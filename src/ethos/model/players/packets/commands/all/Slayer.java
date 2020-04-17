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
public class Slayer extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inWild()) {
			c.sendMessage("You can only use this command outside the wilderness.");
			return;
		}
		c.getDH().sendDialogues(943, -1);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens up all options of slayer masters.");
	}
}
