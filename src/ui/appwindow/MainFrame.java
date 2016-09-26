package ui.appwindow;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ClientUI {
	private JMenuBar menuBar;
	private JPanel infoBar;
	private JPanel graphics;
	private JPanel bottomPanel;

	public MainFrame(){
		super("Team 39");
		setJMenuBar(menuBar);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(true);
	}


	@Override
	public void addChat(String text) {}

	@Override
	public void sendChat(String input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGameChat(String output) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addToInventory(int itemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStat(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFloor(int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayItemOptions(String[] options) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performActionOnItem(int itemId, int actionId) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args){
		new MainFrame();
	}

}
