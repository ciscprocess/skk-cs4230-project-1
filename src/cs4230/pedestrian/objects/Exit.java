package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;


public class Exit extends Cell{

	private static final int maxMult = 100;
	
	public Exit(int x, int y) {
		super(x, y, maxMult);
		// TODO Auto-generated constructor stub
	}
	
	public void update() {
		//remove occupant, handle statistics method and set null
		if(this.isOccupied) {
			grid.getExited().remove(this.occupant);
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
