package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import dataStorage.LoadGame;
import dataStorage.XMLReader;
import gameWorld.characters.CharacterModel;
import gameWorld.characters.Character;
import gameWorld.item.Item;
import util.XMLWriter;

/**
 * Tests for the XML components of the project.
 *
 * @author Aden
 */

public class DataTests {

	/**
	 * This isn't actually a test. It just sets the logging file to a test one
	 * through reflection rather than using the actual logging file.
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

	/*	*//**
			 * Test to make sure the CreateXML path actually creates a file.
			 *//*
			 *
			 * @Test public void testCreateBasicXML() {
			 *
			 * new CreateXML(null, "createXMLTest");
			 *
			 * String testPath = "tests/createXMLTest.xml"; // Path for the file
			 * to be created in.
			 *
			 * File file = new File(testPath);
			 *
			 * Path path = file.toPath(); // The path to the file.
			 *
			 * if (file.isFile()) { // Make sure the file exists. try {
			 * Files.delete(path); // It exists, we can clean up. }
			 *
			 * catch (IOException e) { System.out.
			 * println("testCreateBasicXML failed: The file couldn't be read.");
			 * fail(); // Problem with file. Test fails. } }
			 *
			 * else { System.out.
			 * println("testCreateBasicXML failed: The XML file was not created."
			 * ); fail(); // No file at all. Test fails. }
			 *
			 * }
			 */

	@Test
	public void testWriteXML() {
		new XMLWriter("testing", "items", "testing2", "characters");

		String testPath = "xml/testing.xml";

		File file = new File(testPath);

		Path path = file.toPath();

		if (file.isFile()) {
			try {
				Files.delete(path);
			}

			catch (IOException e) {
				fail();
			}
		}

		else {
			fail();
		}
	}

	@Test
	public void testPlayersLoad() {

		Set<Character> set = LoadGame.getInstance().getPlayers();

		assert (set.size() > 0);
	}

	@Test
	public void testReadXML() {
		new XMLWriter("testing", "items", "testing2", "chars");

		// String testPath = "xml/testing.xml";

		XMLReader reader = XMLReader.getInstance();
		Map<Integer, Item> map = reader.getItems();
		Map<Integer, CharacterModel> map2 = reader.getCharacters();

		assertNotSame(map.size(), 0);
		assertNotSame(map2.size(), 0);

		Item item = map.get(31);
		assertNotSame(item, null);

		assertEquals(item, item.clone());

		item = map.get(5000);
		assertSame(item, null);

		CharacterModel character = map2.get(1000);

		assertNotSame(character, null);

		character = map2.get(12000);

		assertSame(character, null);
	}

}
