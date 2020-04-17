package ethos.world.objects;

import ethos.clip.Region;
import ethos.clip.RegionProvider;
import ethos.model.content.instances.InstancedArea;
import ethos.model.players.Position;

/**
 * A global object is a visual model that is viewed by all players within a region. This class represents the identification value, x and y position, as well as the height of the
 * object.
 * 
 * A key factor is the ticks remaining. The ticksRemaining variable represents how many game ticks this object will remain visible for. If the value is negative the object will
 * remain indefinitly. On the flip side, if the value is positive then every tick the total remaining will reduce by one until it hits zero.
 * 
 * @author Jason MacKeigan
 * @date Dec 17, 2014, 6:18:20 PM
 */
public class GlobalObject {

	private int id;

	private int x;

	private int y;

	private int height;

	private int face;

	private int ticksRemaining;

	private int restoreId;

	private int type;
	
	private InstancedArea instance;

	public GlobalObject(int id, int x, int y, int height) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
		this.type = 10;
	}

	public GlobalObject(int id, int x, int y, int height, int face) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
		this.face = face;
	}

	public GlobalObject(int id, int x, int y, int height, int face, int type) {
		this(id, x, y, height, face);
		this.type = type;
	}

	public GlobalObject(int id, int x, int y, int height, int face, int type, int ticksRemaining) {
		this(id, x, y, height, face, type);
		this.ticksRemaining = ticksRemaining;
	}

	public GlobalObject(int id, int x, int y, int height, int face, int type, int ticksRemaining, int restoreId) {
		this(id, x, y, height, face, type, ticksRemaining);
		this.restoreId = restoreId;
	}

	public GlobalObject(int id, Position p) {
		this(id, p.getX(), p.getY(), p.getHeight(), 0, 10, -1, 0);
	}

	public void removeTick() {
		this.ticksRemaining--;
	}

	public int getObjectId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getFace() {
		return face;
	}

	public int getTicksRemaining() {
		return ticksRemaining;
	}

	public int getRestoreId() {
		return restoreId;
	}
	
	public Position getPosition() {
		return new Position(x, y, height);
	}

	public int getType() {
		return type;
	}

	public InstancedArea getInstance() {
		return instance;
	}

	public GlobalObject setInstance(InstancedArea instance) {
		this.instance = instance;
		return this;
	}
	
	public RegionProvider getRegionProvider() {
		if (instance != null) {
			return instance;
		} else {
			return RegionProvider.getGlobal();
		}
	}

	@Override
	public String toString() {
		return "GlobalObject [id=" + id + ", x=" + x + ", y=" + y + ", height=" + height + ", face=" + face
				+ ", ticksRemaining=" + ticksRemaining + ", restoreId=" + restoreId + ", type=" + type + "]";
	}
	
}
