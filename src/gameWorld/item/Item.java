package gameWorld.item;

import java.util.ArrayList;
import java.util.List;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.characters.Character;
import util.Buildable;

public class Item implements Buildable {
	public enum Type {
		WEAPON,
		SHIELD,
		ARMOR,
		HELMET,
		POTION,
		QUEST,
		TRASH
	}

	private int ID;
	private String name;
	private Type type;
	private int value;
	private int saleValue;
	
	private Character holder;
	// TODO: private Container container;
	private List<Action> actions;

	public Item(ItemBuilder builder) {
		this.ID = builder.getID();
		this.name = builder.getName();
		this.type = builder.getType();
		this.value = builder.getValue();
		this.saleValue = builder.getSaleValue();
		this.actions = new ArrayList<Action>();
		addActions();
	}
	
	public Item(Item item) {
		this.ID = item.getID();
		this.name = item.getName();
		this.type = item.getType();
		this.value = item.getValue();
		this.saleValue = item.getSaleValue();
		this.actions = new ArrayList<Action>();
		addActions();
	}

	private void addActions() {
		switch (type) {
		case WEAPON:
		case SHIELD:
		case ARMOR:
		case HELMET:
			actions.add(new Action() {
				public String name() { return "Equip";}
				public void perform(Character caller) {
					tryEquip(caller);
				}
			});
		default:
			break;
		}
		
		actions.add(new Action() {
			public String name() { return "Pick up";}
			public void perform(Character caller) {
				tryPickUp(caller);
			}
		});
		
		if (holder != null) {
			actions.add(new Action() {
				public String name() { return "Sell";}
				public void perform(Character caller) {
					trySell(caller);
				}
			});
		} else {
			actions.add(new Action() {
				public String name() { return "Buy";}
				public void perform(Character caller) {
					tryBuy(caller);
				}
			});
		}
		
	}
	
	public void tryEquip(Character equipper) {
		if (equipper.equals(holder)) {
			holder.equip(this);
		}
	}
	
	public void tryPickUp(Character pickerUpperer) {
		if (holder == null) {
			pickerUpperer.pickUp(this);
			holder = pickerUpperer;
		}
	}
	
	public void trySell(Character seller) {
		if (seller.equals(holder)) {
			Entity[][] entities = holder.room().entities();
			for (int i = 0; i < entities.length; ++i) {
				for (int j = 0; j < entities[i].length; ++j) {
					if (entities[i][j] instanceof Character) {
						Character ch = (Character) entities[i][j];
						if (ch.getType().equals(Character.Type.VENDOR)) {
							holder.sellItem(this, (int) (saleValue*(1+((double)ch.getRank())/10)));
						}
					}
				}
			}
		}
	}
	
	public void tryBuy(Character buyer) {
		if (holder == null) {
			Entity[][] entities = buyer.room().entities();
			for (int i = 0; i < entities.length; ++i) {
				for (int j = 0; j < entities.length; ++j) {
					if (entities[i][j] instanceof Character) {
						Character ch = (Character) entities[i][j];
						if (ch.getType().equals(Character.Type.VENDOR)) {
							if (ch.getItems().contains(ID)) {
								int amount = (int) (saleValue*(1.2+((double)ch.getRank())/10));
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

}