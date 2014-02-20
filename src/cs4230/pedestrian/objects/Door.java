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
	
<<<<<<< HEAD
<<<<<<< HEAD
	// The exit direction
	public Door(int x, int y) {
		super(field);
=======
	public Door(int x, int y) {
		super(field);
		//this.setPosition(x, y);
>>>>>>> 0d0f64452c046afb82cb78fa4970fd51c8df2d8d
=======
	public Door(int x, int y) {
		super(field);
		//this.setPosition(x, y);
>>>>>>> 0d0f64452c046afb82cb78fa4970fd51c8df2d8d
		this.x = x;
		this.y = y;
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
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 0d0f64452c046afb82cb78fa4970fd51c8df2d8d
=======
>>>>>>> 0d0f64452c046afb82cb78fa4970fd51c8df2d8d
}
