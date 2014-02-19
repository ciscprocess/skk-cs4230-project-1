package cs4230.pedestrian.objects;
import java.awt.Color;
import java.awt.Graphics;
import java.util.PriorityQueue;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.math.LinCogRandom;
import cs4230.pedestrian.math.Statistics;


public class Cell implements Comparable<Cell>{
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
	
	public Cell(int x, int y, double mult) {
		requestedMove = new PriorityQueue<Pedestrian>();
		this.x = x;
		this.y = y;
		this.mult = mult;
		isOccupied = false;
		isOccupiable = true;
		if(random==null)
			random = new LinCogRandom();
	}
	
	public void draw(Graphics gfx) {
		int green = (int)(255*Statistics.sigmoid(getMultiplier()));
		Color col = new Color(0, green, 0);
		gfx.setColor(col);
		gfx.fillRect(x * TILE_PX, y * TILE_PX, TILE_PX, TILE_PX);
		if(this.isOccupied) {
			this.occupant.draw(gfx);
		}
	}
	
	/**
	 * Computes the relative weight of this cell based on attractiveness
	 * @return the overall multiplier of probability
	 */
	public double getMultiplier() {
		//return (isOccupiable) ? Math.exp(Kd*dynamic)*Math.exp(Ks*mult - ((isOccupied) ? 3:0) - 3*requestedMove.size()):0;
		return (isOccupiable) ? Math.exp(Ks*mult - ((isOccupied) ? 5:0) - 3*requestedMove.size()):0;
	}
	
	public void setMult(double mult) {
		this.mult = mult;
	}
	
	public void enqueuePedestrian(Pedestrian ped) {
		//do not accept move requests if I am already occupied
		if(isOccupied)
			return;
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
		
		
		this.handleCollisions();
		
	}
	
	
	/**
	 * Move pedestrian of highest priority onto this cell
	 */
	public void handleCollisions() {
		if(!requestedMove.isEmpty()) {
			//System.out.println("!");
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}

	@Override
	public int compareTo(Cell o) {
		return (int)Math.signum(o.mult-this.mult);
	}
}
