package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.packets.commands.Command;

public class Event extends Command {

	@Override
	public void execute(Player player, String input) {
		if(MonsterHunt.getCurrentLocation() != null) {
			player.sendMessage("@red@[Wildy Pursuit] @bla@Current Location: " + MonsterHunt.getCurrentLocation().getLocationName());
			player.sendMessage("@red@[Wildy Pursuit] @bla@Current Monster: " + MonsterHunt.getName());
			player.sendMessage("@red@[Wildy Pursuit] @bla@ Type ::telepursuit to get to the monsters location. @red@Wilderness!!");
		} else {
			player.sendMessage("@red@[Wildy Pursuit] @bla@No monster is currently in pursuit.");
		}
		
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teles you to wildy event");
	}
}
