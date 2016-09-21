package gameWorld.characters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import gameWorld.Location;
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

	public Player(Location location, String name, String description, Direction facing) {
		super(location, name, description, facing);
	}
	
	public void action_Inspect() {
		// TODO: display a message to the user like
		// 'Another adventurer, just like you.'
	}

	@Override
	public List<String> actions() {
		List<String> actions = new ArrayList<String>();
		
		for (Method m : this.getClass().getMethods()) {
			if (m.getName().startsWith("action_")) {
				actions.add(methodToAction(m.getName()));
			}
		}
		
		return actions;
	}

	@Override
	public boolean performAction(String action) {
		String methodName = actionToMethod(action);
		
		for (Method m : this.getClass().getMethods()) {
			if (m.getName().equals(methodName)) {
				try {
					m.invoke(this);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
			}
		}
		
		return false;
	}

	@Override
	protected String methodToAction(String methodName) {
		return methodName.replaceFirst("action_", "")
				.replace("_", " ");
		
	}
	
	@Override
	protected String actionToMethod(String action) {
		return "action_" + action.replace(" ", "_");
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
			xp = tempXP;
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
