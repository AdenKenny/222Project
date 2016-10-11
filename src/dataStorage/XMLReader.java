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

import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import gameWorld.objects.ObjectModel;
import util.BuilderBuilder;
import util.Logging;

/**
 * A class for reading XML input into the game, mainly regarding loading hardcoded items into the game.
 *
 * @author Aden
 */

public final class XMLReader implements XMLInteractable {

	private Document doc;

	private Map<Integer, Item> mapOfItems; // Map of the items.
	private Map<Integer, CharacterModel> mapOfCharacters; // Map of the
															// characters.
	private Map<Integer, ObjectModel> mapOfObjects;
	private static XMLReader INSTANCE = null;

	private XMLReader() { // Singleton pattern.

		this.mapOfItems = readItems(); // Get items.
		this.mapOfCharacters = readCharacters(); // Characters.
		this.mapOfObjects = readObjects(); // Objects.
	}

	/**
	 * Returns the instance of this (XMLReader) class as there can only be one as this class is a singleton. If the class has not already
	 * been created a new one will be created and after that this method will only return that single instance of the class.
	 *
	 * @return A singleton of the XMLReader class.
	 */

	public static synchronized XMLReader getInstance() { // Stop multiple threads accessing.
		if (INSTANCE == null) { // Do we need to create the singleton?
			INSTANCE = new XMLReader(); // Yes.
		}

		return INSTANCE; // Already exists.
	}

	/**
	 * Reads the objects from an xml file.
	 *
	 * @return A HashMap<Integer, ObjectModel> with the Integer representing the UID of the object.
	 */

	private Map<Integer, ObjectModel> readObjects() {
		File file = new File("xml/" + "objects" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
			this.doc = dBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("object"); // Get all objects.

			HashMap<Integer, ObjectModel> map = new HashMap<>();

			for (int i = 0, len = list.getLength(); i < len; ++i) {
				Node node = list.item(i);

				Element e = (Element) node;

				try {
					BuilderBuilder builder = new BuilderBuilder("Object"); // Object

					builder.addField("ID", e.getElementsByTagName("ID").item(0).getTextContent());
					builder.addField("name", e.getElementsByTagName("name").item(0).getTextContent());
					builder.addField("type", e.getElementsByTagName("type").item(0).getTextContent());
					builder.addField("value", e.getElementsByTagName("value").item(0).getTextContent());
					builder.addField("items", e.getElementsByTagName("items").item(0).getTextContent());
					builder.addField("description", e.getElementsByTagName("description").item(0).getTextContent());

					ObjectModel object = (ObjectModel) builder.build().build(); // Build the builder that builds the builder that builds the
																				// item.

					map.put(object.getID(), object);
				}

				catch (UnsupportedOperationException unOp) { // This was passed up the hierarchy.

					String exMes = unOp.getMessage();

					Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE,
							"Wrong builder (type \"" + exMes + "\" is not appropriate).");
				}

				catch (IllegalArgumentException ilArg) { // This was passed up the hierarchy.
					String exMes = ilArg.getMessage();

					Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE,
							"Bad field (field \"" + exMes + "\" is not appropriate).");

				}
			}

			return map;
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
	 * Returns a HashMap<Integer, Item> with the items loaded in from XML mapped to their unique ID that was gotten from the file.
	 *
	 * @return A map of <Integer, Item> representing the items loaded in.
	 */

	private Map<Integer, Item> readItems() {
		File file = new File("xml/" + "items" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
			this.doc = dBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("item"); // Get all items.

			HashMap<Integer, Item> map = new HashMap<>();

			for (int i = 0; i < list.getLength(); ++i) {
				Node node = list.item(i);

				Element e = (Element) node; // This should be the base node of an item.

				try {
					BuilderBuilder builder = new BuilderBuilder("Item");

					builder.addField("ID", e.getElementsByTagName("ID").item(0).getTextContent());
					builder.addField("name", e.getElementsByTagName("name").item(0).getTextContent());
					builder.addField("type", e.getElementsByTagName("type").item(0).getTextContent());
					builder.addField("value", e.getElementsByTagName("value").item(0).getTextContent());
					builder.addField("description", e.getElementsByTagName("description").item(0).getTextContent());
					builder.addField("saleValue", e.getElementsByTagName("saleValue").item(0).getTextContent());

					Item item = (Item) builder.build().build(); // Build the item.

					map.put(item.getID(), item); // Put item in map with ID as key.
				}

				catch (UnsupportedOperationException unOp) { // This was passed up the hierarchy.

					String exMes = unOp.getMessage();

					Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE,
							"Wrong builder (type \"" + exMes + "\" is not appropriate).");
				}

				catch (IllegalArgumentException ilArg) { // This was passed up the hierarchy.
					String exMes = ilArg.getMessage();

					Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE,
							"Bad field (field \"" + exMes + "\" is not appropriate).");
				}

			}

			return map;
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
	 * Returns a HashMap<Integer, CharacterModel> with the characters loaded in from XML mapped to their unique ID that was also got from
	 * the file.
	 *
	 * @return A map of <Integer, CharacterModel> representing all the characters loaded in.
	 */

	private Map<Integer, CharacterModel> readCharacters() {

		File file = new File("xml/" + "characters" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = factory.newDocumentBuilder();
			this.doc = dBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("character"); // Get all characters.

			HashMap<Integer, CharacterModel> map = new HashMap<>();

			for (int i = 0, len = list.getLength(); i < len; ++i) {
				Node node = list.item(i);

				Element e = (Element) node; // This should be the base node of
											// an item.

				BuilderBuilder builder = new BuilderBuilder("Character");

				builder.addField("ID", e.getElementsByTagName("ID").item(0).getTextContent());
				builder.addField("name", e.getElementsByTagName("name").item(0).getTextContent());
				builder.addField("type", e.getElementsByTagName("type").item(0).getTextContent());
				builder.addField("value", e.getElementsByTagName("value").item(0).getTextContent());
				builder.addField("items", e.getElementsByTagName("items").item(0).getTextContent());
				builder.addField("description", e.getElementsByTagName("description").item(0).getTextContent());

				CharacterModel character = (CharacterModel) builder.build().build();

				map.put(character.getID(), character); // Put char in map with
				// ID as key.
			}

			return map;
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
	 * Returns a Map<Integer, Item> of the items that were loaded from XML. An item is mapped to ID.
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
	 * Returns a Map<Integer, Character> of the characters that were loaded from XML. A character is mapped to ID.
	 *
	 * @return A Map<Integer, Character>.
	 */

	public Map<Integer, CharacterModel> getCharacters() {

		if (this.mapOfCharacters.size() > 0) { // Check to make sure we actually
												// have correct XML.
			return this.mapOfCharacters;
		}

		Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE, "Failed to load chars from .xml file");

		return new HashMap<>();
	}

	/**
	 * Returns a Map<Integer, ObjectModel> of the objects that were loaded from XML. An object is mapped to an ID.
	 *
	 * @return A Map<Integer, ObjectModel>.
	 */

	public Map<Integer, ObjectModel> getObjects() {
		if (this.mapOfObjects.size() > 0) {
			return this.mapOfObjects;
		}

		Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE, "Failed to load objects from .xml file");

		return new HashMap<>(); // Empty Map
	}

	/**
	 * Gets all the nodes with the specified name in the tree structure.
	 *
	 * @param tagName
	 *            A String representing the name of that we want to search for nodes with.
	 * @return A NodeList representing all the nodes. Note: NodeList doesn't implement 'Iterable'.
	 */

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}
}