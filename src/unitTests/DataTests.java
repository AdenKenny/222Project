package unitTests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import dataStorage.CreateXML;

/**
 * Tests for the XML components of the project.
 *
 * @author Aden
 */

public class DataTests {

	/**
	 * Test to make sure the CreateXML path actually creates a file.
	 */

	@Test
	public void testCreateBasicXML() {
		new CreateXML(null, "createXMLTest");

		String testPath = "tests/createXMLTest.xml"; //Path for the file to be created in.

		File file = new File(testPath);

		Path path = file.toPath(); //The path to the file.

		if(file.isFile()) { //Make sure the file exists.
			try {
				Files.delete(path); //It exists, we can clean up.
			}

			catch (IOException e) {
				System.out.println("testCreateBasicXML failed: The file couldn't be read.");
				fail(); //Problem with file. Test fails.
			}
		}

		else {
			System.out.println("testCreateBasicXML failed: The XML file was not created.");
			fail(); //No file at all. Test fails.
		}

	}

}
