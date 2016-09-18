package gameWorld;

public class Floor {
	private Floor previousFloor;
	private Floor nextFloor;
	
	private Room[][] rooms;
	
	private int width;
	private int depth;
	
	private int level;
	
	public Floor(Floor previousFloor, int level, int width, int depth, int roomWidth, int roomDepth) {
		this.previousFloor = previousFloor;
		
		this.level = level;
		
		this.width = width;
		this.depth = depth;
		
		setupRooms(roomWidth, roomDepth);
	}
	
	private void setupRooms(int roomWidth, int roomDepth) {
		rooms = new Room[depth][width];
		
		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				rooms[row][col] = new Room(this, row, col, roomWidth, roomDepth);
			}
		}
		
		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				if (row > 0) rooms[row][col].setNorth(rooms[row-1][col]);
				if (row < depth - 1) rooms[row][col].setSouth(rooms[row+1][col]);
				if (col > 0) rooms[row][col].setWest(rooms[row][col-1]);
				if (col < width - 1) rooms[row][col].setEast(rooms[row][col+1]);
			}
		}
	}
	
	public Floor previousFloor() {
		return previousFloor;
	}
	
	public Floor nextFloor() {
		return nextFloor;
	}
	
	public void setNextFloor(Floor nextFloor) {
		this.nextFloor = nextFloor;
	}
	
	public Room[][] rooms() {
		return rooms;
	}
	
	public int width() {
		return width;
	}
	
	public int depth() {
		return depth;
	}
	
	public int level() {
		return level;
	}
	
}
