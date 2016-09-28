package clientServer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gameWorld.Entity;
import gameWorld.Room;
import gameWorld.Sendable;
import gameWorld.World;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.CharacterModel;

public class ClientSideGame extends Thread implements Game {
	private final Map<Integer, Sendable> sendables;
	private final Set<Integer> unknownIds;
	private Room room;
	private Character player;

	public ClientSideGame() {
		this.sendables = new HashMap<>();
		this.unknownIds = new HashSet<>();
	}

	@Override
	public synchronized void tick() {

	}

	public void newRoom(byte[] received) {
		sendables.clear();
		this.room = new Room(null, -1, -1, received[1], received[2]);
	}

	public void addSendable(byte[] received) {
		Character.Type type = Character.Type.values()[received[1]];

		if (type.equals(Character.Type.MONSTER)) {
			boolean isAlive = (received[2] == 1);
			World.Direction facing = World.Direction.values()[received[3]];
			int modelId = Sendable.bytesToInt(received, 4);
			int ID = Sendable.bytesToInt(received, 8);
			int health = Sendable.bytesToInt(received, 12);
			int level = Sendable.bytesToInt(received, 16);
			int xPos = Sendable.bytesToInt(received, 20);
			int yPos = Sendable.bytesToInt(received, 24);
			CharacterModel model = mapOfCharacters.get(modelId);
			Character toAdd = new Character(this.room, xPos, yPos, model.getDescription(), facing, level, model);
			toAdd.setAlive(isAlive);
			toAdd.setHealth(health);
			sendables.put(ID, toAdd);
			this.room.entities()[xPos][yPos] = toAdd;
		}
		else if (type.equals(Character.Type.VENDOR)) {
			World.Direction facing = World.Direction.values()[received[2]];
			int modelId = Sendable.bytesToInt(received, 3);
			int ID = Sendable.bytesToInt(received, 7);
			int xPos = Sendable.bytesToInt(received, 11);
			int yPos = Sendable.bytesToInt(received, 15);
			CharacterModel model = mapOfCharacters.get(modelId);
			Character toAdd = new Character(this.room, xPos, yPos, model.getDescription(), facing, -1, model);
			sendables.put(ID, toAdd);
			this.room.entities()[xPos][yPos] = toAdd;
		}
		else if (type.equals(Character.Type.PLAYER)) {
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
			Character toAdd = new Character(name.toString());
			toAdd.setAlive(isAlive);
			toAdd.setFacing(facing);
			toAdd.setHealth(health);
			toAdd.setLevel(level);
			toAdd.setXPos(xPos);
			toAdd.setYPos(yPos);
			sendables.put(ID, toAdd);
			this.room.entities()[xPos][yPos] = toAdd;
		}
	}

	public void updateSendable(byte[] received) {
		int id = Sendable.bytesToInt(received, 3);
		Sendable toUpdate = sendables.get(id);
		if (toUpdate == null) {
			this.unknownIds.add(id);
		}
		if (toUpdate instanceof Character) {
			Character c = (Character) toUpdate;
			Entity[][] entities = room.entities();

			entities[c.yPos()][c.xPos()] = null;
			c.setAlive(received[1] == 1);
			c.setFacing(Direction.values()[received[2]]);
			c.setHealth(Sendable.bytesToInt(received, 7));
			c.setLevel(Sendable.bytesToInt(received, 11));
			c.setXPos(Sendable.bytesToInt(received, 15));
			c.setYPos(Sendable.bytesToInt(received, 19));

			entities[c.yPos()][c.xPos()] = c;
		}

	}

	public Room getRoom() {
		return room;
	}

	public Character getPlayer(String name) {
		if (room == null) return null;

		Entity[][] entities = room.entities();
		int depth = room.depth();
		int width = room.width();
		for (int i = 0; i < depth; ++i) {
			for (int j = 0; j < width; ++j) {
				if (entities[i][j] != null
						&& entities[i][j].name().equals(name)) {
					return (Character) entities[i][j];
				}
			}
		}
		return null;
	}

	public Set<Integer> getUnknown() {
		return this.unknownIds;
	}
}
