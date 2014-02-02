package cs4230.pedestrian.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.Timer;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.math.LinCogRandom;
import cs4230.pedestrian.objects.AttractorSource;
import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Doors;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Pedestrian;

public class TimeEngine implements ActionListener {
	private Timer ticker;
	private Grid gameGrid;
	private PriorityQueue<Pedestrian> peds;
	private ArrayList<Pedestrian> exitPeds;
	private AttractorSource contaminant;
	private Doors doorMan;
	
	private DisplayPanel dPanel;
	public TimeEngine(DisplayPanel panel) {
		ticker = new Timer(5, this);
		gameGrid = Grid.loadFromXLSX("C:\\Users\\Nathan\\git\\skk-cs4230-project-1\\Map.xlsx");
		Pedestrian.setGrid(gameGrid);
		DisplayPanel.setGrid(gameGrid);
		Cell.setGrid(gameGrid);
		dPanel = panel;
		peds = new PriorityQueue<Pedestrian>();
		exitPeds = new ArrayList<Pedestrian>();
		//double[][] field = {{0, 0, 0}, {0.2,0.1,0.1}, {0.1, 0.3, 0.1}};
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		peds.add(new Pedestrian(Pedestrian.generateUniform()));
		LinCogRandom rand = new LinCogRandom();
		
		//for (Pedestrian ped : peds) {
			//ped.setPosition(rand.nextInt(20), rand.nextInt(20));
		//}
		
		contaminant = new AttractorSource(0, 11, -0.5);
		doorMan = new Doors(new int[] {3}, new int[] {6}, gameGrid);
		doorMan.setQueue(peds);
		doorMan.setExited(exitPeds);
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
		doorMan.egress();
		dPanel.update();
		for (Pedestrian ped : exitPeds) {
			if (ped.getX() >= 0 && ped.getY() >= 0) {
				ped.requestMove();
			}
		}
		gameGrid.update();
	}

}
