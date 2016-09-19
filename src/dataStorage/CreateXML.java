package dataStorage;

import java.io.File;

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

public class CreateXML {
	
	public CreateXML() {
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.newDocument(); //Create actual document.
			Element root = doc.createElement("game");
			doc.appendChild(root); //Create root of tree
			
			Element player = doc.createElement("player"); //Represents a player
			player.setAttribute("id", new Integer(15).toString()); //Their unique id is 15
			root.appendChild(player); //Append the player to the root.
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer(); //Transformer for writing.
			DOMSource source = new DOMSource(doc); 
			StreamResult result = new StreamResult(new File("xml/testing.xml")); //The file to output to.
			
			//result = new StreamResult(System.out); //Print to console.

			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //Formating options.
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			transformer.transform(source, result); 

		}
		
		catch (ParserConfigurationException e) {
			//TODO Error handling.
		} 
		
		catch (TransformerConfigurationException e) {
			
		} 
		
		catch (TransformerException e) {

		}
	}
}
