package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open the forums in the default web browser.
 * 
 * @author Emiel
 */
public class Pg extends Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().sendFrame126("http://www.NefariousPkz.com/forums/index.php?/topic/34-3bv-wisdom-price-guide/", 12000);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens a web page for a price guide.");
	}

}
