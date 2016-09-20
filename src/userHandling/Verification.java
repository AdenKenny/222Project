package userHandling;

/**
 * A class dealing with a verifying a user's login details.
 *
 * @author Aden
 */

public final class Verification {

	private Verification() { //Shouldn't be initialised.
		throw new AssertionError();
	}

	/**
	 * Checks to see if a user has successfully logged in. Takes the user's login
	 * details, i.e. their username and password and checks the database to see if the
	 * user exists. If the user exists the entered password is then hashed and compared
	 * against the hash stored in the database for the user matching the username that
	 * the login attempt entered.
	 * 
	 * If the user account does not exist or the hash generated from the inputed
	 * password does not match the hash stored in the database this method will
	 * return null rather than the logged in user.
	 * 
	 * @param username The username of the account the user wants to access.
	 * @param password The password of the account the user wants to access.
	 * @return The user account which the user logged into.
	 */
	
	public static User login(String username, String password) {

		if(Register.userExists(username)) { //Check to see if user exits.
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
