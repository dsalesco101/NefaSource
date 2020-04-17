package ethos.model.players.packets.commands.all;

import java.sql.*;
import java.util.Optional;


import ethos.database.impl.Donation;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Opens the store page in the default web browser.
 * 
 * @author Emiel
 */
public class Donate extends Command {

	@Override
	public void execute(Player c, String input) {

	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens store");
	}

}
