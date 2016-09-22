package unitTests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
	 * This isn't actually a test. It just sets the logging file to a test one through reflection
	 * rather than using the actual logging file.
	 */

	@Test
	public void setLoggingFile() {
		try {

			Class<?> loggingClass = Class.forName("util.Logging");

			Field field = loggingClass.getDeclaredField("LOG_FILE");

			field.setAccessible(true); // Make private field accessible.

			Field modifiers = field.getClass().getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			field.set(loggingClass, new File("tests/log.log"));

		}

		catch (ClassNotFoundException e1) {
			fail();
		}

		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test to make sure the CreateXML path actually creates a file.
	 */

	@Test
	public void testCreateBasicXML() {

		new CreateXML(null, "createXMLTest");

		String testPath = "tests/createXMLTest.xml"; // Path for the file to be created in.

		File file = new File(testPath);

		Path path = file.toPath(); // The path to the file.

		if (file.isFile()) { // Make sure the file exists.
			try {
				Files.delete(path); // It exists, we can clean up.
			}

			catch (IOException e) {
				System.out.println("testCreateBasicXML failed: The file couldn't be read.");
				fail(); // Problem with file. Test fails.
			}
		}

		else {
			System.out.println("testCreateBasicXML failed: The XML file was not created.");
			fail(); // No file at all. Test fails.
		}

	}

}