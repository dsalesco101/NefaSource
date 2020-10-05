package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.event.Event;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc; 

public class HPRegen extends Event<Object> {


	private static final int INTERVAL = Misc.toCycles(1, TimeUnit.MINUTES);

	
	public HPRegen() {
		super(new String(), new Object(), INTERVAL);
	}	
	
	@Override
	public void execute() {
		PlayerHandler.nonNullStream().forEach(player -> { 
			if (player.getHealth().getCurrentHealth() <= 10 && player.combatLevel == 3) {
				return;
			}
			if (player.getHealth().getCurrentHealth() > 98) {
				return;
			}
			player.getHealth().HPRegen();
		});
	}
} 