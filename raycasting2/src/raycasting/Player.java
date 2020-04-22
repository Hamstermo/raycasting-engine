package raycasting;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
	
	private final float SPEED = 0.05f;
	private final float ROT_SPEED = 0.04f;
	
	private double x, y, rotation;
	
	private boolean up, down, left, right, rotLeft, rotRight;
	
	private Game game;

	public Player(Game game, double x, double y, double rotation) {
		this.game = game;
		
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}
	
	public void tick() {
		double xPrevious = x;
		double yPrevious = y;
		
		if (up) {
			x += Math.cos(rotation+Math.PI/2) * SPEED;
			y += Math.sin(rotation+Math.PI/2) * SPEED;
		}
		else if (down) {
			x -= Math.cos(rotation+Math.PI/2) * SPEED;
			y -= Math.sin(rotation+Math.PI/2) * SPEED;
		}
		
		/*
		if (left) {
			x -= Math.cos(rotation) * SPEED/2;
			y -= Math.sin(rotation) * SPEED/2;
		}
		else if (right) {
			x += Math.cos(rotation) * SPEED/2;
			y += Math.sin(rotation) * SPEED/2;
		}
		*/
		
		if (left)
			rotation += ROT_SPEED;
		else if (right)
			rotation -= ROT_SPEED;
		
		int floorX = (int) Math.floor(x);
		int floorY = (int) Math.floor(y);
		
		int floorXPrev = (int) Math.floor(xPrevious);
		int floorYPrev = (int) Math.floor(yPrevious);
		
		if (game.getMap()[floorX][floorYPrev] == 1)
			x = xPrevious;
		if (game.getMap()[floorXPrev][floorY] == 1)
			y = yPrevious;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = false;
	}

}
