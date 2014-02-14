package cs4230.pedestrian.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.Timer;

import cs4230.pedestrian.graphics.DisplayPanel;
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
	private AttractorSource goal;
	private Doors doorMan;
	private long ticks = 0;
	
	private DisplayPanel dPanel;
	public TimeEngine(DisplayPanel panel) {
		ticker = new Timer(5, this);
		gameGrid = Grid.loadFromXLSX("BelowManagementBuilding.xlsx");
		
		Pedestrian.setGrid(gameGrid);
		DisplayPanel.setGrid(gameGrid);
		Cell.setGrid(gameGrid);
		
		dPanel = panel;
		peds = new PriorityQueue<Pedestrian>();
		exitPeds = new ArrayList<Pedestrian>();
		
		for (int i = 0; i < 500; i++) {
			peds.add(new Pedestrian(Pedestrian.generateUniform()));	
		}

		contaminant = new AttractorSource(0, 11, -1);
		goal = new AttractorSource(47, 8, 1);
		
		gameGrid.addAttractorSource(goal);
		//gameGrid.addAttractorSource(contaminant);
		
		doorMan = new Doors(gameGrid.getDoorLocations(), gameGrid);
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
		ticks++;
	}

	public long getTicks() {
		return ticks;
	}

}
