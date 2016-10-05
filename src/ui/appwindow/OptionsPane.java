package ui.appwindow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import gameWorld.Action;

/**
 * Used as the glassPane for MainWindow, allowing for an options box
 * to be drawn over top of everything. It receives input as the graphics pane is
 * right clicked on an Entity. The options will then be drawn according to click position
 *
 * @author Clinton
 *
 */

public class OptionsPane extends JPanel  {
	public static int OPTION_HEIGHT;
	private MainWindow window;
	private int x = 100;
	private int y = 100;
	private List<Action> latestOptions;

	public OptionsPane(MainWindow window) {
		this.window = window;
		this.setVisible(false);

	}

	public void displayAndDrawList(int x, int y, List<Action> actions) {
		this.x = x;
		this.y = y;
		this.addMouseListener(new OptionsListener(this, new Rectangle(x, y, 150, 100)));
		this.setBounds(new Rectangle(x, y, 150, 100));
		this.revalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, 150, 100);

		g.drawLine(0, 20, 150, 20);

		g.drawLine(0, 40, 150, 40);

		g.drawLine(0, 60, 150, 60);

		g.drawLine(0, 80, 150, 80);
		g.drawLine(0, 100, 150, 100);

	}

	/*
	 * Calculates the option selected based on y position
	 */
	public void selectOption(int y) {
		int item = y%20;
	}

	public void hideList(){

	}
}
