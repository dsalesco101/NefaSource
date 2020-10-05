package ethos.model.shops;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.IntStream;

import ethos.Config;
import ethos.Server;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.wogw.Wogwitems;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemList;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.model.content.achievement.AchievementTier;
import ethos.model.content.achievement.Achievements.Achievement;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;
import ethos.world.ShopHandler;

public class ShopAssistant {

	private Player c;

	public ShopAssistant(Player client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		c.getPA().resetScrollPosition(64015);
		if (c.getItems().playerHasItem(995)) {
			int amountInInventory = c.getItems().getItemAmount(995);
			c.getItems().deleteItem(995, amountInInventory);
			c.sendMessage("Your coins have disappeared because there no need for them.");	
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (!c.getMode().isShopAccessible(ShopID)) {
			c.sendMessage("Your game mode does not permit you to access this shop.");
			c.getPA().closeAllWindows();
			return;
		}
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
			c.sendMessage("You should stop what you are doing before opening a shop.");
			return;
		}
		c.nextChat = 0;
		c.dialogueOptions = 0;
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(64000, 3822);

		switch (ShopID) {
		
		/*case 82:
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID] + " - " + c.getShayPoints() + " Assault Points", 64003);
			break;*/

			default: c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 64003);
		}
		//c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 64003); //3901 (Possibly item container)
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true && PlayerHandler.players[i].myShopId == c.myShopId && i != c.getIndex()) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		// synchronized (c) {
		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		if (ShopID == 3) {
			c.getPA().sendFrame171(0, 64017);
			c.getPA().sendFrame126("bounty hunter points: " + Misc.insertCommas(Integer.toString(c.bountyp)), 64019);
		} else {
			c.getPA().sendFrame171(1, 64017);
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeUnsignedWord(64016);
		c.getOutStream().writeUnsignedWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double TotPrice = 0;

		ItemList itemList = Server.itemHandler.ItemList[ItemID];

		if (itemList != null) {
			ShopValue = itemList.ShopValue;
		}

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	public static int getItemShopValue(int itemId) {
		if (itemId < 0) {
			return 0;
		}

		ItemList itemList = Server.itemHandler.ItemList[itemId];

		if (itemList != null) {
			return (int) itemList.ShopValue;
		}

		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0, removeSlot));
		ShopValue *= 1.00;
		ShopValue = c.getMode().getModifiedShopPrice(c.myShopId, removeId, ShopValue);
		String ShopAdd = "";
		if (c.myShopId == 1) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] pkp.");
			return;
		}
		if (c.myShopId == 2) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": will sell for [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Bounty Hunter points.");
			return;
		}
		if (c.myShopId == 3) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Bounty Hunter points.");
			return;
		}
		if (c.myShopId == 10) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @blu@" + getSpecialItemValue(removeId) + " @bla@] Slayer Points.");
			return;
		}
		if (c.myShopId == 121) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Pvm Points.");
			return;
		}
		if (c.myShopId == 18) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " marks of grace.");
			return;
		}
		if (c.myShopId == 40) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " mage arena points.");
			return;
		}
		if (c.myShopId == 131) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Tournament Points.");
			return;
		}
		if (c.myShopId == 77) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Vote points.");
			return;
		}
		if (c.myShopId == 183 || c.myShopId == 184 || c.myShopId == 113 || c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 6) {		
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs nothing [@gre@FREE@bla@].");
			return;
		}
		if (c.myShopId == 183 || c.myShopId == 184 || c.myShopId == 113 || c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 6) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Blood Money points.");
			return;
		}

		if (c.myShopId == 9) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " Donator points.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		} else if (ShopValue <= 0) {
			ShopAdd = " @bla@(@gre@FREE@bla@)";
		}
		c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + ShopValue + " coins" + ShopAdd);
	}

	public int getSpecialItemValue(int id) {

		switch (c.myShopId) {
		case 1: //BLOOD MONEY SHOP
			switch(id) {
				case 12006: 
					return 1000;
				case 11802:
					return 5000;
				case 11806:
					return 1000;
				case 11804:
				case 11808:
					return 1000;
				case 22296:
					return 2000;
				case 11791:
						return 2500;
				case 12929:
				case 13198:
				case 13196:
						return 3000;
				case 11832:
				case 11834:
						return 1000;
				case 11826:
						return 500;
				case 11828:
				case 11830:
						return 1000;
				case 11785:
					return 2000;
				case 11824:
						return 600;
				case 21012:
						return 2300;
				case 21902:
						return 2600;
				case 4153:
						return 300;
				case 4716:
				case 4753:
				case 4745:
				case 4732:
				case 4724:
				case 4708:
						return 50;
				case 4718:
				case 4726:
				case 4710:
				case 4755:
				case 4747:
				case 4734:
						return 75;
				case 4720:
				case 4722:
				case 4736:
				case 4738:
				case 4712:
				case 4714:
				case 4749:
				case 4751:
				case 4757:
				case 4759:
				case 4728:
				case 4730:
						return 100;
				case 12954:
						return 1000;
				case 12848:
						return 500;
				case 22954:
						return 1000;
				case 13239:
				case 13237:
				case 13235:
						return 1000;
				case 6918:
				case 6916:
				case 6924:
						return 250;
				case 6920:
						return 250;
				case 6914:
						return 550;
				case 6912:
						return 50;
				case 22109:
						return 1000;
				case 12791:
						return 1000;
				case 11924:
				case 11926:
						return 1000;
				case 11335:
				case 3140:
				case 4087:
				case 4585:
				case 1187:
						return 25;
				case 11840:
						return 60;
				case 6889:
						return 1250;
				case 10551:
						return 250;
				case 6570:
						return 250;
				case 12829:
						return 500;
				case 21298:
						return 150;
				case 21301:
				case 21304:
						return 300;
				case 19547:
				case 19553:
						return 1500;
				case 6585:
						return 1000;
				case 2577:
				case 2581:
						return 200;
				case 12596:
						return 350;
				case 11235:
						return 300;
				case 7509:
						return 700;
				case 4212:
				case 4224:
						return 400;
				case 22975:
						return 3000;
				case 12853:
						return 250;
				case 1249:
						return 50;
				case 11770:
				case 11771:
				case 11772:
				case 11773:
						return 250;
				case 10547:
				case 10548:
				case 10549:
				case 10550:						
						return 50;
				case 22951:
				case 23389:
						return 100;
				case 6731:
				case 6733:
				case 6735:
				case 6737:
						return 125;
				case 12695:
				case 12625:
				case 12913:
						return 2;
				case 22804:
				case 19484:
				case 11230:
				case 21905:
				case 11212:
				case 11937:
				case 13442:
				case 3145:
				case 12934:
				case 2996:
						return 1;
			
			}
			break;
		case 2: //BLOOD MONEY SHOP
			switch(id) {
			case 12746:
					return 100;
			case 12748:
					return 200;
			case 12749:
					return 400;
			case 12750:
					return 800;
			case 12751:
					return 1500;
			case 12752:
					return 2400;
			case 12753:
					return 3500;
			case 12754:
					return 5000;
			case 12755:
					return 7000;
			case 12756:
					return 10000;
			
			}
				break;
		case 3: //BOUNTY HUNTER SHOP
			switch(id) {
			case 23995:
					return 35000;
			case 22324:
					return 35000;
			case 20784:
					return 40000;
			case 21003:
					return 105000;
			case 11802:
					return 7000;
			case 21015:
					return 30000;
			case 21006:
					return 20000;
			case 22326:
					return 20000;
			case 22327:
			case 22328:
					return 25000;
			case 21012:
					return 6000;
			case 13072:
			case 13073:
					return 8500;
			case 22322:
					return 9000;
			case 19564:
			case 21000:
					return 6500;
			case 22981:
					return 5000;
			case 21018:
					return 20000;
			case 21021:
			case 21024:
					return 25000;
			case 20517:
					return 3000;
			case 20520:
			case 20595:
					return 6000;
			case 12846:
					return 5000;
			case 12806:
					return 5500;
			case 12809:
					return 3000;
			case 12849:
					return 1000;
			case 12757:
			case 12759:
			case 12761:
			case 12763:
				
			case 12526:
			case 20062:
			case 20065:
			case 20068:
			case 20071:
			case 20074:
			case 20077:
			case 20143:
			case 22246:
					return 4100;
			case 11941:
					return 525;
			case 683:
					return 1;
					
			}
				break;
		case 10: //slayer Shop
			switch (id) {
				case 6924:
				case 6922:
				case 6920:
				case 6918:
				case 6916:
					return 10;
				case 4081:
					return 475;
				case 13116:
					return 60;
				case 4170:
					return 35;
				case 13226:
					return 15;
				case 4166:
				case 4164:
				case 4168:
				case 4551:
					return 15;
				case 6121:
					return 370;
				case 10551:
					return 400;
				case 10548:
					return 100;
				case 7462:
					return 200;
				case 7509:
					return 65;
				case 405:
					return 275;
				case 23943:
					return 300;
			}
			break;
		case 40: //kolodions shop
			switch (id) {
				case 6718:
					return 0;
			}
			break;
		
		case 121: //pvm Shop
			switch (id) {
				case 23246:
					return 1400;
				case 10551:
					return 3200;
				case 10548:
					return 630;
				case 10549:
					return 630;
				case 10547:
					return 630;
				case 10555:
					return 250;
				case 10553:
					return 150;
				case 10550:
					return 300;
				case 11730:
					return 850;
				case 11824:
					return 8000;
				case 2572:
					return 13000;
				case 6746:
					return 4000;
				case 12769:
				case 12771:
					return 15500;
				case 1409:
					return 5000;
				case 19564:
					return 2000;
				case 6889:
					return 9000;
				case 6914:
					return 5000;
				case 3842:
				case 12610:
				case 12612:
					return 500;
				case 989:
					return 1500;
				case 11212:
				case 11230:
					return 21;
				case 19484:
					return 71;
				case 12596:
					return 7200;
				case 405:
					return 750;
				case 7462:
					return 3500;
				case 20143:
				case 12526:
				case 20062:
				case 22246:
				case 20065:
					return 1800;
				case 20068:
					return 2100;
				case 20074:
				case 20077:
					return 1100;
				case 20071:
					return 1400;
				case 11128:
					return 2300;
				case 4209:
					return 630;
			}
			break;
	case 18: //BOUNTY HUNTER SHOP
			switch(id) {
		case 11850:
			return 35;
		case 11852:
			return 40;
		case 11854:
			return 55;
		case 11856:
			return 60;
		case 11858:
			return 30;
		case 11860:
			return 40;
		case 12792:
			return 15;
		case 12641:
			return 10;
			}
			break;
	case 26: //THIEVING STORE
		switch(id) {
	case 1893:
		return 1;
	case 712:
		return 3;
	case 2961:
		return 5;
	case 1613:
		return 7;
	case 1993:
		return 10;
		}
		break;
	case 131: //tournament Shop
		switch (id) {
			case 22114:
				return 20;
			case 23351:
				return 50;
			case 10724:
			case 10725:
			case 10726:
			case 10727:
			case 10728:
				return 15;
			case 23389:
				return 5;
			case 23206:
				return 10;
			case 23101:
				return 5;
			case 23099:
				return 3;
			case 23097:
			case 23095:
				return 10;
			case 23093:
				return 4;
			case 23091:
				return 5;
			case 8132:
			case 10591:
				return 125;
			case 21298:
				return 15;
			case 21301:
			case 21304:
				return 20;

		}
		break;
		case 9: //Donator Point Shop
			switch (id) {
			case 21295:
					return 80;
			case 962:
					return 45;
			case 11862:
				return 70;
			case 1050:
				return 35;
			case 13343:
				return 50;
			case 4084:
				return 30;
			case 1419:
				return 15;
			case 6199:
					return 5;
			case 6828:
					return 10;
			case 13346:
				return 15;
			case 21259:
					return 10;
			case 12785:
					return 60;
			case 21034:
			case 21079:
					return 25;
			case 15098:
					return 60;
				
			}
			break;
		case 77: //Vote Shop
			switch (id) {
				case 6570:
					return 6;
				case 7509:
					return 10;
				case 11739:
					return 1;
				case 2572:
					return 25;
				case 2528:
					return 6;
				case 23933:
					return 1;
				case 11936:
					return 3;
				case 11920:
					return 60;
				case 12797:
					return 55;
				case 6739:
					return 25;
				case 2577:
					return 35;
				case 12526:
					return 25;
				case 20211:
				case 20214:
				case 20217:
					return 20;
				case 20050:
					return 30;
				case 13221:
					return 100;
				case 20756:
					return 50;
				case 21028:
					return 60;
				case 13241:
				case 13242:
				case 13243:
					return 100;
				case 6666:
					return 15;
				case 12783:
					return 400;
				case 12639:
				case 12637:
				case 12638:
					return 45;
				case 20836:
					return 85;
				case 5608:
					return 150;
				case 12600:
					return 30;
				case 19941:
					return 85;
				case 20056:
					return 150;
				case 10507:
					return 50;
				case 10723:
					return 120;
				case 21439:
					return 45;
				case 13148:
					return 15;
				case 23677:
					return 55;
				case 23294:
				case 23285:
				case 23288:
				case 23291:
					return 10;
			}
			break;
			
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		boolean CANNOT_SELL = IntStream.of(Config.ITEM_SELLABLE).anyMatch(sellable -> sellable == removeId);
		if (c.myShopId != 116 && c.myShopId != 115) {
			if (CANNOT_SELL) {
				c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		if (IsIn == false) {
			c.sendMessage("You can't sell that item to this store.");
		} else {
			int ShopValue = 0;
			String ShopAdd = "";
			if (c.myShopId != 26) {
				ShopValue *= 0.75;
			}
			if (c.myShopId == 83) {
				int i = 0;
				for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
					if (t.getItemId() == removeId) {
						ShopValue = (int) Math.floor(t.getItemWorth());
						i++;
					}
				}
				if (i == 0) {
					c.sendMessage("You can't sell this item to this store.");
					return;
				}
			} else {
				ShopValue = (int) Math.floor(getItemShopValue(removeId, 1, removeSlot));
			}
			if (c.myShopId == 3) {
				c.sendMessage("You can't sell items to this store.");
				return;
			}
			if (c.myShopId != 26) {
				c.sendMessage("You can't sell items in this store.");
				return;
			}
			if (c.myShopId == 44) {
				if (!Item.getItemName(removeId).contains("head")) {
					c.sendMessage("You cannot sell this to the slayer shop.");
					return;
				} else {
					c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " slayer points" + ShopAdd);
				}
			} else if (c.myShopId == 18) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " marks of grace" + ShopAdd);
			} else if (c.myShopId == 26) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ((int) Math.ceil((getSpecialItemValue(removeId) * 1)) + " blood money." + ShopAdd));
			} else if (c.myShopId == 172 || c.myShopId == 173) {
				c.sendMessage("You cannot sell items to this shop.");
			} else if (c.myShopId == 116) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ((int) Math.ceil((getSpecialItemValue(removeId) * 0.60)) + " blood money"));
			}  else if (c.myShopId == 29) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " tokkul" + ShopAdd);
			} else {
				ShopValue *= 0.75;
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " blood money" + ShopAdd);
			}
		}
	}

	/**
	 * Selling items back to a store
	 * @param itemID
	 * 					itemID that is being sold
	 * @param fromSlot
	 * 					fromSlot the item currently is located in
	 * @param amount
	 * 					amount that is being sold
	 * @return
	 * 					true is player is allowed to sell back to the store,
	 * 					else false
	 */
	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return false;
		}
		if (!c.getMode().isItemSellable(c.myShopId, itemID)) {
			c.sendMessage("Your game mode does not permit you to sell this item to the shop.");
			return false;
		}
		if (c.myShopId != 26) {
			c.sendMessage("You can't sell items in this store.");
			return false;
		}
		if (c.myShopId != 115) {
			if (itemID == 13307) {
				c.sendMessage("You can't sell this item.");
				return false;
			}
		}

		switch (c.myShopId) {
		case 0:
		case 2:
		case 1:
		case 3:
		case 4:
			c.sendMessage("You cannot sell items to this shop.");
			return false;
		}
		boolean CANNOT_SELL = IntStream.of(Config.ITEM_SELLABLE).anyMatch(sellable -> sellable == itemID);
		if (c.myShopId != 116 && c.myShopId != 115) {
			if (CANNOT_SELL) {
				c.sendMessage("You can't sell " + ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell that item to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot] && (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(c.playerItems[fromSlot] - 1)] == true)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID) && Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == false
					&& Item.itemStackable[(c.playerItems[fromSlot] - 1)] == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			int TotPrice3 = 0;
			int TotPrice4 = 0;
			// int Overstock;
			//for (int i = amount; i > 0; i--) {
			TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1, fromSlot));
			TotPrice3 = (int) Math.floor(getSpecialItemValue(itemID));
			if (c.myShopId == 83) {
				int i = 0;
				for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
					if (t.getItemId() == itemID) {
						TotPrice4 = (int) Math.floor(t.getItemWorth());
						i++;
					}
				}
				if (i == 0) {
					c.sendMessage("You can't sell that item to this store.");
					return false;
				}
			}
			if (c.myShopId != 26) {
				TotPrice2 *= 0.75;
			}
			if (c.myShopId == 26) {
				TotPrice3 *= 1;
			}
			TotPrice2 = TotPrice2 * amount;
			TotPrice4 = TotPrice4 * amount;
			if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(13307)) {
				if ((Item.itemStackable[itemID] || Item.itemIsNote[itemID]) && c.getItems().playerHasItem(itemID, amount)) {
					logShop("sell", itemID, amount);
					c.getItems().deleteItemNoSave(itemID,c.getItems().getItemSlot(itemID),amount);
					if (c.myShopId != 12 && c.myShopId != 29 && c.myShopId != 44 && c.myShopId != 18  && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {
						c.getItems().addItem(13307, TotPrice2);
						logShop("received", 13307, TotPrice2);
					} else if (c.myShopId == 29) {
						c.getItems().addItem(6529, TotPrice2);
						logShop("received", 6529, TotPrice2);
					} else if (c.myShopId == 26) {
						c.getItems().addItem(13307, getSpecialItemValue(itemID) * amount);
						logShop("received", 13307, getSpecialItemValue(itemID) * amount);
					} else if (c.myShopId == 18) {
						c.getItems().addItem(11849, TotPrice2);
						logShop("received", 11849, TotPrice2);
					} else if (c.myShopId == 83) {
						c.getItems().addItem(13307, TotPrice4);
						logShop("received", 13307, TotPrice4);
						addShopItem(itemID, amount);
					}  else if (c.myShopId == 116) {
						c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.30));
						logShop("received", 13307, (int) Math.ceil(TotPrice3 *= 0.30));
					} else if (c.myShopId == 44) {
						if (!Item.getItemName(itemID).contains("head")) {
							return false;
						} else {
							c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
							c.refreshQuestTab(5);
						}
					}
					//addShopItem(itemID, amount);
					ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
					c.getItems().resetItems(3823);
					resetShop(c.myShopId);
					updatePlayerShop();
					return false;
				} else {
					if (c.myShopId != 12 && c.myShopId != 29 && c.myShopId != 44 && c.myShopId != 18 && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {
						c.getItems().addItem(13307, TotPrice2);
						logShop("received", 13307, TotPrice2);
					} else if (c.myShopId == 29) {
						c.getItems().addItem(6529, TotPrice2);
						logShop("received", 13307, TotPrice2);
					} else if (c.myShopId == 18) {
						c.getItems().addItem(11849, TotPrice2);
						logShop("received", 13307, TotPrice2);
					} else if (c.myShopId == 83) {
						c.getItems().addItem(13307, TotPrice4);
						logShop("received", 13307, TotPrice4);
						addShopItem(itemID, amount);
					} else if (c.myShopId == 116) {
						c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.60));
						logShop("received", 13307, (int) Math.ceil(TotPrice3 *= 0.60));
					} else if (c.myShopId == 44) {
						if (!Item.getItemName(itemID).contains("head")) {
							return false;
						} else {
							c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
							c.refreshQuestTab(5);
						}
					}

					if (Item.itemIsNote[itemID] == false) {
						logShop("sold", itemID, amount);
						c.getItems().deleteItem2(itemID, amount);
					}
					// addShopItem(itemID, 1);
				}
			} else {
				c.sendMessage("You don't have enough space in your inventory.");
				c.getItems().resetItems(3823);
				return false;
			}
			//}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			PlayerSave.saveGame(c);
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}

		if (Item.itemIsNote[itemID] == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Buying item(s) from a store
	 * @param itemID
	 * 					itemID that the player is buying
	 * @param fromSlot
	 * 					fromSlot the items is currently located in
	 * @param amount
	 * 					amount of items the player is buying
	 * @return
	 * 					true if the player is allowed to buy the item(s),
	 * 					else false
	 */
	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			if (c.debugMessage)
				c.sendMessage("");
			return false;
		}
		if (!c.getMode().isItemPurchasable(c.myShopId, itemID)) {
			c.sendMessage("Your game mode does not allow you to buy this item.");
			return false;
		}
		if (c.myShopId == 83) {
			c.sendMessage("You cannot buy items from this shop.");
			return false;
		}
		if (c.myShopId == 2) {
			c.sendMessage("You cannot buy from this shop, only sell.");
			return false;
		}
		/*
		 * ACHIEVMENT LOST N FOUND AREA
		 */
		if (c.myShopId == 178) {
			if (itemID == 10941 || itemID == 10933 && c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Woodcutting_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 10939 && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_CHOPPER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 10940 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_CHOPPER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13258 && !c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Fishing_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13259 || itemID == 13261 && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_FISHER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13260 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_FISHER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 12013 || itemID == 12016 && !c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Mining_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 12014 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_MINER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 12015 && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_MINER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13646 && itemID == 13644 && !c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Farming_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13640 && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_FARMER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 13642 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_FARMER.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 20710 && !c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Firemaking_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 20706 || itemID == 20704  && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_PYRO.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 20708 || itemID == 20712  && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_PYRO.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 5556 || itemID == 5557  && !c.getAchievements().isComplete(AchievementTier.TIER_1.ordinal(), Achievement.Theiving_Task_I.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 5555 || itemID == 5553  && !c.getAchievements().isComplete(AchievementTier.TIER_2.ordinal(), Achievement.INTERMEDIATE_THIEF.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 5554 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.EXPERT_THIEF.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		if (c.myShopId == 178) {
			if (itemID == 20164 || itemID == 6666 && !c.getAchievements().isComplete(AchievementTier.TIER_3.ordinal(), Achievement.CLUE_CHAMP.getId())) {
				c.sendMessage("You have not yet unlocked this item.");
				return false;
			}
		}
		/*
		 * ACHIEVMENT LOST N FOUND AREA ENDING
		 */
		if (c.myShopId == 81) {
			if (!c.getDiaryManager().getWildernessDiary().hasDoneHard()) {
				c.sendMessage("You must have completed wilderness hard diaries to purchase this.");
				return false;
			}
		}
		if (c.myShopId == 115) {
			if (!c.inClanWarsSafe()) {
				System.out.println("[District] " + c.playerName + " Attempting to purchase from free shop outside disitrict.");
				return false;
			}
			if (Item.itemStackable[itemID]) {
				amount = 100000;
			}
			if (itemID == 12934) {
				amount = 10000;
			}
		}
		if (c.myShopId == 116) {
			if (!c.inClanWarsSafe()) {
				System.out.println("[District] " + c.playerName + " Attempting to purchase from bm shop outside disitrict.");
				return false;
			}
		}
		if (c.myShopId == 77) {
			if ((itemID == 2528) && (c.getMode().isIronman() || c.getMode().isUltimateIronman() || c.getMode().isHCIronman() || c.getMode().isOsrs())) {
				c.sendMessage("You cannot buy an experience lamp on this mode.");
				return false;
			}
		}
		if (itemID == LootingBag.LOOTING_BAG) {
			if (c.getItems().getItemCount(LootingBag.LOOTING_BAG, true) > 0
					|| c.getItems().getItemCount(LootingBag.LOOTING_BAG_OPEN, true) > 0) {
				c.sendMessage("It seems like you already have one of these.");
				return false;
			}
		}
		if (itemID == 10941) {
			if (c.getItems().getItemCount(10941, true) > 0) {
				c.sendMessage("It seems like you already have one of these.");
				return false;
			}
		}
		/**
		 * Slayer shop
		 */
		if (c.myShopId == 44) {
			if (Item.getItemName(itemID).contains("head")) {
				c.sendMessage("This product cannot be purchased.");
				return false;
			}
		}
		/**
		 * Avaas
		 */
		if (c.myShopId == 19) {
			if (itemID == 10498) {
				if (!c.getItems().playerHasItem(886, 75)) {
					c.sendMessage("You must have 75 steel arrows to exchange for this attractor");
					return false;
				}
				c.getItems().deleteItem(886, 775);
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.ATTRACTOR);
			}
		}
		/**
		 * RFD Shop
		 */
		if (c.myShopId == 14) {
			if (itemID == 7458 && c.rfdGloves < 1) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7459 && c.rfdGloves < 2) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7460 && c.rfdGloves < 3) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7461 && c.rfdGloves < 5) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7462) {
				if (c.rfdGloves < 6) {
					c.sendMessage("You are not eligible to buy these.");
					return false;
				}
			}
		}
		if (c.myShopId == 17) {
			skillBuy(itemID);
			return false;
		}
		if (c.myShopId == 179) {
			millBuy(itemID);
			return false;
		}
		if (!shopSellsItem(itemID))
			return false;
		if (amount > 0) {
			if (c.myShopId != 115) {
				if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
					amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
				}
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;

			switch (c.myShopId) {
				case 1: //blood money shop
				case 2: //bounty hunter shop
				case 3:
				case 9:
				case 26:
				case 10:
				case 18:
				case 77:
				case 121:
				case 131:
				case 40:
				case 113:
				case 4:
				case 5:
				case 6:
				case 183:
				case 184:
					handleOtherShop(itemID, amount);
					return false;
			}
			TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
			TotPrice2 = c.getMode().getModifiedShopPrice(c.myShopId, itemID, TotPrice2);
			Slot = c.getItems().getItemSlot(13307);
			Slot1 = c.getItems().getItemSlot(6529);
			if (TotPrice2 <= 1) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
				TotPrice2 *= 1.66;
			}
			if (c.myShopId == 115) {
				TotPrice2 = -1;
			}
			if(c.myShopId==124 && c.amDonated>=150 && itemID == 299){
				TotPrice2=0;
			}

			if (Item.itemStackable[itemID]) {
				if (c.myShopId != 29) {
					if (!c.getItems().playerHasItem(13307, TotPrice2 * amount)) {
						int coinAmount = c.getItems().getItemAmount(13307);
						int amountThatCanBeBought = (int) Math.floor(coinAmount / TotPrice2);
						if (amountThatCanBeBought > 0) {
							amount = amountThatCanBeBought;
						}
						c.sendMessage("You don't have enough blood money.");
					}

					if (c.getItems().playerHasItem(13307, TotPrice2 * amount)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(13307, TotPrice2 * amount);
							c.getItems().addItem(itemID, amount);
							logShop("bought", itemID, amount);
							if (c.myShopId != 115) {
								//ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							c.getItems().resetItems(3823);
							return false;
						}
					} else {
						c.sendMessage("You don't have enough blood money.");
						c.getItems().resetItems(3823);
						return false;
					}
				}
			} else if (c.myShopId == 29) {
				if (c.getRechargeItems().hasItem(11136)) {
					TotPrice2 = (int) (TotPrice2 * 0.95);
				}
				if (c.getRechargeItems().hasItem(11138)) {
					TotPrice2 = (int) (TotPrice2 * 0.9);
				}
				if (c.getRechargeItems().hasItem(11140)) {
					TotPrice2 = (int) (TotPrice2 * 0.85);
				}
				if (c.getRechargeItems().hasItem(13103)) {
					TotPrice2 = (int) (TotPrice2 * 0.75);
				}
				if (c.playerItemsN[Slot1] >= TotPrice2 * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.getItems().deleteItem(6529, TotPrice2 * amount);
						c.getItems().addItem(itemID, amount);
						logShop("bought", itemID, amount);
						ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
						ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
						if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
							ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
						}
					} else {
						c.sendMessage("You don't have enough space in your inventory.");
						c.getItems().resetItems(3823);
						return false;
					}
				} else {
					c.sendMessage("You don't have enough tokkul.");
					c.getItems().resetItems(3823);
					return false;
				}
			} else {
				int boughtAmount = 0;


				// Iterate and buy each single item individually
				for (int i = amount; i > 0; i--) {
					if (c.myShopId == 115) {
						TotPrice2 = -1;
					}
					if (Slot == -1 && c.myShopId != 29 && c.myShopId != 115) {
						c.sendMessage("You don't have enough blood money.");
						return false;
					}
					if (Slot1 == -1 && (c.myShopId == 29)) {
						c.sendMessage("You don't have enough tokkul.");
						return false;
					}

					// not tokkul
					if (c.myShopId != 29) {
						if (c.getItems().playerHasItem(13307, TotPrice2)) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(13307, c.getItems().getItemSlot(13307), TotPrice2);
								c.getItems().addItem(itemID, 1);
								boughtAmount++;
								if (c.myShopId == 6) {	
										if (c.getRechargeItems().useItem(2, 1)) {

										} else {
											c.sendMessage("You have already purchased 150 battlestaves today.");
											return false;
										}
									}
								//if (c.myShopId != 115) {
									//ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								//}
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().resetItems(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough blood money.");
							c.getItems().resetItems(3823);
							return false;
						}
					}

					// tokkul
					if (c.myShopId == 29) {
						if (c.getRechargeItems().hasItem(11136)) {
							TotPrice2 = (int) (TotPrice2 * 0.95);
						}
						if (c.getRechargeItems().hasItem(11138)) {
							TotPrice2 = (int) (TotPrice2 * 0.9);
						}
						if (c.getRechargeItems().hasItem(11140)) {
							TotPrice2 = (int) (TotPrice2 * 0.85);
						}
						if (c.getRechargeItems().hasItem(13103)) {
							TotPrice2 = (int) (TotPrice2 * 0.75);
						}
						if (c.playerItemsN[Slot1] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().resetItems(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough tokkul.");
							c.getItems().resetItems(3823);
							return false;
						}
					}
				}



				if (boughtAmount > 0) {
					logShop("bought", itemID, boughtAmount);
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void logShop(String qualifier, int itemId, int amount) {
		PlayerLogging.write(PlayerLogging.LogType.SHOP, c, String.format("%s %s shop id=%d name=[%s]", qualifier, new GameItem(itemId, amount),
				c.myShopId, ShopHandler.ShopName[c.myShopId]));
	}

	/**
	 * Special currency stores
	 * @param itemID
	 * 					itemID that is being bought
	 * @param amount
	 * 					amount that is being bought
	 */
	public void handleOtherShop(int itemID, int amount) {
		if (amount <= 0) {
			if (c.myShopId == 172 || c.myShopId == 173) {
				c.sendMessage("This item cannot be bought, its on showcase only.");
				return;
			}
			c.sendMessage("You need to buy atleast one or more of this item.");
			return;
		}
		if (!c.getItems().isStackable(itemID)) {
			if (amount > c.getItems().freeSlots()) {
				amount = c.getItems().freeSlots();
			}
		}
		if (c.myShopId == 40) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getArenaPoints() < itemValue) {
				c.sendMessage("You do not have enough arena points to buy this from the shop.");
				return;
			}
			c.setArenaPoints(c.getArenaPoints() - itemValue);
			c.refreshQuestTab(4);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 18) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(11849, itemValue)) {
				c.sendMessage("You do not have enough marks of grace to purchase this.");
				return;
			}
			c.getItems().deleteItem(11849, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 26) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(13307, getSpecialItemValue(itemID) * amount)) {
				c.sendMessage("You do not have enough blood money to purchase this.");
				return;
			}
			c.getItems().deleteItem(13307, getSpecialItemValue(itemID) * amount);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			logShop("bought", itemID, amount);
			return;
		}
		if (c.myShopId == 1) {
			if (c.pkp >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pkp -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough pkp to buy this item.");
			}
		} else if (c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 6 || c.myShopId == 183 || c.myShopId == 184 || c.myShopId == 113) {
					if (c.getItems().freeSlots() > 0) {
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
						logShop("bought", itemID, amount);
				} else {
					c.sendMessage("You do not have enough space.");
				}
		} else if (c.myShopId == 2) {
				if (c.bountyp >= getSpecialItemValue(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.bountyp -= getSpecialItemValue(itemID) * amount;
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
						logShop("bought", itemID, amount);
					}
				} else {
					c.sendMessage("You cannot buy emblems, you cannot sell..");
				}
		} else if (c.myShopId == 3) {
			if (c.bountyp >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.bountyp -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough bounty hunter points to buy this item.");
			}
		} else if (c.myShopId == 121) {
			if (c.pvmp >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pvmp -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(1);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough Pvm Points to buy this item.");
			}
		} else if (c.myShopId == 131) {
			if (c.tPoint >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.tPoint -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(1);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough Tournament Points to buy this item.");
			}
		} else if (c.myShopId == 10) {
			if (c.getSlayer().getPoints() >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayer().setPoints(c.getSlayer().getPoints() - (getSpecialItemValue(itemID) * amount));
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					c.refreshQuestTab(5);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 77) {
			if (c.votePoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(1);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		} else if (c.myShopId == 9) {
			if (c.donatorPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.donatorPoints -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(5);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					logShop("bought", itemID, amount);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 17;
		setupSkillCapes(capes, get99Count());
	}
	public void openMillCape() {
		int capes = 23;
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 179;
		c.getShops().openShop(179);
		//setupMillCapes(capes, 23);
	}
	public void setupSkillCapes(int capes, int capes2) {
		c.getPA().sendFrame171(1, 28050);
		c.getPA().sendFrame171(1, 28053);
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 17;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeUnsignedWord(3900);
		c.getOutStream().writeUnsignedWord(TotalItems);
		for (int i = 0; i < 22; i++) {
			if (c.getLevelForXP(c.playerXP[i]) < 99)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public void setupMillCapes(int capes, int capes2) {
		c.getPA().sendFrame171(1, 28050);
		c.getPA().sendFrame171(1, 28053);
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 179;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("200m Cape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeUnsignedWord(3900);
		c.getOutStream().writeUnsignedWord(TotalItems);
		for (int i = 0; i < 22; i++) {
			if (c.playerXP[i] >= 200000000)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(millCapes[i]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}
	/*
	 * public int[][] skillCapes = {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680 ,4359,2682},{3,2701,4341,2703 },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733 },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730 },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727}, {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361, 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */

	
	public int getMillCount() {
		int count = 0;
		for (int j = 0; j < 22; j++) {
			if (c.playerXP[j] >= 200000000) {
				count++;
			}
		}

		return count;
	}


	public int[] millCapes = { 33033, 33034, 33035, 33036, 33037, 33038, 33039, 33040, 33041, 33042, 33043, 33044, 33045, 33046, 33047, 33048, 33049, 33050, 33051, 33052, 33053, 33054, 33055 };

	public void millBuy(int item) {
	int millcapeskill = (item - 33033);
		if (c.getItems().freeSlots() > 1) {
			if (c.getItems().playerHasItem(13307, 10000)) {
				if (c.playerXP[millcapeskill] >= 200000000) {
					c.getItems().deleteItem(13307, c.getItems().getItemSlot(13307), 10000);
					c.getItems().addItem(item, 1);
						} else {
							c.sendMessage("You must have 200m XP in the skill of the cape you're trying to buy.");
						}
					} else {
						c.getDH().sendStatement("You need 10k blood money to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}


			}

	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < 22; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}


	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(13307, c.getItems().getItemSlot(13307), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void openVoid() {
	}

	public void buyVoid(int item) {
	}

}
