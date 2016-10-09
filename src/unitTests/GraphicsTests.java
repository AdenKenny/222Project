package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.javafx.scene.traversal.Direction;

import Graphics.GraphicsPanel;
import clientServer.Player;
import gameWorld.World;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;

public class GraphicsTests extends GraphicsPanel {

	public GraphicsTests() {
		super(null, null);
	}

	@Test
	public void testNorthNorth() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.NORTH, new int[] {2, 5}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testNorthNorth2() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.NORTH, new int[] {1, 3}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testNorthEast() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.EAST, new int[] {2, 5}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testNorthEast2() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.EAST, new int[] {0, 2}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testNorthSouth() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.SOUTH, new int[] {2, 5}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testNorthSouth2() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.SOUTH, new int[] {0, 3}, new int[] {4, 5}), Side.Back);
	}

	@Test
	public void testNorthWest() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.WEST, new int[] {2, 5}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testNorthWest2() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.WEST, new int[] {0, 3}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testSouthNorth() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.NORTH, new int[] {8, 5}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testSouthNorth2() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.NORTH, new int[] {8, 4}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testSouthEast() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.EAST, new int[] {8, 5}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testSouthEast2() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.EAST, new int[] {9, 4}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testSouthSouth() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.SOUTH, new int[] {8, 5}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testSouthSouth2() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.SOUTH, new int[] {10, 7}, new int[] {4, 5}), Side.Front);
	}

	@Test
	public void testSouthWest() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.WEST, new int[] {8, 5}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testSouthWest2() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.WEST, new int[] {8, 5}, new int[] {4, 5}), Side.Left);
	}
	
}
