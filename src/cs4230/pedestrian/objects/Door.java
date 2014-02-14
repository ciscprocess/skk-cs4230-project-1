package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

import cs4230.pedestrian.math.Statistics;

/**
 * An instance of a Door. Pedestrians are created on adjacent squares
 * @author Nathan
 */
public class Door extends Particle {
	private static double[][] field = {{0,0,0}, {0,0,0}, {0,0,0}};
	
	// The exit direction
	// TODO: This is not implemented currently. So implement it.
	private int exitDir = -1;
	public Door(int x, int y, int exitDirection) {
		super(field);
		this.setPosition(x, y);
		this.setExitDir(exitDirection);
	}

	@Override
	public void requestMove() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Graphics gfx) {
		int green = (int)(255*Statistics.sigmoid(0.2));
		Color col = new Color(122, green, 30);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX + 6, y * TILE_PX + 6, TILE_PX - 12, TILE_PX - 12);

	}

	public int getExitDir() {
		return exitDir;
	}

	public void setExitDir(int exitDir) {
		this.exitDir = exitDir;
	}

}
