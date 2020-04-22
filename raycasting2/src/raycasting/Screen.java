package raycasting;

import java.awt.Color;
import java.awt.Graphics;

public class Screen {
	
	private final float MAX_WALL_DRAW_DIST = 6.0f;
	
	private Game game;
	private Texture floorTexture, wallTexture;
	
	private double[] depth;
	
	public Screen(Game game) {
		this.game = game;
		
		depth = new double[(int) (game.WIDTH/game.STRIP_WIDTH)];
		
		floorTexture = new Texture("res/floor.png");
		wallTexture = new Texture("res/wall.png");
	}

	public void drawFloorAndCeiling(Graphics g) {
		for (int i = 0; i < game.WIDTH; i += game.STRIP_WIDTH) {
			for (int j = (int) (game.HEIGHT * 3/5); j < game.HEIGHT; j += game.STRIP_WIDTH) {
				double x0 = game.getPlayer().getX();
				double y0 = game.getPlayer().getY();
				double rot = game.getPlayer().getRotation();
				
				double xx = (double) (i-game.WIDTH/2)/game.WIDTH;
				
				double dx = Math.cos(rot)*xx - Math.sin(rot);
				double dy = Math.sin(rot)*xx + Math.cos(rot);
				double dz = (double) (j-game.HEIGHT/2)/game.HEIGHT;
				
				double scalar = 0.5/dz;
				
				double x = x0 + scalar*dx;
				double y = y0 + scalar*dy;
				
				double floorX = Math.floor(x);
				double floorY = Math.floor(y);
				
				double distX = x - x0;
				double distY = y - y0;
				double dist = Math.sqrt(distX*distX + distY*distY);

				int sampleX = (int) ((x-floorX)*16);
				int sampleY = (int) ((y-floorY)*16);
				
				int[] pixel = floorTexture.getPixel(sampleX, sampleY);
				
				if (dist < 1) dist = 1;
				
				pixel[0] = (int) (pixel[0] / (dist*dist*dist));
				pixel[1] = (int) (pixel[1] / (dist*dist*dist));
				pixel[2] = (int) (pixel[2] / (dist*dist*dist));
				
				g.setColor(new Color(pixel[0], pixel[1], pixel[2]));
				
				g.fillRect(i, j, game.STRIP_WIDTH, game.STRIP_WIDTH);
				g.fillRect(i, (game.HEIGHT-j), game.STRIP_WIDTH, game.STRIP_WIDTH);
			}
		}
	}
	
	public void drawWalls(Graphics g) {
		for (int i = 0; i < game.WIDTH; i += game.STRIP_WIDTH) {
			double x0 = game.getPlayer().getX();
			double y0 = game.getPlayer().getY();
			double rot = game.getPlayer().getRotation();
			
			double xx = (double) (i-game.WIDTH/2)/game.WIDTH;
			double dx = Math.cos(rot)*xx - Math.sin(rot);
			double dy = Math.sin(rot)*xx + Math.cos(rot);
				
			float step = 0.01f;
			float dist = 0f;
				
			while (dist < MAX_WALL_DRAW_DIST) {
				dist += step;
				
				double x = x0 + dist*dx;
				double y = y0 + dist*dy;
				
				int floorX = (int) Math.floor(x);
				int floorY = (int) Math.floor(y);
				
				if (game.getMap()[floorX][floorY] == 1) {					
					int stripHeight = (int) (game.HEIGHT/dist);
					
					float midX = floorX + 0.5f;
					float midY = floorY + 0.5f;
					
					float testAngle = (float) Math.atan2(y-midY, x-midX);
					
					for (int j = 0; j < stripHeight; j += game.STRIP_WIDTH) {
						if (game.HEIGHT/2-stripHeight/2+j > 0 && game.HEIGHT/2-stripHeight/2+j < game.HEIGHT) {
							int sampleX = 0;
							
							// find which side has been hit and sample appropriately
							if (testAngle >= -Math.PI*0.25 && testAngle < Math.PI*0.25)
								sampleX = (int) ((y-floorY)*16);
							else if (testAngle >= Math.PI*0.25 && testAngle < Math.PI*0.75)
								sampleX = (int) ((x-floorX)*16);
							else if (testAngle < -Math.PI*0.25 && testAngle >= -Math.PI*0.75)
								sampleX = (int) ((x-floorX)*16);
							else if (testAngle >= Math.PI*0.75 || testAngle < -Math.PI*0.75)
								sampleX = (int) ((y-floorY)*16);
						
							int sampleY = (int) ((float)j/(float)stripHeight*16.0f);
							
							int[] pixel = wallTexture.getPixel(sampleX, sampleY);
							
							if (dist < 1) dist = 1;
							
							pixel[0] = (int) (pixel[0] / (dist*dist*dist));
							pixel[1] = (int) (pixel[1] / (dist*dist*dist));
							pixel[2] = (int) (pixel[2] / (dist*dist*dist));
							
							g.setColor(new Color(pixel[0], pixel[1], pixel[2]));
							g.fillRect(i, game.HEIGHT/2-stripHeight/2+j, game.STRIP_WIDTH, game.STRIP_WIDTH);
						}
					}
					
					break;
				}
				
				depth[i/game.STRIP_WIDTH] = dist;
			}
		}
	}
	
	public double[] getDepth() {
		return depth;
	}
	
}
