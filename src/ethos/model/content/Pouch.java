package ethos.model.content;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import ethos.model.entity.Entity;
import ethos.model.players.Player;
import ethos.model.items.GameItem;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.Server;
import ethos.util.log.PlayerLogging;
import lombok.Getter;



public abstract class Pouch {
	@Getter
	protected Player player;
	
	@Getter
	protected List<GameItem> items = Lists.newArrayList();
	
	public void handleDeath(Player player, String entity){
		Entity killer = player.getKiller();

		PlayerLogging.write(PlayerLogging.LogType.DEATH, player, String.format("Dropped from sack %s", items));
		for (Iterator<GameItem> iterator=items.iterator(); iterator.hasNext();) {
			GameItem item = iterator.next();

			if (item == null) {
				continue;
			}
			if (item.getId() <= 0 || item.getAmount() <= 0) {
				continue;
			}
			if (entity.equals("PVP")) {
				if (killer != null && killer instanceof Player) {
					Player playerKiller = (Player) killer;
					if (playerKiller.getMode().isItemScavengingPermitted()) {
						Server.itemHandler.createGroundItem(playerKiller, item.getId(), player.getX(), player.getY(), player.getHeight(), item.getAmount(), player.killerId);
					} else {
						Server.itemHandler.createUnownedGroundItem(item.getId(), player.getX(), player.getY(), player.getHeight(), item.getAmount());
					}
				}
			} else {
				Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(),
				                                    player.getHeight(), item.getAmount(), player.getIndex());
			}
			iterator.remove();
		}
	}

	public int countItems(int id) {
		int count = 0;
		for (GameItem item : items) {
			if (item.getId() == id + 1) {
				count += item.getAmount();
			}
		}
		return count;
	}

	public void withdrawItems(){
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		for (Iterator<GameItem> iterator = items.iterator(); iterator.hasNext();) {
			GameItem item = iterator.next();
			if (!player.getItems().addItem(item.getId(), item.getAmount())) {
				break;
			}
			iterator.remove();
		}
	}

	public boolean sackContainsItem(int id) {
		for (GameItem item : items) {
			if (item.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean addItemToList(int id, int amount) {
		for (GameItem item : items) {
			if (item.getId() == id) {
				if (item.getAmount() + amount >= 61) {
					return false;
				}
				if (player.getItems().isStackable(id)) {
					item.incrementAmount(amount);
					return false;
				}
			}
		}
		items.add(new GameItem(id, amount));
		return true;
	}

	public boolean configurationPermitted() {
		if (player.inDuelArena() || player.inPcGame() || player.inPcBoat() || player.isInJail() || player.getInterfaceEvent().isActive() || player.getPA().viewingOtherBank
				|| player.isDead || player.getLootingBag().isWithdrawInterfaceOpen() || player.getLootingBag().isDepositInterfaceOpen()) {
			return false;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return false;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return false;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			player.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return false;
		}
		if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
			player.sendMessage("You must decline the trade to start walking.");
			return false;
		}
		if (player.isStuck) {
			player.isStuck = false;
			player.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return false;
		}
		return true;
	}

}
