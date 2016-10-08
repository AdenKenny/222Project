package util;

import java.util.HashMap;
import java.util.Map;

import gameWorld.characters.PlayerBuilder;
import gameWorld.item.ItemBuilder;
import gameWorld.objects.ObjectBuilder;

public class BuilderBuilder {
	
	private Map<String, String> fields;
	private String type;
	
	public BuilderBuilder(String type) {
		this.fields = new HashMap<>();
		this.type = type;
	}
	
	public void addField(String key, String value) {
		this.fields.put(key, value);
	}
	
	private AbstractBuilder getType() throws UnsupportedOperationException {
		switch(this.type) {
		case "Object":
			return new ObjectBuilder();
		case "Player":
			return new PlayerBuilder();
		case "Room":
			throw new UnsupportedOperationException();
		case "Item":
			return new ItemBuilder();
		default:
			break;
		}
		
		return null;
	}
	
	public AbstractBuilder build() throws UnsupportedOperationException {
		
		try {
			AbstractBuilder builder = getType();
			
			for(String s : this.fields.keySet()) {
				switch(s) {
				
				case "ID":
					builder.setID(this.fields.get(s));
					break;
				case "name":
					builder.setName(this.fields.get(s));
					break;
				case "type":
					builder.setType(this.fields.get(s));
					break;
				case "value":
					builder.setValue(this.fields.get(s));
					break;
				case "description":
					builder.setDescription(this.fields.get(s));
					break;
				case "items":
					builder.setItems(this.fields.get(s));
					break;
				default:
					break;
				}
			}
			return builder;
		}
		
		catch (UnsupportedOperationException e) { //RoomBuilder should not be built this way. 
			throw new UnsupportedOperationException();
		}
	}
}