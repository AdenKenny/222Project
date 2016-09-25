package gameWorld.characters;

import java.util.Set;

import gameWorld.item.Item;
import util.AbstractBuilder;

public final class CharacterBuilder implements AbstractBuilder {

	private String buildID;
	private String buildName;
	private String buildType;
	private String buildValue;
	private String buildItems;

	private int ID;
	private String name;
	private Character.Type type;
	private int value;
	private Set<Item> setOfItems;
	
	@Override
	public void setID(String buildID) {
		this.buildID = buildID;
	}

	@Override
	public void setName(String buildName) {
		this.buildName = buildName;
	}

	@Override
	public void setType(String buildType) {
		this.buildType = buildType;
	}

	@Override
	public void setValue(String buildValue) {
		this.buildValue = buildValue;
	}

	public void setBuildItems(String buildItems) {
		//Code to split item set.
		
		System.out.println(buildItems);
		
//		if (items.substring(items.length() - 1, items.length()).equals("}")) {
//			items = items.substring(0, items.length() - 1);
	//
//			String[] arrItems = items.split(" ");
//			String[] newArr = new String[arrItems.length];
//			
//			for(int i = 0; i < arrItems.length; i++) {
//				String val = arrItems[i];
//				
//				if(val.contains(",")) {
//					val = val.replace(",", "");
//				}
//									
//				newArr[i] = val;
//			}
//			
//			for(String r : newArr) {
//				System.out.println(r);
//			}
//		}
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Character.Type getType() {
		return this.type;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public Set<Item> getSaleValue() {
		return this.setOfItems;
	}

	@Override
	public CharacterModel build() {
		
		if (this.buildID == null || this.buildName == null || this.buildType == null || this.buildValue == null
				|| this.buildItems == null) {
			return null;
		}
		
		
		return new CharacterModel();
	}

	

}
