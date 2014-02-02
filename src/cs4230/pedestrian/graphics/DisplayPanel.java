package cs4230.pedestrian.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cs4230.pedestrian.math.Statistics;
import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Grid;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -5184830046918043282L;
	public static final int TILE_PX = 30;
	private static Grid grid;
	private BufferedImage ground;
	private BufferedImage particleLayer;
	private BufferedImage cellLayer;
	
	public static void setGrid(Grid toSet) {
		grid = toSet;
	}
	
	public DisplayPanel() {
		Dimension size = new Dimension(600, 600);
		setPreferredSize(size);
		this.ground = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.particleLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.cellLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initializeGround();
		
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
			bgfx.drawLine(0, y * TILE_PX, ground.getHeight(), y * TILE_PX);
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
		plgfx.setColor(Color.RED);
		for (int x = 0; x < Grid.WIDTH; x++) {
			for (int y = 0; y < Grid.HEIGHT; y++) {
				Cell cell = grid.getCell(x, y);
				if (cell.isOccupied()) {
					plgfx.fillOval(x * TILE_PX + 3, y * TILE_PX + 3, TILE_PX - 6, TILE_PX - 6);
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
		gfx.drawImage(ground, 0, 0, null);
		
		// Draw Cells
		gfx.drawImage(cellLayer, 0, 0, null);
		
		// Draw Pedestrians
		gfx.drawImage(particleLayer, 0, 0, null);
		
		// Border for JPanel -- nothing amazing
		gfx.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}
