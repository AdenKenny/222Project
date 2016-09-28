package dataStorage;

import java.io.File;
import java.util.List;

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

import gameWorld.characters.Character;
import gameWorld.item.Item;

public final class SaveGame {

	private Document doc;
	private File file;

	private Element players;
	private Element monsters;

	private SaveGame() {
		this.file = new File("xml/game.xml");

		//savePlayer();

	}

	public void savePlayer(Character player) {
		try {
			Element root = getRoot("game");

			this.doc.appendChild(root); // Append root to tree.

			Element playerTag = this.doc.createElement("player");

			playerTag.appendChild(createNode("UID", player.ID() + ""));
			playerTag.appendChild(createNode("type", player.getType().toString()));
			playerTag.appendChild(createNode("items", convertToString(player.getItems())));

			playerTag.appendChild(createNode("health", player.getHealth() + ""));
			playerTag.appendChild(createNode("xp", player.getXp() + ""));
			playerTag.appendChild(createNode("gold", player.getGold() + ""));

			playerTag.appendChild(createNode("level", player.getLevel() + ""));
			playerTag.appendChild(createNode("equips", itemToString(player.getEquips())));

			root.appendChild(playerTag);

			transform("xml/game.xml");
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void saveMonster() {

	}

	private String convertToString(List<Integer> list) {

		StringBuilder builder = new StringBuilder();

		for(Integer i : list) {
			builder.append(i);
		}

		return builder.toString();
	}

	private String itemToString(List<Item> list) {
		StringBuilder builder = new StringBuilder();

		for(Item i : list) {
			builder.append(i.getID());
		}

		return builder.toString();
	}

	private Element createNode(String nodeName, String nodeValue) {
		Element element = this.doc.createElement(nodeName);
		element.appendChild(this.doc.createTextNode(nodeValue));

		return element;
	}

	private Element getRoot(String fileName) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.newDocument(); // Create actual document.
		Element root = doc.createElement(fileName); // The name of the node.

		this.doc = doc; // Set the root of the tree

		return root; // Return the root.
	}

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
		new SaveGame();
	}

}
