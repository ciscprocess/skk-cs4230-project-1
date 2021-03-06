package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Stoplight is a Cell that toggles its traversability 
 * @author Nathan
 */
public class Stoplight extends Cell {
	
	private int lightIncrement, count, maxCounts;
	
	/**
	 * Constructs a stoplight with custom parameters to define its behavior
	 * @param x - the x location on the map
	 * @param y - the y location on the map
	 * @param mult - the base desirability of this cell
	 * @param duration - the time spent in an off and on cycle
	 * @param lightIncrement - the time that the light switches from off to on
	 */
	public Stoplight(int x, int y, double mult, int duration, int lightIncrement) {
		
		super(x,y,mult);
		
		this.lightIncrement = lightIncrement;
		count = 0;
		this.mult = mult;
		this.maxCounts = duration+lightIncrement;
	}
	
	/**
	 * Same as the other constructor, but can start with an offset
	 */
	public Stoplight(int x, int y, double mult, int duration, int lightIncrement, int startCount) {
		
		this(x,y,mult,lightIncrement,duration);
		
		this.count = startCount;
	}
	
	@Override
	public void draw(Graphics gfx) {
		int color = (this.isOccupiable) ? 255:100;
		Color col = new Color(color, color, 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
		gfx.setColor(Color.black);
		gfx.drawString("SP", x * TILE_PX + 5, y * TILE_PX + 15);
		if(this.isOccupied) {
			this.occupant.draw(gfx);
		}
	}
		
	@Override
	public void update() {
		count++;
		if(count>maxCounts) {
			count = 0;
			this.isOccupiable = false;
		}
		else if(count>lightIncrement) {
			this.isOccupiable = true;
		}
		else {
			this.isOccupiable = false;
		}
		
		super.update();
	}
	
}
