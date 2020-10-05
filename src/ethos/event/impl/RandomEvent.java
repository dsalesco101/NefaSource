package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.event.Event;
import ethos.model.content.tournaments.TourneyManager;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc; 

public class RandomEvent extends Event<Object> {


	private static final int INTERVAL = Misc.toCycles(2, TimeUnit.HOURS);

	public static int eventNumber = 0;
	
	
	public static String eventName = "";
	
	public static boolean stopEvent =false;
	
	public RandomEvent() {
		super(new String(), new Object(), INTERVAL);
	}	
	 public static String getEventName() {
		 if (stopEvent == true) {
			 return "@red@Disabled"; 
		 }
	        if (eventNumber < 1) {
	            return "@red@Not Active";   
	        } else {
	            return "@yel@View";
	        }
	    }

	
	@Override
	public void execute() {
		PlayerHandler.nonNullStream().forEach(player -> { 
			int pickEvent = 1 + Misc.random(11);
			eventNumber = pickEvent;
				 if (eventNumber == 1) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@CUT DOWN 275 MAGIC LOGS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 2) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@BURY 75 DRAGON BONES WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 3) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@THIEVE 125 SCIMITAR STALLS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 4) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@OPEN CRYSTAL CHEST 3 TIMES WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 5) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@KILL 5 KRAKENS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 6) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@USE EMOTE DANCE 75 TIMES WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 7) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@GAIN 150 PVM POINTS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 8) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@CATCH 25 BLACK CHINCHOMPAS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 9) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@COMPLETE 5 PEST CONTROL RUNS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 10) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@COMPLETE 2 BOSS SLAYER TASKS RUNS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 11) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@COMPLETE 50 BARROWS KILLS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 12) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@EXCHANGE 5 ITEMS WITHIN THE FIRE OF EXCHANGE WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 13) {
					if (!TourneyManager.getSingleton().isLobbyOpen() == true) {
						eventNumber = pickEvent; // rerolls event
						return;
					}
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@PLAY A TOURNAMENT WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 14) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@COMPLETE 15 WILDERNESS LAPS WITHIN @red@2HRS TO COMPLETE THE EVENT.");
				} else if (eventNumber == 15) {
					player.eventFinished = false;
					player.eventStage = 0;
					PlayerHandler.executeGlobalMessage("@red@[EVENT] @pur@COMPLETE 1 RAIDS RUN WITHIN @red@2HRS TO COMPLETE THE EVENT.");
					
				}
			
		});
	}
} 
