package cs4230.pedestrian.objects;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class contains all the cells in the Automata simulation. 
 * For now, it is an organizational placeholder.
 * @author Nathan
 */
public class Grid {
	
	private Cell[][] cells;
	private ArrayList<Point> doors;
	private ArrayList<AttractorSource> pois = new ArrayList<AttractorSource>();
	private ArrayList<Pedestrian> exited;
	public int time;
	
	public Grid(int width, int height) {
		doors = new ArrayList<Point>();
		cells = new Cell[width][height];
		time = 0;
	}
	
	/**
	 * Factory method that generates a new Grid instance from XLSX file.
	 * Each cell in the file represents a cell in the simulation, with text that defines
	 * what type of cell it will be. 
	 * @param path the path to the XLSX file.
	 * @return a new Grid instance
	 */
	public static Grid loadFromXLSX(String path) {
		Grid newGrid = null;
		
		try {
			// STEP ONE: find width and height of the Excel file
			
			// Get a handle to the xlsx file we wish to load
			FileInputStream file = new FileInputStream(new File(path));
			
			// Load the file into the Apache library
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			
			// Get the first sheet of the XLSX file, where the map is located
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			// Get the iterator for the rows
			Iterator<Row> iter = sheet.iterator();
			
			// Initializes current height and width estimates for the Grid
			int width = 0, height = 0;
			
			// Read all rows until an undefined one
			while (iter.hasNext()) {
				// Get the current row, and advance to the next
				Row currentRow = iter.next();
				
				// get the column iterator
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				
				// now that we know there is at least one row, increase our height estimate
				height++;
				
				
				// find the width of this row, and compare it to the width of the currently found
				// maximum. if it's greater, update the maximum, and thus the width estimate
				int twidth = 0;
				while (colIter.hasNext()) {
					twidth++;
					width = Math.max(width, twidth);
					colIter.next();
				}
			}
			
			
			// STEP TWO: Actually load the excel file
			newGrid = new Grid(width, height);
			
			int x = 0, y = 0;
			
			// Load in the file again, as above
			file = new FileInputStream(new File(path));
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			iter = sheet.iterator();
			
			// data structures for creating static field around exits
			PriorityQueue<Cell> currentSet = new PriorityQueue<Cell>();
			HashSet<Cell> toExplore = new HashSet<Cell>();
			
			
			Scanner scan;
			
			// Loop through all rows
			while (iter.hasNext() && y < height) {
				x = 0;
				// Get next row
				Row currentRow = iter.next();
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				
				// Loop through all columns
				while (colIter.hasNext() && x < width) {
					// Get next column
					org.apache.poi.ss.usermodel.Cell xlCell = colIter.next();
					
					// Ensure that this Excel Cell contains a string
					if (xlCell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING) {
						// Get Excel Cell contents
						String name = xlCell.getStringCellValue();
						
						// TODO: Move these string constants to the Cell class, and expand them
						// Check what type of Simulation Cell is described by the current Excel Cell
						if (name.contains("stoplight")) {
							// Stoplight time in form of off-time, on-time
							scan = new Scanner(name.replaceAll("[a-zA-Z]", ""));
							newGrid.cells[x][y] = new Stoplight(x, y, 0.5, scan.nextInt(), scan.nextInt(), scan.nextInt());
							
							// walkable, so add to the BFS explore set
							toExplore.add(newGrid.cells[x][y]);
						} else if (name.contains("wall")) {
							newGrid.cells[x][y] = new Wall(x, y);
						} else if (name.contains("open")) {
							//set minimum mult initially so that creating static field works
							newGrid.cells[x][y] = new Cell(x, y, 0);
							
							// walkable, so add to the BFS explore set
							toExplore.add(newGrid.cells[x][y]);
						} else if (name.contains("road")){
							newGrid.cells[x][y] = new Road(x, y);
						} else if (name.contains("exit")){
							// reads exit weight from cell
							scan = new Scanner(name.replaceAll("[a-zA-Z]", ""));
							if (scan.hasNextInt()) {
								newGrid.cells[x][y] = new Exit(x, y,scan.nextInt());	
							} else {
								newGrid.cells[x][y] = new Exit(x, y, 5);
							}
							
							// this is an exit, so add it to one of the reverse-bfs starting points
							currentSet.add(newGrid.cells[x][y]);
						} else if (name.contains("crosswalk")) {
							newGrid.cells[x][y] = new Crosswalk(x, y, 0.1);
							
							// walkable, so add to the BFS explore set
							toExplore.add(newGrid.cells[x][y]);
						}
						if (name.contains("-d")) {
							newGrid.doors.add(new Point(x, y));
						}
					}
					x++;
				}
				y++;
			}
			
			// initialize grid with actual weights to guide particles to exit
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
		
		System.out.println("grid is: " + grid.getHeight() + " by " + grid.getWidth());
		
		while(!toExplore.isEmpty() && !currentSet.isEmpty()) {
			Cell current = currentSet.remove();
			//set neighbor weights and add to the current exploring set
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					Cell temp = grid.getCell(current.x + i, current.y + j);
					if(temp!=null && toExplore.remove(temp)) {
						//decrement cell by appropriate amount if cell is diagonal
						if(Math.abs(i)==Math.abs(j))
							temp.setMult(current.mult-5);
						else
							temp.setMult(current.mult-3);
						currentSet.add(temp);
					}
				}
			}
		}
		
		if (!toExplore.isEmpty()) {
			System.out.println("WARNING: The entire Grid was NOT able to be explored to set the static field.");
			System.out.println("         The area containing the exits is probably blocked in by roads.");
		}
	}
	
	public void update() {
		// Update every cell in the grid
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (cells[x][y] != null) cells[x][y].update();
			}
		}
		time++;
	}
	
	/**
	 * Manually insert a Cell 
	 * @param x - x location of Cell to insert
	 * @param y - y location of Cell to insert
	 * @param cell - the Cell to insert
	 */
	public void setCell(int x, int y, Cell cell) {
		cells[x][y] = cell;
	}
	
	/**
	 * Gets the cell at a specific location. Returns null for an invalid location
	 * @param x - x location of Cell to retrieve
	 * @param y - y location of Cell to retrieve
	 * @return the cell at the location (x, y)
	 */
	public Cell getCell(int x, int y) {
		if(x < 0 || y < 0 || x >= cells.length || y>=cells[1].length)
			return null;
		return cells[x][y];
	}
	
	/**
	 * Gets width of cell grid
	 * @return width
	 */
	public int getWidth() {
		return cells.length;
	}
	
	/**
	 * Gets height of the Cell grid
	 * @return
	 */
	public int getHeight() {
		return cells[0].length;
	}
	
	/**
	 * Gets the ArrayList of all door locations
	 * @return the ArrayList
	 */
	public ArrayList<Point> getDoorLocations() {
		return doors;
	}
	
	/**
	 * Adds an attractor source
	 * @param add
	 */
	public void addAttractorSource(AttractorSource add) {
		pois.add(add);
	}
	
	/**
	 * Gets a list of all AttractorSources
	 * @return
	 */
	public ArrayList<AttractorSource> getAttractorSources() {
		return pois;
	}

	/**
	 * Sets reference to the list of Pedestrians that have exited the map
	 * @param exited
	 */
	public void setExited(ArrayList<Pedestrian> exited) {
		this.exited = exited;
	}
	
	/**
	 * Gets the reference to the list of Pedestrians that have exited the map
	 * @return
	 */
	public ArrayList<Pedestrian> getExited() {
		return this.exited;
	}
}
