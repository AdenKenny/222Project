package integrationGraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import IDGUI.Frame;
import clientServer.ClientSideGame;
import clientServer.Slave;
import gameWorld.Entity;
import gameWorld.Room;
import gameWorld.World.Direction;
import gameWorld.characters.Character;

public class GraphicsPanel extends JPanel {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 650;

	private static final int IMG_WIDTH = 110;
	private static final int IMG_HEIGHT = 180;

	private static final String path = "assets/sprites/";

	private Map<Direction, Color> wallColors;
	private Color floorColor;

	private ClientSideGame game;
	private String username;

	public GraphicsPanel(ClientSideGame game, String username) {
		this.game = game;
		this.username = username;

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

		Room room = game.getRoom();
		Character player = game.getPlayer(username);

		if (room == null || player == null) {
			g.setColor(Color.GREEN.brighter());
			g.fillRect(0, 0, WIDTH, HEIGHT);
			return;
		}


		g.setColor(floorColor);
		int[] xs = {0, 150, 850, 1000};
		int[] ys = {650, 500, 500, 650};
		g.fillPolygon(xs, ys, 4);

		// front wall
		g.setColor(wallColors.get(player.facing()));
		xs = new int[] {150, 150, 850, 850};
		ys = new int[] {0, 500, 500, 0};
		g.fillPolygon(xs, ys, 4);

		Direction left = player.facing().getLeft();
		Direction right = player.facing().getRight();

		// left wall
		g.setColor(wallColors.get(left));
		xs = new int[] {0, 0, 150, 150};
		ys = new int[] {0, 650, 500, 0};
		g.fillPolygon(xs, ys, 4);

		// right wall
		g.setColor(wallColors.get(right));
		xs = new int[] {850, 850, 1000, 1000};
		ys = new int[] {0, 500, 650, 0};
		g.fillPolygon(xs, ys, 4);

		// lines
		g.setColor(Color.BLACK);
		g.drawLine(150, 0, 150, 500);
		g.drawLine(150, 500, 0, 650);
		g.drawLine(150, 500, 850, 500);
		g.drawLine(850, 500, 1000, 650);
		g.drawLine(850, 500, 850, 0);

		Entity[][] entities = room.entities();

		int row, col, rowTarget, colTarget, rowChange, colChange;

		switch (player.facing()) {
		case NORTH:
			row = 0;
			col = 0;
			rowTarget = entities.length;
			colTarget = entities[0].length;
			rowChange = 1;
			colChange = 1;
		case EAST:
			row = 0;
			col = entities[0].length-1;
			rowTarget = entities.length;
			colTarget = -1;
			rowChange = 1;
			colChange = -1;
		case SOUTH:
			row = entities.length-1;
			col = entities[0].length-1;
			rowTarget = -1;
			colTarget = -1;
			rowChange = -1;
			colChange = -1;
		default:
			row = entities.length-1;
			col = 0;
			rowTarget = -1;
			colTarget = entities[0].length;
			rowChange = -1;
			colChange = 1;
		}

		for (; row != rowTarget; row += rowChange) {
			for (; col != colTarget; col += colChange) {
				if (entities[row][col] != null) {
					String name = entities[row][col].name();
					String dir;
					if (entities[row][col] instanceof Character) {
						if (((Character)entities[row][col]).getType().equals(Character.Type.PLAYER)) {
							name = "player";
						}
					}
					if (entities[row][col].facing().equals(player.facing())) {
						dir = "back";
					} else if (entities[row][col].facing().equals(player.facing().getLeft())) {
						dir = "left";
					} else if (entities[row][col].facing().equals(player.facing().getRight())) {
						dir = "right";
					} else {
						dir = "back";
					}

					File sprite = new File(path + name + "/" + dir + ".png");

					try {
						InputStream in = getClass().getResourceAsStream(path + name + "/" + dir + ".png");

						BufferedImage img = ImageIO.read(sprite);

						int xScale, yScale;

						switch (player.facing()) {
						case NORTH:
							xScale = col;
							yScale = row;
						case EAST:
							xScale = row;
							yScale = entities[0].length - col;
						case SOUTH:
							xScale = entities[0].length - col;
							yScale = entities.length - row;
						default:
							xScale = entities.length - row;
							yScale = col;
						}

						double rowWidth = 700 + 300 * (2.0 * yScale - 1) / 18;
						int x = (int)((1000-rowWidth)/2 + rowWidth * (2.0 * xScale - 1) / 18);
						int y = 500 + (int) (150 * (2.0 * yScale - 1) / 18);

						g.drawImage(img, x, y, IMG_WIDTH, IMG_HEIGHT, null);
					} catch (IOException e) {
						System.out.println("Sprite reading failed");
					}
				}
			}
		}

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
