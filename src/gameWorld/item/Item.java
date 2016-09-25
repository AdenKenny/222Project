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

	public Item(ItemBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.saleValue = builder.getSaleValue();
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getSaleValue() {
		return this.saleValue;
	}

	public void setSaleValue(int saleValue) {
		this.saleValue = saleValue;
	}



}
