package ethos.model.players.packets.commands.donator;

import ethos.Server;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.model.players.RightGroup;
import ethos.model.players.packets.commands.Command;
import ethos.punishments.PunishmentType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Tells the player they need to be a donator to use this feature.
 * 
 * @author Emiel
 */
public class Yell extends Command {

	private static final Right[] PERMITTED = { Right.REGULAR_DONATOR, Right.SUPER_DONOR, Right.EXTREME_DONOR, Right.ULTRA_DONATOR, Right.LEGENDARY_DONATOR, Right.DIAMOND_CLUB, 
			Right.ONYX_CLUB, Right.PLATINUM, Right.DIVINE, Right.HELPER, Right.MODERATOR, Right.ADMINISTRATOR,
			Right.OWNER, Right.GAME_DEVELOPER, Right.YOUTUBER, Right.HITBOX };

	private static final String[] ILLEGAL_ARGUMENTS = { ":tradereq:", "<img", "@cr", "<tran", "#url#", ":duelreq:", ":chalreq:" };

	public void test(Player player) {
		for (Right right : Right.values()) {
			player.sendMessage(formatMessage("", right.name(), right.getColor(), right.getValue() - 1, "Hello world!"));
		}
	}

	@Override
	public void execute(Player player, String input) {
		RightGroup rights = player.getRights();

		Set<Right> prohibited = new HashSet<>(Arrays.asList(PERMITTED));

		long count = rights.getSet().stream().filter(r -> prohibited.stream().anyMatch(r::isOrInherits)).count();

		if (count == 0) {
			player.sendMessage("You do not have the rights to access this command.");
			return;
		}
		
		if (rights.getPrimary() == Right.PLAYER) {
			player.sendMessage("You do not have the rights to access this command");
			return;
		}

		long delay = getDelay(player);
		if (System.currentTimeMillis() - player.lastYell < delay) {
			player.sendMessage("You must wait another " + TimeUnit.MILLISECONDS.toSeconds(player.lastYell + delay - System.currentTimeMillis()) + " seconds before you can yell.");
			return;
		}
		if (Server.getPunishments().contains(PunishmentType.MUTE, player.playerName)) {
			player.sendMessage("You are muted, you cannot use this system.");
			return;
		}
		if (Server.getPunishments().contains(PunishmentType.NET_MUTE, player.connectedFrom)) {
			player.sendMessage("You are muted, you cannot use this system.");
			return;
		}
		if (System.currentTimeMillis() < player.muteEnd) {
			player.sendMessage("You are muted, you cannot use this system.");
			return;
		}

		for (String argument : ILLEGAL_ARGUMENTS) {
			if (input.contains(argument) && rights.getPrimary() != Right.OWNER) {
				player.sendMessage("Your message contains an illegal set of characters, you cannot yell this.");
				return;
			}
		}
		String message = formatMessage(player.getTitles().getCurrentTitle(), StringUtils.capitalize(player.playerName.toLowerCase()), player.getRights().getPrimary().getColor(),
				player.getRights().getPrimary().getValue() - 1, StringUtils.capitalize(input));
		player.lastYell = System.currentTimeMillis();
		PlayerHandler.executeGlobalMessage(message);
	}

	private String formatMessage(String title, String username, String color, int rights, String message) {
		if (title.equalsIgnoreCase("None")) {
			title = "";
		}
		return "<img=" + rights + ">[<col=" + color + ">" + title + "</col>]<col=" + color + ">" + username + "</col>: " + StringUtils.capitalize(message.toLowerCase());
	}

	private long getDelay(Player player) {
		RightGroup rights = player.getRights();

		if (rights.isOrInherits(Right.MODERATOR)) {
			return 0;
		}
		if (rights.contains(Right.ONYX_CLUB) || rights.contains(Right.HELPER)) {
			return TimeUnit.SECONDS.toMillis(1);
		} else if (rights.contains(Right.DIAMOND_CLUB)) {
			return TimeUnit.SECONDS.toMillis(1);
		} else if (rights.contains(Right.LEGENDARY_DONATOR)) {
			return TimeUnit.SECONDS.toMillis(30);
		} else if (rights.contains(Right.ULTRA_DONATOR)) {
			return TimeUnit.SECONDS.toMillis(60);
		} else if (rights.contains(Right.EXTREME_DONOR)) {
			return TimeUnit.SECONDS.toMillis(60);
		} else if (rights.contains(Right.SUPER_DONOR)) {
			return TimeUnit.SECONDS.toMillis(60);
		} else if (rights.contains(Right.REGULAR_DONATOR) || 
					rights.contains(Right.HITBOX) || 
					rights.contains(Right.YOUTUBER)) {
			return TimeUnit.SECONDS.toMillis(60);
		} else {
			return TimeUnit.SECONDS.toMillis(60);
		}
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Sends a global chat message");
	}

	@Override
	public Optional<String> getParameter() {
		return Optional.of("message");
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
