package gameWorld.rooms;

import gameWorld.Floor;
import util.AlsoBuildable;

/**
 * A class to build an instance of a room.
 *
 * @author Aden
 */
public final class RoomBuilder implements AlsoBuildable {

	private String buildPlayerSpawn;
	private String buildNpcSpawn;
	private String buildTargetRoom;
	private String buildModelID;
	private String buildXPos;
	private String buildYPos;
	private String buildWidth;
	private String buildDepth;
	private String buildLevel;

	private boolean playerSpawn;
	private boolean npcSpawn;
	private boolean targetRoom;
	private int modelID;
	private int xPos;
	private int yPos;
	private int width;
	private int depth;
	private int level;

	private Floor floor;

	public RoomBuilder(Floor floor) {
		this.floor = floor;
	}

	public RoomBuilder() {
		
	}
	
	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	/**
	 * Sets the value for whether the Room that is being built is a PlayerSpawn
	 * or not. This value will be read from a file.
	 * 
	 * @param buildPlayerSpawn
	 *            Whether the Room is a PlayerSpawn
	 */
	public void setBuildPlayerSpawn(String buildPlayerSpawn) {
		this.buildPlayerSpawn = buildPlayerSpawn;
	}

	/**
	 * Sets the value for whether the Room that is being built is a NPCSpawn or
	 * not. This value will be read from a file.
	 * 
	 * @param buildNpcSpawn
	 *            Whether the Room is a NPCSpawn
	 */
	public void setBuildNpcSpawn(String buildNpcSpawn) {
		this.buildNpcSpawn = buildNpcSpawn;
	}

	/**
	 * Sets the value for whether the Room that is being built is a TargetRoom
	 * or not. This value will be read from a file.
	 * 
	 * @param buildTargetRoom
	 *            Whether the Room is a TargetRoom
	 */
	public void setBuildTargetRoom(String buildTargetRoom) {
		this.buildTargetRoom = buildTargetRoom;
	}

	/**
	 * Sets the ID of the model of the NPC that will be spawned by the Room that
	 * is being built if this Room is a NPCSpawn.
	 * 
	 * @param modelID
	 *            the model ID of the NPC that will be spawned
	 */
	public void setBuildModelID(String modelID) {
		this.buildModelID = modelID;
	}

	/**
	 * Sets the x-position of the Room that is being built on its Floor.
	 * 
	 * @param buildXPos
	 *            The x-position of the Room
	 */
	public void setBuildXPos(String buildXPos) {
		this.buildXPos = buildXPos;
	}

	/**
	 * Sets the y-position of the Room that is being built on its Floor.
	 * 
	 * @param buildYPos
	 *            The y-position of the Room
	 */
	public void setBuildYPos(String buildYPos) {
		this.buildYPos = buildYPos;
	}

	/**
	 * Sets the width of the Room that is being built.
	 * 
	 * @param buildWidth
	 *            The width of the Room
	 */
	public void setBuildWidth(String buildWidth) {
		this.buildWidth = buildWidth;
	}

	/**
	 * Sets the depth of the Room that is being built.
	 * 
	 * @param buildDepth
	 *            The depth of the Room
	 */
	public void setBuildDepth(String buildDepth) {
		this.buildDepth = buildDepth;
	}

	/**
	 * Sets the level of the NPC that will be spawned by the Room that is being
	 * built.
	 * 
	 * @param buildLevel
	 *            The level of the NPC to be spawned
	 */
	public void setLevel(String buildLevel) {
		this.buildLevel = buildLevel;
	}

	/**
	 * Returns the ID of the model of the NPC that will be spawned by the Room
	 * that is being built if the Room is a NPCSpawn.
	 * 
	 * @return The ID of the NPC model
	 */
	public int getmodelID() {
		return this.modelID;
	}

	/**
	 * Returns the x-position of the Room that is being built on its Floor.
	 * 
	 * @return The Room's x-position
	 */
	public int getxPos() {
		return this.xPos;
	}

	/**
	 * Returns the y-position of the Room that is being built on its Floor.
	 * 
	 * @return The Room's y-position
	 */
	public int getyPos() {
		return this.yPos;
	}

	/**
	 * Returns the width of the Room that is being built.
	 * 
	 * @return The Room's width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the depth of the Room that is being built.
	 * 
	 * @return The Room's depth
	 */
	public int getDepth() {
		return this.depth;
	}

	/**
	 * Returns the level of the NPC that will be spawned if the Room that is
	 * being built is a NPCSpawn.
	 * 
	 * @return The level of the NPC
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Builds a Room given all the information that has been passed in. If not
	 * enough information has been passed in or there is an error with one of
	 * the number Strings, returns null. Otherwise, returns the Room that is
	 * built from the given information.
	 * 
	 * @return The Room that is built from the given information.
	 */
	public Room build() {

		if (this.buildPlayerSpawn == null || this.buildNpcSpawn == null || this.buildTargetRoom == null
				|| this.buildModelID == null || this.buildXPos == null || this.buildYPos == null
				|| this.buildWidth == null || this.buildDepth == null || this.buildLevel == null) {

			return null;
		}

		try {
			this.playerSpawn = Boolean.parseBoolean(this.buildPlayerSpawn);
			this.npcSpawn = Boolean.parseBoolean(this.buildNpcSpawn);
			this.targetRoom = Boolean.parseBoolean(this.buildTargetRoom);
			this.modelID = Integer.parseInt(this.buildModelID);
			this.xPos = Integer.parseInt(this.buildXPos);
			this.yPos = Integer.parseInt(this.buildYPos);
			this.width = Integer.parseInt(this.buildWidth);
			this.depth = Integer.parseInt(this.buildDepth);
			this.level = Integer.parseInt(this.buildLevel);

			if (this.playerSpawn) {
				return new PlayerSpawn(this.floor, this);
			} else if (this.npcSpawn) {
				return new NPCSpawn(this.floor, this);
			} else if (this.targetRoom) {
				return new TargetRoom(this.floor, this);
			} else {
				return new Room(this.floor, this);
			}
		} catch (NumberFormatException e) {

		}

		return null;
	}

}