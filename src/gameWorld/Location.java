package gameWorld;

public class Location {
	private Room room;
	
	private Entity entity;
	
	private int xPos, yPos;
	
	public Location(Room room, int xPos, int yPos) {
		this.room = room;
		
		this.entity = null;
		
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Location(Room room, Entity entity, int xPos, int yPos) {
		this.room = room;
		
		this.entity = entity;
		
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Room room() {
		return room;
	}
	
	public Entity entity() {
		return entity;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public int xPos() {
		return xPos;
	}
	
	public int yPos() {
		return yPos;
	}
}
