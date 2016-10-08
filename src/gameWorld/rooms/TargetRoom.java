package gameWorld.rooms;

import gameWorld.Entity;
import gameWorld.Floor;

public class TargetRoom extends Room {
	
	private boolean reached;
	private long reachedTime;

	public TargetRoom(Floor floor, RoomBuilder builder) {
		super(floor, builder);
		this.floor.setTargetRoom(this);
		this.reached = false;
	}
	
	public boolean hasBeenReached() {
		return this.reached;
	}
	
	public void tick() {
		if (this.reached) {
			// give other players 10 seconds to reach this room 
			if (reachedTime + 10000 <= System.currentTimeMillis()) {
				this.floor.finishFloor();
			}
		}
		
		// If there are any players in this room, return true
		for (Entity[] es : entities) {
			for (Entity e : es) {
				if (e.isPlayer()) {
					this.reached = true;
					this.reachedTime = System.currentTimeMillis();
				}
			}
		}
	}

}
