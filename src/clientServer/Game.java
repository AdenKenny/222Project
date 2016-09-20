package clientServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import userHandling.User;
import userHandling.Verification;

public class Game {

	private final Set<User> allUsers;
	private final Map<Long, User> connectedUsers;

	public Game() {
		this.allUsers = new HashSet<>();
		//TODO load users from database and add to allUsers
		this.connectedUsers = new HashMap<>();
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, User user) {
		connectedUsers.put(uid, user);
	}

	public void addUser(User user) {
		allUsers.add(user);
	}

	public void readInput(long uid, byte[] input) {
		//TODO
	}

	public byte[] toByteArray(long uid) {
		//get the character of the user
		User user = connectedUsers.get(uid);
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
