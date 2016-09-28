package gameWorld.rooms;

import clientServer.ServerSideGame;
import gameWorld.Floor;
import gameWorld.Room;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.CharacterModel;

public class NPCSpawn extends Room implements SpawnRoom {

	private static final int RESPAWN_TIME = 5000;

	private Character npc;
	private long deathTime;

	public NPCSpawn(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		CharacterModel model = ServerSideGame.mapOfCharacters.get(builder.getmodelID());

		npc = new Character(null, -1, -1, model.getDescription(), Direction.NORTH,
				builder.getLevel(), model);

		deathTime = -1;
		npc.setAlive(false);

		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {

		//System.out.println(npc.isAlive());

		if (!npc.isAlive()) {
			if (deathTime == -1) {
				deathTime = System.currentTimeMillis();
			} else {
				if (deathTime + RESPAWN_TIME <= System.currentTimeMillis()) {
					int x = width / 2, y = depth / 2;
					while (entities[y][x] != null) {
						x = (int) (Math.random() * width) + 1;
						y = (int) (Math.random() * depth) + 1;
					}

					npc.respawn(this, x, y, Direction.NORTH);
					deathTime = -1;
				}
			}
		}

	}
}
