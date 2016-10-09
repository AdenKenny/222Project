package gameWorld.rooms;

import java.util.ArrayList;

import clientServer.Game;
import clientServer.ServerSideGame;
import gameWorld.Entity;
import gameWorld.Floor;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.Character.Type;
import gameWorld.characters.CharacterModel;
import gameWorld.objects.ObjectModel;
import gameWorld.objects.StationaryObject;

/**
 * A class to represent Rooms which can spawn monster or vendor Characters.
 * 
 * @author Louis
 */
public class NPCSpawn extends Room implements SpawnRoom {
	private static final int RESPAWN_TIME = 5000;
	private Character npc;
	private long deathTime;
	private boolean wasAlive;

	public NPCSpawn(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		CharacterModel model = ServerSideGame.mapOfCharacters.get(builder.getmodelID());
		this.npc = new Character(null, -1, -1, model.getDescription(), Direction.NORTH, builder.getLevel(), model);
		this.deathTime = -1;
		this.wasAlive = false;
		this.npc.setAlive(false);
		floor.addSpawnRoom(this);
	}

	@Override
	public void tick() {
		if (this.npc.getType().equals(Type.MONSTER)) {
			if (!this.npc.isAlive()) {
				if (this.deathTime == -1) {
					this.deathTime = System.currentTimeMillis();

					if (this.wasAlive) {
						// create a Drop Entity at the position of the Monster
						// that died
						int x = this.npc.xPos(), y = this.npc.yPos();
						ObjectModel dropModel = Game.mapOfObjects.get(0);
						dropModel.setItems(this.npc.getItems());
						this.entities[y][x] = new StationaryObject(dropModel, this, x, y, Direction.NORTH);
					}
				} else {
					if (this.deathTime + RESPAWN_TIME <= System.currentTimeMillis()) {
						if (this.wasAlive) {
							int x = this.npc.xPos(), y = this.npc.yPos();
							if (this.entities[y][x] != null) {
								if (this.entities[y][x] instanceof StationaryObject) {
									// the drop hasn't been picked up yet, so
									// wait until it is
									return;
								}
							}
						}

						int x = this.width / 2, y = this.depth / 2;
						while (this.entities[y][x] != null) {
							x = (int) (Math.random() * this.width);
							y = (int) (Math.random() * this.depth);
						}
						this.npc.respawn(this, x, y, Direction.NORTH);
						this.deathTime = -1;
						this.wasAlive = true;
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
		} else {
			if (!this.npc.isAlive()) {
				int x = this.width / 2, y = this.depth / 2;
				while (this.entities[y][x] != null) {
					x = (int) (Math.random() * this.width);
					y = (int) (Math.random() * this.depth);
				}
				this.entities[y][x] = this.npc;
				this.npc.respawn(this, x, y, Direction.NORTH);
			}
		}
	}
}