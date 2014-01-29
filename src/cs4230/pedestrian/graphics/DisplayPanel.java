package cs4230.pedestrian.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import cs4230.pedestrian.objects.Cell;
import cs4230.pedestrian.objects.Grid;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -5184830046918043282L;
	private static final int TILE_PX = 40;
	private Grid grid;
	private BufferedImage ground;
	private BufferedImage particleLayer;
	private BufferedImage cellLayer;
	

	public DisplayPanel(Grid grid) {
		Dimension size = new Dimension(600, 600);
		setPreferredSize(size);
		this.grid = grid;
		this.ground = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.particleLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.cellLayer = new BufferedImage(TILE_PX * Grid.WIDTH, TILE_PX * Grid.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initializeGround();
		clearParticleLayer();
		clearCellLayer();
		
		drawPedestrians();
		drawCells();
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
		Composite old = ((Graphics2D)lgfx).getComposite();
		((Graphics2D)lgfx).setComposite(AlphaComposite.Clear);
		lgfx.fillRect(0, 0, particleLayer.getWidth(), particleLayer.getWidth());
		((Graphics2D)lgfx).setComposite(old);
	}
	
	private void drawPedestrians() {
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
		Graphics lgfx = this.cellLayer.getGraphics();
		Composite old = ((Graphics2D)lgfx).getComposite();
		((Graphics2D)lgfx).setComposite(AlphaComposite.Clear);
		lgfx.fillRect(0, 0, cellLayer.getWidth(), cellLayer.getWidth());
		((Graphics2D)lgfx).setComposite(old);
	}
	
	private void drawCells() {
		Graphics cgfx = cellLayer.getGraphics();
		for (int x = 0; x < Grid.WIDTH; x++) {
			for (int y = 0; y < Grid.HEIGHT; y++) {
				Cell cell = grid.getCell(x, y);
				int green = (int)Math.round(255*cell.getMultiplier());
				Color col = new Color(0, green, 0);
				cgfx.setColor(col);
				cgfx.fillRect(x * TILE_PX + 1, y * TILE_PX + 1, TILE_PX - 1, TILE_PX - 1);
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
