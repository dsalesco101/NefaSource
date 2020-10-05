package ethos.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ethos.model.players.Player;

public class Hiscores implements Runnable {

	public static final String HOST = "185.201.11.127"; // website ip address
	public static final String USER = "u603323171_hiscores";
	public static final String PASS = "Testy4594";
	public static final String DATABASE = "u603323171_hiscores";

	public static final String TABLE = "hs_users";

	private Player player;
	private Connection conn;
	private Statement stmt;
	
	public Hiscores(Player player) {
		this.player = player;
	}
	public boolean connect(String host, String database, String user, String pass) {

		String url = "jdbc:mysql://" + host + ":3306/" + database;
		try {
			this.conn = DriverManager.getConnection(url, user, pass);
			return true;
		} catch (SQLException e) {
			if (e.getMessage().contains("Access denied")) {
				System.err.println(String.format("Unable to connect to database URL[%s] User[%s] Password[%s]", url, user, pass));
			} else {
				e.printStackTrace();
			}
			return false;
		}
	}

	public long getTotalExp() {
		long total = 0;
		for (int exp : player.playerXP) {
			total += exp;
		}
		return total;
	}

	public int getTotalLevel() {
		int total = 0;
		for (int exp : player.playerXP) {
			total += player.getLevelForXP(exp);
		}
		return total;
	}
	
	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getName();

			PreparedStatement stmt1 = prepare("DELETE FROM " + TABLE + " WHERE username=?");
			stmt1.setString(1, name);
			stmt1.execute();

			PreparedStatement stmt2 = prepare(generateQuery());
			stmt2.setString(1, name);
			//Update Highscores
			stmt2.setInt(2, player.getRights().getPrimary().getValue());
			
			//Update Highscores
			stmt2.setInt(3, player.getMode() != null ? player.getMode().getType().ordinal() : 0);
			stmt2.setInt(4, this.getTotalLevel()); // total level

			stmt2.setLong(5, this.getTotalExp());
			for (int i = 0; i < 25; i++)
				stmt2.setInt(6 + i, player.playerXP[i]);
			stmt2.execute();

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}

	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + TABLE + " (");
		sb.append("username, ");
		sb.append("rights, ");
		sb.append("mode, ");
		sb.append("total_level, ");
		sb.append("overall_xp, ");
		sb.append("attack_xp, ");
		sb.append("defence_xp, ");
		sb.append("strength_xp, ");
		sb.append("constitution_xp, ");
		sb.append("ranged_xp, ");
		sb.append("prayer_xp, ");
		sb.append("magic_xp, ");
		sb.append("cooking_xp, ");
		sb.append("woodcutting_xp, ");
		sb.append("fletching_xp, ");
		sb.append("fishing_xp, ");
		sb.append("firemaking_xp, ");
		sb.append("crafting_xp, ");
		sb.append("smithing_xp, ");
		sb.append("mining_xp, ");
		sb.append("herblore_xp, ");
		sb.append("agility_xp, ");
		sb.append("thieving_xp, ");
		sb.append("slayer_xp, ");
		sb.append("farming_xp, ");
		sb.append("runecrafting_xp, ");
		sb.append("hunter_xp, ");
//		sb.append("construction_xp, ");
//		sb.append("summoning_xp, ");
//		sb.append("dungeoneering_xp) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		return sb.toString();
	}

}