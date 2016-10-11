package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gameWorld.Entity;
import gameWorld.Sendable;
import gameWorld.Sendable.Types;
import gameWorld.World;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.CharacterModel;
import gameWorld.objects.StationaryObject;
import gameWorld.rooms.Room;

public class ClientSideGame extends Thread implements Game {

	//map of all items in the room sent by server: ID to object
	private final Map<Integer, Sendable> sendables;

	//map of whether an item in the room has been sent by server this iteration: ID to whether it has been sent
	private final Map<Integer, Boolean> receivedSendables;

	//the current room
	private Room room;

	//the current floor number
	private int floor;

	//ID of the character object of the player
	private int characterID;

	//player's character instance
	private Character player;

	//player's username
	private String username;

	public ClientSideGame(String username) {
		this.username = username;
		this.sendables = new HashMap<>();
		this.receivedSendables = new HashMap<>();
	}

	@Override
	public synchronized void tick() {
		//might be needed in the future
	}

	/**
	 * Whenever the character enters a new room, the server sends a packet indicating this.
	 * This function is called by the slave when it receives that packet. This function
	 * clears the map of sendables, and creates a new room with details based on the
	 * information from the server.
	 *
	 * @param received a byte array from the server, containing information about the room
	 */
	public void newRoom(byte[] received) {
		// clears the list of sendables, as those objects won't be in the new room
		this.sendables.clear();

		this.room = new Room(null, received[1], received[2], received[3], received[4]);

		//the player's character is retained, as we know they are going to be in the new room
		if (this.player != null) {
			this.sendables.put(this.characterID, this.player);
			this.player.setRoom(this.room);
		}

		//deserialize where the doors are
		int doorCode = received[5];
		this.room.setDoor(Direction.WEST, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.SOUTH, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.EAST, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.NORTH, doorCode % 2 == 1);

		//set the floor number
		this.floor = received[6];

		if (this.player != null) {
			this.player.setRoom(this.room);
		}
	}

	/**
	 * After the server has sent all of its sendables, it sends a packet
	 * saying so. This function is then called by the slave. This function
	 * goes through the list of sendables and checks if any weren't received
	 * since the last end packet, we know that that sendable is no longer in
	 * the room.
	 */
	public void endSendables() {
		ArrayList<Integer> keysToRemove = new ArrayList<>();

		//iterate through the map of IDs to see whether they've been received this tick or not
		for (int key : this.receivedSendables.keySet()) {
			//set the boolean for that key to false, and check if it was already false
			if (this.receivedSendables.put(key, false) == false) {
				/*
				 * if it was already false, that means no information was received about it
				 * this tick. That means it is no longer in the room. Add it to a list of
				 * keys to be removed
				 */
				keysToRemove.add(key);
			}
		}

		//iterate through all the keys to be removed, i.e. the ones that were false in receivedSendables
		for (int key : keysToRemove) {
			//remove the key from receivedSendables and sendables
			this.receivedSendables.remove(key);
			Sendable s = this.sendables.remove(key);
			if (s instanceof Entity) {
				Entity c = (Entity) s;
				//remove the item from the grid of entities in the room
				this.room.entities()[c.yPos()][c.xPos()] = null;
			}
		}
	}

	/**
	 * When the slave receives a sendable information packet, it passes it
	 * on to updateSendable. If the sendable isn't already in the list
	 * of sendables, this is called to create the object.
	 *
	 * @param received the details of the sendable, sent from the server
	 */
	public void addSendable(byte[] received) {
		//check what type of sendable it is
		Types type = Types.values()[received[1]];

		if (type.equals(Types.MONSTER)) {
			//parse its details from the packet
			boolean isAlive = (received[2] == 1);
			Direction facing = Direction.values()[received[3]];
			int ID = Sendable.bytesToInt(received, 4);
			int modelId = Sendable.bytesToInt(received, 8);
			int health = Sendable.bytesToInt(received, 12);
			int level = Sendable.bytesToInt(received, 16);
			int xPos = Sendable.bytesToInt(received, 20);
			int yPos = Sendable.bytesToInt(received, 24);

			//get its character model from the map
			CharacterModel model = mapOfCharacters.get(modelId);

			//create the character and update its fields
			Character toAdd = new Character(this.room, xPos, yPos, facing, level, model);
			toAdd.setAlive(isAlive);
			toAdd.setHealth(health);
			//add it to the list of sendables
			this.sendables.put(ID, toAdd);
			//add the character to the grid of entities
			this.room.entities()[yPos][xPos] = toAdd;
			//set the character to hold the current room
			toAdd.setRoom(this.room);
			//set the character's id
			toAdd.setID(ID);
		}

		else if (type.equals(Types.VENDOR)) {
			//parse its details from the packet
			Direction facing = Direction.values()[received[2]];
			int ID = Sendable.bytesToInt(received, 4);
			int modelId = Sendable.bytesToInt(received, 8);
			int xPos = Sendable.bytesToInt(received, 12);
			int yPos = Sendable.bytesToInt(received, 16);

			//get its character model from the map
			CharacterModel model = mapOfCharacters.get(modelId);

			//create the character
			Character toAdd = new Character(this.room, xPos, yPos, facing, -1, model);
			//add it to the list of sendables
			this.sendables.put(ID, toAdd);
			//add the character to the grid of entities
			this.room.entities()[yPos][xPos] = toAdd;
			//set the character to hold the current room
			toAdd.setRoom(this.room);
			//set the character's id
			toAdd.setID(ID);
		}

		else if (type.equals(Types.PLAYER)) {
			//parse its details from the packet
			boolean isAlive = (received[2] == 1);
			Direction facing = Direction.values()[received[3]];
			int ID = Sendable.bytesToInt(received, 4);
			int health = Sendable.bytesToInt(received, 8);
			int xp = Sendable.bytesToInt(received, 12);
			int level = Sendable.bytesToInt(received, 16);
			int xPos = Sendable.bytesToInt(received, 20);
			int yPos = Sendable.bytesToInt(received, 24);

			//get the items
			int itemsSize = received[28];
			List<Integer> items = new ArrayList<>();
			for (int i = 0; i < itemsSize * 4; i+=4) {
				items.add(Sendable.bytesToInt(received, 29 + i));
			}

			//get the name from the packet
			StringBuilder name = new StringBuilder();
			for (int i = 29 + itemsSize * 4; i < received.length; i++) {
				name.append((char) received[i]);
			}
			String username = name.toString();

			//create the character
			Character toAdd = new Character(username);
			//check if this is the character for this player
			if (this.player == null && username.equals(this.username)) {
				this.player = toAdd;
				this.characterID = ID;
			}

			//update the character's fields
			toAdd.setAlive(isAlive);
			toAdd.turn(facing);
			toAdd.setHealth(health);
			toAdd.setXp(xp);
			toAdd.setLevel(level);
			toAdd.setXPos(xPos);
			toAdd.setYPos(yPos);
			toAdd.setItems(items);
			//add it to the list of sendables
			this.sendables.put(ID, toAdd);
			//add the character to the grid of entities
			this.room.entities()[yPos][xPos] = toAdd;
			//set the character to hold the current room
			toAdd.setRoom(this.room);
			//set the character's id
			toAdd.setID(ID);
		}

		else if (type.equals(Types.DROP)) {
			World.Direction facing = Direction.values()[received[2]];
			int ID = Sendable.bytesToInt(received, 3);
			int modelID = Sendable.bytesToInt(received, 7);
			int xPos = Sendable.bytesToInt(received, 11);
			int yPos = Sendable.bytesToInt(received, 15);
			int item = Sendable.bytesToInt(received, 19);
			StationaryObject toAdd = new StationaryObject(mapOfObjects.get(modelID), this.room, xPos, yPos, facing);
			toAdd.setItem(item);
			this.sendables.put(ID, toAdd);
			this.room.entities()[yPos][xPos] = toAdd;
			toAdd.setID(ID);
		}
	}

	/**
	 * Whenever the slave receives a game information packet, this function is called.
	 *
	 * @param received a packet from the server containing the details of a sendable
	 */
	public void updateSendable(byte[] received) {
		//get the id from the packet
		int id = Sendable.bytesToInt(received, 4);
		//list this id as having been updated this tick
		this.receivedSendables.put(id, true);
		//get the sendable this corresponds to from the map of sendables
		Sendable toUpdate = this.sendables.get(id);
		//if the sendable is not in the map
		if (toUpdate == null) {
			//create the sendable
			addSendable(received);
			return;
		}

		if (toUpdate instanceof Character) {
			Character c = (Character) toUpdate;
			Entity[][] entities = this.room.entities();
			//remove the character from its old position on the grid
			entities[c.yPos()][c.xPos()] = null;

			Character.Type type = Character.Type.values()[received[1]];

			if (!type.equals(Character.Type.VENDOR)) {
				//update shared fields, where type doesn't matter
				c.setAlive(received[2] == 1);
				c.turn(Direction.values()[received[3]]);
				c.setXPos(Sendable.bytesToInt(received, 20));
				c.setYPos(Sendable.bytesToInt(received, 24));
			}

			//change fields dependent on type
			if (type.equals(Character.Type.MONSTER)) {
				c.setHealth(Sendable.bytesToInt(received, 12));
			}
			else if (type.equals(Character.Type.PLAYER)) {
				c.setHealth(Sendable.bytesToInt(received, 8));
				c.setXp(Sendable.bytesToInt(received, 12));
				c.setLevel(Sendable.bytesToInt(received, 16));
				//get the items
				List<Integer> items = new ArrayList<>();
				int itemsSize = received[28];
				for (int i = 0; i < itemsSize * 4; i+=4) {
					items.add(Sendable.bytesToInt(received, 29 + i));
				}
			}

			//add the character to its new position on the grid
			entities[c.yPos()][c.xPos()] = c;
		}

	}

	/**
	 * Called by slave when the game is won
	 */
	public void gameWon() {
		//TODO game won
	}

	/**
	 * Get the current room the player is in
	 *
	 * @return current room
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Get the floor number the player is on
	 *
	 * @return floor number
	 */
	public int getFloor() {
		return this.floor;
	}

	/**
	 * Get the character of the player
	 *
	 * @return player character
	 */
	public synchronized Character getPlayer() {
		return this.player;
	}
}
