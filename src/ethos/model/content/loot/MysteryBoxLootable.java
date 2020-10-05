package ethos.model.content.loot;

import java.util.List;
import java.util.Random;

import ethos.model.items.GameItem;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public abstract class MysteryBoxLootable implements Lootable {

    public abstract int getItemId();

    /**
     * The player object that will be triggering this event
     */
    private Player player;

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public MysteryBoxLootable(Player player) {
        this.player = player;
    }

    /**
     * Can the player open the mystery box
     */
    public boolean canMysteryBox = true;

    /**
     * The prize received
     */
    private int mysteryPrize;

    private int mysteryAmount;

    private int spinNum = 0;

    /**
     * The chance to obtain the item
     */
    private int random;
    private boolean active;
    private final int INTERFACE_ID = 47000;
    private final int ITEM_FRAME = 47101;

    /**
     * The rarity of the reward
     */
    private MysteryBoxRarity rewardRarity;

    public boolean isActive() {
        return active;
    }

    public void draw() {
        openInterface();
        if (spinNum == 0) {
            for (int i=0; i<66; i++){
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                GameItem NotPrize = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity()));
                final int NOT_PRIZE_ID = NotPrize.getId();
                sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID,1);
            }
        } else {
            for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                final int NOT_PRIZE_ID = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity())).getId();
                sendItem(i, (spinNum+1)*50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
            }
        }
        spinNum++;
    }



    public void spin() {
        // Server side checks for spin
        if (!canMysteryBox) {
            player.sendMessage("Please finish your current spin.");
            return;
        }
        if (!player.getItems().playerHasItem(getItemId())) {
            player.sendMessage("You require a super mystery box to do this.");
            return;
        }

        // Delete box
        player.getItems().deleteItem(getItemId(), 1);
        // Initiate spin
        player.sendMessage(":resetBox");
        for (int i=0; i<66; i++){
            player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
        }
        spinNum = 0;
        player.sendMessage(":spin");
        process();
    }

    public void process() {
        // Reset prize
        mysteryPrize = -1;

        mysteryAmount = -1;
        // Can't spin when already in progress
        canMysteryBox = false;
        active = true;
        random = Misc.random(100);
        List<GameItem> itemList = random < 50 ? getLoot().get(MysteryBoxRarity.COMMON.getLootRarity()) : random >= 50
                && random <= 90 ? getLoot().get(MysteryBoxRarity.UNCOMMON.getLootRarity())
                : getLoot().get(MysteryBoxRarity.RARE.getLootRarity());
        rewardRarity = random < 50 ? MysteryBoxRarity.COMMON
                : random >= 50 && random <= 90 ? MysteryBoxRarity.UNCOMMON
                : MysteryBoxRarity.RARE;

        GameItem item = Misc.getRandomItem(itemList);

        mysteryPrize = item.getId();

        mysteryAmount = item.getAmount();

        // Send items to interface
        // Move non-prize items client side if you would like to reduce server load
        if (spinNum == 0) {
            for (int i=0; i<66; i++){
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                GameItem NotPrize =Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity()));
                final int NOT_PRIZE_ID = NotPrize.getId();
                final int NOT_PRIZE_AMOUNT = NotPrize.getAmount();
                sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID,1);
            }
        } else {
            for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
                MysteryBoxRarity notPrizeRarity = MysteryBoxRarity.values()[new Random().nextInt(MysteryBoxRarity.values().length)];
                final int NOT_PRIZE_ID = Misc.getRandomItem(getLoot().get(notPrizeRarity.getLootRarity())).getId();
                sendItem(i, (spinNum+1)*50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
            }
        }
        spinNum++;
    }

    public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID, int amount) {
        if (i == prizeSlot) {
            player.getPA().mysteryBoxItemOnInterface(PRIZE_ID, amount, ITEM_FRAME, i);
        }
        else {
            player.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, amount, ITEM_FRAME, i);
        }
    }

    public void openInterface() {
        player.boxCurrentlyUsing = getItemId();
        // Reset interface
        player.sendMessage(":resetBox");
        for (int i=0; i<66; i++){
            player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
        }
        spinNum = 0;
        // Open
        player.getPA().sendString("Mystery Box", 47002);
        player.getPA().showInterface(INTERFACE_ID);
    }

    public void canMysteryBox() {
        canMysteryBox = true;

    }

    @Override
    public void roll(Player player) {
        if (mysteryPrize == -1) {
            canMysteryBox = true;
            player.getNormalMysteryBox().canMysteryBox();
            player.getUltraMysteryBox().canMysteryBox();
            return;
        }

        player.getItems().addItemUnderAnyCircumstance(mysteryPrize, mysteryAmount);
        active = false;
        player.inDonatorBox = true;

        // Reward message
        if (random >= 89) {
            String name = ItemDefinition.forId(mysteryPrize).getName();
            
            String itemName = ItemDefinition.forId(getItemId()).getName(); 
            PlayerHandler.executeGlobalMessage("[<col=CC0000>" + itemName + "</col>] <col=255>"
                    + Misc.formatPlayerName(player.playerName)
                    + "</col> hit the jackpot and got a <col=CC0000>"+name+"</col>!");
        }

        // Can now spin again
        canMysteryBox = true;
        player.getNormalMysteryBox().canMysteryBox();
        player.getUltraMysteryBox().canMysteryBox();
    }
}
