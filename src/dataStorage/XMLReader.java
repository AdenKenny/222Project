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

import gameWorld.item.Item;
import gameWorld.item.ItemBuilder;
import util.Logging;

public final class XMLReader {

	private Document doc;

	private Map<Integer, Item> mapOfItems;
	private Map<Integer, Character> mapOfCharacters;

	private static final XMLReader INSTANCE = new XMLReader(); //TODO fix this.
	
	private XMLReader() {
		this.mapOfItems = readItems();
	}
	
	public static XMLReader getInstance() {
		return INSTANCE;
	}
	
	/*public ReadXML(String itemsName, String charactersName) {

		//this.mapOfItems = readItems(itemsName);
		//this.mapOfCharacters = readCharacters(charactersName);

	}
*/
	private Map<Integer, Item> readItems() {
		File file = new File("xml/" + "items" + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("item"); //Get all items.

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

				String saleValue = e.getElementsByTagName("saleValue").item(0).getTextContent();
				build.setSaleValue(saleValue); // Set sale value.

				Item item = build.build(); // Build the item.

				map.put(item.getID(), item); // Put item in map with
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

		return this.mapOfItems;
	}

	private Map<Integer, Character> readCharacters(String charactersName) {
		HashMap<Integer, Character> map = new HashMap<>();

		return map;
	}

	/**
	 * Returns a Map<Integer, Item> of the items that were loaded from XML. An
	 * item is mapped to it's ID.
	 *
	 * @return A Map<Integer, Item>.
	 */

	public Map<Integer, Item> getItems() {

		if (this.mapOfItems != null) {
			return this.mapOfItems;
		}

		Logging.logEvent(XMLReader.class.getName(), Logging.Levels.SEVERE, "Failed to load items from .xml file");

		return new HashMap<>(); // Error results in no items being loaded.

	}

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

}
