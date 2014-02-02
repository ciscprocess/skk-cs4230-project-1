package cs4230.pedestrian.graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import cs4230.pedestrian.engine.TimeEngine;

public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = 3027249664206981502L;
	
	private GridLayout layout;
	
	private JButton stepBack;
	private JButton play;
	private JButton pause;
	private JButton stepForward;
	private TimeEngine controls;
	
	public ControlsPanel(TimeEngine controls) {
		Dimension dims = new Dimension(600, 30);
		this.setPreferredSize(dims);
		layout = new GridLayout(0, 4);
		setLayout(layout);
		stepBack = new JButton("Step <");
		play = new JButton("Play");
		pause = new JButton("Pause");
		stepForward = new JButton("Step >");
		this.controls = controls;
		pause.addActionListener(new PauseAction());
		play.addActionListener(new PlayAction());
		add(stepBack);
		add(play);
		add(pause);
		add(stepForward);
	}
	
	class PauseAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controls.getTicker().stop();
		}
	}
	
	class PlayAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controls.getTicker().start();
		}
	}

}
