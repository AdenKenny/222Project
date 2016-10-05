package gameWorld;

import gameWorld.characters.Character;

public abstract class Action {
	private static int IDCount = 0;
	private int ID;

	public Action() {
		this.ID = IDCount++;
	}

	/**
	 * Returns the unique ID of this Action.
	 * 
	 * @return this Action's ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Returns the name of this Action.
	 * 
	 * @return this Action's name
	 */
	public abstract String name();

	/**
	 * Performs the effects of this Action on this Action's owner. If this
	 * Action is a client-side action, the argument should be of type
	 * MainWindow, otherwise the argument should be the Character of the player
	 * that tried to perform this Action.
	 * 
	 * @param caller
	 *            the Character that called, or the MainWindow of their UI
	 */
	public abstract void perform(Object caller);

	/**
	 * Returns true if this Action can be performed on the client-side, and
	 * false if it has to be sent as a request to the server.
	 * 
	 * @return whether this Action is a client-side action or not
	 */
	public abstract boolean isClientAction();
}
