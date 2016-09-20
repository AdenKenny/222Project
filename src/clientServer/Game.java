package clientServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import userHandling.User;

public class Game {

	private final Map<Long, User> connectedUsers;

	public Game() {
		this.connectedUsers = new HashMap<>();
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, User user) {
		this.connectedUsers.put(uid, user);
	}

	public void readInput(long uid, byte[] input) {
		//TODO
	}

	public byte[] toByteArray(long uid) {
		//get the character of the user
		User user = this.connectedUsers.get(uid);
		//TODO placeholder
		byte[] data = new byte[0];
		return data;
	}

	public boolean userOnline(User user) {
		return this.connectedUsers.containsValue(user);
	}
	
	public Set<User> getAllUsers() {
		Collection<User> set = connectedUsers.values();
		return (Set<User>) set;
	}
}