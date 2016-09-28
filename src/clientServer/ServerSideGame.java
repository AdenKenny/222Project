package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataStorage.XMLReader;
import gameWorld.World;
import gameWorld.characters.Character;
import gameWorld.characters.CharacterModel;
import gameWorld.item.Item;
import userHandling.User;

public class ServerSideGame implements Game {

	private static Map<String, Character> players = new HashMap<String, Character>();
	private static XMLReader reader = XMLReader.getInstance();
	public final static Map<Integer, Item> mapOfItems = reader.getItems();
	public final static Map<Integer, CharacterModel> mapOfCharacters = reader.getCharacters();

	//private static World world =

	private final Map<Long, User> connectedUsers;
	private final ArrayList<String> textMessages;

	public ServerSideGame() {
		this.connectedUsers = new HashMap<>();
		this.textMessages = new ArrayList<>();

		/*	TODO: remove if the Game still works
		XMLReader reader = XMLReader.getInstance();
		ServerSideGame.mapOfItems = reader.getItems();
		ServerSideGame.mapOfCharacters = reader.getCharacters();
		*/
	}

	public synchronized void tick() {

	}

	/**
	 * associate the id of a Master with the user logged in on that connection,
	 * and associate the name of the user with their character
	 *
	 * @param uid
	 * @param user
	 */
	public void registerConnection(long uid, User user) {
		this.connectedUsers.put(uid, user);
		players.put(user.getUsername(), new Character(user.getUsername()));
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
		/*Set<Sendable> sendables = room.getSendables();
		 *int extra = newlyEntered ? 2 : 1;
		 *byte[][] data = new byte[sendables.size() + extra][];
		 *
		 *data[0] = new byte[3];
		 *data[0][0] = PackageCode.Codes.GAME_POSITION_UPDATE.value;
		 *data[0][1] = player tile x
		 *data[0][2] = player tile y
		 *
		 *int i = extra;
		 */
		if (true /*newlyEntered*/) {
			/*
			 *data[1] = new byte[3];
			 *data[1][0] = PackageCode.Codes.GAME_NEW_ROOM.value();
			 *data[1][1] = room.getWidth();
			 *data[1][2] = room.getHeight();
			 *for (Sendable s : sendables) {
			 *		data[i++] = s.onEntry();
			 *}
			 */
		}
		else {
			/*for (Sendable s : sendabless) {
			 *		data[i++] = s.roomUpdate();
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

	/**
	 * Returns a Map of all the Users' names to their respective
	 * player Character.
	 *
	 * @return	a Map of Usernames to Characters
	 */
	public static Map<String, Character> getAllPlayers() {
		return players;
	}
}
