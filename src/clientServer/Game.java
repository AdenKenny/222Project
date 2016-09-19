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
		for (User user : allUsers) {
			if (user.getId() == id) {
				connectedUsers.put(uid, user);
			}
		}
	}

	public boolean login(long uid, byte[] input) {
		String user = "";
		String password = "";
		int i = 0;
		byte b;
		while ((b = input[i++]) != 0) {
			user += (char)b;
		}
		while ((b = input[i++]) != 0) {
			password += (char)b;
		}
		System.out.println(user);
		System.out.println(password);
		return false;
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
