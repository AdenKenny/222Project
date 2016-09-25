package clientServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import userHandling.User;

public class Game {

	private final Map<Long, User> connectedUsers;
	private static Map<Integer, Item> mapOfItems;
	private static Map<Integer, CharacterModel> mapOfCharacters;

	public Game() {
		this.connectedUsers = new HashMap<>();
		
		/*XMLReader reader = XMLReader.getInstance();
		
		Game.mapOfItems = reader.getItems();
		Game.mapOfCharacters = reader.getCharacters();
		
		for(Entry<Integer, CharacterModel> e : mapOfCharacters.entrySet()) {
			System.out.println(e.getValue().getName());
		}*/
		
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, User user) {
		this.connectedUsers.put(uid, user);
	}

	public void readInput(long uid, byte[] input) {
		// TODO
	}

	public byte[] toByteArray(long uid) {
		// get the character of the user
		User user = this.connectedUsers.get(uid);
		// TODO placeholder
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