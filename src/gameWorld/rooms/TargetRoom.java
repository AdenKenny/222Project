package gameWorld.rooms;

import gameWorld.Entity;
import gameWorld.Floor;

/**
 * A class which represents Rooms that act as goals for each Floor. Once a
 * player finds their way to the TargetRoom of a Floor, the game moves all
 * players to the next Floor (after ca. 10 seconds).
 * 
 * @author Louis
 */
public class TargetRoom extends Room {

	private boolean reached;
	private long reachedTime;

	public TargetRoom(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		this.floor.setTargetRoom(this);
		this.reached = false;
	}

	/**
	 * Checks whether this TargetRoom has been reached by a player yet.
	 * 
	 * @return true if a player has reached this TargetRoom, false otherwise
	 */
	public boolean hasBeenReached() {
		return this.reached;
	}

	/**
	 * A method to allow this Room to check whether it has been reached, and if
	 * it has, start a timer to move all the players to the next Floor.
	 */
	public void tick() {
		if (this.reached) {
			// give other players 10 seconds to reach this room
			if (reachedTime + 10000 <= System.currentTimeMillis()) {
				this.floor.finishFloor();
			}
			return;
		}

		// If there are any players in this room, return true
		for (Entity[] es : entities) {
			for (Entity e : es) {
				if (e != null && e.isPlayer()) {
					this.reached = true;
					this.reachedTime = System.currentTimeMillis();
				}
			}
		}
	}

}
