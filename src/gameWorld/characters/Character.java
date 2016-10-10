package gameWorld.characters;

import java.util.ArrayList;
import java.util.List;

import clientServer.Game;
import clientServer.PackageCode;
import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.Sendable;
import gameWorld.World.Direction;
import gameWorld.item.Item;
import gameWorld.rooms.Room;
import ui.appwindow.MainWindow;
import util.Buildable;
import util.Logging;

/**
 * A class which represents all non-object Entities.
 * 
 * @author Louis
 */
public class Character extends Entity implements Buildable, Sendable, Cloneable {

	/**
	 * An enumeration representing the different types of Characters that exist.
	 * 
	 * @author Louis
	 */
	public enum Type {
		MONSTER(45, Types.MONSTER), VENDOR(-1, Types.VENDOR), PLAYER(100, Types.PLAYER);

		private int baseXP;
		private Types sendableType;

		private Type(int baseXP, Types sendableType) {
			this.baseXP = baseXP;
			this.sendableType = sendableType;
		}

		/**
		 * Returns the base XP of this type of Character.
		 * 
		 * @return the base XP for this Type
		 */
		public int getBaseXP() {
			return this.baseXP;
		}

		/**
		 * Returns the type of Sendable that this Type is associated with.
		 * 
		 * @return the corresponding Sendable.Types
		 */
		public Types sendableType() {
			return this.sendableType;
		}
	}

	/* Constants for Player leveling calculations */
	// xpForLevel = BASE_XP + (level-1)^XP_FACTOR
	private int baseXP;
	private static final double XP_FACTOR = 2.20779;
	// maxHealth = BASE_HEALTH ^ (1 + HEALTH_FACTOR * ((level-1)/100))
	private static final int BASE_HEALTH = 100;
	private static final double HEALTH_FACTOR = 1.010101;
	// damage = BASE_DAMAGE ^ (1 + DAMAGE_FACTOR * ((level-1)/100))
	private static final int BASE_DAMAGE = 10;
	private static final double DAMAGE_FACTOR = 1.974;
	// scale factor for Monster and Vendor ranks
	private static final double RANK_SCALE_FACTOR = 0.3;

	// Would prefer not to have this hard-coded, but for now this is simplest
	private static final int ATTACK_SPEED = 1000; // ms

	/* Fields for all characters */
	private Type type; // ?
	private List<Integer> items;

	/* Fields for NPCs */
	private int rank;
	private int modelID;

	/* Fields for combat characters (Players, Monsters) */
	private int health;
	private int maxHealth; // *
	private int damage; // *
	private int xp;
	private int gold;
	private boolean isAlive;
	private long attackTimer = 0;

	// extra fields for Players
	private int level;
	private int xpForLevel;

	// TODO: find some way of actually getting equipment to work
	private List<Item> equips;

	public Character(Room room, int xPos, int yPos, String description, Direction facing, int level,
			CharacterModel model) {
		super(room, xPos, yPos, model.getName(), description, facing);

		this.modelID = model.getID();
		this.items = new ArrayList<>(model.getSetOfItems());
		this.type = model.getType();
		this.baseXP = this.type.getBaseXP();
		this.rank = model.getValue();
		this.level = level;
		this.xp = 0;
		this.isAlive = true;
		this.equips = new ArrayList<>();

		setFields();
		addActions();
	}

	public Character(PlayerBuilder builder) {
		super(null, -1, -1, builder.getName(), builder.getDescription(), null);

		this.ID = builder.getID();
		adjustIDCount(this.ID);
		this.name = builder.getName();
		this.level = builder.getValue();
		this.gold = builder.getGold();
		this.xp = builder.getXp();
		this.type = builder.getType();
		this.items = builder.getItems();
		this.equips = new ArrayList<>();
		for (int i : builder.getEquips()) {
			this.equips.add(Game.mapOfItems.get(i));
		}

		this.rank = -1;
		this.isAlive = false;
		this.health = this.maxHealth = (int) Math.pow(BASE_HEALTH, 1 + HEALTH_FACTOR * ((this.level - 1) / 100));
		this.damage = (int) Math.pow(BASE_DAMAGE, 1 + DAMAGE_FACTOR * ((this.level - 1) / 100));
		this.xpForLevel = this.baseXP + (int) Math.pow(this.level - 1, XP_FACTOR);
	}

	public Character(String username) {
		super(null, -1, -1, username, "A player, just like you!", null);

		this.modelID = -1;
		this.items = new ArrayList<>();
		this.type = Type.PLAYER;
		this.baseXP = this.type.getBaseXP();
		this.rank = -1;
		this.level = 1;
		this.xp = 0;
		this.isAlive = false;
		this.equips = new ArrayList<>();

		setFields();
		addActions();
	}

	/**
	 * Moves the Character to the specified Room, at the specified position,
	 * facing the specified Direction. Also returns the Character to life and
	 * fully heals the Character.
	 * 
	 * @param room
	 *            The Room to spawn in
	 * @param x
	 *            The x position to spawn in
	 * @param y
	 *            The y position to spawn in
	 * @param facing
	 *            The Direction to be facing when spawning
	 */
	public void respawn(Room room, int x, int y, Direction facing) {
		this.room = room;
		this.xPos = x;
		this.yPos = y;
		this.facing = facing;
		this.isAlive = true;
		this.health = this.maxHealth;
	}

	private void addActions() {
		if (this.type.equals(Type.VENDOR)) {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Item Info";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof MainWindow)) {
						util.Logging.logEvent("Character", util.Logging.Levels.WARNING,
								"Vendor action 'Item Info' expected MainWindow argument, got "
										+ caller.getClass().getName() + " argument.");
						return;
					}

					MainWindow mw = (MainWindow) caller;

					Item sellItem = Game.mapOfItems.get(items.toArray(new Integer[1])[0]);

					int saleValue = sellItem.getSaleValue();
					int price = saleValue + (saleValue * (rank - 1) / 5);

					mw.addGameChat("++++++++++++++++++\n" + sellItem.getNiceName() + "\n" + sellItem.getDescription()
							+ "\n" + String.format("Price: %d", price) + "\n" + "++++++++++++++++++");
				}

				@Override
				public boolean isClientAction() {
					return true;
				}
			});

			this.actions.add(new Action() {

				@Override
				public String name() {
					return "Buy Item";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						util.Logging.logEvent("Character", util.Logging.Levels.WARNING,
								"Vendor action 'Buy Item' expected Character argument, got "
										+ caller.getClass().getName() + " argument.");
						return;
					}

					Character ch = (Character) caller;

					Item sellItem = Game.mapOfItems.get(items.toArray(new Integer[1])[0]);

					int saleValue = sellItem.getSaleValue();
					int price = saleValue + (saleValue * (rank - 1) / 5);

					ch.items.add(items.toArray(new Integer[1])[0]);
					ch.setGold(ch.getGold() - price);
				}

				@Override
				public boolean isClientAction() {
					return false;
				}
			});
		}

		if (this.type.equals(Type.MONSTER)) {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Attack";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						util.Logging.logEvent("Character", util.Logging.Levels.WARNING,
								"Monster action 'Attack' expected Character argument, got "
										+ caller.getClass().getName() + " argument.");
						return;
					}

					Character ch = (Character) caller;

					tryAttack(ch);
				}

				@Override
				public boolean isClientAction() {
					return false;
				}
			});
		}
	}

	private void setFields() {
		if (this.items == null)
			this.items = new ArrayList<>();

		if (this.type.equals(Type.VENDOR)) {
			this.maxHealth = -1;
			this.health = -1;
			this.damage = -1;
			this.xp = -1;
			this.gold = -1;
			this.level = -1;
			this.xpForLevel = -1;
		} else if (this.type.equals(Type.PLAYER)) {
			this.rank = -1;
			this.maxHealth = (int) Math.pow(BASE_HEALTH, 1 + HEALTH_FACTOR * ((this.level - 1) / 100));
			this.health = this.maxHealth;
			this.damage = (int) Math.pow(BASE_DAMAGE, 1 + DAMAGE_FACTOR * ((this.level - 1) / 100));
			this.gold = 0;
			this.xpForLevel = this.baseXP + (int) Math.pow(this.level - 1, XP_FACTOR);
		} else if (this.type.equals(Type.MONSTER)) {
			this.maxHealth = (int) (Math.pow(BASE_HEALTH, 1 + 1 * ((this.level - 1) / 100))
					* (0.45 * (1 + RANK_SCALE_FACTOR * (this.rank - 1))));
			this.health = this.maxHealth;
			this.damage = (int) (Math.pow(BASE_DAMAGE, 1 + 1 * ((this.level - 1) / 100))
					* (0.45 * (1 + RANK_SCALE_FACTOR * (this.rank - 1))));
			this.gold = (int) (Math.pow(2, 1 + 1 * ((this.level - 1) / 100))
					* (0.45 * (1 + RANK_SCALE_FACTOR * (this.rank - 1))));
			this.xp = (int) (Math.pow(this.baseXP, 1 + 1 * ((this.level - 1) / 100))
					* (0.45 * (1 + RANK_SCALE_FACTOR * (this.rank - 1))));
		}
	}

	/**
	 * Determines whether the attacker can attack this Character or not. If it
	 * is possible, the attack will then be applied.
	 * 
	 * @param attacker
	 *            The Character that is attacking this one
	 */
	public void tryAttack(Character attacker) {
		if (attacker.room().equals(this.room)) {
			if ((attacker.xPos == this.xPos - 1 || attacker.xPos == this.xPos + 1) && attacker.yPos == this.yPos
					|| (attacker.yPos == this.yPos - 1 || attacker.yPos == this.yPos + 1)
							&& attacker.xPos == this.xPos) {
				if (System.currentTimeMillis() > attacker.attackTimer + ATTACK_SPEED)
					applyAttack(attacker);
			}
		}
	}

	private void applyAttack(Character attacker) {
		int attack = attacker.getAttack(); // max ~1000
		int defense = 0; // max 350
		for (Item item : this.equips) {
			switch (item.getType()) {
			case ARMOR:
			case SHIELD:
			case HELMET:
				defense += item.getValue();
				break;
			// $CASES-OMITTED$
			default:
				break;
			}
		}

		int damageDone = attack - defense;
		this.health = this.health - damageDone;
		attacker.startAttackTimer();
	}

	/**
	 * Sets the attack timer, which is used to check how long it has been since
	 * this Character attacked, in order to enforce a maximum attack speed.
	 */
	public void startAttackTimer() {
		this.attackTimer = System.currentTimeMillis();
	}

	/**
	 * Returns the attack timer, which is used to check how long it has been
	 * since this Character attacked, in order to enforce a maximum attack
	 * speed. This attack timer is measured in UNIX time.
	 * 
	 * @return The time at which the last attack occurred
	 */
	public long getAttackTimer() {
		return this.attackTimer;
	}

	/**
	 * Attempts to equip the specified Item to this Character. Only works if the
	 * Character is holding that Item.
	 * 
	 * @param item
	 *            The Item to equip
	 */
	public void equip(Item item) {
		for (Integer id : this.items) {
			if (id == item.getID()) {
				this.items.remove(id);
				this.equips.add(new Item(item));
			}
		}
	}

	/**
	 * Picks up the given Item and adds it to this Character's inventory.
	 * 
	 * @param item
	 *            The Item to pick up
	 */
	public void pickUp(Item item) {
		this.items.add(item.getID());
	}

	/**
	 * Sells the Item corresponding to the given ID for the given amount.
	 * 
	 * @param itemID
	 *            The Item to sell
	 * @param value
	 *            The amount to sell for
	 */
	public void sellItem(int itemID, int value) {
		for (Integer id : this.items) {
			if (id == itemID) {
				this.items.remove(id);
				this.gold += value;
				return;
			}
		}
	}

	/**
	 * Buys the given Item for the given amount of money.
	 * 
	 * @param item
	 *            The Item to buy
	 * @param value
	 *            The amount to buy for
	 */
	public void buyItem(Item item, int value) {
		this.items.add(item.getID());
		this.gold -= value;
	}

	/**
	 * Attempts to move in the given Direction.
	 * 
	 * @param dir
	 *            The Direction to move in
	 */
	public void move(Direction dir) {
		if (this.room != null) {
			this.room.move(this, dir);
		}
	}

	/**
	 * Attempts to turn in or to the given Direction.
	 * 
	 * @param dir
	 *            The Direction to turn in/to
	 */
	public void turn(Direction dir) {
		switch (dir) {
		case NORTH:
			this.facing = Direction.NORTH;
			break;
		case EAST:
			this.facing = Direction.EAST;
			break;
		case SOUTH:
			this.facing = Direction.SOUTH;
			break;
		case WEST:
			this.facing = Direction.WEST;
			break;
		case LEFT:
			turnLeft();
			break;
		case RIGHT:
			turnRight();
			break;
		default:
			break;
		}
	}

	/**
	 * Attempts to turn the Character 90 degrees to the left.
	 */
	public void turnLeft() {
		switch (this.facing) {
		case NORTH:
			this.facing = Direction.WEST;
			break;
		case EAST:
			this.facing = Direction.NORTH;
			break;
		case SOUTH:
			this.facing = Direction.EAST;
			break;
		case WEST:
			this.facing = Direction.SOUTH;
			break;
		default:
			break;
		}
	}

	/**
	 * Attempts to turn the Character 90 degrees to the right.
	 */
	public void turnRight() {
		switch (this.facing) {
		case NORTH:
			this.facing = Direction.EAST;
			break;
		case EAST:
			this.facing = Direction.SOUTH;
			break;
		case SOUTH:
			this.facing = Direction.WEST;
			break;
		case WEST:
			this.facing = Direction.NORTH;
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the Direction that this Character is facing to the specified
	 * Direction.
	 * 
	 * @deprecated Use the {@link #turn(Direction) turn} method instead.
	 * @param facing
	 *            The Direction to turn to
	 */
	@Deprecated
	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	/**
	 * Returns the Type of Character that this Character is.
	 * 
	 * @return This Character's Type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Sets the Type of this Character to the specified Type.
	 * 
	 * @param type
	 *            The Type to set this Character to
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Gets the inventory of this Character, as a {@literal List<Integer>} of
	 * Item IDs.
	 * 
	 * @return a List of Item IDs
	 */
	public List<Integer> getItems() {
		return this.items;
	}

	/**
	 * Sets the inventory of this Character to the specified List of Item IDs.
	 * 
	 * @param items
	 *            a List of Item IDs
	 */
	public void setItems(List<Integer> items) {
		this.items = items;
	}

	/**
	 * Returns the rank of this Character. Not applicable to Player-type
	 * Characters.
	 * 
	 * @return This Character's rank
	 */
	public int getRank() {
		return this.rank;
	}

	/**
	 * Returns the model ID of this Character. Not applicable to Player-type
	 * Characters.
	 * 
	 * @return This Character's model ID
	 */
	public int getModelID() {
		return this.modelID;
	}

	/**
	 * Returns this Character's current health.
	 * 
	 * @return This Character's health
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Sets this Character's current health to the specified value.
	 * 
	 * @param health
	 *            The amount to set this Character's health to
	 */
	public void setHealth(int health) {
		this.health = health;
		if (health > this.maxHealth)
			health = this.maxHealth; // TODO The param is assigned here? Should
										// this.health be assigned?
		if (health < 0) {
			this.isAlive = false;
			// TODO: die();
		}
	}

	/**
	 * Returns the maximum health that this Character can have, given their
	 * current level and (in the case of Monsters) rank.
	 * 
	 * @return This Character's current maximum health
	 */
	public int getMaxHealth() {
		return this.maxHealth;
	}

	/**
	 * Sets this Character's maximum health to the specified value.
	 * 
	 * @param maxHealth
	 *            The new value for this Character's maximum health
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * Returns the total amount of damage an attack by this Character can do,
	 * after taking into account any damage-increasing effects of items.
	 * 
	 * @return the amount of damage done by one attack
	 */
	public int getAttack() {
		int attack = this.damage;
		for (Item item : this.equips) {
			if (item.getType().equals(Item.Type.WEAPON)) {
				attack += item.getValue();
			}
		}
		return attack;
	}

	/**
	 * Returns the amount of damage this Character has without taking into
	 * account any damage-increasing effects of items.
	 * 
	 * @return the amount of damage this Character can do
	 */
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Sets this Character's base damage to the specified value.
	 * 
	 * @param damage
	 *            This Character's new damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Returns this Character's current XP.
	 * 
	 * @return The current XP
	 */
	public int getXp() {
		return this.xp;
	}

	/**
	 * Sets this Characters current XP.
	 * 
	 * @param xp
	 *            The new amount of XP
	 */
	public void setXp(int xp) {
		this.xp = xp;
		if (this.xp > this.xpForLevel) {
			++this.level;
			this.xp -= this.xpForLevel;
			int gold = this.gold;
			setFields();
			this.gold = gold;
		}
	}

	/**
	 * Returns this Character's current amount of gold.
	 * 
	 * @return The current amount of gold
	 */
	public int getGold() {
		return this.gold;
	}

	/**
	 * Sets this Character's current amount of gold to the specified value.
	 * 
	 * @param gold
	 *            The new amount of gold
	 */
	public void setGold(int gold) {
		this.gold = gold;
		if (this.gold < 0)
			this.gold = 0;
	}

	/**
	 * Returns this Character's current level.
	 * 
	 * @return The current level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Sets this Character's current level to the specified value.
	 * 
	 * @param level
	 *            The new level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Returns the amount of XP that is required for this Character to get to
	 * the next level, not taking into account any progress that has been made
	 * so far.
	 * 
	 * @return The amount of XP until the next level
	 */
	public int getXpForLevel() {
		return this.xpForLevel;
	}

	/**
	 * Sets the amount of XP that is required for this Character to get to the
	 * next level.
	 * 
	 * @param xpForLevel
	 *            The new amount of XP until the next level
	 */
	public void setXpForLevel(int xpForLevel) {
		this.xpForLevel = xpForLevel;
	}

	/**
	 * Checks whether this Character is alive or not.
	 * 
	 * @return Whether this Character is alive
	 */
	public boolean isAlive() {
		return this.isAlive;
	}

	/**
	 * Sets whether this Character is a live or not.
	 * 
	 * @param isAlive
	 *            Whether this Character is alive
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * Returns a List of Items that this Character has equipped
	 * 
	 * @return This Character's equipment
	 */
	public List<Item> getEquips() {
		return this.equips;
	}

	@Override
	public byte[] toSend() {
		byte[] bytes;
		int i;

		switch (this.type) {
		case MONSTER:
			bytes = new byte[28];
			bytes[0] = PackageCode.Codes.GAME_SENDABLE.value();
			bytes[1] = this.type.sendableType().value();
			bytes[2] = this.isAlive ? (byte) 1 : 0;
			bytes[3] = this.facing.value();
			i = 4;
			for (byte b : Sendable.intsToBytes(this.ID, this.modelID, this.health, this.level, this.xPos, this.yPos)) {
				bytes[i++] = b;
			}
			return bytes;
		case VENDOR:
			bytes = new byte[20];
			bytes[0] = PackageCode.Codes.GAME_SENDABLE.value();
			bytes[1] = this.type.sendableType().value();
			bytes[2] = this.facing.value();
			bytes[3] = PackageCode.Codes.BREAK.value(); // empty slot
			i = 4;
			for (byte b : Sendable.intsToBytes(this.ID, this.modelID, this.xPos, this.yPos)) {
				bytes[i++] = b;
			}
			return bytes;
		case PLAYER:
			bytes = new byte[24 + this.name.length()];
			bytes[0] = PackageCode.Codes.GAME_SENDABLE.value();
			bytes[1] = this.type.sendableType().value();
			bytes[2] = this.isAlive ? (byte) 1 : 0;
			bytes[3] = this.facing.value();
			i = 4;
			for (byte b : Sendable.intsToBytes(this.ID, this.health, this.level, this.xPos, this.yPos)) {
				bytes[i++] = b;
			}
			for (char c : this.name.toCharArray()) {
				bytes[i++] = (byte) c;
			}
			return bytes;
		default:
			break;
		}

		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public int getValue() {
		return this.level;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.attackTimer ^ (this.attackTimer >>> 32));
		result = prime * result + this.baseXP;
		result = prime * result + this.damage;
		result = prime * result + ((this.equips == null) ? 0 : this.equips.hashCode());
		result = prime * result + this.gold;
		result = prime * result + this.health;
		result = prime * result + (this.isAlive ? 1231 : 1237);
		result = prime * result + ((this.items == null) ? 0 : this.items.hashCode());
		result = prime * result + this.level;
		result = prime * result + this.maxHealth;
		result = prime * result + this.modelID;
		result = prime * result + this.rank;
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = prime * result + this.xp;
		result = prime * result + this.xpForLevel;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (this.attackTimer != other.attackTimer)
			return false;
		if (this.baseXP != other.baseXP)
			return false;
		if (this.damage != other.damage)
			return false;
		if (this.equips == null) {
			if (other.equips != null)
				return false;
		} else if (!this.equips.equals(other.equips))
			return false;
		if (this.gold != other.gold)
			return false;
		if (this.health != other.health)
			return false;
		if (this.isAlive != other.isAlive)
			return false;
		if (this.items == null) {
			if (other.items != null)
				return false;
		} else if (!this.items.equals(other.items))
			return false;
		if (this.level != other.level)
			return false;
		if (this.maxHealth != other.maxHealth)
			return false;
		if (this.modelID != other.modelID)
			return false;
		if (this.rank != other.rank)
			return false;
		if (this.type != other.type)
			return false;
		if (this.xp != other.xp)
			return false;
		if (this.xpForLevel != other.xpForLevel)
			return false;
		return true;
	}

	@Override
	public Character clone() {
		try {
			return (Character) super.clone();
		}

		catch (CloneNotSupportedException e) {
			Logging.logEvent(Character.class.getName(), Logging.Levels.WARNING, "Failed to clone a character");
		}

		return null;
	}

	@Override
	public boolean isPlayer() {
		return this.type.equals(Type.PLAYER);
	}

	/**
	 * Slays this Character.
	 */
	public void slay() {
		this.isAlive = false;
	}
}
