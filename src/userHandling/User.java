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
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	@Override
	public String toString() { //This will be what is stored in the database.
		return this.id + " " + this.username + " " + this.hash;
	}

}
