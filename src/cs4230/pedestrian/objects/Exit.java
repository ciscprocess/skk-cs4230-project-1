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
		// TODO implement the removal of occupying pedestrian
		// TODO determine pedestrian exit interval and how to accommodate
		super.update();
	}
	
	public void draw(Graphics gfx) {
		Color col = new Color(255 , 0 , 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
	}

}
