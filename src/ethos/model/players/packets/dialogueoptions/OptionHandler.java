package ethos.model.players.packets.dialogueoptions;

import ethos.model.content.lootbag.LootingBag;
import ethos.model.players.Player;

public class OptionHandler {

    public static void handleOptions(Player player, int option) {
        switch (player.dialogueAction) {
            case LootingBag.ITEM_ON_ITEM_DIALOGUE_ID:
                if (option == 4) {
                    player.getPA().sendEnterAmount(LootingBag.DEPOSIT_INTERFACE_ID);
                } else {
                    player.getLootingBag().handleClickItem(player.getLootingBag().getSelectedItem(),
                            option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : Integer.MAX_VALUE);
                }
                break;
            case LootingBag.OPTIONS_DIALOGUE_ID:
                player.getLootingBag().setUseAction(LootingBag.LootingBagUseAction.values()[option - 1]);
                player.getPA().closeAllWindows();
                break;
        }
    }

}
