package gameWorld;

import java.util.List;

import gameWorld.World.Direction;
import gameWorld.characters.Character;

public abstract class Entity {
	private static int IDCount = 0;

	protected int ID;

	protected Room room;
	protected int xPos;
	protected int yPos;

	protected String name;
	protected String description;

	protected Direction facing;

	protected List<Action> actions;

	/**
	 * Constructs an Entity at the given Location,
	 * with the given name and description, facing in
	 * the given Direction.
	 *
	 * @param location
	 * @param actions
	 * @param name
	 * @param description
	 * @param facing
	 */
	public Entity(Room room, int xPos, int yPos, String name, String description, Direction facing) {
		this.ID = IDCount++;

		this.room = room;
		this.xPos = xPos;
		this.yPos = yPos;

		this.name = name;
		this.description = name;

		this.facing = (facing == null) ? Direction.NORTH : facing;
	}

	public Room room() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int xPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public int yPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	/**
	 * Returns the name of this Entity.
	 *
	 * @return	the name
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the description of this Entity.
	 *
	 * @return	the description
	 */
	public String description() {
		return description;
	}

	/**
	 * Returns the Direction this Entity is facing.
	 *
	 * @return	the Direction
	 */
	public Direction facing() {
		return facing;
	}

	/**
	 * Returns the unique ID of this Entity.
	 *
	 * @return	a unique ID
	 */
	public int ID() {
		return ID;
	}

	/**
	 * Returns the actions that may be performed on this
	 * Entity.
	 *
	 * @return	the List of actions
	 */
	public List<Action> actions() {
		return actions;
	}

	/**
	 * Attempts to perform the specified action on this Entity.
	 * Will fail if the specified action cannot be found in the
	 * List of actions that may be performed on this Entity.
	 *
	 * @param action
	 * @return	whether the action succeeded or not
	 */
	public boolean performAction(String action, Character caller) {

		for (Action a : actions) {
			if (a.name().equals(action)) {
				a.perform(caller);
				return true;
			}
		}
		return false;
	}
}
