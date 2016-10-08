package gameWorld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import clientServer.ServerSideGame;
import gameWorld.characters.Character;

/**
 * A class used to represent the world in which the game takes place. It
 * contains a number of Floors
 * 
 * @author Louis
 */
public class World {
	/**
	 * An enumeration used to represent the directions in the game. Directions
	 * such as North, East, South and West are absolute Directions, and should
	 * be used when referring to a specific direction, whereas Directions such
	 * as Forward, Back, Left and Right and relative, and should mainly be used
	 * for user input, as they will act relative to the Player's absolute
	 * Direction.
	 * 
	 * @author Louis
	 */
	public enum Direction {
		NORTH(false), EAST(false), SOUTH(false), WEST(false), FORWARD(true), BACK(true), LEFT(true), RIGHT(true), NONE(
				false);

		private boolean relative;

		private Direction(boolean isRelative) {
			this.relative = isRelative;
		}

		/**
		 * Returns the byte value of this Direction.
		 * 
		 * @return this Direction as a byte
		 */
		public byte value() {
			return (byte) ordinal();
		}

		/**
		 * Checks whether this Direction is relative to the player or is
		 * absolute. Relative Directions will include Directions like FORWARD,
		 * BACK, LEFT and RIGHT, while absolute Directions will include
		 * Directions like NORTH, EAST, SOUTH and WEST.
		 * 
		 * @return whether this Direction is relative
		 */
		public boolean isRelative() {
			return this.relative;
		}

		/**
		 * Gets the Direction to the left of this Direction. This only works
		 * when called on an absolute Direction, otherwise it will return null.
		 * 
		 * @return the Direction to the left of this one
		 */
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

		/**
		 * Gets the Direction to the right of this Direction. This only works
		 * when called on an absolute Direction, otherwise it will return null.
		 * 
		 * @return the Direction to the right of this one
		 */
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

	private Set<Character> players;

	public World() {
		this.floors = new ArrayList<>();
		this.currentFloor = 0;

		this.players = new HashSet<Character>();
	}

	/**
	 * Adds another Floor to this World.
	 *
	 * @param floor
	 *            A new Floor
	 */
	public void addFloor(Floor floor) {
		this.floors.add(floor);
	}

	/**
	 * Returns the Floor that the Players are currently on.
	 *
	 * @return the current Floor
	 */
	public Floor getCurrentFloor() {
		return this.floors.get(this.currentFloor);
	}

	/**
	 * Moves the game to the next Floor.
	 */
	public void goUpFloor() {
		++this.currentFloor;
		// Slay all players so that they get respawned in the new Floor's spawn
		// room.
		for (Character c : players) {
			c.slay();
		}
	}

	/**
	 * Adds a Character to the World, meaning that they will from then on be
	 * part of the game.
	 * 
	 * @param c
	 *            the Character to add
	 */
	public void addPlayer(Character c) {
		this.players.add(c);
	}

	/**
	 * Removes a Character from the World, taking them out of the game.
	 * 
	 * @param c
	 *            the Character to remove
	 */
	public void removePlayer(Character c) {
		this.players.remove(c);
	}

	/**
	 * Returns the Set of Characters that are currently a part of the game.
	 * 
	 * @return a Set of the Characters in the game
	 */
	public Set<Character> getPlayers() {
		return this.players;
	}
}
