package Ranked;


import java.util.ArrayList;

import ethos.model.players.Player;
import ethos.util.Misc;

public class Ranked  {

	
	private static int noRank = 0;
	private static int bronze = 5;
	private static int iron = 12;
	private static int silver = 35;
	private static int gold = 100;
	private static int platinum = 275;
	private static int master = 500;
	private static int masterT2 = 1000;
	private static int masterT3 = 2000;
	
	static String currentRank = "";

	
	public enum rankTypes {
		PVM,
		SKILLING,
		MINIGAMES,
		OTHER
		;
	}
	public static void kills(Player player, int npcId) {
		switch (npcId) {
		case 239:
			increase(player, rankTasks.KBD);
			break;
		}
	}
	//22_500 = 60 seconds : 1 minute
	private static final long TASK_TIME = 22_500L * 60;
	public enum rankTasks {
		 // RANK TYPE : KILLS: TIME: MESSAGE
		KBD(rankTypes.PVM, 10, "Get 10 kbd kills in under 1 hour."),
		;
	
		public rankTypes type;
		public int amount;
		String rankMessage;
		
		private rankTasks(rankTypes type, int amount, String message) {
			
			this.type = type;
			this.amount = amount;
			this.rankMessage = message;
			
		}
		
	}
	
	
	/**
	 * Assigns a task to a player when it has daily's enabled and has no current task.
	 * @param player
	 */
	
	public static void assignTask(Player player) {
			if(player.rankTask != null) {
				player.sendMessage("@red@You already have a ranked task.");
				return;
			}
			player.taskTime = System.currentTimeMillis();
			player.rankTask = getRandomTask(player.challengerChoice);
			player.sendMessage("@red@New Daily Task: " + player.rankTask.rankMessage);
				
	}
			
			
	
	
	public static void complete(Player player) {
		if(player.rankTask == null)
			return;
		if (System.currentTimeMillis() - player.taskTime < TASK_TIME) {
			player.sendMessage("@blu@@cr10@ You have completed your ranked task: "+ player.rankTask.name().toLowerCase() + ", take these rewards!");
			// give rewards still
			player.sendMessage("Due to not completing in time given, your rank has been decreased.");
			Upgrading(player);
			player.ranking -= 3;
			return;
		}
			player.sendMessage("@blu@@cr10@ You have completed your ranked task: "+ player.rankTask.name().toLowerCase() + ", take these rewards!");
			player.sendMessage("You are slowly moving towards another rank!");
			//add reward
			player.rankTask = null;
			player.ranking+= 3;
			Upgrading(player);
		}
	
	public static void increase(Player player, rankTasks task) {
		if(player.rankTask == null)
			return;
		
		if (player.rankTask.equals(task)) {
			complete(player);
		}
	}
	
	/**
	 * Gets a random task on the choice of what the player has selected
	 * @param type
	 * @return
	 */
	private static rankTasks getRandomTask(rankTypes type) {
		ArrayList<rankTasks> ranktasks = new ArrayList<rankTasks>();
		for(rankTasks tasks : rankTasks.values())
			if(tasks.type.equals(type))
				ranktasks.add(tasks);
		return ranktasks.get(Misc.random(ranktasks.size()));
	}
	
	
		public void currentRank(Player player) {
			if (player.ranking >= bronze && player.ranking < iron) {
				currentRank = "bronze";
			} else if (player.ranking >= iron && player.ranking < silver) {
				currentRank = "iron";
			} else if (player.ranking >= silver && player.ranking < gold) {
				currentRank = "silver";
			} else if (player.ranking >= gold && player.ranking < platinum) {
				currentRank = "gold";
			} else if (player.ranking >= platinum && player.ranking < master) {
				currentRank = "platinum";
			} else if (player.ranking >= master && player.ranking < masterT2) {
				currentRank = "master";
			} else if (player.ranking >= masterT2 && player.ranking < masterT3) {
				currentRank = "master tier 3";
			} else if (player.ranking >= masterT3) {
				currentRank = "master tier 3";
	}
}
		public static void Upgrading(Player player) {
			if (player.ranking >= bronze && player.ranking < iron) {
				player.ranking = bronze;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= iron && player.ranking < silver) {
				player.ranking = iron;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= silver && player.ranking < gold) {
				player.ranking = silver;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= gold && player.ranking < platinum) {
				player.ranking = gold;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= platinum && player.ranking < master) {
				player.ranking = platinum;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= master && player.ranking < masterT2) {
				player.ranking = master;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= masterT2 && player.ranking < masterT3) {
				player.ranking = masterT2;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
			} else if (player.ranking >= masterT3) {
				player.ranking = masterT3;
				player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
	}
}
		public void Demoting(Player player) {
				if (player.ranking >= bronze && player.ranking < iron) {
					player.ranking = noRank;
					currentRank(player);	
					player.sendMessage("You rank has been demoted to @red@"+currentRank+"");
				} else if (player.ranking >= iron && player.ranking < silver) {
					player.ranking = iron;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= silver && player.ranking < gold) {
					player.ranking = silver;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= gold && player.ranking < platinum) {
					player.ranking = gold;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= platinum && player.ranking < master) {
					player.ranking = platinum;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= master && player.ranking < masterT2) {
					player.ranking = master;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= masterT2 && player.ranking < masterT3) {
					player.ranking = masterT2;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
				} else if (player.ranking >= masterT3) {
					player.ranking = masterT3;
					currentRank(player);	
					player.sendMessage("You rank has been upgraded to @red@"+currentRank+"");
		}			
}







}
