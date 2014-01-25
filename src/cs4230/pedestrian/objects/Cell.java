package cs4230.pedestrian.objects;
import java.util.PriorityQueue;

import cs4230.pedestrian.math.LinCogRandom;


public class Cell {
	protected double mult;
	protected int dynamic;
	protected int x,y;
	protected PriorityQueue<Pedestrian> requestedMove;
	protected static LinCogRandom random;
	
	//diffusion constant for dynamic field 
	protected final double alpha = .5;
	//decay constant for dynamic field
	protected final double delta = .5;
	
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
				for(int j = 0; j < 9; j ++) {
					moveCell = (moveCell+1)%9;
					tempX = x + moveCell/3 -1;
					tempY = y + moveCell%3 -1;
					//TODO fix with cell grid wrapper class
					if(tempX > -1 && tempX < cellGrid.length && tempY > -1 && tempY < cellGrid[0].length && cellGrid[tempX][tempY]!=null) {
						//TODO get cell and incrementField();
					}
				}
			}
		}
		dynamic = newDynamic;
		
		this.handleCollisions();
		
	}
	
	public void handleCollisions() {
		if(!requestedMove.isEmpty()) {
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}
}
