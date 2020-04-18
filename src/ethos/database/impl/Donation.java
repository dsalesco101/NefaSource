package ethos.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import ethos.model.content.wogw.Wogw;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Right;


/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Donation implements Runnable {

	public static final String HOST = "153.92.6.164"; // website ip address
	public static final String USER = "u697564708_store";
	public static final String PASS = "Procode1288";
	public static final String DATABASE = "u697564708_store";

	private Player player;
	private Connection conn;
	private Statement stmt;

	public int GoalIsReached = 0;
	/**
	 * The constructor
	 * @param player
	 */
	public Donation(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				List<Player> Owners = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> p.getRights().isOrInherits(Right.OWNER)).collect(Collectors.toList());
				switch (item_number) {// add products according to their ID in the ACP					
				case 27: 
					player.getItems().addItem(2403, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $10 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 10 * quantity;
					break;
				case 30: 
					player.getItems().addItem(2396, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $25 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 25 * quantity;
					break;
				case 31: 
					player.getItems().addItem(786, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $50 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 50 * quantity;
					break;
				case 32: 
					player.getItems().addItem(761, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $100 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 100 * quantity;
					break;
				case 33: 
					player.getItems().addItem(607, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $250 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 250 * quantity;
					break;
				case 34: 
					player.getItems().addItem(608, 1 * quantity);
					player.sendMessage("Thank you for supporting wisdom!");
					player.sendMessage("You have received your $500 Donator Scroll.");
					PlayerHandler.sendMessage("@red@["+player.playerName+"]@pur@ Has just donated for a total of $"+paid+"", Owners);
					GoalIsReached = 500 * quantity;
					break;

				default:
					player.sendMessage("@blu@The user @red@"+player.playerName+"@blu@ has not yet donated.");
					break;
				}
				
				player.sendMessage("Please be sure to press `received item` on paypal transcation.");
				rs.updateInt("claimed", 1); 
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host the host ip address or url
	 * @param database the name of the database
	 * @param user the user attached to the database
	 * @param pass the users password
	 * @return true if connected
	 */
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection
	 * and statement instances
	 */
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

	/**
	 * Executes an update query on the database
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
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

	/**
	 * Executres a query on the database
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
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
