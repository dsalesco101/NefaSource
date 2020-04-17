package ethos.model.players.packets.commands.admin;

import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

public class Givecollection extends Command {

	@Override
	public void execute(Player player, String input) {
		String args[] = input.split("-");
		if (args.length < 4) {
			player.sendMessage("Syntax: ::givecollection-name-npcid-itemid-amount");
			return;
		}
		String playerName = args[0];
		int npcId = Integer.parseInt(args[1]);
		int itemId = Integer.parseInt(args[2]);
		int amount = Integer.parseInt(args[3]);
		Player p = PlayerHandler.getPlayer(playerName);
		if (p != null) {
			p.getCollectionLog().handleDrop(npcId, itemId, amount);
			p.sendMessage(player.playerName + " has given you a collection item.");
			player.sendMessage("You have give " + p.playerName + " a collection item.");
		}
	}

}
