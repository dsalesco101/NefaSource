package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.database.impl.Donation;
import ethos.database.impl.Vote;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Voted extends Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.getItems().freeSlots() < 1) {
             c.sendMessage("You need atleast one free slots to use this command.");
             return;
         }
		 if (Boundary.isIn(c, Boundary.OUTLAST)) {
             c.sendMessage("You cannot do this right now.");
             return;
         }
    	new Thread(new Vote(c)).start();
		c.sendMessage("@blu@Succesfully scanned the name @red@"+c.playerName+" @blu@for your votes.");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Claim your voted reward.");
	}
}
