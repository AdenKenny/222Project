package userHandling;

/**
 * A class which handles registering a user and stores them in the database of users.
 *
 * @author Aden
 */

public class Register {

	private Register() {

	}

	public static User createUser(String username, String password) {
		String hash = Hashing.createHash(password.toCharArray());
		password = null; //Let password be garbage collected.

		return new User(username, hash);
	}

}
