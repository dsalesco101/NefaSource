package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;

public class Task extends Command {

    @Override
    public void execute(Player player, String input) {
    	if (!player.getSlayer().getTask().isPresent()) {
            player.sendMessage("You do not have a task, please talk with a slayer master!");
            return;
        }
        player.sendMessage("I currently have " + player.getSlayer().getTaskAmount() + " " + player.getSlayer().getTask().get().getPrimaryName() + "'s to kill.");
        player.getPA().closeAllWindows();
    }
    @Override
	public Optional<String> getDescription() {
		return Optional.of("Shows your current slayer task");
	}
}

