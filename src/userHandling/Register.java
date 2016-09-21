package userHandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A class which handles registering a user and stores them in the database of users.
 *
 * @author Aden
 */

public final class Register {
	private static final File DB_FILE = new File("database/db.txt"); //The file representing the database.

	/**
	 * Enums representing the position of elements in the string that is read out of
	 * the database.
	 */

	private enum Position {

		ID_POSITION(0),
		USER_POSITION(1),
		HASH_POSITION(2);

		Position(int value) {
			this.value = value;
		}

		final int value;
	}

	private static long currentID = 0; //The max ID currently assigned.

	private Register() { // This shouldn't be initialised.
		throw new AssertionError();
	}

	/**
	 * Creates a User object when given a username and password in the form of two strings. Will
	 * create the User object then add the user ID, username, and hash in the database. The User
	 * object will be null if the username already exists in the database.
	 *
	 * @param username
	 *            The requested username.
	 * @param password
	 *            The requested password.
	 * @return The user created from the details. Will have a unique ID and username and hash.
	 *         Returns null if username is taken.
	 */

	public static User createUser(String username, String password) {

		try (FileWriter fileWriter = new FileWriter(DB_FILE, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

			if (userExists(username)) { // Username is already taken.
				throw new RegistrationException();
			}

			String hash = Hashing.createHash(password.toCharArray()); // Create hash with password.

			password = null; // Let the password be garbage collected.

			User user = new User(++currentID, username, hash); // Create user with details including
																// incremented ID.
			printWriter.println(user.dbString());

			return user;
		}

		catch (IOException e) {
			return null;
		}

		catch (RegistrationException e) {
			System.out.println("A user already exists with this name"); //TODO Proper errors here.
			return null;
		}
	}

	/**
	 * Checks the database to make sure their is not already a user registered with this username.
	 * Duplicate usernames are not acceptable.
	 *
	 * @param username
	 *            The username to check.
	 * @return A boolean based on the existence of the username.
	 */

	public static boolean userExists(String username) {

		Scanner scan = null; // Scanner to read file.
		try {
			scan = new Scanner(DB_FILE);

			while (scan.hasNextLine()) { // While there is still data to read.

				String line = scan.nextLine(); // Read data.

				String[] arr = line.split(" "); // Split all

				if (arr[Position.USER_POSITION.value].equals(username)) { // Username is already in use.
					return true;
				}

				currentID = Long.parseLong(arr[Position.ID_POSITION.value]); // Get id of last user.
			}

		}

		catch (FileNotFoundException e) {
			throw new Error("Error with database"); // Database not found.
		}

		finally {
			if (scan != null) {
				scan.close();
			}
		}

		return false;
	}

	/**
	 * Gets a user and their details if they exist in the database. Returns null if the user
	 * doesn't exist.
	 *
	 * @param username The username of the user we're trying to get.
	 * @return A User object with the username that was passed to this method.
	 */

	static User getUser(String username) {

		Scanner scan = null; // Scanner to read file.
		try {
			scan = new Scanner(DB_FILE);

			while (scan.hasNextLine()) { // While there is still data to read.

				String line = scan.nextLine(); // Read data.

				String[] arr = line.split(" "); // Split all

				if (arr[Position.USER_POSITION.value].equals(username)) { // Username is already in use.

					try {
						long id = Long.parseLong(arr[Position.ID_POSITION.value]);

						return new User(id, username, arr[Position.HASH_POSITION.value]);
					}

					catch (NumberFormatException e) {
						throw new FileNotFoundException(); // Problem with database.
					}

				}

				currentID = Long.parseLong(arr[Position.ID_POSITION.value]); // Get id of last user.
			}

		}

		catch (FileNotFoundException e) {
			throw new Error("Error with database"); // Database not found.
		}

		finally {
			if (scan != null) { //Make sure scan isn't null.
				scan.close();
			}
		}

		return null;
	}

	public boolean removeUser(String username) {
				//TODO

		return true;
	}

	public static void main(String[] args) {
		/*createUser("Mark", "Testing").dbString();
		createUser("Tim", "Testing").dbString();

		System.out.println(Verification.login("Mark", "Testing"));*/

	}

}

class RegistrationException extends Exception {
	public RegistrationException() {
		super("This username is taken");
	}
}
