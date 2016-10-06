package gameWorld.rooms;

import java.util.ArrayList;

import clientServer.ServerSideGame;
import gameWorld.Entity;
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

		npc = new Character(null, -1, -1, model.getDescription(), Direction.NORTH, builder.getLevel(), model);

		deathTime = -1;
		npc.setAlive(false);

		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {

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
		} else {
			if (npc.getAttackTimer() < System.currentTimeMillis()) {
				int x = npc.xPos(), y = npc.yPos();
				ArrayList<Entity> adjacents = new ArrayList<>(4);

				if (entities[y - 1][x] instanceof Character) {
					adjacents.add(entities[y - 1][x]);
				}
				if (entities[y + 1][x] instanceof Character) {
					adjacents.add(entities[y + 1][x]);
				}
				if (entities[y][x - 1] instanceof Character) {
					adjacents.add(entities[y][x - 1]);
				}
				if (entities[y][x + 1] instanceof Character) {
					adjacents.add(entities[y][x + 1]);
				}

				if (adjacents.isEmpty()) {
					return;
				}

				Character attackChar = (Character) adjacents.get((int) (Math.random() * adjacents.size()));

				attackChar.tryAttack(npc);
			}
		}

	}
}
