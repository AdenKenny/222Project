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
		System.out.println("Got graphics Click");
		client.displayEntityOptions(entity, x, y);
		if(entity==null) return;

		if(alt){
			// display, then get the index of the selected option
			client.displayEntityOptions(entity, x, y);
			// check Action.isClientAction()
			//		if true -> Action.perform(client) !needs to be MainWindow
			//		else send to server
		}
		else{
			//get default left click option for the entity
		}
	}

}
