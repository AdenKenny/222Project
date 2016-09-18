package gameWorld;

import java.util.List;

public abstract class Entity {
	private Location location;
	
	private List<String> actions;
	
	private String name;
	private String description;
	
	public Entity(Location location, List<String> actions, String name, String description) {
		this.location = location;
		
		this.actions = actions;
		
		this.name = name;
		this.description = name;
	}
	
	public Location location() {
		return location;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	public List<String> actions() {
		return actions;
	}
	
	public abstract boolean performAction(String action);
}
