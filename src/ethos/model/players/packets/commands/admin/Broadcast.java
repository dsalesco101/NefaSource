package ethos.model.players.packets.commands.admin;

import java.util.Arrays;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/15/19
 *
 */
public class Broadcast extends Command {

    @Override
    public void execute(Player player, String input) {
        Arrays.stream(PlayerHandler.players).forEach(p -> {
            if (p != null) {
                p.getPA().sendBroadCast(input);
            }
        });
    }
}