package userHandling;

/**
 * A class representing a registered user.
 *
 * @author Aden
 */

public final class User {

	private final long id; //Unique identifier of the user.
	private final String username; //Username of user.

	private String hash; //Stored hash.

	/**
	 * Creates a user with the passed details.
	 *
	 * @param id The unique identifier of the user.
	 * @param username The username of the user.
	 * @param hash The generated hash for the user's password.
	 */

	public User(long id, String username, String hash) {
		this.id = id;
		this.username = username;
		this.hash = hash;
	}

	/**
	 * Returns the ID of the user.
	 *
	 * @return The unique identifier of the user.
	 */

	public long getId() {
		return this.id;
	}

	/**
	 * Returns the username of the user.
	 *
	 * @return The user's username.
	 */

	public String getUsername() {
		return this.username;
	}

	/**
	 * Returns the hash of the user that was generated when the user was registered.
	 *
	 * @return The hash of the user. To check versus the entered password.
	 */

	public String getHash() {
		return this.hash;
	}

	/**
	 * Sets the hash for the user. Should be used when the user's password is changed.
	 *
	 * @param hash The new hash. Should be generated with the 'Hashing' class in the 'userHandling' package.
	 */

	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Returns a string representation of this user. The string is the user's ID, their
	 * username, and the hash. This is what is stored in the database.
	 */

	public String dbString() {

		return this.id + " " + this.username + " " + this.hash;
	}

	/**
	 * Returns a string representation of the user, although this method doesn't include
	 * the hash of the user.
	 */

	@Override
	public String toString() {

		return this.id + " " + this.username;
	}

	/**
	 * Returns the hashCode of the item.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.hash == null) ? 0 : this.hash.hashCode());
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (this.hash == null) {
			if (other.hash != null) {
				return false;
			}
		} else if (!this.hash.equals(other.hash)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		if (this.username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!this.username.equals(other.username)) {
			return false;
		}
		return true;
	}



}
