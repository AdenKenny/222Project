package gameWorld.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;

/**
 * A model representing an object in the game. An object is a piece of furniture or such
 * items that are mainly for decorative purposes.
 *
 * @author Aden
 */

public class ObjectModel implements Buildable {

	private String name;
	private int ID;
	private int value;
	private String description;
	private Type type;
	private Set<Integer> items;

	public ObjectModel(ObjectBuilder builder) {
		this.name = builder.getName();
		this.ID = builder.getID();
		this.value = builder.getValue();
		this.description = builder.getDescription();
		this.type = builder.getType();
		this.items = builder.getSetOfItems();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the Type of the StationaryObject that this model represents.
	 *
	 * @return The Type of the StationaryObject that this model represents.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the item contained in this object mode.
	 *
	 * @return An int representing the ID of an object or 0 if there are no items.
	 */

	 /*"This is only ever going to return the first one right?"
	 * "Yes."
	 * "Is that ok?"
	 * "Yes."
	 * "K."
	 */

	public int getItem() {
		for (int i : this.items) {
			return i;
		}
		return 0;
	}

	/**
	 * Sets the ID of the Item in the StationaryObject that this model represents.
	 *
	 * @param items
	 *            The IDs of the Items in the StationaryObject that this model represents.
	 */
	public void setItem(int item) {
		this.items = new HashSet<>();
		this.items.add(item);
	}
}
