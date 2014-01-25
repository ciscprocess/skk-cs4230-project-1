public class Stoplight extends Cell {
	
	private int lightIncrement, count, maxCounts;
	private double currentMult;
	
	public Stoplight(int x, int y, double mult, int lightIncrement, int duration) {
		super(x,y,mult);
		this.lightIncrement = lightIncrement;
		count = 0;
		currentMult = 0;
		this.maxCounts = duration+lightIncrement;
	}
	
	public double getMultiplier() {
		return currentMult;
	}
	
	public void update() {
		count++;
		if(count>maxCounts) {
			count = 0;
			currentMult = 0;
		}
		else if(count>lightIncrement) {
			currentMult = mult;
		}
	}
	
}
