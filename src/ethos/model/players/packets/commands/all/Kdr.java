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
public class Kdr extends Command {

	@Override
	public void execute(Player c, String input) {
        c.forcedChat("My Kill/Death ratio is " + c.killcount + " Kills: " + c.deathcount + " Deaths ");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Lets you know your KDR.");
	}
}
