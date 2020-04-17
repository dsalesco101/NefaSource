package ethos.event.impl;

import java.util.concurrent.TimeUnit;

import ethos.event.Event;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc; 

public class UpdateQuestTab extends Event<Object> {


	private static final int INTERVAL = Misc.toCycles(5, TimeUnit.SECONDS);

	
	public UpdateQuestTab() {
		super(new String(), new Object(), INTERVAL);
	}	
	
	@Override
	public void execute() {
		PlayerHandler.nonNullStream().forEach(player -> { 
			player.getQuestTab().updateInformationTab();
		});
	}
} 