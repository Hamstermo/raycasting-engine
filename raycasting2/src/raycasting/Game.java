package raycasting;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

import raycasting.entities.Handler;
import raycasting.entities.TestEntity;

public class Game implements Runnable {
	
	public final int WIDTH = 640;
	public final int HEIGHT = 480;
	public final int STRIP_WIDTH = 5;
	
	private JFrame frame;
	private Canvas canvas;
	
	private byte[][] map = new byte[16][16];
	
	private Player player;
	private Screen screen;
	
	private Handler handler;
	
	public Game() {
		frame = new JFrame("Raycasting");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas = new Canvas());
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Generating world
		Random rand = new Random();
		
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (rand.nextInt(10) == 1 || x == 0 || x == map.length-1 || y == 0 || y == map[0].length-1) {
					map[x][y] = 1; 
				}
			}
		}
		
		map[6][8] = 1;
		
		player = new Player(this, map.length/2, map.length/2, 0);
		frame.addKeyListener(player);
		
		screen = new Screen(this);
		
		handler = new Handler();
		handler.addEntity(new TestEntity(this, map.length/2+0.5, 10.5));
		handler.addEntity(new TestEntity(this, map.length/2+1.5, 10.5));
		
		new Thread(this).start();
	}
	
	
	
	private void tick() {
		player.tick();
		handler.tick();
	}
	
	private void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		screen.drawFloorAndCeiling(g);
		screen.drawWalls(g);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Game();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
		   
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames + " Ticks: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public byte[][] getMap() {
		return map;
	}
	
}
