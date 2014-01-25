package cs4230.pedestrian.graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = 3027249664206981502L;
	
	private GridLayout layout;
	
	private JButton stepBack;
	private JButton play;
	private JButton pause;
	private JButton stepForward;
	
	public ControlsPanel() {
		Dimension dims = new Dimension(600, 30);
		this.setPreferredSize(dims);
		layout = new GridLayout(0, 4);
		setLayout(layout);
		stepBack = new JButton("Step <");
		play = new JButton("Play");
		pause = new JButton("Pause");
		stepForward = new JButton("Step >");
		
		add(stepBack);
		add(play);
		add(pause);
		add(stepForward);
	}

}
