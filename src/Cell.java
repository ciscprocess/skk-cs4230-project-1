import java.util.PriorityQueue;


public class Cell {
	private double mult;
	private int x,y;
	private PriorityQueue<Pedestrian> requestedMove;
	
	
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
