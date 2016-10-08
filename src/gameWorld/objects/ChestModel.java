package gameWorld.objects;


import java.util.List;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;

/**
 * A class representing a chest in the game. Uses the super class ObjectModel for all it's stuff.
 * 
 * @author Aden
 */

public class ChestModel extends ObjectModel implements Buildable {

	public ChestModel(ObjectBuilder builder) {
		super(builder);
	}



	@Override
	public String getName() {
		return super.getName();
}
	@Override
	public int getID() {

		return super.getID();
	}

	@Override
	public int getValue() {

		return super.getValue();

	}

	@Override
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	public Type getType() {
		return super.getType();
	}
	
	@Override
	public Set<Integer> getItems() {
		return super.getItems();
	}
	
	@Override
	public void setItems(List<Integer> items) {
		super.setItems(items);
	}

}
