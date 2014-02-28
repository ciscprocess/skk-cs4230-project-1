package cs4230.pedestrian.objects;

import java.awt.Graphics;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.math.LinCogRandom;

/**
 * Particle models anything that moves or needs to be on a separate graphics layer in the map.
 * It models anything on top of the base map.
 * @author Nathan
 */
public abstract class Particle {
	public int x,y; // Probably should be private
	protected int move_Increment, moveCount;
	protected double[][] moveField;
	protected static LinCogRandom random = new LinCogRandom();;
	protected static Grid grid;
	
	public double priority;
	
	public static final int TILE_PX = DisplayPanel.TILE_PX;
	
	/**
	 * Constructs a Particle with a defined movement probability field. 
	 * @param moveField - the movement probability field
	 */
	protected Particle(double[][] moveField) {
		moveCount = 0;
		this.moveField = moveField;
		x = -1;
		y = -1;
	}
	
	public static void setGrid(Grid newGrid) {
		grid = newGrid;
	}
	
	/**
	 * Not used. Deprecated. Used to be used to generate a probabilty mask which favors a
	 * direction to a specific point. Scrapped once we developed the reverse BFS floor plan.
	 * @param x
	 * @param y
	 * @param mult
	 * @return
	 */
	public double[][] generateMoveMask(int x, int y, double mult) {
		double centerDist = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
		double[][] mg = new double[3][3];
		for (int i = 0; i < mg.length; i++) {
			for (int j = 0; j < mg[0].length; j++) {
				int newX = this.x + (i - 1);
				int newY = this.y + (j - 1);
				double dist = Math.sqrt(Math.pow(x - newX, 2) + Math.pow(y - newY, 2)) - centerDist;
				if (mult < 0) {
					dist = 1 / dist;
				}
				mg[i][j] = (10 / Math.pow(0.1 * dist + 1, 23));
			}
		}
		return mg;
	}
	
	/**
	 * Generates a Uniform movement probability field. A 3x3 matrix of form (1/9)*I
	 * @return the movement probability matrix
	 */
	public static double[][] generateUniform() {
		double[][] mg = new double[3][3];
		for (int i = 0; i < mg.length; i++) {
			for (int j = 0; j < mg[0].length; j++) {
				mg[i][j] = 1.0 / 9;
			}
		}
		
		return mg;
	}
	
	/**
	 * Set position, primarily for door assignment and
	 * Used by cell at end of each update
	 */
	public void setPosition(int x, int y) {
		
		// increments field of cell previously occupied by pedestrian
		Cell temp = grid.getCell(this.x, this.y);
		if(temp!=null) {
			temp.incrementField();
			//set old position to empty
			temp.setVoid();
		}		
		this.x = x;
		this.y = y;
		temp = grid.getCell(x, y);
		temp.setOccupied(this);
	}
	
	public abstract void requestMove();
	public abstract void draw(Graphics gfx);

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
