package Graphics;

import gameWorld.Entity;

import java.util.List;

/**
 * Class to listen for clicks on the GraphicsPanel.
 */
public interface GraphicsClickListener {

    void onClick(Entity entity, boolean alt, int x, int y);

}
