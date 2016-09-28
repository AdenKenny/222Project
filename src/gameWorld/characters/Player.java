package gameWorld.characters;

import java.util.List;

import gameWorld.Room;
import gameWorld.World.Direction;

public class Player extends Character {
	private final int baseXP = 100;	// xp required for initial level
	private final double xpCurveFactor = 2.2; // both constants required to calculate later levels' xp

	private int maxHealth = 100;
	private int health = this.maxHealth;
	private int damage = 10;
	private int level = 1;
	private int xp = 0;
	private int xpToNextLevel = this.baseXP;

	public Player(Room room, int x, int y, String name, String description, Direction facing) {
		super(room, x, y, description, facing, 0, null);
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public int getDamage() {
		return this.damage;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	public int GetXp() {
		return this.xp;
	}

	public void addXP(int xp) {
		this.xp += xp;
		if (xp > this.xpToNextLevel) {
			// in case an instant-level item is added,
			// just design these methods in a way that
			// allows it to work just by calling levelUp()
			int tempXP = xp - this.xpToNextLevel;
			levelUp();
			this.xp = tempXP;
		}
	}

	public void levelUp() {
		this.maxHealth += 50;
		this.health = this.maxHealth;
		this.damage += 5;
		this.xp = 0;
		this.level++;

		this.xpToNextLevel = 100 + (int)(Math.pow(this.level-1, this.xpCurveFactor));
	}

}