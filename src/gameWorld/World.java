package gameWorld;

import java.util.ArrayList;
import java.util.Random;

public class World {
	private ArrayList<Floor> floors;
	
	private Random rng = new Random(System.currentTimeMillis());
	
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
