package userHandling;

/**
 * A class dealing with a verifying a user's login details.
 *
 * @author Aden
 */

public final class Verification {

	private Verification() {
		throw new AssertionError();
	}

	public static User login(String username, String password) {

		if(Register.userExists(username)) {
			User user = Register.getUser(username);

			if(Hashing.verifyPassword(password, user.getHash())) { //Correct password.
				password = null;
				return user;
			}

			password = null;
		}

		return null;
	}


}
