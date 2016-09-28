package gameWorld.rooms;

import clientServer.Game;
import gameWorld.Floor;
import gameWorld.Room;

public class PlayerSpawn extends Room {

	//private Map<String, Character>

	public PlayerSpawn(Floor floor, int xPos, int yPos, int width, int depth) {
		super(floor, xPos, yPos, width, depth);
		//Game.getAllPlayers();
	}

	public void tick() {

	}

}
