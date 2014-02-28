package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A non-traversable Cell
 * @author Nathan
 */
public class Road extends Cell {

	public Road(int x, int y) {
		super(x, y, 0);
		
		this.isOccupiable = false;
	}
	
	@Override
	public void update() {
		//road shouldn't require any additional computation time
	}
	
	/**
	 * Draws a darker-colored cell for visual verification
	 */
	@Override
	public void draw(Graphics gfx) {
		int color = 175;
		Color col = new Color(color, color, color);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
	}

}
