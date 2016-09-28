package integrationGraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

import clientServer.ClientSideGame;
import gameWorld.World.Direction;

public class GraphicsPanel extends JPanel {

	private final static int WIDTH = 1000;
	private final static int HEIGHT = 650;

	private Map<Direction, Color> wallColors;
	private Color floorColor;
	
	private ClientSideGame game;

	public GraphicsPanel(ClientSideGame game) {
		this.game = game;
		
		wallColors = new HashMap<Direction, Color>();
		wallColors.put(Direction.NORTH, Color.GREEN.darker());
		wallColors.put(Direction.SOUTH, Color.BLUE.darker());
		wallColors.put(Direction.EAST, Color.RED.darker());
		wallColors.put(Direction.WEST, Color.YELLOW.darker());
		floorColor = Color.GRAY.darker();
	}

	@Override
	public Dimension getPreferredSize() {
		return isPreferredSizeSet() ?
			super.getPreferredSize() : new Dimension(WIDTH, HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		
		Color foreground = g.getColor();
		
		g.setColor(floorColor);
		int[] xs = {0, 150, 850, 1000};
		int[] ys = {650, 500, 500, 650};
		g.fillPolygon(xs, ys, xs.length);
		
		
		

		//  Custom code to paint all the Rectangles from the List

		/*Color foreground = g.getColor();

		g.setColor( Color.BLACK );
		g.drawString("Add a rectangle by doing mouse press, drag and release!", 40, 15);

		for (DrawingArea.ColoredRectangle cr : coloredRectangles)
		{
			g.setColor( cr.getForeground() );
			Rectangle r = cr.getRectangle();
			g.drawRect(r.x, r.y, r.width, r.height);
		}

		//  Paint the Rectangle as the mouse is being dragged

		if (shape != null)
		{
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor( foreground );
			g2d.draw( shape );
		}*/
	}

	public void clear() {
		//
		repaint();
	}
}
