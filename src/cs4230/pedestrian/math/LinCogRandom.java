package cs4230.pedestrian.math;

/**
 * Linear Congruential Random Number Generator
 * @author Nathan
 */
public class LinCogRandom {
	
	private long m;
	private long a;
	private long c;
	private long x;
	
	/**
	 * create a new instance of the RNG with a custom seed.
	 * @param seed - the starting value for the RNG
	 */
	public LinCogRandom(long seed) {
		x = seed;
		a = 1103515245L;
		m = 1L << 32L;
		c = 12345;
	}
	
	/**
	 * create a new instance of the RNG with the system time as the seed.
	 */
	public LinCogRandom() {
		this(System.currentTimeMillis());
	}
	
	/**
	 * Generates a new positive random integer with an exclusive upper bound
	 * @param upper the upper bound
	 * @return the random integer
	 */
	public int nextInt(int upper) {
		x = (a*x + c) & (0xFFFFFFFFL);
		return (int)((x >> 16) % upper);
	}
	
	/**
	 * Generates a new positive random real number on [0, 1)
	 * @return the random number
	 */
	public double nextDouble() {
		x = (a*x + c) & (0xFFFFFFFFL);
		return (double)((x >> 16)) / (m >> 16);
	}

}
