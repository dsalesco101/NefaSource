package ethos.model.content.trails;

import java.util.List;

import ethos.Server;
import ethos.model.items.Item;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.hunter.impling.ItemRarity;
import ethos.util.Misc;

public class TreasureTrails {

	private Player player;

	public TreasureTrails(Player player) {
		this.player = player;
	}

	public void addRewards(RewardLevel difficulty) {
		if (difficulty != RewardLevel.SHARED) {
			player.getNpcDeathTracker().add(Misc.optimizeText(difficulty.name().toLowerCase()));
		}
		//int rights = player.getRights().getPrimary().getValue() - 1;
		List<RewardItem> rewards = CasketRewards.getRandomRewards(difficulty);
		for (RewardItem item : rewards) {
			if (Item.getItemName(item.getId()).contains("3rd") || 
				item.getId() == 2577 || 
				Item.getItemName(item.getId()).contains("mage's")) {
				PlayerHandler.executeGlobalMessage(
						"[<col=CC0000>Treasure</col>] @cr18@ <col=255>" + player.playerName + "</col> received <col=255>" + Item.getItemName(item.getId()) + "</col> from a Treasure Trail.");

			}
			if (!player.getItems().addItem(item.getId(), item.getAmount())) {
				Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
			}
			if (item.getRarity() != ItemRarity.COMMON) {
				player.getCollectionLog().handleDrop(difficulty.ordinal(), item.getId(), item.getAmount());
			}
		}
		displayRewards(rewards);
	}

	public void displayRewards(List<RewardItem> rewards) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeUnsignedWord(6963);
		player.outStream.writeUnsignedWord(rewards.size());
		for (int i = 0; i < rewards.size(); i++) {
			if (player.playerItemsN[i] > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(rewards.get(i).getAmount());
			} else {
				player.outStream.writeByte(rewards.get(i).getAmount());
			}
			if (rewards.size() > 0) {
				player.outStream.writeWordBigEndianA(rewards.get(i).getId() + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.getPA().showInterface(6960);
	}
}
