package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataStorage.XMLReader;
import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import userHandling.User;

public class Game {

	private final Map<Long, User> connectedUsers;
	private static Map<Integer, Item> mapOfItems;
	private static Map<Integer, CharacterModel> mapOfCharacters;
	private final ArrayList<String> textMessages;

	public Game() {
		this.connectedUsers = new HashMap<>();
		this.textMessages = new ArrayList<>();

		XMLReader reader = XMLReader.getInstance();

		Game.mapOfItems = reader.getItems();
		Game.mapOfCharacters = reader.getCharacters();
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, User user) {
		this.connectedUsers.put(uid, user);
	}

	public void disconnect(long uid) {
		this.connectedUsers.remove(uid);
	}

	public void readInput(long uid, byte[] input) {
		// TODO
	}

	public byte[][] toByteArray(long uid) {
		// get the character of the user
		User user = this.connectedUsers.get(uid);
		// TODO placeholder
		byte[][] data = new byte[0][0];

		return data;
	}

	public void textMessage(long uid, String message) {
		//add the users name to the start of the text message
		message = this.connectedUsers.get(uid).getUsername() + ": " + message;
		textMessages.add(message);
	}

	public String[] getMessages(int messagesReceived) {
		String[] messages = new String[this.textMessages.size() - messagesReceived];
		for (int i = 0; i + messagesReceived < this.textMessages.size(); i++) {
			messages[i] = this.textMessages.get(i + messagesReceived);
		}
		return messages;
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
