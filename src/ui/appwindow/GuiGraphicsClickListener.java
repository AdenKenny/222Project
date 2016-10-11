package ui.appwindow;

import Graphics.GraphicsClickListener;
import gameWorld.Entity;

public class GuiGraphicsClickListener implements GraphicsClickListener {

	private MainWindow client;

	public GuiGraphicsClickListener(MainWindow client) {
		this.client = client;
	}

	@Override
	public void onClick(Entity entity, boolean alt, int x, int y) {
		client.displayEntityOptions(entity, x, y);
		if(entity==null) return; //should never happen

		if(alt){
			// display, then get the index of the selected option
			//client.displayEntityOptions(entity, x, y);
		}
		else{
			//get default left click option for the entity
		}
	}

}