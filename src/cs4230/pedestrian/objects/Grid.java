package cs4230.pedestrian.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class contains all the cells in the Automata simulation. 
 * For now, it is an organizational placeholder.
 * @author Nathan
 */
public class Grid {
	
	public static final int WIDTH = 60;
	public static final int HEIGHT = 60;
	
	private Cell[][] cells;
	
	private Grid() {
		cells = new Cell[WIDTH][HEIGHT];
	}
	
	public static Grid loadFromXLSX(String path) {
		Grid newGrid = new Grid();
		
		try {
			FileInputStream file = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook (file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iter = sheet.iterator();
			int x = 0, y = 0;
			while (iter.hasNext()) {
				Row currentRow = iter.next();
				Iterator<org.apache.poi.ss.usermodel.Cell> colIter = currentRow.iterator();
				
				while (colIter.hasNext()) {
					org.apache.poi.ss.usermodel.Cell xlCell = colIter.next();
					if (xlCell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING) {
						String name = xlCell.getStringCellValue();
						
						// TODO: Move these string constants to the Cell class, and expand them
						if (name == "stoplight") {
							newGrid.cells[x][y] = new Cell(x, y, 0.5);
						} else if (name == "wall") {
							
						} else if (name == "grass") {
							
						} else {
							
						}
					}
					y++;
				}
				x++;
			}
			
		} catch (IOException e) {
			System.out.println("Failed to load map-defining Excel file.");
			e.printStackTrace();
		}
		return newGrid;
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
	
}
