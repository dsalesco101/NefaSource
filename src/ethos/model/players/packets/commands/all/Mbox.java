package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.content.loot.LootableInterface;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open the mbox in the default web browser.
 * 
 * @author Noah
 */
public class Mbox extends Command {

	@Override
	public void execute(Player c, String input) {
		LootableInterface.openInterface(c);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens the loot table interface.");
	}

}
