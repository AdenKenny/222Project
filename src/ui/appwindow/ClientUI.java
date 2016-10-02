package ui.appwindow;

import java.util.List;

import gameWorld.Action;
import gameWorld.item.Item;

public interface ClientUI {

	public void addChat(String text);

	public void sendChat(String input);

	public void addGameChat(String output);

	public void addToInventory(Item item);

	public void setStat(int id, int value);

	public void setFloor(int number);

	public void updateGold(int amount);

	public void displayItemOptions(List<Action> options, int x, int y);

	public void performActionOnItem(int itemId, int actionId);

}
