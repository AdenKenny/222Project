package gameWorld;

import java.util.ArrayList;

import clientServer.ServerSideGame;
import gameWorld.characters.Character;

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
		RIGHT(true),
		NONE(false);

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

		public Direction getLeft() {
			if (this.equals(NORTH)) {
				return WEST;
			} else if (this.equals(EAST)) {
				return NORTH;
			} else if (this.equals(SOUTH)) {
				return EAST;
			} else if (this.equals(WEST)) {
				return SOUTH;
			}
			return null;
		}

		public Direction getRight() {
			if (this.equals(NORTH)) {
				return EAST;
			} else if (this.equals(EAST)) {
				return SOUTH;
			} else if (this.equals(SOUTH)) {
				return WEST;
			} else if (this.equals(WEST)) {
				return NORTH;
			}
			return null;
		}
	}

	private ArrayList<Floor> floors;
	private int currentFloor;

	public World() {
		this.floors = new ArrayList<>();
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
		// Slay all players so that they get respawned in the new Floor's spawn room.
		for (Character c : ServerSideGame.getAllPlayers().values()) {
			c.slay();
		}
	}
}