package ethos.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ethos.model.content.loot.impl.VoteChest;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;


public class Voting implements Runnable {
	
	public static final String HOST = "153.92.6.164"; 
	public static final String USER = "u697564708_vote";
	public static final String PASS = "Procode1288";
	public static final String DATABASE = "u697564708_vote";

	public static int VOTES = 10;
	
	private Player player;
	private Connection conn;
	private Statement stmt;

	public Voting(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName().replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date IS NOT NULL");

			while (rs.next()) {
				player.sendMessage("You have succesfully voted on the site.");
				player.votePoints+=1;
				VOTES +=1;
				player.dayv+=1;
				if (VOTES == 30) {
					PlayerHandler.executeGlobalMessage("@red@[VOTE] @blu@Another 10 people have voted for wisdom!");
					VOTES = 0;
					return;
				}
				if (player.dayv >= 30) {
					player.dayv = 0;
					player.getItems().addItem(VoteChest.KEY, 1);
				}
		        int petchance = Misc.random(6000);
				 if (petchance >= 5999) {
					 player.getItems().addItem(21262, 1);
			            PlayerHandler.executeGlobalMessage("@red@- "+player.playerName+"@blu@ has just received the @red@Vote Genie Pet");
			            player.sendMessage("@red@@cr10@You pet genie is waiting in your bank, waiting to serve you as his master.");
			            player.gfx100(1028);
			        }
	            if (player.amDonated >=10 && player.amDonated <= 49) {
	            	player.getItems().addItemUnderAnyCircumstance(995, 100000);
		     } else if (player.amDonated >=50 && player.amDonated <= 99) {
		    	 player.getItems().addItemUnderAnyCircumstance(995, 120000);
					//360k cash
		     } else if (player.amDonated >=100 && player.amDonated <= 199) {
		    	 player.getItems().addItemUnderAnyCircumstance(995, 150000);
			    //450k
		     } else if (player.amDonated >=200 && player.amDonated <= 299) {
		    	 player.votePoints+=1;
				    player.getItems().addItemUnderAnyCircumstance(995, 120000);
					//3 vote points
				    //360k cash
		     } else if (player.amDonated >=300 && player.amDonated <= 499) {
				 	player.votePoints+=1;
				 	player.getItems().addItemUnderAnyCircumstance(995, 150000);
					//extra 450k
					//extra 3 vote points
		     } else if (player.amDonated >=500 && player.amDonated <= 999) {
		    	 player.votePoints+=1;
		    	 player.getItems().addItemUnderAnyCircumstance(995, 20000);
					//3 vote points
				    //600k cash
		     } else if (player.amDonated >=1000) {
		    	 player.votePoints+=2;
		    	 player.getItems().addItemUnderAnyCircumstance(995, 250000);
					//6 extra vote points
				    //750k cash
		     
	 }

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	public void destroy() {
        try {
    		conn.close();
        	conn = null;
        	if (stmt != null) {
    			stmt.close();
        		stmt = null;
        	}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public int executeUpdate(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

	public ResultSet executeQuery(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
