/**
 * 
 */
package ethos.model.players.combat;


import ethos.Config;
import ethos.clip.PathChecker;
import ethos.model.content.SkillcapePerks;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.clip.doors.Location;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCClipping;
import ethos.model.npcs.NPCDumbPathFinder;
import ethos.model.players.Player;
import ethos.model.players.skills.Skill;
import ethos.util.Misc;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ReverendDread
 * Mar 9, 2019
 */
@Slf4j
public abstract class CombatScript {
	
	/**
	 * Handles an attack for an {@link NPC} vs another {@link Entity}.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param source
	 * 			the target.
	 * @return
	 * 			the delay of the attack in ticks.
	 */
	public abstract int attack(final NPC npc, final Entity target);
	
	/**
	 * Gets the attack distance for the npc.
	 * 
	 * @param npc
	 * 			the npc.
	 * @return
	 * 			the attack distance.
	 */
	public abstract int getAttackDistance(final NPC npc);
	
	/**
	 * If the npc ignores projectile clipping, to prevent safe spotting.
	 * 
	 * @return
	 * 			true to ignore clipping, false otherwise.
	 */
	public abstract boolean ignoreProjectileClipping();
	
	/**
	 * Gets the max follow distance before returning to respawn tile area.
	 * 
	 * @param npc
	 * 			the npc.
	 * @return
	 */
	public int getFollowDistance(final NPC npc) {
		return 16;
	}
	
	/**
	 * If the npc always follows the target.
	 * @return
	 */
	public boolean followClose() {
		return false;
	}
	
	/**
	 * If script will ignore npc collision so player can flinch it.
	 * @return
	 */
	public boolean ignoreCollision() {
		return true;
	}
	
	/**
	 * Handles melee hits for npcs.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @param projectile 
	 * 			the projectile.
	 * @param hit
	 * 			the hit.
	 */
	public final void handleHit(final NPC npc, final Entity target, CombatType type, Projectile projectile, Graphic endGraphic, Hit hit) {
		//System.out.println("handleHit: npc - " + npc.npcType + ", type - " + type.name() + ", hit - " + hit.getDamage());
		sendProjectile(npc, target, projectile);
		npc.attackType = type;
		applyHitmark(npc, target, hit, endGraphic);
	}
	
	/**
	 * Handles melee hits for npcs.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @param hit
	 * 			the hit.
	 */
	public final void handleHit(final NPC npc, final Entity target, CombatType type, Projectile projectile, Hit hit) {
		handleHit(npc, target, type, projectile, null, hit);
	}
	
	/**
	 * Handles melee hits for npcs.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @param hit
	 * 			the hit.
	 */
	public final void handleHit(final NPC npc, final Entity target, CombatType type, Graphic endGraphic, Hit hit) {
		handleHit(npc, target, type, null, endGraphic, hit);
	}
	
	/**
	 * Handles melee hits for npcs.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @param hit
	 * 			the hit.
	 */
	public final void handleHit(final NPC npc, final Entity target, CombatType type, Hit hit) {
		handleHit(npc, target, type, null, null, hit);
	}
	
	/**
	 * Checks attack distance for npc, can be overriden for special cases such as multi combat type scripts.
	 * @return
	 */
	public void attackStyleChange(final NPC npc, final Entity target) {
	
	}
	
	/**
	 * Gets the monsters damage reduction amount.
	 * @return
	 */
	public double getDamageReduction(final NPC npc) {
		return 1.0;
	}
	
	/**
	 * If the npc is aggressive or not.
	 * @return
	 */
	public boolean isAggressive() {
		return false;
	}
	
	/**
	 * Applies the hit info to the npc's next attack.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param hit
	 * 			the hit.
	 */
	public final void applyHitmark(final NPC npc, final Entity target, final Hit hit, final Graphic graphic) {
		if (hit != null && npc.getLocation().withinDistance(new Location(target.getX(), target.getY(), target.getHeight()), 32)) {
			CycleEvent damageEvent = new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (target.getHealth().getCurrentHealth() - hit.getDamage() < 0) {
						hit.damage = target.getHealth().getCurrentHealth();
					}
					if (target.isPlayer()) {
						target.asPlayer().logoutDelay = System.currentTimeMillis();
					}
					if (hit.getDamage() > -1) {
						target.appendDamage(hit.getDamage(), (hit.getDamage() > 0 ? hit.getType() : Hitmark.MISS));
						target.addDamageTaken(npc, hit.getDamage());
					}	
					container.stop();
				}		
			};
			CycleEventHandler.getSingleton().addEvent(new CycleEventContainer(0, npc, damageEvent, hit.getDelay()));
			if (graphic != null) {
				//Handles end graphics being sent when hit is received.
				CycleEvent gfxEvent = new CycleEvent() {
		
					@Override
					public void execute(CycleEventContainer container) {
						if (target.isPlayer()) {
							target.asPlayer().gfx(graphic.getId(), graphic.getHeight());
						} else {
							target.asNPC().gfx100(graphic.getId(), graphic.getHeight());
						}
						container.stop();
					}
					
				};
				//Add the cycle event
				CycleEventHandler.getSingleton().addEvent(new CycleEventContainer(0, npc, gfxEvent, hit.getDelay()));
			}
		}
		//Ring of life effect.
		if (target.isPlayer()) {
			int hitpoints = target.asPlayer().getHealth().getCurrentHealth() - hit.getDamage();
			if (hitpoints > 0 && hitpoints < target.asPlayer().getHealth().getMaximumHealth() / 10) {
				boolean defenceCape = SkillcapePerks.DEFENCE.isWearing(target.asPlayer());
				boolean maxCape = SkillcapePerks.isWearingMaxCape(target.asPlayer());
				if (target.asPlayer().getItems().isWearingItem(2570) || defenceCape || (maxCape && target.asPlayer().getRingOfLifeEffect())) {
					if (System.currentTimeMillis() - target.asPlayer().teleBlockDelay < target.asPlayer().teleBlockLength) {
						target.asPlayer().sendMessage("The ring of life effect does not work as you are teleblocked.");
						return;
					}
					if (defenceCape || maxCape) {
						target.asPlayer().sendMessage("Your cape activated the ring of life effect and saved you!");
					} else {
						target.asPlayer().getItems().deleteEquipment(2570, target.asPlayer().playerRing);
						target.asPlayer().sendMessage("Your ring of life saved you!");
					}
					target.asPlayer().getPA().spellTeleport(3087, 3499, 0, false);
				}
			}
		}
	}
	
	/**
	 * Handles combat processing for combat scripts.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 */
	public void process(final NPC npc, final Entity target) {
		handleFollowing(npc, target);
	}
	
	/**
	 * Handles special following for combat scripts.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 */
	public void handleFollowing(final NPC npc, final Entity target) {
		npc.facePlayer(target.getIndex());
		npc.randomWalk = false;
		
		int size = ignoreCollision() ? 1 : npc.getSize();
		boolean collides = target.collides(npc.getX(), npc.getY(), size, target.getX(), target.getY(), 1);
		
		if (collides) {
			stepAway(npc);
		}
		
		attackStyleChange(npc, target);
		
		if (!followClose() && npc.getDistance(target.getX(), target.getY()) <= ((double) getAttackDistance(npc)) + (npc.getSize() > 1 ? 0.5 : 0.0)) {
			return;
		}
		
		if ((npc.getX() < (npc.makeX + getFollowDistance(npc)) && npc.getX() > (npc.makeX - getFollowDistance(npc)) &&
			npc.getY() < (npc.makeY + getFollowDistance(npc)) && npc.getY() > (npc.makeY - getFollowDistance(npc)))) {
			if (npc.getHeight() == target.getHeight()) {
				NPCDumbPathFinder.follow(npc, target);
			}
		} else {
			npc.facePlayer(0);
			npc.randomWalk = true;
			npc.underAttack = false;
			//TODO make npc walk back to respawn tile when away from it, when it loses target.
		}
	}
	
	/**
	 * Handles an attack that is dodgeable by the target if moved.
	 * 
	 * @param npc
	 * 			the npc.
	 * @param targets
	 * 			the targets.
	 * @param projectile
	 * 			the projectile.
	 * @param graphic
	 * 			the hit gfx.
	 * @param hit
	 * 			the hit.
	 */
	public final void handleDodgableAttack(final NPC npc, final Entity target, final Projectile projectile, final Graphic graphic, final Hit hit) {
		if (npc == null || target == null)
			return;	
		final Location hitLoc = new Location(target.getX(), target.getY(), target.getHeight());
		if (target.isPlayer()) {
			sendProjectileToTile(npc, target, projectile);
		}
		CycleEvent event = new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (target.getX() != hitLoc.getX() || target.getY() != hitLoc.getY() || target.getHeight() != hitLoc.getZ()) {
					container.stop();
					return;
				}
				hit.setDelay(2);
				applyHitmark(npc, target, hit, graphic);
				container.stop();
			}
			
		};
		int sync = projectile.getHitSyncToTicks(npc.getLocation(), new Location(target.getX(), target.getY(), target.getHeight()));
		CycleEventHandler.getSingleton().addEvent(new CycleEventContainer(0, npc, event, sync));
	}
	
	/**
	 * Handles stepping away.
	 * 
	 * @param npc
	 * 			the npc.
	 */
	public final void stepAway(final NPC npc) {
		int[][] points = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] delta : points) {
			int dir = NPCClipping.getDirection(delta[0], delta[1]);
			if (NPCDumbPathFinder.canMoveTo(npc, dir)) {
				NPCDumbPathFinder.walkTowards(npc, npc.getX() + NPCClipping.DIR[dir][0], npc.getY() + NPCClipping.DIR[dir][1]);
				break;
			}
		}
	}
	
	/**
	 * Sends a projectile from the npc to the target.
	 * 
	 * @param npc
	 * @param target
	 * @param projectile
	 * TODO finish projectiles
	 */
	public void sendProjectile(final NPC npc, final Entity target, Projectile projectile) {
		if (projectile == null || !target.isPlayer())
			return;
		int centerX = npc.getX() + npc.getSize() / 2;
		int centerY = npc.getY() + npc.getSize() / 2;
		int offX = ((npc.getX() + projectile.getOffset()) - target.getX()) * -1;
		int offY = ((npc.getY() + projectile.getOffset()) - target.getY()) * -1;
		target.asPlayer().getPA().createPlayersProjectile(centerX, centerY, offX, offY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -target.getIndex() - 1, 65, projectile.getDelay());  
	}
	
	/**
	 * Sends a projectile from the npc to the targets location.
	 * @param npc
	 * @param target
	 * @param projectile
	 */
	public void sendProjectileToTile(final NPC npc, final Entity target, Projectile projectile) {
		if (projectile == null || !target.isPlayer()) 
			return;
		final Location loc = new Location(target.getX(), target.getY(), target.getHeight());
		int centerX = npc.getX() + npc.getSize() / 2;
		int centerY = npc.getY() + npc.getSize() / 2;
		int x1 = loc.getX();
		int y1 = loc.getY();
		int offsetY = (centerX - x1) * -1;
		int offsetX = (centerY - y1) * -1;
		target.asPlayer().getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
	}
	
	/**
	 * Sends a projectile from the npc to the targets location.
	 * @param npc
	 * @param target
	 * @param projectile
	 */
	public void sendProjectileToTile(final NPC npc, final Entity target, final Location location, Projectile projectile) {
		if (projectile == null || !target.isPlayer()) 
			return;
		int centerX = npc.getX() + npc.getSize() / 2;
		int centerY = npc.getY() + npc.getSize() / 2;
		int x1 = location.getX();
		int y1 = location.getY();
		int offsetY = (centerX - x1) * -1;
		int offsetX = (centerY - y1) * -1;
		target.asPlayer().getPA().createPlayersProjectile(centerX, centerY, offsetX, offsetY, projectile.getAngle(), projectile.getSpeed(), projectile.getGfx(), projectile.getStartHeight(), projectile.getEndHeight(), -1, 65, projectile.getDelay());
	}
	
	/**
	 * Handles attacking for an npc.
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @return
	 * 			if the attack can be performed.
	 */
	public final boolean handleAttack(final NPC npc, final Entity target) {
		if (!checkConditions(npc, target)) {
			return false;
		}
		//Distance from target to npc.
		int distance = target.distanceToPoint(npc.getX(), npc.getY());
		boolean hasDistance = npc.getDistance(target.getX(), target.getY()) <= ((double) getAttackDistance(npc)) + (npc.getSize() > 1 ? 0.5 : 0.0);
		if (ignoreProjectileClipping()) {
			if (distance < 10) {
				if (target.isPlayer()) { //Player target
					target.asPlayer().getPA().removeAllWindows();
					npc.oldIndex = target.getIndex();
					target.asPlayer().underAttackByNpc = npc.getIndex();
					target.asPlayer().singleCombatDelay2 = System.currentTimeMillis();
				} else { //NPC target TODO
					
				}
			}
		}
		if (hasDistance) {
			if (npc.attackType == CombatType.MAGE || npc.attackType == CombatType.RANGE) {
				if (!PathChecker.isProjectilePathClear(npc, target, npc.getX(), npc.getY(), npc.getHeight(), target.getX(), target.getY())
						&& !PathChecker.isProjectilePathClear(npc, target, target.getX(), target.getY(), npc.getHeight(), npc.getX(), npc.getY())) {
					return false;
				}
			}
		}
		npc.attackTimer = attack(npc, target);
		return true;
	}
	
	/**
	 * Checks the preconditions to initiating combat.
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @return
	 * 			true if combat can be initiated, false otherwise.
	 */
	public final boolean checkConditions(final NPC npc, final Entity target) {
		boolean isPlayer = target.isPlayer();
		if (npc.isDead || npc.getHealth().getCurrentHealth() <= 0) {
			log.debug("Stopping script, npc is dead.");
			resetCombat(npc);
			return false;
		}
		if (isPlayer && target.asPlayer().isInvisible()) {
			return false;
		}
		if (isPlayer && target.asPlayer().getBankPin().requiresUnlock()) {
			target.asPlayer().getBankPin().open(2);
			return false;
		}
		if (npc.inMulti() && npc.underAttackBy > 0 && npc.underAttackBy != target.getIndex()) {
			resetCombat(npc);
			return false;
		}
		if (npc.inMulti() && (isPlayer && (target.asPlayer().underAttackByPlayer > 0 && target.asPlayer().underAttackByNpc != npc.getIndex())
				|| (isPlayer && (target.asPlayer().underAttackByNpc > 0 && target.asPlayer().underAttackByNpc != npc.getIndex())))) {
			resetCombat(npc);
			return false;
		}
		if (npc.getHeight() != target.getHeight()) {
			resetCombat(npc);
			return false;
		}
		return true;
	}
	
	/**
	 * Resets the npcs combat.
	 * @param npc
	 * 			the npc.
	 */
	public final void resetCombat(final NPC npc) {
		npc.killerId = 0;
		npc.facePlayer(0);
		npc.underAttack = false;
		npc.randomWalk = true;
		npc.lastRandomlySelectedPlayer = 0;
	}
	
	/**
	 * Gets a random max hit using calculated defensive stats.
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 * @param type
	 * 			the combat type.
	 * @param maxhit
	 * 			the max hit.
	 * @return
	 */
	public final int getRandomMaxHit(final NPC npc, final Entity target, CombatType type, int maxhit) {
		double accuracy = npc.attack;
		double defence;
		if (target.isPlayer()) { //player as target
			Player player = target.asPlayer();
			int def = 0;
			switch (type) {
				case MAGE:
					def = player.getCombat().mageDef();
					break;
				case MELEE:
					def= player.getCombat().calculateMeleeDefence();
					break;
				case RANGE:
					def = player.getCombat().calculateRangeDefence();
					break;
				default:
					break;			
			}
			defence = player.getSkills().getLevel(Skill.DEFENCE) + (2 * def);
		} else { //npc as target
			NPC n = target.asNPC();
			defence = n.defence;
		}
		double probability = accuracy / defence;
		if (probability > 0.90)
			probability = 0.90;
		else if (probability < 0.05)
			probability = 0.05;
		if (probability < Math.random())
			return 0;
		return Misc.random(maxhit);
	}
	
	/**
	 * Represents a hit in npc combat, used for combat scripts.
	 * @author ReverendDread
	 * Mar 9, 2019
	 */
	@Getter @Setter
	public class Hit {

		private Hitmark type;
		private int damage, delay;
		
		public Hit(Hitmark type, int damage, int delay) {
			this.type = type;
			this.damage = damage;
			this.delay = delay;
		}
		
	}
	
	/**
	 * Represents a projectile
	 * @author ReverendDread
	 * Mar 9, 2019
	 */
	@Getter @Setter
	public class Projectile {
	
		public int angle, gfx, startHeight, endHeight, delay, speed, offset;

		public Projectile(int gfx, int startHeight, int endHeight, int delay, int speed, int offset, int angle) {
			this.gfx = gfx;
			this.startHeight = startHeight;
			this.endHeight = endHeight;
			this.delay = delay;
			this.speed = speed;
			this.angle = angle;
		}
		
		/**
		 * Gets the time in milliseconds until the projectile hits the target.
		 * @param from
		 * 			the location from which the projectile originated from.
		 * @param to
		 * 			the location the projectile is going to.
		 * @return
		 * 			the time in milliseconds until the projectile hits the target.
		 */
		public final long getHitSyncToMillis(Location from, Location to) {
			return getHitSyncToTicks(from, to) * Config.CYCLE_TIME; 
		}
		
		/**
		 * Gets the time in ticks till the projectile hits the target.
		 * @param from
		 * 			the location from which the projectile originated from.
		 * @param to
		 * 			the location the projectile is going to.
		 * @return
		 * 			the time in game ticks till the projectile hits the target.
		 */
		public final int getHitSyncToTicks(Location from, Location to) {
			return (int) Math.floor(from.getDistance(to) / 3) + 2;
		}
		
	}
	
	@Getter @Setter
	public class Graphic {
		
		private int id, height;
		
		public Graphic(int id, int height) {
			this.id = id;
			this.height = height;
		}
		
	}
	
	/**
	 * Creates a new instance of this class
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public CombatScript newInstance() throws InstantiationException, IllegalAccessException {
		return this.getClass().newInstance();
	}
	
}
