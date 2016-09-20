package dataStorage;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class ReadXML implements XMLInterface {

	Document doc;
	
	public ReadXML(String fileName) {
				
		File file = new File("xml/" + fileName + ".xml");

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(file);

			this.doc.getDocumentElement().normalize();

			System.out.println(this.doc.getDocumentElement().getNodeName());

			NodeList list = getNodes("Players"); //Node list is not iterable.
			
			
			for(int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				
				System.out.println(node.getNodeName());
			}
			
		}

		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		catch (SAXException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private NodeList getNodes(String tagName) {
		return this.doc.getElementsByTagName(tagName);
	}

	@Override
	public Element getRoot() throws ParserConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element appendNode(String tagName, Element root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element appendNode(String tagName, String attName, String attVal, Element root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transform(String fileName) {
		// TODO Auto-generated method stub

	}

}
