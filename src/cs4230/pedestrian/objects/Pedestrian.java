package cs4230.pedestrian.objects;

import java.awt.Color;
import java.awt.Graphics;

import cs4230.pedestrian.math.*;

public class Pedestrian extends Particle implements Comparable<Pedestrian> {
	
	private static Statistics stats;

	public Pedestrian(double[][] moveField) {
		super(moveField);
		stats = new Statistics(random);
		moveIncrement = stats.normalInt(10, 1);
	}
		
	/**
	 * Place move request on the cell
	 */
	@Override
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
			}
		}
		
		/*
		ArrayList<AttractorSource> sources = grid.getAttractorSources();
		for (AttractorSource source : sources) {
			double[][] mask = this.generateMoveMask(source.x, source.y, source.getMultiplier());
			tempMove = MatrixTools.multiplyInPlace(mask, tempMove);
		}
		*/
		
		sum = MatrixTools.sum(tempMove);
		
		// check for move possibility
		if(sum <= 0) {
			// if no probabilities of moving, don't request to move but reset chance to move next time
			// mitigates propagation delay in crowds
			moveCount = moveIncrement - 1;
			return;
		}
		
		
		// 10 because of edge conditions
		double[] chances = new double[9];
		Cell[] targets = new Cell[9];
		
		// normalize move probabilities 
		double current = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tempMove[i][j] /= sum;
				targets[3 * i + j] = grid.getCell(x + i - 1, y + j - 1);
				current += tempMove[i][j];
				chances[3 * i + j] = current;
			}
		}
		
		double move = random.nextDouble() + Double.MIN_VALUE;
		int count = 0;
		while(move > chances[count] && (count < chances.length)) {
			count++;
		}
		
		/*
		System.out.println();
		System.out.print("Chances: ");
		MatrixTools.print(chances);
		
		System.out.println("Move: " + move);
		System.out.println("Count: " + count);
		*/
		
		//sanity check
		int tempX = x + (count / 3 - 1);
		int tempY = y + (count % 3 - 1);

		
		if (tempX >= Grid.WIDTH || tempY >= Grid.HEIGHT || tempX < 0 || tempY < 0) {
			return;
		}
		
		//set priority and request move from cell
		priority = tempMove[count / 3][count % 3];
		
		grid.getCell(tempX, tempY).enqueuePedestrian(this);
	}

	@Override
	public int compareTo(Pedestrian o) {
		return (int)Math.signum(o.priority - this.priority);
	}

	@Override
	public void draw(Graphics gfx) {
		gfx.setColor(Color.RED);
		gfx.fillOval(x * TILE_PX + 3, y * TILE_PX + 3, TILE_PX - 6, TILE_PX - 6);
	}	
}
