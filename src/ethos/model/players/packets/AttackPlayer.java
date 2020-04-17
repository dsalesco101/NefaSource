package ethos.model.players.packets;

import ethos.Config;
import ethos.Server;
import ethos.ServerState;
import ethos.model.items.ItemAssistant;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.magic.LunarSpells;
import ethos.model.players.combat.magic.MagicData;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.playerAttackingIndex = 0;
		player.npcAttackingIndex = 0;
		if (player.isForceMovementActive()) {
			return;
		}
		switch (packetType) {
		case ATTACK_PLAYER:
			player.playerAttackingIndex = player.getInStream().readSignedWordBigEndian();
			player.lastPlayerAttackingIndex = player.playerAttackingIndex;
			player.lastNpcAttackingIndex = 0;

			if (player.playerAttackingIndex >= PlayerHandler.players.length || player.playerAttackingIndex < 0) {
				return;
			}
			if (PlayerHandler.players[player.playerAttackingIndex] == null) {
				break;
			}
			if (player.morphed) {
				return;
			}
			if (player.respawnTimer > 0) {
				break;
			}
			if (player.getTutorial().isActive()) {
				player.getTutorial().refresh();
				return;
			}
			if (player == (PlayerHandler.players[player.playerAttackingIndex])) {
				player.sendMessage("You cannot attack yourself, " + player.playerName + ".");
				return;
			}
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
			}
			if (player.autocastId > 0)
				player.autocasting = true;

			if (!player.autocasting && player.spellId > 0) {
				player.spellId = 0;
			}
			player.mageFollow = false;
			player.spellId = 0;
			player.usingMagic = false;

			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = player.getItems().isWearingAnyItem(9185, 11785, 21012, 21902);
			boolean usingBlowpipe = player.getItems().isWearingItem(12926);
			boolean usingBow = usingBlowpipe;

			for (int bowId : player.BOWS) {
				if (player.playerEquipment[player.playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : player.ARROWS) {
						if (player.playerEquipment[player.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : player.OTHER_RANGE_WEAPONS) {
				if (player.playerEquipment[player.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}

			if ((usingBow || player.autocasting)
					&& player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerAttackingIndex].getX(), PlayerHandler.players[player.playerAttackingIndex].getY(), 7)) {
				player.usingBow = true;
				player.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerAttackingIndex].getX(), PlayerHandler.players[player.playerAttackingIndex].getY(), 3)) {
				player.usingRangeWeapon = true;
				player.stopMovement();
			}
			if (!usingBow)
				player.usingBow = false;
			if (!usingOtherRangeWeapons)
				player.usingRangeWeapon = false;

			if (!usingBlowpipe && !usingCross && !usingArrows && usingBow
					&& ((player.playerEquipment[player.playerWeapon] < 4212) || (player.playerEquipment[player.playerWeapon] > 4223))
					&& !(player.playerEquipment[player.playerWeapon] == 22550)) {
				player.sendMessage("You have run out of arrows!");
				return;
			}
			if ((player.getCombat().usingCrystalBow() || player.playerEquipment[player.playerWeapon] == 22550) && player.usingArrows && player.getCombat().properBolts()) {
				player.sendMessage("You cannot use ammo with this bow.");
				return;
			}
			
			if (!player.getCombat().correctBowAndArrows()
					&& Config.CORRECT_ARROWS
					&& !usingBlowpipe
					&& usingBow
					&& !player.getCombat().usingCrystalBow()
					&& !(player.playerEquipment[player.playerWeapon] == 22550) && !player.getItems().isWearingAnyItem(4734, 9185, 11785, 21012, 19481, 19478, 21902)) {
				player.sendMessage("You can't use " + ItemAssistant.getItemName(player.playerEquipment[player.playerArrows]).toLowerCase() + "'s with a "
						+ ItemAssistant.getItemName(player.playerEquipment[player.playerWeapon]).toLowerCase() + ".");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return;
			}
			if (player.getItems().isWearingAnyItem(9185, 11785, 21012, 21902) && !player.getCombat().properBolts()) {
				player.sendMessage("You must use bolts with a crossbow.");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return;
			}
			if (player.getCombat().checkReqs()) {
				player.followId = player.playerAttackingIndex;
				player.combatFollowing = true;
				if (!player.usingMagic && !usingBow && !usingOtherRangeWeapons) {
					player.followDistance = 1;
					player.usingMelee = true;
					player.combatFollowing = true;
					player.getPA().followPlayer();
					player.stopMovement();
				}
				if (player.attackTimer <= 0) {
					// client.sendMessage("Tried to attack...");
					// client.getCombat().attackPlayer(client.playerAttackingIndex);
					// client.attackTimer++;
				}

			}
			break;
		case MAGE_PLAYER:

			if (!player.mageAllowed) {
				player.mageAllowed = true;
				break;
			}

			if (player.morphed) {
				return;
			}
			// client.usingSpecial = false;
			// client.getItems().updateSpecialBar();

			player.playerAttackingIndex = player.getInStream().readSignedWordA();
			if (player.playerAttackingIndex >= PlayerHandler.players.length || player.playerAttackingIndex < 0) {
				return;
			}
			int castingSpellId = player.getInStream().readSignedWordBigEndian();
			player.usingMagic = false;
			if (PlayerHandler.players[player.playerAttackingIndex] == null) {
				break;
			}

			if (player.respawnTimer > 0) {
				break;
			}
			if (castingSpellId > 30_000) {
				LunarSpells.CastingLunarOnPlayer(player, castingSpellId);
			}
			for (int i = 0; i < MagicData.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == MagicData.MAGIC_SPELLS[i][0]) {
					player.spellId = i;
					player.usingMagic = true;
					break;
				}
			}

			if (player.autocasting)
				player.autocasting = false;

			if (!player.getCombat().checkReqs()) {
				break;
			}
			if (player == (PlayerHandler.players[player.playerAttackingIndex])) {
				player.sendMessage("You cannot attack yourself, " + player.playerName + ".");
				return;
			}

			for (int r = 0; r < player.REDUCE_SPELLS.length; r++) { // reducing
				// spells,
				// confuse etc
				if (PlayerHandler.players[player.playerAttackingIndex].REDUCE_SPELLS[r] == MagicData.MAGIC_SPELLS[player.spellId][0]) {
					if ((System.currentTimeMillis()
							- PlayerHandler.players[player.playerAttackingIndex].reduceSpellDelay[r]) < PlayerHandler.players[player.playerAttackingIndex].REDUCE_SPELL_TIME[r]) {
						player.sendMessage("That player is currently immune to this spell.");
						player.usingMagic = false;
						player.stopMovement();
						player.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (Server.getConfiguration().getServerState() != ServerState.DEBUG
					&& System.currentTimeMillis() - PlayerHandler.players[player.playerAttackingIndex].teleBlockDelay
							< PlayerHandler.players[player.playerAttackingIndex].teleBlockLength
					&& MagicData.MAGIC_SPELLS[player.spellId][0] == 12445) {
				player.sendMessage("That player is already affected by this spell.");
				player.usingMagic = false;
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
			}

			/*
			 * if(!client.getCombat().checkMagicReqs(client.spellId)) { client.stopMovement(); client.getCombat().resetPlayerAttack(); break; }
			 */

			if (player.usingMagic) {
				if (player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerAttackingIndex].getX(), PlayerHandler.players[player.playerAttackingIndex].getY(), 8)) {
					player.stopMovement();
				}
				if (player.getCombat().checkReqs()) {
					player.followId = player.playerAttackingIndex;
					player.combatFollowing = true;
					player.mageFollow = true;
					if (player.attackTimer <= 0) {
						// client.getCombat().attackPlayer(client.playerAttackingIndex);
						// client.attackTimer++;
					}
				}

			}
			break;
		}

	}

}
