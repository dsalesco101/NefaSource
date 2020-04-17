package ethos.model.players.packets.commands.admin;

import ethos.model.content.polls.PollTab;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * @author Grant_ | www.rune-server.ee/members/grant_ | 2/10/20
 */
public class Endpoll extends Command {

    @Override
    public void execute(Player player, String input) {
        PollTab.endPollManually();
        player.sendMessage("You have ended the current poll.");
        PollTab.updateInterface(player);
    }
}
