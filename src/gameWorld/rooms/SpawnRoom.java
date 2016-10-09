package gameWorld.rooms;

/**
 * An interface for Rooms which spawn Characters.
 * 
 * @author Louis
 */
public interface SpawnRoom {
	/**
	 * A method which can be called by the game clock in order to allow this
	 * SpawnRoom to spawn the Characters it is in charge of.
	 */
	public void tick();
}
