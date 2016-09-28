package dataStorage;

import java.io.File;
import java.util.ArrayList;
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
import util.Logging;

public final class SaveGame {

	private Document doc;
	private File file;

	private Element root;

	private Element players;
	private Element monsters;
	private Element vendors;

	private SaveGame() {
		this.file = new File("xml/game.xml");

		try {
			this.root = getRoot("game");
			this.doc.appendChild(this.root); // Append root to tree.

			this.players = this.doc.createElement("players");
			this.root.appendChild(this.players);

			this.monsters = this.doc.createElement("monsters");
			this.root.appendChild(this.monsters);

			this.vendors = this.doc.createElement("vendors");
			this.root.appendChild(this.vendors);

		}

		catch (ParserConfigurationException e) {
			Logging.logEvent(SaveGame.class.getName(), Logging.Levels.SEVERE, "Could not save game");
		}

		Character player = new Character("Aden");

		List<Integer> temp = new ArrayList<>();

		temp.add(1);
		temp.add(2);
		temp.add(51);

		player.setItems(temp);

		savePlayer(player);

		transform("xml/game.xml");
	}

	public void savePlayer(Character player) {

		Element playerTag = this.doc.createElement("player");

		playerTag.appendChild(createNode("username", player.name()));
		playerTag.appendChild(createNode("UID", player.ID() + ""));
		playerTag.appendChild(createNode("type", player.getType().toString()));
		playerTag.appendChild(createNode("items", convertToString(player.getItems())));
		//playerTag.appendChild(createNode("room", player.getRoomID()+ ""));

		playerTag.appendChild(createNode("health", player.getHealth() + ""));
		playerTag.appendChild(createNode("xp", player.getXp() + ""));
		playerTag.appendChild(createNode("gold", player.getGold() + ""));

		playerTag.appendChild(createNode("level", player.getLevel() + ""));
		playerTag.appendChild(createNode("equips", itemToString(player.getEquips())));

		this.players.appendChild(playerTag);
	}

	public void saveMonster(Character monster) {
		Element monsterTag = this.doc.createElement("monster");

		monsterTag.appendChild(createNode("UID", monster.ID() + ""));
		monsterTag.appendChild(createNode("type", monster.getType().toString()));
		monsterTag.appendChild(createNode("items", convertToString(monster.getItems())));
		//monsterTag.appendChild(createNode("room", monster.getRoomID()+ ""));

		monsterTag.appendChild(createNode("rank", monster.getRank() + ""));
		monsterTag.appendChild(createNode("modelID", monster.getModelID() + ""));

		monsterTag.appendChild(createNode("health", monster.getHealth() + ""));
		monsterTag.appendChild(createNode("xp", monster.getXp() + ""));
		monsterTag.appendChild(createNode("gold", monster.getGold() + ""));


		this.monsters.appendChild(monsterTag);
	}

	public void saveVendor(Character vendor) {
		Element vendorTag = this.doc.createElement("vendor");

		vendorTag.appendChild(createNode("UID", vendor.ID() + ""));
		vendorTag.appendChild(createNode("type", vendor.getType().toString()));
		vendorTag.appendChild(createNode("items", convertToString(vendor.getItems())));
		//vendorTag.appendChild(createNode("room", vendor.getRoomID()+ ""));

		this.vendors.appendChild(vendorTag);
	}

	private static String convertToString(List<Integer> list) {

		StringBuilder builder = new StringBuilder();

		for(Integer i : list) {
			builder.append(i);
			builder.append(", ");
		}

		String buildOut = builder.toString();

		return buildOut.substring(0, buildOut.length() - 2);

	}

	private static String itemToString(List<Item> list) {
		StringBuilder builder = new StringBuilder();

		for(Item i : list) {
			builder.append(i.getID());
			builder.append(", ");
		}

		String buildOut = builder.toString();

		if(buildOut.length() > 0) {
			return buildOut.substring(0, buildOut.length() - 2);
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
