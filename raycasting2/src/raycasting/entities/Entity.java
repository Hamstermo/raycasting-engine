package raycasting.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import raycasting.Game;
import raycasting.Texture;

public abstract class Entity {

	private final float MAX_ENTITY_DRAW_DIST = 12.0f;
	
	private Texture texture;
	private int currentFrame;
	private int numFrames;
	private int timeSinceFrameChange = 0;
	
	protected EntityType type;
	protected Game game;
	protected double x, y, dist;
	
	public Entity(EntityType type, Game game, double x, double y, String path) {
		this.type = type;
		this.game = game;
		this.x = x;
		this.y = y;
		
		texture = new Texture(path);
		numFrames = texture.getImage().getWidth() / 8;
	}
	
	public abstract void tick();
	
	public void render(Graphics g) {
		animate();
		
		double xDif = x - game.getPlayer().getX();
		double yDif = y - game.getPlayer().getY();
		
		double rot = game.getPlayer().getRotation();
		
		double xx = Math.cos(rot)*xDif + Math.sin(rot)*yDif;
		double yy = Math.cos(rot)*yDif - Math.sin(rot)*xDif;
		
		if (yy > 0) { // only draw if in front of player			
			double[] depth = game.getScreen().getDepth();
			dist = Math.sqrt(xDif*xDif + yDif*yDif);
			
			double f = game.WIDTH/yy;
			
			int size = (int) (game.HEIGHT/yy);
			int screenY = (int) (game.HEIGHT/2 - size/2);
			
			for (int i = 0; i < 8; i++) {
				int screenX = (int) (game.WIDTH/2 + xx*f + (i-4) * size/16);
				
				if (screenX > 0 && screenX < game.WIDTH && dist < MAX_ENTITY_DRAW_DIST) {
					int sampleX = (int) (screenX/game.STRIP_WIDTH);
					
					if (depth[sampleX] > dist) {
						BufferedImage img = texture.getImage().getSubimage(i+currentFrame*8, 0, 1, 16);
						float darkness = (float) (10/(dist*dist*dist));
						if (darkness > 1.0f) darkness = 1.0f;
						RescaleOp op = new RescaleOp(darkness, 0, null);
					    img = op.filter(img, null);
						g.drawImage(img, screenX-size/32, screenY, size/16+1, size, null);
					}
				}
			}
		}
	}
	
	private void animate() {
		timeSinceFrameChange ++;
		
		if (timeSinceFrameChange >= 60) {
			currentFrame ++;
			timeSinceFrameChange = 0;
			
			if (currentFrame >= numFrames)
				currentFrame = 0;
		}
	}
	
	public double getDist() {
		return dist;
	}
	
}
