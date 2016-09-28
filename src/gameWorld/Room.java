package gameWorld;

import java.util.HashMap;
import gameWorld.World.Direction;

public class Room {
	protected Floor floor;

	protected HashMap<Direction, Room> neighbours;

	protected Entity[][] entities;

	protected int xPos;
	protected int yPos;

	protected int width;
	protected int depth;

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

		this.entities = new Entity[depth][width];
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
	 * Returns the grid of Entities as a 2D array of Entities,
	 * with depth x width Entities.
	 *
	 * @return	the Entities in this Room
	 */
	public Entity[][] entities() {
		return entities;
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

	public boolean move(Entity entity, Direction dir) {
		if (entity == null || dir == null) return false;
		if (!entity.room().equals(this)) return false;

		int changeX = 0;
		int changeY = 0;

		if (dir.isRelative()) {
			// handling for forward, back, left, right

			Direction facing = entity.facing();

			// first get the change values sorted for when facing North
			switch (dir) {
			case FORWARD:
				changeY = -1;
				break;
			case BACK:
				changeY = 1;
				break;
			case LEFT:
				changeX = -1;
				break;
			case RIGHT:
				changeX = 1;
				break;
			default:
				break;
			}

			// then, adjust them to fit the Direction that the Entity is facing
			switch (facing) {
			case NORTH:	// everything fine
				break;
			case EAST:	// rotate 90 degrees right
				int tempY = changeY;
				changeY = changeX;
				changeX = -tempY;
				break;
			case SOUTH:	// opposite of North, so invert
				changeX = -changeX;
				changeY = -changeY;
				break;
			case WEST:	// rotate 90 degrees left
				int tempX = changeX;
				changeX = changeY;
				changeY = -tempX;
				break;
			default:
				break;
			}
		} else {
			// handling for North, East, South, West

			switch (dir) {
			case NORTH:
				changeY = -1;
				break;
			case EAST:
				changeX = 1;
				break;
			case SOUTH:
				changeY = 1;
				break;
			case WEST:
				changeX = -1;
				break;
			default:
				break;
			}
		}

		int targetX = entity.xPos() + changeX;
		int targetY = entity.yPos() + changeY;

		// check we're not leaving the room
		// TODO: allow moving from room to room
		if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= depth)
			return false;

		//Location target = locations[targetY][targetX];

		if (entities[targetY][targetX] == null) {
			// target location is free, so can move there
			entities[targetY][targetX] = entity;
			entities[entity.yPos()][entity.xPos()] = null;
			entity.setXPos(targetX);
			entity.setYPos(targetY);
			return true;
		}

		return false;
	}
}
