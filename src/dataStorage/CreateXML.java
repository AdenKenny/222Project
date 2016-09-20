package dataStorage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
import org.w3c.dom.Element; //TODO Is this allowed?

import userHandling.User;

/**
 * A class for creating a .xml file.
 * 
 * @author Aden
 */

public class CreateXML implements XMLInterface {

	Document doc;

	public CreateXML(String fileName) {

		try {

			Element root = getRoot(); // Create a new root.

			this.doc.appendChild(root); // Append root to tree.

			Set<User> users = new HashSet<>(); // DataGetter.getUsers(); //TODO Fix this.

			Element playersNode = appendNode("Players", root); // Append players node.

			users.add(new User(15l, "Aden", "151231231j"));

			for (User user : users) { // Append all users to tree.
				String playerID = String.valueOf(user.getId());
				String username = user.getUsername();

				appendNode(username, "ID", playerID, playersNode); // Swap around ID and username?
			}

			transform("xml/" + fileName + ".xml"); // Print to file.
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the root of the tree, in this case the node is called 'game'.
	 */

	@Override
	public Element getRoot() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder(); // Java factories...

		Document doc = builder.newDocument(); // Create actual document.
		Element root = doc.createElement("game");

		this.doc = doc; // Set the root of the tree

		return root; // Return the root.
	}

	/**
	 * Appends a basic node to the tree.
	 * 
	 * @param tagName
	 *            The name of the tag.
	 * @param root
	 *            The node which this element will be appended to.
	 * @return The element that we created.
	 */

	@Override
	public Element appendNode(String tagName, Element root) { // TODO change the name of root?
		Element element = this.doc.createElement(tagName);

		root.appendChild(element);

		return element;
	}

	/**
	 * Appends a node with an attribute to the tree.
	 * 
	 * @param tagName
	 *            The name of the tag.
	 * @param attName
	 *            The name of the attribute.
	 * @param attVal
	 *            The value of the attribute.
	 * @param root
	 *            The node which this element will be appended to.
	 */

	@Override
	public Element appendNode(String tagName, String attName, String attVal, Element root) {
		Element element = this.doc.createElement(tagName);
		element.setAttribute(attName, attVal);

		root.appendChild(element);

		return element;
	}
	
	/**
	 * Outputs the tree to a .xml file.
	 * @param fileName The name of the file we will be outputting to.
	 */

	@Override
	public void transform(String fileName) {

		try {

			TransformerFactory transFactory = TransformerFactory.newInstance();

			Transformer transformer = transFactory.newTransformer();

			DOMSource source = new DOMSource(this.doc);
			StreamResult result = new StreamResult(new File(fileName));

			// result = new StreamResult(System.out); //Print to console.

			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formating options.
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);
		}

		catch (TransformerConfigurationException e) { // TODO Add as throws clauses?
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		}

	}
}
