package gameWorld;

import java.util.ArrayList;
import java.util.Random;

public class World {
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
	
	private ArrayList<Floor> floors;
	
	private Random rng = new Random(System.currentTimeMillis());
	
	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being a random width and depth (each between 10 and 15),
	 * and each Floor having random Room width and depth (between 8 and 10).
	 * 
	 * @param numFloors
	 */
	public World(int numFloors) {
		floors = new ArrayList<Floor>();
		
		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					rng.nextInt(6) + 10,	// 10 - 15
					rng.nextInt(6) + 10,
					rng.nextInt(3) + 8,		// 8 - 10
					rng.nextInt(3) + 8));
			
			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}
	
	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being the specified width and depth,
	 * but each Floor having a random Room width and depth (between 8 and 10).
	 * 
	 * @param numFloors
	 * @param floorWidth
	 * @param floorDepth
	 */
	public World (int numFloors, int floorWidth, int floorDepth) {
		floors = new ArrayList<Floor>();
		
		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					floorWidth,
					floorDepth,
					rng.nextInt(3) + 8,		// 8 - 10
					rng.nextInt(3) + 8));
			
			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}
	
	/**
	 * Constructs a World with the specified number of Floors,
	 * with each Floor being the specified width (floorWidth) and depth (floorDepth),
	 * and each Room being the specified width (roomWidth) and depth (roomDepth).
	 * 
	 * @param numFloors
	 * @param floorWidth
	 * @param floorDepth
	 * @param roomWidth
	 * @param roomDepth
	 */
	public World(int numFloors, int floorWidth, int floorDepth, int roomWidth, int roomDepth) {
		floors = new ArrayList<Floor>();
		
		for (int level = 0; level < numFloors; level++) {
			floors.add(new Floor(
					(level == 0)? null : floors.get(level-1),
					level,
					floorWidth,
					floorDepth,
					roomWidth,
					roomDepth));
			
			if (level > 0) floors.get(level-1).setNextFloor(floors.get(level));
		}
	}
}
