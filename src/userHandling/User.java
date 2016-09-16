package userHandling;

/**
 * A class representing a registered user.
 *
 * @author Aden
 *
 */

public class User {

	private int id; //Unique identifier of the user.
	private String username; //Username of user.

	private String hash; //Stored hash.

	public User(String username, String hash) {
		//this.id = id;
		this.username = username;
		this.hash = hash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
