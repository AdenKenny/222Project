package gameWorld;

public class Location {
	private Room room;
	
	private Entity entity;
	
	private int xPos, yPos;
	
	/**
	 * Constructs a new Location in the specified Room
	 * at the specified co-ordinates in that Room.
	 * 
	 * @param room
	 * @param xPos
	 * @param yPos
	 */
	public Location(Room room, int xPos, int yPos) {
		this.room = room;
		
		this.entity = null;
		
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	/**
	 * Constructs a new Location in the specified Room
	 * at the specified co-ordinates in that Room, containing
	 * the specified Entity.
	 * 
	 * @param room
	 * @param entity
	 * @param xPos
	 * @param yPos
	 */
	public Location(Room room, Entity entity, int xPos, int yPos) {
		this.room = room;
		
		this.entity = entity;
		
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	/**
	 * Returns the Room that this Location is in.
	 * 
	 * @return	this Location's Room
	 */
	public Room room() {
		return room;
	}
	
	/**
	 * Returns the Entity contained in this Location.
	 * 
	 * @return	the Entity in this Location
	 */
	public Entity entity() {
		return entity;
	}
	
	/**
	 * Sets the Entity in this Location to entity.
	 * 
	 * @param entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Returns the x position (that is, width-wise) of this Location
	 * in the Room.
	 * 
	 * @return	the x position in the Room grid
	 */
	public int xPos() {
		return xPos;
	}
	
	/**
	 * Returns the y position (that is, depth-wise) of this Location
	 * in the Room.
	 * 
	 * @return	the y position in the Room grid
	 */
	public int yPos() {
		return yPos;
	}
}
