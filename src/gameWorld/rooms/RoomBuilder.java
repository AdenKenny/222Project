package gameWorld.rooms;

import gameWorld.Room;

public final class RoomBuilder {

	private String buildPlayerSpawn;
	private String buildNpcSpawn;
	private String buildModelID;
	private String buildXPos;
	private String buildYPos;
	private String buildWidth;
	private String buildDepth;

	private boolean playerSpawn;
	private boolean npcSpawn;
	private int modelID;
	private int xPos;
	private int yPos;
	private int width;
	private int depth;

	public void setBuildPlayerSpawn(String buildPlayerSpawn) {
		this.buildPlayerSpawn = buildPlayerSpawn;
	}

	public void setBuildNpcSpawn(String buildNpcSpawn) {
		this.buildNpcSpawn = buildNpcSpawn;
	}

	public void setBuildModelID(String buildModelID) {
		this.buildModelID = buildModelID;
	}

	public void setBuildXPos(String buildXPos) {
		this.buildXPos = buildXPos;
	}

	public void setBuildYPos(String buildYPos) {
		this.buildYPos = buildYPos;
	}

	public void setBuildWidth(String buildWidth) {
		this.buildWidth = buildWidth;
	}

	public void setBuildDepth(String buildDepth) {
		this.buildDepth = buildDepth;
	}

	public boolean isPlayerSpawn() {
		return this.playerSpawn;
	}

	public boolean isNpcSpawn() {
		return this.npcSpawn;
	}

	public int getModelID() {
		return this.modelID;
	}

	public int getxPos() {
		return this.xPos;
	}

	public int getyPos() {
		return this.yPos;
	}

	public int getWidth() {
		return this.width;
	}

	public int getDepth() {
		return this.depth;
	}

	public Room build() {

		if(this.buildPlayerSpawn == null || this.buildNpcSpawn == null || this.buildModelID == null || this.buildXPos == null
				|| this.buildYPos == null || this.buildWidth == null || this.buildDepth == null){
			return null;
		}

		try {
			this.playerSpawn = Boolean.parseBoolean(this.buildPlayerSpawn);
			this.npcSpawn = Boolean.parseBoolean(this.buildNpcSpawn);
			this.modelID = Integer.parseInt(this.buildModelID);
			this.xPos = Integer.parseInt(this.buildXPos);
			this.yPos = Integer.parseInt(this.buildYPos);
			this.width = Integer.parseInt(this.buildWidth);
			this.depth = Integer.parseInt(this.buildDepth);

			if (this.playerSpawn) {
				//return new PlayerSpawn(this);
			} else if (this.npcSpawn) {
				// TODO: make NPCSpawn class
			} else {
				//return new Room(this);
			}

		}

		catch (NumberFormatException e) {

		}

		return null;
	}

}
