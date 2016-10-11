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
import gameWorld.item.Item;

/**
 * Used as the glassPane for MainWindow, allowing for an options box to be drawn
 * over top of everything. It receives input as the graphics pane is right
 * clicked on an Entity. The options will then be drawn according to click
 * position
 *
 * @author Clinton
 *
 */

public class OptionsPane extends JPanel  {
	public static final int OPTION_HEIGHT = 20;
	private static int OPTION_WIDTH = 150;
	private MainWindow window;
	private int x;
	private int y;
	private Entity clickedEntity; //clicked item or clicked entity must be null.
	private Item clickedItem;
	private List<Action> latestOptions;
	private MouseListener clickListener;


	public OptionsPane(MainWindow window) {
		this.window = window;
		this.setVisible(false);
	}

	public void displayAndDrawEntityList(int x, int y, Entity entity) {
		this.latestOptions = entity.actions();
		// make sure bounds are within main window
		if (x + OPTION_WIDTH > window.getWidth()) {
			x = window.getWidth() - OPTION_WIDTH;
		}
		if (y + OPTION_HEIGHT * this.latestOptions.size() + 40 > window.getHeight()) {
			y = window.getHeight() - OPTION_HEIGHT * this.latestOptions.size() - 40;
		}
		this.x = x;
		this.y = y;
		this.clickedEntity = entity;
		this.clickedItem = null;
		this.removeMouseListener(clickListener);
		this.clickListener = new OptionsListener(this);
		this.addMouseListener(clickListener);
		this.setBounds(new Rectangle(this.x, this.y, OPTION_WIDTH, OPTION_HEIGHT * this.latestOptions.size()));
		this.revalidate();
		this.setVisible(true);
		repaint();

	}

	public void displayAndDrawItemList(int x, int y, Item item) {
		this.latestOptions = item.actions();
		// make sure bounds are within main window
		if (x + OPTION_WIDTH > window.getWidth()) {
			x = window.getWidth() - OPTION_WIDTH;
		}
		if (y + OPTION_HEIGHT * this.latestOptions.size() + 40 > window.getHeight()) {
			y = window.getHeight() - OPTION_HEIGHT * this.latestOptions.size() - 40;
		}
		this.x = x;
		this.y = y;
		this.clickedItem = item;
		this.clickedEntity = null;
		this.removeMouseListener(clickListener);
		this.clickListener = new OptionsListener(this);
		this.addMouseListener(clickListener);
		this.setBounds(new Rectangle(this.x, this.y, OPTION_WIDTH, OPTION_HEIGHT * this.latestOptions.size()));
		this.revalidate();
		this.setVisible(true);
		repaint();

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, OPTION_WIDTH, OPTION_HEIGHT * this.latestOptions.size());

		for (int i = 0, size = latestOptions.size(); i < size; ++i) {
			g.drawLine(0, OPTION_HEIGHT * (i + 1), 150, OPTION_HEIGHT * (i + 1));
			g.drawString(latestOptions.get(i).name(), 2, OPTION_HEIGHT * (i + 1) - 10);
		}

	}

	/*
	 * Calculates the option selected based on y position
	 */
	public void selectOption(int y) {
		int listItem = y / 20;

		Action action = latestOptions.get(listItem);
		if (action.isClientAction()) {
			// perform Action
			action.perform(window);
		} else {
			if(clickedEntity!= null){
				window.performActionOnEntity(clickedEntity, action.name());
			}
			else if(clickedItem != null)
				window.performActionOnItem(clickedItem, action.name());
		}
	}
}
