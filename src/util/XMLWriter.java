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

		ID(0, "ID"),
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

		/*
		 * TODO This is really bad and needs to be changed. This uses ordinility.
		 * This is really really bad and will be changed. //First noted on 21/9/16.
		 * Seen again 28/9/16.
		*/
		static Position getPos(int i) {
			switch (i) {
			case 0:
				return Position.ID;
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

	public XMLWriter(String itemsFile, String itemsRead, String charsFile, String charsRead) {
		writeItems(itemsFile, itemsRead);
		writeChars(charsFile, charsRead);
	}

	public void writeItems(String fileName, String readFile) {
		try {

			Element root = getRoot(fileName); // Create a new root.

			this.doc.appendChild(root); // Append root to tree.

			Scanner scan = null;

			try {
				scan = new Scanner(new File("xml/" + readFile + ".txt"));

				while (scan.hasNextLine()) {
					String line = scan.nextLine();

					// Skip comments prefaced with //
					if (line.startsWith("//") || line.startsWith(" "))
						continue;

					String[] temp = line.split("\\(");

					String values = temp[0];

					String[] arr = values.split(" "); // Split on space

					Element item = this.doc.createElement("item"); // Create new item.

					for (int i = 0; i < arr.length; ++i) { // For the amount of words.
						Position pos = Position.getPos(i); // Get what the field should be called.

						Element tag = this.doc.createElement(pos.getName()); // Create new element.
						tag.appendChild(this.doc.createTextNode(arr[i])); // Append value to field.

						item.appendChild(tag); // Append the field to the item.
					}

					String description = temp[1].substring(0, temp[1].length() - 1);

					Element desc = this.doc.createElement("description");
					desc.appendChild(this.doc.createTextNode(description));

					item.appendChild(desc);

					String name = item.getElementsByTagName("name").item(0).getTextContent();
					Logging.logEvent(XMLWriter.class.getName(), Logging.Levels.EVENT, "Created XML of item: " + name);

					root.appendChild(item); // Append the new item to the root.
				}
			} catch (IOException e) {
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

	public void writeChars(String fileName, String readFile) {

		try {

			Element root = getRoot(fileName);

			this.doc.appendChild(root); // Append root to tree.

			Scanner scan = null;

			try {

				scan = new Scanner(new File("xml/" + readFile + ".txt"));

				while (scan.hasNextLine()) {
					String line = scan.nextLine();

					String[] test = line.split("\\{"); // Split set of items from main details.

					String values = test[0]; // Main values, i.e. ID, name, type, and value.

					String[] details = values.split(" "); // Split the main values on space.

					Element character = this.doc.createElement("character");

					for (int i = 0; i < details.length; ++i) { // For the amount of words.
						Position pos = Position.getPos(i); // Get what the field should be called.

						Element tag = this.doc.createElement(pos.getName()); // Create new element.
						tag.appendChild(this.doc.createTextNode(details[i])); // Append value to
																				// field.

						character.appendChild(tag); // Append the field to the item.
					}

					String other = test[1]; // Set of items.

					String[] tmp = other.split("\\(");

					String items = tmp[0];

					items = items.substring(0, items.length() - 2); //Remove end curly brace.

					Element tag = this.doc.createElement("items"); //Create the items node.
					tag.appendChild(this.doc.createTextNode(items));

					character.appendChild(tag); //Append the items to the char.

					Element description = this.doc.createElement("description");

					String desc = tmp[1].substring(0, tmp[1].length() - 1);

					description.appendChild(this.doc.createTextNode(desc));

					character.appendChild(description);

					String name = character.getElementsByTagName("name").item(0).getTextContent();
					Logging.logEvent(XMLWriter.class.getName(), Logging.Levels.EVENT, "Created XML of char: " + name);

					root.appendChild(character); // Append the new char to the root.

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

	private Element getRoot(String fileName) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.newDocument(); // Create actual document.
		Element root = doc.createElement(fileName); // The name of the node.

		this.doc = doc; // Set the root of the tree

		return root; // Return the root.
	}

	/**
	 * Outputs the tree to a .xml file.
	 *itemID
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
		new XMLWriter("items", "items", "characters", "characters");
		XMLReader.getInstance();
	}
}