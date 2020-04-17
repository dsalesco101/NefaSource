package ethos.model.players.packets.commands.admin;

import ethos.model.content.polls.PollTab;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

/**
 * @author Grant_ | www.rune-server.ee/members/grant_ | 2/10/20
 */
public class Reloadpoll extends Command {

    @Override
    public void execute(Player player, String input) {
        PollTab.reloadPoll();
        player.getPA().sendBroadCast("A new poll has opened!");
        player.sendMessage("You have loaded in a new poll named, " + PollTab.getPoll().getQuestion());
        PollTab.updateInterface(player);
    }
}
