package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

import cs4230.pedestrian.math.Statistics;

public class Exit extends Cell{

	private static final int maxMult = 100;
	public Exit(int x, int y) {
		super(x, y, maxMult);
		// TODO Auto-generated constructor stub
	}
	
	public void update() {
		//remove occupant, handle statistics method and set null
		if(this.isOccupied) {
			// TODO make grid store reference to timer 
			// so that can tell it to delete pedestrian from list
			//this.grid.removePedestrian(this.occupant);
			this.setVoid();
		}
		super.update();
	}
	
	public void draw(Graphics gfx) {
		Color col = new Color(255 , 0 , 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
	}

}
