package gameWorld.objects;

import java.util.List;
import java.util.Set;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;

public class StationaryObject extends Entity {

	public enum Type {
		FURNITURE, CHEST, DOOR, DROP
	}

	private Type type;
	private int item;
	private int modelID;

	// private boolean isOpen;
	// private long openTime;

	public StationaryObject(ObjectModel model, Room room, int xPos, int yPos, Direction facing) {
		super(room, xPos, yPos, model.getName(), model.getDescription(), facing);

		this.type = model.getType();
		this.item = model.getItem();
		this.modelID = model.getID();

		// this.isOpen = false;

		if (this.type.equals(Type.CHEST) || this.type.equals(Type.DROP)) {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Open";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						util.Logging.logEvent("StationaryObject", util.Logging.Levels.WARNING,
								"StationaryObject action 'Open' expected Character argument, got "
										+ caller.getClass().getName() + " argument.");
						return;
					}

					Character ch = (Character) caller;
					List<Integer> chItems = ch.getItems();
					chItems.add(open());
				}

				@Override
				public boolean isClientAction() {
					return false;
				}

			});
		}
	}

	/**
	 * Opens this StationaryObject and removes it from the game, then returns
	 * the ID of an Item that was contained within it. Should only be called on
	 * StationaryObjects of Types CHEST or DROP.
	 *
	 * @return The ID of the Item in this StationaryObject
	 */
	public int open() {
		this.room.entities()[this.yPos][this.xPos] = null;
		return this.item;
	}

	/**
	 * Returns the Type of this StationaryObject.
	 *
	 * @return This StationaryObject's Type.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the integer representing the Item in this StationaryObject.
	 *
	 * @return This StationaryObject's Items.
	 */
	public int getItem() {
		return this.item;
	}

	/**
	 * Returns the ID of the model that this StationaryObject is based on.
	 *
	 * @return This StationaryObject's model's ID.
	 */
	public int getModelID() {
		return this.modelID;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

}
