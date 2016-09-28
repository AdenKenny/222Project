package gameWorld;

import gameWorld.characters.Character;

public abstract class Action {
	private static int IDCount = 0;
	private int ID;
	
	public Action() {
		ID = IDCount++;
	}
	
	public abstract String name();
	public abstract void perform(Character caller);
}
