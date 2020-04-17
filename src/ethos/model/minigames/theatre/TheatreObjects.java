package ethos.model.minigames.theatre;

import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class TheatreObjects {

	public static boolean handleObjectClick(Player c, int objectId) {
		switch (objectId) {

		case 32755:
			if (Boundary.isIn(c, Boundary.MAIDEN)) {
				if (c.getX() >= 3186) {
					c.getPA().movePlayer(3184, c.getY(), c.getHeight());
				} else if (c.getX() <= 3184) {
					c.getPA().movePlayer(3186, c.getY(), c.getHeight());
				}
			}
			if (Boundary.isIn(c, Boundary.BLOAT)) {
				if (c.getX() <= 3286) {
					c.getPA().movePlayer(3288, c.getY(), c.getHeight());
				}
				if (c.getX() >= 3288 && c.getX() <= 3291) {
					c.getPA().movePlayer(3286, c.getY(), c.getHeight());
				}
				if (c.getX() >= 3300 && c.getX() <= 3303) {
					c.getPA().movePlayer(3305, c.getY(), c.getHeight());
				}
				if (c.getX() >= 3305) {
					c.getPA().movePlayer(3303, c.getY(), c.getHeight());
				}
			}
			if (Boundary.isIn(c, Boundary.NYLOCAS)) {
				if (c.getY() >= 4256) {
					c.getPA().movePlayer(c.getX(), 4254, c.getHeight());
				}
				if (c.getY() <= 4254) {
					c.getPA().movePlayer(c.getX(), 4256, c.getHeight());
				}
			}
			if (Boundary.isIn(c, Boundary.SOTETSEG)) {
				if (c.getY() <= 4306) {
					c.getPA().movePlayer(c.getX(), 4308, c.getHeight());
				}
				if (c.getY() >= 4308) {
					c.getPA().movePlayer(c.getX(), 4306, c.getHeight());
				}
			}
			if (Boundary.isIn(c, Boundary.XARPUS)) {
				if (c.getY() <= 4378) {
					c.getPA().movePlayer(c.getX(), 4380,  c.getHeight());
				}
				if (c.getY() >= 4380 && c.getY() <= 4385) {
					c.getPA().movePlayer(c.getX(), 4378,  c.getHeight());
				}
				if (c.getY() <= 4394 && c.getY() >= 4389) {
					c.getPA().movePlayer(c.getX(), 4396,  c.getHeight());
				}
				if (c.getY() >= 4396) {
					c.getPA().movePlayer(c.getX(), 4394, c.getHeight());
				}
			}
			break;

		case 33113:
			if (Boundary.isIn(c, Boundary.MAIDEN)) {
				if (c.killedMaiden) {
					c.getPA().movePlayer(3322, 4448);
				} else {
					c.sendMessage("You must defeat Maiden before progressing!");
				}
			}
			if (Boundary.isIn(c, Boundary.BLOAT)) {
				if (c.killedBloat) {
					c.getPA().movePlayer(3296, 4283);
				} else {
					c.sendMessage("You must defeat Bloat before progressing!");
				}
			}
			if (Boundary.isIn(c, Boundary.NYLOCAS)) {
				if (c.killedNylocas) {
					c.getPA().movePlayer(3291, 4328);
				} else {
					c.sendMessage("You must defeat Nylocas before progressing!");
				}
			}
			if (Boundary.isIn(c, Boundary.SOTETSEG)) {
				if (c.killedSotetseg) {
					c.getPA().movePlayer(3170, 4375, c.getHeight() + 1);
				} else {
					c.sendMessage("You must defeat Sotetseg before progressing!");
				}
			}
			break;

		case 32751:
			if (Boundary.isIn(c, Boundary.XARPUS)) {
				if (c.killedXarpus) {
					c.getPA().movePlayer(3168, 4303, c.getHeight() - 1);
				} else {
					c.sendMessage("You must defeat Xarpus before progressing!");
				}
			}
			break;

		case 32741:
			// TODO: Object to give staff for Verzik fight (in xarpus exit area - 3171,4397)
			break;
			
		case 32738:
			if (c.killedVerzik) {
				c.getPA().movePlayer(3237, 4307, c.getHeight());
			} else {
				c.sendMessage("You must defeat Verzik before progressing!");				
			}
			break;
		case 32995:
			c.sendMessage("You cannot go back!");
			break;

		case 32996:
			c.getPA().movePlayer(3050, 9950);
			c.sendMessage("Congratulations on completing Theatre of Blood!");
			break;

		}
		return false;
	}

}
