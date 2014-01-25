import java.util.PriorityQueue;


public class Cell {
	protected double mult,dynamic;
	protected int x,y;
	protected PriorityQueue<Pedestrian> requestedMove;
	
	
	public Cell(int x, int y, double mult) {
		requestedMove = new PriorityQueue<Pedestrian>();
		this.x = x;
		this.y = y;
		this.mult = mult;
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
	 * Handles pedestrian collision and updates the dynamic field values
	 */
	public void update() {
		this.handleCollisions();
		
		
	}
	
	public void handleCollisions() {
		if(!requestedMove.isEmpty()) {
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}
}
