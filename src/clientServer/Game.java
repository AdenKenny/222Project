package clientServer;

import java.util.Map;

import dataStorage.XMLReader;
import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import gameWorld.objects.ObjectModel;

/**
 * The interface for client and server side games
 *
 * @author popesimo
 *
 */
public interface Game {
	public final static XMLReader reader = XMLReader.getInstance();
	public final static Map<Integer, Item> mapOfItems = reader.getItems();
	public final static Map<Integer, CharacterModel> mapOfCharacters = reader.getCharacters();
	public static final Map<Integer, ObjectModel> mapOfObjects = reader.getObjects();
	void tick();
}
