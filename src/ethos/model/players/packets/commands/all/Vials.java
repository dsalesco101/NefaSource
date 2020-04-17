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
public class Vials extends Command {

	@Override
	public void execute(Player player, String input) {
		if (player.barbarian == true) {
		if (player.breakVials == true) {
			player.breakVials = false;
			player.sendMessage("You will no longer break your viles after the last sip.");
			return;
		} else {
			player.breakVials = true;
			player.sendMessage("You will now break your vials after the last sip.");
			return;
		}
		}player.sendMessage("You have not learned how to break vials yet.");
		
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Turns on and off break vials.");
	}
}
