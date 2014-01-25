package cs4230.pedestrian.objects;

public class Pedestrian implements Comparable{
	
	private Cell[][] cellGrid;
	private int x,y,moveIncrement,moveCount;
	private double[][] moveField;
	public double priority;
	
	
	public Pedestrian(Cell[][] cellGrid, double[][] moveField) {
		this.cellGrid = cellGrid;
		// TODO randomly assign speed (inverse of moveCount) based on distribution
		moveIncrement = 10;
		moveCount = 0;
		this.moveField = moveField;
	}
	
	/**
	 * Set position, primarily for door assignment and
	 * Used by cell at end of each update
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Place move request on the cell
	 */
	public void requestMove() {
		moveCount++;
		
		//only update on moves when necessary based on speed
		if(moveIncrement<=moveCount) {
			moveCount=0;
		}
		else {
			return;
		}
		
		
		double[][] tempMove = new double[3][3];
		double sum = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				tempMove[i][j] = moveField[i][j];
				int tempX = x+i-1;
				int tempY = y+j-1;
				double mult = (tempX > -1 && tempX < cellGrid.length && tempY > -1 && tempY < cellGrid[0].length) ? cellGrid[tempX][tempY].getMultiplier():0; 
				tempMove[i][j] *= mult;
				sum+=tempMove[i][j];
			}
		}
		
		//check for move possibility
		if(sum <= 0) {
			//if no probabilities of moving, don't request to move but reset chance to move next time
			//mitigates propagation delay in crowds
			moveCount = moveIncrement-1;
			return;
		}
		
		//normalize move probabilities 
		double[] chances = new double[9];
		double current = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				tempMove[i][j] /= sum;
				chances[i*3+j] = current;
				current+=tempMove[i][j];
			}
		}
		
		// TODO implement random number generator (uniform)
		double move = Math.random();
		int count = 0;
		while(move < chances[count]) {
			count++;
		}
		
		//sanity check
		int tempX = x - count/3-1;
		int tempY = y - count%3-1;
		System.out.println("Sanity check pass: " + (tempX > -1 && tempX < cellGrid.length && tempY > -1 && tempY < cellGrid[0].length && cellGrid[tempX][tempY]!=null));
		
		//set priority and request move from cell
		priority = tempMove[count/3][count%3];
		cellGrid[tempX][tempY].enqueuePedestrian(this);
	}

	@Override
	public int compareTo(Object o) {
		return (int)Math.signum(this.priority - ((Pedestrian)o).priority);
	}	
}
