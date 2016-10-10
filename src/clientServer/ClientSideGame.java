package clientServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gameWorld.Entity;
import gameWorld.Sendable;
import gameWorld.Sendable.Types;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.CharacterModel;
import gameWorld.rooms.Room;

public class ClientSideGame extends Thread implements Game {
	private final Map<Integer, Sendable> sendables;
	private final Map<Integer, Boolean> receivedSendables;
	private Room room;
	private int characterID;
	private Character player;
	private String username;

	public ClientSideGame(String username) {
		this.username = username;
		this.sendables = new HashMap<>();
		this.receivedSendables = new HashMap<>();
	}

	@Override
	public synchronized void tick() {

	}

	public void newRoom(byte[] received) {
		this.sendables.clear();
		
		// clean up room before moving to new room
		if (this.room != null && this.player != null) {
			this.room.entities()[this.player.yPos()][this.player.xPos()] = null;
		}
		this.room = new Room(null, received[1], received[2], received[3], received[4]);
		if (this.player != null) {
			this.sendables.put(this.characterID, this.player);
			this.player.setRoom(this.room);
		}

		int doorCode = received[5];
		this.room.setDoor(Direction.WEST, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.SOUTH, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.EAST, doorCode % 2 == 1);
		doorCode = doorCode / 2;
		this.room.setDoor(Direction.NORTH, doorCode % 2 == 1);
		
		if (this.player != null) {
			this.player.setRoom(this.room);
		}
	}

	public void endSendables() {
		ArrayList<Integer> keysToRemove = new ArrayList<>();

		for (int key : this.receivedSendables.keySet()) {
			if (this.receivedSendables.put(key, false) == false) {
				keysToRemove.add(key);
			}
		}

		for (int key : keysToRemove) {
			this.receivedSendables.remove(key);
			Sendable s = this.sendables.remove(key);
			if (s instanceof Character) {
				System.out.println("removing player");
				Character c = (Character) s;
				this.room.entities()[c.yPos()][c.xPos()] = null;
			}
		}
	}

	public void addSendable(byte[] received) {
		Types type = Types.values()[received[1]];

		if (type.equals(Types.MONSTER)) {
			boolean isAlive = (received[2] == 1);
			Direction facing = Direction.values()[received[3]];
			int ID = Sendable.bytesToInt(received, 4);
			int modelId = Sendable.bytesToInt(received, 8);
			int health = Sendable.bytesToInt(received, 12);
			int level = Sendable.bytesToInt(received, 16);
			int xPos = Sendable.bytesToInt(received, 20);
			int yPos = Sendable.bytesToInt(received, 24);
			CharacterModel model = mapOfCharacters.get(modelId);
			Character toAdd = new Character(this.room, xPos, yPos, facing, level, model);
			toAdd.setAlive(isAlive);
			toAdd.setHealth(health);
			this.sendables.put(ID, toAdd);
			this.room.entities()[yPos][xPos] = toAdd;
		}
		else if (type.equals(Types.VENDOR)) {
			Direction facing = Direction.values()[received[2]];
			int ID = Sendable.bytesToInt(received, 4);
			int modelId = Sendable.bytesToInt(received, 8);
			int xPos = Sendable.bytesToInt(received, 12);
			int yPos = Sendable.bytesToInt(received, 16);
			CharacterModel model = mapOfCharacters.get(modelId);
			Character toAdd = new Character(this.room, xPos, yPos, facing, -1, model);
			this.sendables.put(ID, toAdd);
			this.room.entities()[yPos][xPos] = toAdd;
		}
		else if (type.equals(Types.PLAYER)) {
			boolean isAlive = (received[2] == 1);
			Direction facing = Direction.values()[received[3]];
			int ID = Sendable.bytesToInt(received, 4);
			int health = Sendable.bytesToInt(received, 8);
			int level = Sendable.bytesToInt(received, 12);
			int xPos = Sendable.bytesToInt(received, 16);
			int yPos = Sendable.bytesToInt(received, 20);
			StringBuilder name = new StringBuilder();
			for (int i = 24; i < received.length; i++) {
				name.append((char) received[i]);
			}
			String username = name.toString();
			Character toAdd = new Character(username);
			if (this.player == null && username.equals(this.username)) {
				this.player = toAdd;
				this.characterID = ID;
			}
			toAdd.setAlive(isAlive);
			toAdd.turn(facing);
			toAdd.setHealth(health);
			toAdd.setLevel(level);
			toAdd.setXPos(xPos);
			toAdd.setYPos(yPos);
			this.sendables.put(ID, toAdd);
			this.room.entities()[yPos][xPos] = toAdd;
			toAdd.setRoom(this.room);
		}
	}

	public void updateSendable(byte[] received) {
		int id = Sendable.bytesToInt(received, 4);
		this.receivedSendables.put(id, true);
		Sendable toUpdate = this.sendables.get(id);
		if (toUpdate == null) {
			addSendable(received);
			return;
		}
		if (toUpdate instanceof Character) {
			Character c = (Character) toUpdate;
			Entity[][] entities = this.room.entities();
			entities[c.yPos()][c.xPos()] = null;

			Character.Type type = Character.Type.values()[received[1]];

			if (type.equals(Character.Type.MONSTER)) {
				c.setAlive(received[2] == 1);
				c.turn(Direction.values()[received[3]]);
				c.setHealth(Sendable.bytesToInt(received, 12));
				c.setLevel(Sendable.bytesToInt(received, 16));
				c.setXPos(Sendable.bytesToInt(received, 20));
				c.setYPos(Sendable.bytesToInt(received, 24));
			}
			else if (type.equals(Character.Type.PLAYER)) {
				c.setAlive(received[2] == 1);
				c.turn(Direction.values()[received[3]]);
				c.setHealth(Sendable.bytesToInt(received, 8));
				c.setLevel(Sendable.bytesToInt(received, 12));
				c.setXPos(Sendable.bytesToInt(received, 16));
				c.setYPos(Sendable.bytesToInt(received, 20));
			}

			entities[c.yPos()][c.xPos()] = c;
		}

	}

	public void removeSendable(byte[] received) {
		int id = Sendable.bytesToInt(received, 1);
		Sendable toRemove = null;
		Set<Sendable> sendables = this.room.getSendables();
		for (Sendable s : sendables) {
			if (s instanceof Character) {
				Character c = (Character)s;
				if (c.getID() == id) {
					toRemove = s;
					this.room.entities()[c.yPos()][c.xPos()] = null;
					break;
				}
			}
		}
		if (toRemove != null) {
			sendables.remove(toRemove);
		}
	}

	public Room getRoom() {
		return this.room;
	}

	public synchronized Character getPlayer() {
		return this.player;
	}
}
