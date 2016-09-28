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

public final class LoadGame {

	private Document doc;

	private Set<Character> setOfCharacters;

	private LoadGame() {

	}

	private void readGame() {
		File file = new File("xml/game.xml");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(file);

			this.doc.getDocumentElement().normalize();

			NodeList list = getNodes("player"); // Get all characters.

			this.setOfCharacters = new HashSet<>();

			for(int i = 0, len = list.getLength(); i < len; ++i) { //NodeList not iterable.
				Node node = list.item(i);

				Element e = (Element) node;
			}
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
	}

	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

}
