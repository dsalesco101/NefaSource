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
			if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
    		|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
    		|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
    		&& c.LoginStreak == 1){
				c.sendMessage("You can only start the daily logins on monday.");
				return;
			}
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@1@blu@ days in a row this week!");
			break;
		case 2: //2 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@2@blu@ days in a row this week!");
			break;
		case 3: //3 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@3@blu@ days in a row this week!");
			break;
		case 4: //4 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@4@blu@ days in a row this week!");
			break;
		case 5: //5 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@5@blu@ days in a row this week!");
			break;
		case 6: //6 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak+=1;
			c.sendMessage("@blu@You have logged in for @red@6@blu@ days in a row this week!");
			break;
		case 7: //7 logins
			c.sendMessage("BETA MESSAGE FOR DAILY LOGIN STREAK, DAY : MONDAY");
			c.LoginStreak = 0;
			c.sendMessage("@blu@You have logged in for @red@7@blu@ days in a row this week!");
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