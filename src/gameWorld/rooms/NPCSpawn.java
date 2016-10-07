package gameWorld.rooms;

import java.util.ArrayList;

import clientServer.Game;
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
		CharacterModel model = Game.mapOfCharacters.get(builder.getmodelID());

		this.npc = new Character(null, -1, -1, model.getDescription(), Direction.NORTH, builder.getLevel(), model);

		this.deathTime = -1;
		this.npc.setAlive(false);

		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {

		if (!this.npc.isAlive()) {
			if (this.deathTime == -1) {
				this.deathTime = System.currentTimeMillis();
			} else {
				if (this.deathTime + RESPAWN_TIME <= System.currentTimeMillis()) {
					int x = this.width / 2, y = this.depth / 2;
					while (this.entities[y][x] != null) {
						x = (int) (Math.random() * this.width) + 1;
						y = (int) (Math.random() * this.depth) + 1;
					}

					this.npc.respawn(this, x, y, Direction.NORTH);
					this.deathTime = -1;
				}
			}
		} else {
			if (this.npc.getAttackTimer() < System.currentTimeMillis()) {
				int x = this.npc.xPos(), y = this.npc.yPos();
				ArrayList<Entity> adjacents = new ArrayList<>(4);

				if (this.entities[y - 1][x] instanceof Character) {
					adjacents.add(this.entities[y - 1][x]);
				}
				if (this.entities[y + 1][x] instanceof Character) {
					adjacents.add(this.entities[y + 1][x]);
				}
				if (this.entities[y][x - 1] instanceof Character) {
					adjacents.add(this.entities[y][x - 1]);
				}
				if (this.entities[y][x + 1] instanceof Character) {
					adjacents.add(this.entities[y][x + 1]);
				}

				if (adjacents.isEmpty()) {
					return;
				}

				Character attackChar = (Character) adjacents.get((int) (Math.random() * adjacents.size()));

				attackChar.tryAttack(this.npc);
			}
		}

	}
}
