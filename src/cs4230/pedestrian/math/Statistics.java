package cs4230.pedestrian.math;

public class Statistics {
	private LinCogRandom random;
	private static int spare;
	private static boolean isSpareReady = false;
	
	private static int[] leaveDoorTimes;
	private static int leftDoor;
	private static int[] leaveAreaTimes;
	private static int leftArea;
	private static int[] totalSteps;
	private static int[] walkingSteps;
	private static double[] distance;
	
	
	
	public Statistics(LinCogRandom random){
		this.random = random;
	}
	
	
	public static void setPedestrianNumber(int pedestrians) {
		leaveDoorTimes = new int[pedestrians];
		leaveAreaTimes = new int[pedestrians];
		distance = new double[pedestrians];
		totalSteps = new int[pedestrians];
		walkingSteps = new int[pedestrians];
		
		leftArea = 0;
		leftDoor = 0;
	}
	
	public static void oneLeftArea(int time, int steps, int walked, double distance) {
		if(leftArea<leaveAreaTimes.length) {
			leaveAreaTimes[leftArea] = time;
			Statistics.distance[leftArea] = distance;
			Statistics.totalSteps[leftArea] = steps;
			Statistics.walkingSteps[leftArea] = walked;
			leftArea++;
		}
		else 
			System.out.println("Too many pedestrians leaving area somehow");
	}
	
	public static void oneLeftDoor(int time) {
		if(leftDoor<leaveDoorTimes.length) {
			leaveDoorTimes[leftDoor] = time;
			leftDoor++;
		}
		else
			System.out.println("Too many pedestrians leaving door somehow");
	}
	
	/**
	 * Uses the polar method
	 * @param mean and stdDev of the normal distribution
	 * @return a random int from a normal distribution
	 */
	public int normalInt(int mean, double stdDev){
	    if (isSpareReady) {
	        isSpareReady = false;
	        return (int)(spare * stdDev) + mean;
	    } else {
	        double u, v, s;
	        do {
	            u = random.nextDouble() * 2 - 1;
	            v = random.nextDouble() * 2 - 1;
	            s = u * u + v * v;
	        } while (s >= 1 || s == 0);
	        double mul = Math.sqrt(-2.0 * Math.log(s) / s);
	        spare = (int)(v * mul);
	        isSpareReady = true;
	        return mean + (int)( stdDev * u * mul);
	    }
	}
	
	public static double sigmoid(double x) {
		return 1 / (1 + Math.pow(Math.E, -x));
	}
	
	/**
	 * Performs a Chi-Square test of randomness for the random number generator
	 */
	public static void main(String[] args) {
		LinCogRandom rand = new LinCogRandom();
		int n = 100000;
		int k = 1000;
		int[] bins = new int[k];
		int max = 1000;
		
		for(int i = 0; i < n; i++) {
			bins[(int)(rand.nextInt(max)/(double)max*k)]++;
		}
		
		double chi2 = 0;
		for (int i = 0; i < k; i++) {
			double number = Math.pow(bins[i]-(double)(n/k),2) / (n/k);
			System.out.println("Number: "  + number);
			chi2 += number;
		}
		
		System.out.println("Expected is: " + n/k);
		System.out.println("Chi-Square is: " + chi2);
		System.out.println("Critical value is: " + 1106);
	}
}
