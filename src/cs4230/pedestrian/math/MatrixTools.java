package cs4230.pedestrian.math;

public class MatrixTools {
	
	public static double sum(double[][] a) {
		double s = 0.0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				s += a[i][j];
			}
		}
		return s;
	}
	
	public static double[][] generateZeros(int width, int height) {
		double[][] n = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				n[i][j] = 0;
			}
		}
		return n;
	}
	
	public static double[][] multiplyInPlace(double[][] a, double[][] b) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] * b [i][j];
				}
			}
		return n;
	}
	
	public static double[][] scale(double[][] a, double factor) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] * factor;
				}
			}
		return n;
	}
	
	public static double[][] add(double[][] a, double[][] b) {
		double[][] n = new double[a.length][a[0].length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					n[i][j] = a[i][j] + b [i][j];
				}
			}
		return n;
	}
	
	public static void print(double[][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				System.out.print(a[i][j] + ", ");
			}
		}
		System.out.println();
	}
	
	public static void print(double[] a) {
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i] + ", ");
		}
		System.out.println();
	}
	
}
