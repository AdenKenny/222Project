package clientServer;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import gameWorld.Sendable;
import gameWorld.World;
import gameWorld.characters.Character;

public class ClientSideGame extends Thread {
	private Set<Sendable> sendables;

	public ClientSideGame() {
		this.sendables = new HashSet<>();
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
		}
		else if (type.equals(Character.Type.VENDOR)) {
			World.Direction facing = World.Direction.values()[received[2]];
			/*modelID, ID, xPos, yPos*/
		}
		else if (type.equals(Character.Type.PLAYER)) {
			boolean isAlive = (received[2] == 1);
			World.Direction facing = World.Direction.values()[received[3]];
			/*ID, health, level, xPos, yPos*/
			//String builder
		}
	}

	public void updateSendable(byte[] received) {

	}

	public int bytesToInt(byte[] bytes, int start) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = bytes[start + i];
		}
		return ByteBuffer.wrap(bs).getInt();
	}
}
