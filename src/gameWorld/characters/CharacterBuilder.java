package gameWorld.characters;

import java.util.HashSet;
import java.util.Set;

import gameWorld.item.Item;
import gameWorld.item.ItemBuilder;
import util.AbstractBuilder;
import util.Logging;

/**
 *
 *
 * @author Aden
 */

public final class CharacterBuilder implements AbstractBuilder {

	private String buildID;
	private String buildName;
	private String buildType;
	private String buildValue;
	private String buildItems;

	private int ID;
	private String name;
	private Character.Type type;
	private int value;
	private Set<Integer> setOfItems;

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

	public void setBuildItems(String buildItems) {

		this.buildItems = buildItems.replace(",", "");

		String[] itemValues = buildItems.split(" ");

		this.setOfItems = new HashSet<>();

		try {

			for(String string : itemValues) {
				int value = Integer.parseInt(string);
				this.setOfItems.add(value);
			}
		}

		catch (NumberFormatException e){
			Logging.logEvent(CharacterBuilder.class.getName(), Logging.Levels.SEVERE, "Failed to build character.");
		}
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Character.Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public Set<Integer> getSetOfItems() {
		return this.setOfItems;
	}

	@Override
	public CharacterModel build() {

		if (this.buildID == null || this.buildName == null || this.buildType == null || this.buildValue == null
				|| this.buildItems == null) {


			try {
				this.ID = Integer.parseInt(this.buildID);
				this.name = this.buildName;
				this.type = Character.Type.valueOf(this.buildType);
				this.value = Integer.parseInt(this.buildValue);
				setBuildItems(this.buildItems);

				return new CharacterModel(this);
			}

			catch (NumberFormatException e) {
				Logging.logEvent(ItemBuilder.class.getName(), Logging.Levels.WARNING,
						"Improperly formatted XML file on item loading.");

			}
		}

		return null;
	}
}
