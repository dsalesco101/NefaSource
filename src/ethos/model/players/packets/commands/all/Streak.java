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
public class Streak extends Command {

	@Override
	public void execute(Player c, String input) {
		c.sendMessage("@blu@ You have @red@"+c.getSlayer().getConsecutiveTasks()+" @blu@consecutive tasks.");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Tells you your slayer consecutive tasks.");
	}
}
