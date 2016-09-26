package util;

/**
 * A marker interface to provide type safety from the AbstractBuilder interface. The 'build' method in
 * that interface returns a type of 'Buildable' which must be either an 'Item' or a
 * 'CharacterModel' as those are the two classes that implement this class.
 *
 * @author Aden
 */

public interface Buildable {

	String getName();

	void setName(String name);

	int getID();

	void setID(int ID);

	int getValue();

	void setValue(int value);

}