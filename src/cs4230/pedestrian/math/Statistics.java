package cs4230.pedestrian.math;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
			//when last pedestrian leaves, write out statistics
			if(leftArea==leaveAreaTimes.length) {
				writeDataOut();
				System.out.println("Done!");
			}
		}
		else 
			System.out.println("Too many pedestrians leaving area somehow");
	}
	
	public static void writeDataOut() {
		int i = 0;
		File path;
		do {
			path = new File("output" + i + ".xlsx");
			i++;
		} while (path.exists());
		
		try {
			FileOutputStream file = new FileOutputStream(path);
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("dataSheet");
			
			//write column headers
			Row row = sheet.createRow((short)0);
			row.createCell(0).setCellValue("Left Door Time (not same Pedestrian as rest of stats)");
			//note the leave door time does not correspond to the same pedestrian as the rest of the statistics
			row.createCell(1).setCellValue("Left Area Time");
			row.createCell(2).setCellValue("Time Steps While Outside Building");
			row.createCell(3).setCellValue("Times Placed on New Cell");
			row.createCell(4).setCellValue("Distance walked from Door to Exit");
			
			for(i = 0; i < leaveDoorTimes.length; i++) {
				row = sheet.createRow((short)(i+1));
				//write various outputs in each row
				row.createCell(0).setCellValue(leaveDoorTimes[i]);
				//note the leave door time does not correspond to the same pedestrian as the rest of the statistics
				row.createCell(1).setCellValue(leaveAreaTimes[i]);
				row.createCell(2).setCellValue(totalSteps[i]);
				row.createCell(3).setCellValue(walkingSteps[i]);
				row.createCell(4).setCellValue(distance[i]);
			}
			
			//write out data and close file
			workbook.write(file);
			file.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Failure writing data out");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

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
