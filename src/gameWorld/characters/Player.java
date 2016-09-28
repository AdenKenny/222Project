package gameWorld.characters;

import java.util.List;

import gameWorld.Room;
import gameWorld.World.Direction;

public class Player extends Character {
	private final int baseXP = 100;	// xp required for initial level
	private final double xpCurveFactor = 2.2; // both constants required to calculate later levels' xp

	private int maxHealth = 100;
	private int health = maxHealth;
	private int damage = 10;
	private int level = 1;
	private int xp = 0;
	private int xpToNextLevel = baseXP;

	public Player(Room room, int x, int y, String name, String description, Direction facing) {
		super(room, x, y, description, facing, 0, null);
	}

	public int health() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int damage() {
		return damage;
	}

	public int level() {
		return level;
	}

	public int xp() {
		return xp;
	}

	public void addXP(int xp) {
		this.xp += xp;
		if (xp > xpToNextLevel) {
			// in case an instant-level item is added,
			// just design these methods in a way that
			// allows it to work just by calling levelUp()
			int tempXP = xp - xpToNextLevel;
			levelUp();
			this.xp = tempXP;
		}
	}

	public void levelUp() {
		maxHealth += 50;
		health = maxHealth;
		damage += 5;
		xp = 0;
		level++;

		xpToNextLevel = 100 + (int)(Math.pow(level-1, xpCurveFactor));
	}

}