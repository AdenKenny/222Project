package gameWorld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import gameWorld.World.Direction;
import gameWorld.rooms.RoomBuilder;

public class Room {

	protected Floor floor;

	protected HashMap<Direction, Room> neighbours;

	protected Entity[][] entities;

	protected int xPos;
	protected int yPos;

	protected int ID;

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

		this.ID = Entity.getNewID();

		this.floor = floor;

		this.neighbours = new HashMap<Direction, Room>();

		this.xPos = xPos;
		this.yPos = yPos;

		this.width = width;
		this.depth = depth;

		this.entities = new Entity[depth][width];
	}

	public Room(Floor floor, RoomBuilder builder) {
		this.floor = floor;

		this.ID = Entity.getNewID();

		this.neighbours = new HashMap<Direction, Room>();

		this.xPos = builder.getxPos();
		this.yPos = builder.getyPos();

		this.width = builder.getWidth();
		this.depth = builder.getDepth();

		this.entities = new Entity[this.depth][this.width];
	}

	/**
	 * Returns the Floor that this Room is situated on.
	 *
	 * @return	this Room's Floor
	 */
	public Floor floor() {
		return this.floor;
	}

	/**
	 * Returns the Room immediately adjacent to this Room in the
	 * specified Direction.
	 *
	 * @param direction
	 * @return	the Room in the Direction specified
	 */
	public Room neighbour(Direction direction) {
		return this.neighbours.get(direction);
	}

	/**
	 * Sets the neighbouring Room in the specified Direction to
	 * neighbour.
	 *
	 * @param direction
	 * @param neighbour
	 */
	public void setNeighbour(Direction direction, Room neighbour) {
		this.neighbours.put(direction, neighbour);
	}

	/**
	 * Returns the grid of Entities as a 2D array of Entities,
	 * with depth x width Entities.
	 *
	 * @return	the Entities in this Room
	 */
	public Entity[][] entities() {
		return this.entities;
	}

	public Set<Sendable> getSendables() {
		Set<Sendable> sendables = new HashSet<Sendable>();
		for (Entity[] es : entities) {
			for (Entity e : es) {
				if (e instanceof Sendable) {
					sendables.add((Sendable) e);
				}
			}
		}
		return sendables;
	}

	/**
	 * Returns the x position (that is, width-wise) of this Room on this Floor.
	 *
	 * @return	the x position in the Floor grid
	 */
	public int xPos() {
		return this.xPos;
	}

	/**
	 * Returns the y position (that is, depth-wise) of this Room on this Floor.
	 *
	 * @return	the y position in the Floor grid
	 */
	public int yPos() {
		return this.yPos;
	}

	/**
	 * Returns the width of this Room, in Locations.
	 *
	 * @return	the width of this Room
	 */
	public int width() {
		return this.width;
	}

	public int getID() {
		return this.ID;
	}

	/**
	 * Returns the depth of this Room, in Locations.
	 *
	 * @return	the depth of this Room
	 */
	public int depth() {
		return this.depth;
	}

	public boolean move(Entity entity, Direction dir) {
		if (entity == null || entity.room() == null || dir == null) return false;
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

		if (targetX < 0) {
			Room targetRoom = this.neighbours.get(Direction.WEST);
			if (targetRoom != null) {
				if (targetRoom.entities[yPos][targetRoom.width-1] == null) {
					targetRoom.entities[yPos][targetRoom.width-1] = entity;
				}
			}
			return false;
		} else if (targetX >= this.width) {
			Room targetRoom = this.neighbours.get(Direction.EAST);
			if (targetRoom != null) {
				if (targetRoom.entities[yPos][0] == null) {
					targetRoom.entities[yPos][0] = entity;
				}
			}
			return false;
		} else if (targetY < 0) {
			Room targetRoom = this.neighbours.get(Direction.NORTH);
			if (targetRoom != null) {
				if (targetRoom.entities[targetRoom.depth-1][xPos] == null) {
					targetRoom.entities[targetRoom.depth-1][xPos] = entity;
				}
			}
			return false;
		} else if (targetY >= this.depth){
			Room targetRoom = this.neighbours.get(Direction.SOUTH);
			if (targetRoom != null) {
				if (targetRoom.entities[0][xPos] == null) {
					targetRoom.entities[0][xPos] = entity;
					return true;
				}
			}
			return false;
		}

		if (this.entities[targetY][targetX] == null) {
			// target location is free, so can move there
			this.entities[targetY][targetX] = entity;
			this.entities[entity.yPos()][entity.xPos()] = null;
			entity.setXPos(targetX);
			entity.setYPos(targetY);
			return true;
		}

		return false;
	}
}
