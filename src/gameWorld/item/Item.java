package gameWorld.item;

import util.Buildable;

public class Item implements Buildable {
	public enum Type {
		WEAPON,
		SHIELD,
		ARMOR,
		HELMET,
		POTION,
		QUEST,
		TRASH
	}

	private int ID;
	private String name;
	private Type type;
	private int value;
	private int saleValue;
	private String description;

	public Item(ItemBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.saleValue = builder.getSaleValue();
		this.description = builder.getDescription();
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public int getSaleValue() {
		return this.saleValue;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
