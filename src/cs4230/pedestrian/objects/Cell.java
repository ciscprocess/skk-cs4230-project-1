package cs4230.pedestrian.objects;
import java.util.PriorityQueue;

import cs4230.pedestrian.math.LinCogRandom;


public class Cell {
	protected double mult;
	protected int dynamic;
	protected int x,y;
	protected PriorityQueue<Pedestrian> requestedMove;
	protected static LinCogRandom random;
	protected static Grid grid;
	
	//diffusion constant for dynamic field 
	protected final double alpha = .5;
	//decay constant for dynamic field
	protected final double delta = .5;
	
	
	public static void setGrid(Grid newGrid) {
		grid = newGrid;
	}
	
	public Cell(int x, int y, double mult) {
		requestedMove = new PriorityQueue<Pedestrian>();
		this.x = x;
		this.y = y;
		this.mult = mult;
		if(random==null)
			random = new LinCogRandom();
	}
	
	
	/**
	 * Computes the 
	 * @return the overall multiplier of probability
	 */
	public double getMultiplier() {
		return (mult+dynamic);
	}
	
	public void enqueuePedestrian(Pedestrian ped) {
		requestedMove.add(ped);
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
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}
}
