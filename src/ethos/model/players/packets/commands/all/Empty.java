package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Empty the inventory of the player.
 * 
 * @author Emiel
 */
public class Empty extends Command {

	@Override
	public void execute(Player c, String input) {
		c.getDH().sendDialogues(450,-1);
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Empty your inventory");
	}
}
