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
		if(entity==null) return;
		
		if(alt){
			client.displayItemOptions(entity.actions(), x, y);
		}
		else{
			//get default left click option for the entity
		}
	}

}
