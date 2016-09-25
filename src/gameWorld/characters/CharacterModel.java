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

	public CharacterModel(CharacterBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.setOfItems = builder.getSetOfItems();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Character.Type getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Character.Type type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the setOfItems
	 */
	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

	/**
	 * @param setOfItems
	 *            the setOfItems to set
	 */
	public void setSetOfItems(Set<Integer> setOfItems) {
		this.setOfItems = setOfItems;
	}
	
	public int getID() {
		return this.ID;
	}

	/**
	 * @param ID
	 *            the ID to set
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

}
