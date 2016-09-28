package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataStorage.LoadGame;
import gameWorld.Room;
import gameWorld.Sendable;
import gameWorld.World;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.rooms.SpawnRoom;
import userHandling.User;

public class ServerSideGame implements Game {


	private static Map<String, Character> players = new HashMap<String, Character>();

	public static final World world = new World();

	private final Map<Long, User> connectedUsers;
	private final Map<Long, Boolean> roomDetails;
	private final List<String> textMessages;

	public ServerSideGame() {
		LoadGame loader = new LoadGame();

		/*for(Character c : loader.getPlayers()) {
			players.put(c.getName(), c); //Loads players into game.
		}*/

		this.connectedUsers = new HashMap<>();
		this.roomDetails = new HashMap<>();
		this.textMessages = new ArrayList<>();

		/*	TODO: remove if the Game still works
		XMLReader reader = XMLReader.getInstance();
		ServerSideGame.mapOfItems = reader.getItems();
		ServerSideGame.mapOfCharacters = reader.getCharacters();
		*/
	}

	@Override
	public synchronized void tick() {
		//System.out.println(world.getCurrentFloor().getSpawns());
		if (world.getCurrentFloor().getSpawns() != null) {
			for (SpawnRoom spawn : world.getCurrentFloor().getSpawns()) {
				spawn.tick();
			}
		}
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
		this.roomDetails.put(uid, false);
		//players.put(user.getUsername(), new Character(user.getUsername()));
	}

	/**
	 * a connection has stopped, so remove the user from connected users
	 * @param uid
	 */
	public void disconnect(long uid) {
		Character player = players.get(this.connectedUsers.get(uid).getUsername());
		player.room().entities()[player.xPos()][player.yPos()] = null;;
		this.connectedUsers.remove(uid);
		this.roomDetails.remove(uid);
	}

	/**
	 * parse actions made by a player
	 * @param uid
	 * @param input
	 */
	public void keyPress(long uid, byte input) {
		//TODO check for timing restriction
		Character p = players.get(connectedUsers.get(uid).getUsername());
		if (input == PackageCode.Codes.KEY_PRESS_W.value()) {
			p.move(Direction.FORWARD);
		}
		else if (input == PackageCode.Codes.KEY_PRESS_A.value()) {
			p.move(Direction.LEFT);
		}
		else if (input == PackageCode.Codes.KEY_PRESS_S.value()) {
			p.move(Direction.BACK);
		}
		else if (input == PackageCode.Codes.KEY_PRESS_D.value()) {
			p.move(Direction.RIGHT);
		}
		else if (input == PackageCode.Codes.KEY_PRESS_Q.value()) {
			p.turnLeft();
		}
		else if (input == PackageCode.Codes.KEY_PRESS_E.value()) {
			p.turnRight();
		}
	}

	/**
	 * get a game state to send to the player
	 * @param uid
	 * @return
	 */
	public synchronized byte[][] toByteArray(long uid) {
		// get the character of the user
		Character player = players.get(this.connectedUsers.get(uid).getUsername());
		Room room = player.room();
		if (room == null) {
			return new byte[0][];
		}
		Set<Sendable> sendables = room.getSendables();
		boolean newlyEntered = !this.roomDetails.get(uid);
		int extra = newlyEntered ? 1 : 0;
		byte[][] data = new byte[sendables.size() + extra][];
		int i = extra;
		if (newlyEntered) {
			this.roomDetails.put(uid, true);
			data[0] = new byte[3];
			data[0][0] = PackageCode.Codes.GAME_NEW_ROOM.value();
			data[0][1] = (byte)room.width();
			data[0][2] = (byte)room.depth();
			for (Sendable s : sendables) {
				data[i++] = s.onEntry();
			}
		}
		else {
			for (Sendable s : sendables) {
				data[i++] = s.roomUpdate();
			}
		}
		return data;
	}

	public synchronized byte[] getSendable(long uid, int id) {
		Set<Sendable> sendables = players.get(this.connectedUsers.get(uid).getUsername()).room().getSendables();
		for (Sendable s : sendables) {
			if (s instanceof Character && ((Character)s).getID() == id) {
				return s.onEntry();
			}
		}
		return null;
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
