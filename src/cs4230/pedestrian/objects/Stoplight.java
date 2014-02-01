package cs4230.pedestrian.objects;
public class Stoplight extends Cell {
	
	private int lightIncrement, count, maxCounts;
	private double onMult;
	
	public Stoplight(int x, int y, double mult, int lightIncrement, int duration) {
		
		super(x,y,mult);
		
		this.lightIncrement = lightIncrement;
		count = 0;
		this.staticMult = 0;
		onMult = mult;
		this.maxCounts = duration+lightIncrement;
	}
		
	@Override
	public void update() {
		
		count++;
		if(count>maxCounts) {
			count = 0;
			staticMult = 0;
		}
		else if(count>lightIncrement) {
			staticMult = onMult;
		}
		
		super.update();
	}
	
}
