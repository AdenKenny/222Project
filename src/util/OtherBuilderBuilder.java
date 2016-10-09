package util;

import java.util.HashMap;
import java.util.Map;

import gameWorld.characters.PlayerBuilder;
import gameWorld.rooms.RoomBuilder;

public final class OtherBuilderBuilder implements AbstractBuilderBuilder {

	private Map<String, String> fields; //A map representing the fields in the builder that will be built.
	private String type; //The type of the builder we will build with this class.

	public OtherBuilderBuilder(String type) {
		this.fields = new HashMap<>();
		this.type = type;
	}
	
	@Override
	public void addField(String key, String value) {
		this.fields.put(key, value);
	}

	public AlsoBuildable getType() throws UnsupportedOperationException {
		switch(this.type) { 
		case "Object":
			throw new UnsupportedOperationException(); //Can't do. Send error up hierarchy.
		case "Player":
			return new PlayerBuilder();
		case "Room":
			return new RoomBuilder();
		case "Item":
			throw new UnsupportedOperationException(); //Can't do. Send error up hierarchy.
		case "Character":
			throw new UnsupportedOperationException(); //Can't do. Send error up hierarchy.
		default:
			break;
		}
		
		return null;
	}

	public AlsoBuildable build() throws UnsupportedOperationException, IllegalArgumentException {
		
		try {
			AlsoBuildable proto = getType();
			
			Object builder; //This is so sketchy.
			
			if(proto instanceof RoomBuilder) {
				builder = new RoomBuilder();
			}
			
			else { 
				builder = new PlayerBuilder();
			}
			
			for(String s : this.fields.keySet()) { //TODO Finish this with roombuilder stuff.
				switch(s) {
				case "ID":
					((PlayerBuilder) builder).setID(this.fields.get(s));
					break;
				case "name":
					((PlayerBuilder) builder).setName(this.fields.get(s));
					break;
				case "type":
					((PlayerBuilder) builder).setType(this.fields.get(s));
					break;
				case "value":
					((PlayerBuilder) builder).setValue(this.fields.get(s));
					break;
				case "items":
					((PlayerBuilder) builder).setItems(this.fields.get(s));
					break;
				case "health":
					((PlayerBuilder) builder).setHealth(this.fields.get(s));
					break;
				case "equips":
					((PlayerBuilder) builder).setEquips(this.fields.get(s));
					break;
				case "xp":
					((PlayerBuilder) builder).setXp(this.fields.get(s));
					break;
				default:
					break;
			
				}
			}
			
			return (AlsoBuildable) builder;
		}
		
		catch (ClassCastException e) {
			throw new UnsupportedOperationException();
		}
		
		catch (UnsupportedOperationException e) {
			throw e;
		}
	}

}
