package ethos.model.players.packets.commands.all;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ethos.model.content.Referrals;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;
import ethos.util.Misc;

public class Refer extends Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		
		switch (args[0]) {
		
		case "":
			c.sendMessage("Usage: ::refer person/site");
			break;
		case "rsps-list":
			List<String> starters = Referrals.getUsedReferrals();
			long usedReferral = starters.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (usedReferral >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
			c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@RSPS-LIST");
        	String loc = "./data/refers/rsps-list/" +c.playerName + ".txt";
            File file = new File(loc);
            try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "moparscape":
			List<String> startersmopar = Referrals.getUsedReferrals();
			long usedReferralmopar = startersmopar.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (usedReferralmopar >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
			c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@MOPARSCAPE");
        	String locmopar = "./data/refers/moparscape/" +c.playerName + ".txt";
            File filemopar = new File(locmopar);
            try {
				filemopar.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "top100arena":
			List<String> starterstop100arena = Referrals.getUsedReferrals();
			long usedReferraltop100arena = starterstop100arena.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (usedReferraltop100arena >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
			c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@TOP100ARENA");
        	String loctop100arena = "./data/refers/top100arena/" +c.playerName + ".txt";
            File filetop100arena = new File(loctop100arena);
            try {
				filetop100arena.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "rune-locus":
			List<String> startersrunelocus = Referrals.getUsedReferrals();
			long usedReferralrunelocus = startersrunelocus.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (usedReferralrunelocus >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
			c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@RUNE-LOCUS");
        	String locrunelocus = "./data/refers/rune-locus/" +c.playerName + ".txt";
            File filerunelocus = new File(locrunelocus);
            try {
				filerunelocus.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "topg":
			List<String> starterstopg = Referrals.getUsedReferrals();
			long usedReferraltopg = starterstopg.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (usedReferraltopg >= 1) {
				c.getPA().closeAllWindows();
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
			c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.getItems().addItemToBank(6199, 1);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@TOPG");
        	String loctopg = "./data/refers/topg/" +c.playerName + ".txt";
            File filetopg = new File(loctopg);
            try {
				filetopg.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Sets your referral to the person that invited you.");
	}
}
