package ethos.clip;

import java.util.HashMap;
import java.util.Map;

public class RegionProvider {
	
	private static final RegionProvider GLOBAL = new RegionProvider();
	
	public static RegionProvider getGlobal() {
		return GLOBAL;
	}
	
	private final Map<Integer, Region> regions = new HashMap<>();
	
	public RegionProvider() { }
	
	public Region get(int x, int y) {
		return regions.get(getHash(x, y));
	}

	public boolean contains(int x, int y) {
		return regions.get(getHash(x, y)) != null;
	}
	
	public int getHash(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		return ((regionX / 8) << 8) + (regionY / 8);
	}
	
	public void add(Region region) {
		regions.put(region.id(), region);
	}

	public Map<Integer, Region> getRegions() {
		return regions;
	}
	
	public int getClipping(int x, int y, int height) {
		if (height > 3)
			height %= 4;
		Region r = get(x, y);
		if (r != null) {
			return r.getClip(x, y, height);
		}
		return 0;
	}
	
	public boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height %= 4;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				// System.out.println("[FATAL ERROR]: At getClipping: " + x + ", "
				// + y + ", " + height + ", " + moveTypeX + ", "
				// + moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}
	
	public boolean blockedNorth(int x, int y, int z) {
    	return (getClipping(x, y + 1, z) & 0x1280120) != 0;
    }
    public boolean blockedEast(int x, int y, int z) {
    	return (getClipping(x + 1, y, z) & 0x1280180) != 0;
    }
    public boolean blockedSouth(int x, int y, int z) {
    	return (getClipping(x, y - 1, z) & 0x1280102) != 0;
    }
    public boolean blockedWest(int x, int y, int z) {
    	return (getClipping(x - 1, y, z) & 0x1280108) != 0;
    }
    public boolean blockedNorthEast(int x, int y, int z) {
    	return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
    }
    public boolean blockedNorthWest(int x, int y, int z) {
    	return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
    }
    public boolean blockedSouthEast(int x, int y, int z) {
    	return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
    }
    public boolean blockedSouthWest(int x, int y, int z) {
    	return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
    }
    
    public boolean canMove(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !blockedNorthWest(x, y, z) && !blockedNorth(x, y, z)
					&& !blockedWest(x, y, z);
		} else if (direction == 1) {
			return !blockedNorth(x, y, z);
		} else if (direction == 2) {
			return !blockedNorthEast(x, y, z) && !blockedNorth(x, y, z)
					&& !blockedEast(x, y, z);
		} else if (direction == 3) {
			return !blockedWest(x, y, z);
		} else if (direction == 4) {
			return !blockedEast(x, y, z);
		} else if (direction == 5) {
			return !blockedSouthWest(x, y, z) && !blockedSouth(x, y, z)
					&& !blockedWest(x, y, z);
		} else if (direction == 6) {
			return !blockedSouth(x, y, z);
		} else if (direction == 7) {
			return !blockedSouthEast(x, y, z) && !blockedSouth(x, y, z)
					&& !blockedEast(x, y, z);
		}
		return false;
	}
    
    public boolean blockedNorthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y + size + 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y + 1, z) != 0);
	}

	public boolean blockedEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y, z) != 0);
	}

	public boolean blockedSouthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y - 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y - 1, z) != 0);
	}

	public boolean blockedWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y, z) != 0);
	}

	public boolean blockedNorthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y + 1, z) != 0);
	}

	public boolean blockedNorthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y + 1, z) != 0);
	}

	public boolean blockedSouthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y - 1, z) != 0);
	}

	public boolean blockedSouthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y - 1, z) != 0);
	}
	
	public boolean canShoot(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !projectileBlockedNorthWest(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 1) {
			return !projectileBlockedNorth(x, y, z);
		} else if (direction == 2) {
			return !projectileBlockedNorthEast(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		} else if (direction == 3) {
			return !projectileBlockedWest(x, y, z);
		} else if (direction == 4) {
			return !projectileBlockedEast(x, y, z);
		} else if (direction == 5) {
			return !projectileBlockedSouthWest(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 6) {
			return !projectileBlockedSouth(x, y, z);
		} else if (direction == 7) {
			return !projectileBlockedSouthEast(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		}
		return false;
	}

	public int getProjectileClipping(int x, int y, int height) {
		if (height > 3)
			height %= 4;
		Region r = get(x, y);
		if (r != null) {
			return r.getProjectileClip(x, y, height);
		}
		return 0;
	}
	
	public boolean projectileBlockedNorth(int x, int y, int z) {
		return (getProjectileClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public boolean projectileBlockedEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public boolean projectileBlockedSouth(int x, int y, int z) {
		return (getProjectileClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public boolean projectileBlockedWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public boolean projectileBlockedNorthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public boolean projectileBlockedNorthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public boolean projectileBlockedSouthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public boolean projectileBlockedSouthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}
}
