package gameWorld.objects;


import java.util.List;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;

/**
 * A class representing a chest in the game. Uses the super class ObjectModel for all it's stuff.
 *
 * @author kennyaden - 300334300
 */

public class ChestModel extends ObjectModel {

	public ChestModel(ObjectBuilder builder) {
		super(builder);
	}

	/**
	 * Sets the items in a chest.
	 *
	 * @param items A List<Integer>. The Integer represents the UID of the item that should be in the chest.
	 */

	public void setItems(List<Integer> items) {
		for (int i : items) {
			super.setItem(i);
		}
	}

}
