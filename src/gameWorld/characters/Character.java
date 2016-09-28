package gameWorld.characters;

import java.util.ArrayList;
import java.util.List;

import clientServer.ServerSideGame;
import clientServer.PackageCode;
import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.Room;
import gameWorld.Sendable;
import gameWorld.World.Direction;
import gameWorld.item.Item;
import util.Buildable;

public class Character extends Entity implements Buildable, Sendable {

	public enum Type {
		MONSTER(45),
		VENDOR(-1),
		PLAYER(100);

		private int baseXP;

		private Type(int baseXP) {
			this.baseXP = baseXP;
		}

		public int getBaseXP() {
			return this.baseXP;
		}
	}

	/*
	 * All Characters have names, items
	 * Vendors and Monsters have ranks and modelIDs
	 * Players and Monsters have gold, xp, health (& max health) and damage
	 * Players have level, as well as scaling factors for their various stats
	 * Players also have a value for xp to next level, which does not take
	 * 		into account the amount of xp already earned this level
	 */

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
	private Type type; //?
	private List<Integer> items;

	/* Fields for NPCs */
	private int rank;
	private int modelID;

	/* Fields for combat characters (Players, Monsters) */
	private int health;
	private int maxHealth; //*
	private int damage; //*
	private int xp;
	private int gold;
	private boolean isAlive;
	private long attackTimer = 0;

	// extra fields for Players
	private int level;
	private int xpForLevel;

	private List<Item> equips;

	/*public Character(Room room, int xPos, int yPos,
			String name, String description, Direction facing) {
		super(room, xPos, yPos, name, description, facing);
	}*/

	public Character(Room room, int xPos, int yPos,
			String description, Direction facing, int level,
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
		this.name = builder.getName();
		this.level = builder.getValue();
		this.items = builder.getItems();
		this.equips = new ArrayList<Item>();
		for (int i : builder.getEquips()) {
			this.equips.add(ServerSideGame.mapOfItems.get(i));
		}
	}
	//TODO constructor.
	//username, UID, type, items, health, 0, gold, level, equips.

	public Character(String username) {
		super(null, -1, -1, username, "A player, just like you!", null);

		this.modelID = -1;
		this.items = new ArrayList<Integer>();
		this.type = Type.PLAYER;
		this.baseXP = type.getBaseXP();
		this.rank = -1;
		this.level = 1;
		this.xp = 0;
		this.isAlive = false;
		this.equips = new ArrayList<Item>();

		setFields();
		addActions();
	}

	public void respawn(Room room, int x, int y, Direction facing) {
		this.room = room;
		this.xPos = x;
		this.yPos = y;
		this.facing = facing;
		this.isAlive = true;
	}

	private void addActions() {
		if (this.type.equals(Type.VENDOR))
			this.actions.add(new Action() {
				@Override
				public String name() { return "Trade";}
				@Override
				public void perform(Character caller) {
					// TODO UI.showTradeDialog(); or something
				}
			});

		if (this.type.equals(Type.MONSTER))
			this.actions.add(new Action() {
				@Override
				public String name() { return "Attack";}
				@Override
				public void perform(Character caller) {
					tryAttack(caller);
				}
			});
	}

	private void setFields() {
		if (this.items == null) this.items = new ArrayList<>();

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
			this.maxHealth = (int) Math.pow(BASE_HEALTH, 1+HEALTH_FACTOR*((this.level-1)/100));
			this.health = this.maxHealth;
			this.damage = (int) Math.pow(BASE_DAMAGE, 1+DAMAGE_FACTOR*((this.level-1)/100));
			this.gold = 0;
			this.xpForLevel = this.baseXP + (int) Math.pow(this.level-1, XP_FACTOR);
		} else if (this.type.equals(Type.MONSTER)) {
			this.maxHealth = (int) (Math.pow(BASE_HEALTH, 1+1*((this.level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(this.rank-1))));
			this.health = this.maxHealth;
			this.damage = (int) (Math.pow(BASE_DAMAGE, 1+1*((this.level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(this.rank-1))));
			this.gold = (int) (Math.pow(2, 1+1*((this.level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(this.rank-1))));
			this.xp = (int) (Math.pow(this.baseXP, 1+1*((this.level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(this.rank-1))));
		}
	}

	public void tryAttack(Character attacker) {
		if (attacker.room().equals(this.room)) {
			if (attacker.xPos() == this.xPos-1 || attacker.xPos() == this.xPos+1
					|| attacker.yPos() == this.yPos-1 || attacker.yPos() == this.yPos+1) {
				if (System.currentTimeMillis() > attacker.attackTimer+ATTACK_SPEED)
					applyAttack(attacker);
			}
		}
	}

	private void applyAttack(Character attacker) {
		int attack = attacker.getAttack();	// max ~1000
		int defense = 0;	// max 350
		for (Item item : this.equips) {
			switch (item.getType()) {
			case ARMOR:
			case SHIELD:
			case HELMET:
				defense += item.getValue();
				break;
			default:
				break;
			}
		}

		int damageDone = attack - defense;
		setHealth(this.health-damageDone);
		attacker.startAttackTimer();
	}

	public void startAttackTimer() {
		this.attackTimer = System.currentTimeMillis();
	}

	public long getAttackTimer() {
		return this.attackTimer;
	}

	public void equip(Item item) {
		for (Integer id : this.items) {
			if (id == item.getID()) {
				this.items.remove(id);
				this.equips.add(new Item(item));
			}
		}
	}

	public void pickUp(Item item) {
		this.items.add(item.getID());
	}

	public void sellItem(Item item, int value) {
		for (Integer id : this.items) {
			if (id == item.getID()) {
				this.items.remove(id);
				this.gold += value;
			}
		}
	}

	public void buyItem(Item item, int value) {
		this.items.add(item.getID());
		this.gold -= value;
	}

	public void move(Direction dir) {
		this.room.move(this, dir);
	}

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

	private void turnLeft() {
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

	private void turnRight() {
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

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Integer> getItems() {
		return this.items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}

	public int getRank() {
		return this.rank;
	}

	public int getModelID() {
		return this.modelID;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
		if (health > this.maxHealth)
			health = this.maxHealth;
		if (health < 0) {
			this.isAlive = false;
			// TODO: die();
		}
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getAttack() {
		int attack = this.damage;
		for (Item item : this.equips) {
			if (item.getType().equals(Item.Type.WEAPON)) {
				attack += item.getValue();
			}
		}
		return attack;
	}

	public int getDamage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getXp() {
		return this.xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
		if (this.xp > this.xpForLevel) {
			++this.level;
			this.xp -= this.xpForLevel;
			setFields();
		}
	}

	public int getGold() {
		return this.gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		if (gold < 0)
			gold = 0;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXpForLevel() {
		return this.xpForLevel;
	}

	public void setXpForLevel(int xpForLevel) {
		this.xpForLevel = xpForLevel;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public List<Item> getEquips() {
		return this.equips;
	}
	public byte[] onEntry() {
		byte[] bytes;
		int i;

		switch (type) {
		case MONSTER:
			bytes = new byte[28];
			bytes[0] = PackageCode.Codes.GAME_ROOM_ENTRY.value();
			bytes[1] = Sendable.Types.MONSTER.value();
			bytes[2] = isAlive ? (byte) 1 : 0;
			bytes[3] = facing.value();
			i = 4;
			for (byte b : intsToBytes(modelID, ID, health, level, xPos, yPos)) {
				bytes[i++] = b;
			}
			return bytes;
		case VENDOR:
			bytes = new byte[19];
			bytes[0] = PackageCode.Codes.GAME_ROOM_ENTRY.value();
			bytes[1] = Sendable.Types.VENDOR.value();
			bytes[2] = facing.value();
			i = 3;
			for (byte b : intsToBytes(modelID, ID, xPos, yPos)) {
				bytes[i++] = b;
			}
			return bytes;
		case PLAYER:
			bytes = new byte[24+name.length()];
			bytes[0] = PackageCode.Codes.GAME_ROOM_ENTRY.value();
			bytes[1] = Sendable.Types.PLAYER.value();
			bytes[2] = isAlive ? (byte) 1 : 0;
			bytes[3] = facing.value();
			i = 4;
			for (byte b : intsToBytes(ID, health, level, xPos, yPos)) {
				bytes[i++] = b;
			}
			for (char c : name.toCharArray()) {
				bytes[i++] = (byte) c;
			}
			return bytes;
		}

		return null;
	}

	public byte[] roomUpdate() {
		byte[] bytes;
		int i;

		switch (type) {
		case MONSTER:
			bytes = new byte[20];
			bytes[0] = PackageCode.Codes.GAME_ROOM_UPDATE.value();
			bytes[1] = Sendable.Types.MONSTER.value();
			bytes[2] = isAlive ? (byte) 1 : 0;
			bytes[3] = facing.value();
			i = 4;
			for (byte b : intsToBytes(ID, health, level, xPos, yPos)) {
				bytes[i++] = b;
			}
			return bytes;
		case PLAYER:
			bytes = new byte[24];
			bytes[0] = PackageCode.Codes.GAME_ROOM_UPDATE.value();
			bytes[1] = Sendable.Types.PLAYER.value();
			bytes[2] = isAlive ? (byte) 1 : 0;
			bytes[3] = facing.value();
			i = 4;
			for (byte b : intsToBytes(ID, health, level, xPos, yPos)) {
				bytes[i++] = b;
			}
			return bytes;
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

}