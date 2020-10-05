package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Open the forums in the default web browser.
 * 
 * @author Emiel
 */
public class Gulag extends Command {

	@Override
	public void execute(Player c, String input) {
		c.sendMessage("@red@[GULAG]@blu@ Bank your items and enter the portal to join the tournament! Good Luck!");
		c.getPA().startTeleport(2759, 2578, 0, "modern", false);
}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Quick teleport to Gulag.");
	}

}
