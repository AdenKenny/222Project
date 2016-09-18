package gameWorld;

public class Room {
	private Floor floor;
	
	private Room north;
	private Room east;
	private Room south;
	private Room west;
	
	private Location[][] locations;
	
	private int xPos;
	private int yPos;
	
	private int width;
	private int depth;
	
	public Room(Floor floor, int xPos, int yPos, int width, int depth) {
		this.floor = floor;
		
		north = null;
		east = null;
		south = null;
		west = null;
		
		this.xPos = xPos;
		this.yPos = yPos;
		
		this.width = width;
		this.depth = depth;
		
		setupLocations();
	}
	
	private void setupLocations() {
		locations = new Location[depth][width];
		
		for (int row = 0; row < depth; row++) {
			for (int col = 0; col < width; col++) {
				locations[row][col] = new Location(this, row, col);
			}
		}
	}
	
	public Floor floor() {
		return floor;
	}
	
	public Room north() {
		return north;
	}
	
	public void setNorth(Room north) {
		this.north = north;
	}
	
	public Room east() {
		return east;
	}
	
	public void setEast(Room east) {
		this.east = east;
	}
	
	public Room south() {
		return south;
	}
	
	public void setSouth(Room south) {
		this.south = south;
	}
	
	public Room west() {
		return west;
	}
	
	public void setWest(Room west) {
		this.west = west;
	}
	
	public Location[][] locations() {
		return locations;
	}
	
	public int xPos() {
		return xPos;
	}
	
	public int yPos() {
		return yPos;
	}
	
	public int width() {
		return width;
	}
	
	public int depth() {
		return depth;
	}
}
