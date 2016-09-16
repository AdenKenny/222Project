package userHandling;

public class Register {

	private Register() {

	}

	public static User createUser(String username, String password) {
		String hash = Hashing.createHash(password.toCharArray());
		password = null;

		return new User(username, hash);
	}

}
