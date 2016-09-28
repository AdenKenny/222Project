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
			this.relative = isRelative;
		}

		public byte value() {
			return (byte)ordinal();
		}
		public boolean isRelative() {
			return this.relative;
		}
	}

	private ArrayList<Floor> floors;
	private int currentFloor;

	public World() {
		this.floors = new ArrayList<Floor>();
		this.currentFloor = 0;
	}

	/**
	 * Adds another Floor to this World.
	 *
	 * @param floor	A new Floor
	 */
	public void addFloor(Floor floor) {
		this.floors.add(floor);
	}

	/**
	 * Returns the Floor that the Players are currently on.
	 *
	 * @return	the current Floor
	 */
	public Floor getCurrentFloor() {
		return this.floors.get(this.currentFloor);
	}

	/**
	 * Moves the game to the next Floor.
	 */
	public void goUpFloor() {
		++this.currentFloor;
	}
}