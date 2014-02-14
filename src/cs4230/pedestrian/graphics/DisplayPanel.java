package cs4230.pedestrian.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Grid;
import cs4230.pedestrian.objects.Particle;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -5184830046918043282L;
	public static final int TILE_PX = 20;
	private static Grid grid;
	private BufferedImage ground;
	private BufferedImage particleLayer;
	private BufferedImage cellLayer;
	private int originX = 0;
	private int originY = 0;
	private int mouseButtonMask;
	private Point mousePress;
	private double zoomFactor = 1.0;
	
	
	public static void setGrid(Grid toSet) {
		grid = toSet;
	}
	
	public DisplayPanel() {
		Dimension size = new Dimension(1000, 600);
		setPreferredSize(size);
		this.ground = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.particleLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.cellLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initializeGround();
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseHandler());
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		this.requestFocus();
	}
	
	public void update() {
		clearParticleLayer();
		clearCellLayer();
		
		drawPedestrians();
		drawCells();
		repaint();
	}
	
	private void initializeGround() {
		Graphics bgfx = this.ground.getGraphics();
		bgfx.setColor(Color.WHITE);
		bgfx.fillRect(0, 0, ground.getWidth(), ground.getHeight());
		
		bgfx.setColor(Color.BLACK);
		// Paint Grid Lines
		for (int x = 0; x < Grid.WIDTH; x++) {
			bgfx.drawLine(x * TILE_PX, 0, x * TILE_PX, ground.getHeight());
		}
		
		for (int y = 0; y < Grid.HEIGHT; y++) {
			bgfx.drawLine(0, y * TILE_PX, ground.getWidth(), y * TILE_PX);
		}
	}
	
	private void clearParticleLayer() {
		Graphics lgfx = this.particleLayer.getGraphics();
		bufferToClear((Graphics2D) lgfx);
	}
	
	private void drawPedestrians() {
		if (grid == null) {
			return;
		}
		
		Graphics plgfx = particleLayer.getGraphics();
		for (int x = 0; x < Grid.WIDTH; x++) {
			for (int y = 0; y < Grid.HEIGHT; y++) {
				Cell cell = grid.getCell(x, y);
				if (cell.isOccupied()) {
					Particle todraw = cell.getOccupant();
					todraw.draw(plgfx);
				}
			}
		}
	}
	
	private void clearCellLayer() {
		Graphics lgfx = this.particleLayer.getGraphics();
		bufferToClear((Graphics2D) lgfx);
	}
	
	private void bufferToClear(Graphics2D aGfx) {
		Composite translucent = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
		aGfx.setComposite(translucent);
		aGfx.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	private void drawCells() {
		if (grid == null) {
			return;
		}
		
		Graphics cgfx = cellLayer.getGraphics();
		for (int x = 0; x < Grid.WIDTH; x++) {
			for (int y = 0; y < Grid.HEIGHT; y++) {
				Cell cell = grid.getCell(x, y);
				cell.draw(cgfx);
			}
		}
	}
	
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
