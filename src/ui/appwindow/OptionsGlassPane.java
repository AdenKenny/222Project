package ui.appwindow;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JComponent;

import gameWorld.Action;

/**
 * Used as the glassPane for MainWindow, allowing for an options box 
 * to be drawn over top of everything. It receives input as the graphics pane is 
 * right clicked on an Entity. The options will then be drawn according to click position
 * 
 * @author Clinton
 *
 */

public class OptionsGlassPane extends JComponent  {
	private MainWindow window;
	private int x = 100;
	private int y = 100;
	private List<Action> latestOptions;
	
	public OptionsGlassPane(MainWindow window) {
		this.window = window;
	}
	
	public void displayAndDrawList(int x, int y, List<Action> actions) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.yellow);
		g.fillRect(x, y, 100, 100);
	}

	/*
	 * Calculates the option selected based on y position
	 */
	public void selectOption(int y) {
		
		
	}
	
	public void hideList(){
		
	}
}
