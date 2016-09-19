package gameWorld;

import java.util.HashMap;
import gameWorld.World.Direction;

public class Room {
	private Floor floor;
	
	private HashMap<Direction, Room> neighbours;
	
	private Location[][] locations;
	
	private int xPos;
	private int yPos;
	
	private int width;
	private int depth;
	
	/**
	 * Constructs a Room containing a grid of Locations which has
	 * the specified width and depth.
	 * 
	 * @param floor
	 * @param xPos
	 * @param yPos
	 * @param width
	 * @param depth
	 */
	public Room(Floor floor, int xPos, int yPos, int width, int depth) {
		this.floor = floor;
		
		this.neighbours = new HashMap<Direction, Room>();
		
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
	
	/**
	 * Returns the Floor that this Room is situated on.
	 * 
	 * @return	this Room's Floor
	 */
	public Floor floor() {
		return floor;
	}
	
	/**
	 * Returns the Room immediately adjacent to this Room in the
	 * specified Direction.
	 * 
	 * @param direction
	 * @return	the Room in the Direction specified
	 */
	public Room neighbour(Direction direction) {
		return neighbours.get(direction);
	}
	
	/**
	 * Sets the neighbouring Room in the specified Direction to
	 * neighbour.
	 * 
	 * @param direction
	 * @param neighbour
	 */
	public void setNeighbour(Direction direction, Room neighbour) {
		neighbours.put(direction, neighbour);
	}
	
	/**
	 * Returns the grid of Locations as a 2D array of Locations,
	 * with depth x width Locations.
	 * 
	 * @return	the Locations in this Room
	 */
	public Location[][] locations() {
		return locations;
	}
	
	/**
	 * Returns the x position (that is, width-wise) of this Room on this Floor.
	 * 
	 * @return	the x position in the Floor grid
	 */
	public int xPos() {
		return xPos;
	}
	
	/**
	 * Returns the y position (that is, depth-wise) of this Room on this Floor.
	 * 
	 * @return	the y position in the Floor grid
	 */
	public int yPos() {
		return yPos;
	}
	
	/**
	 * Returns the width of this Room, in Locations.
	 * 
	 * @return	the width of this Room
	 */
	public int width() {
		return width;
	}
	
	/**
	 * Returns the depth of this Room, in Locations.
	 * 
	 * @return	the depth of this Room
	 */
	public int depth() {
		return depth;
	}
}
