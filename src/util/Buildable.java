package util;

/**
 * A marker interface to provide type safety from the AbstractBuilder interface. The 'build' method in
 * that interface returns a type of 'Buildable' which must be either an 'Item', a 'Character' or a
 * 'CharacterModel' as those are the two classes that implement this class.
 *
 * @author Aden
 */

public interface Buildable {

	/**
	 * Returns the name of the object who's class implements this interface, Buildable.
	 *
	 * @return A String representing the name of the object.
	 */

	String getName();

	/**
	 * Returns the ID of the object who's class implements this interface, Buildable.
	 *
	 * @return An int representing the ID of the object.
	 */

	int getID();

	/**
	 * Returns the value of the object who's class implements this interface, Buildable.
	 *
	 * Note: The term value is ambiguous, as the term naturally applies to Items, which does
	 * implement this interface, but Character also implements this, but value does not
	 * naturally apply, this value is then used a 'toughness' or 'dangerousness' level of
	 * a monster. For example a spider is tough than a dragon therefore would have a lower
	 * toughness and therefore a lower value when built.
	 *
	 * Note: Players do not use this.
	 *
	 * @return An int representing the value of the object.
	 */

	int getValue();

	/**
	 * Returns the description of the object who's class implements this interface, Buildable.
	 *
	 * @return A String representing the description of this object.
	 */

	String getDescription();

}