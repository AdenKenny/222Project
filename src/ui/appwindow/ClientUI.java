package ui.appwindow;

import java.util.List;

import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.item.Item;
import gameWorld.rooms.Room;

public interface ClientUI {

	public void addChat(String text);

	public void sendChat(String input);

	public void addGameChat(String output);

	public void addToInventory(Item item);

	public void setStat(int id, int value);

	public void setRoom(int floor, Room room);

	public void updateGold(int amount);

	public void displayEntityOptions(Entity entity, int x, int y);

	public void performActionOnEntity(Entity entity, String actionName);

	void displayItemOptions(Item item, int x, int y);

}
