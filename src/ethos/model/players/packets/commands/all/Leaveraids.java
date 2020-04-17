package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.clip.doors.Location;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.all.Commands;
import ethos.model.minigames.raids.Raids;

public class Leaveraids extends Commands {

	@Override
	public void execute(Player player, String input) {
		Raids raidInstance = player.getRaidsInstance();
		if(raidInstance != null) {
			player.sendMessage("@blu@You are now leaving the raid...");
			raidInstance.leaveGame(player);
		} else {
			player.sendMessage("@red@You need to be in a raid to do this...");
		}
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Forces you to leave raids.");
	}

}
