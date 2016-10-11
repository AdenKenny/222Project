package gameWorld;

import java.util.HashSet;
import java.util.Set;

import clientServer.ServerSideGame;
import gameWorld.World.Direction;
import gameWorld.rooms.Room;
import gameWorld.rooms.SpawnRoom;
import gameWorld.rooms.TargetRoom;

/**
 * A class which represents a Floor that contains various Rooms.
 *
 * @author Louis
 */
public class Floor {
	private Floor previousFloor;
	private Floor nextFloor;

	private Room[][] rooms;
	private Set<SpawnRoom> spawns;

	private TargetRoom targetRoom;

	private int width;
	private int depth;

	private int level;

	public Floor(String level, String width, String depth) {
		try {
			this.level = Integer.parseInt(level);
			this.width = Integer.parseInt(width);
			this.depth = Integer.parseInt(depth);

			this.spawns = new HashSet<>();

			this.rooms = new Room[this.depth][this.width];
		} catch (NumberFormatException e) {
		}
	}

	/**
	 * Sets the neighbouring Rooms for every Room on this Floor.
	 */
	public void setupNeighbours() {
		for (int row = 0; row < this.depth; row++) {
			for (int col = 0; col < this.width; col++) {
				Room room = this.rooms[row][col];
				if (room == null) {
					continue;
				}

				if (row > 0) {
					room.setNeighbour(Direction.NORTH, this.rooms[row - 1][col]);
				} else {
					room.setNeighbour(Direction.NORTH, null);
				}

				if (row < this.depth - 1) {
					room.setNeighbour(Direction.SOUTH, this.rooms[row + 1][col]);
				} else {
					room.setNeighbour(Direction.SOUTH, null);
				}

				if (col > 0) {
					room.setNeighbour(Direction.WEST, this.rooms[row][col - 1]);
				} else {
					room.setNeighbour(Direction.WEST, null);
				}

				if (col < this.width - 1) {
					room.setNeighbour(Direction.EAST, this.rooms[row][col + 1]);
				} else {
					room.setNeighbour(Direction.EAST, null);
				}
			}
		}
	}

	/**
	 * Returns the Floor underneath this one, if there is one.
	 *
	 * @return the previous Floor
	 */
	public Floor previousFloor() { //TODO this is never set.
		return this.previousFloor;
	}

	/**
	 * Returns the Floor above this one, if there is one.
	 *
	 * @return the next Floor
	 */
	public Floor nextFloor() {
		return this.nextFloor;
	}

	/**
	 * Sets the Floor above this one to be nextFloor.
	 *
	 * @param nextFloor
	 */
	public void setNextFloor(Floor nextFloor) {
		this.nextFloor = nextFloor;
	}

	/**
	 * Returns the grid of Rooms as a 2D array of Rooms, with depth x width
	 * Rooms.
	 *
	 * @return the rooms on this Floor
	 */
	public Room[][] rooms() {
		return this.rooms;
	}

	/**
	 * Adds the specified Room to this Floor at the specified position.
	 *
	 * @param room
	 *            the Room to add
	 * @param x
	 *            the row in which to add the room
	 * @param y
	 *            the column in which to add the room
	 */
	public void addRoom(Room room, int x, int y) {
		this.rooms[y][x] = room;
	}

	/**
	 * Returns the width of this Floor, measured in Rooms.
	 *
	 * @return the width of this Floor
	 */
	public int width() {
		return this.width;
	}

	/**
	 * Returns the depth of this Floor, measured in Rooms.
	 *
	 * @return the depth of this Floor
	 */
	public int depth() {
		return this.depth;
	}

	/**
	 * Returns the level of this Floor - that is, which Floor this is, when
	 * counted from the ground up.
	 *
	 * @return this Floor's level
	 */
	public int level() {
		return this.level;
	}

	/**
	 * Adds a SpawnRoom to this Floor's Set of SpawnRooms, so that they can have
	 * tick called on them.
	 *
	 * @param spawn
	 */
	public void addSpawnRoom(SpawnRoom spawn) {
		this.spawns.add(spawn);
	}

	/**
	 * Returns the Set of SpawnRooms on this Floor.
	 *
	 * @return this Floor's SpawnRooms
	 */
	public Set<SpawnRoom> getSpawns() {
		return this.spawns;
	}

	/**
	 * Returns the Room on this Floor which the players are trying to get to.
	 *
	 * @return this Floor's TargetRoom
	 */
	public TargetRoom getTargetRoom() {
		return this.targetRoom;
	}

	/**
	 * Sets the TargetRoom of this Floor to the specified targetRoom.
	 *
	 * @param targetRoom
	 *            this Floor's new TargetRoom
	 */
	public void setTargetRoom(TargetRoom targetRoom) {
		this.targetRoom = targetRoom;
	}

	/**
	 * Checks whether any player has reached this Floor's target room. Returns
	 * true if they have, false otherwise.
	 *
	 * @return whether a player has reached the targetRoom
	 */
	public boolean targetReached() {
		return targetRoom.hasBeenReached();
	}

	/**
	 * Moves the game to the next Floor up.
	 */
	public void finishFloor() {
		ServerSideGame.world.goUpFloor();
	}

}
