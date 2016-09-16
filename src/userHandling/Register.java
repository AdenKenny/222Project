package userHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A class which handles registering a user and stores them in the database of
 * users.
 *
 * @author Aden
 */

public class Register {

	private Register() {

	}

	public static User createUser(String username, String password) {

		Scanner scan = null;
		long currentID = 0;
		boolean userExists = false;

		try {
			scan = new Scanner(new File("database/db.txt"));

			while (scan.hasNextLine()) {

				currentID = Long.parseLong(scan.nextLine());

				String line = scan.nextLine();

				String[] arr = line.split(" ");

				if (arr[1].equals(username)) { // Username is already in use.
					userExists = true;
					throw new RegistrationException();
				}

				currentID = Long.parseLong(arr[0]); // Get id of last user.
			}

			String hash = Hashing.createHash(password.toCharArray()); //Create hash.


			password = null; // Let password be garbage collected.

			User user = new User(currentID++, username, hash);

			return user;

		}

		catch (FileNotFoundException e) {
			throw new Error("Error with database"); //Database not found.
		}

		catch (RegistrationException e) { //TODO Add this functionality.
			throw new Error(""); //Change this.
		}

		finally {
			if (scan != null) {
				scan.close();
			}
		}

	}


}

class RegistrationException extends Exception {
	public RegistrationException() {
		super("This username is taken");
	}
}


