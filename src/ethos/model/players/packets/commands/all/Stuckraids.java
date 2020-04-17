package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.clip.doors.Location;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.all.Commands;
import ethos.model.minigames.raids.Raids;

public class Stuckraids extends Commands {

	@Override
	public void execute(Player player, String input) {
		Raids raidInstance = player.getRaidsInstance();
			if(raidInstance != null) {
    			player.sendMessage("@blu@Sending you back to starting room...");
    			Location startRoom = raidInstance.getStartLocation();
    			player.getPA().movePlayer(startRoom.getX(), startRoom.getY(), raidInstance.currentHeight);
    			raidInstance.resetRoom(player);
    		} else {
    			player.sendMessage("@red@You need to be in a raid to do this...");
    		}
	}
	public Optional<String> getDescription() {
		return Optional.of("Teleports you out of raids if your stuck.");
	}
}
