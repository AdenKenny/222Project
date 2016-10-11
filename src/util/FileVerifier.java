package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verifies to make sure all files exist. Will create them if they don't exist. There will still be errors even if it does create the files
 * as there will be no text to read from.
 *
 * @author Aden
 */

public final class FileVerifier {

	private static FileVerifier INSTANCE = null;

	private FileVerifier() {

	}

	public static synchronized FileVerifier getInstance() { // Singleton.
		if (INSTANCE == null) { // Lazy initialisation.
			INSTANCE = new FileVerifier();
		}

		return INSTANCE;
	}

	/**
	 * Checks to make sure all needed files for the game to run exist.
	 */

	public static void checkFiles() {
		String xmlPath = "xml"; // The name of the xml file.

		File xmlFile = new File(xmlPath);

		if (xmlFile.isDirectory()) { // The xml folder exists.
			makeFiles(); // Still check for files in folder.
		}

		else { // Create a folder.

			try {
				xmlFile.mkdir();
			}

			catch (SecurityException e) {
				e.printStackTrace();
			}

			makeFiles(); // Now check for stuff in folder.
		}
	}

	/**
	 * Checks for the needed files inside of the xml file. If they don't exist they will be created.
	 */

	private static void makeFiles() {

		String xmlPath = "xml";
		String xmlPath2 = "xml/";

		File xmlFile = new File(xmlPath);

		String[] fileNames = { "characters.txt", "characters.xml", "game.xml", "items.xml", "items.txt", "objects.txt",
				"objects.xml", "testing.xml", "world.xml" }; // Array of the needed files.

		File[] arrayOfFiles = xmlFile.listFiles(); // Get the files in the existing directory.

		assert (arrayOfFiles != null); //This will be null if the directory doesn't exist.

		List<File> listOfFiles = new ArrayList<>(Arrays.asList(arrayOfFiles)); // Convert to ArrayList.

		if (listOfFiles.size() == 9) { // Check if files exist.
			Logging.logEvent(FileVerifier.class.getName(), Logging.Levels.EVENT, "Files successfully verified.");
			return; //No problem with files. Well the correct number exist.
		}

		Logging.logEvent(FileVerifier.class.getName(), Logging.Levels.WARNING, "Had to recreate files.");
		Logging.logEvent(FileVerifier.class.getName(), Logging.Levels.WARNING, "The game will not work.");

		for(File f : listOfFiles) { //Delete all existing files.

			try {
				f.delete();
			}

			catch (@SuppressWarnings("unused") SecurityException e) {
				Logging.logEvent(FileVerifier.class.getName(), Logging.Levels.SEVERE, "Failed to delete files, Security denied action.");
			}
		}

		for(String name : fileNames) { //Create the required files.
			File newFile = new File(xmlPath2 + "/" + name); //Get the file.

			try {
				Files.createFile(newFile.toPath()); //Create.
			}

			catch (IOException e) {
				e.printStackTrace();
			}

			catch (@SuppressWarnings("unused") SecurityException e) {
				Logging.logEvent(FileVerifier.class.getName(), Logging.Levels.SEVERE, "Failed to create files, Security denied action");
			}
		}
	}
}
