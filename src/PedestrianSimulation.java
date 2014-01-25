import javax.swing.JFrame;

import cs4230.pedestrian.graphics.MainPanel;
import cs4230.pedestrian.math.LinCogRandom;

public class PedestrianSimulation {

	public static void main(String[] args) {
		JFrame meFrame = new JFrame("Pedestrian Simulation");
		meFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainPanel panel = new MainPanel();
		meFrame.add(panel);
		meFrame.pack();
		meFrame.setVisible(true);
		
		/*LinCogRandom rand = new LinCogRandom();
		for (int i = 0; i < 100000; i++) {
			System.out.println(rand.nextDouble());
		}*/
		
	}

}
