package ethos.model.players.packets.commands.all;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Playtime extends Command {

	@Override
	public void execute(Player c, String input) {
		 long milliseconds = (long) c.playTime * 600;
	        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
	        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
	        String time = days + " days, " + hours + " hrs";
		c.forcedChat("My total play time is "+time+".");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Announces your playtime.");
	}
}
