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
			FileInputStream file = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iter = sheet.iterator();
			int width = 0, height = 0;
			while (iter.hasNext()) {
				Row currentRow = iter.next();
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				height++;
				int twidth = 0;
				while (colIter.hasNext()) {
					twidth++;
					width = Math.max(width, twidth);
					colIter.next();
				}
			}
			
			newGrid = new Grid(width, height);
			
			int x = 0, y = 0;
			
			
			file = new FileInputStream(new File(path));
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			iter = sheet.iterator();
			
			//data structures for creating static field around exits
			PriorityQueue<Cell> currentSet = new PriorityQueue<Cell>();
			HashSet<Cell> toExplore = new HashSet<Cell>();
			
			
			Scanner scan;
			
			while (iter.hasNext() && y < height) {
				x = 0;
				Row currentRow = iter.next();
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				
				while (colIter.hasNext() && x < width) {
					org.apache.poi.ss.usermodel.Cell xlCell = colIter.next();
					if (xlCell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING) {
						String name = xlCell.getStringCellValue();
						System.out.println("name: " + name);
						// TODO: Move these string constants to the Cell class, and expand them
						if (name.contains("stoplight")) {
							// Stoplight time in form of off-time, on-time
							scan = new Scanner(name);
							newGrid.cells[x][y] = new Stoplight(x, y, 0.5, scan.nextInt(), scan.nextInt());
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
							// reads exit weight from cell
							scan = new Scanner(name);
							newGrid.cells[x][y] = new Exit(x, y,scan.nextInt());
							currentSet.add(newGrid.cells[x][y]);
						} else if (name.contains("crosswalk")) {
							newGrid.cells[x][y] = new Crosswalk(x, y, 0.1);
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
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if(cells[x][y]!=null) cells[x][y].update();
			}
		}
		time++;
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

	public void setExited(ArrayList<Pedestrian> exited) {
		this.exited = exited;
	}
	
	public ArrayList<Pedestrian> getExited() {
		return this.exited;
	}
}
