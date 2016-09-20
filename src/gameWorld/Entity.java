package gameWorld;

import java.util.List;

import gameWorld.World.Direction;

public abstract class Entity {
	protected Location location;
	
	protected String name;
	protected String description;
	
	protected Direction facing;
	
	/**
	 * Constructs an Entity at the given Location,
	 * with the given name and description, facing in
	 * the given Direction.
	 * 
	 * @param location
	 * @param actions
	 * @param name
	 * @param description
	 * @param facing
	 */
	public Entity(Location location, String name, String description, Direction facing) {
		this.location = location;
		
		this.name = name;
		this.description = name;
		
		this.facing = (facing == null) ? Direction.NORTH : facing;
	}
	
	/**
	 * Returns the Location of this Entity.
	 * 
	 * @return	the current Location
	 */
	public Location location() {
		return location;
	}
	
	/**
	 * Sets this Entity's Location to location.
	 * 
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Returns the name of this Entity.
	 * 
	 * @return	the name
	 */
	public String name() {
		return name;
	}
	
	/**
	 * Returns the description of this Entity.
	 * 
	 * @return	the description
	 */
	public String description() {
		return description;
	}
	
	/**
	 * Returns the Direction this Entity is facing.
	 * 
	 * @return	the Direction
	 */
	public Direction facing() {
		return facing;
	}
	
	/**
	 * Returns the actions that may be performed on this
	 * Entity.
	 * 
	 * @return	the List of actions
	 */
	public abstract List<String> actions();
	
	/**
	 * Attempts to perform the specified action on this Entity.
	 * Will fail if the specified action cannot be found in the
	 * List of actions that may be performed on this Entity.
	 * 
	 * @param action
	 * @return	whether the action succeeded or not
	 */
	public abstract boolean performAction(String action);
	
	protected abstract String methodToAction(String methodName);
	
	protected abstract String actionToMethod(String action);
}
