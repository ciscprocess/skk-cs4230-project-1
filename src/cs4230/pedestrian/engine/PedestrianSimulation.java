package cs4230.pedestrian.engine;
import javax.swing.JFrame;

import cs4230.pedestrian.graphics.MainPanel;

/**
 * Driver class for the application. Nothing interesting here.
 * @author Nathan
 */
public class PedestrianSimulation {

	public static void main(String[] args) {
		JFrame meFrame = new JFrame("Pedestrian Simulation");
		meFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainPanel panel = new MainPanel();
		meFrame.add(panel);
		meFrame.pack();
		
		if (TimeEngine.DRAW_MAP) {
			meFrame.setVisible(true);
		}
		
	}
}
