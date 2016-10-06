package IDGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import clientServer.ClientSideGame;
import clientServer.PackageCode;
import clientServer.Slave;
import integrationGraphics.FrameRate;
import integrationGraphics.GraphicsPanel;
import IDGUI.MenuBar;

public class Frame extends JFrame implements KeyListener {

	private Slave slave;
	private ClientSideGame game;

	public Frame() {
		super("RoomScape");
		reconnect();
		addKeyListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(1000, 800));
		setLayout(new BorderLayout());
		pack();
		setResizable(false);

		this.setJMenuBar(new MenuBar(this).getBar()); //Create menu bar.

		setVisible(true);
	}

	public void accountResult(byte result) {
		String text = "";
		if (result == PackageCode.Codes.LOGIN_SUCCESS.value()) {
			enterGame();
			text = "Login successful.";
		}
		else if (result == PackageCode.Codes.LOGIN_INCORRECT_USER.value()) {
			text = "Incorrect username.";
		}
		else if (result == PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value()) {
			text = "Incorrect password.";
		}
		else if (result == PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value()) {
			text = "That character is already online.";
		}
		else if (result == PackageCode.Codes.NEW_USER_SUCCESS.value()) {
			enterGame();
			text = "Account created.";
		}
		else if (result == PackageCode.Codes.NEW_USER_NAME_TAKEN.value()) {
			text = "That name is unavailable.";
		}
		threadedMessage(text);
	}

	public void login() {
		if (this.slave.inGame()) {
			threadedMessage("You are already logged in.");
			return;
		}
		String[] details = getDetails();
		if (details == null) {
			return;
		}
		this.slave.login(details[0], details[1]);
	}

	public void newUser() {
		if (this.slave.inGame()) {
			threadedMessage("You are already logged in.");
			return;
		}
		String[] details = getDetails();
		if (details == null) {
			return;
		}
		this.slave.newUser(details[0], details[1]);
	}

	public void reconnect() {
		if (this.slave != null && this.slave.connected()) {
			threadedMessage("You are already logged in.");
			return;
		}
		//this.slave = new Slave(this);
		//this.slave.start();
	}

	public void mockLogin() {
		if (this.slave.inGame()) {
			threadedMessage("You are already logged in.");
			return;
		}
		this.slave.login("Simon", "hunter2");
	}

	private String[] getDetails() {
		String username = JOptionPane.showInputDialog("Username");
		if (username == null) {
			return null;
		}
		String password = JOptionPane.showInputDialog("Password");
		if (password == null) {
			return null;
		}
		String[] details = {username, password};
		return details;
	}

	private void enterGame() {
		this.game = this.slave.getGame();
		GraphicsPanel gfx = new GraphicsPanel(this.slave.getGame(), this.slave.getUsername());
		this.add(gfx);
		new FrameRate(gfx).start();
		this.pack();
		this.repaint();
	}

	public void threadedMessage(String text) {
		new Thread() {
			@Override
			public void run() {
				new MessageDialog(Frame.this, text);
			}
		}.start();
	}

	public static void main(String[] args) {
		new Frame();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Not used.
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_W.value());
		}

		else if (code == KeyEvent.VK_A) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_A.value());
		}

		else if (code == KeyEvent.VK_S) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_S.value());
		}

		else if (code == KeyEvent.VK_D) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_D.value());
		}

		else if (code == KeyEvent.VK_Q) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_Q.value());
		}

		else if (code == KeyEvent.VK_E) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_E.value());
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Not used.
	}
}
