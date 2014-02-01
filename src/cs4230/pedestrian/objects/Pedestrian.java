package cs4230.pedestrian.objects;

import cs4230.pedestrian.math.*;

public class Pedestrian implements Comparable{
	
	private int x,y,moveIncrement,moveCount;
	private double[][] moveField;
	public double priority;
	
	private static LinCogRandom random;
	private static Grid grid;
	private static Statistics stats;
	
	public static void setGrid(Grid newGrid) {
		grid = newGrid;
	}
	
	public static double[][] generateUniform() {
		double[][] mg = new double[3][3];
		for (int i = 0; i < mg.length; i++) {
			for (int j = 0; j < mg[0].length; j++) {
				mg[i][j] = 1.0 / 9;
			}
		}
		
		return mg;
	}
	
	public Pedestrian(double[][] moveField) {
		moveCount = 0;
		this.moveField = moveField;
		if(random==null)
			random = new LinCogRandom();
		stats = new Statistics(random);
		moveIncrement = stats.normalInt(10, 1);
		x = -1;
		y = -1;
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
		temp.setOccupied();
	}
	
	/**
	 * Place move request on the cell
	 */
	public void requestMove() {
		moveCount++;
		
		// only update on moves when necessary based on speed
		if(moveIncrement <= moveCount) {
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
				int tempX = x + i - 1;
				int tempY = y + j - 1;
				Cell temp = grid.getCell(tempX, tempY); 
				tempMove[i][j] *= (temp != null) ? temp.getMultiplier(): 0;
				sum += tempMove[i][j];
			}
		}
		
		// check for move possibility
		if(sum <= 0) {
			// if no probabilities of moving, don't request to move but reset chance to move next time
			// mitigates propagation delay in crowds
			moveCount = moveIncrement - 1;
			return;
		}
		
		// normalize move probabilities 
		double[] chances = new double[9];
		double current = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tempMove[i][j] /= sum;
				chances[3 * i + j] = current;
				current += tempMove[i][j];
			}
		}
		
		double move = random.nextDouble();
		int count = 0;
		while(move > chances[count] && (count < chances.length - 1)) {
			count++;
		}
		
		count -= 1;
		//sanity check
		int tempX = x - (count / 3 - 1);
		int tempY = y - (count % 3 - 1);
		System.out.println("Sanity check pass: " + (grid.getCell(tempX, tempY) != null));
		
		//set priority and request move from cell
		priority = tempMove[count / 3][count % 3];
		grid.getCell(tempX, tempY).enqueuePedestrian(this);
	}

	@Override
	public int compareTo(Object o) {
		return (int)Math.signum(this.priority - ((Pedestrian)o).priority);
	}	
}
