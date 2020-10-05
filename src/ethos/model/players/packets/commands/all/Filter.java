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
public class Filter extends Command {

	@Override
	public void execute(Player player, String input) {
		if (player.notification == false) {
			player.notification = true;
			player.sendMessage("@blu@You will now see all the skilling messages.");
			return;
		} else {
			player.notification = false;
			player.sendMessage("@blu@You have filtered out all of the skilling messages.");
			return;
		}
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Allows you to filter skilling messages.");
	}
}
