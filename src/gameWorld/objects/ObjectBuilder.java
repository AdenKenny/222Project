package gameWorld.objects;

import java.util.HashSet;
import java.util.Set;

import gameWorld.characters.CharacterBuilder;
import gameWorld.objects.StationaryObject.Type;
import util.AbstractBuilder;
import util.Logging;

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

	public void setBuildItems(String buildItems) {

		String temp = buildItems.replace(",", ""); //Remove commas.

		String[] itemValues = temp.split(" "); //Split into unique strings.

		this.setOfItems = new HashSet<>(); //Set to put item IDs in.

		try {

			for(String string : itemValues) {
				int itemVal = Integer.parseInt(string);
				this.setOfItems.add(itemVal); //Add the id to the set.
			}
		}

		catch (NumberFormatException e){
			Logging.logEvent(CharacterBuilder.class.getName(), Logging.Levels.SEVERE, "Failed to build object.");
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

	public Type getType() {
		return this.type;
	}

	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

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

}
