package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Show the current position.
 * 
 * @author Noah
 *
 */
public class Announce extends Command {

	@Override
	public void execute(Player player, String input) {
		if (player.announce == false) {
			player.announce = true;
			player.sendMessage("@blu@Global rare announcements are now @red@enabled.");
			return;
		} else {
			player.announce = false;
			player.sendMessage("@blu@Global rare announcements are now @gre@disabled.");
			return;
		}
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Allows you to turn on/off your drop announcements.");
	}
}
