package ethos.model.minigames.raids;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import ethos.Config;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.skills.Skill;

public class RaidConstants {

	public static boolean lobbyActive = false;//players cannot go into other raid rooms while this is true
	
	public static List<Raids> raidGames = Lists.newArrayList();//gets players in lobby
	
	public static int raidLobbyTimer = 60;//how many seconds til timer starts
	
	public static int timeLeft = 0;//how much time is left until raid starts

	public static int currentRaidHeight = 4;
	
	public static void checkInstances() {
		Lists.newArrayList(raidGames).stream().filter(Objects::nonNull).forEach(raid -> {
			if(raid.getPlayers().size() == 0 && System.currentTimeMillis() - raid.getLastActivity() > 60000) {
				raid.killAllSpawns();
				System.out.println("raid destroyed");
				raidGames.remove(raid);
			}
		});
	}
	
	public static void checkLogin(Player player) {
		checkInstances();
		if (Boundary.isIn(player, Boundary.RAIDS) || Boundary.isIn(player, Boundary.OLM)) {
			boolean[] addedToGame = {false};
			Lists.newArrayList(raidGames)
			.stream()
			.filter(Objects::nonNull)
			.filter(raid -> raid.hadPlayer(player))
			.findFirst()
			.ifPresent(raid -> {
				boolean added = raid.login(player);
				if(added) {
					addedToGame[0] = true;
				}
			});
			if(!addedToGame[0]) {
				player.sendMessage("You logged out and were removed from the raid instance!");
				player.getPA().movePlayerUnconditionally(3034, 6067, 0);
				player.setRaidsInstance(null);
				player.specRestore = 120;
				player.specAmount = 10.0;
				player.setRunEnergy(100);
				player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
				player.getPA().refreshSkill(Config.PRAYER);
				player.getHealth().removeAllStatuses();
				player.getHealth().reset();
				player.getPA().refreshSkill(5);
			}
		}
		
	}

}
