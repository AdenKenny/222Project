package gameWorld.characters;

import java.util.ArrayList;
import java.util.List;

import gameWorld.characters.Character.Type;
import gameWorld.item.ItemBuilder;
import util.AbstractBuilder;
import util.Buildable;
import util.Logging;

public final class PlayerBuilder implements AbstractBuilder {

	private String buildUsername;
	private String buildID;
	private String buildType;
	private String buildItems;
	private String buildHealth;
	private String buildXp;
	private String buildGold;
	private String buildLevel;
	private String buildEquips;

	private String username;
	private int ID;
	private Type type;
	private List<Integer> items;
	private int health;
	private int xp;
	private int gold;
	private int level;
	private String description = "A player, just like you!";
	private List<Integer> equips;

	@Override
	public void setID(String ID) {
		this.buildID = ID;
	}

	@Override
	public void setName(String name) {
		this.buildUsername = name;
	}

	@Override
	public void setType(String type) {
		this.buildType = type;
	}

	@Override
	public void setValue(String value) {
		this.buildLevel = value;
	}

	public void setGold(String gold) {
		this.buildGold = gold;
	}

	public void setItems(String items) {
		this.buildItems = items;
	}

	public void setHealth(String health) {
		this.buildHealth = health;
	}

	public void setEquips(String equips) {
		this.buildEquips = equips;
	}

	public void setXp(String xp) {
		this.buildXp = xp;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getItems() {
		return this.items;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.username;
	}

	@Override
	public int getValue() {
		return this.level;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public List<Integer> getEquips() {
		return this.equips;
	}

	@Override
	public Character build() {

		if (this.buildUsername == null || this.buildID == null || this.buildType == null || this.buildItems == null
				|| this.buildXp == null || this.buildGold == null || this.buildLevel == null || this.buildEquips == null) {

			return null;
		}

		try {
			this.username = this.buildUsername;
			this.ID = Integer.parseInt(this.buildID);
			this.type = Type.valueOf(this.buildType);
			this.health = Integer.parseInt(this.buildHealth);
			this.xp = Integer.parseInt(this.buildXp);
			this.gold = Integer.parseInt(this.buildGold);
			this.level = Integer.parseInt(this.buildLevel);

			this.items = new ArrayList<>();
			for (String s : this.buildItems.replaceAll(" ", "").split(",")) {
				this.items.add(Integer.parseInt(s));
			}

			this.equips = new ArrayList<>();
			for (String s : this.buildEquips.replaceAll(" ", "").split(",")) {
				this.equips.add(Integer.parseInt(s));
			}

			return new Character(this);

		}

		catch (NumberFormatException e) {
			Logging.logEvent(PlayerBuilder.class.getName(), Logging.Levels.WARNING,
					"Improperly formatted XML file on player loading.");

		}

		// TODO Auto-generated method stub
		return null;
	}

}
