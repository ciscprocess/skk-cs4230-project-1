package cs4230.pedestrian.math;

public class Statistics {
	private LinCogRandom random;
	private static int spare;
	private static boolean isSpareReady = false;
	
	public Statistics(LinCogRandom random){
		this.random = random;
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
	
}
