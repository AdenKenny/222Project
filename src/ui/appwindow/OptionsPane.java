package ui.appwindow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import gameWorld.Action;
import gameWorld.Entity;

/**
 * Used as the glassPane for MainWindow, allowing for an options box
 * to be drawn over top of everything. It receives input as the graphics pane is
 * right clicked on an Entity. The options will then be drawn according to click position
 *
 * @author Clinton
 *
 */

public class OptionsPane extends JPanel  {
	public static int OPTION_HEIGHT = 20;
	private static int OPTION_WIDTH = 150;
	private MainWindow window;
	private int x;
	private int y;
	private Entity clickedEntity;
	private List<Action> latestOptions;
	private MouseListener clickListener;

	public OptionsPane(MainWindow window) {
		this.window = window;
		this.setVisible(false);
	}

	public void displayAndDrawList(int x, int y, Entity entity) {
		//make sure bounds are within main window
		if(x+OPTION_WIDTH > window.getWidth()){
			x = window.getWidth()-OPTION_WIDTH;
		}
		if(y+OPTION_HEIGHT*5 +40> window.getHeight()){ //TODO: make it the list size instead of OPTION_HEIGHT*5
			y = window.getHeight()-OPTION_HEIGHT*5-40; //TODO: and here
		}
		this.x = x;
		this.y = y;
		this.clickedEntity = entity;
		//this.latestOptions = clickedEntity.actions(); TODO: remove once working
		this.removeMouseListener(clickListener);
		this.clickListener = new OptionsListener(this);
		this.addMouseListener(clickListener);
		this.setBounds(new Rectangle(this.x, this.y, 150, 100));
		this.revalidate();
		this.setVisible(true);
		repaint();
		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, 149, 99);

		g.drawLine(0, 20, 150, 20);

		g.drawLine(0, 40, 150, 40);

		g.drawLine(0, 60, 150, 60);

		g.drawLine(0, 80, 150, 80);
		g.drawLine(0, 100, 150, 100);
		
		//TODO: Once get actions works uncomment
//		for(int i=0; i< latestOptions.size(); i++){
//			g.drawLine(0, OPTION_HEIGHT*(i+1), 150, OPTION_HEIGHT*(i+1));
//			g.drawString(latestOptions.get(i).name(), 2, OPTION_HEIGHT*(i+1)-10);
//		}

	}

	/*
	 * Calculates the option selected based on y position
	 */
	public void selectOption(int y) {
		int listItem = y/20;
		System.out.println("Selected List item: " + listItem);
		
		//TODO: Once get actions works uncomment
//		Action action = latestOptions.get(listItem);
//		if(action.isClientAction()){
//			//perform Action
//			action.perform(window);
//		}
//		else{
//			window.performActionOnEntity(clickedEntity.ID(), action.getID());
//		}
		
	}
}
