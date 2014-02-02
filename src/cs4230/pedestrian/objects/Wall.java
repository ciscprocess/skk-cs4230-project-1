package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

import cs4230.pedestrian.math.Statistics;

public class Wall extends Cell {
	public Wall(int x, int y) {
		super(x, y, 0);
	}
	
	@Override
	public void draw(Graphics gfx) {
		int red = (int)(255*Statistics.sigmoid(getMultiplier()));
		Color col = new Color(red, 0, 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
	}
	
	@Override
	/**
	 * sets the cell to occupied
	 * We can never have a Wall be occupied.
	 */
	public void setOccupied(Particle occupant) {
		super.setOccupied(occupant);
		System.out.println("Warning: Setting a Wall as occupied.");
	}
	
	@Override
	public void incrementField() {
		dynamic = 0;
	}
	
	@Override
	public void update() {
		super.update();
		this.mult = 0;
		this.dynamic = 0;
	}

}
