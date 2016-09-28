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

/**
 * A class to load entities from XML.
 *
 * @author Aden
 */

public final class LoadGame {

	private Document doc; //Document we will do tree operations.

	private Set<Character> setOfCharacters; //Set of loaded in characters.

	public LoadGame() {
		this.setOfCharacters = readPlayers();
		readRooms();
	}

	/**
	 * Gets a set of Characters from the XML file that was specified when the LoadGame
	 * class was initialised.
	 *
	 * @return A Set<Character> of characters that were loaded from file.
	 */

	public Set<Character> getPlayers() {
		return this.setOfCharacters;
	}

	/**
	 * Reads the rooms from file to create floors that the game is played on.
	 */

	private synchronized void readRooms() {
		File file = new File("xml/world.xml"); //We will read from world.xml

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder(); //Builders.
			this.doc = docBuilder.parse(file); //Parse the file to a document.

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("floor"); //Get a list of the floors.

			for (int i = 0, len = list.getLength(); i < len; ++i) { //Iterate through elements of the floor.

				Node node = list.item(i); //Get the node at position.

				Element e = (Element) node; //Safe cast.

				String level = e.getElementsByTagName("level").item(0).getTextContent(); //Get the level of the floor.

				String width = e.getElementsByTagName("width").item(0).getTextContent(); //The width.

				String depth = e.getElementsByTagName("depth").item(0).getTextContent(); //The depth.

				Floor floor = new Floor(level, width, depth); //Create a new floor with the values above.

				ServerSideGame.world.addFloor(floor); //Add the floor to the world in server.

				NodeList children = getNodes("room"); //Get a list of the rooms in a floor.

				for (int j = 0, length = children.getLength(); j < length; ++j) {

					if (children.item(j).getNodeType() == Node.ELEMENT_NODE) { //Check cast.
						Element child = (Element) children.item(j); //The cast is safe.

						RoomBuilder build = new RoomBuilder(floor); //New room builder.

						String playerSpawn = child.getElementsByTagName("playerSpawn").item(0).getTextContent();
						build.setBuildPlayerSpawn(playerSpawn); //Get values from tag and add values to builder.

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

						Room room = build.build(); //Build the builder, returning a room.

						floor.addRoom(room, room.xPos(), room.yPos()); //Add the newly built room the floor.
					}

				}

				floor.setupNeighbours(); //Work out the neighboring rooms for all the rooms in the floor.
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

	/**
	 * Returns a Set<Character> from player entries in XML. These players are built
	 * with a PlayerBuilder and values read from xml.
	 *
	 * @return A Set<Character> representing players that were loaded from XML.
	 */

	private synchronized Set<Character> readPlayers() {
		File file = new File("xml/game.xml"); //Once again, the file we'll read from.

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			this.doc = docBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("player"); //We're doing players this time.

			Set<Character> set = new HashSet<>(); //Create a new set.

			for (int i = 0, len = list.getLength(); i < len; ++i) {

				Node node = list.item(i);

				Element e = (Element) node;

				PlayerBuilder build = new PlayerBuilder(); //Player builder this time.

				String username = e.getElementsByTagName("username").item(0).getTextContent();
				build.setName(username); //Get values and pass them to the builder.

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

				Character character = build.build(); //Build it to create a Character.

				set.add(character); //Add this newly created player to the set of characters.
			}

			return set; //Return our newly populated (if we loaded any players) set.
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

	/**
	 * Gets all the nodes by the tag name.
	 *
	 * @param tagName The name of the nodes you want to get.
	 * @return A NodeList representing all the nodes that tag names match.
	 */

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

	public static void main(String[] args) {
		new LoadGame();
	}

}
