package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.model.players.packets.commands.Command;

public class Slayertp extends Command {

	@Override
	public void execute(Player player, String input) {
		if (player.amDonated < 10 && player.getItems().playerHasItem(995, 200000)) {
				player.getItems().deleteItem(995, 200000);
				PlayerAssistant.ringOfCharosTeleport(player);
			} else {
				player.sendMessage("You need @red@200k coins@blu@ in order to use this command.");
			}

			if (player.amDonated > 10 && player.getItems().playerHasItem(995, 100000)) {
				PlayerAssistant.ringOfCharosTeleport(player);
			} else {
				player.sendMessage("You need @red@100k coins@blu@ in order to use this command.");
			}
			return;
		}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teleports you to your slayer task.");
	}
}
