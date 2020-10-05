package ethos.model.lobby.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ethos.Server;
import ethos.ServerState;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;
import ethos.model.lobby.Lobby;
import ethos.model.minigames.raids.Raids;
import ethos.util.Misc;

public class ChambersOfXericLobby extends Lobby {

	public ChambersOfXericLobby() {
		
	}
	
	@Override
	public void onJoin(Player player) {
		player.getPA().movePlayer((3033+Misc.random(-3,3)), (6054+Misc.random(-3,3)));
		player.sendMessage("You are now in the lobby for Chambers of Xeric.");
		String timeLeftString = formattedTimeLeft();
		player.sendMessage("Raid starts in: " + timeLeftString);
	}

	@Override
	public void onLeave(Player player) {
		player.getPA().movePlayer(3059, 9947, 0);
		player.sendMessage("@red@You have left the Chambers of Xeric.");
	}

	@Override
	public boolean canJoin(Player player) {
		if ((player.getMode().isRegular() || player.getMode().isIronman() ||
				player.getMode().isUltimateIronman() || player.getMode().isHCIronman()) && player.totalLevel < 1500) {
		player.sendMessage("You need a total level of atleast 1500 to join this raid!");
		return false;
		}
		 if (player.getMode().isOsrs() && player.totalLevel < 750) {
			player.sendMessage("You need a total level of atleast 750 to join this raid!");
			return false;
		 }
		 if (player.getMode().isMedMode() && player.totalLevel < 1000) {
			player.sendMessage("You need a total level of atleast 1000 to join this raid!");
			return false;
		}

		// Don't allow multi-logging outside of debug
		if (Server.getConfiguration().getServerState() != ServerState.DEBUG) {
			boolean accountInLobby = getFilteredPlayers()
					.stream()
					.anyMatch(lobbyPlr -> lobbyPlr.getMacAddress().equalsIgnoreCase(player.getMacAddress()));
			if (accountInLobby) {
				player.sendMessage("You already have an account in the lobby!");
				return false;
			}
		}

		return true;
	}

	@Override
	public long waitTime() {
		return Server.getConfiguration().getServerState() == ServerState.DEBUG ? 5000 : 60000;
	}

	@Override
	public int capacity() {
		return 22;
	}

	@Override
	public String lobbyFullMessage() {
		return "Chambers of Xeric is currently full!";
	}

	@Override
	public boolean shouldResetTimer() {
		return this.getWaitingPlayers().isEmpty();
	}

	@Override
	public void onTimerFinished(List<Player> lobbyPlayers) {
		List<Player> raidPlayers;
		if (Server.getConfiguration().getServerState() != ServerState.DEBUG) {
			Map<String, Player> macFilter = Maps.newConcurrentMap();
			lobbyPlayers.forEach(plr -> macFilter.put(plr.getMacAddress(), plr));
			raidPlayers = Lists.newArrayList(macFilter.values());
			lobbyPlayers.stream().filter(plr -> !macFilter.values().contains(plr)).forEach(plr -> {
				plr.sendMessage("You had a different account in this lobby, you will be added to the next one");
				onJoin(plr);
			});
		} else {
			raidPlayers = Lists.newArrayList(lobbyPlayers);
		}

		new Raids().startRaid(raidPlayers);
	}

	@Override
	public void onTimerUpdate(Player player) {
		String timeLeftString = formattedTimeLeft();
		player.getPA().sendString("Raid begins in: @gre@" + timeLeftString, 6570);
		player.getPA().sendString("", 6572);
		player.getPA().sendString("", 6664);
		player.getPA().walkableInterface(6673);
	}

	@Override
	public Boundary getBounds() {
		return Boundary.RAIDS_LOBBY;
	}

}
