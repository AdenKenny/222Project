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

import gameWorld.characters.Character;
import gameWorld.characters.PlayerBuilder;

public final class LoadGame {

	private Document doc;

	private Set<Character> setOfCharacters;

	public LoadGame() {
		this.setOfCharacters = readGame();



		for(Character c : setOfCharacters) {
			System.out.println(c.getName());
		}
	}

	private Set<Character> readGame() {
		File file = new File("xml/game.xml");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			this.doc = docBuilder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("player"); // Get all characters.

			Set<Character> set = new HashSet<>();

			for(int i = 0, len = list.getLength(); i < len; ++i) { //NodeList not iterable.
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

		catch(IOException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return new HashSet<>(); //Empty set as failed.
	}

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

	public static void main(String[] args) {
		new LoadGame();
	}

}
