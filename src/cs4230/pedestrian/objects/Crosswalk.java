package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;


public class Crosswalk extends Cell {
	public Crosswalk(int x, int y, double mult) {
		super(x,y,mult);
	}
	
	public void draw(Graphics gfx) {
		
		Color col = new Color(0,0,0);
		gfx.setColor(col);
		//gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
		gfx.drawString("" + ((int)getMultiplier())%100, x * TILE_PX, y * TILE_PX);
		/*
		gfx.setColor(Color.BLACK);
		gfx.fillRect(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
		*/
		if(this.isOccupied) {
			this.occupant.draw(gfx);
		}
	}
}
