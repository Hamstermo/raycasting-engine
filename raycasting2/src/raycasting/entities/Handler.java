package raycasting.entities;

import java.awt.Graphics;
import java.util.Comparator;
import java.util.LinkedList;

public class Handler {

	private LinkedList<Entity> entities = new LinkedList<Entity>();
	private Comparator<Entity> renderSorter = new Comparator<Entity>() {
		@Override
		public int compare(Entity a, Entity b) {
			if (a.getDist() < b.getDist())
				return 1;
			return -1;
		}
	};
	
	public void tick() {
		for (Entity entity : entities) {
			entity.tick();
		}
		
		entities.sort(renderSorter);
	}
	
	public void render(Graphics g) {
		for (Entity entity : entities) {
			entity.render(g);
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
}
