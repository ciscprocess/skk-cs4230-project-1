package cs4230.pedestrian.objects;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Doors {
	private static ArrayList<Pedestrian> exited;
	private static PriorityQueue<Pedestrian> queuedPeople;
	int[] x,y;
	private static Grid grid;
	
	/**
	 * Creates a new door linked to central supply
	 * @param x = x positions of the door exit squares
	 * @param y = y positions of the door exit squares
	 */
	public Doors(int[] x, int[] y, Grid grid) {
		if(x.length!=y.length) {
			System.out.println("You need the same number of x and y values...");
			return;
		}
		this.x = x;
		this.y = y;
		Doors.grid = grid;
	}
	
	/**
	 * 
	 * @param queue the backing ArrayList of people inside the building this door is linked to
	 */
	public void setQueue(PriorityQueue<Pedestrian> queue) {
		queuedPeople = queue;
	}
	
	/**
	 * 
	 * @param exitedPeople ArrayList of people who have exited the building
	 */
	public void setExited(ArrayList<Pedestrian> exitedPeople) {
		exited = exitedPeople;
	}
	
	/**
	 * places a person at the exit square of this door if it is not occupied
	 */
	public void egress() {
		for(int i = 0; i < x.length; i++) {
			if(!grid.getCell(x[i], y[i]).isOccupied()) {
				Pedestrian temp = queuedPeople.remove();
				temp.setPosition(x[i], y[i]);
				exited.add(temp);
			}
		}
	}
	
	
}
