package gameWorld.characters;

import java.util.ArrayList;
import java.util.List;

import gameWorld.characters.Character.Type;
import util.AbstractBuilder;
import util.Logging;

/**
 * A class to build an instance of Character that represents a player.
 *
 * @author Aden
 */

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
	private int[] items;
	private int health;
	private int xp;
	private int gold;
	private int level;
	private String description = "A player, just like you!";
	private int[] equips;

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

	/**
	 * Sets the amount of gold that the Character that is being built has. This
	 * will be a value that is read from a file.
	 *
	 * @param gold
	 *            The amount of gold that the Character being built has.
	 */
	public void setGold(String gold) {
		this.buildGold = gold;
	}

	@Override
	public void setItems(String items) {
		this.buildItems = items;
	}

	/**
	 * Sets the amount of health that the Character that is being built has.
	 * This will be a value that is read from a file.
	 *
	 * @param health
	 *            The amount of health that the Character being built has.
	 */
	public void setHealth(String health) {
		this.buildHealth = health;
	}

	/**
	 * Sets the equipment that the Character that is being built has. This will
	 * be a value that is read from a file.
	 *
	 * @param equips
	 *            The equipment that the Character being built has.
	 */
	public void setEquips(String equips) {
		this.buildEquips = equips;
	}

	/**
	 * Sets the amount of XP that the Character that is being built has. This
	 * will be a value that is read from a file.
	 *
	 * @param xp
	 *            The amount of XP that the Character being built has.
	 */
	public void setXp(String xp) {
		this.buildXp = xp;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a List of Integers containing the IDs of the Items that the
	 * Character that is being built has.
	 *
	 * @return The List of Item IDs of the Character being built.
	 */
	public int[] getItems() {
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

	/**
	 * Returns a List of Integers containing the IDs of the Items that the
	 * Character that is being built has equipped.
	 *
	 * @return The IDs of the Items that the Character being built has equipped.
	 */
	public int[] getEquipIndexes() {
		return this.equips;
	}

	/**
	 * Returns the Type of the Character that is being built. Since this is a
	 * PlayerBuilder, this should always be Type.PLAYER.
	 *
	 * @return The Type of the Character being built.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the health of the Character that is being built.
	 *
	 * @return The health of the Character being built.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Returns the XP of the Character that is being built.
	 *
	 * @return The XP of the Character being built.
	 */
	public int getXp() {
		return xp;
	}

	/**
	 * Returns the amount of gold that the Character that is being built has.
	 *
	 * @return The gold of the Character being built.
	 */
	public int getGold() {
		return gold;
	}

	@Override
	public Character build() {

		if (this.buildUsername == null || this.buildID == null || this.buildType == null || this.buildItems == null
				|| this.buildXp == null || this.buildGold == null || this.buildLevel == null
				|| this.buildEquips == null) {

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

			this.items = new int[8];
			int i = 0;
			for (String s : this.buildItems.replaceAll(" ", "").split(",")) {
				if (s.isEmpty()) {
					continue;
				}
				this.items[i++] = Integer.parseInt(s);
				if (i == 8) break;
			}

			this.equips = new int[8];
			i = 0;
			for (String s : this.buildEquips.replaceAll(" ", "").split(",")) {
				if (s.isEmpty()) {
					continue;
				}
				this.equips[i++] = Integer.parseInt(s);
				if (i == 8) break;
			}

			return new Character(this);

		}

		catch (NumberFormatException e) {
			Logging.logEvent(PlayerBuilder.class.getName(), Logging.Levels.WARNING,
					"Improperly formatted XML file on player loading.");

		}

		return null;
	}

	/**
	 * This should never be called.
	 */

	@Override
	@Deprecated
	public void setSaleValue(String value) {
		throw new AssertionError();
	}

}
