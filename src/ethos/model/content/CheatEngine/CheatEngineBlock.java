package ethos.model.content.CheatEngine;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ethos.model.content.tournaments.TourneyManager;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.util.Misc;

public class CheatEngineBlock {
	
	public static boolean tradingPostAlert(Player c) {
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Trading post.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Trading post.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Trading post.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Trading post.");
			List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
			PlayerHandler.sendMessage("@red@[STAFF - MESSAGE] "+c.playerName+" @blu@is using a cheat engine for the @red@trading post!", staff);
			c.teleportToX = 2086;
			c.teleportToY = 4466;
			c.heightLevel = 0;
			c.jailEnd = 214700000;
			c.sendMessage("You have been permenantly jailed for using a cheat engine, please speak to staff.");
			c.getPA().closeAllWindows();
			return true;
	}


		public static boolean BankAlert(Player c) {
				Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Bank.");
				Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Bank.");
				Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Bank.");
				Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Bank.");
				List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
				PlayerHandler.sendMessage("@red@[STAFF - MESSAGE] "+c.playerName+" @blu@is using a cheat engine for the @red@Bank!", staff);
				c.teleportToX = 2086;
				c.teleportToY = 4466;
				c.heightLevel = 0;
				c.jailEnd = 214700000;
				c.sendMessage("You have been permenantly jailed for using a cheat engine, please speak to staff.");
				c.getPA().closeAllWindows();
				return true;
			}
		
		public static boolean PresetAlert(Player c) {
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Presets.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Presets.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Presets.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the Presets.");
			List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
			PlayerHandler.sendMessage("@red@[STAFF - MESSAGE] "+c.playerName+" @blu@is using a cheat engine for the @red@Presets!", staff);
			TourneyManager.getSingleton().leaveLobby(c);
			c.teleportToX = 2086;
			c.teleportToY = 4466;
			c.heightLevel = 0;
			c.jailEnd = 214700000;
			c.sendMessage("You have been permenantly jailed for using a cheat engine, please speak to staff.");
			c.getPA().closeAllWindows();
			return true;
		}
		public static boolean DonatorBoxAlert(Player c) {
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the donator boxes.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the donator boxes.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the donator boxes.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the donator boxes.");
			List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
			PlayerHandler.sendMessage("@red@[STAFF - MESSAGE] "+c.playerName+" @blu@is using a cheat engine for the @red@donator boxes!", staff);
			c.teleportToX = 2086;
			c.teleportToY = 4466;
			c.heightLevel = 0;
			c.jailEnd = 214700000;
			c.sendMessage("You have been permenantly jailed for using a cheat engine, please speak to staff.");
			c.getPA().closeAllWindows();
			return true;
		}
		public static boolean ExperienceAbuseAlert(Player c) {
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the lamps.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the lamps.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the lamps.");
			Misc.println(""+c.playerName+" is trying to use a cheatengine to open the lamps.");
			List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> (p.getRights().isOrInherits(Right.OWNER)|| p.getRights().isOrInherits(Right.MODERATOR))).collect(Collectors.toList());
			PlayerHandler.sendMessage("@red@[STAFF - MESSAGE] "+c.playerName+" @blu@is using a cheat engine for the @red@lamps!", staff);
			c.teleportToX = 2086;
			c.teleportToY = 4466;
			c.heightLevel = 0;
			c.jailEnd = 214700000;
			c.sendMessage("You have been permenantly jailed for using a cheat engine, please speak to staff.");
			c.getPA().closeAllWindows();
			return true;
		}

}