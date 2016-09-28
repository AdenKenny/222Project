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

public class ServerSideGame {

	private final Map<Long, User> connectedUsers;
	private static Map<Integer, Item> mapOfItems;
	private static Map<Integer, CharacterModel> mapOfCharacters;
	private final ArrayList<String> textMessages;

	public ServerSideGame() {
		this.connectedUsers = new HashMap<>();
		this.textMessages = new ArrayList<>();

		XMLReader reader = XMLReader.getInstance();

		ServerSideGame.mapOfItems = reader.getItems();
		ServerSideGame.mapOfCharacters = reader.getCharacters();
	}

	public synchronized void tick() {

	}

	/**
	 * associate the id of a Master with the user logged in on that connection
	 * @param uid
	 * @param user
	 */
	public void registerConnection(long uid, User user) {
		this.connectedUsers.put(uid, user);
	}

	/**
	 * a connection has stopped, so remove the user from connected users
	 * @param uid
	 */
	public void disconnect(long uid) {
		this.connectedUsers.remove(uid);
	}

	/**
	 * parse actions made by a player
	 * @param uid
	 * @param input
	 */
	public void readInput(long uid, byte input) {
		// TODO
	}

	/**
	 * get a game state to send to the player
	 * @param uid
	 * @return
	 */
	public byte[][] toByteArray(long uid) {
		// get the character of the user
		User user = this.connectedUsers.get(uid);
		//TODO check if returning room enter or room update
		//TODO get room of player
		/*Set<Entity> entities = room.getEntities();
		 *byte[][] data = new byte[entities.size() + 1][];
		 *
		 *data[0] = new byte[5];
		 *data[0][0] = PackageCode.Codes.GAME_POSITION_UPDATE.value;
		 *data[0][1] = player room x
		 *data[0][1] = player room y
		 *data[0][1] = player tile x
		 *data[0][1] = player tile y
		 *
		 *int i = 1;
		 */
		if (true /*newlyEntered*/) {
			/*
			 *for (Entity e : entities) {
			 *		data[i++] = e.onEntry();
			 *}
			 */
		}
		else {
			/*for (Entity e : entities) {
			 *		data[i++] = e.roomUpdate();
			 *}
			 */
		}
		//return data;
		return null; //TODO
	}

	/**
	 * add a message sent by a user to the list of sent messages
	 * @param uid
	 * @param message
	 */
	public void textMessage(long uid, String message) {
		//add the users name to the start of the text message
		message = this.connectedUsers.get(uid).getUsername() + ": " + message;
		textMessages.add(message);
	}

	/**
	 * send all unreceived messages to a user
	 * @param messagesReceived
	 * @return
	 */
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
