package clientServer;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import gameWorld.Room;
import gameWorld.Sendable;
import gameWorld.World;
import gameWorld.characters.Character;

public class ClientSideGame extends Thread implements Game {
	private Set<Sendable> sendables;
	private Room room;

	public ClientSideGame() {
		this.sendables = new HashSet<>();
	}

	public synchronized void tick() {

	}

	public void updatePosition(byte[] received) {
		/*
		 * TODO set character position
		 * x = received[1]
		 * y = received[2]
		 */
	}

	public void newRoom(byte[] received) {
		/*
		 * TODO this.room = new room of width = received[1] and height = received[2]
		 */
	}

	public void addSendable(byte[] received) {
		Character.Type type = Character.Type.values()[received[1]];
		if (type.equals(Character.Type.MONSTER)) {
			boolean isAlive = (received[2] == 1);
			World.Direction facing = World.Direction.values()[received[3]];
			int modelId = bytesToInt(received, 4);
			int ID = bytesToInt(received, 8);
			int health = bytesToInt(received, 12);
			int level = bytesToInt(received, 16);
			int xPos = bytesToInt(received, 20);
			int yPos = bytesToInt(received, 24);
			//TODO create character
		}
		else if (type.equals(Character.Type.VENDOR)) {
			World.Direction facing = World.Direction.values()[received[2]];
			int modelId = bytesToInt(received, 3);
			int ID = bytesToInt(received, 7);
			int xPos = bytesToInt(received, 11);
			int yPos = bytesToInt(received, 15);
			//TODO create character
		}
		else if (type.equals(Character.Type.PLAYER)) {
			boolean isAlive = (received[2] == 1);
			World.Direction facing = World.Direction.values()[received[3]];
			int ID = bytesToInt(received, 4);
			int health = bytesToInt(received, 8);
			int level = bytesToInt(received, 12);
			int xPos = bytesToInt(received, 16);
			int yPos = bytesToInt(received, 20);
			StringBuilder name = new StringBuilder();
			for (int i = 24; i < received.length; i++) {
				name.append((char) received[i]);
			}
			//TODO create character
		}
	}

	public void updateSendable(byte[] received) {
		/*
		 * TODO
		 * Sendable toUpdate = room.getSendable(bytesToInt(received, 3));
		 * if (toUpdate instanceof Character) {
		 * 		Character c = (Character) toUpdate;
		 * 		c.setAlive(received[1] == 1);
		 * 		c.setFacing(received[2]);
		 * 		c.setHealth(bytesToInt(received, 7));
		 * 		c.setLevel(bytesToInt(received, 11));
		 * 		c.setX(bytesToInt(received, 15));
		 * 		c.setY(bytesToInt(received, 19));
		 * }
		 */
	}

	public int bytesToInt(byte[] bytes, int start) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = bytes[start + i];
		}
		return ByteBuffer.wrap(bs).getInt();
	}
}
