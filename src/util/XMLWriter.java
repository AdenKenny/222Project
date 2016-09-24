package util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataStorage.XMLReader;

/**
 * A class for creating a .xml file.
 *
 * @author Aden
 */

public final class XMLWriter {

	private enum Position {

		ITEM_ID(0, "itemID"),
		NAME(1, "name"),
		TYPE(2, "type"),
		VALUE(3, "value"),
		SALE_VALUE(4, "saleValue");

		final int pos;
		final String name;

		Position(int pos, String name) {
			this.pos = pos;
			this.name = name;
		}

		String getName() {
			return this.name;
		}

		static Position getPos(int i) {
			switch (i) {
			case 0:
				return Position.ITEM_ID;
			case 1:
				return Position.NAME;
			case 2:
				return Position.TYPE;
			case 3:
				return Position.VALUE;
			case 4:
				return Position.SALE_VALUE;
			default:
				return null;
			}
		}
	}

	private Document doc;
		
	public XMLWriter(String fileName, String readFile) {
		writeItems(fileName, readFile);
	}
	
	public void writeItems(String fileName, String readFile) {
		try {

			Element root = getRoot(); // Create a new root.

			this.doc.appendChild(root); // Append root to tree.

			Scanner scan = null;

			try {
				scan = new Scanner(new File("xml/" + readFile + ".txt"));

				while (scan.hasNextLine()) {
					String line = scan.nextLine();

					// Skip comments prefaced with //
					if (line.startsWith("//") || line.startsWith(" ")) continue;

					String[] arr = line.split(" "); //Split on space

					Element item = this.doc.createElement("item"); //Create new item.

					for (int i = 0; i < arr.length; ++i) { //For the amount of words.
						Position pos = Position.getPos(i); //Get what the field should be called.

						Element tag = this.doc.createElement(pos.getName()); //Create new element.
						tag.appendChild(this.doc.createTextNode(arr[i])); //Append value to field.

						item.appendChild(tag); //Append the field to the item.
					}

						String name = item.getElementsByTagName("name").item(0).getTextContent();
						Logging.logEvent(XMLWriter.class.getName(), Logging.Levels.EVENT, "Created XML of item: " + name);
						
						root.appendChild(item); //Append the new item to the root.
				}
			}
			catch (IOException e) {
			}

			finally {
				if (scan != null) {
					scan.close();
				}
			}
			transform("xml/" + fileName + ".xml"); // Print to file.
		}

		catch (ParserConfigurationException e) {
			Logging.logEvent(XMLWriter.class.getName(), Logging.Levels.SEVERE, "Failed to parse in XML Writer");
		}
	}
	
	private Element getRoot() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.newDocument(); // Create actual document.
		Element root = doc.createElement("items"); //The name of the node.

		this.doc = doc; // Set the root of the tree

		return root; // Return the root.
	}

	/**
	 * Outputs the tree to a .xml file.
	 *
	 * @param fileName
	 *            The name of the file we will be outputting to.
	 */

	private void transform(String fileName) {

		try {

			TransformerFactory transFactory = TransformerFactory.newInstance();

			Transformer transformer = transFactory.newTransformer();

			DOMSource source = new DOMSource(this.doc);
			StreamResult result = new StreamResult(new File(fileName));

			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formating
																		// options.
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);
		}

		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new XMLWriter("items", "items");
		XMLReader.getInstance();
	}
}
