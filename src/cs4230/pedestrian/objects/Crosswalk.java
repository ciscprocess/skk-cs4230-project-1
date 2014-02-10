package cs4230.pedestrian.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Crosswalk extends Cell {
	public Crosswalk(int x, int y, double mult) {
		super(x,y,mult);
	}
	
	public void draw(Graphics gfx) {
		gfx.setColor(Color.BLACK);
		gfx.fillRect(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
		Graphics2D g2 = (Graphics2D)gfx;
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
		g2.drawLine(x * TILE_PX + 1, y * TILE_PX - 1, TILE_PX - 1, TILE_PX + 1);
	}
}
