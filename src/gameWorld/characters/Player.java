package gameWorld.characters;

import java.util.List;

import gameWorld.Location;
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

	public Player(Location location, String name, String description, Direction facing) {
		super(location, name, description, facing);
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return this.damage;
	}

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