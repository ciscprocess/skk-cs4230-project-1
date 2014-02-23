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
		
<<<<<<< HEAD
		Grid gameGrid = Grid.loadFromXLSX("EvacMap.xlsx");
=======
		Grid gameGrid = Grid.loadFromXLSX("BelowManagementBuilding.xlsx");
>>>>>>> 1b5dea61d8bfb8b35f58aae565754f5133f75672
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
