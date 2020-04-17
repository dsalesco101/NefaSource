package ethos.model.players.packets.ExchangeSystem;

import ethos.model.content.eventcalendar.EventChallenge;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class FireOfExchange {

		public static void exchangeOfFire(Player c, int objectID, int objectX, int objectY, int itemId) {
			c.turnPlayerTo(objectX, objectY);
			c.objectYOffset = 5;
			c.objectXOffset = 5;
			c.objectDistance = 5;	
		    switch (itemId) {  

			case 4722://barrows start
			case 4720:
			case 4716:
			case 4718:
			case 4714:
			case 4712:
			case 4708:
			case 4710:
			case 4736:
			case 4738:
			case 4732:
			case 4734:
			case 4753:
			case 4755:
			case 4757:
			case 4759:
			case 4745:
			case 4747:
			case 4749:
			case 4751:
			case 4724:
			case 4726:
			case 4728:
			case 4730:// all barrows complete
			case 11836://bandos boots	
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=400;
				c.sendMessage("You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " for @red@400@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 400;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 400) ;
			break;
			case 11840://dragon boots
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=200;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@200@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 200;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 200) ;
			break;
			case 2577://ranger boots
			case 6585://fury
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=2000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@2000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 2000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 2000) ;
			break;
			case 4151://whip
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=750;
				c.sendMessage("You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " for @red@750@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 750;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 750) ;
			break;
			case 6737://b ring
			case 6733://archer ring
			case 6731://seers ring
			case 12603:
			case 12605:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=1850;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@1850@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 1850;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 1850) ;
				break;
			case 21902:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=7000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@7000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 7000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 7000) ;
				break;
			case 11834://bcp
			case 11832://tassets
			case 11826://army helm
			case 11828://army plate
			case 11830://arma leg
			case 13239:// primordials ect + 
			case 13237://pegasion
			case 13235://eternal
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=7500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@7500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 7500;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 7500) ;
				break;
			case 13576://d warhammer
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=14000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@14000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 14000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 14000) ;
				break;
			case 20784://claws
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=17000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@17000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 17000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 17000) ;
				break;
			case 11802://ags
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=12000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@12000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 12000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 12000) ;
				break;
			case 13265://abby dagger
			case 11808://zgs		
			case 11804://bgs	
			case 11806://sgs
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 5000) ;
				break;
			case 13263://bludgon			
			case 19552://zenyte brace
			case 19547://anguish
			case 19553://torture
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=8500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@8500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 8500;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 8500) ;
				break;
			case 12002: //occult
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=800;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@800@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 800;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 800) ;
				break;
			case 12809://sara blessed
			case 12006://tent whip
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 5000) ;
				break;
			case 11284: //dfs
			case 11283://dfs
			case 19478://light ballista
			case 12851:
			case 12852:
			case 12853://amulet of damned
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=6500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 6500;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 6500) ;
				break;
			case 19481://heavy ballista
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=13000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@13000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 13000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 13000) ;
				break;
			case 12821://spectral
			case 12825://arcane
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=25000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@25000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 25000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 25000) ;
				break;
			case 12817://ely
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=45000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@45000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 45000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 45000) ;
				break;
			case 11785://arma crossbow
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=6500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 6500;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 6500) ;
				break;
			case 21012://dhcb
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=9000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@9000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 9000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 9000) ;
				break;
			case 12924://blowpipe
			case 12926:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=20000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@20000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 20000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 20000) ;
				break;
			case 11770://seers i
			case 11771://archer i
			case 11772://warrior i
			case 11773://b ring i
			case 12691://tyrannical ring i
			case 12692://tres ring (i)
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 5000) ;
				break;
			case 20997://t bow
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=100000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@100000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 100000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 100000) ;
				break;
			case 12806://malediction
			case 12807://odium
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
			c.getItems().deleteItem(itemId, 1);
			c.exchangeP+=6000;
			c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6000@blu@ Exchange Points!");
			Player.FIRE_OF_EXCHANGE = 6000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 6000) ;
			break;
			case 21006:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=10000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@10000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 10000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 10000) ;
				break;
			case 22322://avernic
			case 22477:
			case 21015://dihns bulwark
			case 21003://elder maul
			case 13196://tanz helm
			case 12929://serp helm
			case 13198://magma helm
			case 12902: //toxic staff of the dead
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=9000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@9000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 9000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 9000) ;
				break;
			case 20517://elder top
			case 20520://elder robe
			case 20595://elder hood
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=1200;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@1200@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 1200;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 1200) ;
				break;
			case 20095://ankou start
			case 20098:
			case 20101:
			case 20104:
			case 20107://ankou end
			case 20080://mummy start
			case 20083:
			case 20086:
			case 20089:
			case 20092://mummy end
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=4750;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@4750@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 4750;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 4750) ;
				break;
			case 21018:
			case 21021:
			case 21024:
			case 22326:
			case 22327:
			case 22328:
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=25000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@25000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 25000;
				c.getEventCalendar().progress(EventChallenge.GAIN_X_EXCHANGE_POINTS, 25000) ;
				break;
			default:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.sendMessage("@red@You cannot exchange @blu@" + ItemAssistant.getItemName(itemId) + " for @red@ Exchange Points.");
				break;
		    }
		    if (Player.FIRE_OF_EXCHANGE >= 100_000) {
				PlayerHandler.executeGlobalMessage("@bla@[@red@FIRE OF EXCHANGE@bla@]@blu@ Has just consumed another @red@100,000@blu@ exchange points!");
				Player.FIRE_OF_EXCHANGE = 0;
				
			}

		}
}
