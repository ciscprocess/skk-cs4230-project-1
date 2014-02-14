package cs4230.pedestrian.objects;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

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
	
	public static final int WIDTH = 450;
	public static final int HEIGHT = 94;
	
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
			
			//data structures for creating static field around exits
			PriorityQueue<Cell> currentSet = new PriorityQueue<Cell>();
			HashSet<Cell> toExplore = new HashSet<Cell>();
			
			
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
							newGrid.cells[x][y] = new Stoplight(x, y, 0.5, 400, 300);
							toExplore.add(newGrid.cells[x][y]);
						} else if (name.contains("wall")) {
							newGrid.cells[x][y] = new Wall(x, y);
						} else if (name.contains("open")) {
							//set minimum mult initially so that creating static field works
							newGrid.cells[x][y] = new Cell(x, y, 0);
							toExplore.add(newGrid.cells[x][y]);
						} else if (name.contains("road")){
							newGrid.cells[x][y] = new Road(x, y);
						} else if (name.contains("exit")){
							newGrid.cells[x][y] = new Exit(x, y);
							currentSet.add(newGrid.cells[x][y]);
						} else if (name.contains("crosswalk")) {
							newGrid.cells[x][y] = new Crosswalk(x, y, 0.1);
						}
						
						if (name.contains("-d")) {
							newGrid.doors.add(new Point(x, y));
						}
					}
					x++;
				}
				y++;
			}
			
			//initialize grid with actual weights to guide particles to exit
			createStaticField(newGrid, currentSet, toExplore);
			
		} catch (IOException e) {
			System.out.println("Failed to load map-defining Excel file.");
			e.printStackTrace();
		}
		
		return newGrid;
	}

	/**
	 * Uses exit arrangement to create a static weight on the field
	 * such that pedestrians are drawn to exits
	 * 
	 * Note: all open slots must be part of a continuous set from the exits
	 * (no islands or else will not converge)
	 * 
	 * @param grid The Grid to initialize
	 * @param currentSet The current set of Cells to begin from (Exits)	
	 * @param toExplore The cells that are valid neighbors to set weights to
	 */
	public static void createStaticField(Grid grid, PriorityQueue<Cell> currentSet, HashSet<Cell> toExplore) {
		while(!toExplore.isEmpty() && !currentSet.isEmpty()) {
			Cell current = currentSet.remove();
			//set neighbor weights and add to the current exploring set
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					Cell temp = grid.getCell(current.x + i, current.y + j);
					if(temp!=null && toExplore.remove(temp)) {
						//decrement cell by appropriate amount if cell is diagonal
						if(Math.abs(i)==Math.abs(j))
							temp.setMult(current.mult-4.5);
						else
							temp.setMult(current.mult-3);
						currentSet.add(temp);
					}
				}
			}
		}
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
