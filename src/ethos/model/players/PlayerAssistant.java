package ethos.model.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ethos.model.items.bank.Bank;
import ethos.model.minigames.inferno.Inferno;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.players.skills.slayer.SlayerUnlock;
import ethos.util.log.PlayerLogging;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import ethos.Config;
import ethos.Server;
import ethos.clip.PathChecker;
import ethos.event.CycleEventHandler;
import ethos.event.DelayEvent;
import ethos.event.impl.WheatPortalEvent;
import ethos.clip.doors.Location;
import ethos.model.content.RunePouch;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.content.bonus.DoubleExperience;
import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.content.instances.InstancedArea;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.content.kill_streaks.Killstreak;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.entity.Entity;
import ethos.model.items.EquipmentSet;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.items.bank.BankTab;
import ethos.model.lobby.Lobby;
import ethos.model.lobby.LobbyManager;
import ethos.model.lobby.LobbyType;
import ethos.model.minigames.bounty_hunter.TargetState;
import ethos.model.minigames.lighthouse.DisposeType;
import ethos.model.minigames.pest_control.PestControl;
import ethos.model.minigames.pk_arena.Highpkarena;
import ethos.model.minigames.pk_arena.Lowpkarena;
import ethos.model.minigames.raids.Raids;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.instance.InstanceSoloFight;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.Degrade.DegradableItem;
import ethos.model.players.combat.effects.DragonfireShieldEffect;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.magic.NonCombatSpells;
import ethos.model.players.mode.Mode;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Cooking;
import ethos.model.players.skills.Fishing;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.SkillHandler;
import ethos.model.players.skills.Smelting;
import ethos.model.players.skills.Smelting.Bars;
import ethos.model.players.skills.crafting.CraftingData;
import ethos.model.players.skills.crafting.Enchantment;
import ethos.model.players.skills.mining.Mineral;
import ethos.model.players.skills.slayer.Task;
import ethos.model.players.skills.woodcutting.Tree;
import ethos.model.shops.ShopAssistant;
import ethos.net.outgoing.messages.ComponentVisibility;
import ethos.util.Misc;
import ethos.util.Stream;
import ethos.world.Clan;

public class PlayerAssistant {

	private final Player c;

	public PlayerAssistant(Player Client) {
		this.c = Client;
	}

	public int CraftInt, Dcolor, FletchInt;

	public void destroyItem() {

		int itemId = c.droppedItem;
		if (c.getItems().playerHasItem(itemId) && itemId != -1
				&& c.playerItemsN[c.getItems().getItemSlot(itemId)] > 0) {
			@SuppressWarnings("unused")
			String itemName = ItemAssistant.getItemName(itemId);
			PlayerLogging.write(PlayerLogging.LogType.DROP, c, String.format("Destroyed %s", new GameItem(itemId, c.playerItemsN[c.getItems().getItemSlot(itemId)])));
			c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId),
					c.playerItemsN[c.getItems().getItemSlot(itemId)]);
			// c.sendMessage("Your " + itemName + " vanishes as you drop it on the
			// ground.");
			removeAllWindows();
		}
	}

	/**
	 * Retribution
	 *
	 * @param i
	 *            Player i
	 * @return Return statement
	 */
	private boolean checkRetributionReqsSingle(int i) {
		if (c.inPits && PlayerHandler.getPlayers().get(i).inPits || PlayerHandler.getPlayers().get(i).inDuel) {
			if (PlayerHandler.getPlayers().get(i) != null || i == c.getIndex())
				return true;
		}
		int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel,
				PlayerHandler.getPlayers().get(i).combatLevel);
		if (PlayerHandler.getPlayers().get(i) == null || i != c.getIndex()
				|| !PlayerHandler.getPlayers().get(i).inWild() || combatDif1 > c.wildLevel
				|| combatDif1 > PlayerHandler.getPlayers().get(i).wildLevel) {
			return false;
		}
		if (Config.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.getPlayers().get(i).inMulti()) {
				if (PlayerHandler.getPlayers().get(i).underAttackByPlayer == c.getIndex()
						|| PlayerHandler.getPlayers().get(i).underAttackByPlayer <= 0
						|| PlayerHandler.getPlayers().get(i).getIndex() == c.underAttackByPlayer || c.underAttackByPlayer <= 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void appendRetribution(Player o) {
		System.out.println("here");
		if (o != null && checkRetributionReqsSingle(c.getIndex())) {
			System.out.println("here1");
			if (!c.goodDistance(o.getX(), o.getY(), c.getX(), c.getY(), 1))
				return;
			int damage = (int) (getLevelForXP(c.playerXP[5]) * 2.50 / 10);
			if (o.getLevelForXP(c.playerXP[3]) - damage < 0) {
				damage = o.getLevelForXP(c.playerXP[3]);
			}
			System.out.println("here2");
			c.gfx0(437);
			c.appendDamage(damage, Hitmark.HIT);
			c.getPA().refreshSkill(3);
			c.totalPlayerDamageDealt += damage;
		}
	}

	public void openWebAddress(String address) {
		sendString(address, 12_000);
	}

	public void sendPlayerObjectAnimation(Player player, int x, int y, int animation, int type, int orientation,
			int height) {
		if (player == null)
			return;
		// if (p.getPosition().isViewableFrom(position)) {
		player.getOutStream().createFrame(85);
		player.getOutStream().writeByteC(y - (player.mapRegionY * 8));
		player.getOutStream().writeByteC(x - (player.mapRegionX * 8));
		player.getOutStream().createFrame(160);
		player.getOutStream().writeByteS(((0 & 7) << 4) + (0 & 7));
		player.getOutStream().writeByteS((type << 2) + (orientation & 3));
		player.getOutStream().writeWordA(animation);
		// }
	}

	public void destroyInterface(String config) {// Destroy item created by Remco
		int itemId = c.droppedItem;
		String itemName = ItemAssistant.getItemName(c.droppedItem);
		String[][] info = { // The info the dialogue gives
				{ "Are you sure you want to " + config + " this item?", "14174" }, { "Yes.", "14175" },
				{ "No.", "14176" }, { "", "14177" }, { "If you wish to remove this confirmation,", "14182" },
				{ "simply type '::toggledrop off'.", "14183" }, { itemName, "14184" } };
		sendFrame34(itemId, 0, 14171, 1);
		for (String[] anInfo : info)
			sendFrame126(anInfo[0], Integer.parseInt(anInfo[1]));
		sendFrame164(14170);
	}

	public void imbuedHeart() {
		c.playerLevel[6] += imbuedHeartStats();
		c.getPA().refreshSkill(6);
		c.gfx0(1316);
		c.sendMessage("Your Magic has been temportarily raised.");
	}

	private int imbuedHeartStats() {
		int increaseBy;
		int skill = 6;
		increaseBy = (int) (c.getLevelForXP(c.playerXP[skill]) * .10);
		if (c.playerLevel[skill] + increaseBy > c.getLevelForXP(c.playerXP[skill]) + increaseBy + 1) {
			return c.getLevelForXP(c.playerXP[skill]) + increaseBy - c.playerLevel[skill];
		}
		return increaseBy;
	}

	public void assembleSlayerHelmet() {
		if (!c.getSlayer().getUnlocks().contains(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
			c.sendMessage("You don't have the knowledge to do this. You must learn how to first.");
			return;
		}
		if (c.playerLevel[Player.playerCrafting] < 55) {
			c.sendMessage("@blu@You need a crafting level of 55 to assemble a slayer helmet.");
			return;
		}
		if (c.getItems().playerHasItem(4166) && c.getItems().playerHasItem(4168) && c.getItems().playerHasItem(4164)
				&& c.getItems().playerHasItem(4551) && c.getItems().playerHasItem(8901)) {
			c.sendMessage("@blu@You assemble the pieces and create a full slayer helmet!");
			c.getItems().deleteItem(4166, 1);
			c.getItems().deleteItem(4164, 1);
			c.getItems().deleteItem(4168, 1);
			c.getItems().deleteItem(4551, 1);
			c.getItems().deleteItem(8901, 1);
			c.getItems().addItem(11864, 1);
		} else {
			c.sendMessage(
					"You need a @blu@Facemask@bla@, @blu@Nose peg@bla@, @blu@Spiny helmet@bla@ and @blu@Earmuffs");
			c.sendMessage("@bla@in order to assemble a slayer helmet.");
		}
	}

	public long lastReward = System.currentTimeMillis();

	public void otherInv(Player c, Player o) {
		if (o == c || o == null || c == null)
			return;
		int[] backupItems = c.playerItems;
		int[] backupItemsN = c.playerItemsN;
		c.playerItems = o.playerItems;
		c.playerItemsN = o.playerItemsN;
		c.getItems().resetItems(3214);
		c.playerItems = backupItems;
		c.playerItemsN = backupItemsN;
	}

	public void multiWay(int i1) {
		// synchronized(c) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.setUpdateRequired(true);
		c.setAppearanceUpdateRequired(true);

	}

	private final int[] backupItems = new int[Config.BANK_TAB_SIZE];
	private final int[] backupItemsN = new int[Config.BANK_TAB_SIZE];

	public void otherBank(Player c, Player o) {
		if (o == c || o == null || c == null) {
			return;
		}

		for (int i = 0; i < o.bankItems.length; i++) {
			backupItems[i] = c.bankItems[i];
			backupItemsN[i] = c.bankItemsN[i];
			c.bankItemsN[i] = o.bankItemsN[i];
			c.bankItems[i] = o.bankItems[i];
		}
		openUpBank();

		for (int i = 0; i < o.bankItems.length; i++) {
			c.bankItemsN[i] = backupItemsN[i];
			c.bankItems[i] = backupItems[i];
		}
	}

	public void sendString(final String s, final int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}
	/**
     * @author Grant_ | www.rune-server.ee/members/grant_ | 10/15/19
     * @param s
     */
    public void sendBroadCast(final String s) {
        if (c.getOutStream() != null && c != null) {
            c.getOutStream().createFrameVarSizeWord(170);
            c.getOutStream().writeString(s);
            c.getOutStream().endFrameVarSizeWord();
            c.flushOutStream();
        }
    }

	/**
	 * Changes the main displaying sprite on an interface. The index represents the
	 * location of the new sprite in the index of the sprite array.
	 * 
	 * @param componentId
	 *            the interface
	 * @param index
	 *            the index in the array
	 */
	public void sendChangeSprite(int componentId, byte index) {
		if (c == null || c.getOutStream() == null) {
			return;
		}
		Stream stream = c.getOutStream();
		stream.createFrame(7);
		stream.writeDWord(componentId);
		stream.writeByte(index);
		c.flushOutStream();
	}

	public static void sendItems(Player player, int componentId, List<GameItem> items, int capacity) {
		if (player == null || player.getOutStream() == null) {
			return;
		}
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeUnsignedWord(componentId);
		int length = items.size();
		int current = 0;
		player.getOutStream().writeUnsignedWord(length);
		for (GameItem item : items) {
			if (item.getAmount() > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.getAmount());
			} else {
				player.getOutStream().writeByte(item.getAmount());
			}
			player.getOutStream().writeWordBigEndianA(item.getId() + 1);
			current++;
		}
		for (; current < capacity; current++) {
			player.getOutStream().writeByte(1);
			player.getOutStream().writeWordBigEndianA(-1);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}

	public void setConfig(int id, int state) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
		// }
	}

	public double getAgilityRunRestore(Player c) {
		return 2260 - (c.playerLevel[16] * 10);
	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(interfaceChild);
			c.getOutStream().writeUnsignedWord(zoom);
			c.getOutStream().writeUnsignedWord(itemId);
			c.flushOutStream();
		}
	}

	public void itemOnInterface(int item, int amount, int frame, int slot) {
		if (c.getOutStream() != null) {
			c.outStream.createFrameVarSizeWord(34);
			c.outStream.writeUnsignedWord(frame);
			c.outStream.writeByte(slot);
			c.outStream.writeUnsignedWord(item + 1);
			c.outStream.writeByte(255);
			c.outStream.writeDWord(amount);
			c.outStream.endFrameVarSizeWord();
		}
	}

	public void sendTabAreaOverlayInterface(int interfaceId) {
		if (c.getOutStream() != null) {
			c.outStream.createFrame(142);
			c.outStream.writeUnsignedWord(interfaceId);
			c.flushOutStream();
		}
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	/**
	 * If the player is using melee and is standing diagonal from the opponent, then
	 * move towards opponent.
	 */

	public void movePlayerDiagonal(int i) {
		Player c2 = PlayerHandler.players[i];
		boolean hasMoved = false;
		int c2X = c2.getX();
		int c2Y = c2.getY();
		if (c.goodDistance(c2X, c2Y, c.getX(), c.getY(), 1)) {
			if (c.getX() != c2.getX() && c.getY() != c2.getY()) {
				if (c.getX() > c2.getX() && !hasMoved) {
					if (c.getRegionProvider().getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
						hasMoved = true;
						walkTo(-1, 0);
					}
				} else if (c.getX() < c2.getX() && !hasMoved) {
					if (c.getRegionProvider().getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
						hasMoved = true;
						walkTo(1, 0);
					}
				}

				if (c.getY() > c2.getY() && !hasMoved) {
					if (c.getRegionProvider().getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
						walkTo(0, -1);
					}
				} else if (c.getY() < c2.getY() && !hasMoved) {
					if (c.getRegionProvider().getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
						walkTo(0, 1);
					}
				}
			}
		}
	}

	public Clan getClan() {
		if (Server.clanManager.clanExists(c.playerName)) {
			return Server.clanManager.getClan(c.playerName);
		}
		return null;
	}

	public void clearClanChat() {
		c.clanId = -1;
		c.getPA().sendString("Talking in: ", 18139);
		c.getPA().sendString("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			c.getPA().sendString("", j);
		}
	}

	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(c.playerName);
		if (!exists || c.clan == null) {
			sendString("Join chat", 18135);
			sendString("Talking in: Not in chat", 18139);
			sendString("Owner: None", 18140);
		}
		if (!exists) {
			sendString("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				sendString(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendString("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendString("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(c.playerName);
		sendString(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendString(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					sendString("<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index), 18323 + index);
				} else {
					sendString("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendString(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendString("", 18424 + index);
				}
			}
		}
	}

	public void resetAutocast() {
		resetAutocast(true);
	}
	
	public void resetAutocast(boolean sendWeapon) {
		c.autocastId = -1;
		c.autocastingDefensive = false;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
		c.getPA().sendFrame36(109, 0);
		if (sendWeapon) {
			c.setSidebarInterface(0, 328);
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon]);
		}
		c.getPA().sendAutocastState(false);
	}

	public int getItemSlot(int itemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == itemID) {
				return i;
			}
		}
		return -1;
	}

	public boolean isItemInBag(int itemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == itemID) {
				return true;
			}
		}
		return false;
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void turnTo(int pointX, int pointY) {
		c.focusPointX = 2 * pointX + 1;
		c.focusPointY = 2 * pointY + 1;
		c.setUpdateRequired(true);
	}

	public void movePlayerUnconditionally(int x, int y, int h) {
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		c.teleTimer = 4;
		requestUpdates();
		c.lastMove = System.currentTimeMillis();
	}

	public void movePlayer(int x, int y) {
		movePlayer(x, y, c.heightLevel);
	}

	public void movePlayer(int x, int y, int h) {
		if (c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
			if (!c.isDead) {
				return;
			}
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		//if (System.currentTimeMillis() - c.lastSpear < 3000) {
		//	c.sendMessage("You're trying to move too fast.");
		//	return;
		//}
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return;
		}
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
		c.resetWalkingQueue();
		c.getCombat().resetPlayerAttack();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		c.teleTimer = 2;
		requestUpdates();
	}

	public void movePlayer(Coordinate coord) {
		movePlayer(coord.getX(), coord.getY(), coord.getH());
	}

	public void movePlayerDuel(int x, int y, int h) {
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session) && session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION
				&& Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			return;
		}
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		requestUpdates();
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	private int absX;
	private int absY;
	public int heightLevel;

	public static void sendQuest(Player client, String s, int i) {
		client.getOutStream().createFrameVarSizeWord(126);
		client.getOutStream().writeString(s);
		client.getOutStream().writeWordA(i);
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - (c.mapRegionY * 8));
		c.getOutStream().writeByteC(x - (c.mapRegionX * 8));
		c.getOutStream().createFrame(4);
		c.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
										// //Tiles away from
		// absX and absY.
		c.getOutStream().writeUnsignedWord(id); // Graphic ID.
		c.getOutStream().writeByte(heightS); // Height of the graphic when
												// cast.
		c.getOutStream().writeUnsignedWord(timeBCS); // Time before the graphic
												// plays.
		c.flushOutStream();
	}

	public void createArrow(int type, int id) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(type); // 1=NPC, 10=Player
			c.getOutStream().writeUnsignedWord(id); // NPC/Player ID
			c.getOutStream().write3Byte(0); // Junk
		}
	}

	public void createArrow(int x, int y, int height, int pos) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
												// = west, 4 = east, 5 = south,
												// 6 = north)
			c.getOutStream().writeUnsignedWord(x); // X-Coord of Object
			c.getOutStream().writeUnsignedWord(y); // Y-Coord of Object
			c.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public void loadQuests() {

	}

	public void sendFrame126(String s, int id) {
		if (s == null) {
			return;
		}
		if (!c.checkPacket126Update(s, id) && !s.startsWith("www")
				&& !s.startsWith("http") && id != 12_000/* url id */) {
			return;
		}
		if (c.getOutStream() != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	public void sendLink(String s) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			if(skillNum==21){
				skillNum=22;
			}
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
		// }
	}

	public void sendFrame36(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}

	}

	public void sendFrame185(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}

	}

	public void sendLogout() {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(109);
			c.flushOutStream();
		}
	}

	public void resetScrollBar(int interfaceId) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(2);
			c.getOutStream().writeUnsignedWord(interfaceId);
			c.flushOutStream();
		}
	}

	public void setScrollableMaxHeight(int interfaceId, int scrollMax) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(3);
			c.getOutStream().writeUnsignedWord(interfaceId);
			c.getOutStream().writeUnsignedWord(scrollMax);
			c.flushOutStream();
		}
	}

	public void showInterface(int interfaceid) {
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
			c.sendMessage("You should stop what you are doing before continuing.");
			return;
		}
		if (c.getOutStream() != null && c != null) {
			c.setInterfaceOpen(interfaceid);
			c.getOutStream().createFrame(97);
			c.getOutStream().writeUnsignedWord(interfaceid);
			c.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeUnsignedWord(SubFrame);
			c.flushOutStream();

		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeUnsignedWord(SubFrame);
			c.getOutStream().writeUnsignedWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame171(int state, int componentId) {
		if (c.getPacketDropper().requiresUpdate(171, new ComponentVisibility(state, componentId))) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(171);
				c.getOutStream().writeByte(state);
				c.getOutStream().writeUnsignedWord(componentId);
				c.flushOutStream();
			}
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeUnsignedWord(MainFrame);
			c.getOutStream().writeUnsignedWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeUnsignedWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}

	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}

	}

	public void sendFrame164(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}

	}

	public void sendFrame87(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}

	}

	public void sendPM(long name, RightGroup rightsGroup, byte[] chatMessage) {
		c.getOutStream().createFrameVarSize(196);
		c.getOutStream().writeQWord(name);
		c.getOutStream().writeDWord(new Random().nextInt());
		writeRightsGroup(c.getOutStream(), rightsGroup);
		c.getOutStream().writeBytes(chatMessage, chatMessage.length, 0);
		c.getOutStream().endFrameVarSize();
	}

	public static void writeRightsGroup(Stream outStream, RightGroup rightsGroup) {
		List<Right> rightsSet = rightsGroup.getDisplayedRights();
		outStream.writeByte(rightsSet.size());
		for (Right right : rightsSet) {
			outStream.writeByte(right.ordinal());
		}
	}

	public void createPlayerHints(int type, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeUnsignedWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeUnsignedWord(x);
			c.getOutStream().writeUnsignedWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}

	}

	private void sendAutocastState(boolean state) {
		if (c == null || c.disconnected || c.getSession() == null) {
			return;
		}
		Stream stream = c.getOutStream();
		if (stream == null) {
			return;
		}
		stream.createFrame(15);
		stream.writeByte(state ? 1 : 0);
		c.flushOutStream();
	}

	public void sendFriend(long friend, int world) {
		c.getOutStream().createFrame(50);
		c.getOutStream().writeQWord(friend);
		c.getOutStream().writeByte(world);

	}

	public void removeAllWindows() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.nextChat = 0;
			c.dialogueOptions = 0;
		}
		resetVariables();
	}

	public void updatePoisonStatus() {
		sendConfig(1359, c.getHealth().getStatus().ordinal());
	}

	public void closeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.isBanking = false;
			c.inSafeBox = false;
			c.viewingPresets = false;
			c.setEnterAmountInterfaceId(0);
			c.getLootingBag().setWithdrawInterfaceOpen(false);
			c.getLootingBag().setDepositInterfaceOpen(false);
			c.getLootingBag().setSelectDepositAmountInterfaceOpen(false);
			c.viewingRunePouch = false;
			c.nextChat = 0;
			c.dialogueOptions = 0;
			c.placeHolderWarning = false;
			resetVariables();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith screen
			c.outStream.writeUnsignedWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeUnsignedWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}

	}

	public void walkableInterface(int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}

	}

	public void shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		if (c != null && c.getOutStream() != null) {
			c.getOutStream().createFrame(35);
			c.getOutStream().writeByte(verticleAmount);
			c.getOutStream().writeByte(verticleSpeed);
			c.getOutStream().writeByte(horizontalAmount);
			c.getOutStream().writeByte(horizontalSpeed);
			c.flushOutStream();
		}
	}

	public void resetShaking() {
		shakeScreen(1, 1, 1, 1);
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		// synchronized(c) {
		if (c != null && c.getOutStream() != null) {
			c.getOutStream().createFrame(99);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}

	public void sendCrashFrame() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(123);
			c.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	private void frame1() {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}

			}
		}
	}

	/**
	 * Creating projectile
	 **/
	private void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeUnsignedWord(lockon);
			c.getOutStream().writeUnsignedWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeUnsignedWord(time);
			c.getOutStream().writeUnsignedWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();

		}
	}

	private void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeUnsignedWord(lockon);
			c.getOutStream().writeUnsignedWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeUnsignedWord(time);
			c.getOutStream().writeUnsignedWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}

	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player p = PlayerHandler.players[i];
				if (p.WithinDistance(c.absX, c.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == c.heightLevel) {
						if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeUnsignedWord(AtkIndex);
							p.outStream.writeUnsignedWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeUnsignedWord(51);
							p.outStream.writeUnsignedWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int delay) {
		if (delay <= 0) {
			createPlayersProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight, lockon, time);
		} else {
			Server.getEventHandler().submit(new DelayEvent(delay) {
				@Override
				public void onExecute() {
					createPlayersProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight, lockon,
							time);
				}
			});
		}
	}
	public void mysteryBoxItemOnInterface(int item, int amount , int frame, int slot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeUnsignedWord(frame);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeUnsignedWord(item + 1);
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
PlayerHandler
.nonNullStream()
.filter(p -> p.distanceToPoint(x, y) <= 25)
.filter(p -> p.getHeight() == c.getHeight())
.filter(p -> c.sameInstance(p))
.forEach(p -> {
p.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
endHeight, lockon, time);
});

}


	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		PlayerHandler
		.nonNullStream()
		.filter(p -> p.distanceToPoint(x, y) <= 25)
		.filter(p -> p.getHeight() == c.getHeight())
		.filter(c::sameInstance)
		.forEach(p -> {
			p.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight,
					lockon, time, slope);
		});
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeUnsignedWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeUnsignedWord(time);
			c.flushOutStream();
		}

	}

	public void useChargeSkills() {
		int[][] skillsNeck = { { 11968, 11970, 5 }, { 11970, 11105, 4 }, { 11105, 11107, 3 }, { 11107, 11109, 2 },
				{ 11109, 11111, 1 }, { 11111, 11113, 0 } };
		for (int[] aSkillsNeck : skillsNeck) {
			if (c.itemUsing == aSkillsNeck[0]) {
				if (c.isOperate) {
					c.getItems().deleteItem(aSkillsNeck[0], 1);
					c.getItems().addItem(aSkillsNeck[1], 1);
				}
				if (aSkillsNeck[2] > 1) {
					c.sendMessage("Your amulet has " + aSkillsNeck[2] + " charges left.");
				} else {
					c.sendMessage("Your amulet has " + aSkillsNeck[2] + " charge left.");
				}
			}
		}
		// c.getItems().updateSlot(c.playerAmulet);
		c.isOperate = false;
		c.itemUsing = -1;
	}

	public void stillGfx(int id, int x, int y, int height, int time, byte face) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(face);
			c.getOutStream().writeUnsignedWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeUnsignedWord(time);
			c.flushOutStream();
		}

	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				if (p.getOutStream() != null) {
					if (p.distanceToPoint(x, y) <= 25) {
						p.getPA().stillGfx(id, x, y, height, time);
					}
				}
			}
		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	private String optionType = "null";

	public void showOption(int i, int l, String s, int a) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}

		}
	}

	/**
	 * Open bank
	 **/
	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeUnsignedWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeUnsignedWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/6/19
	 * @param frame
	 * @param item
	 * @param slot
	 * @param amount
	 * @param opaque
	 */
	public void sendItemToSlotWithOpacity(int frame, int item, int slot, int amount, boolean opaque) {
		final int bitpackedValue = opaque ? setBit(15, item + 1) : item + 1;
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeUnsignedWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeUnsignedWord(bitpackedValue);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public void sendStringContainer(int containerInterfaceId, List<String> strings) {
		String[] stringArray = new String[strings.size()];
		for (int index = 0; index < strings.size(); index++)
			stringArray[index] = strings.get(index);
		sendStringContainer(containerInterfaceId, stringArray);
	}

	public void sendStringContainer(int containerInterfaceId, String...strings) {
		if (c.outStream != null) {
			c.outStream.createFrameVarSizeWord(5);
			c.outStream.writeUnsignedWord(containerInterfaceId);
			c.outStream.writeUnsignedWord(strings.length);
			Arrays.stream(strings).forEach(string -> c.outStream.writeString(string));
			c.outStream.endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	private int setBit(int bit, int target) {
        // Create mask
        int mask = 1 << bit;
        // Set bit
        return target | mask;
     }
	
	private boolean isKthBitSet(int n, int k) { 
		if ((n & (1 << (k - 1))) == 1) {
			return true;
		} else {
			return false;
		}
	}

	public void openUpBank() {
		c.inBank = true;
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		c.getPA().sendChangeSprite(58014, c.placeHolders ? (byte) 1 : (byte) 0);
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
			c.sendMessage("You should stop what you are doing before opening the bank.");
			return;
		}
		resetVariables();
		if (c.getBankPin().isLocked() && c.getBankPin().getPin().trim().length() > 0) {
			c.getBankPin().open(2);
			c.isBanking = false;
			c.inSafeBox = false;
			return;
		}
//		if (c.takeAsNote)
//			sendFrame36(116, 1);
//		else
//			sendFrame36(116, 0);

		if (c.inWild() && !(c.getRights().isOrInherits(Right.ADMINISTRATOR))) {
			c.sendMessage("You can't bank in the wilderness!");
			return;
		}
		if (!c.getMode().isBankingPermitted()) {
			c.sendMessage("Your game mode prohibits use of the banking system.");
			return;
		}
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			} else {
				c.sendMessage("You cannot bank whilst dueling.");
				return;
			}
		}
		c.getPA().sendFrame126("Search", 58063);
		if (c.getOutStream() != null && c != null) {
			c.isBanking = true;
			c.getItems().resetItems(5064);
			c.getItems().resetBankInstant();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(Bank.INTERFACE_ID);
			c.getOutStream().writeUnsignedWord(5063);
			c.flushOutStream();
		}
	}

	public boolean viewingOtherBank;
	private final BankTab[] backupTabs = new BankTab[9];

	public void resetOtherBank() {
		for (int i = 0; i < backupTabs.length; i++)
			c.getBank().setBankTab(i, backupTabs[i]);
		viewingOtherBank = false;
		removeAllWindows();
		c.isBanking = false;
		c.inSafeBox = false;
		c.getBank().setCurrentBankTab(c.getBank().getBankTab()[0]);
		c.getItems().resetBank();
		c.sendMessage("You are no longer viewing another players bank.");
	}

	public void openOtherBank(Player otherPlayer) {
		if (otherPlayer == null)
			return;

		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		if (otherPlayer.getPA().viewingOtherBank) {
			c.sendMessage("That player is viewing another players bank, please wait.");
			return;
		}
		for (int i = 0; i < backupTabs.length; i++)
			backupTabs[i] = c.getBank().getBankTab(i);
		for (int i = 0; i < otherPlayer.getBank().getBankTab().length; i++)
			c.getBank().setBankTab(i, otherPlayer.getBank().getBankTab(i));
		c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		viewingOtherBank = true;
		openUpBank();
	}

	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {

		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (!Objects.isNull(session)) {
			if (session.getRules().contains(Rule.NO_DRINKS)) {
				c.sendMessage("Drinks have been disabled for this duel.");
				return;
			}
		}
		if (!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the " + ItemAssistant.getItemName(itemId).toLowerCase() + ".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/*
	 * Vengeance
	 */

	public void castVengeance() {
		c.usingMagic = true;
		if (c.playerLevel[1] < 40) {
			c.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCast < 30000) {
			c.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (c.vengOn) {
			c.sendMessage("You already have vengeance casted.");
			return;
		}
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (!Objects.isNull(session)) {
			if (session.getRules().contains(Rule.NO_MAGE)) {
				c.sendMessage("You can't cast this spell because magic has been disabled.");
				return;
			}
		}
		if (!c.getCombat().checkMagicReqs(55)) {
			return;
		}
		c.getPA().sendGameTimer(ClientGameTimer.VENGEANCE, TimeUnit.SECONDS, 30);
		c.startAnimation(4409); //change in future
		c.gfx100(726);
		addSkillXP(c.getMode().isOsrs() ? 112 : 10000, 6, true);
		refreshSkill(6);
		c.vengOn = true;
		c.usingMagic = false;
		c.lastCast = System.currentTimeMillis();
	}

	/**
	 * Magic on items
	 **/
	public void alchemy(int itemId, String alch) {

		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@There is no need to do this here.");
			return;
		}

		switch (alch) {
		case "low":
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				for (int[] items : EquipmentSet.IRON_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch iron man amour.");
						return;
					}
				}
				for (int[] items : EquipmentSet.ULTIMATE_IRON_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch ultimate iron man amour.");
						return;
					}
				}
				for (int[] items : EquipmentSet.HC_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch hardcore iron man amour.");
						return;
					}
				}
				if (itemId == LootingBag.LOOTING_BAG || itemId == LootingBag.LOOTING_BAG_OPEN || itemId == RunePouch.RUNE_POUCH_ID) {
					c.sendMessage("This kind of sorcery cannot happen.");
					return;
				}
				if (!c.getCombat().checkMagicReqs(49)) {
					return;
				}
				if (!c.getItems().playerHasItem(itemId, 1) || itemId == 995) {
					return;
				}
				if (Boundary.isIn(c, Boundary.FOUNTAIN_OF_RUNE_BOUNDARY)) {
					c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.LOW_ALCH);
				}
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(995, ShopAssistant.getItemShopValue(itemId) / 3);
				c.startAnimation(MagicData.MAGIC_SPELLS[49][2]);
				c.gfx100(MagicData.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(MagicData.MAGIC_SPELLS[49][7] * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : 125), 6,
						true);
				refreshSkill(6);
			}
			break;

		case "high":
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				for (int[] items : EquipmentSet.IRON_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch iron man amour.");
						break;
					}
				}
				for (int[] items : EquipmentSet.ULTIMATE_IRON_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch ultimate iron man amour.");
						break;
					}
				}
				for (int[] items : EquipmentSet.HC_MAN_ARMOUR.getEquipment()) {
					if (Misc.linearSearch(items, itemId) > -1) {
						c.sendMessage("You cannot alch hardcore iron man amour.");
						break;
					}
				}
				if (itemId == LootingBag.LOOTING_BAG || itemId == LootingBag.LOOTING_BAG_OPEN || itemId == RunePouch.RUNE_POUCH_ID) {
					c.sendMessage("This kind of sorcery cannot happen.");
					break;
				}
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (!c.getItems().playerHasItem(itemId, 1) || itemId == 995) {
					break;
				}
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(995, (int) (ShopAssistant.getItemShopValue(itemId) * .75));
				c.startAnimation(MagicData.MAGIC_SPELLS[50][2]);
				c.gfx100(MagicData.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				c.droppedItem = -1;
				c.droppingItem = false;
				sendFrame106(6);
				addSkillXP(MagicData.MAGIC_SPELLS[50][7] * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : 125), 6,
						true);
				refreshSkill(6);
			}
			break;
		}
	}

	public void magicOnItems(int slot, int itemId, int spellId) {

		NonCombatSpells.attemptDate(c, spellId);

		switch (spellId) {

		case 1173:
			NonCombatSpells.superHeatItem(c, itemId);
			break;

		case 1162: // low alch
			alchemy(itemId, "low");
			break;

		case 1178: // high alch
			c.droppedItem = itemId;
			c.droppingItem = false;
			if (c.showDropWarning()) {
				c.getPA().destroyInterface("alch");
				return;
			}
			alchemy(itemId, "high");
			break;

		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			Enchantment.getSingleton().enchantItem(c, itemId, spellId);
			enchantBolt(spellId, itemId, 28);
			break;
		}
	}

	private final int[][] boltData = { { 1155, 879, 9, 9236 }, { 1155, 9337, 17, 9240 }, { 1165, 9335, 19, 9237 },
			{ 1165, 880, 29, 9238 }, { 1165, 9338, 37, 9241 }, { 1176, 9336, 39, 9239 }, { 1176, 9339, 59, 9242 },
			{ 1180, 9340, 67, 9243 }, { 1187, 9341, 78, 9244 }, { 6003, 9342, 97, 9245 }, { 6003, 9341, 78, 9244 } };

	public int[][] runeData = { { 1155, 555, 1, -1, -1 }, { 1165, 556, 3, -1, -1 }, { 1176, 554, 5, -1, -1 },
			{ 1180, 557, 10, -1, -1 }, { 1187, 555, 15, 557, 15 }, { 6003, 554, 20, 557, 20 } };

	private void enchantBolt(int spell, int bolt, int amount) {

		for (int[] aBoltData : boltData) {
			if (spell == aBoltData[0]) {
				if (bolt == aBoltData[1]) {
					switch (spell) {
					case 1155:
						if (!c.getCombat().checkMagicReqs(56)) {
							return;
						}
						break;
					case 1165:
						if (!c.getCombat().checkMagicReqs(57)) {
							return;
						}
						break;
					case 1176:
						if (!c.getCombat().checkMagicReqs(58)) {
							return;
						}
						break;
					case 1180:
						if (!c.getCombat().checkMagicReqs(59)) {
							return;
						}
						break;
					case 1187:
						if (!c.getCombat().checkMagicReqs(60)) {
							return;
						}
						break;
					case 6003:
						if (!c.getCombat().checkMagicReqs(61)) {
							return;
						}
						break;
					}
					if (!c.getItems().playerHasItem(aBoltData[1], amount))
						amount = c.getItems().getItemAmount(bolt);
					c.getItems().deleteItem(aBoltData[1], c.getItems().getItemSlot(aBoltData[1]), amount);
					c.getPA().addSkillXP(aBoltData[2] * amount, 6, true);
					c.getItems().addItem(aBoltData[3], amount);
					c.getPA().sendFrame106(6);
					return;
				}
			}
		}
	}

	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}

	public void applyDead() {
		if (c.insurance == false && c.wildLevel < 20) {
			c.sendMessage("@red@Be careful! if you go over 20 wilderness you will lose your pet for no insurance!");
		}
		if (c.hasFollower == true && c.insurance == false && c.wildLevel > 20) {
            c.summonId = -1;
            c.hasFollower = false;
			c.sendMessage("@red@You have lost your pet due to wilderness level and having no pet insurance!");
		}
		if (c.hasFollower == true && c.insurance == true && c.wildLevel > 20) {
			c.sendMessage("@red@You have saved your pet due to having pet insurance!");
			c.insurance = false;
		}
		c.getPA().sendFrame126(":quicks:off", -1);
		c.isFullHelm = Item.isFullHat(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
		c.getPA().requestUpdates();
		c.respawnTimer = 15;
		c.isDead = false;
		c.graceSum = 0;
		c.freezeTimer = 1;
		c.recoilHits = 0;
		c.totalHunllefDamage = 0;
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			duelSession = null;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			resetDeath();
		}
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
		if (c.getMode().isHCIronman()) {
			if (Boundary.isIn(c, Boundary.DUEL_ARENA) || Boundary.isIn(c, Boundary.FIGHT_CAVE)
					|| c.inClanWarsSafe() || Boundary.isIn(c, Boundary.INFERNO) 
					|| Boundary.isIn(c, Boundary.OUTLAST_AREA)
					|| Boundary.isIn(c, Boundary.PEST_CONTROL_AREA)
					|| Boundary.isIn(c, Boundary.RAIDS)
					|| Boundary.isIn(c, Boundary.OLM)
					|| Boundary.isIn(c, Boundary.RAID_MAIN)
					|| Boundary.isIn(c, Boundary.XERIC)) { // TODO: Other areas.
				return;
			}
			if (c.totalLevel > 500) {
				PlayerHandler.executeGlobalMessage("@red@News: <img=25>@blu@" + Misc.capitalize(c.getName())
				+ " @pur@has just died in hardcore ironman mode, with a skill total of " + c.totalLevel
				+ "!");
			}
			c.getRights().remove(Right.HC_IRONMAN);
			c.setMode(Mode.forType(ModeType.IRON_MAN));
			c.getRights().setPrimary(Right.IRONMAN);
			c.sendMessage("You are now a normal Ironman.");
			PlayerSave.save(c);
			/*
			 * c.outStream.createFrame(109); CycleEventHandler.getSingleton().stopEvents(c);
			 * c.properLogout = true; c.disconnected = true; c.logoutDelay = 10;
			 * ConnectedFrom.addConnectedFrom(c, c.connectedFrom);
			 */
		}
		
		if (Objects.isNull(duelSession)) {
			Entity killer = c.calculateKiller();
			if (killer != null) {
				c.setKiller(killer);
				if (killer instanceof Player) {
					Player playerKiller = (Player) killer;
					c.killerId = killer.getIndex();
					if (c.killerId != c.getIndex()) {
						if (c.inWild()) {
							if (!Boundary.isIn(c, Boundary.SAFEPK)) {
								if (c.getItems().playerHasItem(13226)) {
									c.getHerbSack().onDeath(c, "PVP");
								}
								if (c.getItems().playerHasItem(12020)) {
									c.getGemBag().onDeath(c, "PVP");
								}
							}
							if (c.getLootingBag().hasLootingBagInInventory()) {
								c.getLootingBag().onDeath(c, "PVP");
							}
							if (playerKiller.inClanWars()) {
								playerKiller.getItems()
										.addSpecialBar(playerKiller.playerEquipment[playerKiller.playerWeapon]);
								playerKiller.specAmount = 10;
							}
							if (c.playerEquipment[c.playerWeapon] == 22550 || c.getItems().playerHasItem(22550)) { //craws bow
								int weaponQuantity = c.getItems().getItemAmount(22550);
								c.getItems().wearItem(-1, 1, 3);
								c.getItems().addItem(22547, 1);
								c.getItems().deleteItem(22550, 1 * weaponQuantity);
								Server.itemHandler.createGroundItem(playerKiller, 21820, playerKiller.absX, playerKiller.absY, playerKiller.heightLevel, playerKiller.crawsbowCharge, playerKiller.getIndex());
								c.decreaseCrawsBowCharge(c.crawsbowCharge);
								PlayerLogging.write(PlayerLogging.LogType.DEATH, c, String.format("Dropped %s", new GameItem(21820, c.crawsbowCharge)));
							}
							if (c.playerEquipment[c.playerWeapon] == 22545 || c.getItems().playerHasItem(22545)) { //vig chainmace
								int weaponQuantity = c.getItems().getItemAmount(22545);
								c.getItems().wearItem(-1, 1, 3);
								c.getItems().addItem(22542, 1);
								c.getItems().deleteItem(22545, 1 * weaponQuantity);
								Server.itemHandler.createGroundItem(playerKiller, 21820, playerKiller.absX, playerKiller.absY, playerKiller.heightLevel, playerKiller.viggoraCharge, playerKiller.getIndex());
								c.decreaseViggoraCharge(c.viggoraCharge);
								PlayerLogging.write(PlayerLogging.LogType.DEATH, c, String.format("Dropped %s", new GameItem(21820, c.viggoraCharge)));
							}
							if (c.playerEquipment[c.playerWeapon] == 22555 || c.getItems().playerHasItem(22555)) { //thammaron
								int weaponQuantity = c.getItems().getItemAmount(22555);
								c.getItems().wearItem(-1, 1, 3);
								c.getItems().addItem(22552, 1);
								c.getItems().deleteItem(22555, 1 * weaponQuantity);
								Server.itemHandler.createGroundItem(playerKiller, 21820, playerKiller.absX, playerKiller.absY, playerKiller.heightLevel, playerKiller.thammaronCharge, playerKiller.getIndex());
								c.decreaseThammaronCharge(c.thammaronCharge);
								PlayerLogging.write(PlayerLogging.LogType.DEATH, c, String.format("Dropped %s", new GameItem(21820, c.thammaronCharge)));
							}
							if (c.playerEquipment[c.playerHands] == 21817 || c.getItems().playerHasItem(21817)) { //bracelet
								int weaponQuantity = c.getItems().getItemAmount(21816);
								c.getItems().wearItem(-1, 1, 3);
								c.getItems().addItem(21816, 1);
								c.getItems().deleteItem(21817, 1 * weaponQuantity);
								Server.itemHandler.createGroundItem(playerKiller, 21820, playerKiller.absX, playerKiller.absY, playerKiller.heightLevel, playerKiller.ether, playerKiller.getIndex());
								c.braceletDecrease(c.ether);
								PlayerLogging.write(PlayerLogging.LogType.DEATH, c, String.format("Dropped %s", new GameItem(21820, c.ether)));
							}
							if (!playerKiller.getPlayerKills().killedRecently(c.connectedFrom)
									&& !playerKiller.getMacAddress().equals(c.getMacAddress())) {
								playerKiller.getPlayerKills().add(c.connectedFrom);
								c.deathcount++;
								playerKiller.killcount++;
								playerKiller.refreshQuestTab(0);
								playerKiller.refreshQuestTab(7);
								playerKiller.getPA().sendFrame126("@or1@Hunter KS: @gre@"
										+ playerKiller.getKillstreak().getAmount(Killstreak.Type.HUNTER) + "@or1@, "
										+ "Rogue KS: @gre@"
										+ playerKiller.getKillstreak().getAmount(Killstreak.Type.ROGUE), 29165);

								/*
								 * Killing targets
								 */
								if (Config.BOUNTY_HUNTER_ACTIVE) {
									c.getBH().dropPlayerEmblem(playerKiller);
									if (c.getBH().hasTarget()
											&& c.getBH().getTarget().getName().equalsIgnoreCase(playerKiller.playerName)
											&& playerKiller.getBH().hasTarget() && playerKiller.getBH().getTarget()
													.getName().equalsIgnoreCase(c.playerName)) {
										playerKiller.getBH().setCurrentHunterKills(
												playerKiller.getBH().getCurrentHunterKills() + 1);
										if (playerKiller.getBH().getCurrentHunterKills() > playerKiller.getBH()
												.getRecordHunterKills()) {
											playerKiller.getBH()
													.setRecordHunterKills(playerKiller.getBH().getCurrentHunterKills());
										}
										playerKiller.getKillstreak().increase(Killstreak.Type.HUNTER);
										playerKiller.getBH().upgradePlayerEmblem();
										playerKiller.getBH()
												.setTotalHunterKills(playerKiller.getBH().getTotalHunterKills() + 1);
										playerKiller.getBH().removeTarget();
										c.getBH().removeTarget();
										playerKiller.getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
										playerKiller.sendMessage(
												"<col=255>You have killed your target: " + c.playerName + ".");
									} else {
										playerKiller.getKillstreak().increase(Killstreak.Type.ROGUE);
										playerKiller.getBH()
												.setCurrentRogueKills(playerKiller.getBH().getCurrentRogueKills() + 1);
										playerKiller.getBH()
												.setTotalRogueKills(playerKiller.getBH().getTotalRogueKills() + 1);
										if (playerKiller.getBH().getCurrentRogueKills() > playerKiller.getBH()
												.getRecordRogueKills()) {
											playerKiller.getBH()
													.setRecordRogueKills(playerKiller.getBH().getCurrentRogueKills());
										}
									}
									playerKiller.getBH().updateStatisticsUI();
									playerKiller.getBH().updateTargetUI();
								}

								int personDieingKillstreak = c.getKillstreak().getAmount(Killstreak.Type.ROGUE);
								if (Boundary.isIn(c, Boundary.WILDERNESS_PARAMETERS)) {
									if (personDieingKillstreak > 10) {
										playerKiller.getItems().addItemUnderAnyCircumstance(2996, 30);
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ 10 pkp @bla@for ending a kill streak.");

									}
									int random = 30;
									playerKiller.getEventCalendar().progress(EventChallenge.GET_X_KILLS_IN_WILDY);
									playerKiller.pkp += random;
									playerKiller.refreshQuestTab(0);
									if(c.amDonated >= 10 && c.amDonated <= 49) { //regular donator
										int bonuspkp = 10;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");
										
									} else if(playerKiller.amDonated >= 50 && playerKiller.amDonated <= 99) { //super donator
										int bonuspkp = 20;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");
										
									} else if(playerKiller.amDonated >= 100 && playerKiller.amDonated <= 199) { //extreme donator
										int bonuspkp = 20;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");
										
									} else if(playerKiller.amDonated >= 200 && playerKiller.amDonated <= 299) { //ultra donator
										int bonuspkp = 30;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");
										
									} else if(playerKiller.amDonated >= 300 && playerKiller.amDonated <= 499) { //legendary donator
										int bonuspkp = 30;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");
										
									} else if(playerKiller.amDonated >= 500 && playerKiller.amDonated <= 999) { //diamond club
										int bonuspkp = 40;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");										
										
									} else if(playerKiller.amDonated >= 1000 && playerKiller.amDonated <= 2500) { //onyx club
										int bonuspkp = 50;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");										
										
									} else if(playerKiller.amDonated >= 1000 && playerKiller.amDonated <= 2500) { //platinum
										int bonuspkp = 60;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");										
										
									} else if(playerKiller.amDonated >= 2500 && playerKiller.amDonated <= 5000) { //divine
										int bonuspkp = 80;
										playerKiller.pkp+=bonuspkp;
										playerKiller.sendMessage("[@red@PKP@bla@] You are rewarded an extra@red@ " + bonuspkp + " pkp @bla@for your donator rank.");										
									}
								
								c.getKillstreak().resetAll();
								c.getPA().loadQuests();
								playerKiller.getPA().loadQuests();
								}
							} else {
									playerKiller.sendMessage(
											"You do not get any PK Points as you have recently defeated @blu@"
													+ Misc.optimizeText(c.playerName) + "@bla@.");
							}
						}
						PlayerSave.saveGame(playerKiller);
					}
				} else if (killer instanceof NPC) {
					if (!Boundary.isIn(c, Boundary.FIGHT_CAVE) && !Boundary.isIn(c, Zulrah.BOUNDARY)
							&& !Boundary.isIn(c, Boundary.KRAKEN_CAVE) && !Boundary.isIn(c, Boundary.RFD)
							&& !Boundary.isIn(c, Boundary.LIGHTHOUSE) && !Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)
							&& !Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM) && !Boundary.isIn(c, Boundary.RAIDROOMS)) {
						if (c.getLootingBag().hasLootingBagInInventory()) {
							c.getLootingBag().onDeath(c, "MOB");
						}

						if (c.getItems().playerHasItem(13226)) {
							c.getHerbSack().onDeath(c, "MOB");
						}
						if (c.getItems().playerHasItem(12020)) {
							c.getGemBag().onDeath(c, "MOB");
						}
					}
				}
				c.totalDamageDealt = 0;
				c.sendMessage("Oh dear you are dead!");
			}
		}

		/*
		 * Reset bounty hunter statistics
		 */
		if (Config.BOUNTY_HUNTER_ACTIVE) {
			c.getBH().setCurrentHunterKills(0);
			c.getBH().setCurrentRogueKills(0);
			c.getBH().updateStatisticsUI();
			c.getBH().updateTargetUI();
		}
		c.faceUpdate(0);
		c.stopMovement();

		/*
		 * Death within the duel arena
		 */
		if (duelSession != null && duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
			if (!duelSession.getWinner().isPresent()) {
				c.sendMessage("You have lost the duel!");

				c.setDuelLossCounter(c.getDuelLossCounter() + 1);
				c.sendMessage("You have now lost a total of @blu@" + c.getDuelLossCounter() + " @bla@ duels.");
				Player opponent = duelSession.getOther(c);
				opponent.logoutDelay = System.currentTimeMillis();
				if (!duelSession.getWinner().isPresent()) {
					duelSession.setWinner(opponent);
				}
				PlayerSave.saveGame(opponent);
			} else {
				c.sendMessage("Congratulations, you have won the duel.");
			}
			c.logoutDelay = System.currentTimeMillis();
		}
		c.startAnimation(2304);
		resetDeath();
	}

	private void resetDeath() {
		c.playerStandIndex = 808;
		c.playerWalkIndex = 819;
		c.playerRunIndex = 824;
		PlayerSave.saveGame(c);
		c.getPA().requestUpdates();
		removeAllWindows();
		closeAllWindows();
		resetFollowers();
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		c.specAmount = 10;
		c.attackTimer = 10;
		c.respawnTimer = 15;
		c.lastVeng = 0;
		c.recoilHits = 0;
		c.graceSum = 0;
		c.freezeTimer = 1;
		c.vengOn = false;
		c.isDead = false;
		c.tradeResetNeeded = true;
		c.doubleHit = false;
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	private void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.getIndex()) {
					Player c = PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}
	public void sendSpecialAttack(int amount, int specialEnabled) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(204);
			c.getOutStream().writeByte(amount);
			c.getOutStream().writeByte(specialEnabled);
			c.flushOutStream();
		}
	}
	/**
	 * Handles what happens after a player death
	 */
	public void giveLife() {
		// Set the visual masks of a player
		c.setInstance(null);
		c.isFullHelm = Item.isFullHat(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
		c.isDead = false;
		c.totalHunllefDamage = 0;
		c.faceUpdate(-1);
		c.freezeTimer = 1;
		c.setTektonDamageCounter(0);
		if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
			c.setGlodDamageCounter(79);
			c.setIceQueenDamageCounter(79); 
			}
		c.refreshQuestTab(7);

		// If a player is in any of these areas, their items will not be dropped to
		// themselves nor others
		if (!c.inDuelArena() && !Boundary.isIn(c, Boundary.DUEL_ARENA)&& !Vorkath.inVorkath(c)

				&& !Boundary.isIn(c, Boundary.FIGHT_CAVE) && !Boundary.isIn(c, Boundary.LIGHTHOUSE)
				&& !Boundary.isIn(c, PestControl.GAME_BOUNDARY) && !c.inSafemode()
				&& !Boundary.isIn(c, Boundary.INFERNO) && !Boundary.isIn(c, Zulrah.BOUNDARY)
				&& !Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS) && !Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)
				&& !Boundary.isIn(c, Boundary.RFD) && !Boundary.isIn(c, Boundary.OLM) && (c.inWild())
				&& !Boundary.isIn(c, Boundary.RAIDS) && Lowpkarena.getState(c) == null && !Boundary.isIn(c, Boundary.HYDRA_BOSS_ROOM)
				&& Highpkarena.getState(c) == null && !TourneyManager.getSingleton().isInArena(c)) {

			// If any items are on this list, delete them
			for (int itemId : Config.DROP_AND_DELETE_ON_DEATH) {
				if (c.getItems().isWearingItem(itemId)) {
					int slot = c.getItems().getItemSlot(itemId);
					if (slot != -1) {
						c.getItems().removeItem(itemId, slot);
					}
				}
				if (c.getItems().playerHasItem(itemId)) {
					c.getItems().deleteItem2(itemId, c.getItems().getItemAmount(itemId));
				}
			}

			// Get the killer
			Entity killer = c.getKiller();
 
			// Degrade, unequip venomous items
			c.getCombat().degradeVenemousItems(killer);

			// If a player is not an ultimate ironman, update the items kept on death
			if (c.getMode().isUltimateIronman() || c.getMode().isIronman() || c.getMode().isHCIronman() || c.getMode().isRegular() || c.getMode().isOsrs()) {
				c.getItems().resetKeepItems();
				c.updateItemsOnDeath();
			}
			// Handles the items kept on death
			for (int item = 0; c.wildLevel < 21 && item < Config.ITEMS_KEPT_ON_DEATH.length; item++) {
				int itemId = Config.ITEMS_KEPT_ON_DEATH[item];
				int itemAmount = c.getItems().getItemAmount(itemId) + c.getItems().getWornItemAmount(itemId);
				if (c.getItems().playerHasItem(itemId) || c.getItems().isWearingItem(itemId)) {
					if (c.getMode().isUltimateIronman()) {
						Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel, itemAmount,
								c.getIndex());
					} else {
						c.getItems().sendItemToAnyTab(itemId, 1 * itemAmount);
					}
				}
			}

			if (c.getMode().isUltimateIronman() || c.getMode().isIronman() || c.getMode().isHCIronman() || c.getMode().isRegular() || c.getMode().isOsrs()) {
				c.getItems().dropAllItems();
				c.getItems().deleteAllItems();
			}
			if (!c.isSkulled) {
				for (int i1 = 0; i1 < 3; i1++) {
					if (c.itemKeptId[i1] > 0) {
						c.getItems().addItem(c.itemKeptId[i1], 1);
					}
				}
			}
				// If protect item is active, one extra item will be kept
			if (c.prayerActive[10]) {
				if (c.itemKeptId[3] > 0) {
					c.getItems().addItem(c.itemKeptId[3], 1);
				}
			}
			c.getItems().resetKeepItems();
			
			/*
			 * } else if (c.inPits) { Server.fightPits.removePlayerFromPits(c.playerId);
			 * c.pitsStatus = 1;
			 */
		} else if (TourneyManager.getSingleton().isInArena(c)) {
			Entity tourneyKiller = c.calculateTourneyKiller();
            TourneyManager.getSingleton().handleDeath(c.playerName);
            TourneyManager.getSingleton().handleKill(tourneyKiller);
		} if (Boundary.isIn(c, Boundary.PEST_CONTROL_AREA)) {
			c.getPA().movePlayer(2657, 2639, 0);
		} 
		if (Boundary.isIn(c, PestControl.GAME_BOUNDARY)) {
			c.getPA().movePlayer(2656 + Misc.random(2), 2614 - Misc.random(3), 0);
		} else if (Boundary.isIn(c, Zulrah.BOUNDARY)) {
			c.getPA().movePlayer(2202, 3056, 0);
			InstancedArea instance = c.getZulrahEvent().getInstancedZulrah();
			if (instance != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance);
			}
//			c.getZulrahLostItems().store();
//			c.talkingNpc = 2040;
//			c.getDH().sendNpcChat("It looks like Zulrah beat you.", "I'll give you back your items for 500,000GP.",
//					"Talk to me when you're ready.");
		} else if (Boundary.isIn(c, Boundary.KRAKEN_CAVE)) {
			c.getPA().movePlayer(2280, 10016, 0);
			InstancedArea instance = c.getKraken().getInstance();
			if (instance != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance);
			}
		} if (Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)) {
			c.getPA().movePlayer(1309, 1250, 0);
//			c.getCerberusLostItems().store();
//			c.talkingNpc = 5870;
//			c.getDH().sendNpcChat("It looks like Cerberus beat you.", "I'll give you back your items for 500,000GP.",
//					"Talk to me when you're ready.");
		} else if (Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)) {
			c.getPA().movePlayer(1665, 10045, 0);
//			c.getSkotizoLostItems().store();
//			c.talkingNpc = 7283;
//			c.getDH().sendNpcChat("It looks like Skotizo beat you.", "I'll give you back your items for 500,000GP.",
//					"Talk to me when you're ready.");
		} else if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)
					&& duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
				Player opponent = duelSession.getWinner().get();
				if (opponent != null) {
					opponent.getPA().createPlayerHints(10, -1);
					duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
				}
			}
		} else if (Boundary.isIn(c, Boundary.LIGHTHOUSE) && c.getDagannothMother() != null) {
			c.getDagannothMother().end(DisposeType.INCOMPLETE);
		} else if (Boundary.isIn(c, Boundary.HYDRA_BOSS_ROOM)) {
	        c.getPA().movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
		} else if (Boundary.isIn(c, Boundary.RFD) && c.getrecipeForDisaster() != null) {
			c.getrecipeForDisaster().end(DisposeTypes.INCOMPLETE);
		} else if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.getFightCave().handleDeath();
		} else if (Boundary.isIn(c, Boundary.INFERNO) && c.getInferno() != null) {
			c.getInferno().handleDeath();
		} else if (Boundary.isIn(c, Boundary.XERIC)) {
			c.getXeric().leaveGame(c, true);
		} else if (Boundary.isIn(c, Boundary.RAIDS)) {
			Raids raidInstance = c.getRaidsInstance();
			if(raidInstance != null || !Boundary.isIn(c, Boundary.OLM)) {
				Location startRoom = raidInstance.getStartLocation();
				c.getPA().movePlayer(startRoom.getX(), startRoom.getY(), raidInstance.currentHeight);
				raidInstance.resetRoom(c);
			}
		} else if (Boundary.isIn(c, Boundary.OLM)) {
			Raids raidInstance = c.getRaidsInstance();
			if(raidInstance != null) {
				Location olmWait = raidInstance.getOlmWaitLocation();
				c.getPA().movePlayer(olmWait.getX(), olmWait.getY(), raidInstance.currentHeight);
				raidInstance.resetOlmRoom(c);
				//c.setInstance(raidInstance.getInstance());
			}
		} else if (Highpkarena.getState(c) != null) {
			Highpkarena.handleDeath(c);
		} else if (Lowpkarena.getState(c) != null) {
			Lowpkarena.handleDeath(c);
		} else if (c.inClanWars() || c.inClanWarsSafe()) {
			movePlayer(c.absX, 4759, 0);
		} else if (Boundary.isIn(c, Boundary.SAFEPKSAFE)) {
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
			removeAllWindows();
			closeAllWindows();
		} else {
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
			removeAllWindows();
			closeAllWindows();
		}
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.startAnimation(65535);
		updateLife();
	}

	/**
	 * Update and reset certain objects of an account
	 */
	private void updateLife() {
		PlayerSave.saveGame(c);
		c.resetDamageTaken();
		c.totalHunllefDamage = 0;
		c.getCombat().resetPlayerAttack();
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.getHealth().reset();
		c.getHealth().removeAllStatuses();
		c.getHealth().removeAllImmunities();
		requestUpdates();
		c.tradeResetNeeded = true;
	}

	/**
	 * Teleporting
	 * 
	 * @param homeTeleport
	 *            TODO
	 **/
	public void spellTeleport(int x, int y, int height, boolean homeTeleport) {
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return;
		}
		if (Boundary.isIn(c, Boundary.OUTLAST)) {
			return;
		}
		if (c.jailEnd > 1) {
			return;
		}
		c.getPA().startTeleport(x, y, height, c.playerMagicBook == 1 ? "ancient" : "modern", homeTeleport);
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
	}

	public void startLeverTeleport(int x, int y, int height) {
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.OUTLAST)) {
			return;
		}
		if (c.jailEnd > 1) {
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("The cold from the ice-path is preventing you from teleporting.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDS)) {
			c.getPotions().resetOverload();
		}
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerAttackingIndex > 0 || c.npcAttackingIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();
			Inferno.reset(c);
			c.waveType = 0;
			c.waveId = 0;
			c.waveInfo = new int[3];
			c.teleX = x;
			c.teleY = y;
			c.npcAttackingIndex = 0;
			c.playerAttackingIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(2140);
			c.teleTimer = 8;
			c.setTektonDamageCounter(0);
			if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
				c.setGlodDamageCounter(79);
				c.setIceQueenDamageCounter(79); 
			}
			c.sendMessage("You pull the lever..");
		}
		c.getSkilling().stop();
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
		}
		if (c.getSkotizo() != null) {
			InstancedAreaManager.getSingleton().disposeOf(c.getSkotizo());
		}
	}

	public boolean morphPermissions() {
		if (c.morphed) {
			return false;
		}
		if (c.inWild()) {
			return false;
		}
		if (System.currentTimeMillis() - c.lastSpear < 3000) {
			c.sendMessage("You cannot do this now.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				c.sendMessage("You cannot do this here.");
				return false;
			}
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return false;
		}
		if (Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
			c.sendMessage("You cannot do this here.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.RFD)) {
			c.sendMessage("You cannot do this here.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot do this here.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("You cannot do this here.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You cannot do this now.");
			return false;
		}
		return true;
	}

	public boolean canTeleport(String type) {
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return false;
		}
		boolean inLobby = LobbyType.stream().anyMatch(lobbyType -> {
			Optional<Lobby> lobbyOpt = LobbyManager.get(lobbyType);
			if(lobbyOpt.isPresent()) {
				return lobbyOpt.get().inLobby(c);
			}
			return false;
		});
		if(inLobby) {
			c.sendMessage("You cannot teleport from here, please exit the way you entered!");
			return false;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				c.sendMessage("You cannot teleport whilst in a duel.");
				return false;
			}
		}
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return false;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return false;
		}
		if (Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
			c.sendMessage("You cannot teleport from here, use the ladder.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.RFD)) {
			c.sendMessage("You cannot teleport from here, use the portal.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot teleport out of fight caves.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("The cold from the ice-path is preventing you from teleporting.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You must finish what you're doing before you can teleport.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.RAIDROOMS)) {
			if(c.getRaidsInstance() != null) {
				c.sendMessage("You cannot teleport out of the raids. Please use the stairs!");
				return false;
			}
		}
		if (c.isInJail() && !(c.getRights().isOrInherits(Right.MODERATOR))) {
			c.sendMessage("You cannot teleport out of jail.");
			return false;
		}
		if (c.inWild()) {
			if (!type.equals("glory") || !type.equals("pod")) {
				if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
					c.sendMessage(
							"You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					c.getPA().closeAllWindows();
					return false;
				}
			} else {
				if (c.wildLevel > 30) {
					c.sendMessage("You can't teleport above level 30 in the wilderness.");
					c.getPA().closeAllWindows();
					return false;
				}
			}
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		return true;
	}
	
	public void setScrollPosition(int scrollPosition, int frame) {
		c.getPA().sendFrame126(":scp: " + scrollPosition, frame);
	}
	
	public void getScrollPosition(int frame) {
		c.getPA().sendFrame126(":scpfind:", frame);
	}
	
	boolean flag = false;
	public void resetScrollPosition(int frame) {
		if (flag)
			c.getPA().sendFrame126(":scp: 0", frame);
		else
			c.getPA().sendFrame126(":scp: 00", frame);
		
		flag = !flag;
	}

	public void startTeleport(int x, int y, int height, String teleportType, boolean homeTeleport) {
		c.isWc = false;

		if (TourneyManager.getSingleton().isInArenaBounds(c) || TourneyManager.getSingleton().isInLobbyBounds(c)) {
			c.sendMessage("You cannot teleport when in the tournament arena.");
			return;
		}
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
			c.stopPlayerSkill = false;
		}
		if (c.isForceMovementActive()) {
			return;
		}
		resetVariables();
		SkillHandler.isSkilling[12] = false;
		
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You must finish what you're doing first.");
			return;
		}
		if (Boundary.isIn(c, Boundary.HUNLLEF_BOSS_ROOM)  && c.hunllefDead == false) {
			c.sendMessage("The Hunllef's powers disable your teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
			c.sendMessage("You cannot teleport from here, use the ladder.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDROOMS)) {
			if(c.getRaidsInstance() != null) {
				c.sendMessage("Please use the stairs or type @red@::leaveraids!");
				return ;
			}
		}
		if (Boundary.isIn(c, Boundary.RAIDS)) {
			c.getPotions().resetOverload();
		}
		if (Boundary.isIn(c, Boundary.RFD)) {
			c.sendMessage("You cannot teleport from here, use the portal.");
			return;
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot teleport out of fight caves.");
			return;
		}
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("The cold from the ice-path is preventing you from teleporting.");
			return;
		}
		if (c.isInJail() && !(c.getRights().isOrInherits(Right.MODERATOR))) {
			c.sendMessage("You cannot teleport out of jail.");
			return;
		}
		if (c.isDead) {
			return;
		}
		if (c.inWild() && !(c.getRights().isOrInherits(Right.MODERATOR))) {
			if (!teleportType.equals("glory") && !teleportType.equals("obelisk") && !teleportType.equals("pod")) {
				if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
					c.sendMessage(
							"You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					c.getPA().closeAllWindows();
					return;
				}
			} else if (!teleportType.equals("obelisk")) {
				if (c.wildLevel > 30) {
					c.sendMessage("You can't teleport above level 30 in the wilderness.");
					c.getPA().closeAllWindows();
					return;
				}
			}
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerAttackingIndex > 0 || c.npcAttackingIndex > 0) {
				c.getCombat().resetPlayerAttack();
			}
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcAttackingIndex = 0;
			c.playerAttackingIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.setTektonDamageCounter(0);
			if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
				c.setGlodDamageCounter(79);
				c.setIceQueenDamageCounter(79); 
				}
			if (teleportType.equalsIgnoreCase("modern") || teleportType.equals("glory")) {
				c.startAnimation(714);
				c.teleTimer = 11;
				c.teleGfx = 308;
				c.teleEndAnimation = 715;
			} else if (teleportType.equalsIgnoreCase("ancient")) {
				c.startAnimation(1979);
				c.teleGfx = 0;
				c.teleTimer = 9;
				c.teleEndAnimation = 0;
				c.gfx0(392);
			} else if (teleportType.equalsIgnoreCase("pod")) {
				c.startAnimation(4544);
				c.teleEndAnimation = 65535;
				c.teleGfx = 0;
				c.teleTimer = 9;
				c.gfx0(767);
				
			} else if (teleportType.equals("obelisk")) {
				c.startAnimation(1816);
				c.teleTimer = 11;
				c.teleGfx = 661;
				c.teleEndAnimation = 65535;
			} else if (teleportType.equals("puropuro")) {
				c.startAnimation(6724);
				c.gfx0(1118);
				c.teleTimer = 13;
				c.teleEndAnimation = 65535;
			}
			if (!homeTeleport) {
				c.lastTeleportX = x;
				c.lastTeleportY = y;
				c.lastTeleportZ = height;
			}
			c.getSkilling().stop();
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.getSkotizo() != null) {
				InstancedAreaManager.getSingleton().disposeOf(c.getSkotizo());
			}
		}
	}

	public void teleTabTeleport(int x, int y, int height, String teleportType) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You cannot teleport until you finish what you're doing.");
			return;
		}
		boolean inLobby = LobbyType.stream().anyMatch(lobbyType -> {
			Optional<Lobby> lobbyOpt = LobbyManager.get(lobbyType);
			if(lobbyOpt.isPresent()) {
				return lobbyOpt.get().inLobby(c);
			}
			return false;
		});
		if(inLobby) {
			c.sendMessage("You cannot teleport from here, please exit the way you entered!");
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDS)) {
			c.getPotions().resetOverload();
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDROOMS)) {
			if(c.getRaidsInstance() != null) {
				c.sendMessage("You cannot teleport out of the raids. Please use the stairs!");
				return ;
			}
		}
		if (Boundary.isIn(c, Boundary.HUNLLEF_BOSS_ROOM) && c.hunllefDead == false) {
			c.sendMessage("The Hunllef's powers disable your teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
			c.sendMessage("You cannot teleport from here, use the ladder.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RFD)) {
			c.sendMessage("You cannot teleport from here, use the portal.");
			return;
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot teleport out of fight caves.");
			return;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("The cold from the ice-path is preventing you from teleporting.");
			return;
		}
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return;
		}
		if (c.isDead) {
			return;
		}
		if (c.getSlayer().superiorSpawned) {
			c.getSlayer().superiorSpawned = false;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerAttackingIndex > 0 || c.npcAttackingIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcAttackingIndex = 0;
			c.playerAttackingIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.setTektonDamageCounter(0);
			if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
				c.setGlodDamageCounter(79);
				c.setIceQueenDamageCounter(79); 
				}
			c.getSkilling().stop();
			if (teleportType.equalsIgnoreCase("teleTab")) {
				c.startAnimation(4731);
				c.teleEndAnimation = 0;
				c.teleTimer = 8;
				c.gfx0(678);
			}
		}
		if (c.getSkotizo() != null) {
			InstancedAreaManager.getSingleton().disposeOf(c.getSkotizo());
		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You cannot teleport until you finish what you're doing.");
			return;
		}
		boolean inLobby = LobbyType.stream().anyMatch(lobbyType -> {
			Optional<Lobby> lobbyOpt = LobbyManager.get(lobbyType);
			if(lobbyOpt.isPresent()) {
				return lobbyOpt.get().inLobby(c);
			}
			return false;
		});
		if(inLobby) {
			c.sendMessage("You cannot teleport from here, please exit the way you entered!");
			return;
		}
		if (c.inClanWars() || c.inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDS)) {
			c.getPotions().resetOverload();
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (Boundary.isIn(c, Boundary.HUNLLEF_BOSS_ROOM) && c.hunllefDead == false) {
			c.sendMessage("The Hunllef's powers disable your teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.LIGHTHOUSE)) {
			c.sendMessage("You cannot teleport from here, use the ladder.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RFD)) {
			c.sendMessage("You cannot teleport from here, use the portal.");
			return;
		}
		if (Boundary.isIn(c, Boundary.RAIDROOMS)) {
			if(c.getRaidsInstance() != null) {
				c.sendMessage("You cannot teleport out of the raids. Please use the stairs!");
				return ;
			}
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot teleport out of fight caves.");
			return;
		}
		if (Boundary.isIn(c, Boundary.ICE_PATH) || Boundary.isIn(c, Boundary.ICE_PATH_TOP) && c.heightLevel > 0) {
			c.sendMessage("The cold from the ice-path is preventing you from teleporting.");
			return;
		}
		if (Lowpkarena.getState(c) != null || Highpkarena.getState(c) != null) {
			c.sendMessage("You can't teleport from a Pk event!");
			return;
		}
		if (c.isDead) {
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcAttackingIndex = 0;
			c.playerAttackingIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(714);
			c.teleTimer = 9;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;
			c.setTektonDamageCounter(0);
			if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
				c.setGlodDamageCounter(79);
				c.setIceQueenDamageCounter(79); 
				}
			c.getSkilling().stop();
		}
		if (c.getSkotizo() != null) {
			InstancedAreaManager.getSingleton().disposeOf(c.getSkotizo());
		}
	}

	public void processTeleport() {
		if (c.isDead) {
			return;
		}
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
		if (c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
		}
	}

	public void followNpc() {
		if (NPCHandler.npcs[c.followId2] == null || NPCHandler.npcs[c.followId2].isDead) {
			c.followId2 = 0;
			return;
		}
		if (c.morphed) {
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		NPC npc = NPCHandler.npcs[c.followId2];
		int otherX = NPCHandler.npcs[c.followId2].getX();
		int otherY = NPCHandler.npcs[c.followId2].getY();
		double distanceToNpc = npc.getDistance(absX, absY);
		boolean withinDistance = distanceToNpc <= 2;
		
		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId2 = 0;
			return;
		}

		c.faceUpdate(c.followId2);
		if (distanceToNpc <= 1) {
			if (!npc.insideOf(c.absX, c.absY)) {
				if (npc.getSize() == 0) {
					stopDiagonal(otherX, otherY);
				}
				return;
			}
		}

		boolean projectile = c.usingOtherRangeWeapons || c.usingBow || c.usingMagic || c.autocasting
				|| c.getCombat().usingCrystalBow() || c.playerEquipment[c.playerWeapon] == 22550;
		if (!projectile
				|| projectile && (PathChecker.isProjectilePathClear(c, npc, c.absX, c.absY, c.heightLevel, otherX, otherY)
						|| PathChecker.isProjectilePathClear(c, npc, otherX, otherY, c.heightLevel, c.absX, c.absY))) {
			if (c.checkNpcAttackDistance(npc)) {
				return;
			}
		}

		final int x = c.absX;
		final int y = c.absY;
		final int z = c.heightLevel;
		final boolean inWater = npc.npcType == 2042 || npc.npcType == 2043 || npc.npcType == 2044;

		if (npc.getFollowPosition() == null) {
			if (!inWater) {
				if (npc.getSize() == 1) {
					Pair<Integer, Integer> tile = getFollowPosition(otherX, otherY);
					if (tile.getLeft() != 0 && tile.getRight() != 0) {
						playerWalk(tile.getLeft(), tile.getRight());
					} else {
						playerWalk(otherX, otherY);
					}
				} else {
					double lowDist = 99999;
					int lowX = 0;
					int lowY = 0;
					int x3 = otherX;
					int y3 = otherY - 1;
					final int loop = npc.getSize();

					for (int k = 0; k < 4; k++) {
						for (int i = 0; i < loop - (k == 0 ? 1 : 0); i++) {
							if (k == 0) {
								x3++;
							} else if (k == 1) {
								if (i == 0) {
									x3++;
								}
								y3++;
							} else if (k == 2) {
								if (i == 0) {
									y3++;
								}
								x3--;
							} else if (k == 3) {
								if (i == 0) {
									x3--;
								}
								y3--;
							}

							if (Misc.distance(x3, y3, x, y) < lowDist) {
								boolean allowsInteraction = !projectile/* && PathChecker.isMeleePathClear(c, x3, y3, z, otherX, otherY) */ // Melee path check isn't used in combat
										|| projectile && PathChecker.isProjectilePathClear(c, npc, x3, y3, z, otherX, otherY);
								boolean accessible = PathFinder.getPathFinder().accessable(c, x3, y3);

								if (allowsInteraction && accessible) {
									lowDist = Misc.distance(x3, y3, x, y);
									lowX = x3;
									lowY = y3;
								}
							}
						}
					}


					if (lowX != 0 && lowY != 0) {
						PathFinder.getPathFinder().findRoute(c, lowX, lowY, true, 18, 18);
						if (c.debugMessage) {
							c.sendMessage("Path found to : " + lowX + ", " + lowY);
						}
					} else {
						PathFinder.getPathFinder().findRoute(c, npc.absX, npc.absY, true, 18, 18);
						if (c.debugMessage) {
							c.sendMessage("No path found, reverting to npc position.");
						}
					}
				}
			} else {

				if (otherX == c.absX && otherY == c.absY) {
					int r = Misc.random(3);
					switch (r) {
						case 0:
							playerWalk(0, -1);
							break;
						case 1:
							playerWalk(0, 1);
							break;
						case 2:
							playerWalk(1, 0);
							break;
						case 3:
							playerWalk(-1, 0);
							break;
					}
				} else if (c.isRunning2 && !withinDistance) {
					if (otherY > c.getY() && otherX == c.getX()) {
						// walkTo(0, getMove(c.getY(), otherY - 1) + getMove(c.getY(),
						// otherY - 1));
						playerWalk(otherX, otherY - 1);
					} else if (otherY < c.getY() && otherX == c.getX()) {
						// walkTo(0, getMove(c.getY(), otherY + 1) + getMove(c.getY(),
						// otherY + 1));
						playerWalk(otherX, otherY + 1);
					} else if (otherX > c.getX() && otherY == c.getY()) {
						// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
						// otherX - 1), 0);
						playerWalk(otherX - 1, otherY);
					} else if (otherX < c.getX() && otherY == c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
						// otherX + 1), 0);
						playerWalk(otherX + 1, otherY);
					} else if (otherX < c.getX() && otherY < c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
						// otherX + 1), getMove(c.getY(), otherY + 1) +
						// getMove(c.getY(), otherY + 1));
						playerWalk(otherX + 1, otherY + 1);
					} else if (otherX > c.getX() && otherY > c.getY()) {
						// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
						// otherX - 1), getMove(c.getY(), otherY - 1) +
						// getMove(c.getY(), otherY - 1));
						playerWalk(otherX - 1, otherY - 1);
					} else if (otherX < c.getX() && otherY > c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
						// otherX + 1), getMove(c.getY(), otherY - 1) +
						// getMove(c.getY(), otherY - 1));
						playerWalk(otherX + 1, otherY - 1);
					} else if (otherX > c.getX() && otherY < c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
						// otherX + 1), getMove(c.getY(), otherY - 1) +
						// getMove(c.getY(), otherY - 1));
						playerWalk(otherX + 1, otherY - 1);
					}
				} else {
					if (otherY > c.getY() && otherX == c.getX()) {
						// walkTo(0, getMove(c.getY(), otherY - 1));n
						playerWalk(otherX, otherY - 1);
					} else if (otherY < c.getY() && otherX == c.getX()) {
						// walkTo(0, getMove(c.getY(), otherY + 1));
						playerWalk(otherX, otherY + 1);
					} else if (otherX > c.getX() && otherY == c.getY()) {
						// walkTo(getMove(c.getX(), otherX - 1), 0);
						playerWalk(otherX - 1, otherY);
					} else if (otherX < c.getX() && otherY == c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1), 0);
						playerWalk(otherX + 1, otherY);
					} else if (otherX < c.getX() && otherY < c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
						// otherY + 1));
						playerWalk(otherX + 1, otherY + 1);
					} else if (otherX > c.getX() && otherY > c.getY()) {
						// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
						// otherY - 1));
						playerWalk(otherX - 1, otherY - 1);
					} else if (otherX < c.getX() && otherY > c.getY()) {
						// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
						// otherY - 1));
						playerWalk(otherX + 1, otherY - 1);
					} else if (otherX > c.getX() && otherY < c.getY()) {
						// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
						// otherY + 1));
						playerWalk(otherX - 1, otherY + 1);
					}
				}
			}
		} else {
			playerWalk(npc.getFollowPosition().getX(), npc.getFollowPosition().getY());
		}
	}

	/**
	 * Following
	 **/

	public void followPlayer() {
		if (PlayerHandler.players[c.followId] == null || PlayerHandler.players[c.followId].isDead) {
			c.followId = 0;
			return;
		}
		if (c.morphed) {
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (!Objects.isNull(session)) {
				if (session.getRules().contains(Rule.NO_MOVEMENT)) {
					c.followId = 0;
					return;
				}
			}
		}
		if (inPitsWait()) {
			c.followId = 0;
		}

		if (c.isDead || c.getHealth().getCurrentHealth() <= 0)
			return;
		if (System.currentTimeMillis() - c.lastSpear < 3000) {
			c.sendMessage("You are stunned, you cannot move.");
			c.followId = 0;
			return;
		}
		
		final Player other = PlayerHandler.players[c.followId];
		final int otherX = PlayerHandler.players[c.followId].getX();
		final int otherY = PlayerHandler.players[c.followId].getY();

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			return;
		}
		if (c.combatFollowing) {
			boolean projectile = c.usingOtherRangeWeapons
					|| c.usingBow
					|| c.usingMagic
					|| c.getCombat().usingCrystalBow()
					|| c.playerEquipment[c.playerWeapon] == 22550
					;
			if (!projectile
					|| projectile && (PathChecker.isProjectilePathClear(c, other, c.absX, c.absY, c.heightLevel, otherX, otherY)
							|| PathChecker.isProjectilePathClear(c, other, otherX, otherY, c.heightLevel, c.absX, c.absY))) {
			
				if (c.checkCombatDistance(c, other, true)) {
					c.stopMovement();
					return;
				}
	
			}
		
			if (otherX == c.absX && otherY == c.absY) {
				if (!other.isWalkingQueueEmpty()) {
					c.stopMovement();
					return;
				}
			}
			
			Pair<Integer, Integer> tile = getFollowPosition(otherX, otherY);
			if (tile.getLeft() != 0 && tile.getRight() != 0) {
				playerWalk(tile.getLeft(), tile.getRight());
			} else {
				playerWalk(otherX, otherY);
			}
			
		} else {
			int followX = other.lastX;
			int followY = other.lastY;
			if (followX != 0 && followY != 0 && PathFinder.getPathFinder().accessable(c, followX, followY)) {
				playerWalk(followX, followY);
			} else if (Misc.distance(c.absX, c.absY, otherX, otherY) != 1.0) {
				Pair<Integer, Integer> tile = getFollowPosition(otherX, otherY);
				if (tile.getLeft() != 0 && tile.getRight() != 0) {
					playerWalk(tile.getLeft(), tile.getRight());
				} else {
					playerWalk(otherX, otherY);
				}
			}
		}
		
		c.faceUpdate(c.followId + 32768);
	}

	public Pair<Integer, Integer> getFollowPosition(int otherX, int otherY) {
		int lowX = 0;
		int lowY = 0;
		double lowDist = 0;
		
		int[][] nondiags = { { 0, 1 }, { -1, 0 }, { 1, 0 }, { 0, -1 }, };
		for (int[] nondiag : nondiags) {
			int x2 = otherX + nondiag[0];
			int y2 = otherY + nondiag[1];
			double dist = Misc.distance(c.absX, c.absY, x2, y2);
			//System.out.println(dist);
			if (lowDist == 0 || dist < lowDist) {
				if (PathFinder.getPathFinder().accessable(c, x2, y2)) {
					lowX = x2;
					lowY = y2;
					lowDist = dist;
				}
			}
		}
		
		return Pair.of(lowX, lowY);
	}
	
	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void sendStatement(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
	}

	public void resetFollow() {
		c.combatFollowing = false;
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
		if (c.outStream != null) {
			c.outStream.createFrame(174);
			c.outStream.writeUnsignedWord(0);
			c.outStream.writeByte(0);
			c.outStream.writeUnsignedWord(1);
		}
	}

	public void walkTo3(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.absX + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
		int l = c.absY + j;
		l -= c.mapRegionY * 8;
		c.isRunning2 = false;
		c.isRunning = false;
		c.getNewWalkCmdX()[0] += k;
		c.getNewWalkCmdY()[0] += l;
		c.poimiY = l;
		c.poimiX = k;
	}

	private final int[] tmpNWCX = new int[50];
	private final int[] tmpNWCY = new int[50];

	public void walkTo(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	private void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		if (c.freezeTimer > 0) // player can't move
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 3000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759 && c.playerEquipment[c.playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return c.playerEquipment[c.playerHat] == 4724 && c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730 && c.playerEquipment[c.playerWeapon] == 4726;
	}

	public void requestUpdates() {
		// if (!c.initialized) {
		// return;
		// }
		c.setUpdateRequired(true);
		c.setAppearanceUpdateRequired(true);
	}

	/*
	 * public void Obelisks(int id) { if (!c.getItems().playerHasItem(id)) {
	 * c.getItems().addItem(id, 1); } }
	 */

	public void levelUp(int skill) {
		int totalLevel = (getLevelForXP(c.playerXP[0]) + getLevelForXP(c.playerXP[1]) + getLevelForXP(c.playerXP[2])
				+ getLevelForXP(c.playerXP[3]) + getLevelForXP(c.playerXP[4]) + getLevelForXP(c.playerXP[5])
				+ getLevelForXP(c.playerXP[6]) + getLevelForXP(c.playerXP[7]) + getLevelForXP(c.playerXP[8])
				+ getLevelForXP(c.playerXP[9]) + getLevelForXP(c.playerXP[10]) + getLevelForXP(c.playerXP[11])
				+ getLevelForXP(c.playerXP[12]) + getLevelForXP(c.playerXP[13]) + getLevelForXP(c.playerXP[14])
				+ getLevelForXP(c.playerXP[15]) + getLevelForXP(c.playerXP[16]) + getLevelForXP(c.playerXP[17])
				+ getLevelForXP(c.playerXP[18]) + getLevelForXP(c.playerXP[19]) + getLevelForXP(c.playerXP[20])
				+ getLevelForXP(c.playerXP[21]));
		sendFrame126("" + totalLevel, 3984);
		switch (skill) {
		case 0:
			sendFrame126("Congratulations, you just advanced an attack level!", 6248);
			sendFrame126("Your attack level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 1:
			sendFrame126("Congratulations, you just advanced a defence level!", 6254);
			sendFrame126("Your defence level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 2:
			sendFrame126("Congratulations, you just advanced a strength level!", 6207);
			sendFrame126("Your strength level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 3:
			c.getHealth().setMaximumHealth(c.getLevelForXP(c.playerXP[c.playerHitpoints]));
			sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
			sendFrame126("Your hitpoints level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 4:
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 5:
			sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
			sendFrame126("Your prayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 6:
			sendFrame126("Congratulations, you just advanced a magic level!", 6212);
			sendFrame126("Your magic level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);
			if (c.combatLevel >= 126) {
				c.getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
			}
			break;

		case 7:
			sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
			sendFrame126("Your cooking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);
			break;

		case 8:
			sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
			sendFrame126("Your woodcutting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);
			break;

		case 9:
			sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
			sendFrame126("Your fletching level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
			sendFrame126("Your fishing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
			sendFrame126("Your firemaking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
			sendFrame126("Your crafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
			sendFrame126("Your smithing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126("Congratulations, you just advanced a mining level!", 4417);
			sendFrame126("Your mining level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
			sendFrame126("Your herblore level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126("Congratulations, you just advanced a agility level!", 4278);
			sendFrame126("Your agility level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
			sendFrame126("Your theiving level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
			sendFrame126("Your slayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			break;

		case 19:
			c.sendMessage("Congratulations! You've just advanced a Farming level.");
			break;

		case 20:
			sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
			sendFrame126("Your runecrafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			break;

			case 21:
		case 22:
			c.sendMessage("Congratulations! You've just advanced a Hunter level.");
			break;
		}
		if (c.maxRequirements(c)) {
			PlayerHandler
					.executeGlobalMessage("@red@[Server]" + Misc.capitalize(c.playerName) + " has reached max total level!");
		}
		if (c.totalLevel >= 2000) {
			c.getEventCalendar().progress(EventChallenge.HAVE_2000_TOTAL_LEVEL);
		}
		
		if (getLevelForXP(c.playerXP[skill]) == 99) {
			Skill s = Skill.forId(skill);
			PlayerHandler.executeGlobalMessage(
					"@red@[Server]" + Misc.capitalize(c.playerName) + " has reached level 99 " + s.toString() + "!");
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
	}

	public void refreshSkill(int i) {
		c.combatLevel = c.calculateCombatLevel();
		switch (i) {
		case 0:
			sendFrame126("" + c.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendFrame126("" + c.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1) + "", 4045);
			requestUpdates();
			break;

		case 1:
			sendFrame126("" + c.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendFrame126("" + c.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			sendFrame126("" + c.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendFrame126("" + c.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			sendFrame126("" + c.getHealth().getCurrentHealth() + "", 4016);
			sendFrame126("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendFrame126("" + c.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			sendFrame126("" + c.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendFrame126("" + c.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			sendFrame126("" + c.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendFrame126("" + c.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1) + "", 4069);
			sendFrame126("" + c.playerLevel[5] + "/" + getLevelForXP(c.playerXP[5]) + "", 687);// Prayer
																								// frame
			break;

		case 6:
			sendFrame126("" + c.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendFrame126("" + c.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			sendFrame126("" + c.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendFrame126("" + c.playerXP[7] + "", 4134);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			sendFrame126("" + c.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendFrame126("" + c.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			sendFrame126("" + c.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendFrame126("" + c.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			sendFrame126("" + c.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendFrame126("" + c.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			sendFrame126("" + c.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendFrame126("" + c.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			sendFrame126("" + c.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendFrame126("" + c.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			sendFrame126("" + c.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendFrame126("" + c.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			sendFrame126("" + c.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendFrame126("" + c.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			sendFrame126("" + c.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendFrame126("" + c.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			sendFrame126("" + c.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendFrame126("" + c.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			sendFrame126("" + c.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendFrame126("" + c.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			sendFrame126("" + c.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendFrame126("" + c.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			sendFrame126("" + c.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendFrame126("" + c.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			case 21:
			case 22:
			sendFrame126("" + c.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendFrame126("" + c.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4159);
			break;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public static class XpDrop {
		private int[] skill;
		private int amount;

		public XpDrop(int amount, int...skill) {
			this.skill = skill;
			this.amount = amount;
		}
	}

	private final List<XpDrop> xpDrops = new ArrayList<>();

	public void addXpDrop(XpDrop xpDrop) {
		if (xpDrop.amount <= 0)
			return;
		if (xpDrop.skill.length > 1) {
			throw new IllegalArgumentException("Only length of 1 is allowed for xp drop skill.");
		}

		for (Iterator<XpDrop> i = xpDrops.iterator(); i.hasNext();) {
			XpDrop drop = i.next();
			if (drop.skill == xpDrop.skill) {
				i.remove();
				xpDrops.add(new XpDrop(xpDrop.amount + drop.amount, drop.skill));
				return;
			}
		}

		xpDrops.add(xpDrop);
	}

	public void sendXpDrops() {
		List<XpDrop> send = new ArrayList<>();

		main: for (Iterator<XpDrop> i = xpDrops.iterator(); i.hasNext();) {
			XpDrop drop = i.next();
			i.remove();

			if (drop.skill[0] <= 6) {
				// Combine combat skill drops

				for (XpDrop dropSend : send) {
					if (dropSend.skill[0] <= 6) {
						for (int index = 0; index < dropSend.skill.length; index++) {
							if (dropSend.skill[index] == drop.skill[0]) {
								dropSend.amount += drop.amount;
								continue main;
							}
						}

						int[] newSkills = new int[dropSend.skill.length + 1];
						System.arraycopy(dropSend.skill, 0, newSkills, 0, dropSend.skill.length);
						dropSend.skill = newSkills;
						dropSend.amount += drop.amount;
						dropSend.skill[dropSend.skill.length - 1] = drop.skill[0];
						continue main;
					}
				}

				send.add(drop);
			} else {
				send.add(drop);
			}
		}

		for (Iterator<XpDrop> i = send.iterator(); i.hasNext();) {
			XpDrop drop = i.next();
			i.remove();
			c.getPA().sendExperienceDrop(true, drop.amount, drop.skill);
		}
	}

	public boolean addSkillXP(int amount, int skill, boolean dropExperience) {
		if (amount <= 0)
			return false;
		if (c.skillLock[skill]) {
			return false;
		}
		if (TourneyManager.getSingleton().isInArena(c)) {
			return false;
		}
		if (Boundary.isIn(c, Boundary.FOUNTAIN_OF_RUNE_BOUNDARY)) {
			return false;
		}
		if (c.expLock && skill <= 6) {
			return false;
		}
		if (amount + c.playerXP[skill] < 0) {
			return false;
		}
		// Amount applied

		// If within thedonator zone, VIP accounts get bonus xp while bonus weekend is
		// off
		if (Boundary.isIn(c, Boundary.DONATOR_ZONE) && c.getRights().isOrInherits(Right.ULTRA_DONATOR)
				&& !DoubleExperience.isDoubleExperience()) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND_BOOSTED;

			// If within thedonator zone, VIP accounts get bonus xp while bonus weekend is
			// on
		} else if (Boundary.isIn(c, Boundary.DONATOR_ZONE) && c.getRights().isOrInherits(Right.ULTRA_DONATOR)
				&& DoubleExperience.isDoubleExperience()) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND_BOOSTED + Config.SERVER_EXP_BONUS;

			// If bonus weekend is enabled but no vote bonus xp is active, you get reg bonus
			// (20%)
		} else if (DoubleExperience.isDoubleExperience() && c.bonusXpTime == 0) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND;

			// If bonus weekend is enabled and vote bonus xp is active, you get additional
			// bonus (35%)
		} else if (DoubleExperience.isDoubleExperience() && c.bonusXpTime > 0) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND_BOOSTED;

			// If bonus weekend is disabled and vote bonus xp is active, you get reg vote
			// bonus (15%)
		} else if (!DoubleExperience.isDoubleExperience() && c.bonusXpTime > 0) {
			amount *= Config.SERVER_EXP_BONUS_BOOSTED;

			// If wogw bonus xp is enabled and vote bonus is active, you get additional
			// bonus (35%)
		} else if (Config.BONUS_XP_WOGW && c.bonusXpTime > 0) {
			amount *= Config.SERVER_EXP_BONUS_BOOSTED;
			// If wogw bonus xp is enabled and bonus weekend is active, you get additional
			// bonus (35%)
		} else if (Config.BONUS_XP_WOGW && DoubleExperience.isDoubleExperience()) {
			amount *= Config.SERVER_EXP_BONUS_BOOSTED;
			// Else (20%)
		} else if (Config.BONUS_XP_WOGW && c.bonusXpTime == 0 && !DoubleExperience.isDoubleExperience()) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND;
		} else if (c.xpScroll && c.bonusXpTime == 0 && !DoubleExperience.isDoubleExperience()) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND;

			// OSRS Modes receive only 1x the experience
		} else if (c.getMode().getType().equals(ModeType.OSRS)) {
			amount *= Config.SERVER_EXP_BONUS;

			// If none of the above are applied, regular experience is given
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}

		if (dropExperience) {
			addXpDrop(new XpDrop(amount, skill));
		}
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		int oldExperience = c.playerXP[skill];
		if (oldExperience < 200_000_000 && oldExperience + amount >= 200_000_000) {
			Skill s = Skill.forId(skill);
			c.xpMaxSkills += 1;
			PlayerHandler.executeGlobalMessage(
					"@red@" + Misc.capitalize(c.playerName) + " has reached 200M XP in " + s.toString() + "!");
			c.sendMessage("@blu@You have now maxed 200m experience in @red@"+c.xpMaxSkills+" skills!");



			if (c.xpMaxSkills > 21) {
				PlayerHandler.executeGlobalMessage(
						"@red@" + Misc.capitalize(c.playerName) + " has reached 200M XP in all skills!");
			}
		}
		amount = amount * 1;
		if (c.playerXP[skill] + amount > 200000000) {
			c.playerXP[skill] = 200000000;
		} else {
			c.playerXP[skill] += amount;
		}
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			c.combatLevel = c.calculateCombatLevel();
			c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
			levelUp(skill);
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				// TODO Skill Activity feed
			}
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	private static final int[] Runes = { 4740, 558, 560, 565 };
	private static final int[] Pots = {};

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		// synchronized(c) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeUnsignedWord(j);
			c.outStream.writeUnsignedWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeUnsignedWord(k);
			c.outStream.writeUnsignedWord(l);
			c.outStream.writeByte(j);
		}

	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].getIndex() == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, y, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		// TODO move these so they remove region clipping
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);



	}
	public void updateQuestTab(){
		if (c.d1Complete == true) {
		sendFrame126("@gre@Varrock", 29480);
		} else {
			sendFrame126("@red@Varrock", 29480);	
		}
		if (c.d2Complete == true) {
			sendFrame126("@gre@Ardougne", 29481);
		} else {
			sendFrame126("@red@Ardougne", 29481);
		}
		if (c.d3Complete == true) {
			sendFrame126("@gre@Desert", 29482);
		} else {
			sendFrame126("@red@Desert", 29482);
		}
		if (c.d4Complete == true) {
			sendFrame126("@gre@Falador", 29483);
		} else {
			sendFrame126("@red@Falador", 29483);
		}
		if (c.d5Complete == true) {
			sendFrame126("@gre@Fremnnik", 29484);
		} else {
			sendFrame126("@red@Fremnnik", 29484);
		}
		if (c.d6Complete == true) {
			sendFrame126("@gre@Kandarin", 29485);
		} else {
			sendFrame126("@red@Kandarin", 29485);
		}
		if (c.d7Complete == true) {
			sendFrame126("@gre@Karamja", 29486);
		} else {
			sendFrame126("@red@Karamja", 29486);
		}
		if (c.d8Complete == true) {
			sendFrame126("@gre@Lumbridge & Draynor", 29487);
		} else {
			sendFrame126("@red@Lumbridge & Draynor", 29487);
		}
		if (c.d9Complete == true) {
			sendFrame126("@gre@Morytania", 29488);
		} else {
			sendFrame126("@red@Morytania", 29488);
		}
		if (c.d10Complete == true) {
			sendFrame126("@gre@Western", 29489);
		} else {
			sendFrame126("@red@Western", 29489);
		}
		if (c.d11Complete == true) {
			sendFrame126("@gre@Wilderness", 29490);
		} else {
			sendFrame126("@red@Wilderness", 29490);
		}
	}
	public void handleGlory(int gloryId) {
		if (c.inWild() && c.wildLevel > Config.glory) {
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You cannot do that right now.");
			return;
		}
		c.getDH().sendOption4("Edgeville", "Karamja", "Draynor", "Al Kharid");
		c.sendMessage("You rub the amulet...");
		c.usingGlory = true;
	}

	public void handleSkills(int skillsId) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You cannot do that right now.");
			return;
		}
		c.getDH().sendOption4("Land's End", "Piscarilius Mining", "Resource Area", "Beach Bank");
		c.sendMessage("You rub the amulet...");
		c.usingSkills = true;
	}

	public void resetVariables() {
		if (c.playerIsCrafting) {
			CraftingData.resetCrafting(c);
		}
		if (c.playerSkilling[9]) {
			c.playerSkilling[9] = false;
		}
		if (c.isBanking) {
			c.isBanking = false;
		}
		c.viewingRunePouch = false;
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen())
			c.getLootingBag().closeLootbag();
		c.inSafeBox = false;
		c.viewingPresets = false;
		c.usingGlory = false;
		c.usingSkills = false;
		c.smeltInterface = false;
		if (c.dialogueAction > -1) {
			c.dialogueAction = -1;
		}
		if (c.teleAction > -1) {
			c.teleAction = -1;
		}
		if (c.battlestaffDialogue) {
			c.battlestaffDialogue = false;
		}
		if (c.craftDialogue) {
			c.craftDialogue = false;
		}
		c.setInterfaceOpen(-1);
		CycleEventHandler.getSingleton().stopEvents(c, CycleEventHandler.Event.BONE_ON_ALTAR);
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175 && c.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public double getAgilityRunRestore() {
		return 2260 - (c.playerLevel[16] * 10);
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int[] anItemsToCheck : itemsToCheck) {
			if (anItemsToCheck[1] < c.getItems().getTotalCount(anItemsToCheck[0]))
				return true;
		}
		return false;
	}

	public int totalLevel() {
		int total = 0;
		for (int i = 0; i <= 21; i++) {

			total += getLevelForXP(c.playerXP[i]);
		}
		return total;
	}

	public int xpTotal() {
		int xp = 0;
		for (int i = 0; i <= 21; i++) {
			xp += c.playerXP[i];
		}
		return xp;
	}

	public long getTotalXP() {
		return Arrays.stream(c.playerXP).asLongStream().sum();
	}


	public void logStuck() {
		String filePath = "./Data/stucklog.txt";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + c.playerName + "]: " + "[" + c.connectedFrom + "] " + ": " + " X: " + c.getX() + " Y: "
					+ c.getY() + "]");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public void logEmote(int id, String information) {
		String filePath = "./Data/" + c.playerName + ".txt";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + id + "] - " + information);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public void sendFrame34P2(int item, int slot, int frame, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeUnsignedWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeUnsignedWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public static boolean ringOfCharosTeleport(final Player player) {
		Task task = player.getSlayer().getTask().orElse(null);

		if (task == null) {
			player.sendMessage("You need a slayer task to use this.");
			return false;
		}
		if (player.inWild()) {
			player.sendMessage("You cannot use this from the wilderness.");
			return false;
		}
		int x = task.getTeleportLocation()[0];
		int y = task.getTeleportLocation()[1];
		int z = task.getTeleportLocation()[2];
		if (x == -1 && y == -1 && z == -1) {
			player.sendMessage("This task cannot be easily teleported to.");
			return false;
		}

		player.sendMessage("You are teleporting to your task of " + task.getPrimaryName() + ".");
		player.getPA().startTeleport(x, y, z, "modern", false);
		return true;
	}

	public void useOperate(int itemId) {
		ItemDefinition def = ItemDefinition.forId(itemId);
		Optional<DegradableItem> d = DegradableItem.forId(itemId);
		if (d.isPresent()) {
			Degrade.checkPercentage(c, itemId);
			return;
		}
		switch (itemId) {
		case 9948: // Teleport to puro puro
		case 9949:
			if (WheatPortalEvent.xLocation > 0 && WheatPortalEvent.yLocation > 0) {
				c.getPA().spellTeleport(WheatPortalEvent.xLocation + 1, WheatPortalEvent.yLocation + 1, 0, false);
			} else {
				c.sendMessage("There is currently no portal available, wait 5 minutes.");
				return;
			}
			break;
		case 12904:
			c.sendMessage(
					"The toxic staff of the dead has " + c.getToxicStaffOfTheDeadCharge() + " charges remaining.");
			break;
		case 13199:
		case 13197:
			c.sendMessage("The " + def.getName() + " has " + c.getSerpentineHelmCharge() + " charges remaining.");
			break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
			c.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
			break;
		case 12926:
			def = ItemDefinition.forId(c.getToxicBlowpipeAmmo());
			c.sendMessage("The blowpipe has " + c.getToxicBlowpipeAmmoAmount() + " " + def.getName() + " and "
					+ c.getToxicBlowpipeCharge() + " charge remaining.");
			break;
		case 19675:
			c.sendMessage("Your Arclight has " + c.getArcLightCharge() + " charges remaining.");
			break;
		case 12931:
			def = ItemDefinition.forId(itemId);
			if (def == null) {
				return;
			}
			c.sendMessage("The " + def.getName() + " has " + c.getSerpentineHelmCharge() + " charge remaining.");
			break;

		case 13136:
			if (c.inClanWars() || c.inClanWarsSafe()) {
				c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				c.sendMessage("You cannot do that right now.");
				return;
			}
			if (c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
				c.sendMessage(
						"You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return;
			}
			c.getPA().spellTeleport(3426, 2927, 0, false);
			break;

		case 13125:
		case 13126:
		case 13127:
			if (c.getRunEnergy() < 100) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishRun(50);
				}
			} else {
				c.sendMessage("You already have full run energy.");
				return;
			}
			break;

		case 13128:
			if (c.getRunEnergy() < 100) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishRun(100);
				}
			} else {
				c.sendMessage("You already have full run energy.");
				return;
			}
			break;

		case 13117:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishPrayer(4);
				}
			} else {
				c.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13118:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishPrayer(2);
				}
			} else {
				c.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13119:
		case 13120:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				if (c.getRechargeItems().useItem(itemId)) {
					c.getRechargeItems().replenishPrayer(1);
				}
			} else {
				c.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13111:
			if (c.getRechargeItems().useItem(itemId)) {
				c.getPA().spellTeleport(3236, 3946, 0, false);
			}
			break;
		case 10507:
			if (c.getItems().isWearingItem(10507)) {
				if (System.currentTimeMillis() - c.lastPerformedEmote < 2500)
					return;
				c.startAnimation(6382);
				c.gfx0(263);
				c.lastPerformedEmote = System.currentTimeMillis();
			}
			break;
		case 10026:
			if (c.getItems().isWearingItem(10026)) {
				if (System.currentTimeMillis() - c.lastPerformedEmote < 2500)
					return;
				c.startAnimation(2769);
				c.lastPerformedEmote = System.currentTimeMillis();
			}
			break;
		case 20243:
			if (System.currentTimeMillis() - c.lastPerformedEmote < 2500)
				return;
			c.startAnimation(7268);
			c.lastPerformedEmote = System.currentTimeMillis();
			break;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			c.sendMessage("You currently have " + (250 - c.crystalBowArrowCount)
					+ " charges left before degradation to " + (c.playerEquipment[3] == 4223 ? "Crystal seed"
							: ItemAssistant.getItemName(c.playerEquipment[3] + 1)));
			break;

		case 11864:
		case 11865:
		case 19639:
		case 19641:
		case 19643:
		case 19645:
		case 19647:
		case 19649:
			if (!c.getSlayer().getTask().isPresent()) {
				c.sendMessage("You do not have a task!");
				return;
			}
			c.sendMessage("I currently have @blu@" + c.getSlayer().getTaskAmount() + " "
					+ c.getSlayer().getTask().get().getPrimaryName() + "@bla@ to kill.");
			c.getPA().closeAllWindows();
			break;

		case 4202:
		case 9786:
		case 9787:
			ringOfCharosTeleport(c);
			break;

		case 11283:
		case 11284:
			if (Boundary.isIn(c, Zulrah.BOUNDARY) || Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)
					|| Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)) {
				return;
			}
			DragonfireShieldEffect dfsEffect = new DragonfireShieldEffect();
			if (c.npcAttackingIndex <= 0 && c.playerAttackingIndex <= 0) {
				return;
			}
			if (c.getHealth().getCurrentHealth() <= 0 || c.isDead) {
				return;
			}
			if (dfsEffect.isExecutable(c)) {
				Damage damage = new Damage(Misc.random(25));
				if (c.playerAttackingIndex > 0) {
					Player target = PlayerHandler.players[c.playerAttackingIndex];
					if (Objects.isNull(target)) {
						return;
					}
					c.attackTimer = 7;
					dfsEffect.execute(c, target, damage);
					c.setLastDragonfireShieldAttack(System.currentTimeMillis());
				} else if (c.npcAttackingIndex > 0) {
					NPC target = NPCHandler.npcs[c.npcAttackingIndex];
					if (Objects.isNull(target)) {
						return;
					}
					c.attackTimer = 7;
					dfsEffect.execute(c, target, damage);
					c.setLastDragonfireShieldAttack(System.currentTimeMillis());
				}
			}
			break;

		case 1712:
		case 1710:
		case 1708:
		case 1706:
			if (c.inClanWars() || c.inClanWarsSafe()) {
				c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;
		case 11968:
		case 11970:
		case 11105:
		case 11107:
		case 11109:
		case 11111:
			if (c.inClanWars() || c.inClanWarsSafe()) {
				c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			c.getPA().handleSkills(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			c.getPA().spellTeleport(3304, 3130, 0, false);
			break;

		/*
		 * Max capes
		 */
		case 13280:
		case 13329:
		case 13337:
		case 21898:
		case 13331:
		case 13333:
		case 13335:
		case 20760:
		case 21285:
			c.getDH().sendDialogues(76, 1);
			break;

		/*
		 * Crafting cape
		 */
		case 9780:
		case 9781:
			c.getPA().startTeleport(2936, 3283, 0, "modern", false);
			break;

		/*
		 * Magic skillcape
		 */
		case 9762:
		case 9763:
			if (!Boundary.isIn(c, Boundary.EDGEVILLE_PERIMETER)) {
				c.sendMessage("This cape can only be operated within the edgeville perimeter.");
				return;
			}
			if (c.inWild()) {
				return;
			}
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.autocasting = false;
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to the lunar spellbook.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;
		}
	}

	public void getSpeared(int otherX, int otherY, int distance) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		int xOffset = 0;
		int yOffset = 0;
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session) && session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("You cannot use this special whilst in the duel arena.");
			return;
		}
		if (x > 0) {
			if (c.getRegionProvider().getClipping(c.getX() + distance, c.getY(), c.heightLevel, 1, 0)) {
				xOffset = distance;
			}
		} else if (x < 0) {
			if (c.getRegionProvider().getClipping(c.getX() - distance, c.getY(), c.heightLevel, -1, 0)) {
				xOffset = -distance;
			}
		}
		if (y > 0) {
			if (c.getRegionProvider().getClipping(c.getX(), c.getY() + distance, c.heightLevel, 0, 1)) {
				yOffset = distance;
			}
		} else if (y < 0) {
			if (c.getRegionProvider().getClipping(c.getX(), c.getY() - distance, c.heightLevel, 0, -1)) {
				yOffset = -distance;
			}
		}
		moveCheck(xOffset, yOffset, distance);
		c.lastSpear = System.currentTimeMillis();
	}

	private void moveCheck(int x, int y, int distance) {
		PathFinder.getPathFinder().findRoute(c, c.getX() + x, c.getY() + y, true, 1, 1);
	}

	public void resetTzhaar() {
		c.waveId = -1;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}
	}

	/*
	 * public void fixAllBarrows() { int totalCost = 0; int cashAmount =
	 * c.getItems().getItemAmount(995); for (int j = 0; j < c.playerItems.length;
	 * j++) { boolean breakOut = false; for (int i = 0; i <
	 * c.getItems().brokenBarrows.length; i++) { if (c.playerItems[j]-1 ==
	 * c.getItems().brokenBarrows[i][1]) { if (totalCost + 80000 > cashAmount) {
	 * breakOut = true; c.sendMessage("You have run out of money."); break; } else {
	 * totalCost += 80000; } c.playerItems[j] = c.getItems().brokenBarrows[i][0]+1;
	 * } } if (breakOut) break; } if (totalCost > 0) c.getItems().deleteItem(995,
	 * c.getItems().getItemSlot(995), totalCost); }
	 */

	public void handleLoginText() {
		// modern
		c.getPA().sendFrame126("Teleport name", 1300); // varrock
		c.getPA().sendFrame126("Description", 1301); // varrock description
		c.getPA().sendFrame126("Teleport name", 1325); // lumbridge
		c.getPA().sendFrame126("Description", 1326); // lumbridge description
		c.getPA().sendFrame126("Teleport name", 1350); // falador
		c.getPA().sendFrame126("Description", 1351); // falador description
		c.getPA().sendFrame126("Teleport name", 1382); // camelot
		c.getPA().sendFrame126("Description", 1383); // camelot description
		c.getPA().sendFrame126("Teleport name", 1415); // ardougne
		c.getPA().sendFrame126("Description", 1416); // ardougne description
		c.getPA().sendFrame126("Teleport name", 1454); // watchtower
		c.getPA().sendFrame126("Description", 1455); // watchtower description
		c.getPA().sendFrame126("Teleport name", 7457); // trollheim
		c.getPA().sendFrame126("Description", 7458); // trollheim description
		c.getPA().sendFrame126("Teleport name", 18472); // ape atoll
		c.getPA().sendFrame126("Description", 18473); // ape atoll description
	}

	/**
	 * 
	 * @author Jason MacKeigan (http://www.rune-server.org/members/jason)
	 * @date Sep 26, 2014, 12:57:42 PM
	 */
	public enum PointExchange {
		PK_POINTS, VOTE_POINTS, BLOOD_POINTS
	}

	/**
	 * Exchanges all items in the player owners inventory to a specific to whatever
	 * the exchange specifies. Its up to the switch statement to make the
	 * conversion.
	 * 
	 * @param pointVar
	 *            the point exchange we're trying to make
	 * @param itemId
	 *            the item id being exchanged
	 * @param exchangeRate
	 *            the exchange rate for each item
	 */
	public void exchangeItems(PointExchange pointVar, int itemId, int exchangeRate) {
		try {
			int amount = c.getItems().getItemAmount(itemId);
			String pointAlias = Misc.capitalizeJustFirst(pointVar.name().toLowerCase().replaceAll("_", " "));
			if (exchangeRate <= 0 || itemId < 0) {
				throw new IllegalStateException();
			}
			if (amount <= 0) {
				c.getDH().sendStatement("You do not have the items required to exchange", "for " + pointAlias + ".");
				c.nextChat = -1;
				return;
			}
			int exchange = amount * exchangeRate;
			c.getItems().deleteItem2(itemId, amount);
			switch (pointVar) {
			case PK_POINTS:
				c.pkp += exchange;
				c.refreshQuestTab(0);
				break;

			case VOTE_POINTS:
				c.votePoints += exchange;
				c.refreshQuestTab(2);
				break;
			case BLOOD_POINTS:
				c.bloodPoints += amount;
				exchange = amount;
				break;
			}
			c.getDH().sendStatement("You exchange " + amount + " currency for " + exchange + " " + pointAlias + ".");
			c.nextChat = -1;
		} catch (IllegalStateException exception) {
			Misc.println("WARNING: Illegal state has been reached.");
			exception.printStackTrace();
			System.out.println("PlayerAssistant - Check for error");
		}
	}

	/**
	 * Sends some information to the client about screen fading.
	 * 
	 * @param text
	 *            the text that will be displayed in the center of the screen
	 * @param state
	 *            the state should be either 0, -1, or 1.
	 * @param seconds
	 *            the amount of time in seconds it takes for the fade to transition.
	 *            <p>
	 *            If the state is -1 then the screen fades from black to
	 *            transparent. When the state is +1 the screen fades from
	 *            transparent to black. If the state is 0 all drawing is stopped.
	 */
	public void sendScreenFade(String text, int state, int seconds) {
		if (c == null || c.getOutStream() == null) {
			return;
		}
		if (seconds < 1 && state != 0) {
			throw new IllegalArgumentException("The amount of seconds cannot be less than one.");
		}
		c.getOutStream().createFrameVarSize(9);
		c.getOutStream().writeString(text);
		c.getOutStream().writeByte(state);
		c.getOutStream().writeByte(seconds);
		c.getOutStream().endFrameVarSize();
	}

	public void stillCamera(int x, int y, int height, int speed, int angle) {
		c.outStream.createFrame(177);
		c.outStream.writeByte(x / 64);
		c.outStream.writeByte(y / 64);
		c.outStream.writeUnsignedWord(height);
		c.outStream.writeByte(speed);
		c.outStream.writeByte(angle);
	}

	public void spinCamera(int i1, int i2, int i3, int i4, int i5) {
		c.outStream.createFrame(166);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeUnsignedWord(i3);
		c.outStream.writeByte(i4);
		c.outStream.writeByte(i5);
	}

	public void resetCamera() {
		c.outStream.createFrame(107);
		c.setUpdateRequired(true);
		c.appearanceUpdateRequired = true;
	}

	/*
	 * c.getHealth().removeAllStatuses(); c.getHealth().reset(); c.setRunEnergy(99);
	 * c.sendMessage("@red@Your hitpoints and run energy have been restored!"); if
	 * (c.specRestore > 0) { c.sendMessage("You have to wait another " +
	 * c.specRestore + " seconds to restore special."); } else { c.specRestore =
	 * 120; c.specAmount = 10.0;
	 * c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]); c.
	 * sendMessage("Your special attack has been restored. You can restore it again in 3 minutes."
	 * ); }
	 */

	public static void switchSpellBook(Player c) {
		switch (c.playerMagicBook) {
		case 0:
			c.playerMagicBook = 1;
			c.setSidebarInterface(6, 838);
			c.sendMessage("An ancient wisdomin fills your mind.");
			break;
		case 1:
			c.sendMessage("You switch to the lunar spellbook.");
			c.setSidebarInterface(6, 29999);
			c.playerMagicBook = 2;
			break;
		case 2:
			c.setSidebarInterface(6, 938);
			c.playerMagicBook = 0;
			c.sendMessage("You feel a drain on your memory.");
			break;
		}
	}

	public static void refreshHealthWithoutPenalty(Player c) {
		c.getHealth().setCurrentHealth(c.getHealth().getMaximumHealth() + 2);
		c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]) + 2;
		c.startAnimation(645);
		c.setRunEnergy(100);
		c.getPA().refreshSkill(5);
		c.sendMessage("You recharge your hitpoints, prayer and run energy.");
	}

	public static void refreshSpecialAndHealth(Player c) {
		c.getHealth().removeAllStatuses();
		c.getHealth().reset();
		c.setRunEnergy(100);
		c.sendMessage("@red@Your hitpoints and run energy have been restored!");
		if (c.specRestore > 0) {
			c.sendMessage("You have to wait another " + c.specRestore + " seconds to restore special.");
		} else {
			c.specRestore = 120;
			c.specAmount = 10.0;
			c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
			c.sendMessage("Your special attack has been restored. You can restore it again in 3 minutes.");
		}
	}

	public void icePath() {
		int random = Misc.random(20);
		if (random == 5) {
			c.startAnimation(767);
			c.appendDamage(Misc.random(1) + 1, Hitmark.HIT);
			c.resetWalkingQueue();
			c.forcedChat("Ouch!");
		}
	}

	public static void noteItems(Player player, int item) {
		ItemDefinition definition = ItemDefinition.forId(item);
		ItemDefinition notedDefinition = ItemDefinition.forId(item + 1);
		if (definition == null || notedDefinition == null
				|| !notedDefinition.getName().contains(definition.getName())) {
		 player.sendMessage("You cannot note this item, it is unnotable.");
			return;
		}
		if (!player.getItems().playerHasItem(item, 1)) {
			return;
		}
		for (int index = 0; index < player.playerItems.length; index++) {
			int amount = player.playerItemsN[index];
			if (player.playerItems[index] == item + 1 && amount > 0) {
				player.getItems().deleteItem(item, index, amount);
				player.getItems().addItem(item + 1, amount);
			}
		}
		player.getDH().sendStatement("You note all your " + definition.getName() + ".");
		player.nextChat = -1;
	}

	public static void decantHerbs(Player player, int item) {
		ItemDefinition definition = ItemDefinition.forId(item);
		ItemDefinition notedDefinition = ItemDefinition.forId(item + 1);
		if (definition == null || notedDefinition == null
				|| !notedDefinition.getName().contains(definition.getName())) {
			//player.sendMessage("You cannot note this item, it is unnotable.");
			return;
		}
		if (!(item >= 249) || !(item <= 270) && !(item == 2481) && !(item == 2998) && !(item == 3000)) {
			player.sendMessage("The master farmer cannot assist you with this.");
			return;
		}
		for (int index = 0; index < player.playerItems.length; index++) {
			int amount = player.playerItemsN[index];
			if (player.playerItems[index] == item + 1 && amount > 0) {
				player.getItems().deleteItem(item, index, amount);
				player.getItems().addItem(item + 1, amount);
			}
		}
		player.getDH().sendStatement("You note all your " + definition.getName() + ".");
		player.nextChat = -1;
	}

	public static void decantResource(Player player, int item) {
		ItemDefinition definition = ItemDefinition.forId(item);
		ItemDefinition notedDefinition = ItemDefinition.forId(item + 1);
		int cost = 0;
		if (definition == null || notedDefinition == null
				|| !notedDefinition.getName().contains(definition.getName())) {
			//player.sendMessage("You cannot note this item, it is unnotable.");
			return;
		}
		if (!isSkillAreaItem(item)) {
			player.sendMessage("You can only note items that are resources obtained from skilling in this area.");
			return;
		}
		if (!player.getRights().isOrInherits(Right.REGULAR_DONATOR) && !player.getRechargeItems().hasItem(13111)) {
			int inventoryAmount = player.getItems().getItemAmount(item);
			if (inventoryAmount < 4) {
				player.sendMessage("You need at least 4 of this item to note it.");
				return;
			}
			cost = (int) Math.round(inventoryAmount / 4.0D);
			if (!player.getItems().playerHasItem(item, cost)) {
				return;
			}
			player.getItems().deleteItem2(item, cost);
		}
		for (int index = 0; index < player.playerItems.length; index++) {
			int amount = player.playerItemsN[index];
			if (player.playerItems[index] == item + 1 && amount > 0) {
				player.getItems().deleteItem(item, index, amount);
				player.getItems().addItem(item + 1, amount);
			}
		}
		if (!player.getRights().isOrInherits(Right.REGULAR_DONATOR)) {
			player.getDH().sendStatement(
					"You note most of your " + definition.getName() + " at the cost of " + cost + " resources.");
		} else {
			player.getDH().sendStatement("You note all your " + definition.getName() + ".");
		}
		player.nextChat = -1;
	}

	private static boolean isSkillAreaItem(int item) {
		for (Mineral m : Mineral.values()) {
			if (Misc.linearSearch(m.getMineralReturn().inclusives(), item) != -1) {
				return true;
			}
		}
		for (Tree t : Tree.values()) {
			if (t.getWood() == item)
				return true;
		}
		for (int[] fish : Fishing.data) {
			if (fish[4] == item)
				return true;
		}
		for (int cookFish : Cooking.fishIds) {
			if (cookFish == item)
				return true;
		}
		for (Bars b : Smelting.Bars.values()) {
			if (b.getBar() == item)
				return true;
		}
		return false;
	}

	public void sendEntityTarget(int state, Entity entity) {
		if (c.disconnected || c.getOutStream() == null) {
			return;
		}
		Stream stream = c.getOutStream();
		stream.createFrameVarSize(222);
		stream.writeByte(state);
		if (state != 0) {
			stream.writeUnsignedWord(entity.getIndex());
			stream.writeUnsignedWord(entity.getHealth().getCurrentHealth());
			stream.writeUnsignedWord(entity.getHealth().getMaximumHealth());
		}
		stream.endFrameVarSize();
	}

	public void sendEnterAmount(int interfaceId) {
		c.getOutStream().createFrame(27);
		c.setEnterAmountInterfaceId(interfaceId);
	}

	public void sendGameTimer(ClientGameTimer timer, TimeUnit unitOfTime, int duration) {
		if (c == null || c.disconnected) {
			return;
		}
		Stream stream = c.getOutStream();
		if (stream == null) {
			return;
		}
		// System.out.println(duration);
		int seconds = (int) Long.min(unitOfTime.toSeconds(duration), 65535);
		// System.out.println(seconds);
		stream.createFrame(223);
		stream.writeByte(timer.getTimerId());
		stream.writeUnsignedWord(seconds);
		c.flushOutStream();
	}

	public void sendExperienceDrop(boolean increase, long amount, int... skills) {
		if (c.disconnected || c.getOutStream() == null) {
			return;
		}
		List<Integer> illegalSkills = new ArrayList<>();

		for (int index = 0; index < skills.length; index++) {
			int skillId = skills[index];
			if (skillId < 0 || skillId > Skill.MAXIMUM_SKILL_ID) {
				illegalSkills.add(index);
			}
		}
		if (!illegalSkills.isEmpty()) {
			skills = ArrayUtils.removeAll(skills,
					ArrayUtils.toPrimitive(illegalSkills.toArray(new Integer[illegalSkills.size()])));
		}
		if (ArrayUtils.isEmpty(skills)) {
			return;
		}
		if (increase) {
			c.setExperienceCounter(c.getExperienceCounter() + amount);
		}

		Stream stream = c.getOutStream();

		stream.createFrameVarSize(11);
		stream.writeQWord(amount);
		stream.writeByte(skills.length);
		for (int skillId : skills) {
			stream.writeByte(skillId);
		}
		stream.endFrameVarSize();
	}

	public void sendTimeZone() {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrameVarSize(6);
			c.getOutStream().writeString(new GregorianCalendar().getTimeZone().getID());
			c.getOutStream().endFrameVarSize();
		}
	}

	public void sendConfig(final int id, final int state) {
		if (this.c.getOutStream() != null && this.c != null) {
			if (state < 128) {
				this.c.getOutStream().createFrame(36);
				this.c.getOutStream().writeWordBigEndian(id);
				this.c.getOutStream().writeByte(state);
			} else {
				this.c.getOutStream().createFrame(87);
				this.c.getOutStream().writeWordBigEndian_dup(id);
				this.c.getOutStream().writeDWord_v1(state);
			}
			this.c.flushOutStream();
		}
	}

	public void sendTradingPost(int frame, int item, int slot, int amount) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeUnsignedWord(frame);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeUnsignedWord(item + 1);
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	/**
	 * Creates a Solo instance of the player for whatever choosen boss
	 * 
	 * @param i
	 */
	public void createSoloInstance(int i) {
		if (!c.getItems().playerHasItem(995, 400000)) {
			c.sendMessage("Instancing a boss costs 400k!");
			removeAllWindows();
			return;
		}
		InstanceSoloFight isf = c.createSoloFight();
		if (isf == null) {
			c.sendMessage("Could not find proper instanced area! Contact developer!");
			return;
		}
		// Delete items after, incase there is no spot found
		c.getItems().deleteItem(995, 400000);
		removeAllWindows();

		// Example 239 = KBD
		c.getSoloFight().init(i, c);
	}

}
