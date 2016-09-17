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

	public User(long id, String username, String hash) {
		this.id = id;
		this.username = username;
		this.hash = hash;
	}

	public long getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
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
