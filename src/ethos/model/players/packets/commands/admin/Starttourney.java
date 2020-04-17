package ethos.model.players.packets.commands.admin;

import ethos.model.content.tournaments.TourneyManager;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/3/19
 *
 */
public class Starttourney extends Command {

	@Override
	public void execute(Player player, String input) {
		TourneyManager.getSingleton().createEvent(player, input);
	}

}
