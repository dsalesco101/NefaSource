package ethos.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


import ethos.Server;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.eventcalendar.ChallengeParticipant;
import ethos.model.content.eventcalendar.EventCalendar;
import ethos.model.content.loot.impl.VoteChest;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;
import ethos.sql.DatabaseConfiguration;
import ethos.sql.eventcalendar.queries.AddParticipantEntryOnVoteQuery;
import ethos.util.Misc;
import ethos.util.log.PlayerLogging;


public class Vote implements Runnable {

	public static final String HOST = "153.92.6.164";
	public static final String USER = "u697564708_vote";
	public static final String PASS = "Procode598"; //change here
	public static final String DATABASE = "u697564708_vote";

	public static int GLOBALVOTES = 10;
	int GLOBALPOINTS = 15;


	private Connection conn;
	private Statement stmt;
	private Player p;
	
	public Vote(Player c) {
		this.p = c;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}
			String name = p.playerName.replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date IS NOT NULL");
			while (rs.next())  {
	        	p.setLastVote(LocalDate.now());
				Server.getDatabaseManager().execute(DatabaseConfiguration.EVENT_CALENDAR, new AddParticipantEntryOnVoteQuery(
						new ChallengeParticipant(p, EventCalendar.getDateProvider())));

	             p.votePoints+=1;//3
	             p.dayv+=1; //3			
	             GLOBALVOTES += 1;
				PlayerLogging.write(PlayerLogging.LogType.VOTE, p, "Claimed vote.");
				 p.sendMessage("You have gained 1 Voting point and gp!");
				 p.getItems().addItemUnderAnyCircumstance(995, 200000);
				 if (p.dayv >= (30)) {
						int key = VoteChest.KEY;
					    p.getItems().addItemToBank(key, 1);
					    p.sendMessage("@pur@One @gre@vote key @pur@has been added to your bank for getting 40 votes!");
					    p.dayv = 0;
				     }
				 if (GLOBALVOTES  >= 15) {
					 	p.getItems().addItem(989, 1);
						PlayerHandler.executeGlobalMessage("@red@@cr10@Another "+GLOBALPOINTS+" players have voted! Make sure to ::vote");
						p.sendMessage("@red@@cr10@You received a crystal key for the server hitting "+GLOBALPOINTS+" total votes.");
						GLOBALVOTES = 0;
					}
			     if (p.amDonated >=5 && p.amDonated <= 49) {
					    p.getItems().addItemUnderAnyCircumstance(995, 10000);
						//300k cash	
			     } else if (p.amDonated >=50 && p.amDonated <= 99) {
					    p.getItems().addItemUnderAnyCircumstance(995, 120000);
						//360k cash
			     } else if (p.amDonated >=100 && p.amDonated <= 199) {
				    p.getItems().addItemUnderAnyCircumstance(995, 150000);
				    //450k
			     } else if (p.amDonated >=200 && p.amDonated <= 299) {
					    p.votePoints+=1;
					    p.getItems().addItemUnderAnyCircumstance(995, 120000);
						//3 vote points
					    //360k cash
			     } else if (p.amDonated >=300 && p.amDonated <= 499) {
					 	p.votePoints+=1;
					    p.getItems().addItemUnderAnyCircumstance(995, 150000);
						//extra 450k
						//extra 3 vote points
			     } else if (p.amDonated >=500 && p.amDonated <= 999) {
					    p.votePoints+=1;
					    p.getItems().addItemUnderAnyCircumstance(995, 20000);
						//3 vote points
					    //600k cash
			     } else if (p.amDonated >=1000) {
					    p.votePoints+=2;
					    p.getItems().addItemUnderAnyCircumstance(995, 250000);
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
