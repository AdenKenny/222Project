package IDGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import clientServer.Slave;
import IDGUI.MenuBar;

public class Frame extends JFrame {

	private Slave slave;

	public Frame(Slave slave) {
		super("RoomScape");
		this.slave = slave;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(1000, 680));
		setLayout(new BorderLayout());
		pack();
		setResizable(false);

		this.setJMenuBar(new MenuBar(this).getBar()); //Create menu bar.

		setVisible(true);
	}

	public void login() {
		String[] details = getDetails();
		this.slave.login(details[0], details[1]);
	}

	public void newUser() {

	}

	private String[] getDetails() {
		String[] details = new String[2];
		return details;
	}

	public static void main(String[] args) {
		Slave slave = new Slave();
		new Frame(slave);
	}
}
