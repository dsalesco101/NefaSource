package ethos.model.players.packets.commands.owner;

import ethos.clip.Region;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;

public class Getclip extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int z = Integer.parseInt(args[2]);
		System.out.println("Clip: " + c.getRegionProvider().getClipping(x, y, z));
	}

}
