package ethos.model.players.packets.ExchangeSystem;

import ethos.event.impl.RandomEvent;
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
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=400;
				c.sendMessage("You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " for @red@400@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 400;
			break;
			case 11840://dragon boots
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=200;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@200@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 200;
			break;
			case 2577://ranger boots
			case 6585://fury
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=2000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@2000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 2000;
			break;
			case 4151://whip
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=750;
				c.sendMessage("You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " for @red@750@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 750;
			break;
			case 6737://b ring
			case 6733://archer ring
			case 6731://seers ring
			case 12603:
			case 12605:
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=1850;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@1850@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 1850;
				break;
			case 21902:
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=7000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@7000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 7000;
				break;
			case 11834://bcp
			case 11832://tassets
			case 11826://army helm
			case 11828://army plate
			case 11830://arma leg
			case 13239:// primordials ect + 
			case 13237://pegasion
			case 13235://eternal
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=7500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@7500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 7500;
				break;
			case 13576://d warhammer
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=14000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@14000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 14000;
				break;
			case 20784://claws
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=17000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@17000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 17000;
				break;
			case 11802://ags
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=12000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@12000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 12000;
				break;
			case 13265://abby dagger
			case 11808://zgs		
			case 11804://bgs	
			case 11806://sgs
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				break;
			case 13263://bludgon			
			case 19552://zenyte brace
			case 19547://anguish
			case 19553://torture
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=8500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@8500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 8500;
				break;
			case 12002: //occult
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=800;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@800@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 800;
				break;
			case 12809://sara blessed
			case 12006://tent whip
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				break;
			case 11284: //dfs
			case 11283://dfs
			case 19478://light ballista
			case 12851:
			case 12852:
			case 12853://amulet of damned
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=6500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 6500;
				break;
			case 19481://heavy ballista
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=13000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@13000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 13000;
				break;
			case 12821://spectral
			case 12825://arcane
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=25000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@25000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 25000;
				break;
			case 12817://ely
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				  
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=45000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@45000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 45000;
				break;
			case 11785://arma crossbow
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=6500;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6500@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 6500;
				break;
			case 21012://dhcb
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=9000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@9000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 9000;
				break;
			case 12924://blowpipe
			case 12926:
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=20000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@20000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 20000;
				break;
			case 11770://seers i
			case 11771://archer i
			case 11772://warrior i
			case 11773://b ring i
			case 12691://tyrannical ring i
			case 12692://tres ring (i)
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=5000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@5000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 5000;
				break;
			case 20997://t bow
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=100000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@100000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 100000;
				break;
			case 12806://malediction
			case 12807://odium
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=6000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@6000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 6000;
			break;
			case 21006:
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=10000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@10000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 10000;
				break;
			case 22322://avernic
			case 22477:
			case 21015://dihns bulwark
			case 21003://elder maul
			case 13196://tanz helm
			case 12929://serp helm
			case 13198://magma helm
			case 12902: //toxic staff of the dead
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=9000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@9000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 9000;
				break;
			case 20517://elder top
			case 20520://elder robe
			case 20595://elder hood
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=1200;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@1200@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 1200;
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
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=4750;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@4750@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 4750;
				break;
			case 21018:
			case 21021:
			case 21024:
			case 22326:
			case 22327:
			case 22328:
				  if (c.eventFinished == false && RandomEvent.eventNumber == 12) {
				   		c.eventStage += 1;
				  	}
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=25000;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@25000@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 25000;
				break;
			case 19550: // ring of suffering
			case 19551: // ring of suffering
			case 19710: // ring of suffering
			case 20655: // ring of suffering
			case 20657: // ring of suffering
			case 19544: //tormented bracelet
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=925;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@925@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 925;
				break;
			case 22547:
			case 22542:
			case 22552:
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.getItems().deleteItem(itemId, 1);
				c.exchangeP+=925;
				c.sendMessage("@blu@You have successfully exchanged your @blu@" + ItemAssistant.getItemName(itemId) + " @blu@for @red@925@blu@ Exchange Points!");
				Player.FIRE_OF_EXCHANGE = 4500;
				break;
			default:
				c.turnPlayerTo(objectX, objectY);
				c.objectYOffset = 5;
				c.objectXOffset = 5;
				c.objectDistance = 5;
				c.sendMessage("@red@You cannot exchange @blu@" + ItemAssistant.getItemName(itemId) + " for @red@ Exchange Points.");
				break;
		    }
		   if (c.eventStage == 5 && c.eventFinished == false && RandomEvent.eventNumber == 12) {
			   c.sendMessage("@blu@You have completed the event challenge: @red@Exchange 5 items in the Fire Of Exchange.");
			   c.sendMessage("@blu@You receive @red@1 @blu@Event Point for completing the Event Challenge.");
			   c.eventPoints+=1;
			   c.eventStage = 0;
			   c.eventFinished = true;
		}
		    if (Player.FIRE_OF_EXCHANGE >= 100_000) {
				PlayerHandler.executeGlobalMessage("@bla@[@red@FIRE OF EXCHANGE@bla@]@blu@ Has just consumed another @red@100,000@blu@ exchange points!");
				Player.FIRE_OF_EXCHANGE = 0;
				
			}

		}
}
