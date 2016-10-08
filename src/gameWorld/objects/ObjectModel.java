package gameWorld.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;

public class ObjectModel implements Buildable {
	
	private String name;
	private int ID;
	private int value;
	private String description;
	private Type type;
	private Set<Integer> items;
	
	public ObjectModel(ObjectBuilder builder) {
		this.name = builder.getName();
		this.ID = builder.getID();
		this.value = builder.getValue();
		this.description = builder.getDescription();
		this.type = builder.getType();
		this.items = builder.getSetOfItems();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Set<Integer> getItems() {
		return this.items;
	}
	
	public void setItems(List<Integer> items) {
		this.items = new HashSet<Integer>();
		for (Integer i : items) {
			this.items.add(i);
		}
	}

}
