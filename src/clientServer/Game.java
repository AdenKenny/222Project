package clientServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
	
	/**
	 * Returns a set of all the connected users in the game.
	 * 
	 * @return A hash set of all connected users to the game.
	 */
	
	public Set<User> getAllUsers() {
		
		Set<User> set = new HashSet<>();
		
		for(Entry<Long, User> entry : this.connectedUsers.entrySet()) {
			set.add(entry.getValue()); //Add the value of the key value pair.
		}
		
		return set;
	}
}