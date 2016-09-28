package gameWorld;

import java.util.ArrayList;
import java.util.Random;

public class World {
	/**
	 * Used to represent the directions in the game.
	 * Directions such as North, East, South and West are
	 * absolute Directions, and should be used when referring
	 * to a specific direction, whereas Directions such as
	 * Forward, Back, Left and Right and relative, and should
	 * mainly be used for user input, as they will act
	 * relative to the Player's absolute Direction.
	 */
	public enum Direction {
		NORTH(false),
		EAST(false),
		SOUTH(false),
		WEST(false),
		FORWARD(true),
		BACK(true),
		LEFT(true),
		RIGHT(true);

		private boolean relative;

		private Direction(boolean isRelative) {
			relative = isRelative;
		}

		public boolean isRelative() {
			return relative;
		}
	}

	private ArrayList<Floor> floors;
	private int currentFloor;

	private Random rng = new Random(System.currentTimeMillis());

	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being a random width and depth (each between 10 and 15),
	 * and each Floor having random Room width and depth (between 8 and 10).
	 *
	 * @param numFloors
	 */
	public World(int numFloors) {
		floors = new ArrayList<Floor>();
		currentFloor = 0;

		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					rng.nextInt(6) + 10,	// 10 - 15
					rng.nextInt(6) + 10,
					rng.nextInt(3) + 8,		// 8 - 10
					rng.nextInt(3) + 8));

			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}

	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being the specified width and depth,
	 * but each Floor having a random Room width and depth (between 8 and 10).
	 *
	 * @param numFloors
	 * @param floorWidth
	 * @param floorDepth
	 */
	public World (int numFloors, int floorWidth, int floorDepth) {
		floors = new ArrayList<Floor>();
		currentFloor = 0;

		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					floorWidth,
					floorDepth,
					rng.nextInt(3) + 8,		// 8 - 10
					rng.nextInt(3) + 8));

			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}

	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being the specified width (floorWidth) and depth (floorDepth),
	 * and each Room being the specified width (roomWidth) and depth (roomDepth).
	 *
	 * @param numFloors
	 * @param floorWidth
	 * @param floorDepth
	 * @param roomWidth
	 * @param roomDepth
	 */
	public World(int numFloors, int floorWidth, int floorDepth, int roomWidth, int roomDepth) {
		floors = new ArrayList<Floor>();
		currentFloor = 0;

		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					floorWidth,
					floorDepth,
					roomWidth,
					roomDepth));

			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}

	/**
	 * Returns the Floor that the Players are currently on.
	 *
	 * @return	the current Floor
	 */
	public Floor getCurrentFloor() {
		return floors.get(currentFloor);
	}

	/**
	 * Moves the game to the next Floor.
	 */
	public void goUpFloor() {
		++currentFloor;
	}
}
