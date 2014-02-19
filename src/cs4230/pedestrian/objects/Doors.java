package cs4230.pedestrian.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import cs4230.pedestrian.math.Statistics;

/**
 * Doors acts as a manager of the Door classes, since it creates them and fills them with pedestrians.
 * @author Nathan
 *
 */
public class Doors {
	private static ArrayList<Pedestrian> exited;
	private static PriorityQueue<Pedestrian> queuedPeople;
	private static ArrayList<Door> objects = new ArrayList<Door>();
	private static Grid grid;
	
	/**
	 * Creates a new door linked to central supply
	 * @param x = x positions of the door exit squares
	 * @param y = y positions of the door exit squares
	 */
	public Doors(ArrayList<Point> doors, Grid grid) {
		for (int i = 0; i < doors.size(); i++) {
			objects.add(new Door(doors.get(i).x, doors.get(i).y, Direction.RIGHT));
		}
		
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
		for(int i = 0; i < objects.size(); i++) {
			Door cd = objects.get(i);
			if(!grid.getCell(cd.x + 1, cd.y).isOccupied()) {
				if(queuedPeople.isEmpty())
					break;
				Pedestrian temp = queuedPeople.remove();
				temp.setPosition(cd.x + 1, cd.y);
				exited.add(temp);
				Statistics.oneLeftDoor(grid.time);
			}
		}
	}
	
	
}
