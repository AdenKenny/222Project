package userHandling;

/**
 * A class representing a registered user.
 *
 * @author Aden
 */

public class User {

	private long id; //Unique identifier of the user.
	private String username; //Username of user.

	private String hash; //Stored hash.

	public User(long id, String username, String hash) {
		this.id = id;
		this.username = username;
		this.hash = hash;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
