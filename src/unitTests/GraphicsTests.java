package unitTests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.sun.javafx.scene.traversal.Direction;

import Graphics.GraphicsPanel;
import clientServer.Player;
import gameWorld.World;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;

public class GraphicsTests extends GraphicsPanel {

	public GraphicsTests() {
		super();
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
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.WEST, new int[] {0, 3}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testEastNorth() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.NORTH, new int[] {4, 8}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testEastNorth2() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.NORTH, new int[] {4, 8}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testEastEast() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.EAST, new int[] {4, 8}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testEastEast2() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.EAST, new int[] {4, 8}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testEastSouth() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.SOUTH, new int[] {4, 8}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testEastSouth2() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.SOUTH, new int[] {4, 8}, new int[] {4, 5}), Side.Left);
	}

	@Test
	public void testEastWest() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.WEST, new int[] {4, 8}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testEastWest2() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.WEST, new int[] {4, 8}, new int[] {4, 5}), Side.Back);
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
	
	@Test
	public void testWestNorth() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.NORTH, new int[] {4, 1}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testWestNorth2() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.NORTH, new int[] {3, 1}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testWestEast() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.EAST, new int[] {4, 1}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testWestEast2() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.EAST, new int[] {5, 0}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testWestSouth() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.SOUTH, new int[] {4, 1}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testWestSouth2() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.SOUTH, new int[] {3, 0}, new int[] {4, 5}), Side.Right);
	}

	@Test
	public void testWestWest() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.WEST, new int[] {4, 1}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testWestWest2() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.WEST, new int[] {4, 1}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testDiagonalNWEastNorth() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.NORTH, new int[] {3, 4}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testDiagonalNWSouthNorth() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.NORTH, new int[] {3, 4}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testDiagonalNEWestNorth() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.NORTH, new int[] {3, 6}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testDiagonalNESouthNorth() {
		assertEquals(calculateSide(World.Direction.SOUTH, World.Direction.NORTH, new int[] {3, 6}, new int[] {4, 5}), Side.Front);
	}
	
	@Test
	public void testDiagonalSEWestNorth() {
		assertEquals(calculateSide(World.Direction.WEST, World.Direction.NORTH, new int[] {5, 6}, new int[] {4, 5}), Side.Right);
	}
	
	@Test
	public void testDiagonalSENorthNorth() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.NORTH, new int[] {5, 4}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testDiagonalSWEastNorth() {
		assertEquals(calculateSide(World.Direction.EAST, World.Direction.NORTH, new int[] {5, 4}, new int[] {4, 5}), Side.Left);
	}
	
	@Test
	public void testDiagonalSWNorthNorth() {
		assertEquals(calculateSide(World.Direction.NORTH, World.Direction.NORTH, new int[] {5, 6}, new int[] {4, 5}), Side.Back);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesNorth(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.NORTH, 5, 5, 1, 2);
		assertTrue(absolute[0] == 3 && absolute[1] == 6);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesNorth2(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.NORTH, 5, 5, -2, 4);
		assertTrue(absolute[0] == 1 && absolute[1] == 3);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesEast(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.EAST, 5, 5, 4, 8);
		assertTrue(absolute[0] == 9 && absolute[1] == 13);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesEast2(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.EAST, 5, 5, -8, 16);
		assertTrue(absolute[0] == -3 && absolute[1] == 21);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesSouth(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.SOUTH, 5, 5, 16, 32);
		assertTrue(absolute[0] == 37 && absolute[1] == -11);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesSouth2(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.SOUTH, 5, 5, -1, 2);
		assertTrue(absolute[0] == 7 && absolute[1] == 6);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesWest(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.WEST, 5, 5, 2, 4);
		assertTrue(absolute[0] == 3 && absolute[1] == 1);
	}
	
	@Test
	public void testRelativeDeltaToCoordinatesWest2(){
		int[] absolute = calculateCoordinatesFromRelativeDelta(World.Direction.WEST, 5, 5, -4, 8);
		assertTrue(absolute[0] == 9 && absolute[1] == -3);
	}
	
}
