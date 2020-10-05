package ethos.model.multiplayer_session.trade;

import java.util.Arrays;
import java.util.Objects;

import ethos.Server;
import ethos.model.Area;
import ethos.model.SquareArea;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.multiplayer_session.Multiplayer;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import lombok.val;

import static java.util.Arrays.stream;

public class Trade extends Multiplayer {

	public Trade(Player player) {
		super(player);
	}

	private final Area[] restrictedAreas = {
			SquareArea.of(3083, 3083, 3507, 3513),
			SquareArea.of(3074, 3074, 3507, 3513),
			SquareArea.of(2763, 2763, 2570, 2585),
			SquareArea.of(2753, 2753, 2570, 2585)
	};

	@Override
	public boolean requestable(Player requested) {
		if (requested == null) {
			player.sendMessage("The requested player cannot be found.");
			return false;
		}
		
		if (player.getLootingBag().isWithdrawInterfaceOpen() || player.getLootingBag().isDepositInterfaceOpen() ||
				requested.getLootingBag().isWithdrawInterfaceOpen() || requested.getLootingBag().isDepositInterfaceOpen()) {
			return false;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return false;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return false;
		}
		if (requested.getBankPin().requiresUnlock()) {
			return false;
		}
		boolean inside = stream(restrictedAreas).anyMatch(area -> area.inside(player) || area.inside(requested));
		if (inside) {
			player.sendMessage("You cannot trade while one of you are within a restricted area.");
			return false;
		}
		if (requested.getTutorial().isActive()) {
			player.sendMessage("This player has not completed the tutorial yet.");
			return false;
		}
		if (!player.getMode().isTradingPermitted()) {
			player.sendMessage("You are not permitted to trade other players.");
			return false;
		}
		if (!requested.getMode().isTradingPermitted()) {
			player.sendMessage("That player is on a game mode that restricts trading.");
			return false;
		}
		if (player.playTime < (200)) {
			player.sendMessage("You cannot request a trade, you must play for at least 2 minutes.");
			return false;
		}
		if (requested.playTime < (2000)) {
			player.sendMessage("You cannot trade this player, they have not played for 20 minutes.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().requestAvailable(requested, player, MultiplayerSessionType.TRADE) != null) {
			player.sendMessage("You have already sent a request to this player.");
			return false;
		}
		if (player.getBH().hasTarget() && player.getBH().getTarget() != null && player.getBH().getTarget().getName() != null) {
			if (player.getBH().getTarget().getName().equalsIgnoreCase(requested.playerName)) {
				player.sendMessage("You cannot trade your bounty hunter target.");
				return false;
			}
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot request a trade whilst in a session.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(requested)) {
			player.sendMessage("This player is currently is a session with another player.");
			return false;
		}
		if (player.teleTimer > 0 || requested.teleTimer > 0) {
			player.sendMessage("You cannot request or accept whilst you, or the other player are teleporting.");
			return false;
		}
		if (Boundary.isIn(player, PestControl.GAME_BOUNDARY) || Boundary.isIn(requested, PestControl.GAME_BOUNDARY)) {
			player.sendMessage("You cannot trade in the pest control minigame.");
			return false;
		}
		return true;
	}

	@Override
	public void request(Player requested) {
		if (Objects.isNull(requested)) {
			player.sendMessage("The player cannot be found, try again shortly.");
			return;
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return;
		}
		if (requested.underAttackByPlayer > 0 || requested.underAttackByNpc > 0 && requested.underAttackByNpc != requested.lastNpcAttacked && !requested.inMulti()) {
			player.sendMessage("You cannot trade this person whilst he has been recently in combat or in multi.");
			return;
		}
		player.faceUpdate(requested.getIndex());
		MultiplayerSession session = Server.getMultiplayerSessionListener().requestAvailable(player, requested, MultiplayerSessionType.TRADE);
		if (session != null) {
			session.getStage().setStage(MultiplayerSessionStage.OFFER_ITEMS);
			session.populatePresetItems();
			session.updateMainComponent();
			Server.getMultiplayerSessionListener().removeOldRequests(player);
			Server.getMultiplayerSessionListener().removeOldRequests(requested);
			session.getStage().setAttachment(null);
		} else {
			session = new TradeSession(Arrays.asList(player, requested), MultiplayerSessionType.TRADE);
			if (Server.getMultiplayerSessionListener().appendable(session)) {
				player.sendMessage("Sending trade offer...");
				requested.sendMessage(player.playerName + ":tradereq:");
				session.getStage().setAttachment(player);
				Server.getMultiplayerSessionListener().add(session);
			}
		}
	}

}
