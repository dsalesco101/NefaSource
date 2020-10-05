package ethos.database.impl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ethos.model.players.Player;
/*
 * @author Martin
 * 31st Oct 2011
 * Few cans before I go out
 */

public class ForumIntegration {


	private static final int CRYPTION_ID = 85461564;
	
	public static int checkUser(Player player){
		try {
			String urlString = "http://www.nefariouspkz.com/login.php?crypt="+CRYPTION_ID+"&name="+player.playerName.toLowerCase().replace(" ","_")+"&pass="+player.playerPass;
			HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = in.readLine().trim();
			try {
				int returnCode = Integer.parseInt(line);
				switch(returnCode){
				case -1:
					System.out.println("Wrong CRYPTION_ID");
					return 10;
				case 1://invalid password
					return 3;
				case 0://no member exists for this username
					return 12;
				default://login ok
					int memberGroupId = returnCode-2;
					return 2;
				}
			} catch(Exception e){
				//only called if failed to connect to the database
				System.out.println(line);
				return 8;//login server down
			}
		} catch(Exception e2){
			e2.printStackTrace();
		}
		return 11;//website offline
	}
}