package cs4230.pedestrian.objects;
import java.awt.Color;
import java.awt.Graphics;
import java.util.PriorityQueue;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.math.LinCogRandom;

/**
 * Cell is the core of the simulation. It functions as one of the discrete locations
 * where Pedestrians or other Particles can be found, and it contains desirability
 * information about itself.
 * 
 * @author Paul
 */
public class Cell implements Comparable<Cell> {
	protected double mult;
	protected int dynamic;
	protected int x,y;
	protected PriorityQueue<Pedestrian> requestedMove;
	protected static LinCogRandom random;
	protected static Grid grid;
	protected boolean isOccupied;
	protected Particle occupant;
	protected boolean isOccupiable;

	public static final int TILE_PX = DisplayPanel.TILE_PX;
	
	//diffusion constant for dynamic field 
	protected final double alpha = .999;
	//decay constant for dynamic field
	protected final double delta = 0.01;
	//sensitivity constant for dynamic field 
	protected final double Kd = 0;
	//sensitivity constant for static field
	protected final double Ks = 1;
	
	
	public static void setGrid(Grid newGrid) {
		grid = newGrid;
	}
	
	/**
	 * Constructor for cell. Takes in its location in the Grid and a desirability value.
	 * The desirability value influences how likely Pedestrians are to move to its location.
	 * @param x - the X (horizontal) coordinate of the Cell's location in the grid
	 * @param y - the Y (vertical) coordinate of the Cell's location in the grid
	 * @param mult - the cell's desirability value
	 */
	public Cell(int x, int y, double mult) {
		requestedMove = new PriorityQueue<Pedestrian>();
		this.x = x;
		this.y = y;
		this.mult = mult;
		isOccupied = false;
		isOccupiable = true;
		
		if (random == null) {
			random = new LinCogRandom();
		}
	}
	
	/**
	 * The default draw method for a cell in the simulation. Can be overridden by children
	 * of Cell for a more graphically descriptive representation. It takes in a AWT Graphics
	 * object as its drawing surface, and it redraws itself every time this function is called.
	 * @param gfx - the graphics surface to draw to
	 */
	public void draw(Graphics gfx) {
		Color col = new Color(0,0,0);
		gfx.setColor(col);
		gfx.drawString("" + ((int)getMultiplier()) % 100, x * TILE_PX, y * TILE_PX);
	}
	
	/**
	 * Computes the relative weight of this cell based on desirability value.
	 * @return the overall multiplier of probability
	 */
	public double getMultiplier() {
		return (isOccupiable) ? (Ks*mult - ((isOccupied) ? 1:0) - requestedMove.size()):Integer.MIN_VALUE;
	}
	
	/**
	 * Sets the raw desirability value. This is opposed to getMultiplier() which returns
	 * a weight based on this raw desirability.
	 * @param mult
	 */
	public void setMult(double mult) {
		this.mult = mult;
	}
	
	/**
	 * Notify the Cell that a(nother) pedestrian wishes to move here.
	 * At it's turn, the pedestrian with the highest speed gets to move here.
	 * @param ped - the pedestrian to enqueue
	 */
	public void enqueuePedestrian(Pedestrian ped) {
		// do not accept move requests if I am already occupied
		if (isOccupied) {
			return;
		}
		
		requestedMove.add(ped);
	}
	
	/**
	 * indicates a pedestrian is currently occupying this spot
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	
	/**
	 * sets the cell to occupied
	 */
	public void setOccupied(Particle occupant) {
		isOccupied = true;
		this.occupant = occupant;
	}
	
	/**
	 * Sets the cell to unoccupied
	 */
	public void setVoid() {
		isOccupied = false;
		this.occupant = null;
	}
	
	/**
	 * getter for the field "occupant"
	 * @return the occupant
	 */
	public Particle getOccupant() {
		return occupant;
	}
	
	/**
	 * Increments dynamic field from pedestrian movement or dispersion
	 */
	public void incrementField() {
		dynamic++;
	}
	
	/**
	 * Handles pedestrian collision and updates the dynamic field values
	 */
	public void update() {
		// NOTE TO TA:
		// This code was scrapped since we eliminated the use of dynamic field values,
		// as they were a major performance bottleneck and they were found to actually decrease
		// the quality of the simulation during the face validation phase
		
		/*
		int newDynamic = 0;
		for(int i = 0; i < dynamic; i++) {
			//decay
			if(random.nextDouble() > delta) {
				newDynamic++;
			}
			//disperse
			if(random.nextDouble() > alpha) {
				int moveCell = random.nextInt(9);
				int tempX,tempY;
				//check all possible neighbors if the cell chosen is non-existant
				//not uniformly random if all neighbors don't exist...
				for(int j = 0; j < 9; j ++) {
					moveCell = (moveCell+1)%9;
					tempX = x + moveCell/3 -1;
					tempY = y + moveCell%3 -1;
					if (grid == null) {
						System.out.println("We have a problem.");
					}
					Cell temp = grid.getCell(tempX, tempY);
					if(temp!=null) {
						temp.incrementField();
						break;
					}
				}
			}
		}
		dynamic = newDynamic;
		*/
		
		this.handleCollisions();
		
	}
	
	
	/**
	 * Move pedestrian of highest priority onto this cell
	 */
	public void handleCollisions() {
		if(!requestedMove.isEmpty()) {
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}

	@Override
	public int compareTo(Cell o) {
		return (int)Math.signum(o.mult-this.mult);
	}
}
