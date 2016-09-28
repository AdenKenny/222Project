package ui.appwindow;

public interface ClientUI {

	public void addChat(String text);

	public void sendChat(String input);

	public void addGameChat(String output);

	public void addToInventory(int itemId);

	public void setStat(int id, int value);

	public void setFloor(int number);

	public void updateGold(int amount);

	public void displayItemOptions(String[] options);

	public void performActionOnItem(int itemId, int actionId);

}
