package gameWorld.characters;

import java.util.HashSet;
import java.util.Set;

import util.AbstractBuilder;
import util.Logging;

/**
 * A class to build a 'CharacterModel' from input read from XML.
 *
 * @author Aden
 */

public final class CharacterBuilder implements AbstractBuilder {

	private String buildID;
	private String buildName;
	private String buildType;
	private String buildValue;
	private String buildItems;
	private String buildDescription;

	private int ID;
	private String name;
	private Character.Type type;
	private int value;
	private Set<Integer> setOfItems;
	private String description;

	@Override
	public void setID(String buildID) {
		this.buildID = buildID;
	}

	@Override
	public void setName(String buildName) {
		this.buildName = buildName;
	}

	@Override
	public void setType(String buildType) {
		this.buildType = buildType;
	}

	@Override
	public void setValue(String buildValue) {
		this.buildValue = buildValue;
	}

	@Override
	public void setItems(String buildItems) {

		this.buildItems = buildItems.replace(",", ""); // Remove commas.

		String[] itemValues = this.buildItems.split(" "); // Split into unique strings.

		this.setOfItems = new HashSet<>(); // Set to put item IDs in.

		try {

			for(String string : itemValues) {
				int valueS = Integer.parseInt(string);
				this.setOfItems.add(valueS); //Add the id to the set.
			}
		}

		catch (NumberFormatException e) {
			Logging.logEvent(CharacterBuilder.class.getName(), Logging.Levels.SEVERE, "Failed to build character.");
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

	public Character.Type getType() {
		return this.type;
	}

	/**
	 * <pre>
	 * {@code
	* Set<String> s;
	* System.out.println(s);
	* }
	 * 
	 * </pre>
	 **/

	@Override
	public int getValue() {
		return this.value;
	}

	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

	@Override
	public void setDescription(String description) {
		this.buildDescription = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public CharacterModel build() {

		if (this.buildID == null || this.buildName == null || this.buildType == null || this.buildValue == null || this.buildItems == null
				|| this.buildDescription == null) {
			return null;
		}

		try {
			this.ID = Integer.parseInt(this.buildID);
			this.name = this.buildName;
			this.type = Character.Type.valueOf(this.buildType);
			this.value = Integer.parseInt(this.buildValue);
			setItems(this.buildItems);
			this.description = this.buildDescription;

			return new CharacterModel(this);
		}

		catch (NumberFormatException e) {
			Logging.logEvent(CharacterBuilder.class.getName(), Logging.Levels.WARNING, "Improperly formatted XML file on item loading.");

		}

		return null;
	}

	@Override
	public void setSaleValue(String value) {
		throw new AssertionError();
	}

}
