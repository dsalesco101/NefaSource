package ethos.model.players.packets.commands.donator;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Teleports the player to the donator zone.
 * 
 * @author Emiel
 */
public class Oz extends Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inTrade || c.inDuel || c.inWild()) {
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@This player is currently at the pk district.");
			return;
		}
		if (c.amDonated <= 999) {
			c.sendMessage("@red@You need Onyx donator to do this command");
			return;
		} 
		c.getPA().startTeleport(2786, 4840, 0, "modern", false);

	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teleports you to Onyx zone.");
	}

}
