package gameWorld.item;

import java.util.ArrayList;
import java.util.List;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.characters.Character;
import ui.appwindow.MainWindow;
import util.Buildable;
import util.Logging;

/**
 * A class to represent an Item that is never physically present in the game on
 * its own.
 *
 * @author Louis
 */
public class Item implements Buildable, Cloneable {
	/**
	 * An enumeration to represent the different types of Items that there are.
	 *
	 * @author Louis
	 */
	public enum Type {
		WEAPON, SHIELD, ARMOR, HELMET, POTION, QUEST, TRASH
	}

	private int ID;
	private String name;
	private Type type;
	private int value;
	private int saleValue;
	private String description;

	private Character holder;
	// TODO: private Container container;
	private List<Action> actions;

	public Item(ItemBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.saleValue = builder.getSaleValue();
		this.description = builder.getDescription();

		this.actions = new ArrayList<>();
		addActions();
	}

	public Item(Item item) {
		this.ID = item.getID();
		this.name = item.getName();
		this.type = item.getType();
		this.value = item.getValue();
		this.saleValue = item.getSaleValue();
		this.description = item.getDescription();

		this.actions = new ArrayList<>();
		addActions();
	}

	/**
	 * Adds all relevant Actions to this Item's List of Actions, so that they
	 * may then be performed on this Item.
	 */
	private void addActions() {
		this.actions.add(new Action() {

			@Override
			public String name() {
				return "Inspect";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof MainWindow)) {
					util.Logging.logEvent("Item", util.Logging.Levels.WARNING,
							"Item action 'Inspect' expected MainWindow argument, got " + caller.getClass().getName()
									+ " argument.");
					return;
				}

				MainWindow mw = (MainWindow) caller;

				mw.addGameChat(getNiceName() + ": " + description);
			}

			@Override
			public boolean isClientAction() {
				return true;
			}

		});

		this.actions.add(new Action() {
			@Override
			public String name() {
				return "Sell";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof Character)) {
					util.Logging.logEvent("Item", util.Logging.Levels.WARNING,
							"Item action 'Sell' expected Character argument, got " + caller.getClass().getName()
									+ " argument.");
					return;
				}

				Character seller = (Character) caller;
				Entity[][] entities = seller.room().entities();
				for (Entity[] es : entities) {
					for (Entity e : es) {
						if (e instanceof Character) {
							Character c = (Character) e;
							if (c.getType().equals(Character.Type.VENDOR)) {
								int price = saleValue + (saleValue * (c.getRank() - 1) / 5);
								seller.sellItem(ID, price);
								return;
							}
						}
					}
				}
			}

			@Override
			public boolean isClientAction() {
				return false;
			}
		});

		switch (this.type) {
		case WEAPON:
		case SHIELD:
		case ARMOR:
		case HELMET:
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Equip";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						util.Logging.logEvent("Item", util.Logging.Levels.WARNING,
								"Item action 'Equip' expected Character argument, got " + caller.getClass().getName()
										+ " argument.");
						return;
					}

					Character ch = (Character) caller;

					tryEquip(ch);
				}

				@Override
				public boolean isClientAction() {
					return false;
				}
			});
			break;
		case POTION:
			// TODO: add drink/use action
		case QUEST:
		case TRASH:
		default:
			break;
		}

		this.actions.add(new Action() {
			@Override
			public String name() {
				return "Pick Up";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof Character)) {
					util.Logging.logEvent("Item", util.Logging.Levels.WARNING,
							"Item action 'Pick Up' expected Character argument, got " + caller.getClass().getName()
									+ " argument.");
					return;
				}

				Character ch = (Character) caller;

				tryPickUp(ch);
			}

			@Override
			public boolean isClientAction() {
				return false;
			}
		});

	}

	/**
	 * Returns the actions that may be performed on this Item.
	 *
	 * @return the List of actions
	 */
	public List<Action> actions() {
		return this.actions;
	}

	/**
	 * Tries to equip this Item to the specified Character.
	 *
	 * @param equipper
	 *            The Character to equip to.
	 */
	public void tryEquip(Character equipper) {
		if (equipper.equals(this.holder)) {
			this.holder.equip(this);
		}
	}

	/**
	 * Tries to make the specified Character pick up this item.
	 *
	 * @param pickerUpperer
	 *            The Character doing the picking up.
	 */
	public void tryPickUp(Character pickerUpperer) {
		if (this.holder == null) {
			pickerUpperer.pickUp(this);
			this.holder = pickerUpperer;
		}
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Returns a nicer, more readable version of this Item's name.
	 *
	 * @return A nicer version of this Item's name
	 */
	public String getNiceName() {
		String niceName = name;

		niceName = java.lang.Character.toUpperCase(niceName.charAt(0)) + niceName.substring(1);

		for (int i = 1; i < niceName.length(); ++i) {
			if (java.lang.Character.isUpperCase(niceName.charAt(i))) {
				niceName = niceName.substring(0, i) + " " + niceName.substring(i);
				++i;
			}
		}

		return niceName;
	}

	/**
	 * Returns the Type of Item that this Item is.
	 *
	 * @return This Item's Type.
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns this Item's ideal sale value. This is not necessarily how much it
	 * should be bought or sold for, but it is how much it is worth, objectively
	 * speaking.
	 *
	 * @return This Item's sale value
	 */
	public int getSaleValue() {
		return this.saleValue;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.ID;
		result = prime * result + ((this.actions == null) ? 0 : this.actions.hashCode());
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + ((this.holder == null) ? 0 : this.holder.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.saleValue;
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = prime * result + this.value;
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
		Item other = (Item) obj;
		if (this.ID != other.ID)
			return false;
		if (this.actions == null) {
			if (other.actions != null)
				return false;
		} else if (!this.actions.equals(other.actions))
			return false;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.holder == null) {
			if (other.holder != null)
				return false;
		} else if (!this.holder.equals(other.holder))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.saleValue != other.saleValue)
			return false;
		if (this.type != other.type)
			return false;
		if (this.value != other.value)
			return false;
		return true;
	}

	@Override
	public Item clone() {
		try {
			return (Item) super.clone();
		}

		catch (CloneNotSupportedException e) {
			Logging.logEvent(Item.class.getName(), Logging.Levels.WARNING, "Failed to clone an item");
		}

		return null;
	}
}