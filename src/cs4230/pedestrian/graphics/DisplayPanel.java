package cs4230.pedestrian.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import cs4230.pedestrian.math.LinCogRandom;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -5184830046918043282L;
	private LinCogRandom rand;
	private Random rrand;
	public DisplayPanel() {
		Dimension size = new Dimension(600, 600);
		setPreferredSize(size);
		rand = new LinCogRandom();
		rrand = new Random(10);
	}
	
	public void paintComponent(Graphics gfx)
	{
		super.paintComponent(gfx);
		gfx.setColor(Color.RED);
		int width = this.getWidth();
		int height = this.getHeight();
		for (int i = 0; i < 100000; i++) {
			gfx.fillRect(rand.nextInt(width), rand.nextInt(height), 1, 1);
		}
		gfx.setColor(Color.BLACK);
		// Border for JPanel -- nothing amazing
		gfx.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		
	}
}
