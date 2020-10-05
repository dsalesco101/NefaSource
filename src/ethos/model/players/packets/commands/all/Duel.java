package ethos.model.players.packets.commands.all;

import java.util.Optional;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.PunishmentType;
import ethos.punishments.Punishments;

/**
 * Teleport the player to the duel arena.
 * 
 * @author Emiel
 */
public class Duel extends Command {

	@Override
	public void execute(Player c, String input) {
		Punishments punishments = Server.getPunishments();
		if (punishments.contains(PunishmentType.MAC_BAN, c.getMacAddress()) || c.diceBan > 1) {
			int time = c.diceBan / 60;
			c.sendMessage("You are still banned from gambling.");
			c.sendMessage("@blu@You have @red@"+time+"@blu@ seconds left.");
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (c.inWild()) {
			return;
		}
		c.getPA().spellTeleport(3365, 3266, 0, false);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teleports you to the duel arena");
	}

}
