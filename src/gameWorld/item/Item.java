package gameWorld.item;

import java.util.ArrayList;
import java.util.List;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.characters.Character;
import ui.appwindow.MainWindow;
import util.Buildable;

public class Item implements Buildable, Cloneable {
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

		this.actions = new ArrayList<Action>();
		addActions();
	}

	public Item(Item item) {
		this.ID = item.getID();
		this.name = item.getName();
		this.type = item.getType();
		this.value = item.getValue();
		this.saleValue = item.getSaleValue();
		this.description = item.getDescription();

		this.actions = new ArrayList<Action>();
		addActions();
	}

	private void addActions() {
		this.actions.add(new Action() {

			@Override
			public String name() {
				return "Inspect";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof MainWindow)) {
					return;
				}

				MainWindow mw = (MainWindow) caller;

				mw.addGameChat(getNiceName()+": "+description);
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
					return;
				}

				Character ch = (Character) caller;
				Entity[][] entities = ch.room().entities();
				for (Entity[] es : entities) {
					for (Entity e : es) {
						if (e instanceof Character) {
							Character c = (Character) e;
							if (c.getType().equals(Character.Type.VENDOR)) {
								ch.sellItem(ID, saleValue * c.getRank());
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
		default:
			break;
		}

		this.actions.add(new Action() {
			@Override
			public String name() {
				return "Pick up";
			}

			@Override
			public void perform(Object caller) {
				if (!(caller instanceof Character)) {
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

		if (this.holder != null) {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Sell";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						return;
					}

					Character ch = (Character) caller;

					trySell(ch);
				}

				@Override
				public boolean isClientAction() {
					return false;
				}
			});
		} else {
			this.actions.add(new Action() {
				@Override
				public String name() {
					return "Buy";
				}

				@Override
				public void perform(Object caller) {
					if (!(caller instanceof Character)) {
						return;
					}

					Character ch = (Character) caller;

					tryBuy(ch);
				}

				@Override
				public boolean isClientAction() {
					return false;
				}
			});
		}

	}

	public void tryEquip(Character equipper) {
		if (equipper.equals(this.holder)) {
			this.holder.equip(this);
		}
	}

	public void tryPickUp(Character pickerUpperer) {
		if (this.holder == null) {
			pickerUpperer.pickUp(this);
			this.holder = pickerUpperer;
		}
	}

	public void trySell(Character seller) {
		if (seller.equals(this.holder)) {
			Entity[][] entities = this.holder.room().entities();
			for (int i = 0; i < entities.length; ++i) {
				for (int j = 0; j < entities[i].length; ++j) {
					if (entities[i][j] instanceof Character) {
						Character ch = (Character) entities[i][j];
						if (ch.getType().equals(Character.Type.VENDOR)) {
							this.holder.sellItem(this.ID, (int) (this.saleValue * (1 + ((double) ch.getRank()) / 10)));
						}
					}
				}
			}
		}
	}

	public void tryBuy(Character buyer) {
		if (this.holder == null) {
			Entity[][] entities = buyer.room().entities();
			for (int i = 0; i < entities.length; ++i) {
				for (int j = 0; j < entities.length; ++j) {
					if (entities[i][j] instanceof Character) {
						Character ch = (Character) entities[i][j];
						if (ch.getType().equals(Character.Type.VENDOR)) {
							if (ch.getItems().contains(this.ID)) {
								int amount = (int) (this.saleValue * (1.2 + ((double) ch.getRank()) / 10));
								if (buyer.getGold() >= amount) {
									buyer.buyItem(this, amount);
								}
							}
						}
					}
				}
			}
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

	public Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

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
		} catch (CloneNotSupportedException e) {

		}
		return null;
	}

}