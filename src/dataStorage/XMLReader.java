package dataStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gameWorld.characters.CharacterBuilder;
import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import gameWorld.item.ItemBuilder;
import util.Logging;

/**
 * A class for reading XML input into the game, mainly regarding loading hardcoded items into the game.
 *
 * @author Aden
 */

public final class XMLReader {

	private Document doc;

	private Map<Integer, Item> mapOfItems; //Map of the items.
	private Map<Integer, CharacterModel> mapOfCharacters; //Map of the characters.

	private static XMLReader INSTANCE = null;

	private XMLReader() { //Singleton pattern.
		this.mapOfItems = readItems(); //Get items.
		this.mapOfCharacters = readCharacters();
	}

	/**
	 * Returns the instance of this (XMLReader) class. There can only be one as this class is a
	 * singleton. If the class has not already been created a new one will be created and after that
	 * this method will only return that single instance of the parent class.
	 *
	 * @return A singleton of the XMLReader class.
	 */

	public static synchronized XMLReader getInstance() { //Stop multiple threads accessing.

		if(INSTANCE == null) { //Do we need to create the singleton?
			INSTANCE = new XMLReader(); //Yes.
		}

		return INSTANCE; //Already exists.
	}

	/**
	 * Returns a HashMap<Integer, Item> with the items loaded in from XML mapped to their
	 * unique ID that was gotten from the file.
	 *
	 * @return A map of <Integer, Item> representing the items loaded in.
	 */

	private Map<Integer, Item> readItems() {
		File file = new File("xml/" + "items" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("item"); // Get all items.

			HashMap<Integer, Item> map = new HashMap<>();

			for (int i = 0; i < list.getLength(); ++i) {
				Node node = list.item(i);

				Element e = (Element) node; // This should be the base node of
											// an item.

				ItemBuilder build = new ItemBuilder(); // Build an item.

				String itemID = e.getElementsByTagName("ID").item(0).getTextContent();
				build.setID(itemID); // Set the ID.

				String name = e.getElementsByTagName("name").item(0).getTextContent();
				build.setName(name); // Set the name.

				String type = e.getElementsByTagName("type").item(0).getTextContent();
				build.setType(type); // Set the type.

				String value = e.getElementsByTagName("value").item(0).getTextContent();
				build.setValue(value); // Set the value.

				if(e.getElementsByTagName("saleValue").item(0) == null) {
					System.out.println(name);
				}

				String saleValue = e.getElementsByTagName("saleValue").item(0).getTextContent();
				build.setSaleValue(saleValue); // Set sale value.

				Item item = build.build(); // Build the item.

				map.put(item.getID(), item); // Put item in map with
												// ID as key.
			}

			return this.mapOfItems;
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return new HashMap<>();
	}

	/**
	 * Returns a HashMap<Integer, CharacterModel> with the characters loaded in from XML mapped
	 * to their unique ID that was also got from the file.
	 *
	 * @return A map of <Integer, CharacterModel> representing all the characters loaded in.
	 */

	private Map<Integer, CharacterModel> readCharacters() {

		File file = new File("xml/" + "characters" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("character"); // Get all characters.

			HashMap<Integer, CharacterModel> map = new HashMap<>();

			for (int i = 0; i < list.getLength(); ++i) {
				Node node = list.item(i);

				Element e = (Element) node; // This should be the base node of
											// an item.

				CharacterBuilder build = new CharacterBuilder(); // Build an item.

				String itemID = e.getElementsByTagName("ID").item(0).getTextContent();
				build.setID(itemID); // Set the ID.

				String name = e.getElementsByTagName("name").item(0).getTextContent();
				build.setName(name); // Set the name.

				String type = e.getElementsByTagName("type").item(0).getTextContent();
				build.setType(type); // Set the type.

				String value = e.getElementsByTagName("value").item(0).getTextContent();
				build.setValue(value); // Set the value.

				String buildItems = e.getElementsByTagName("items").item(0).getTextContent();
				build.setBuildItems(buildItems); // Set items.

				CharacterModel character = build.build(); // Build the character.

				map.put(character.getID(), character); // Put char in map with
												// ID as key.
			}
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return new HashMap<>();
	}

	/**
	 * Returns a Map<Integer, Item> of the items that were loaded from XML. An item is mapped to
	 * ID.
	 *
	 * @return A Map<Integer, Item>.
	 */

	public Map<Integer, Item> getItems() {

		if (this.mapOfItems.size() > 0) {
			return this.mapOfItems;
		}

		Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE, "Failed to load items from .xml file");

		return this.mapOfItems;

	}

	/**
	 * Returns a Map<Integer, Character of the characters that were loaded from XML. A character is mapped to
	 * ID.
	 *
	 * @return A Map<Integer, Character>.
	 */

	public Map<Integer, CharacterModel> getCharacters() {

		if(this.mapOfCharacters.size() > 0) { //Check to make sure we actually have correct XML.
			return this.mapOfCharacters;
		}

		Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE, "Failed to load chars from .xml file");

		return this.mapOfCharacters;
	}

	/**
	 * Gets all the nodes with the specified name in the tree structure.
	 *
	 * @param tagName A String representing the name of that we want to search for nodes with.
	 *
	 * @return A NodeList representing all the nodes. Note: NodeList doesn't implement 'Iterable'.
	 */

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}
}
