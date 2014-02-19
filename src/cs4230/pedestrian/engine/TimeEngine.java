package cs4230.pedestrian.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.Timer;

import cs4230.pedestrian.graphics.DisplayPanel;
import cs4230.pedestrian.math.Statistics;
import cs4230.pedestrian.objects.AttractorSource;
import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Doors;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Pedestrian;

/**
 * Initializes the simulation and updates each cell and particle on a given time-step interval. 
 * @author Nathan
 *
 */
public class TimeEngine implements ActionListener {
	private Timer ticker;
	private Grid gameGrid;
	private PriorityQueue<Pedestrian> peds;
	private ArrayList<Pedestrian> exitPeds;
	private AttractorSource goal;
	private Doors doorMan;
	private long ticks = 0;
	
	private static final int PEDESTRIANS = 1500;
	
	private DisplayPanel dPanel;
	
	public static boolean DRAW_MAP = false;
	public static int NUM_EXPERIMENTS = 10;
	public int countExperiments;
	
	public TimeEngine(DisplayPanel panel) {
		
		countExperiments = 0;
		
		ticker = new Timer(0, this);
		
		gameGrid = DisplayPanel.getGrid();
		
		Pedestrian.setGrid(gameGrid);
		DisplayPanel.setGrid(gameGrid);
		Cell.setGrid(gameGrid);
		
		dPanel = panel;
		peds = new PriorityQueue<Pedestrian>();
		exitPeds = new ArrayList<Pedestrian>();
		
		Statistics.setPedestrianNumber(PEDESTRIANS);
		Statistics.setEngine(this);
		
		for (int i = 0; i < PEDESTRIANS; i++) {
			peds.add(new Pedestrian(Pedestrian.generateUniform()));	
		}

		// These are commented-out, since we probably won't need them.
		// We have a new system for attraction points
		//contaminant = new AttractorSource(0, 11, -1);
		//goal = new AttractorSource(47, 8, 1);
		
		//gameGrid.addAttractorSource(goal);
		//gameGrid.addAttractorSource(contaminant);
		
		
		doorMan = new Doors(gameGrid.getDoorLocations(), gameGrid);
		gameGrid.setExited(exitPeds);
		
		// non-functional style. "peds" and "exitPeds" are actually updated by the Doors class
		// TODO: consider making the style more functional, since that's what the rest of the
		// project uses
		doorMan.setQueue(peds);
		doorMan.setExited(exitPeds);
		
		// Start the simulation
		ticker.start();
	}
	
	
	public void resetEngine() {
		
		ticker.stop();
		
		if(countExperiments >= NUM_EXPERIMENTS) {
			System.exit(1);
		}
		
		countExperiments++;
		
		//Resets all statistic counters
		Statistics.setPedestrianNumber(PEDESTRIANS);
		
		peds = new PriorityQueue<Pedestrian>();
		exitPeds = new ArrayList<Pedestrian>();
		
		
		for (int i = 0; i < PEDESTRIANS; i++) {
			peds.add(new Pedestrian(Pedestrian.generateUniform()));	
		}

		doorMan = new Doors(gameGrid.getDoorLocations(), gameGrid);
		gameGrid.setExited(exitPeds);
		
		// non-functional style. "peds" and "exitPeds" are actually updated by the Doors class
		// TODO: consider making the style more functional, since that's what the rest of the
		// project uses
		doorMan.setQueue(peds);
		doorMan.setExited(exitPeds);
		
		// Start the new simulation
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
		
		if (DRAW_MAP)
			dPanel.update();

		for (Pedestrian ped : exitPeds) {
			if (ped.getX() >= 0 && ped.getY() >= 0) {
				ped.requestMove();
				//System.out.println("X: " + ped.x + ", Y: " + ped.y);
			}
		}
		
		gameGrid.update();
		ticks++;
	}

	public long getTicks() {
		return ticks;
	}

}
