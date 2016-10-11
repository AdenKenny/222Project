package gameWorld.objects;

import java.util.HashSet;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.AbstractBuilder;
import util.Logging;

/**
 * A builder to build Objects in the game. An object is a piece of furniture or a decoration.
 *
 * @author kennyaden - 300334300
 */

public class ObjectBuilder implements AbstractBuilder {

	private String buildID;
	private String buildType;
	private String buildValue;
	private String name;
	private String description;

	private int ID;
	private Type type;
	private int value;
	private Set<Integer> setOfItems;

	@Override
	public void setID(String ID) {
		this.buildID = ID;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setType(String type) {
		this.buildType = type;
	}

	@Override
	public void setValue(String value) {
		this.buildValue = value;
	}

	@Override
	public void setItems(String buildItems) {

		String temp = buildItems.replace(",", ""); // Remove commas.

		String[] itemValues = temp.split(" "); // Split into unique strings.

		this.setOfItems = new HashSet<>(); // Set to put item IDs in.

		try {

			for (String string : itemValues) {
				int itemVal = Integer.parseInt(string);
				this.setOfItems.add(itemVal); // Add the id to the set.
			}
		}

		catch (NumberFormatException e) {
			Logging.logEvent(ObjectBuilder.class.getName(), Logging.Levels.SEVERE, "Failed to build object.");
			e.printStackTrace();
		}
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the Type of the StationaryObject that is being built.
	 *
	 * @return The Type of the StationaryObject being built.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the Set of Integers containing the IDs of the Items in the
	 * StationaryObject that is being built.
	 *
	 * @return The IDs of the Items in the StationaryObject being built.
	 */
	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

	/**
	 * Builds an ObjectModel from the data that was read from XML.
	 *
	 * @return An ObjectModel that is fully loaded.
	 */

	@Override
	public ObjectModel build() {
		if (this.buildID == null || this.buildType == null || this.buildValue == null || this.name == null
				|| this.description == null || this.setOfItems == null) {

			return null;
		}

		try {
			this.ID = Integer.parseInt(this.buildID);
			this.value = Integer.parseInt(this.buildValue);
			this.type = Type.valueOf(this.buildType);

			return new ObjectModel(this);
		} catch (NumberFormatException e) {
			Logging.logEvent(ObjectBuilder.class.getName(), Logging.Levels.WARNING,
					"Improperly formatted XML file on stationary object loading.");
		}

		return null;
	}

	/**
	 * This should never be called.
	 */

	@Override
	@Deprecated
	public void setSaleValue(String value) {
		throw new AssertionError();
	}

}
