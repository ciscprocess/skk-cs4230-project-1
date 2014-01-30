package cs4230.pedestrian.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Pedestrian;

public class TimeEngine implements ActionListener {
	private Timer ticker;
	private Grid gameGrid;
	private ArrayList<Pedestrian> peds;
	private DisplayPanel dPanel;
	public TimeEngine(DisplayPanel panel) {
		ticker = new Timer(100, this);
		gameGrid = new Grid();
		Pedestrian.setGrid(gameGrid);
		DisplayPanel.setGrid(gameGrid);
		dPanel = panel;
		peds = new ArrayList<Pedestrian>();
		double[][] mg = {{0.1, 0.1, 0}, {0.05, 0.05, 0.1}, {0.2, 0.2, 0.2}};
		peds.add(new Pedestrian(mg));
		Pedestrian ourFriend = peds.get(0);
		ourFriend.setPosition(10, 10);
		ticker.start();
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
