package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

import cs4230.pedestrian.math.Statistics;

public class AttractorSource extends Particle {
	private static double[][] field = {{0,0,0}, {0,0,0}, {0,0,0}};
	
	private double multiplier;
	
	public AttractorSource(int x, int y, double multiplier) {
		super(field);
		setPosition(x, y);
		this.multiplier = multiplier;
	}

	@Override
	public void requestMove() {
		// Do Nothing since AttractorSources don't move
	}

	@Override
	public void draw(Graphics gfx) {
		double t = Statistics.sigmoid(multiplier * 20);
		int r = (int)Math.round(180);
		int g = (int)Math.round(180 * (1 - t));
		int b = (int)Math.round(180 * t);
		gfx.setColor(new Color(r, g, b));
		gfx.fillOval(x * TILE_PX + 3, y * TILE_PX + 3, TILE_PX - 6, TILE_PX - 6);
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
}
