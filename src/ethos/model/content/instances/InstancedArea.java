package ethos.model.content.instances;

import java.util.ArrayList;
import java.util.Optional;

import ethos.clip.Region;
import ethos.clip.RegionProvider;
import ethos.clip.WorldObject;
import ethos.model.entity.Entity;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.world.objects.GlobalObject;

public abstract class InstancedArea extends RegionProvider {

	/**
	 * The boundary or location for this instanced area
	 */
	protected Boundary boundary;

	/**
	 * The height of this area
	 */
	protected int height;
	
	/**
	 * True if the instance has been disposed.
	 */
	private boolean disposed;

	/**
	 * Creates a new area with a boundary
	 * 
	 * @param boundary the boundary or location
	 * @param height the height of the area
	 */
	public InstancedArea(Boundary boundary, int height) {
		this.boundary = boundary;
		this.height = height;
	}

	@Override
	public Region get(int x, int y) {
		if (contains(x, y)) {
			return super.get(x, y);
		} else {
			Region region = RegionProvider.getGlobal().get(x, y).clone(this);
			add(region);
			return region;
		}
	}

	public void tick(Entity entity) {

	}

	public void dispose() {
		disposed = true;
		onDispose();
	}
	
	public void add(NPC npc) {
		npc.setInstance(this);
	}
	
	public void add(Player player) {
		player.setInstance(this);
	}
	
	public void remove(Player player) {
		player.setInstance(null);
		//if (player.getRights().contains(Right.ADMINISTRATOR)) {
			player.sendMessage("Removed from instance.");
		//}
		//player.reloadMapRegion();
	}
	
	/**
	 * When an instanced area is disposed it should be common to clean up that instanced area so that it can be re-used by others.
	 * <p>
	 * This function is called when the {@link InstancedAreaManager#disposeOf(InstancedArea)} function is referenced.
	 * </p>
	 */
	public abstract void onDispose();

	/**
	 * Determines the height of this area
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * The boundary or location of this instanced area
	 * 
	 * @return the boundary
	 */
	public Boundary getBoundary() {
		return boundary;
	}

	public boolean isDisposed() {
		return disposed;
	}

	
}
