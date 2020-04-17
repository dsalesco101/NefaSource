package ethos.model.content;

import java.util.Calendar;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;

public class DailyLogin {
	
	static Calendar cal = Calendar.getInstance();
    static int year = cal.get(Calendar.YEAR);
    static int date = cal.get(Calendar.DAY_OF_MONTH);
    static int month = cal.get(Calendar.MONTH)+1;
    
    private Player c;
    
	public DailyLogin(Player client) {
		this.c = client;
	}
	public static int getLastDayOfMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month -1, date);
		int maxDay = calendar.getActualMaximum(date);
        return maxDay;
	}
	public void IncreaseStreak() {
		c.LastLoginYear = year;
		c.LastLoginDate = date;
		c.LastLoginMonth = month;
		c.LoginStreak++;
		switch(c.LoginStreak) {
		case 1: //1 login
			break;
		case 2: //2 logins
			c.LoyP+=100;
			c.sendMessage("You have logged in for two days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 100 loyalty points.");
			break;
		case 3: //3 logins
			c.LoyP+=200;
			c.sendMessage("You have logged in for three days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 200 loyalty points.");
			break;
		case 4: //4 logins
			c.LoyP+=250;
			c.sendMessage("You have logged in for four days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 250 loyalty points.");
			break;
		case 5: //5 logins
			c.LoyP+=250;
			c.getItems().addItemToBank(11739, 1);
			c.sendMessage("You have logged in for five days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 250 loyalty points & a vote box.");
			c.sendMessage("Your vote box went into your bank for safety purposes.");
			break;
		case 6: //6 logins
			c.LoyP+=300;
			c.sendMessage("You have logged in for six days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 300 loyalty points");
			break;
		case 7: //7 logins
			c.LoyP+=300;
			c.getItems().addItemToBank(11739, 1);
			c.sendMessage("You have logged in for seven days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 250 loyalty points & a vote box.");
			c.sendMessage("Your vote box went into your bank for safety purposes.");
			break;
		case 8: //8 logins
			c.LoyP+=350;
			c.sendMessage("You have logged in for eight days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 350 loyalty points.");
			break;
		case 9: //9 logins
			c.LoyP+=350;
			c.getItems().addItemToBank(11739, 1);
			c.sendMessage("You have logged in for nine days in a row! Log in tomorrow for a better reward");
			c.sendMessage("You have received 350 loyalty points & a vote box.");
			c.sendMessage("Your vote box went into your bank for safety purposes.");
			break;
		case 10: //10 logins
			c.LoyP+=350;
			c.getItems().addItem(989, 1); 
			c.getItems().addItem(11739, 3); 
			c.sendMessage("You have logged in for the tenth time, and recieved the JACKPOT!");
			c.sendMessage("You have received 350 loyalty points & a vote box x3 & 1 million coins.");
			c.sendMessage("Your vote box went into your bank for safety purposes.");
			PlayerHandler.executeGlobalMessage("[@blu@DAILY-LOGINS@bla@]@blu@ " + c.playerName + " @bla@has just logged in 10 days in a row!");
			c.LoginStreak = 0;
			break;
		}
	}
	public void RefreshDates() {
		cal = Calendar.getInstance();
	   year = cal.get(Calendar.YEAR);
	   date = cal.get(Calendar.DAY_OF_MONTH);
	   month = cal.get(Calendar.MONTH)+1;
	}
	public void LoggedIn() {
		RefreshDates();
		if ((c.LastLoginYear == year) && (c.LastLoginMonth == month) && (c.LastLoginDate == date)) {
			c.sendMessage("You have already recieved your daily reward for today.");
			return;
		}
		if ((c.LastLoginYear == year) && (c.LastLoginMonth == month) && (c.LastLoginDate == (date - 1)))
			IncreaseStreak();
		else if ((c.LastLoginYear == year) && ((c.LastLoginMonth + 1) == month) && (1 == date))
			IncreaseStreak();
		else if ((c.LastLoginYear == year-1) && (1 == month) && (1 == date))
			IncreaseStreak();
		else {
			c.LoginStreak = 0;
			IncreaseStreak();
		}
	}
}