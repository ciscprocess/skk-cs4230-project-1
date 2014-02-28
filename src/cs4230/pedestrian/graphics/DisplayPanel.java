package cs4230.pedestrian.graphics;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Pedestrian;

/**
 * This class houses all most of the graphics routines for the application. 
 * However, do note that each Particle and Cell object is responsible for drawing itself.
 * 
 * @author Nathan
 */
public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -5184830046918043282L;
	public static final int TILE_PX = 20;
	private static Grid grid;
	//private BufferedImage ground;
	private BufferedImage particleLayer;
	private BufferedImage cellLayer;
	private int originX = 0;
	private int originY = 0;
	private int mouseButtonMask;
	private Point mousePress;
	private double zoomFactor = 1.0;
	
	/**
	 * This tells the DisplayPanel what it needs to draw.
	 * TODO: I don't like the way these things are static. It's messy and kinda bad form.
	 *       This isn't a very big deal, but if we have a moment, we should look at making them instance based.
	 * @param toSet
	 */
	public static void setGrid(Grid toSet) {
		grid = toSet;
	}
	
	public static Grid getGrid() {
		return grid;
	}
	
	public DisplayPanel() {
		// These are arbitrary dimensions. TODO: refactor these
		Dimension size = new Dimension(1000, 600);
		setPreferredSize(size);
		
		// These are the graphics layers. "ground" is currently not used.
		//this.ground = new BufferedImage(TILE_PX * grid.getWidth(), TILE_PX * grid.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.particleLayer = new BufferedImage(TILE_PX * grid.getWidth(), TILE_PX * grid.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.cellLayer = new BufferedImage(TILE_PX * grid.getWidth(), TILE_PX * grid.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		//initializeGround();
		
		// Add the event handlers which are defined at the bottom of this file
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseHandler());
		addKeyListener(new KeyHandler());
		
		// Necessary for the KeyHandler to actually receive events
		setFocusable(true);
		requestFocus();
		
		clearCellLayer();
		drawCells();
	}
	
	/**
	 * redraws the graphics layers at each tick.
	 */
	public void update() {
		clearParticleLayer();
		//clearCellLayer();
		
		drawPedestrians();
		//drawCells();
		repaint();
	}
	/*
	private void initializeGround() {
		Graphics bgfx = this.ground.getGraphics();
		bgfx.setColor(Color.WHITE);
		bgfx.fillRect(0, 0, ground.getWidth(), ground.getHeight());
		
		bgfx.setColor(Color.BLACK);
		// Paint Grid Lines
		for (int x = 0; x < grid.getWidth(); x++) {
			bgfx.drawLine(x * TILE_PX, 0, x * TILE_PX, ground.getHeight());
		}
		
		for (int y = 0; y < grid.getHeight(); y++) {
			bgfx.drawLine(0, y * TILE_PX, ground.getWidth(), y * TILE_PX);
		}
	}
	*/
	
	/**
	 * makes the particle graphics layer transparent
	 */
	private void clearParticleLayer() {
		Graphics lgfx = this.particleLayer.getGraphics();
		bufferToClear((Graphics2D) lgfx);
	}
	
	/**
	 * loop through all Cells, get the pedestrian, and tell them to draw themselves
	 */
	private void drawPedestrians() {
		if (grid == null) {
			return;
		}
		
		Graphics plgfx = particleLayer.getGraphics();
		for (Pedestrian ped : grid.getExited()) {
			ped.draw(plgfx);
		}
	}
	
	/**
	 * makes the cell graphics layer transparent
	 */
	private void clearCellLayer() {
		Graphics lgfx = this.particleLayer.getGraphics();
		bufferToClear((Graphics2D) lgfx);
	}
	
	
	/**
	 * clears out a graphics interface and makes it the transparent color
	 * @param aGfx - the graphics interface to clear
	 */
	private void bufferToClear(Graphics2D aGfx) {
		Composite translucent = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
		aGfx.setComposite(translucent);
		aGfx.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	/**
	 * loops through each cell and tells it to draw itself
	 */
	private void drawCells() {
		if (grid == null) {
			return;
		}
		
		Graphics cgfx = cellLayer.getGraphics();
		for (int x = 0; x < grid.getWidth(); x++) {
			for (int y = 0; y < grid.getHeight(); y++) {
				Cell cell = grid.getCell(x, y);
				if(cell!=null) cell.draw(cgfx);
			}
		}
	}
	
	/**
	 * overidden to draw the graphics layers to the screen
	 */
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
	
		// Draw the underlying grid each frame
		//gfx.drawImage(ground, originX, originY, 
				     //(int)(zoomFactor * ground.getWidth()), (int)(zoomFactor * ground.getHeight()), null);
		
		// Draw Cells
		gfx.drawImage(cellLayer, originX, originY, 
			     (int)(zoomFactor * cellLayer.getWidth()), (int)(zoomFactor * cellLayer.getHeight()), null);

		// Draw Pedestrians
		gfx.drawImage(particleLayer, originX, originY, 
			     (int)(zoomFactor * particleLayer.getWidth()), (int)(zoomFactor * particleLayer.getHeight()), null);
		
		// Border for JPanel -- nothing amazing
		gfx.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
	
	/**
	 * handles scrolling of the the image
	 * @author Nathan
	 */
	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent aEvt) {
			mouseButtonMask = aEvt.getButton();	
			mousePress = aEvt.getPoint();
		}
		
		@Override
		public void mouseDragged(MouseEvent aEvt) {
			if (mouseButtonMask == MouseEvent.BUTTON3)
			{
				originX += aEvt.getPoint().x - mousePress.x;
				originY += aEvt.getPoint().y - mousePress.y;
				
				DisplayPanel.this.repaint();
				mousePress = aEvt.getPoint();
			}
			else
			{
				mouseMoved(aEvt);
				mouseClicked(aEvt);	
			}
			
		}
	}
	
	/**
	 * Handles zooming of the image
	 * @author Nathan
	 */
	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			char c = e.getKeyChar();
			if (c == '+') {
				zoomFactor += 0.05;
			} else if (c == '-') {
				zoomFactor -= 0.05;
			}
	    }
	}
}
