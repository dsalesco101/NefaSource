package ethos.model.players.packets.npcoptions;

import ethos.Server;
import ethos.model.npcs.NPCDefinitions;
import ethos.model.players.Player;
import ethos.model.players.skills.agility.AgilityHandler;
import ethos.model.players.skills.slayer.SlayerRewardsInterface;
import ethos.model.players.skills.slayer.SlayerRewardsInterfaceData;

/*
 * @author Matt
 * Handles all 4th options on non playable characters.
 */

public class NpcOptionFour {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.rememberNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;
		switch (npcType) {
		case 5293:
		case 5294:
		case 5295:
		case 5296:
		case 8781:
		case 1672:
		case 1673:
		case 1674:
		case 1675:
		case 1676:
		case 1677:
		case 100:
		case 1173:
		case 2790:
		case 2026:
		case 1047:
		case 1680:
		case 2241:
		case 2098:
		case 2084:
		case 891:
		case 690:
		case 970:
		case 975:
		case 5938:
		case 5944:
		case 2919:
		case 448:
		case 414:
		case 406:
		case 481:
		case 419:
		case 421:
		case 3209:
		case 435:
		case 417:
		case 437:
		case 427:
		case 411:
		case 795:
		case 4922:
		case 5129:
		case 3023:
		case 70:
		case 2834:
		case 1545:
		case 2006:
		case 241:
		case 268:
		case 291:
		case 484:
		case 135:
		case 6:
		case 2514:
		case 498:
		case 499:
		case 2841:
		case 2085:
		case 465:
		case 1432:
		case 270:
		case 274:
		case 273:
		case 247:
		case 8030:
		case 8031:
		case 264:
		case 1610:
		case 1611:
		case 1612:
		case 6606: 
		case 6611:
		case 6609:
		case 6615:
		case 6610:
		case 2054:
		case 6619:
		case 6618:
		case 7931:
		case 7932:
		case 7933:
		case 7934:
		case 7935:
		case 7936:
		case 7937:
		case 7938:
		case 7939:
		case 7940:
		case 6593:
		case 6607:
		case 8609:
		case 6342:
		case 2265:
		case 2266:
		case 2267:
		case 3021:
		case 239:
		case 5779:
		case 959:
		case 957:
		case 955:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 319:
		case 415:
		case 7244:
		case 7276:
		case 7278:
		case 7277:
		case 7275:
		case 7274:
		case 7273:
		case 4005:
		case 7268:
		case 2235:
		case 7258:
		case 7279:
		case 7272:
		case 492:
		case 423:
		case 494:
		case 2042:
		case 2043:
		case 2044:
		case 5862:
		case 5890:
		case 7144:
		case 6918:
		case 6914:
		case 6766:
		case 1543:
		case 11:
		case 446:
		case 520:
		case 2844:
		case 2887:
		case 259:
		case 2212:
		case 3139:
		case 2213:
		case 3140:
		case 2211:
		case 2209:
		case 3138:
		case 3160:
		case 2245:
		case 2233:
		case 2234:
		case 2237:
		case 3133:
		case 3137:
		case 2243:
		case 3135:
		case 2244:
		case 2242:
		case 3161:
		case 3134:
		case 3141:
		case 2210:
		case 3166:
		case 3167:
		case 3168:
		case 3169:
			player.getPA().showInterface(39500);
			Server.getDropManager().search(player, NPCDefinitions.get(npcType).getNpcName());
			Server.getDropManager().select(player, 128240);
			break;
		case 5008:
		case 7020:
			player.getDH().sendDialogues(1088, npcType);
			break;
		case 85: //due to some reason it bugging
			player.getPA().showInterface(39500);
			Server.getDropManager().search(player, "ghost");
			Server.getDropManager().select(player, 128240);
			break;
		case 17: //Rug merchant - Sophanem
			player.startAnimation(2262);
			AgilityHandler.delayFade(player, "NONE", 3285, 2815, 0, "You step on the carpet and take off...", "at last you end up in sophanem.", 3);
			break;

		case 2580:
			player.getPA().startTeleport(3039, 4788, 0, "modern", false);
			player.teleAction = -1;
			break;
		case 402:
		case 401:
		case 405:
		case 6797:
		case 7663:
		case 8761:
		case 8623:
		case 5870:
			SlayerRewardsInterface.open(player, SlayerRewardsInterfaceData.Tab.TASK);
			//player.getSlayer().handleInterface("buy");
			break;
			
		case 1501:
			player.getShops().openShop(23);
			break;

		case 315:
			player.getDH().sendDialogues(545, npcType);
			break;
		}
	}

}
