package cs4230.pedestrian.objects;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Doors {
	private static ArrayList<Pedestrian> exited;
	private static PriorityQueue<Pedestrian> queuedPeople;
	private static ArrayList<Door> objects = new ArrayList<Door>();
	private int[] x,y;
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
		
		for (int i = 0; i < x.length; i++) {
			objects.add(new Door(x[i], y[i], 1));
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
		if (queuedPeople.size() == 0) {
			return;
		}
		for(int i = 0; i < x.length; i++) {
			if(!grid.getCell(x[i] + 1, y[i]).isOccupied()) {
				Pedestrian temp = queuedPeople.remove();
				temp.setPosition(x[i] + 1, y[i]);
				exited.add(temp);
			}
		}
	}
	
	
}
