package ui.appwindow;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import clientServer.Slave;

public class MainFrame extends JFrame implements ClientUI {
	private JMenuBar menuBar;
	private JPanel infoBar;
	private JPanel graphics;
	private JPanel bottomPanel;
<<<<<<< HEAD

	private Slave slave;

=======
>>>>>>> refs/remotes/origin/master

	public MainFrame(){
		super("Team 39");

		setJMenuBar(menuBar);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(true);
	}


	@Override
	public void addChat(String text) {}

<<<<<<< HEAD

=======
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD

	}

	private void close() {
		if (this.slave != null) {
			this.slave.close();
		}
        System.exit(0);
=======
>>>>>>> refs/remotes/origin/master
	}

	public static void main(String[] args){
		new MainFrame();
	}

}
