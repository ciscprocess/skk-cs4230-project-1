package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;


/**
 * A wall where pedestrians can never be.
 * TODO: Should it be merged with Road?
 * @author Nathan
 */
public class Wall extends Cell {
	public Wall(int x, int y) {
		super(x, y, 0);
		this.isOccupiable = false;
	}
	
	@Override
	public void draw(Graphics gfx) {
		int red = 190;
		Color col = new Color(red, 0, 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
	}
	
	@Override
	/**
	 * sets the cell to occupied
	 * We can never have a Wall be occupied.
	 */
	public void setOccupied(Particle occupant) {
		super.setOccupied(occupant);
		//System.out.println("Warning: Setting a Wall as occupied.");
	}
	
	@Override
	public void incrementField() {
		dynamic = 0;
	}
	
	@Override
	public void update() {
		//Wall shouldn't take any additional computing resources
		
		//super.update();
		//this.mult = 0;
		//this.dynamic = 0;
	}

}
