package gameWorld.item;

import util.Logging;

public final class ItemBuilder {
	private String buildID;
	private String buildName;
	private String buildType;
	private String buildValue;
	private String buildSaleValue;

	private int ID;
	private String name;
	private Item.Type type;
	private int value;
	private int saleValue;


	public void setID(String buildID) {
		this.buildID = buildID;
	}

	public void setName(String buildName) {
		this.buildName = buildName;
	}

	public void setType(String buildType) {
		this.buildType = buildType;
	}

	public void setValue(String buildValue) {
		this.buildValue = buildValue;
	}

	public void setSaleValue(String buildSaleValue) {
		this.buildSaleValue = buildSaleValue;
	}

	public int getID() {
		return this.ID;
	}

	public String getName() {
		return this.name;
	}

	public Item.Type getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

	public int getSaleValue() {
		return this.saleValue;
	}

	public Item build() {
		if (this.buildID == null || this.buildName == null || this.buildType == null || this.buildValue == null
				|| this.buildSaleValue == null) {
			return null;
		}
		try {
			this.ID = Integer.parseInt(this.buildID);
			this.name = this.buildName;
			this.type = Item.Type.valueOf(this.buildType);
			this.value = Integer.parseInt(this.buildValue);
			this.saleValue = Integer.parseInt(this.buildSaleValue);

			return new Item(this);
		}

		catch (NumberFormatException e) {
			Logging.logEvent(ItemBuilder.class.getName(), Logging.Levels.WARNING,
					"Improperly formatted XML file on item loading.");
		}

		return null;
	}
}
