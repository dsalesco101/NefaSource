package ethos.model.players.packets.commands.all;

import java.util.Optional;
import java.util.stream.IntStream;

import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.packets.commands.Command;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Setlevel extends Command {

	@Override
	public void execute(Player c, String input) {
			int skillId = -1;
			int skillLevel;
			String[] args = input.split(" ");
			if (args.length < 2) {
				return;
			}
			if (!c.getRights().contains(Right.MED_MODE)){
				c.getDH().sendStatement("Only players who chosen the pvp mode can do this.");
				return;
			}
			if (args.length == 3) {
				IntStream.range(Integer.parseInt(args[0]), Integer.parseInt(args[1])).forEach(level -> {
					c.playerLevel[level] = Integer.parseInt(args[2]);
					c.playerXP[level] = c.getPA().getXPForLevel(Integer.parseInt(args[2])) + 1;
					c.getPA().refreshSkill(level);
					c.getPA().setSkillLevel(level, c.playerLevel[level], c.playerXP[level]);
				});
				c.sendMessage("You have set the skill id '" + Integer.parseInt(args[0]) + "' to '"
						+ Integer.parseInt(args[1]) + "' to level " + Integer.parseInt(args[2]) + ".");
			} else {
				try {
					skillLevel = Integer.parseInt(args[1]);
					try {
						skillId = Integer.parseInt(args[0]);
					} catch (NumberFormatException ex) {
						int sID = getSkillID(args[0]);
						if (sID >= 0) {
							skillId = sID;
						} else {
							c.getDH().sendStatement("You must enter a skill name or skill id.");
						}
					}
					if (skillId < 0 || skillId > c.playerLevel.length - 1) {
						c.sendMessage("Unable to set level, skill id cannot exceed the range of 0 -> "
								+ (c.playerLevel.length - 1) + ".");
						return;
					}
					if (skillLevel < 1) {
						skillLevel = 1;
						// } else if (skillLevel > 99) {
						// skillLevel = 99;
					}
					c.playerLevel[skillId] = skillLevel;
					c.playerXP[skillId] = c.getPA().getXPForLevel(skillLevel) + 1;
					c.getPA().refreshSkill(skillId);
					c.sendMessage("You have set the skill id '" + skillId + "' to level " + skillLevel + ".");
					c.getPA().setSkillLevel(skillId, c.playerLevel[skillId], c.playerXP[skillId]);
					c.getPA().levelUp(skillId);
				} catch (Exception e) {
					c.sendMessage("Error. Correct syntax: ::setlevel skillid level");
				}
			}
		}

		private int getSkillID(String type) {

			switch (type) {
				case "attack":
					return 0;
				case "defence":
					return 1;
				case "strength":
					return 2;
				case "hitpoints":
					return 3;
				case "ranged":
					return 4;
				case "prayer":
					return 5;
				case "magic":
					return 6;
				default:
					return -1;
			}
		}
	}