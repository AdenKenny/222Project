package dataStorage;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

//This can probably be removed, it doesn't really serve a purpose at the moment.

/**
 * An interface representing a way we will interact with a .xml file such as
 * creating, reading or editing one.
 *
 * @author Aden
 */

public interface XMLInterface {

	/**
	 * Returns the root of the XML document. If this is called by the
	 * 'CreateXML' class it will create a new root otherwise it will return the
	 * actual root of the file.
	 *
	 * @return An Element representing the root of the tree.
	 * @throws ParserConfigurationException
	 *             If we cannot create the DocumentBuilderFactory.
	 */

	Element getRoot(String name) throws ParserConfigurationException;

	/**
	 * Appends a basic Element without an attribute to the tree. This method
	 * returns the element that is created as other nodes may need to be
	 * appended to this one.
	 *
	 * @param tagName
	 *            The tag of the element.
	 * @param root
	 *            The node which the element will be appended to.
	 * @return The element that we created.
	 */

	Element appendNode(String tagName, Element root);

	/**
	 * Appends an Element with an attribute to the tree. This method returns the
	 * element that is created as other nodes may need to be appended to this
	 * one.
	 *
	 * @param tagName
	 *            The tag of the element.
	 * @param attName
	 *            The name of the attribute which is added to this node.
	 * @param attVal
	 *            The value of the attribute which is added to this node.
	 * @param root
	 *            The node which the element will be appended to.
	 * @return The element that we created.
	 */

	Element appendNode(String tagName, String attName, String attVal, Element root);

	/**
	 * Outputs the tree structure to a .xml file.
	 *
	 * @param fileName
	 *            The name of the file we want to output to.
	 */

	void transform(String fileName);

}
