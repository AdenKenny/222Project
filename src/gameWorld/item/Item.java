package gameWorld.item;

public class Item {
	public enum Type {
		WEAPON,
		SHIELD,
		ARMOR,
		HELMET,
		POTION,
		QUEST,
		TRASH
	}

	private int id;

	public Item(int id, String name, Type type, int value, int saleValue) {

	}

}
