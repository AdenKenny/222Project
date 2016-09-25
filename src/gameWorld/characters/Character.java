package gameWorld.characters;

import gameWorld.Entity;
import gameWorld.Location;
import gameWorld.World.Direction;

public abstract class Character extends Entity {

	public enum Type {
		MONSTER, 
		VENDOR,
		PLAYER;
	}
	
	public Character(Location location, String name, String description, Direction facing) {
		super(location, name, description, facing);
	}
	
	public void move(Direction dir) {
		location.room().move(this, dir);
	}
	
	public void turn(Direction dir) {
		switch (dir) {
		case NORTH:
			facing = Direction.NORTH;
			break;
		case EAST:
			facing = Direction.EAST;
			break;
		case SOUTH:
			facing = Direction.SOUTH;
			break;
		case WEST:
			facing = Direction.WEST;
			break;
		case LEFT:
			turnLeft();
			break;
		case RIGHT:
			turnRight();
			break;
		default:
			break;
		}
	}
	
	private void turnLeft() {
		switch (facing) {
		case NORTH:
			facing = Direction.WEST;
			break;
		case EAST:
			facing = Direction.NORTH;
			break;
		case SOUTH:
			facing = Direction.EAST;
			break;
		case WEST:
			facing = Direction.SOUTH;
			break;
		default:
			break;
		}
	}
	
	private void turnRight() {
		switch (facing) {
		case NORTH:
			facing = Direction.EAST;
			break;
		case EAST:
			facing = Direction.SOUTH;
			break;
		case SOUTH:
			facing = Direction.WEST;
			break;
		case WEST:
			facing = Direction.NORTH;
			break;
		default:
			break;
		}
	}

}
