public class Stoplight extends Cell {
	
	private int lightIncrement, count, maxCounts;
	private double onMult;
	
	public Stoplight(int x, int y, double mult, int lightIncrement, int duration) {
		
		super(x,y,mult);
		
		this.lightIncrement = lightIncrement;
		count = 0;
		this.mult = 0;
		onMult = mult;
		this.maxCounts = duration+lightIncrement;
	}
		
	@Override
	public void update() {
		
		super.update();
		
		count++;
		if(count>maxCounts) {
			count = 0;
			mult = 0;
		}
		else if(count>lightIncrement) {
			mult = onMult;
		}
	}
	
}
