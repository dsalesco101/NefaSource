package ethos.clip;

import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCClipping;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class PathChecker {

	public static boolean isProjectilePathClear(Entity entity, Entity other,
			final int x0, final int y0,
			final int z, final int x1, final int y1) {
		
		if (other instanceof NPC && entity instanceof Player) {
			NPC theNPC = (NPC) other;
			Player c = (Player) entity;
			if (Boundary.isIn(c, Boundary.PEST_CONTROL_AREA) || theNPC.npcType == Skotizo.AWAKENED_ALTAR_NORTH 
							|| theNPC.npcType == Skotizo.AWAKENED_ALTAR_SOUTH 
							|| theNPC.npcType == Skotizo.AWAKENED_ALTAR_WEST 
							|| theNPC.npcType == Skotizo.AWAKENED_ALTAR_EAST 
							|| theNPC.npcType == 7559 
							|| theNPC.npcType == 7560
							|| theNPC.npcType == 7706 /* Inferno final boss */) {
				return true;
			}
		}
		
		
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;

		double error = 0;
		final double deltaError = Math.abs(
				(deltaY) / (deltaX == 0
						? ((double) deltaY)
						: ((double) deltaX)));

		int x = x0;
		int y = y0;

		int pX = x;
		int pY = y;

		boolean incrX = x0 < x1;
		boolean incrY = y0 < y1;

		while (true) {
			if (x != x1) {
				x += (incrX ? 1 : -1);
			}

			if (y != y1) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}

			if (!shootable(entity, x, y, z, pX, pY)) {
				return false;
			}

			if (incrX && incrY
					&& x >= x1 && y >= y1) {
				break;
			} else if (!incrX && !incrY
					&& x <= x1 && y <= y1) {
				break;
			} else if (!incrX && incrY
					&& x <= x1 && y >= y1) {
				break;
			} else if (incrX && !incrY
					&& x >= x1 && y <= y1) {
				break;
			}

			pX = x;
			pY = y;
		}

		return true;
	}

	public static boolean isMeleePathClear(Entity entity, final int x0, final int y0,
			final int z, final int x1, final int y1) {
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;

		double error = 0;
		final double deltaError = Math.abs(
				(deltaY) / (deltaX == 0
						? ((double) deltaY)
						: ((double) deltaX)));

		int x = x0;
		int y = y0;

		int pX = x;
		int pY = y;

		boolean incrX = x0 < x1;
		boolean incrY = y0 < y1;

		while (true) {
			if (x != x1) {
				x += (incrX ? 1 : -1);
			}

			if (y != y1) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}

			if (!canAttackOver(entity, x, y, z, pX, pY)) {
				return false;
			}

			if (incrX && incrY
					&& x >= x1 && y >= y1) {
				break;
			} else if (!incrX && !incrY
					&& x <= x1 && y <= y1) {
				break;
			} else if (!incrX && incrY
					&& x <= x1 && y >= y1) {
				break;
			} else if (incrX && !incrY
					&& x >= x1 && y <= y1) {
				break;
			}

			pX = x;
			pY = y;
		}

		return true;
	}

	private static boolean canAttackOver(Entity entity, int x, int y, int z, int pX, int pY) {
		if (x == pX && y == pY) {
			return true;
		}

		int dir = NPCClipping.getDirection(x, y, pX, pY);
		int dir2 = NPCClipping.getDirection(pX, pY, x, y);

		if (dir == -1 || dir2 == -1) {
			System.out.println("NEGATIVE DIRECTION MELEE CLIP CHECK ERROR");
			return false;
		}
		
		return entity.getRegionProvider().canMove(x, y, z, dir)
				&& entity.getRegionProvider().canMove(pX, pY, z, dir2);
	}

	private static boolean shootable(Entity entity, int x, int y, int z, int pX, int pY) {
		if (x == pX && y == pY) {
			return true;
		}

		int dir = NPCClipping.getDirection(x, y, pX, pY);
		int dir2 = NPCClipping.getDirection(pX, pY, x, y);

		if (dir == -1 || dir2 == -1) {
			System.out.println("NEGATIVE DIRECTION PROJECTILE ERROR");
			return false;
		}

		if (entity.getRegionProvider().canMove(x, y, z, dir)
				&& entity.getRegionProvider().canMove(pX, pY, z, dir2)) {
			return true;
		}
		
		return entity.getRegionProvider().canShoot(x, y, z, dir) && entity.getRegionProvider().canShoot(pX, pY, z, dir2);
	}
}
