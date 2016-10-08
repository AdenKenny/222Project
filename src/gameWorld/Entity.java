package gameWorld;

import java.util.ArrayList;
import java.util.List;

import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;
import ui.appwindow.MainWindow;

/**
 * A class which represents anything that physically exists in the game's world.
 * 
 * @author Louis
 */
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
	 * @param name
	 * @param description
	 * @param facing
	 */
	public Entity(Room room, int xPos, int yPos, String name, String description, Direction facing) {
		this.ID = getNewID();

		this.actions = new ArrayList<>();

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
					util.Logging.logEvent("Entity", util.Logging.Levels.WARNING,
							"Entity action 'Inspect' expected MainWindow argument, got " + caller.getClass().getName()
									+ " argument.");
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

				mw.addGameChat(friendlyName + ": " + description + "\n");
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

	/**
	 * Gets the ID of this Entity's Room.
	 * 
	 * @return This Entity's Room's ID.
	 */
	public int getRoomID() {
		return this.room.getID();
	}

	/**
	 * Returns the Room of this Entity.
	 * 
	 * @return This Entity's Room.
	 */
	public Room room() {
		return this.room;
	}

	/**
	 * Sets this Entity's Room to the specified Room.
	 * 
	 * @param room
	 *            The new Room for this Entity.
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * Returns this Entity's current x position. That is, this Entity's position
	 * along the x-axis of its Room.
	 * 
	 * @return this Entity's x position
	 */
	public int xPos() {
		return this.xPos;
	}

	/**
	 * Sets this Entity's x position to the specified value.
	 * 
	 * @param xPos
	 *            the new position
	 */
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * Returns this Entity's current y position. That is, this Entity's position
	 * along the y-axis of its Room.
	 * 
	 * @return this Entity's y position
	 */
	public int yPos() {
		return this.yPos;
	}

	/**
	 * Sets this Entity's y position to the specified value.
	 * 
	 * @param yPos
	 *            the new position
	 */
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	/**
	 * Returns the name of this Entity.
	 *
	 * @return the name
	 */
	public String name() {
		return this.name;
	}

	/**
	 * Returns the description of this Entity.
	 *
	 * @return the description
	 */
	public String description() {
		return this.description;
	}

	/**
	 * Returns the Direction this Entity is facing.
	 *
	 * @return the Direction
	 */
	public Direction facing() {
		return this.facing;
	}

	/**
	 * Returns the unique ID of this Entity.
	 *
	 * @return a unique ID
	 */
	public int ID() {
		return this.ID;
	}

	/**
	 * Returns the actions that may be performed on this Entity.
	 *
	 * @return the List of actions
	 */
	public List<Action> actions() {
		return this.actions;
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

		for (Action a : this.actions) {
			if (a.name().equals(action)) {
				a.perform(caller);
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.ID;
		result = prime * result + ((this.actions == null) ? 0 : this.actions.hashCode());
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + ((this.facing == null) ? 0 : this.facing.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.room == null) ? 0 : this.room.hashCode());
		result = prime * result + this.xPos;
		result = prime * result + this.yPos;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (this.ID != other.ID)
			return false;
		if (this.actions == null) {
			if (other.actions != null)
				return false;
		} else if (!this.actions.equals(other.actions))
			return false;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.facing != other.facing)
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.room == null) {
			if (other.room != null)
				return false;
		} else if (!this.room.equals(other.room))
			return false;
		if (this.xPos != other.xPos)
			return false;
		if (this.yPos != other.yPos)
			return false;
		return true;
	}

	/**
	 * Returns true if this Entity is a Player, false otherwise.
	 *
	 * @return whether this Entity is a Player
	 */
	public abstract boolean isPlayer();

}
