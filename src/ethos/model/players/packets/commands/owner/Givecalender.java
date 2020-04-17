package ethos.model.players.packets.commands.owner;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.model.content.eventcalendar.EventChallenge;

/**
 * Show the current position.
 * 
 * @author Emiel
 *
 */
public class Givecalender extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			c.sendMessage("You have entered @red@ "+c2.playerName+" into the calender entry.");
			c2.sendMessage("You have been given 1 raid count.");
            c2.getEventCalendar().progress(EventChallenge.COMPLETE_X_RAIDS, 1);
		} else {
			c.sendMessage("user is offline. You can only teleport online players.");
		}
	}
}