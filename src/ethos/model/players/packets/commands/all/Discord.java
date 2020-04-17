package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the vote page in the default web browser.
 * 
 * @author Emiel
 */
public class Discord extends Command {

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Invites you to our Discord server");
	}

	@Override
	public void execute(Player player, String input) {
		 player.getPA()
         .sendFrame126(
                 "https://discord.gg/z6mvKrZ", 12000);
		
	}

}
