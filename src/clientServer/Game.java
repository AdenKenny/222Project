package clientServer;

import java.util.Map;

import dataStorage.XMLReader;
import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;

public interface Game {
	public final static XMLReader reader = XMLReader.getInstance();
	public final static Map<Integer, Item> mapOfItems = reader.getItems();
	public final static Map<Integer, CharacterModel> mapOfCharacters = reader.getCharacters();
	void tick();
}
