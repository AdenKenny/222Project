package clientServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import userHandling.User;

public class Game {

	private final Set<User> allUsers;
	private final Map<Long, User> connectedUsers;

	public Game() {
		this.allUsers = new HashSet<>();
		this.connectedUsers = new HashMap<>();
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, long id) {
		for (User user : this.allUsers) {
			if (user.getId() == id) {
				this.connectedUsers.put(uid, user);
			}
		}
	}

	public byte[] toByteArray(long uid) {
		//get the character of the user
		User user = this.connectedUsers.get(uid);
		//TODO placeholder
		byte[] data = new byte[0];
		return data;
	}
	
	public Set<User> getConnectedUsers() {
		return this.allUsers;
	}
	
	public Set<User> getUsers() {
		return (Set<User>) this.connectedUsers.values();
	}
}
