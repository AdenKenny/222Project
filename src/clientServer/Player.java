package clientServer;

import userHandling.User;
import gameWorld.World.Direction;
import gameWorld.characters.Character;

public class Player {
	private User user;
	private Character character;
	private boolean newlyEntered;
	private Direction toMove;
	private Direction toTurn;
	private Order order;
	
	private enum Order {
		TURN,
		MOVE,
		NONE;
	}
	
	public Player(User user, Character character) {
		this.user = user;
		this.character = character;
		this.newlyEntered = true;
		this.toMove = Direction.NONE;
		this.toTurn = Direction.NONE;
		this.order = Order.NONE;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void doMovement() {
		if (this.order == Order.NONE) {
			return;
		}
		else if (this.order == Order.MOVE) {
			move();
			turn();
		}
		else if (this.order == Order.TURN) {
			turn();
			move();
		}
		this.toMove = Direction.NONE;
		this.toTurn = Direction.NONE;
		this.order = Order.NONE;
	}
	
	private void move() {
		if (this.toMove != Direction.NONE) {
			this.character.move(this.toMove);
		}
	}
	
	private void turn() {
		if (this.toTurn == Direction.LEFT) {
			this.character.turnLeft();
		}
		else if (this.toTurn == Direction.RIGHT) {
			this.character.turnRight();
		}
	}
	
	public Character getCharacter() {
		return this.character;
	}

	public boolean isNewlyEntered() {
		return this.newlyEntered;
	}

	public void setNewlyEntered(boolean newlyEntered) {
		this.newlyEntered = newlyEntered;
	}

	public Direction getToMove() {
		return toMove;
	}

	public void setToMove(Direction toMove) {
		this.toMove = toMove;
		if (this.order == Order.NONE) {
			this.order = Order.MOVE;
		}
	}
	
	public Direction getToTurn() {
		return toTurn;
	}

	public void setToTurn(Direction toTurn) {
		this.toTurn = toTurn;
		if (this.order == Order.NONE) {
			this.order = Order.TURN;
		}
	}
}
