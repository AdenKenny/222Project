package gameWorld.rooms;

import java.util.Set;

import clientServer.ServerSideGame;
import gameWorld.Floor;
import gameWorld.World;
import gameWorld.characters.Character;

public class PlayerSpawn extends Room implements SpawnRoom {

	private Set<Character> players;

	public PlayerSpawn(Floor floor, int xPos, int yPos, int width, int depth) {
		super(floor, xPos, yPos, width, depth);
		this.players = ServerSideGame.world.getPlayers();
		floor.addSpawnRoom(this);
	}

	public PlayerSpawn(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		this.players = ServerSideGame.world.getPlayers();
		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {
		for (Character player : this.players) {
			if (!player.isAlive()) {
				int x = this.width/2, y = this.depth/2;
				World.Direction facing = World.Direction.NORTH;

				while (this.entities[y][x] != null) {
					x = (int) (Math.random() * this.width);
					y = (int) (Math.random() * this.depth);
				}

				this.entities[y][x] = player;

				player.respawn(this, x, y, facing);
			}
		}
	}

}