package cs4230.pedestrian.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Pedestrian;

public class TimeEngine implements ActionListener {
	private Timer ticker;
	private Grid gameGrid;
	private ArrayList<Pedestrian> peds;
	private DisplayPanel dPanel;
	public TimeEngine(DisplayPanel panel) {
		ticker = new Timer(30, this);
		gameGrid = Grid.loadFromXLSX("C:\\Users\\Nathan\\git\\skk-cs4230-project-1\\Map.xlsx");
		Pedestrian.setGrid(gameGrid);
		DisplayPanel.setGrid(gameGrid);
		Cell.setGrid(gameGrid);
		dPanel = panel;
		peds = new ArrayList<Pedestrian>();
		double[][] field = {{0, 0, 0}, {0.2,0.1,0.1}, {0.1, 0.3, 0.1}};
		peds.add(new Pedestrian(field));
		Pedestrian ourFriend = peds.get(0);
		ourFriend.setPosition(6, 1);
		ticker.start();
	}
	
	public Timer getTicker() {
		return ticker;
	}
	
	/**
	 * The game updates here.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		dPanel.update();
		Pedestrian ourFriend = peds.get(0);
		ourFriend.requestMove();
		gameGrid.update();
	}

}
