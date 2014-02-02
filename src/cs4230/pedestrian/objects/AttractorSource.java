package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

public class AttractorSource extends Particle {
	private static double[][] field = {{0,0,0}, {0,0,0}, {0,0,0}};
	
	public AttractorSource(int x, int y, double multiplier) {
		super(field);
		setPosition(x, y);
	}

	@Override
	public void requestMove() {
		// Do Nothing since AttractorSources don't move
	}

	@Override
	public void draw(Graphics gfx) {
		gfx.setColor(Color.YELLOW);
		gfx.fillOval(x * TILE_PX + 3, y * TILE_PX + 3, TILE_PX - 6, TILE_PX - 6);
	}
}
