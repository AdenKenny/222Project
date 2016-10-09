package gameWorld.item;

import util.AbstractBuilder;
import util.Logging;

/**
 * A class to build an Item.
 *
 * @author Aden and Louis
 */
public final class ItemBuilder implements AbstractBuilder {
	private String buildID;
	private String buildName;
	private String buildType;
	private String buildValue;
	private String buildSaleValue;
	private String buildDescription;

	private int ID;
	private String name;
	private Item.Type type;
	private int value;
	private int saleValue;
	private String description;

	@Override
	public void setID(String buildID) {
		this.buildID = buildID;
	}

	@Override
	public void setName(String buildName) {
		this.buildName = buildName;
	}

	@Override
	public void setType(String buildType) {
		this.buildType = buildType;
	}

	@Override
	public void setValue(String buildValue) {
		this.buildValue = buildValue;
	}

	/**
	 * Sets the sale value of the Item that is being built. This value will be
	 * read from a file.
	 * 
	 * @param buildSaleValue
	 *            The sale value of the Item being built.
	 */
	public void setSaleValue(String buildSaleValue) {
		this.buildSaleValue = buildSaleValue;
	}

	@Override
	public void setDescription(String description) {
		this.buildDescription = description;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the Type of the Item that is being built.
	 * 
	 * @return The Type of the Item being built.
	 */
	public Item.Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns the sale value of the Item that is being built.
	 * 
	 * @return The sale value of the Item being built.
	 */
	public int getSaleValue() {
		return this.saleValue;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Item build() {
		if (this.buildID == null || this.buildName == null || this.buildType == null || this.buildValue == null
				|| this.buildSaleValue == null || this.buildDescription == null) {
			return null;
		}
		try {
			this.ID = Integer.parseInt(this.buildID);
			this.name = this.buildName;
			this.type = Item.Type.valueOf(this.buildType);
			this.value = Integer.parseInt(this.buildValue);
			this.saleValue = Integer.parseInt(this.buildSaleValue);
			this.description = this.buildDescription;

			return new Item(this);
		}

		catch (NumberFormatException e) {
			Logging.logEvent(ItemBuilder.class.getName(), Logging.Levels.WARNING,
					"Improperly formatted XML file on item loading.");
		}

		return null;
	}

	@Override
	public void setItems(String s) {
		throw new AssertionError(); // This shouldn't ever be called.
	}
}
