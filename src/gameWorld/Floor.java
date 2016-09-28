package gameWorld;

import java.util.HashSet;
import java.util.Set;

import gameWorld.World.Direction;
import gameWorld.rooms.SpawnRoom;

public class Floor {
	private Floor previousFloor;
	private Floor nextFloor;

	private Room[][] rooms;
	private Set<SpawnRoom> spawns;

	private int width;
	private int depth;

	private int level;

	/**
	 * Constructs a Floor containing a grid of Rooms which has
	 * the specified width and depth, with each Room having
	 * the specified width (roomWidth) and depth (roomDepth).
	 *
	 * @param previousFloor
	 * @param level
	 * @param width
	 * @param depth
	 * @param roomWidth
	 * @param roomDepth
	 */
	public Floor(Floor previousFloor, int level, int width, int depth, int roomWidth, int roomDepth) {
		this.previousFloor = previousFloor;

		this.level = level;

		this.width = width;
		this.depth = depth;

		this.spawns = new HashSet<SpawnRoom>();

		setupRooms(roomWidth, roomDepth);
	}

	public Floor(int level, int width, int depth) {
		this.level = level;
		this.width = width;
		this.depth = depth;

		this.spawns = new HashSet<SpawnRoom>();

		this.rooms = new Room[depth][width];
	}

	public Floor(String level, String width, String depth) {
		try {
			this.level = Integer.parseInt(level);
			this.width = Integer.parseInt(width);
			this.depth = Integer.parseInt(depth);

			this.spawns = new HashSet<SpawnRoom>();

			this.rooms = new Room[this.depth][this.width];
		} catch (NumberFormatException e) {}
	}

	/**
	 * Sets the neighbouring Rooms for every Room on this Floor.
	 */
	public void setupNeighbours() {
		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				if (row > 0) rooms[row][col].setNeighbour(Direction.NORTH, rooms[row-1][col]);
				if (row < depth - 1) rooms[row][col].setNeighbour(Direction.SOUTH, rooms[row+1][col]);
				if (col > 0) rooms[row][col].setNeighbour(Direction.WEST, rooms[row][col-1]);
				if (col < width - 1) rooms[row][col].setNeighbour(Direction.EAST, rooms[row][col+1]);
			}
		}
	}

	private void setupRooms(int roomWidth, int roomDepth) {
		rooms = new Room[depth][width];

		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				rooms[row][col] = new Room(this, row, col, roomWidth, roomDepth);
			}
		}

		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				if (row > 0) rooms[row][col].setNeighbour(Direction.NORTH, rooms[row-1][col]);
				if (row < depth - 1) rooms[row][col].setNeighbour(Direction.SOUTH, rooms[row+1][col]);
				if (col > 0) rooms[row][col].setNeighbour(Direction.WEST, rooms[row][col-1]);
				if (col < width - 1) rooms[row][col].setNeighbour(Direction.EAST, rooms[row][col+1]);
			}
		}
	}

	/**
	 * Returns the Floor underneath this one, if there is one.
	 *
	 * @return	the previous Floor
	 */
	public Floor previousFloor() {
		return previousFloor;
	}

	/**
	 * Returns the Floor above this one, if there is one.
	 *
	 * @return	the next Floor
	 */
	public Floor nextFloor() {
		return nextFloor;
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
	 * Returns the grid of Rooms as a 2D array of Rooms,
	 * with depth x width Rooms.
	 *
	 * @return	the rooms on this Floor
	 */
	public Room[][] rooms() {
		return rooms;
	}

	/**
	 * Adds the specified Room to this Floor at the specified
	 * position.
	 *
	 * @param room	the Room to add
	 * @param x		the row in which to add the room
	 * @param y		the column in which to add the room
	 */
	public void addRoom(Room room, int x, int y) {
		rooms[y][x] = room;
	}

	/**
	 * Returns the width of this Floor, measured in Rooms.
	 *
	 * @return	the width of this Floor
	 */
	public int width() {
		return width;
	}

	/**
	 * Returns the depth of this Floor, measured in Rooms.
	 *
	 * @return	the depth of this Floor
	 */
	public int depth() {
		return depth;
	}

	/**
	 * Returns the level of this Floor - that is, which
	 * Floor this is, when counted from the ground up.
	 *
	 * @return	this Floor's level
	 */
	public int level() {
		return level;
	}

	/**
	 * Adds a SpawnRoom to this Floor's Set of SpawnRooms,
	 * so that they can have tick called on them.
	 *
	 * @param spawn
	 */
	public void addSpawnRoom(SpawnRoom spawn) {
		spawns.add(spawn);
	}

	/**
	 * Returns the Set of SpawnRooms on this Floor.
	 *
	 * @return	this Floor's SpawnRooms
	 */
	public Set<SpawnRoom> getSpawns() {
		return spawns;
	}

}
