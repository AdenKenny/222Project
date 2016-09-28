package gameWorld.characters;

import java.util.List;

import gameWorld.characters.Character.Type;
import util.AbstractBuilder;
import util.Buildable;

public final class PlayerBuilder implements AbstractBuilder {

	private String buildUsername;
	private String buildID;
	private String buildType;
	private String buildItems;
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
	private String description;
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

	public void setEquips(String equips) {
		this.buildEquips = equips;
	}

	public void setXp(String xp) {
		this.buildXp = xp;
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
	public void setDescription(String description) {
		this.description = "A player, just like you!";
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Buildable build() {
		// TODO Auto-generated method stub
		return null;
	}

}
