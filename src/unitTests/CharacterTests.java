package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gameWorld.Entity;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.Character.Type;
import gameWorld.characters.CharacterBuilder;
import gameWorld.characters.CharacterModel;
import gameWorld.characters.PlayerBuilder;

public class CharacterTests {
	
	@Test
	public void testBuilderBuilder() {
		
		//Builder
		
	}
	
	@Test
	public void testPlayerBuilder() {
		PlayerBuilder builder = new PlayerBuilder();
		builder.setName("Test Player");
		builder.setDescription("A test player");
		builder.setID("9");
		builder.setItems("1,2,3");
		builder.setEquips("1");
		builder.setGold("12");
		builder.setHealth("100");
		builder.setXp("23");
		builder.setValue("1");	// level
		builder.setType("PLAYER");
		
		Character c = builder.build();
		assertNotNull(c);
		
		// values directly set by the builder
		assertEquals("Test Player", c.name());
		assertEquals("A test player", c.description());
		assertEquals(9, c.ID());
		List<Integer> items = c.getItems();
		assertEquals(1, (int)items.get(0));
		assertEquals(2, (int)items.get(1));
		assertEquals(3, (int)items.get(2));
		assertEquals(1, (int)c.getEquips().get(0).getID());
		assertEquals(12, c.getGold());
		assertEquals(100, c.getHealth());
		assertEquals(23, c.getXp());
		assertEquals(1, c.getLevel());
		assertEquals(Type.PLAYER, c.getType());
		assertTrue(c.isPlayer());
		
		// values that should be set in the constructor
		assertFalse(c.isAlive());
		assertNull(c.room());
		assertEquals(-1, c.xPos());
		assertEquals(-1, c.yPos());
		assertEquals(100, c.getXpForLevel());
		assertEquals(10, c.getDamage());
		
		// and just another quick check to make sure ID's are working properly
		assertEquals(10, Entity.getNewID());
	}
	
	@Test
	public void testCharacterBuilderForMonster() {
		CharacterBuilder builder = new CharacterBuilder();
		builder.setName("Test Monster");
		builder.setDescription("A test monster");
		builder.setID("3");
		builder.setItems("12, 7");
		builder.setType("MONSTER");
		builder.setValue("1");
		
		CharacterModel model = builder.build();
		assertNotNull(model);
		
		// values that the builder sets directly
		assertEquals("Test Monster", model.getName());
		assertEquals("A test monster", model.getDescription());
		assertEquals(3, model.getID());
		List<Integer> items = new ArrayList<>();
		items.add(12);
		items.add(7);
		for (Integer item : model.getSetOfItems()) {
			if (items.contains(item)) {
				items.remove(item);
			} else {
				fail("CharacterModel.getSetOfItems contains " + item + ", which it should not");
			}
		}
		if (!items.isEmpty()) {
			fail("CharacterModel.getSetOfItems does not contain: " + items.toString());
		}
		assertEquals(Type.MONSTER, model.getType());
		assertEquals(1, model.getValue());
		
		Character c = new Character(null, -1, -1, Direction.NORTH, 1, model);
		
		// values that should be gotten directly from the model
		assertEquals("Test Monster", c.name());
		assertEquals("A test monster", c.description());
		assertEquals(3, c.getModelID());
		items.add(12);
		items.add(7);
		for (Integer item : c.getItems()) {
			if (items.contains(item)) {
				items.remove(item);
			} else {
				fail("Character.getItems contains " + item + ", which it should not");
			}
		}
		if (!items.isEmpty()) {
			fail("Character.getItems does not contains: " + items.toString());
		}
		assertEquals(Type.MONSTER, c.getType());
		assertFalse(c.isPlayer());
		assertEquals(1, c.getRank());
		assertEquals(null, c.room());
		assertEquals(-1, c.xPos());
		assertEquals(-1, c.yPos());
		assertEquals(Direction.NORTH, c.facing());
		assertEquals(1, c.getLevel());
		
		// values that should be set in the constructor
		assertEquals(45, c.getHealth());
		assertEquals(45, c.getMaxHealth());
		assertEquals(4, c.getDamage());
		assertEquals(3, c.getGold());
		assertEquals(20, c.getXp());
		
		// one last little check
		assertEquals(Entity.getNewID(), c.ID() + 1);
	}
	
	@Test
	public void testCharacterBuilderForVendor() {
		CharacterBuilder builder = new CharacterBuilder();
		builder.setName("Test Vendor");
		builder.setDescription("A test vendor");
		builder.setID("6");
		builder.setItems("5");
		builder.setType("VENDOR");
		builder.setValue("1");
		
		CharacterModel model = builder.build();
		assertNotNull(model);
		
		// values that the builder sets directly
		assertEquals("Test Vendor", model.getName());
		assertEquals("A test vendor", model.getDescription());
		assertEquals(6, model.getID());
		List<Integer> items = new ArrayList<>();
		items.add(5);
		for (Integer item : model.getSetOfItems()) {
			if (items.contains(item)) {
				items.remove(item);
			} else {
				fail("CharacterModel.getSetOfItems contains " + item + ", which it should not");
			}
		}
		if (!items.isEmpty()) {
			fail("CharacterModel.getSetOfItems does not contain: " + items.toString());
		}
		assertEquals(Type.VENDOR, model.getType());
		assertEquals(1, model.getValue());
		
		Character c = new Character(null, -1, -1, Direction.NORTH, -1, model);
		
		// values that should be gotten directly from the model
		assertEquals("Test Vendor", c.name());
		assertEquals("A test vendor", c.description());
		assertEquals(6, c.getModelID());
		items.add(5);
		for (Integer item : c.getItems()) {
			if (items.contains(item)) {
				items.remove(item);
			} else {
				fail("Character.getItems contains " + item + ", which it should not");
			}
		}
		if (!items.isEmpty()) {
			fail("Character.getItems does not contains: " + items.toString());
		}
		assertEquals(Type.VENDOR, c.getType());
		assertFalse(c.isPlayer());
		assertEquals(1, c.getRank());
		assertEquals(null, c.room());
		assertEquals(-1, c.xPos());
		assertEquals(-1, c.yPos());
		assertEquals(Direction.NORTH, c.facing());
		assertEquals(-1, c.getLevel());
		
		// values that should be set in the constructor
		assertEquals(-1, c.getHealth());
		assertEquals(-1, c.getMaxHealth());
		assertEquals(-1, c.getDamage());
		assertEquals(-1, c.getGold());
		assertEquals(-1, c.getXp());
		
		// one last little check
		assertEquals(Entity.getNewID(), c.ID() + 1);
	}
}
