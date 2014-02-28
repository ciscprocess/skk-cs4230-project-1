package cs4230.pedestrian.math;

/**
 * provides a variety of functions which operate on 2D arrays, mimicking the behavior
 * of matrices.
 * @author Nathan
 */
public class MatrixTools {
	
	/**
	 * Sum each element in the array/matrix
	 * @param a
	 * @return
	 */
	public static double sum(double[][] a) {
		double s = 0.0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				s += a[i][j];
			}
		}
		return s;
	}
	
	/**
	 * Similar to MATLAB zeros()
	 * @param width
	 * @param height
	 * @return
	 */
	public static double[][] generateZeros(int width, int height) {
		double[][] n = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				n[i][j] = 0;
			}
		}
		return n;
	}
	
	/**
	 * Elementwise multiplication of two matrices
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] multiplyInPlace(double[][] a, double[][] b) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] * b [i][j];
				}
			}
		return n;
	}
	
	/**
	 * Scalar-matrix multiplication
	 * @param a
	 * @param factor
	 * @return
	 */
	public static double[][] scale(double[][] a, double factor) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] * factor;
				}
			}
		return n;
	}
	
	/**
	 * Adds two matrices
	 * @param a
	 * @param b
	 * @return
	 */
	public static double[][] add(double[][] a, double[][] b) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] + b [i][j];
				}
			}
		return n;
	}
	
	/**
	 * Prints a matrix
	 * @param a
	 */
	public static void print(double[][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				System.out.print(a[i][j] + ", ");
			}
		}
		System.out.println();
	}
	
	/**
	 * Prints a vector/1D array
	 * @param a
	 */
	public static void print(double[] a) {
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i] + ", ");
		}
		System.out.println();
	}
	
}
