package cs4230.pedestrian.graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cs4230.pedestrian.engine.TimeEngine;
import cs4230.pedestrian.objects.Grid;

/**
 * Parent container for the Graphics and Controls.
 * @author Nathan
 *
 */
public class MainPanel extends JPanel {

	/**
	 * Serial ID. We won't need this, probably, but it makes Eclipse shut up. 
	 */
	private static final long serialVersionUID = 8801067035854679711L;
	
	private ControlsPanel cPanel;
	private DisplayPanel dPanel;
	
	public MainPanel() {
		
		Grid gameGrid = Grid.loadFromXLSX("TestWalkAndDoor.xlsx");
		DisplayPanel.setGrid(gameGrid);
		
		dPanel = new DisplayPanel();
		TimeEngine engine = new TimeEngine(dPanel);
		cPanel = new ControlsPanel(engine);
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		add(dPanel);
		add(cPanel);
	}

}
