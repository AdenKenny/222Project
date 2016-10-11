package ui.appwindow;

import Graphics.GraphicsClickListener;
import gameWorld.Entity;

/**
 * Click listener for graphics pane, receives result
 * of click on graphics pane and sends to MainWindow.
 *
 * @author normanclin
 *
 */
public class GuiGraphicsClickListener implements GraphicsClickListener {

	private MainWindow client;

	public GuiGraphicsClickListener(MainWindow client) {
		this.client = client;
	}

	/**
	 * Sends the clicked entity to the MainWindow, so it can display
	 * and options list.
	 */
	@Override
	public void onClick(Entity entity, boolean alt, int x, int y) {
		client.displayEntityOptions(entity, x, y);
		if(entity==null) return; //should never happen
	}

}