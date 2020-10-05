package ethos.model.players.packets.commands.all;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ethos.model.content.Referrals;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.packets.commands.Command;

/**
 * Teleport a given player to the player who issued the command.
 * 
 * @author Emiel
 */
public class Referplayer extends Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			List<String> starters = Referrals.getUsedReferrals();
			long usedReferral = starters.stream().filter(data -> data.equals(c.getMacAddress())).count();
			if (c.getMacAddress().equalsIgnoreCase(""+c2.getMacAddress()+"")) {
				c.getDH().sendStatement("@red@You can't refer yourself as its against the rules.");
				return;
			}
			if (c.getIpAddress().equalsIgnoreCase(""+c2.getIpAddress()+"")) {
				c.getDH().sendStatement("@red@You can't refer yourself as its against the rules.");
				return;
			}
			if (c2.playTime > c.playTime) {
				c.getDH().sendStatement("@red@You cannot invite soemone with less play-time than you.");
				return;
			}
			if (usedReferral >= 1) {
         		c.getDH().sendStatement("@red@You have already used your referral");
				return;
				}
			if (c.RefU > (1)) {
         		c.getDH().sendStatement("@red@You have already used your referral");
         		return;
			}
			Referrals.setUsedReferral(c);
        	c.RefU +=5; 
        	c.RefP+=1;
        	c2.sendMessage("You have referred a total of "+c.RefP+" people.");
        	c2.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
        	c2.sendMessage("@red@You can gain more by invitin your friends!");
		    c2.getItems().addItemToBank(995, 1500000);
        	c2.gfx100(199);

		    
			
        	c.gfx100(199);
		    c.getItems().addItemToBank(995, 1500000);
		    c.sendMessage("@red@Your @red@1.5 million coins @bla@has been added to your bank.");
            c.sendMessage("@red@You can gain more by invitin your friends!");
        	PlayerHandler.executeGlobalMessage("@cr10@["+c.playerName+"] has just joined by @red@"+input+"");
        	String loc = "./data/refers/player/"+c.playerName + "referral is "+input+".txt";
            File file = new File(loc);
            try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else {
			c.sendMessage(input + " must be online.");
		}
	}
}