package gameWorld;

import gameWorld.World.Direction;

public class Floor {
	private Floor previousFloor;
	private Floor nextFloor;

	private Room[][] rooms;

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

		setupRooms(roomWidth, roomDepth);
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

}
