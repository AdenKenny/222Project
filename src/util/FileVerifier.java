package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
		if (INSTANCE == null) {
			INSTANCE = new FileVerifier();
		}

		return INSTANCE;
	}

	public void checkFiles() {
		String xmlPath = "xml";

		File xmlFile = new File(xmlPath);

		Path xmlFolder = xmlFile.toPath();

		if (xmlFile.isDirectory()) { // The xml folder exists.
			makeFiles();
		}

		else { // Create an xmlFile.
			xmlFile.mkdir();
			makeFiles();
		}
	}

	private void makeFiles() {
		String xmlPath = "xml";
		String xmlPath2 = "xml/";

		File xmlFile = new File(xmlPath);

		String[] fileNames = { "characters.txt", "characters.xml", "game.xml", "items.xml", "items.txt", "objects.txt", "objects.xml", "testing.xml",
				"world.xml" };

		File[] listOfFiles = xmlFile.listFiles();

		List<File> arrFiles = new ArrayList<>(Arrays.asList(listOfFiles));

		if (arrFiles.size() > 0) {
			outer:
			for (File file : arrFiles) {
				String string = file.getName();

				for (String s : fileNames) {

					if (string.equals(s)) {
						continue outer;
					}

					File temp = new File(xmlPath2 + "/" + string);

					try {
						Files.createFile(temp.toPath());
					}

					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		else {
			for(String temp : fileNames) {
				File tmpFile = new File(xmlPath2 + "/" + temp);
				
				try {
					Files.createFile(tmpFile.toPath());
				}
				
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		getInstance().checkFiles();
	}
}
