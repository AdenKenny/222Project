package gameWorld.rooms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import gameWorld.Entity;
import gameWorld.Floor;
import gameWorld.Sendable;
import gameWorld.World.Direction;

import gameWorld.characters.Character;

/**
 * A class to represent a Room within a Floor. Each Room is made up of a grid of Entities.
 *
 * @author Louis
 */
public class Room {
	protected Floor floor;
	protected HashMap<Direction, Room> neighbours;
	protected HashMap<Direction, Boolean> doors;
	protected Entity[][] entities;
	protected int xPos;
	protected int yPos;
	protected int ID;
	protected int width;
	protected int depth;

	public Room(Floor floor, int xPos, int yPos, int width, int depth) {
		this.ID = Entity.getNewID();
		this.floor = floor;
		this.neighbours = new HashMap<>();
		this.doors = new HashMap<>();
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.depth = depth;
		this.entities = new Entity[depth][width];
	}

	public Room(Floor floor, RoomBuilder builder) {
		this.floor = floor;
		this.ID = Entity.getNewID();
		this.neighbours = new HashMap<>();
		this.doors = new HashMap<>();
		this.xPos = builder.getxPos();
		this.yPos = builder.getyPos();
		this.width = builder.getWidth();
		this.depth = builder.getDepth();
		this.entities = new Entity[this.depth][this.width];
	}

	/**
	 * Returns the Floor that this Room is situated on.
	 *
	 * @return this Room's Floor
	 */
	public Floor floor() {
		return this.floor;
	}

	/**
	 * Returns the Room immediately adjacent to this Room in the specified Direction.
	 *
	 * @param direction
	 * @return the Room in the Direction specified
	 */
	public Room neighbour(Direction direction) {
		return this.neighbours.get(direction);
	}

	/**
	 * Checks whether this Room has a door on the wall in the given Direction.
	 *
	 * @param direction
	 *            The Direction to check
	 * @return Whether there is a door on the specified wall
	 */
	public boolean hasDoor(Direction direction) {
		return this.doors.get(direction);
	}

	/**
	 * Sets the neighbouring Room in the specified Direction to neighbour.
	 *
	 * @param direction
	 * @param neighbour
	 */
	public void setNeighbour(Direction direction, Room neighbour) {
		this.neighbours.put(direction, neighbour);
		this.doors.put(direction, (Boolean) (neighbour != null));
	}

	/**
	 * Sets whether this Room has a door on the wall in the given Direction or not.
	 *
	 * @param direction
	 *            The Direction of the wall
	 * @param hasDoor
	 *            Whether the wall has a door or not
	 */
	public void setDoor(Direction direction, boolean hasDoor) {
		this.doors.put(direction, (Boolean) hasDoor);
	}

	/**
	 * Returns the grid of Entities as a 2D array of Entities, with depth x width Entities.
	 *
	 * @return the Entities in this Room
	 */
	public Entity[][] entities() {
		return this.entities;
	}

	/**
	 * Returns all the Sendables contained within this Room.
	 *
	 * @return The Sendables in this Room.
	 */
	public Set<Sendable> getSendables() {
		Set<Sendable> sendables = new HashSet<>();
		for (Entity[] es : this.entities) {
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
	 * @return the x position in the Floor grid
	 */
	public int xPos() {
		return this.xPos;
	}

	/**
	 * Returns the y position (that is, depth-wise) of this Room on this Floor.
	 *
	 * @return the y position in the Floor grid
	 */
	public int yPos() {
		return this.yPos;
	}

	/**
	 * Returns the width of this Room, in Locations.
	 *
	 * @return the width of this Room
	 */
	public int width() {
		return this.width;
	}

	/**
	 * Returns the ID of this Room.
	 *
	 * @return This Room's ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Returns the depth of this Room, in Locations.
	 *
	 * @return the depth of this Room
	 */
	public int depth() {
		return this.depth;
	}

	/**
	 * Attempts to move the given Entity in the given Direction. If the given Direction is relative, moves the Entity relative to the
	 * Direction it is currently facing, otherwise moves the Entity in the absolute Direction that is specified. Returns true if the move
	 * succeeds, false otherwise.
	 *
	 * @param entity
	 *            The Entity to move
	 * @param dir
	 *            The Direction to move in
	 * @return Whether the move succeeded or not
	 */
	public boolean move(Entity entity, Direction dir) {
		if (entity == null || entity.room() == null || dir == null)
			return false;
		if (!entity.room().equals(this))
			return false;
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
			case NORTH: // everything fine
				break;
			case EAST: // rotate 90 degrees right
				int tempY = changeY;
				changeY = changeX;
				changeX = -tempY;
				break;
			case SOUTH: // opposite of North, so invert
				changeX = -changeX;
				changeY = -changeY;
				break;
			case WEST: // rotate 90 degrees left
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
		// movement between rooms
		if (targetX < 0) {
			if (targetY == this.depth / 2) {
				Room targetRoom = this.neighbours.get(Direction.WEST);

				if (targetRoom != null) {
					if (targetRoom.entities[targetY][targetRoom.width - 1] == null) {
						targetRoom.entities[targetY][targetRoom.width - 1] = entity;
						this.entities[entity.yPos()][entity.xPos()] = null;
						entity.setRoom(targetRoom);
						entity.setXPos(targetRoom.width - 1);
						return true;
					}
				}
			}
			return false;
		} else if (targetX >= this.width) {
			if (targetY == this.depth / 2) {
				Room targetRoom = this.neighbours.get(Direction.EAST);

				if (targetRoom != null) {
					if (targetRoom.entities[targetY][0] == null) {
						targetRoom.entities[targetY][0] = entity;
						this.entities[entity.yPos()][entity.xPos()] = null;
						entity.setRoom(targetRoom);
						entity.setXPos(0);
						return true;
					}
				}
			}
			return false;
		} else if (targetY < 0) {
			if (targetX == this.width / 2) {

				Room targetRoom = this.neighbours.get(Direction.NORTH);

				if (targetRoom != null) {
					if (targetRoom.entities[targetRoom.depth - 1][targetX] == null) {
						targetRoom.entities[targetRoom.depth - 1][targetX] = entity;
						this.entities[entity.yPos()][entity.xPos()] = null;
						entity.setRoom(targetRoom);
						entity.setYPos(targetRoom.depth - 1);
						return true;
					}
				}
			}
			return false;
		} else if (targetY >= this.depth) {
			if (targetX == this.width / 2) {

				Room targetRoom = this.neighbours.get(Direction.SOUTH);

				if (targetRoom != null) {
					if (targetRoom.entities[0][targetX] == null) {
						targetRoom.entities[0][targetX] = entity;
						this.entities[entity.yPos()][entity.xPos()] = null;
						entity.setRoom(targetRoom);
						entity.setYPos(0);
						return true;
					}
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