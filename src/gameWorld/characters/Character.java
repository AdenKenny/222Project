package gameWorld.characters;

import java.util.ArrayList;
import java.util.List;

import clientServer.Game;
import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.Room;
import gameWorld.World.Direction;
import gameWorld.item.Item;

public class Character extends Entity {

	public enum Type {
		MONSTER(45), 
		VENDOR(-1),
		PLAYER(100);
		
		private int baseXP;
		
		private Type(int baseXP) {
			this.baseXP = baseXP;
		}
		
		public int getBaseXP() {
			return baseXP;
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
	
	/* Constants for Player levelling calculations */
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
	private Type type;
	private List<Integer> items;
	
	/* Fields for NPCs */
	private int rank;
	private int modelID;
	
	/* Fields for combat characters (Players, Monsters) */
	private int health;
	private int maxHealth;
	private int damage;
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
		this.items = new ArrayList<Integer>(model.getSetOfItems());
		this.type = model.getType();
		this.baseXP = type.getBaseXP();
		this.rank = model.getValue();
		this.level = level;
		this.xp = 0;
		this.isAlive = true;
		this.equips = new ArrayList<Item>();
		
		setFields();
		addActions();
	}
	
	private void addActions() {
		if (type.equals(Type.VENDOR))
			actions.add(new Action() {
				public String name() { return "Trade";}
				public void perform(Character caller) {
					// TODO UI.showTradeDialog(); or something
				}
			});
		
		if (type.equals(Type.MONSTER))
			actions.add(new Action() {
				public String name() { return "Attack";}
				public void perform(Character caller) {
					tryAttack(caller);
				}
			});
	}
	
	private void setFields() {
		if (items == null) items = new ArrayList<Integer>();
		
		if (type.equals(Type.VENDOR)) {
			maxHealth = -1;
			health = -1;
			damage = -1;
			xp = -1;
			gold = -1;
			level = -1;
			xpForLevel = -1;
		} else if (type.equals(Type.PLAYER)) {
			rank = -1;
			maxHealth = (int) Math.pow(BASE_HEALTH, 1+HEALTH_FACTOR*((level-1)/100));
			health = maxHealth;
			damage = (int) Math.pow(BASE_DAMAGE, 1+DAMAGE_FACTOR*((level-1)/100));
			gold = 0;
			xpForLevel = baseXP + (int) Math.pow(level-1, XP_FACTOR);
		} else if (type.equals(Type.MONSTER)) {
			maxHealth = (int) (Math.pow(BASE_HEALTH, 1+1*((level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(rank-1))));
			health = maxHealth;
			damage = (int) (Math.pow(BASE_DAMAGE, 1+1*((level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(rank-1))));
			gold = (int) (Math.pow(2, 1+1*((level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(rank-1))));
			xp = (int) (Math.pow(baseXP, 1+1*((level-1)/100))
					*(0.45*(1+RANK_SCALE_FACTOR*(rank-1))));
		}
	}
	
	public void tryAttack(Character attacker) {
		if (attacker.room().equals(room)) {
			if (attacker.xPos() == xPos-1 || attacker.xPos() == xPos+1
					|| attacker.yPos() == yPos-1 || attacker.yPos() == yPos+1) {
				if (System.currentTimeMillis() > attacker.attackTimer+ATTACK_SPEED)
					applyAttack(attacker);
			}
		}
	}
	
	private void applyAttack(Character attacker) {
		int attack = attacker.getAttack();	// max ~1000
		int defense = 0;	// max 350
		for (Item item : equips) {
			switch (item.getType()) {
			case ARMOR:
			case SHIELD:
			case HELMET:
				defense += item.getValue();
			default:
				break;
			}
		}
		
		int damageDone = attack - defense;
		setHealth(health-damageDone);
		attacker.startAttackTimer();
	}
	
	public void startAttackTimer() {
		attackTimer = System.currentTimeMillis();
	}
	
	public long getAttackTimer() {
		return attackTimer;
	}
	
	public void equip(Item item) {
		for (Integer id : items) {
			if (id == item.getID()) {
				items.remove(id);
				equips.add(new Item(item));
			}
		}
	}
	
	public void pickUp(Item item) {
		items.add(item.getID());
	}
	
	public void sellItem(Item item, int value) {
		for (Integer id : items) {
			if (id == item.getID()) {
				items.remove(id);
				gold += value;
			}
		}
	}
	
	public void buyItem(Item item, int value) {
		items.add(item.getID());
		gold -= value;
	}

	public void move(Direction dir) {
		room.move(this, dir);
	}

	public void turn(Direction dir) {
		switch (dir) {
		case NORTH:
			facing = Direction.NORTH;
			break;
		case EAST:
			facing = Direction.EAST;
			break;
		case SOUTH:
			facing = Direction.SOUTH;
			break;
		case WEST:
			facing = Direction.WEST;
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
		switch (facing) {
		case NORTH:
			facing = Direction.WEST;
			break;
		case EAST:
			facing = Direction.NORTH;
			break;
		case SOUTH:
			facing = Direction.EAST;
			break;
		case WEST:
			facing = Direction.SOUTH;
			break;
		default:
			break;
		}
	}

	private void turnRight() {
		switch (facing) {
		case NORTH:
			facing = Direction.EAST;
			break;
		case EAST:
			facing = Direction.SOUTH;
			break;
		case SOUTH:
			facing = Direction.WEST;
			break;
		case WEST:
			facing = Direction.NORTH;
			break;
		default:
			break;
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Integer> getItems() {
		return items;
	}

	public void setItems(List<Integer> items) {
		this.items = items;
	}

	public int getRank() {
		return rank;
	}

	public int getModelID() {
		return modelID;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		if (health > maxHealth)
			health = maxHealth;
		if (health < 0) {
			isAlive = false;
			// TODO: die();
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getAttack() {
		int attack = damage;
		for (Item item : equips) {
			if (item.getType().equals(Item.Type.WEAPON)) {
				attack += item.getValue();
			}
		}
		return attack;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
		if (xp > xpForLevel) {
			++level;
			xp -= xpForLevel;
			setFields();
		}
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		if (gold < 0)
			gold = 0;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXpForLevel() {
		return xpForLevel;
	}

	public void setXpForLevel(int xpForLevel) {
		this.xpForLevel = xpForLevel;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}