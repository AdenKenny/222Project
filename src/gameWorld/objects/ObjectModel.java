package gameWorld.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;

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
	 * Returns the IDs of the Items in the StationaryObject that this model represents.
	 * DO NOT USE THIS METHOD, USE getItems() INSTEAD!
	 *
	 * @return The IDs of the Items in the StationaryObject that this model represents.
	 */
	public Set<Integer> getItems() {
		return null;
	}

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
