package gameWorld.objects;

import java.util.Set;

import gameWorld.objects.StationaryObject.Type;
import util.Buildable;



public class ChestModel implements Buildable {

	private String name;
	private int ID;
	private int value;
	private String description;
	private Type type;
	private Set<Integer> items;

	
	public ChestModel(ObjectBuilder builder, Set<Integer> items) {
		this.name = builder.getName();
		this.ID = builder.getID();
		this.value = builder.getValue();
		this.description = builder.getDescription();
		this.type = builder.getType();
		this.items = builder.getSetOfItems();

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
