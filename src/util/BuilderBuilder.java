package util;

import java.util.HashMap;
import java.util.Map;

import gameWorld.characters.CharacterBuilder;
import gameWorld.item.ItemBuilder;
import gameWorld.objects.ObjectBuilder;

/**
 * A class to build a builder that builds other items.
 * The builder that we build here must implement 'AbstractBuilder'. No other builders are supported.
 * 
 * I like builders okay?
 * 
 * @author Aden
 */

public final class BuilderBuilder implements AbstractBuilderBuilder {
	
	private Map<String, String> fields; //A map representing the fields in the builder that will be built.
	private String type; //The type of the builder we will build with this class.
	
	/**
	 * Requires a string representing the type of object we want to build i.e "Character".
	 * 
	 * @param type The type of object we want to build.
	 */
	
	public BuilderBuilder(String type) {
		this.fields = new HashMap<>();
		this.type = type;
	}
	
	/**
	 * Adds a field to our builder that we will build with this builder that we are building.
	 * That's a bit of a mouthful.
	 * 
	 * @param key A string representing the name of the field. I.e. "ID" or "name".
	 * @param value A string representing the value of the field we are building.
	 */
	
	@Override
	public void addField(String key, String value) {
		this.fields.put(key, value);
	}
	
	/**
	 * Gets the type of builder we want to build. This is got from the type that was specified
	 * when we created the instance of this class (BuilderBuilder).
	 * 
	 * @return A builder of the required type.
	 * @throws UnsupportedOperationException Thrown if the type is Room or Player. Neither of these can be
	 * 										 built this way. Should be caught by a method calling this method.
	 */
	
	public AbstractBuilder getType() throws UnsupportedOperationException {
		switch(this.type) { 
		case "Object":
			return new ObjectBuilder();
		case "Player":
			throw new UnsupportedOperationException(); //Can't do. Send error up hierarchy.
		case "Room":
			throw new UnsupportedOperationException(); //Can't do. Send error up hierarchy.
		case "Item":
			return new ItemBuilder();
		case "Character":
			return new CharacterBuilder();
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * Builds a builder of the type specified when the BuilderBuilder was created. The builder
	 * will build with the fields that we passed to it using the the 'addField()' method.
	 * 
	 * @return A builder with which we can use to build the object (that should implement 'Buildable') that we want to build.
	 * @throws UnsupportedOperationException Thrown if we're using BuilderBuilder to create unsupported builders.
	 * @throws IllegalArgumentException Thrown if we try to add fields that should not be added to a specific builder.
	 */
	
	public AbstractBuilder build() throws UnsupportedOperationException, IllegalArgumentException {
		
		try {
			AbstractBuilder builder = getType(); //Get the builder that we will work on.
			
			for(String s : this.fields.keySet()) { //Go through the fields.
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
					if(builder instanceof ItemBuilder) { //Items aren't relevant to an item.
						throw new IllegalArgumentException();
					}
					builder.setItems(this.fields.get(s));
					break;
				case "saleValue":
					if(!(builder instanceof ItemBuilder)) { //Sale value is only relevant to items.
						throw new IllegalArgumentException();
					}
					builder.setSaleValue(this.fields.get(s));
					break;
				default:
					break;
				}
			}
			
			return builder; //Return our builder.
		}
		
		catch (UnsupportedOperationException e) { //RoomBuilder or PlayerBuilder should not be built this way. 
			throw new UnsupportedOperationException();
		}
		
		catch (IllegalArgumentException e) { //This is an irrelevant field for this builder.
			throw e;
		}
	}
}