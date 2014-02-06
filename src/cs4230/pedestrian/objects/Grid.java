package cs4230.pedestrian.objects;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cs4230.pedestrian.math.LinCogRandom;

/**
 * This class contains all the cells in the Automata simulation. 
 * For now, it is an organizational placeholder.
 * @author Nathan
 */
public class Grid {
	
	public static final int WIDTH = 48;
	public static final int HEIGHT = 25;
	
	private Cell[][] cells;
	private ArrayList<Point> doors;
	private ArrayList<AttractorSource> pois = new ArrayList<AttractorSource>();
	
	public Grid() {
		doors = new ArrayList<Point>();
		cells = new Cell[WIDTH][HEIGHT];
	}
	
	public static Grid loadFromXLSX(String path) {
		Grid newGrid = new Grid();
		LinCogRandom rand = new LinCogRandom();
		
		try {
			FileInputStream file = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iter = sheet.iterator();
			int x = 0, y = 0;
			while (iter.hasNext() && y < HEIGHT) {
				x = 0;
				Row currentRow = iter.next();
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				
				while (colIter.hasNext() && x < WIDTH) {
					org.apache.poi.ss.usermodel.Cell xlCell = colIter.next();
					if (xlCell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING) {
						String name = xlCell.getStringCellValue();
						System.out.println("name: " + name);
						// TODO: Move these string constants to the Cell class, and expand them
						if (name.contains("stoplight")) {
							newGrid.cells[x][y] = new Stoplight(x, y, 0.5, 1, 300);
						} else if (name.contains("wall")) {
							newGrid.cells[x][y] = new Wall(x, y);
						} else if (name.contains("open")) {
							newGrid.cells[x][y] = new Cell(x, y, rand.nextDouble());
						} else if (name.contains("road")){
							newGrid.cells[x][y] = new Road(x, y);
						}
						
						if (name.contains("-d")) {
							newGrid.doors.add(new Point(x, y));
						}
					}
					x++;
				}
				y++;
			}
			
		} catch (IOException e) {
			System.out.println("Failed to load map-defining Excel file.");
			e.printStackTrace();
		}
		return newGrid;
	}
	
	public void update() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				cells[x][y].update();
			}
		}
	}
	
	public void setCell(int x, int y, Cell cell) {
		cells[x][y] = cell;
	}
	
	public Cell getCell(int x, int y) {
		if(x < 0 || y < 0 || x >= cells.length || y>=cells[1].length)
			return null;
		return cells[x][y];
	}
	
	public int getWidth() {
		return cells.length;
	}
	
	public int getHeight() {
		return cells[0].length;
	}
	
	public ArrayList<Point> getDoorLocations() {
		return doors;
	}
	
	public void addAttractorSource(AttractorSource add) {
		pois.add(add);
	}
	
	public ArrayList<AttractorSource> getAttractorSources() {
		return pois;
	}
	
}
