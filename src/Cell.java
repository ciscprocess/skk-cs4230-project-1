import java.util.PriorityQueue;


public class Cell {
	protected double mult;
	protected int x,y;
	protected PriorityQueue<Pedestrian> requestedMove;
	
	
	public Cell(int x, int y, double mult) {
		requestedMove = new PriorityQueue<Pedestrian>();
		this.x = x;
		this.y = y;
		this.mult = mult;
	}
	
	public double getMultiplier() {
		return mult;
	}
	
	public void enqueuePedestrian(Pedestrian ped) {
		requestedMove.add(ped);
	}
	
	public void handleCollisions() {
		if(!requestedMove.isEmpty()) {
			requestedMove.remove().setPosition(x, y);
			requestedMove.clear();
		}
	}
}
