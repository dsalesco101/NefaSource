package ethos.model.players.packets.dialogueoptions;

import ethos.Server;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.players.skills.slayer.Slayer;

/*
 * @author Matt
 * Three Option Dialogue actions
 */

public class ThreeOptions {

	/*
	 * Handles all first options on 'Three option' dialogues.
	 */
	public static void handleOption1(Player c) {
		switch (c.dialogueAction) {
		case 698:
			if (!c.getItems().playerHasItem(561, 1 * 10)) {
				c.getDH().sendStatement("You need atleast 10 nature runes.");
				return;
			}
			if (!c.getItems().playerHasItem(554, 4 * 10)) {
				c.getDH().sendStatement("You need atleast 40 fire runes.");
				return;
			}
			if (!c.getItems().playerHasItem(995, 500 * 10)) {
				c.getDH().sendStatement("You need atleast 5k coins.");
				return;
			}
			c.getItems().deleteItem(561, 1 * 10);
			c.getItems().deleteItem(554, 4 * 10);
			c.getItems().deleteItem(995, 500 * 10);
			c.alchCharge += 10;
			c.sendMessage("@blu@You have added @red@10@blu@ charges to your alchemy pouch.");
			c.sendMessage("@blu@You now have a total of @red@"+c.alchCharge+"@blu@ charges to your alchemy pouch.");
			break;
		case 950:
			c.getDH().sendItemStatement("Please use this gem on your partner", 4155);
			break;
		case 265:
			int amount = c.getItems().getItemAmount(20718);
			int pages = 20718;
			if (!c.getItems().playerHasItem(pages)) {
	            c.sendMessage("@blu@You have no pages to store.");
	            c.getPA().closeAllWindows();
				return;
			}

			if (c.pages == 1000) {
	            c.sendMessage("@blu@You have reached the maximum pages in the tome of fire.");
	            c.getPA().closeAllWindows();
				return;
			}
            c.sendMessage("@blu@You now have stored @red@"+amount+ "@blu@ pages in your tome of fire.");
            c.increasePages(amount);
            c.getItems().deleteItem(20718, amount);
            c.getPA().closeAllWindows();
            break;
		case 152:
			c.getDH().sendDialogues(153, 1603);
			break;
		case 1428:
			c.getPrestige().openPrestige();
			break;
		case 809: // Withdraw
			// TODO: withdraw 10
			break;
		case 811: // Deposit
			// TODO: withdraw 10
			break;
		case 806:
			c.getDH().sendDialogues(811, 6773);
			break;

		case 71: // Jad, sell cape
			if (!c.getItems().playerHasItem(6570)) {
				c.sendMessage("You do not have a firecape.");
				return;
			}
			c.getItems().deleteItem(6570, 1);
			c.getItems().addItem(6529, 8_000);
			c.getPA().removeAllWindows();
			break;

		case 55:
			c.getCT().seas("TEN");
			c.dialogueAction = -1;
			c.getPA().removeAllWindows();
			break;
		case 56:
			c.getCT().swamp("TEN");
			c.dialogueAction = -1;
			c.getPA().removeAllWindows();
			break;
		}
		if (c.dialogueAction == 137) {
			c.getPA().openUpBank();
			return;
		}
		if (c.dialogueAction == 126) {
			c.getPA().startTeleport(3039, 4835, 0, "modern", false);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		switch (c.teleAction) {
		case 2:
			c.getPA().spellTeleport(1571, 3656, 0, false);
			break;
		}
		if (c.dialogueAction == 100) {
			c.getShops().openShop(80);
			return;
		}
		if (c.dialogueAction == 2245) {
			c.getPA().startTeleport(2110, 3915, 0, "modern", false);
			c.sendMessage("High Priest teleported you to @red@Lunar Island@bla@.");
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 508) {
			c.getDH().sendDialogues(1030, 925);
			return;
		}
		if (c.teleAction == 2) {
			// brim
			c.getPA().spellTeleport(1571, 3656, 0, false);
		}
		if (c.dialogueAction == 502) {
			c.getDH().sendDialogues(1030, 925);
			return;
		}
		if (c.dialogueAction == 251) {
			c.getPA().openUpBank();
		}
		if (c.teleAction == 200) {
			c.getPA().spellTeleport(2662, 2652, 0, false);
		}
		if (c.doricOption) {
			c.getDH().sendDialogues(306, 284);
			c.doricOption = false;
		}
	}

	/*
	 * Handles all 2nd options on 'Three option' dialogues.
	 */
	public static void handleOption2(Player c) {

		switch (c.dialogueAction) {
		case 698:
			if (!c.getItems().playerHasItem(561, 1 * 100)) {
				c.getDH().sendStatement("You need atleast 100 nature runes.");
				return;
			}
			if (!c.getItems().playerHasItem(554, 4 * 100)) {
				c.getDH().sendStatement("You need atleast 400 fire runes.");
				return;
			}
			if (!c.getItems().playerHasItem(995, 500 * 100)) {
				c.getDH().sendStatement("You need atleast 50k coins.");
				return;
			}
			c.getItems().deleteItem(561, 1 * 100);
			c.getItems().deleteItem(554, 4 * 100);
			c.getItems().deleteItem(995, 500 * 100);
			c.alchCharge += 100;
			c.sendMessage("@blu@You have added @red@100@blu@ charges to your alchemy pouch.");
			c.sendMessage("@blu@You now have a total of @red@"+c.alchCharge+"@blu@ charges to your alchemy pouch.");
			break;
		case 950:
			PlayerHandler.executeGlobalMessage("@pur@Type ::duo "+c.playerName+" to assistant with "+c.getSlayer().taskAmount+" "+c.getSlayer().task.get().getPrimaryName()+".");
			break;
		case 932:
			c.getPA().removeAllWindows();
			break;
		case 265:
			if (c.getItems().freeSlots() < 1) {
                c.sendMessage("You need atleast 1 inventory space to do this.");
    			c.getPA().removeAllWindows();
                return;
            }
			c.getItems().addItem(20718, c.pages);
			c.decreasePages(c.pages);
			c.sendMessage("@blu@You have removed all of your pages from your tome of fire.");
			c.getItems().deleteItem(20714, 1);
	        c.getItems().addItem(20716, 1);
			c.getPA().removeAllWindows();
		    break;
		case 1428:
			c.getPrestige().openShop();
			break;
		case 809: // Withdraw
			// TODO: withdraw 100
			break;
		case 811: // Deposit
			// TODO: withdraw 100
			break;
		case 806:
			c.getDH().sendDialogues(809, 6773);
			break;
		case 71: // Jad, keep cape
			c.getPA().removeAllWindows();
			break;

		case 55:
			c.getCT().seas("HUNDRED");
			c.getPA().removeAllWindows();
			break;
		case 56:
			c.getCT().swamp("HUNDRED");
			c.getPA().removeAllWindows();
			break;
		}
		ethos.model.items.bank.BankPin pin = c.getBankPin();
		if (c.dialogueAction == 137) {
			pin = c.getBankPin();
			if (!pin.getPin().isEmpty()) {
				c.sendMessage("You already have a bank pin.");
				c.getPA().removeAllWindows();
			} else {
				pin.open(1);
			}
			return;
		}
		if (c.dialogueAction == 126) {
			if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.TELEPORT_ESSENCE_VAR);
			}
			if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
				c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ESSENCE_ARD);
			}
			if (Boundary.isIn(c, Boundary.FALADOR_BOUNDARY)) {
				c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_ESSENCE_FAL);
			}
			c.getPA().startTeleport(2929, 4813, 0, "modern", false);
			c.dialogueAction = -1;
			c.teleAction = -1;
			return;
		}
		switch (c.teleAction) {
		case 2:
			c.getPA().spellTeleport(1663, 3527, 0, false);
			c.teleAction = -1;
			break;
		}
		if (c.dialogueAction == 100) {
			c.getDH().sendDialogues(545, 315);
			return;
		}
		if (c.dialogueAction == 2245) {
			c.getPA().startTeleport(3230, 2915, 0, "modern", false);
			c.sendMessage("High Priest teleported you to @red@Desert Pyramid@bla@.");
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 508) {
			c.getDH().sendDialogues(1027, 925);
			return;
		}
		if (c.teleAction == 2) {
			// Tav
			c.getPA().spellTeleport(1663, 3527, 0, false);
		}
		if (c.dialogueAction == 502) {
			c.getDH().sendDialogues(1027, 925);
			return;
		}
		if (c.teleAction == 200) {
			c.getPA().spellTeleport(3365, 3266, 0, false);

		}
		if (c.doricOption) {
			c.getDH().sendDialogues(303, 284);
			c.doricOption = false;
		}
	}

	/*
	 * Handles all 3rd options on 'Three option' dialogues.
	 */
	public static void handleOption3(Player c) {
		switch (c.dialogueAction) {
		case 698:
			if (!c.getItems().playerHasItem(561, 1 * 1000)) {
				c.getDH().sendStatement("You need atleast 1000 nature runes.");
				return;
			}
			if (!c.getItems().playerHasItem(554, 4 * 1000)) {
				c.getDH().sendStatement("You need atleast 4000 fire runes.");
				return;
			}
			if (!c.getItems().playerHasItem(995, 500 * 100)) {
				c.getDH().sendStatement("You need atleast 500k coins.");
				return;
			}
			c.getItems().deleteItem(561, 1 * 1000);
			c.getItems().deleteItem(554, 4 * 1000);
			c.getItems().deleteItem(995, 500 * 1000);
			c.alchCharge += 1000;
			c.sendMessage("@blu@You have added @red@1000@blu@ charges to your alchemy pouch.");
			c.sendMessage("@blu@You now have a total of @red@"+c.alchCharge+"@blu@ charges to your alchemy pouch.");
			break;
		case 265:
		case 950:
			c.getPA().removeAllWindows();
			break;
		case 809: // Withdraw
			// TODO: withdraw all
			break;
		case 811: // Deposit
			// TODO: withdraw all
			break;
		case 806:
			c.getDH().sendDialogues(807, 6773);
			break;
		case 71: // Bargain cape
			c.getDH().sendDialogues(72, 2180);
			break;
		case 55:
			c.getCT().seas("THOUSAND");
			c.getPA().removeAllWindows();
			break;
		case 56:
			c.getCT().swamp("THOUSAND");
			c.getPA().removeAllWindows();
			break;

		case 126:
			if (c.dialogueAction == 126) {
				if (c.getItems().getItemCount(5509, true) == 1) {
					c.getDH().sendNpcChat("You already seem to have a pouch.");
				} else {
					c.getItems().addItem(5509, 1);
					c.getDH().sendItemStatement("The mage hands you a pouch", 5509);
					c.sendMessage("[Rc Pouch] Kill npcs with the pouch in inventory to upgrade it! 1\100 chance");
				}
			}
			break;
		}
		if (c.dialogueAction == 137) {
			c.getPA().removeAllWindows();
			return;
		}
		switch (c.teleAction) {
		case 2:
			c.getPA().spellTeleport(1262, 3501, 0, false);
			return;
		}
		if (c.dialogueAction == 14400 || c.dialogueAction == 100) {
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 2245) {
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 508) {
			c.nextChat = 0;
			c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 502 || c.dialogueAction == 1428) {
			c.nextChat = 0;
			c.getPA().closeAllWindows();
		}
		if (c.teleAction == 2) {
			c.getPA().spellTeleport(1262, 3501, 0, false);
		}
		if (c.dialogueAction == 251) {
			c.getDH().sendDialogues(1015, 394);
		}
		if (c.teleAction == 200) {
			c.getPA().spellTeleport(2439, 5169, 0, false);
			c.sendMessage("Use the cave entrance to start.");
		}
		if (c.doricOption) {
			c.getDH().sendDialogues(299, 284);
		}
	}

}
