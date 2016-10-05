package gameWorld.objects;

import java.util.List;
import java.util.Set;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.Room;
import gameWorld.World.Direction;
import gameWorld.characters.Character;

public class StationaryObject extends Entity {

	public enum Type {
		FURNITURE,
		CHEST,
		DOOR,
		// TODO: DROP
	}

	private Type type;
	private Set<Integer> items;
	private int modelID;

	private boolean isOpen;
	private long openTime;

	public StationaryObject(ObjectModel model, Room room, int xPos, int yPos, Direction facing) {
		super(room, xPos, yPos, model.getName(), model.getDescription(), facing);

		this.type = model.getType();
		this.items = model.getItems();
		this.modelID = model.getID();

		this.isOpen = false;

		if (this.type.equals(Type.CHEST)) {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Open";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						return;
					}

					Character ch = (Character) caller;
					List<Integer> chItems = ch.getItems();
					chItems.add(getRandomItem());
				}

				@Override
				public boolean isClientAction() {
					return false;
				}

			});
		}
	}

	public Integer getRandomItem() {
		if (this.type.equals(Type.CHEST)) {
			Integer[] ints = this.items.toArray(new Integer[this.items.size()]);
			return ints[(int) (Math.random() * ints.length)];
		}
		return null;
	}

	public Type getType() {
		return this.type;
	}

	public Set<Integer> getItems() {
		return this.items;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

}
