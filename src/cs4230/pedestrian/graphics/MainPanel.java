package cs4230.pedestrian.graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cs4230.pedestrian.objects.Grid;

public class MainPanel extends JPanel {

	/**
	 * Serial ID. We won't need this, probably, but it makes Eclipse shut up. 
	 */
	private static final long serialVersionUID = 8801067035854679711L;
	
	private ControlsPanel cPanel;
	private DisplayPanel dPanel;
	
	public MainPanel() {
		cPanel = new ControlsPanel();
		dPanel = new DisplayPanel(new Grid());
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		add(dPanel);
		add(cPanel);
	}

}
