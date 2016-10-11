package unitTests;

import org.junit.Test;

import clientServer.Game;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gameWorld.Action;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.characters.Character.Type;
import gameWorld.characters.CharacterBuilder;
import gameWorld.characters.CharacterModel;
import gameWorld.characters.PlayerBuilder;
import gameWorld.item.Item;
import gameWorld.rooms.Room;

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
		builder.setValue("1"); // level
		builder.setType("PLAYER");

		Character c = builder.build();
		assertNotNull(c);

		// values directly set by the builder
		assertEquals("Test Player", c.name());
		assertEquals("A test player", c.description());
		assertEquals(9, c.ID());
		int[] items = c.getItems();
		assertEquals(1, (int) items[0]);
		assertEquals(2, (int) items[1]);
		assertEquals(3, (int) items[2]);
		assertEquals(1, (int) c.getEquipIndexes()[0]);
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
		assertFalse(c.isAlive());
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
		assertFalse(c.isAlive());
	}

	@Test
	public void testCombat() {
		PlayerBuilder playerBuilder = new PlayerBuilder();
		playerBuilder.setName("Test Player");
		playerBuilder.setDescription("A test player");
		playerBuilder.setID("9");
		playerBuilder.setItems("");
		playerBuilder.setEquips("");
		playerBuilder.setGold("0");
		playerBuilder.setHealth("100");
		playerBuilder.setXp("0");
		playerBuilder.setValue("1"); // level
		playerBuilder.setType("PLAYER");

		Character player = playerBuilder.build();

		CharacterBuilder monsterBuilder = new CharacterBuilder();
		monsterBuilder.setName("Test Monster");
		monsterBuilder.setDescription("A test monster");
		monsterBuilder.setID("3");
		monsterBuilder.setItems("12, 7");
		monsterBuilder.setType("MONSTER");
		monsterBuilder.setValue("1");

		CharacterModel model = monsterBuilder.build();
		Character monster = new Character(null, -1, -1, Direction.NORTH, 1, model);

		Room room = new Room(null, -1, -1, 9, 9);

		player.respawn(room, 0, 4, Direction.NORTH);

		monster.respawn(room, 4, 4, Direction.NORTH);

		room.entities()[4][0] = player;
		room.entities()[4][4] = monster;

		assertTrue(monster.isAlive());
		assertTrue(player.isAlive());

		assertEquals(45, monster.getHealth());

		Action attack = null;

		for (Action a : monster.actions()) {
			if (a.name().equals("Attack")) {
				attack = a;
			}
		}

		assertNotNull("Action 'Attack' not found on monster Character", attack);

		try {
			Field attackTimer = player.getClass().getDeclaredField("attackTimer");
			attackTimer.setAccessible(true);

			attack.perform(player);

			assertEquals(45, monster.getHealth());

			player.setXPos(3);
			room.entities()[4][0] = null;
			room.entities()[4][3] = player;

			attack.perform(player);

			assertEquals(10, player.getDamage());
			assertEquals(10, player.getAttack());
			assertEquals(35, monster.getHealth());

			player.setXPos(5);
			room.entities()[4][3] = null;
			room.entities()[4][5] = player;
			// reset player's attack timer
			attackTimer.set(player, 0);

			attack.perform(player);

			assertEquals(25, monster.getHealth());

			player.setXPos(4);
			player.setYPos(3);
			room.entities()[4][5] = null;
			room.entities()[3][4] = player;
			// reset player's attack timer
			attackTimer.set(player, 0);

			attack.perform(player);

			assertEquals(15, monster.getHealth());

			player.setYPos(5);
			room.entities()[3][4] = null;
			room.entities()[5][4] = player;
			// reset player's attack timer
			attackTimer.set(player, 0);

			attack.perform(player);

			assertEquals(5, monster.getHealth());

			// reset player's attack timer
			attackTimer.set(player, 0);

			attack.perform(player);

			assertFalse(monster.isAlive());
			assertEquals(3, player.getGold());
			assertEquals(20, player.getXp());
			assertNull(room.entities()[monster.yPos()][monster.xPos()]);

		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTrading() {
		PlayerBuilder playerBuilder = new PlayerBuilder();
		playerBuilder.setName("Test Player");
		playerBuilder.setDescription("A test player");
		playerBuilder.setID("9");
		playerBuilder.setItems("3");	// bronze dagger, cost 10
		playerBuilder.setEquips("");
		playerBuilder.setGold("20");
		playerBuilder.setHealth("100");
		playerBuilder.setXp("0");
		playerBuilder.setValue("1"); // level
		playerBuilder.setType("PLAYER");

		Character player = playerBuilder.build();

		CharacterBuilder vendoBuilder = new CharacterBuilder();
		vendoBuilder.setName("Test Vendor");
		vendoBuilder.setDescription("A test vendor");
		vendoBuilder.setID("6");		// bronze long sword, cost 28
		vendoBuilder.setItems("5");
		vendoBuilder.setType("VENDOR");
		vendoBuilder.setValue("1");

		CharacterModel model = vendoBuilder.build();
		Character vendor = new Character(null, -1, -1, Direction.NORTH, -1, model);

		Room room = new Room(null, -1, -1, 9, 9);

		player.respawn(room, 2, 2, Direction.NORTH);
		vendor.respawn(room, 0, 0, Direction.NORTH);

		room.entities()[2][2] = player;
		room.entities()[0][0] = vendor;

		Item sellItem = Game.mapOfItems.get(player.getItems()[0]);
	}
}