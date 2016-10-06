package gameWorld;

import java.util.ArrayList;
import java.util.List;

import gameWorld.World.Direction;
import gameWorld.characters.Character;
import ui.appwindow.MainWindow;

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
	 * Constructs an Entity at the given Location, with the given name and
	 * description, facing in the given Direction.
	 *
	 * @param location
	 * @param actions
	 * @param name
	 * @param description
	 * @param facing
	 */
	public Entity(Room room, int xPos, int yPos, String name, String description, Direction facing) {
		this.ID = getNewID();

		this.actions = new ArrayList<Action>();

		this.room = room;
		this.xPos = xPos;
		this.yPos = yPos;

		this.name = name;
		this.description = name;

		this.facing = (facing == null) ? Direction.NORTH : facing;

		this.actions.add(new Action() {

			@Override
			public String name() {
				return "Inspect";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof MainWindow)) {
					return;
				}

				MainWindow mw = (MainWindow) caller;

				String friendlyName = name;

				if (!isPlayer()) {
					friendlyName = java.lang.Character.toUpperCase(friendlyName.charAt(0)) + friendlyName.substring(1);

					for (int i = 1; i < friendlyName.length(); ++i) {
						if (java.lang.Character.isUpperCase(friendlyName.charAt(i))) {
							friendlyName = friendlyName.substring(0, i) + " " + friendlyName.substring(i);
							++i;
						}
					}
				}

				mw.addGameChat(friendlyName+": "+description+"\n");
			}

			@Override
			public boolean isClientAction() {
				return true;
			}
		});
	}

	/**
	 * Gets a new ID for an object.
	 *
	 * @return The ID of an object.
	 */

	public static synchronized int getNewID() {
		return Entity.IDCount++;
	}

	public int getRoomID() {
		return this.room.getID();
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
	 * @return the name
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the description of this Entity.
	 *
	 * @return the description
	 */
	public String description() {
		return description;
	}

	/**
	 * Returns the Direction this Entity is facing.
	 *
	 * @return the Direction
	 */
	public Direction facing() {
		return facing;
	}

	/**
	 * Returns the unique ID of this Entity.
	 *
	 * @return a unique ID
	 */
	public int ID() {
		return ID;
	}

	/**
	 * Returns the actions that may be performed on this Entity.
	 *
	 * @return the List of actions
	 */
	public List<Action> actions() {
		return actions;
	}

	/**
	 * Attempts to perform the specified action on this Entity. Will fail if the
	 * specified action cannot be found in the List of actions that may be
	 * performed on this Entity.
	 *
	 * @param action
	 * @return whether the action succeeded or not
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

	/**
	 * Returns true if this Entity is a Player
	 *
	 * @return	true if this is a Player
	 */
	public abstract boolean isPlayer();
}
