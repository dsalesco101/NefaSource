package ethos.model.players.packets.itemoptions;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.text.WordUtils;

import ethos.Config;
import ethos.Server;
import ethos.model.content.AlchemyPouch;
import ethos.model.content.DiceHandler;
import ethos.model.content.DonatorBenefit;
import ethos.model.content.DoubleExpScroll;
import ethos.model.content.Packs;
import ethos.model.content.RunePouch;
import ethos.model.content.lootbag.LootingBag;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.teleportation.TeleportTablets;
import ethos.model.content.trails.MasterClue;
import ethos.model.content.trails.RewardLevel;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.magic.NonCombatSpells;
import ethos.model.players.skills.hunter.Hunter;
import ethos.model.players.skills.hunter.trap.impl.BirdSnare;
import ethos.model.players.skills.hunter.trap.impl.BoxTrap;
import ethos.model.players.skills.prayer.Bone;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.players.skills.runecrafting.Pouches;
import ethos.model.players.skills.runecrafting.Pouches.Pouch;
import ethos.model.players.skills.slayer.SlayerUnlock;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

import static ethos.model.content.DiceHandler.DICING_AREA;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ItemOptionOne implements PacketType {
    /**
     * The last planted flower
     */
    private int lastPlantedFlower;

    /**
     /**
     * If the entity is in the midst of planting mithril seeds
     */
    public static boolean plantingMithrilSeeds = false;

    /**
     * The position the mithril seed was planted
     */
    private static final int[] position = new int[2];

    /**
     * Chances of getting to array flower1
     */
    private int flowerChance = Misc.random(100);


    /**
     * Last plant for the timing of the flower
     */
    private long lastPlant = 0;
    /**
     * Array containing flower object ids without black and white flowers
     */
    private int flower[] = {
            2980, 2981, 2982,
            2983, 2984, 2985,
            2986
    };

    /**
     * Array containing flower object ids along with black and white flowers
     */
    private int flower1[] = {
            2980, 2981, 2982,
            2983, 2984, 2985,
            2986, 2987, 2988
    };

    /**
     * Draws a random flower object from the flower arrays
     * @return
     * 			the flower object chosen
     */
    private int randomFlower() {
        if (flowerChance > 29) {
            return flower[(int) (Math.random() * flower.length)];
        }
        return flower[(int) (Math.random() * flower1.length)];
    }

    public void plantMithrilSeed(Player c) {
        position[0] = c.absX;
        position[1] = c.absY;
        lastPlantedFlower = randomFlower();
        GlobalObject object1 = new GlobalObject(lastPlantedFlower, position[0], position[1], c.getHeight(), 3, 10, 120, -1);
        Server.getGlobalObjects().add(object1);
        c.getPA().walkTo(1, 0);
        c.turnPlayerTo(position[0] -1, position[1]);
        c.sendMessage("You planted a flower!");
        plantingMithrilSeeds = true;
        c.getItems().deleteItem(299, c.getItems().getItemSlot(299), 1);
    }
    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        c.getInStream().readSignedShortLittleEndianA();
        int itemSlot = c.getInStream().readSignedWordA();
        int itemId = c.getInStream().readSignedWordBigEndian();
        if (itemSlot >= c.playerItems.length || itemSlot < 0) {
            return;
        }
        if (itemId != c.playerItems[itemSlot] - 1) {
            return;
        }
        if (c.isDead || c.getHealth().getCurrentHealth() <= 0) {
            return;
        }

        if (c.getInterfaceEvent().isActive()) {
            c.sendMessage("Please finish what you're doing.");
            return;
        }
        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        if (c.getTutorial().isActive()) {
            c.getTutorial().refresh();
            return;
        }
        c.lastClickedItem = itemId;
        c.getHerblore().clean(itemId);
        if (c.getFood().isFood(itemId)) {
            c.getFood().eat(itemId, itemSlot);
        } else if (c.getPotions().isPotion(itemId)) {
            c.getPotions().handlePotion(itemId, itemSlot);
        }
        Optional<Bone> bone = Prayer.isOperableBone(itemId);
        if (bone.isPresent()) {
            c.getPrayer().bury(bone.get());
            return;
        }
        TeleportTablets.operate(c, itemId);
        Packs.openPack(c, itemId);
        if (LootingBag.isLootingBag(c, itemId)) {
            c.getLootingBag().toggleOpen();
            return;
        }
        if (RunePouch.isRunePouch(c, itemId)) {
            c.getRunePouch().openRunePouch();
            return;
        }

        switch (itemId) { 
        /*case 13307:
        	if (c.wildLevel > 0) {
        		c.sendMessage("You cant use the exchange option in the wilderness.");
        		return;
        	}
        	if (!c.getItems().playerHasItem(13307, 15)) {
				c.sendMessage("You need 15 blood money in order to exchange it to 1 pkp.");
				return;
			}
        	int bm = c.getItems().getItemAmount(13307) / 15;
        	c.pkp+= 1 * bm;
        	c.sendMessage("You have succesfully exchanged @red@"+bm * 15+"@bla@ blood money into @red@"+bm+"@bla@ pkp.");
        	c.getItems().deleteItem(13307, bm * 15);
        	break;*/
        case 1464:
        	if (c.wildLevel > 0) {
        		c.sendMessage("You cant use the exchange option in the wilderness.");
        		return;
        	}
        	int voteAmount = c.getItems().getItemAmount(1464);
        	c.votePoints+= voteAmount;
        	c.sendMessage("You have succesfully exchanged @red@"+voteAmount+"@bla@ Vote Tickets into Vote Points..");
        	c.getItems().deleteItem(1464, voteAmount);
        	break;
        case 683:
        	if (c.wildLevel > 0) {
        		c.sendMessage("You cant use the exchange option in the wilderness.");
        		return;
        	}
        	int bountyAmount = c.getItems().getItemAmount(683);
        	c.votePoints+= bountyAmount;
        	c.sendMessage("You have succesfully exchanged @red@"+bountyAmount+"@bla@ Bounty Hunter Tickets into Bounty Points.");
        	c.getItems().deleteItem(683, bountyAmount);
        	break;
        case 2839:
        	break;
        case 977:
        	AlchemyPouch.addCharges(c);
        	break;
        	
        /**
         * Coin bags, edit price if you want (int random)
         */
        	
        case 10832:
        	int random = 125000 + Misc.random(4156);
        	c.getItems().addItemUnderAnyCircumstance(995, random);
        	c.getItems().deleteItem(10832, 1);
        	c.getDH().sendItemStatement("The bag contains "+random+" coins.", 10832);
        	break;
        	
        case 10833:
        	int random2 = 250000 + Misc.random(4156);
        	c.getItems().addItemUnderAnyCircumstance(995, random2);
        	c.getItems().deleteItem(10833, 1);
        	c.getDH().sendItemStatement("The bag contains "+random2+" coins.", 10833);
        	break;
        	
        case 10834:
        	int random3 = 500000 + Misc.random(4156);
        	c.getItems().addItemUnderAnyCircumstance(995, random3);
        	c.getItems().deleteItem(10834, 1);
        	c.getDH().sendItemStatement("The bag contains "+random3+" coins.", 10834);
        	break;
        	
        case 10835:
        	int random4 = 1080000 + Misc.random(4156);
        	c.getItems().addItemUnderAnyCircumstance(995, random4);
        	c.getItems().deleteItem(10835, 1);
        	c.getDH().sendItemStatement("The bag contains "+random4+" coins.", 10835);
        	break;
        	
        	
        case 21034:
        	c.getDH().sendDialogues(345, 9120);
        	break;
        	
            case 4278:
            	if (c.wildLevel > 0) {
            		c.sendMessage("You cannot redeem your points in the wildernmess");
            		return;
            	}
            		int amountOfEcto = c.getItems().getItemAmount(4278);
            	
            		c.getItems().deleteItem(4278, amountOfEcto);
    				c.ecoTokensIncrease(amountOfEcto);
    				c.sendMessage("@red'You tranferd "+amountOfEcto+" Mage arena tokens into "+amountOfEcto+" Mage Arena points");
            	break;
        		
        case 21079:
        	c.getDH().sendDialogues(347, 9120);
        	break;
        case 22477:
			c.sendMessage("Attach it onto a dragon defender to make avernic defender.");
        	break;
        case 19564:
        	if (c.wildLevel > 30) {
				c.sendMessage("You can't teleport above level 30 in the wilderness.");
				return;
				}
    		c.getPA().startTeleport(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0, "pod", false);
    		break;
        case 13188:
        	c.startAnimation(7514);
    		c.gfx0(1282);
        	break;
        case 2841:
        	if (!c.getItems().playerHasItem(2841)) {
				c.sendMessage("You need an Double XP Scroll to do this!");
        		return;
        	}
        	if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
    		|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
		c.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, no need to use that now!");
		return;
	}
        	else if (c.xpScroll == false && c.getItems().playerHasItem(2841)) {
				c.getItems().deleteItem(2841, 1);
					DoubleExpScroll.openScroll(c);
					c.sendMessage("@red@You have activated 1 hour of double experience.");
			} else if (c.xpScroll == true) {
				c.sendMessage("@red@You already activated this.");
		}
        	break;
        case 4209:
        	for (int i = 8144; i < 8195; i++) {
				c.getPA().sendFrame126("", i);
			}
			int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173,
					8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };
			c.getPA().sendFrame126("@dre@Kill Tracker for @blu@" + c.playerName + "", 8144);
			c.getPA().sendFrame126("", 8145);
			c.getPA().sendFrame126("@blu@Total kills@bla@ - " + c.getNpcDeathTracker().getTotal() + "", 8147);
			c.getPA().sendFrame126("", 8148);
			int frameIndex = 0;
			for (Map.Entry<String, Integer> entry : c.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (frameIndex > frames.length - 1) {
					break;
				}
				if (entry.getValue() > 0) {
					c.getPA().sendFrame126("@blu@" + WordUtils.capitalize(entry.getKey().toLowerCase()) + ": @red@" + entry.getValue(), frames[frameIndex]);
					frameIndex++;
				}
			}
			c.getPA().showInterface(8134);
        	break;
            case 12007:
            case 22106:
            case 12936:
                c.getDH().sendDialogues(361, 8819);
                break;
        case 6830:   
                    c.getItems().deleteItem(6830, 1);
                    c.getItems().addItem(11021, 1); 
                    c.getItems().addItem(11019, 1);
                    c.getItems().addItem(11020, 1);
                    c.getItems().addItem(11022, 1);     
                    c.getItems().addItem(4566, 1);     
                    c.getItems().addItem(1959, 1);   
                    c.getItems().addItem(1961, 1);     
                    c.getItems().addItem(1989, 1);     
                    c.getItems().addItem(6863, 1);     
                    c.getItems().addItem(6862, 1);     
                    c.gfx100(263);
        	

        	
            break;
            case 2697:
                if (c.getItems().playerHasItem(2697, 1)) {
                    c.getItems().deleteItem(2697, 1);
                    c.gfx100(263);
                    c.donatorPoints += 10;
                    c.amDonated += 10;
                    c.sendMessage("Thank you for donating! 10$ has been added to your total credit.");
                    c.updateRank();
                    c.getPA().closeAllWindows();
                    return;
                }
                break;
            case 6121:
            	if (c.barbarian == false && c.getItems().playerHasItem(6121)) {
            		c.getItems().deleteItem(6121, 1);
            		c.barbarian = true;
            		c.getDH().sendStatement("You may now use ::vials to turn off and on vial smashing!");
            		
            	}else if (c.barbarian == true) {
            		c.sendMessage("You already learned how to do this");
            	}
            	break;
            case 299:
            	if (c.getRegionProvider().getClipping(c.absX, c.absY, c.heightLevel) != 0
				|| Server.getGlobalObjects().anyExists(c.absX, c.absY, c.heightLevel)) {
            		c.sendMessage("You cannot plant a flower here.");
				return;
            	}
                if (!c.getItems().playerHasItem(15098, 1)) {
            		c.sendMessage("You need to purchase a dice bag to use mithril seeds.");
                	return;
                }
                if(!Boundary.isIn(c, DICING_AREA)) {
                    c.sendMessage("You must be in the dice area to plant mithril seeds.");
                    return;
                }
                if(System.currentTimeMillis() - c.lastPlant < 250) {
                    return;
                }
                c.lastPlant = System.currentTimeMillis();
                plantMithrilSeed(c);
                break;
            case 21027: //dark relic
            	  c.inLamp = true;
            	  c.usingLamp = true;
                  c.normalLamp = true;
                  c.antiqueLamp = false;
                  c.sendMessage("You rub the lamp...");
                  c.getPA().showInterface(2808);
            	break;
            case 13148: //dark relic
            	c.inLamp = true;
            	c.usingLamp = true;
                c.normalLamp = true;
                c.antiqueLamp = false;
                c.sendMessage("You have rubbed the skill reset lamp.");
                c.getPA().showInterface(2808);
          	break;
            case 2403: //donator scrolls
            case 2396:
            case 786:
            case 761:
            case 607:
            case 608:
            	c.getDH().sendDialogues(688, 9120);
                break;
            case 13346:
        		if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox)) {
        			c.getPA().showInterface(47000);
        			c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
        			return;
        		}
    			else if (c.getItems().playerHasItem(13346)) {
                	c.inDonatorBox = true;
                    c.getUltraMysteryBox().openInterface();
                    return;
                }
                break;
            case 6199:
        		if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox)) {
        			c.getPA().showInterface(47000);
        			c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
        			return;
        		}
    			else if (c.getItems().playerHasItem(6199)) {
                    c.getNormalMysteryBox().openInterface();
                	c.inDonatorBox = true;
    				c.stopMovement();
                    return;
                }
                break;
            case 6828:
        		if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox)) {
        			c.getPA().showInterface(47000);
        			c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
        			return;
        		}
    			else if (c.getItems().playerHasItem(6828)) {
            		c.getSuperMysteryBox().openInterface();
                	c.inDonatorBox = true;
    				c.stopMovement();
                    return;
                }
                break;
            case 21347:
                c.boltTips = true;
                c.arrowTips = false;
                c.javelinHeads = false;
                c.sendMessage("Your Amethyst method is now Bolt Tips!");
                break;
            case 20724:
                DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(session)) {
                    if (session.getRules().contains(Rule.NO_DRINKS)) {
                        c.sendMessage("Using the imbued heart with 'No Drinks' option is forbidden.");
                        return;
                    }
                }
                if (System.currentTimeMillis() - c.lastHeart < 420000) {
                    c.sendMessage("You must wait 7 minutes between each use.");
                } else {
                    c.getPA().imbuedHeart();
                    c.lastHeart = System.currentTimeMillis();
                }
                break;
            case 405: //Pvm Casket
            	if (System.currentTimeMillis() - c.openCasketTimer < 350) {
                    c.sendMessage("You must wait 3 seconds before opening again.");
                    break;
                } else {
                	if (c.getItems().playerHasItem(405)) {
                        c.getPvmCasket().roll(c);
                        c.openCasketTimer =  System.currentTimeMillis();
                        break;
                    }
                    
                }
    		case 12873://guthan
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12873, 1)) {
    				c.getItems().addItem(4724, 1);
    				c.getItems().addItem(4726, 1);
    				c.getItems().addItem(4728, 1);
    				c.getItems().addItem(4730, 1);
    			}
    	            c.getItems().deleteItem(12873, 1);
    			break;
    		case 12875://verac
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12875, 1)) {
    				c.getItems().addItem(4753, 1);
    				c.getItems().addItem(4755, 1);
    				c.getItems().addItem(4757, 1);
    				c.getItems().addItem(4759, 1);
    			}
    	            c.getItems().deleteItem(12875, 1);
    			break;
    		case 12877://dharok
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12877, 1)) {
    				c.getItems().addItem(4716, 1);
    				c.getItems().addItem(4718, 1);
    				c.getItems().addItem(4720, 1);
    				c.getItems().addItem(4722, 1);
    			}
    	            c.getItems().deleteItem(12877, 1);
    			break;
    		case 12879://torags
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12879, 1)) {
    				c.getItems().addItem(4745, 1);
    				c.getItems().addItem(4747, 1);
    				c.getItems().addItem(4749, 1);
    				c.getItems().addItem(4751, 1);
    			}
    	            c.getItems().deleteItem(12879, 1);
    			break;
    		case 12881://ahrims
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12881, 1)) {
    				c.getItems().addItem(4708, 1);
    				c.getItems().addItem(4710, 1);
    				c.getItems().addItem(4712, 1);
    				c.getItems().addItem(4714, 1);
    			}
    	            c.getItems().deleteItem(12881, 1);
    			break;
    		case 12883://karils
    			if (c.getItems().freeSlots() < 4) {
    				c.sendMessage("You need at least 4 free slots to open this.");
    				return;
    			}
    			if (c.getItems().playerHasItem(12883, 1)) {
    				c.getItems().addItem(4732, 1);
    				c.getItems().addItem(4734, 1);
    				c.getItems().addItem(4736, 1);
    				c.getItems().addItem(4738, 1);
    			}
    	            c.getItems().deleteItem(12883, 1);
    			break;
            case 10006:
                Hunter.lay(c, new BirdSnare(c));
                break;
            case 10008:
                Hunter.lay(c, new BoxTrap(c));
                break;
            case 13249:
            	if (!c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")
						&& !c.getSlayer().getTask().get().getPrimaryName().equals("hellhound")) {
					c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
					return;
            	}
            	c.getItems().deleteItem(13249, 1);
            	Cerberus cerb = c.createCerberusInstance();
					if (!c.getSlayer().isCerberusRoute()) {
						c.sendMessage("You have bought Route into cerberus cave. please wait till you will be teleported.");
						c.getCerberus().init();
						c.cerbDelay = System.currentTimeMillis();
						return;
					}
				if (c.getCerberusLostItems().size() > 0) {
					c.getDH().sendDialogues(642, 5870);
					c.nextChat = -1;
					return;
				}

				if (cerb == null) {
					c.sendMessage("We are unable to allow you in at the moment.");
					c.sendMessage("Too many players.");
					return;
				}

				if (Server.getEventHandler().isRunning(c, "cerb")) {
					c.sendMessage("You're about to fight start the fight, please wait.");
					return;
				}
				c.getCerberus().init();
                break;

            case 13226:
                c.getHerbSack().fillSack();
                break;

            case 12020:
                c.getGemBag().fillBag();
                break;

            case 5509:
                Pouches.fill(c, Pouch.forId(itemId), itemId, 0);
                break;
            case 5510:
                Pouches.fill(c, Pouch.forId(itemId), itemId, 1);
                break;
            case 5512:
                Pouches.fill(c, Pouch.forId(itemId), itemId, 2);
                break;

            /*case 952: //Spade
                int x = c.absX;
                int y = c.absY;
                if (Boundary.isIn(c, Barrows.GRAVEYARD)) {
                    c.getBarrows().digDown();
                }
                if (x == 3005 && y == 3376 || x == 2999 && y == 3375 || x == 2996 && y == 3377) {
                    if (!c.getRechargeItems().hasItem(13120)) {
                        c.sendMessage("You must have the elite falador shield to do this.");
                        return;
                    }
                    c.getPA().movePlayer(1760, 5163, 0);
                }
                break;*/
        }

        if (itemId == 2678) {
            c.getDH().sendDialogues(657, -1);
            return;
        }
        if (itemId == 8015 || itemId == 8014) {
            NonCombatSpells.attemptDate(c, itemId);
        }
        if (itemId == 9553) {
            c.getPotions().eatChoc(9553, -1, itemSlot, 1, true);
        }
        if (itemId == 12846) {
            c.getDH().sendDialogues(578, -1);
        }
        if (itemId == 12938) {
            if (c.getZulrahEvent().getInstancedZulrah() != null) {
                c.sendMessage("It seems your currently in the zulrah instance, relog if this is false.");
                return;
            }
            c.getDH().sendDialogues(625, -1);
            return;
        }
        if (itemId == 4155) {
            if (!c.getSlayer().getTask().isPresent()) {
                c.sendMessage("You do not have a task, please talk with a slayer master!");
                return;
            }
            c.sendMessage("I currently have " + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + "'s to kill.");
            c.getPA().closeAllWindows();
        }
        if (itemId == 2839) {
            if (c.getSlayer().getUnlocks().contains(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
                c.sendMessage("You have already learned this recipe. You have no more use for this scroll.");
                return;
            }
            if (c.getItems().playerHasItem(2839)) {
                c.getSlayer().getUnlocks().add(SlayerUnlock.MALEVOLENT_MASQUERADE);
                c.sendMessage("You have learned the slayer helmet recipe. You can now assemble it");
                c.sendMessage("using a Black Mask, Facemask, Nose peg, Spiny helmet and Earmuffs.");
                c.getItems().deleteItem(2839, 1);
            }
        }
        if (itemId == 15098) {
            DiceHandler.rollDice(c);
        }
        if (itemId == 2701) {
            if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
                return;
            }
            if (c.getItems().playerHasItem(2701, 1)) {
                c.getDH().sendDialogues(4004, -1);
            }
        }
        if (itemId == 7509) {
            if (c.inDuelArena() || Boundary.isIn(c, Boundary.DUEL_ARENA)) {
                c.sendMessage("You cannot do this here.");
                return;
            }
            if (c.getHealth().getStatus().isPoisoned() || c.getHealth().getStatus().isVenomed()) {
                c.sendMessage("You are effected by venom or poison, you should cure this first.");
                return;
            }
            if (c.getHealth().getCurrentHealth() <= 1) {
                c.sendMessage("I better not do that.");
                return;
            }
            c.forcedChat("Ow! I nearly broke a tooth!");
            c.startAnimation(829);
            c.appendDamage(1, Hitmark.HIT);
            return;
        }
        if (itemId == 10269) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10269, 1)) {
                c.getItems().addItem(13307, 30000);
                c.getItems().deleteItem(10269, 1);
            }
        }
        if (itemId == 10271) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10271, 1)) {
                c.getItems().addItem(13307, 10000);
                c.getItems().deleteItem(10271, 1);
            }
        }
        if (itemId == 10273) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10273, 1)) {
                c.getItems().addItem(13307, 14000);
                c.getItems().deleteItem(10273, 1);
            }
        }
        if (itemId == 10275) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10275, 1)) {
                c.getItems().addItem(13307, 18000);
                c.getItems().deleteItem(10275, 1);
            }
        }
        if (itemId == 10277) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10277, 1)) {
                c.getItems().addItem(13307, 22000);
                c.getItems().deleteItem(10277, 1);
            }
        }
        if (itemId == 10279) {
            if (c.inWild() || c.inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10279, 1)) {
                c.getItems().addItem(13307, 26000);
                c.getItems().deleteItem(10279, 1);
            }
        }

   
        if (itemId == 11739)
            if (c.getItems().playerHasItem(11739)) {
                c.getHourlyRewardBox().roll(c);
                return;
            }
        
            
            
        if (itemId == 20703) //Daily Gear Box
            if (c.getItems().playerHasItem(20703)) {
                c.getDailyGearBox().open();
                return;
            }
        if (itemId == 20791) //Daily Skilling Box
            if (c.getItems().playerHasItem(20791)) {
                c.getDailySkillBox().open();
                return;
            }
		if (itemId == 7310) //Skill Casket
			if (c.getItems().playerHasItem(7310)) {
				c.getSkillCasket().open();
				return;
			}

        if (itemId == 2714) { // Easy Clue Scroll Casket
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            c.getItems().deleteItem(itemId, 1);
            c.getTrails().addRewards(RewardLevel.EASY);
            c.setEasyClueCounter(c.getEasyClueCounter() + 1);
            c.sendMessage("@blu@You have completed " + c.getEasyClueCounter() + " easy Treasure Trails.");
        }
        if (itemId == 2802) { // Medium Clue Scroll Casket
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            c.getItems().deleteItem(itemId, 1);
            c.getTrails().addRewards(RewardLevel.MEDIUM);
            c.setMediumClueCounter(c.getMediumClueCounter() + 1);
            c.sendMessage("@blu@You have completed " + c.getMediumClueCounter() + " medium Treasure Trails.");
        }
        if (itemId == 2775) { // Hard Clue Scroll Casket
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            c.getItems().deleteItem(itemId, 1);
            c.getTrails().addRewards(RewardLevel.HARD);
            c.setHardClueCounter(c.getHardClueCounter() + 1);
            c.sendMessage("@blu@You have completed " + c.getHardClueCounter() + " hard Treasure Trails.");
        }
        if (itemId == 19841) { // Master Clue Scroll Casket
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            if (c.getItems().playerHasItem(19841)) {
                c.getItems().deleteItem(itemId, 1);
                c.getTrails().addRewards(RewardLevel.MASTER);
                c.setMasterClueCounter(c.getMasterClueCounter() + 1);
                c.sendMessage("@blu@You have completed " + c.getMasterClueCounter() + " master Treasure Trails.");
                if (Misc.random(200) == 2 && c.getItems().getItemCount(19730, true) == 0 && c.summonId != 19730) {
                    PlayerHandler.executeGlobalMessage("[<col=CC0000>Clue</col>] @cr20@ <col=255>" + c.playerName
                            + "</col> hit the jackpot and got a <col=CC0000>Bloodhound</col> pet!");
                    c.getItems().addItemUnderAnyCircumstance(19730, 1);
                }
            }
        }
        if (itemId == 2677) {
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            Achievements.increase(c, AchievementType.CLUES, 1);
            c.getItems().deleteItem(itemId, 1);
            c.getItems().addItem(2714, 1);
            c.sendMessage("You've recieved a easy clue scroll casket.");
        }
        if (itemId == 2801) {
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            Achievements.increase(c, AchievementType.CLUES, 1);
            c.getItems().deleteItem(itemId, 1);
            c.getItems().addItem(2802, 1);
            c.sendMessage("You've recieved a medium clue scroll casket.");
        }
        if (itemId == 2722) {
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            Achievements.increase(c, AchievementType.CLUES, 1);
            c.getItems().deleteItem(itemId, 1);
            c.getItems().addItem(2775, 1);
            c.sendMessage("You've recieved a hard clue scroll casket.");
        }
        /**
         * Master clue scroll
         */
        if (itemId == 19835) {
        	if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots to open this.");
				return;
			}
            MasterClue.progressScroll(c);
        }
        if (itemId == 2528) {
        	c.inLamp = true;
            c.usingLamp = true;
            c.normalLamp = true;
            c.antiqueLamp = false;
            c.sendMessage("You rub the lamp...");
            c.getPA().showInterface(2808);
        }

   }
}