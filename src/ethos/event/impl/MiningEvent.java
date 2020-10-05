package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.event.Event;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc; 

public class MiningEvent extends Event<Object> {


	private static final int INTERVAL = Misc.toCycles(60, TimeUnit.MINUTES);
	
	public static int TOTAL_MINES = 500;
	
	public static boolean EVENT_STARTED = false;
	
	
	public MiningEvent() {
		super(new String(), new Object(), INTERVAL);
	}	
	
	public void generateReward(Player player) {
		if (player.getItems().isWearingItem(1265) || player.getItems().isWearingItem(1267)) { //BRONZE PICKAXE  //IRON PICKAXE
		} else if (player.getItems().isWearingItem(1269)) { //STEEL PICKAXE
		} else if (player.getItems().isWearingItem(1273)) { //MITHRIL PICKAXE
		} else if (player.getItems().isWearingItem(1271)) { //ADAMANT PICKAXE
		} else if (player.getItems().isWearingItem(1275)) { //RUNE PICKAXE
		} else if (player.getItems().isWearingItem(11920) || player.getItems().isWearingItem(12797)) { //DRAGON PICKAXE

		}
	}
	
	@Override
	public void execute() {
		PlayerHandler.nonNullStream().forEach(player -> { 
			PlayerHandler.executeGlobalMessage("[EVENT MANAGER] A giant rock has fell down from the sky around the home area.");
			EVENT_STARTED = true;
			generateReward(player);
			
		});
	}
} 