package raycasting.entities;

import raycasting.Game;
import raycasting.Texture;

public class TestEntity extends Entity {

	public TestEntity(Game game, double x, double y) {
		super(EntityType.TEST_ENTITY, game, x, y, "res/animate_stickman.png");
	}

	@Override
	public void tick() {
		x += 0.001;
	}
	
}
