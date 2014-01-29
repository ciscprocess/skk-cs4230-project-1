package cs4230.pedestrian.objects;

public class Wall extends Cell {
	public Wall(int x, int y, double mult) {
		super(x, y, 0);
	}
	
	@Override
	/**
	 * sets the cell to occupied
	 * We can never have a Wall be occupied.
	 */
	public void setOccupied() {
		isOccupied = false;
	}

}
