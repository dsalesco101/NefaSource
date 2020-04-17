package ethos.model.content.lootbag;

import java.util.Iterator;
import java.util.Objects;

import ethos.Server;
import ethos.model.entity.Entity;
import ethos.model.items.ItemDefinition;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;

/**
 * Looting bag functionality.
 *
 * @author Sky
 * @Author trees (Fixed)
 */
public class LootingBag {

    public enum LootingBagUseAction {
        OPTION, ONE, TEN, ALL, X
    }

    /**
     * The looting bag id
     */
    public static final int LOOTING_BAG = 11941;
    public static final int LOOTING_BAG_OPEN = 22586;
    public static final int DEPOSIT_INTERFACE_ID = 39448;
    public static final int WITHDRAW_INTERFACE_ID = 39349;
    public static final int ITEM_ON_ITEM_DIALOGUE_ID = 1234;
    public static final int OPTIONS_DIALOGUE_ID = 1235;

    private final Player player;
    private final LootingBagContainer lootingBagContainer;
    private boolean withdrawInterfaceOpen = false;
    private boolean depositInterfaceOpen = false;
    private boolean selectDepositAmountInterfaceOpen = false;
    private int selectedItem = -1;
    private int selectedSlot = -1;
    private LootingBagUseAction useAction = LootingBagUseAction.OPTION;

    public LootingBag(Player player) {
        this.player = player;
        lootingBagContainer = new LootingBagContainer(player);
    }

    public boolean hasLootingBagInInventory() {
        return player.getItems().playerHasItem(LOOTING_BAG_OPEN) || player.getItems().playerHasItem(LOOTING_BAG);
    }

    public static boolean isLootingBag(Player player, int itemId) {
        return itemId == LOOTING_BAG || itemId == LOOTING_BAG_OPEN;
    }

    public void toggleOpen() {
        if (canUseLootingBag()) {
            if (player.getItems().playerHasItem(LOOTING_BAG)) {
                player.getItems().setInventoryItemSlot(player.getItems().getItemSlot(LOOTING_BAG), LOOTING_BAG_OPEN);
                player.sendMessage("You open your looting bag.");
            } else if (player.getItems().playerHasItem(LOOTING_BAG_OPEN)) {
                player.getItems().setInventoryItemSlot(player.getItems().getItemSlot(LOOTING_BAG_OPEN), LOOTING_BAG);
                player.sendMessage("You close your looting bag.");
            }
        }
    }

    public void useItemOnBag(int itemId) {
        if (useAction == LootingBagUseAction.OPTION) {
            setSelectedItem(itemId);
            selectDepositAmountInterfaceOpen = true;
            player.getDH().sendDialogues(ITEM_ON_ITEM_DIALOGUE_ID, 0);
        } else if (useAction == LootingBagUseAction.X) {
            player.getPA().sendEnterAmount(DEPOSIT_INTERFACE_ID);
            setSelectedItem(itemId);
        } else {
            getLootingBagContainer().deposit(itemId, useAction == LootingBagUseAction.ALL ? Integer.MAX_VALUE
                    : useAction == LootingBagUseAction.TEN ? 10  : 1);
        }
    }

    /**
     * Handles deposit and withdrawal of items
     *
     * @param id     The item being configured
     * @param amount The amount of the item being configured
     * @return
     */
    public boolean handleClickItem(int id, int amount) {
        if (!canUseLootingBag()) {
            player.sendMessage("You cannot do this right now.");
            return false;
        } else if (isWithdrawInterfaceOpen() || player.getEnterAmountInterfaceId() == WITHDRAW_INTERFACE_ID) {
            if (amount == -1) {
                player.getPA().sendEnterAmount(DEPOSIT_INTERFACE_ID);
                setSelectedItem(id);
            } else {
                getLootingBagContainer().removeMultipleItemsFromBag(id, amount);
                updateItemContainers();
            }
            return true;
        } else if (isDepositInterfaceOpen() || player.getEnterAmountInterfaceId() == DEPOSIT_INTERFACE_ID
                || selectDepositAmountInterfaceOpen) {
            if (amount == -1) {
                player.getPA().sendEnterAmount(DEPOSIT_INTERFACE_ID);
                setSelectedItem(id);
            } else {
                if (selectDepositAmountInterfaceOpen) {
                    player.getPA().closeAllWindows();
                    selectDepositAmountInterfaceOpen = false;
                }
                getLootingBagContainer().deposit(id, amount);
                updateItemContainers();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens withdrawal mode
     */
    public void openWithdrawalMode() {
        if (!canUseLootingBag()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.getItems().playerHasItem(LOOTING_BAG) && !player.getItems().playerHasItem(LOOTING_BAG_OPEN)) {
            return;
        }

        reset();
        updateItemContainers();
        player.getPA().sendTabAreaOverlayInterface(39342);
        setWithdrawInterfaceOpen(true);
    }

    /**
     * Opens deposit mode
     */
    public void openDepositMode() {
        if (!canUseLootingBag()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.getItems().playerHasItem(LOOTING_BAG) && !player.getItems().playerHasItem(LOOTING_BAG_OPEN)) {
            return;
        }
        if (player.inClanWars() || player.inClanWarsSafe()) {
            return;
        }
        if (!player.inWild()) {
            player.sendMessage("You can only do this in the wilderness.");
            return;
        }
        reset();
        updateItemContainers();
        player.getPA().sendTabAreaOverlayInterface(39443);
        setDepositInterfaceOpen(true);
    }

    private void updateItemContainers() {
        player.getItems().resetItems(DEPOSIT_INTERFACE_ID);
        player.getItems().sendItemContainer(WITHDRAW_INTERFACE_ID, getLootingBagContainer().items);
        updateTotalCost();
    }

    /**
     * Closing the looting bag and resetting
     */
    public void closeLootbag() {
        player.getPA().sendTabAreaOverlayInterface(0);
        reset();
    }

    public void reset() {
        setWithdrawInterfaceOpen(false);
        setDepositInterfaceOpen(false);
    }

    public void depositAllLootBag() {
        if (!canUseLootingBag()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.Area(1592, 1670, 3659, 3696) && !player.inBank() || player.inWild()) {
            player.sendMessage("You must be at home to do this.");
            return;
        }
        if (getLootingBagContainer().items.isEmpty()) {
            player.sendMessage("You don't have any items in your lootbag.");
            return;
        }
        for (Iterator<LootingBagItem> iterator = getLootingBagContainer().items.iterator(); iterator.hasNext(); ) {
            LootingBagItem item = iterator.next();
            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            player.getItems().addItemToBank(item.getId(), item.getAmount());
            iterator.remove();
        }
        updateItemContainers();
    }

    public boolean handleButton(int buttonId) {
        if (buttonId == 153177 || buttonId == 154022) {
            closeLootbag();
            return true;
        } else if (buttonId == 153179) {
            depositAllLootBag();
            return true;
        }
        return false;
    }

    /**
     * Checks whether or not a player is allowed to configure the looting bag
     */
    public boolean canUseLootingBag() {
        if (/*player.underAttackBy > 0 || player.underAttackBy2 > 0 ||*/ player.inDuelArena() || player.inPcGame()
                || player.inPcBoat() || player.isInJail() || player.getInterfaceEvent().isActive()
                || player.getPA().viewingOtherBank || player.isDead || player.viewingRunePouch) {
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
        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
                MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
                && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
            player.sendMessage("Your actions have declined the duel.");
            duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            return false;
        }

        if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
            player.sendMessage("You must decline the trade.");
            return false;
        }

        if (player.isStuck) {
            player.isStuck = false;
            player.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
            return false;
        }
        return true;
    }

    /**
     * Configuring the inventory of the looting bag on death
     *
     * @param player the player
     * @param type the type
     */
    public void onDeath(Player player, String type) {
        Entity killer = player.getKiller();
        player.getItems().deleteItem(LOOTING_BAG, player.getItems().getItemAmount(LOOTING_BAG));
        player.getItems().deleteItem(LOOTING_BAG_OPEN, player.getItems().getItemAmount(LOOTING_BAG_OPEN));

        PlayerLogging.write(PlayerLogging.LogType.DEATH, player, String.format("Dropped from looting bag %s", getLootingBagContainer().items));
        for (Iterator<LootingBagItem> iterator = getLootingBagContainer().items.iterator(); iterator.hasNext(); ) {
            LootingBagItem item = iterator.next();

            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            if (Objects.equals(type, "PVP")) {
                if (killer != null && killer instanceof Player) {
                    Player playerKiller = (Player) killer;
                    if (playerKiller.getMode().isItemScavengingPermitted()) {
                        Server.itemHandler.createGroundItem(playerKiller, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount(), player.killerId);
                    } else {
                        Server.itemHandler.createUnownedGroundItem(item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
                    }
                }

                iterator.remove();
            }
        }
        updateItemContainers();
        PlayerSave.saveGame(player);
    }

    /**
     * Calculating the price of the loot bag
     */
    public void updateTotalCost() {
        int total = 0;
        for (LootingBagItem item : getLootingBagContainer().items) {
            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            if (ItemDefinition.forId(item.getId()) != null) {
                if (player.debugMessage)
                    player.sendMessage("Name: " + ItemDefinition.forId(item.getId()).getName() + "- Value:" + ItemDefinition.forId(item.getId()).getValue() + " - Amount:" + item.getAmount());
                total += ((ItemDefinition.forId(item.getId()).getValue() * item.getAmount()));
            }
        }
        player.getPA().sendFrame126("Value: " + Misc.insertCommas(total), 39348);
    }

    public LootingBagContainer getLootingBagContainer() {
        return lootingBagContainer;
    }

    public boolean isWithdrawInterfaceOpen() {
        return withdrawInterfaceOpen;
    }

    public void setWithdrawInterfaceOpen(boolean withdrawInterfaceOpen) {
        this.withdrawInterfaceOpen = withdrawInterfaceOpen;
    }

    public boolean isDepositInterfaceOpen() {
        return depositInterfaceOpen;
    }

    public void setDepositInterfaceOpen(boolean depositInterfaceOpen) {
        this.depositInterfaceOpen = depositInterfaceOpen;
    }

    public boolean isSelectDepositAmountInterfaceOpen() {
        return selectDepositAmountInterfaceOpen;
    }

    public void setSelectDepositAmountInterfaceOpen(boolean selectDepositAmountInterfaceOpen) {
        this.selectDepositAmountInterfaceOpen = selectDepositAmountInterfaceOpen;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public LootingBagUseAction getUseAction() {
        return useAction;
    }

    public void setUseAction(LootingBagUseAction useAction) {
        this.useAction = useAction;
    }
}
