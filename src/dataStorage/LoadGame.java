package dataStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import clientServer.ServerSideGame;
import gameWorld.Floor;
import gameWorld.Room;
import gameWorld.characters.Character;
import gameWorld.characters.PlayerBuilder;
import gameWorld.rooms.RoomBuilder;

public final class LoadGame {

	private Document doc;

	private Set<Character> setOfCharacters;

	public LoadGame() {
		this.setOfCharacters = readPlayers();
		readRooms();
	}

	public Set<Character> getPlayers() {
		return this.setOfCharacters;
	}

	private synchronized void readRooms() {
		File file = new File("xml/world.xml");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			this.doc = docBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("floor");

			for (int i = 0, len = list.getLength(); i < len; ++i) {

				Node node = list.item(i);

				Element e = (Element) node;

				String level = e.getElementsByTagName("level").item(0).getTextContent();

				String width = e.getElementsByTagName("width").item(0).getTextContent();

				String depth = e.getElementsByTagName("depth").item(0).getTextContent();

				Floor floor = new Floor(level, width, depth);

				ServerSideGame.world.addFloor(floor);

				NodeList c = node.getChildNodes();

				NodeList children = getNodes("room");

				for (int j = 0, length = children.getLength(); j < length; ++j) {




					if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
						Element child = (Element) children.item(j);

						RoomBuilder build = new RoomBuilder(floor);

						String playerSpawn = child.getElementsByTagName("playerSpawn").item(0).getTextContent();
						build.setBuildPlayerSpawn(playerSpawn);

						String npcSpawn = child.getElementsByTagName("npcSpawn").item(0).getTextContent();
						build.setBuildNpcSpawn(npcSpawn);

						String xPos = child.getElementsByTagName("xPos").item(0).getTextContent();
						build.setBuildXPos(xPos);

						String yPos = child.getElementsByTagName("yPos").item(0).getTextContent();
						build.setBuildYPos(yPos);

						String modelID = child.getElementsByTagName("modelID").item(0).getTextContent();
						build.setBuildModelID(modelID);

						String roomWidth = child.getElementsByTagName("width").item(0).getTextContent();
						build.setBuildWidth(roomWidth);

						String roomDepth = child.getElementsByTagName("depth").item(0).getTextContent();
						build.setBuildDepth(roomDepth);

						String roomLevel = child.getElementsByTagName("level").item(0).getTextContent();
						build.setLevel(roomLevel);

						Room room = build.build();

						floor.addRoom(room, room.xPos(), room.yPos());
					}

				}

				floor.setupNeighbours();
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private synchronized Set<Character> readPlayers() {
		File file = new File("xml/game.xml");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			this.doc = docBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("player"); // Get all characters.

			Set<Character> set = new HashSet<>();

			for (int i = 0, len = list.getLength(); i < len; ++i) { // NodeList
																	// not
																	// iterable.
				Node node = list.item(i);

				Element e = (Element) node;

				PlayerBuilder build = new PlayerBuilder();

				String username = e.getElementsByTagName("username").item(0).getTextContent();
				build.setName(username);

				String UID = e.getElementsByTagName("UID").item(0).getTextContent();
				build.setID(UID);

				String type = e.getElementsByTagName("type").item(0).getTextContent();
				build.setType(type);

				String items = e.getElementsByTagName("items").item(0).getTextContent();
				build.setItems(items);

				String health = e.getElementsByTagName("health").item(0).getTextContent();
				build.setHealth(health);

				String xp = e.getElementsByTagName("xp").item(0).getTextContent();
				build.setXp(xp);

				String gold = e.getElementsByTagName("gold").item(0).getTextContent();
				build.setGold(gold);

				String level = e.getElementsByTagName("level").item(0).getTextContent();
				build.setValue(level);

				String equips = e.getElementsByTagName("equips").item(0).getTextContent();
				build.setEquips(equips);

				Character character = build.build();

				set.add(character);
			}

			return set;
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return new HashSet<>(); // Empty set as failed.
	}

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

	public static void main(String[] args) {
		new LoadGame();
	}

}
