package gameWorld.item;

public class ItemBuilder {
	private String ID = null;
	private String name = null;
	private String type = null;
	private String value = null;
	private String saleValue = null;

	public void setID(String iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setSaleValue(String saleValue) {
		this.saleValue = saleValue;
	}

	public Item build() {
		if (ID == null
				|| name == null
				|| type == null
				|| value == null
				|| saleValue == null)
			return null;

		int id = Integer.parseInt(ID);
		Item.Type type = Item.Type.valueOf(this.type);
		int value = Integer.parseInt(this.value);
		int saleValue = Integer.parseInt(this.saleValue);

		return new Item(id, name, type, value, saleValue);
	}
}
