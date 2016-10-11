package util;

/**
 * An interface implemented by builders that build abstract concepts in the game
 * such as an item or a character model. The object that a builder that
 * implements this interface should implement 'Buildable' as all objects built
 * by builders must be buildable.
 *
 * @author kennyaden - 300334300
 */

public interface AbstractBuilder {

	/**
	 * Sets the ID of something being built. Should probably use
	 * Integer.parseInt().
	 *
	 * @param ID
	 *            A string representing the ID read from file. This will then be
	 *            parsed to an int.
	 */
	void setID(String ID);

	/**
	 * Sets the name of the object being built. Shouldn't require any parsing.
	 *
	 * @param name
	 *            A string representing the name. Should be read from a file.
	 */
	void setName(String name);

	/**
	 * Sets the type of the object being built. This will be an enum from the
	 * class of the object being built. The string will be parsed to the
	 * relevant enum.
	 *
	 * @param type
	 *            A string representing the type i.e. MONSTER =
	 *            Characters.type.MONSTER.
	 */
	void setType(String type);

	/**
	 * Sets the value of the object being built. This represents the power of an
	 * item or monster. This value will be parsed from a string read from the
	 * file.
	 *
	 * @param value
	 *            A string representing the value. This will be a string read
	 *            from a file.
	 */
	void setValue(String value);

	/**
	 * Returns the ID of an object that has been built.
	 *
	 * @return An int representing the ID of the object.
	 */
	int getID();

	/**
	 * Sets the items that this object contains(?) (This is really generic as this is a generic interface).
	 *
	 * @param items A string representing the items that will be set.
	 * @throws AssertionError Thrown if this method is called in ItemBuilder as this is irrelevant there.
	 */

	void setItems(String items) throws AssertionError;


	/**
	 * Returns the name of an object that has been built.
	 *
	 * @return A string representing the name of the object.
	 */
	String getName();

	/**
	 * Returns the value of an object that has been built.
	 *
	 * @return An int representing the value of an object.
	 */
	int getValue();

	/**
	 * Sets the description of an object. This will be displayed to users in
	 * game when examining the object.
	 *
	 * @param description
	 *            A String representing the description of the object.
	 */
	void setDescription(String description);

	/**
	 * Sets the sale value of an object.
	 *
	 * Note: This should only be called in ItemBuilder as the sale value is irrelevant anywhere else.
	 *
	 * @param value A string representing the value of an object.
	 * @throws AssertionError Thrown when this is called anywhere but ItemBuilder.
	 */

	void setSaleValue(String value) throws AssertionError;


	/**
	 * Gets the description of an object that has been built.
	 *
	 * @return A string representing the description of an object.
	 */
	String getDescription();

	/**
	 * Builds an object from the params pased to the build. This will return a
	 * type of 'Buildable' which requires that anything being built by a class
	 * that implements 'AbstractBuilder' as an interface must itself implement
	 * the 'Buildable' interface.
	 *
	 * @return The object that has been built by the builder. This must
	 *         implement 'Buildable'.
	 */
	Buildable build();
}
