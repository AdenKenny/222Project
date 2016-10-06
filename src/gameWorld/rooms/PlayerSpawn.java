package gameWorld.rooms;

import java.util.Map;

import clientServer.ServerSideGame;
import gameWorld.Floor;
import gameWorld.Room;
import gameWorld.World;
import gameWorld.characters.Character;

public class PlayerSpawn extends Room implements SpawnRoom {

	private Map<String, Character> players;

	public PlayerSpawn(Floor floor, int xPos, int yPos, int width, int depth) {
		super(floor, xPos, yPos, width, depth);
		players = ServerSideGame.getAllPlayers();
		floor.addSpawnRoom(this);
	}

	public PlayerSpawn(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		players = ServerSideGame.getAllPlayers();
		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {
		for (Character player : players.values()) {
			if (!player.isAlive()) {
				int x = width/2, y = depth/2;
				World.Direction facing = World.Direction.NORTH;

				while (entities[y][x] != null) {
					x = (int) (Math.random() * width);
					y = (int) (Math.random() * depth);
				}

				entities[y][x] = player;

				player.respawn(this, x, y, facing);
			}
		}
	}

}