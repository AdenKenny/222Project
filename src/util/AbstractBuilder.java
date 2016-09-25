package util;

public interface AbstractBuilder {
	
	//Note: No set itemValue or items as implementation differs between items and characters.
	//Note: No type as implementation differs between items and characters.
	
	void setID(String ID);
	
	void setName(String name);
	
	void setType(String type);
	
	void setValue(String value);
	
	int getID();
	
	String getName();
	
	int getValue();
	
	Buildable build();
}
