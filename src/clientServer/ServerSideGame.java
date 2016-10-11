package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataStorage.LoadGame;
import gameWorld.Floor;
import gameWorld.Sendable;
import gameWorld.World;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;
import gameWorld.rooms.SpawnRoom;
import userHandling.User;
import util.FileVerifier;
import util.Logging;

public class ServerSideGame implements Game {

	private static final Map<String, Character> players = new HashMap<>();

	public static final World world = new World();

	private final Map<Long, Player> connectedPlayers;
	private final List<String> textMessages;
	private final Map<Room, byte[][]> byteArrays;
	private int tickCounter = 0;

	public ServerSideGame() {

		for (Character c : LoadGame.getInstance().getPlayers()) {
			players.put(c.getName(), c); // Loads players into game.
		}

		//FileVerifier.getInstance().checkFiles();

		this.connectedPlayers = new HashMap<>();
		this.textMessages = new ArrayList<>();
		this.byteArrays = new HashMap<>();

	}

	@Override
	public synchronized void tick() {
		for (Player player : this.connectedPlayers.values()) {
			player.doMovement();
		}

		Floor current = world.getCurrentFloor();
		if (current.getSpawns() != null) {
			for (SpawnRoom spawn : current.getSpawns()) {
				spawn.tick();
			}
		}

		current.getTargetRoom().tick();

		this.byteArrays.clear();
		for (Room[] rooms : current.rooms()) {
			for (Room room : rooms) {
				Set<Sendable> sendables = room.getSendables();
				byte[][] data = new byte[sendables.size() + 1][];
				int i = 0;
				for (Sendable s : sendables) {
					data[i++] = s.toSend();
				}
				data[data.length - 1] = new byte[1];
				data[data.length - 1][0] = PackageCode.Codes.GAME_SENDABLE_END.value();
				this.byteArrays.put(room, data);
			}
		}
		this.tickCounter++;
	}

	/**
	 * associate the id of a Master with the user logged in on that connection,
	 * and associate the name of the user with their character
	 *
	 * @param uid
	 * @param user
	 */
	public void registerConnection(long uid, User user) {
		String username = user.getUsername();
		Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT,
				"User " + uid + " has logged in as " + username + ".");
		Character character = players.get(username);
		if (character == null) {
			character = new Character(username);
			players.put(username, character);
		}
		world.addPlayer(character);
		this.connectedPlayers.put(uid, new Player(user, character));
		System.out.println(this.connectedPlayers.size());
	}

	/**
	 * a connection has stopped, so remove the user from connected users
	 *
	 * @param uid
	 */
	public void disconnect(long uid) {
		Player player = this.connectedPlayers.remove(uid);
		if (player == null) {
			return;
		}
		Character character = player.getCharacter();
		world.removePlayer(character);
		character.slay();
		Room room = character.room();
		if (room != null) {
			room.entities()[character.yPos()][character.xPos()] = null;
		}
	}

	/**
	 * parse actions made by a player
	 *
	 * @param uid
	 * @param input
	 */
	public void keyPress(long uid, byte input) {
		Player player = this.connectedPlayers.get(uid);
		if (input == PackageCode.Codes.KEY_PRESS_W.value()) {
			player.setToMove(Direction.FORWARD);
		} else if (input == PackageCode.Codes.KEY_PRESS_A.value()) {
			player.setToMove(Direction.LEFT);
		} else if (input == PackageCode.Codes.KEY_PRESS_S.value()) {
			player.setToMove(Direction.BACK);
		} else if (input == PackageCode.Codes.KEY_PRESS_D.value()) {
			player.setToMove(Direction.RIGHT);
		} else if (input == PackageCode.Codes.KEY_PRESS_Q.value()) {
			player.setToTurn(Direction.LEFT);
		} else if (input == PackageCode.Codes.KEY_PRESS_E.value()) {
			player.setToTurn(Direction.RIGHT);
		}
	}

	/**
	 * get a game state to send to the player
	 *
	 * @param uid
	 * @return
	 */
	public synchronized byte[][] toByteArray(long uid) {
		Room room = this.connectedPlayers.get(uid).getCharacter().room();
		if (room == null) {
			return null;
		}
		byte[][] arrays = this.byteArrays.get(room);
		return arrays;
	}

	public synchronized byte[] checkNewlyEntered(long uid) {
		Player player = this.connectedPlayers.get(uid);
		if (player.isNewlyEntered()) {
			Room room = player.getCharacter().room();
			if (room == null) {
				return null;
			}
			player.setNewlyEntered(false);
			byte[] roomEntry = new byte[7];
			roomEntry[0] = PackageCode.Codes.GAME_NEW_ROOM.value();
			roomEntry[1] = (byte) room.xPos();
			roomEntry[2] = (byte) room.yPos();
			roomEntry[3] = (byte) room.width();
			roomEntry[4] = (byte) room.depth();
			// get byte code for where doors are
			int doorCode = room.hasDoor(Direction.NORTH) ? 1 : 0;
			doorCode = doorCode * 2 + (room.hasDoor(Direction.EAST) ? 1 : 0);
			doorCode = doorCode * 2 + (room.hasDoor(Direction.SOUTH) ? 1 : 0);
			doorCode = doorCode * 2 + (room.hasDoor(Direction.WEST) ? 1 : 0);
			roomEntry[5] = (byte) doorCode;
			roomEntry[6] = (byte) room.floor().level();
			return roomEntry;
		}
		return null;
	}

	public synchronized int getTickCounter() {
		return this.tickCounter;
	}

	/**
	 * add a message sent by a user to the list of sent messages
	 *
	 * @param uid
	 * @param message
	 */
	public void textMessage(long uid, String message) { // TODO What the fuck?
														// Why is a string
														// passed here?
		// add the users name to the start of the text message
		message = this.connectedPlayers.get(uid).getCharacter().getName() + ": " + message;
		this.textMessages.add(message);
	}

	/**
	 * Send all unreceived messages to a user.
	 *
	 * @param messagesReceived
	 * @return A String array of the messages that we send.
	 */
	public String[] getMessages(int messagesReceived) {
		String[] messages = new String[this.textMessages.size() - messagesReceived];
		for (int i = 0; i + messagesReceived < this.textMessages.size(); i++) {
			messages[i] = this.textMessages.get(i + messagesReceived);
		}
		return messages;
	}

	/**
	 * Checks whether a particular user is currently online - that is, whether
	 * the user is currently connected to the server.
	 *
	 * @param user
	 *            The user to check for
	 * @return Whether the user is online
	 */
	public boolean userOnline(User user) {

		for (Player p : this.connectedPlayers.values()) {
			if (user.equals(p.getUser())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns a set of all the connected users in the game.
	 *
	 * @return A hash set of all connected users to the game.
	 */

	public Set<User> getAllUsers() {

		Set<User> set = new HashSet<>();

		for (Entry<Long, Player> entry : this.connectedPlayers.entrySet()) {
			set.add(entry.getValue().getUser()); // Add the value of the key
													// value pair.
		}

		return set;
	}

	/**
	 * Returns a Map of all the Users' names to their respective player
	 * Character.
	 *
	 * @return a Map of Usernames to Characters
	 */
	public static Map<String, Character> getAllPlayers() {
		return players;
	}
}
