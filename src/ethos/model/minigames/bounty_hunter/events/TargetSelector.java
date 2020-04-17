package ethos.model.minigames.bounty_hunter.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ethos.Server;
import ethos.ServerState;
import ethos.event.CycleEventContainer;
import ethos.model.minigames.bounty_hunter.BountyHunter;
import ethos.model.minigames.bounty_hunter.Target;
import ethos.model.minigames.bounty_hunter.TargetEvent;
import ethos.model.minigames.bounty_hunter.TargetState;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 13, 2014, 2:27:05 PM
 */
public class TargetSelector extends TargetEvent {

	public TargetSelector(BountyHunter bountyHunter) {
		super(bountyHunter);
	}

	@Override
	public void execute(CycleEventContainer container) {
		BountyHunter bh = super.bountyHunter;
		Player player = bh.getPlayer();
		if (!isExecutable()) {
			container.stop();
			return;
		}
		Predicate<Player> viableTarget = t -> t != null 
				&& t != player //player isnt null
				&& t.inWild() //player must be in wild
				&& !Boundary.isIn(t, Boundary.SAFEPK) //boundary is not in safe pk
				&& !t.getBH().hasTarget() //player doesnt have target already
				&& t.wildLevel > 0 //above 0 wild
				&& !t.getBH().getTargetState().hasKilledRecently() //player killed recently (gives them 1 minute before new target)
				&& !player.getPlayerKills().killedRecently(t.connectedFrom)//checks if you kill same person again
				&& !t.getPlayerKills().killedRecently(player.connectedFrom) 
				&& !t.isInvisible() //wont assign invisible people
				&& (!t.connectedFrom.equalsIgnoreCase(player.connectedFrom) || Server.getConfiguration().getServerState() != ServerState.PUBLIC_PRIMARY) //same ip
				&& !t.inClanWars();//not in clan wars
		List<Player> possibleTargets = new ArrayList<>(1);
		for (int levelOffset = 0; levelOffset < player.wildLevel + 1 ; levelOffset++) {
			//player.sendMessage("" + levelOffset);
			final int level = levelOffset;
			possibleTargets = PlayerHandler.nonNullStream().filter(viableTarget.and(t -> Misc.combatDifference(player, t) <= level)).collect(Collectors.toList());
			
			if (possibleTargets.size() > 0) {
				break;
			}
		}
		if (possibleTargets.size() <= 0) {
			return;
		}
		Optional<Player> randomTarget = Optional.of(possibleTargets.get(Misc.random(possibleTargets.size() - 1)));
		randomTarget.ifPresent(target -> {
			assignTarget(player, target);
			assignTarget(target, player);
			container.stop();
			return;
		});
	}

	@Override
	public void onStopped() {
		if (Objects.nonNull(bountyHunter.getPlayer())) {
			bountyHunter.setTargetState(TargetState.NONE);
		}
	}

	/**
	 * Determines if the selection event should be executed based on some conditions.
	 * 
	 * @return if true, the event will start. Otherwise, the event should come to a halt.
	 */
	public boolean isExecutable() {
		BountyHunter bh = super.bountyHunter;
		Player player = bh.getPlayer();
		if (Objects.isNull(player) || player.disconnected) {
			return false;
		}
		if (bh.getTargetState().hasKilledRecently() || bh.getTargetState().isPenalized() || player.isInvisible()
				|| !player.inWild() || player.inClanWars() || bh.hasTarget() || Boundary.isIn(player, Boundary.SAFEPK)) {
			return false;
		}
		return true;
	}

	private void assignTarget(Player player, Player target) {
		player.getBH().setTargetState(TargetState.SELECTED);
		player.getBH().setTarget(new Target(target.playerName));
		player.getBH().updateTargetUI();
		player.sendMessage("<col=FF0000>You've been assigned a target: " + Misc.capitalize(target.playerName) + "</col>");
		player.getPA().createPlayerHints(10, target.getIndex());
	}
}
