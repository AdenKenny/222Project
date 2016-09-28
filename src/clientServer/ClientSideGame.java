package clientServer;

import java.util.HashSet;
import java.util.Set;

import gameWorld.Room;
import gameWorld.Sendable;

public class ClientSideGame extends Thread {
	private Set<Sendable> sendables;

	private Room currentRoom;

	public ClientSideGame(Room currentRoom) {
		this.sendables = new HashSet<>();
		this.currentRoom = currentRoom;
	}

	public void addSendable(byte[] received) {

	}

	public void updateSendable(byte[] received) {

	}
}
