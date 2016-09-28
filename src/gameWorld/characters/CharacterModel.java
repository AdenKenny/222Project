package gameWorld.characters;

import java.util.Set;

import util.Buildable;

/**
 * A class representing the abstract concept of a character. This stores values of the character
 * such as the unique ID and the name. An actual instance of this character uses this to build
 * itself.
 *
 * @author Aden
 */

public final class CharacterModel implements Buildable {

	private int ID;
	private String name;
	private Character.Type type;
	private int value;
	private Set<Integer> setOfItems;
	private String description;

	public CharacterModel(CharacterBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.setOfItems = builder.getSetOfItems();
		this.description = builder.getDescription();
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the type
	 */
	public Character.Type getType() {
		return this.type;
	}

	/**
	 * @return the value
	 */
	@Override
	public int getValue() {
		return this.value;
	}

	/**
	 * @return the setOfItems
	 */
	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
