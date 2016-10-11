package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verifies to make sure all files exist. Will create them if they don't exist.
 * There will still be errors even if it does create the files as there will be
 * no text to read from.
 *
 * @author Aden
 */

public final class FileVerifier {

	private static FileVerifier INSTANCE = null;

	private FileVerifier() {

	}

	public static synchronized FileVerifier getInstance() { // Singleton.
		if (INSTANCE == null) { //Lazy initialisation.
			INSTANCE = new FileVerifier();
		}

		return INSTANCE;
	}

	/**
	 * Checks to make sure all needed files for the game to run exist.
	 */

	public void checkFiles() {
		String xmlPath = "xml"; //The name of the xml file.

		File xmlFile = new File(xmlPath);

		if (xmlFile.isDirectory()) { // The xml folder exists.
			makeFiles(); //Still check for files in folder.
		}

		else { // Create a folder.

			try {
				assert(xmlFile.mkdir());
			}

			catch(SecurityException e) {
				e.printStackTrace();
			}

			makeFiles(); //Now check for stuff in folder.
		}
	}

	/**
	 * Checks for the needed files inside of the xml file. If they don't exist they will be created.
	 */

	private void makeFiles() {
		String xmlPath = "xml";
		String xmlPath2 = "xml/";

		File xmlFile = new File(xmlPath);

		String[] fileNames = { "characters.txt", "characters.xml", "game.xml", "items.xml", "items.txt", "objects.txt", "objects.xml", "testing.xml",
				"world.xml" }; //Array of the needed files.

		File[] listOfFiles = xmlFile.listFiles(); //Get the files in the existing directory.

		List<File> arrFiles = new ArrayList<>(Arrays.asList(listOfFiles)); //Convert to ArrayList.

		if (arrFiles.size() > 0) { //See if any files already exist.
			outer:
			for (File file : arrFiles) {
				String string = file.getName();

				for (String s : fileNames) {

					if (string.equals(s)) {
						continue outer; //We don't need to make a new file.
					}

					File temp = new File(xmlPath2 + "/" + string); //The path of the needed path.

					try {
						Files.createFile(temp.toPath()); //Create the new file we need.
					}

					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		else {
			for(String temp : fileNames) { //No files exist.
				File tmpFile = new File(xmlPath2 + "/" + temp);

				try {
					Files.createFile(tmpFile.toPath()); //Create the file.
				}

				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
