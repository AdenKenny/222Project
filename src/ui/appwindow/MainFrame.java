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

	private Slave slave;


	public MainFrame(){
		super("Team 39");

		this.slave = null;
		Slave slave = new Slave();
		if (slave.connected()) {
			slave.start();
			this.slave = slave;
		}

		setJMenuBar(menuBar);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(true);

		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        close();
		    }
		});
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

	private void close() {
		if (this.slave != null) {
			this.slave.close();
		}
        System.exit(0);
	}

	public static void main(String[] args){
		new MainFrame();
	}

}
