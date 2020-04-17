package ethos.model.npcs.bosses.hydra;

import java.util.stream.Stream;

import ethos.model.players.Boundary;
import lombok.Getter;

@Getter
public enum HydraStage {
	
	//Change Boundaries here to customize.

	POISON(8615, 8616, 8237, -1, 1100, new Boundary(1368, 10260, 1374, 10266)),

	LIGHTNING(8619, 8617, 8244, 8238, 825, new Boundary(1368, 10269, 1374, 10275)),

	FLAME(8620, 8618, 8251, 8245, 550, new Boundary(1359, 10269, 1365, 10275)),

	ENRAGED(8621, 8622, 8257, 8252, 275, AlchemicalHydra.AREA);
	
	

	private final int npcId, health, deathID, deathAnimation;
	private final int transformation;
	private final Boundary boundary;

	HydraStage(int npcId, int deathID, int deathAnimation, int transformation, int health, Boundary boundary) {
		this.npcId = npcId;
		this.transformation = transformation;
		this.health = health;
		this.boundary = boundary;
		this.deathID = deathID;
		this.deathAnimation = deathAnimation;
	}

	public static Stream<HydraStage> stream() {
		return Stream.of(values());
	}

}