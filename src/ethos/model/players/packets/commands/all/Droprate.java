package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.npcs.drops.DropManager;
import ethos.model.players.Player;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.packets.commands.Command;

public class Droprate extends Command {

    @Override
    public void execute(Player player, String input) {
        player.forcedChat("My drop rate bonus is : " +DropManager.getModifier1(player) + "%.");
    }
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Shows drop rate bonus");
	}
}


