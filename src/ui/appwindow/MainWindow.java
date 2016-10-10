package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Graphics.GraphicsPanel;
import IDGUI.MessageDialog;
import clientServer.ClientSideGame;
import clientServer.PackageCode;
import clientServer.Slave;
import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.item.Item;

public class MainWindow extends JFrame implements ClientUI, KeyListener {
	private Slave slave;
	private ClientSideGame game;
	private InfoPane infoBar;
	private JPanel display; //Login to begin with, then display
	private BottomPanel bottomPanel;
	private OptionsPane optionsPane;
	private boolean enterGame;

	public MainWindow(){
		super("RoomScape");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Overridden
		JFrame frame = this;
		addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                slave.close();
                frame.setVisible(false);
                frame.dispose();
                System.exit(0);
            }

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
        });
		setLayout(new BorderLayout());
		//set size for initial restore down
		int width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize( width-100, height-100);
		setPreferredSize(new Dimension(width, height));
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		setFocusable(true);
	}

	private void initComponents(){
		//Add next level of components
		infoBar = new InfoPane();
		display = new Login(this, slave);
		bottomPanel = new BottomPanel(this);
		add(infoBar, BorderLayout.PAGE_START);
		add(display, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		infoBar.initComponents();

		this.addKeyListener(this);
		
		Login login = (Login) display;
		login.initComponents();

		bottomPanel.initComponents();

		setStat(StatsPane.HEALTH, 50);
		setStat(StatsPane.EXP, 20);
		setStat(StatsPane.LEVEL, 99);
		revalidate();
		setVisible(true);
		this.optionsPane = new OptionsPane(this);
		getLayeredPane().add(optionsPane, new Integer(300)); //Pop-up layer

	}

	protected void setDisplay(JPanel display){
		this.display = display;
	}

	@Override
	public void addChat(String text) {
		bottomPanel.addChat(text);
	}

	@Override
	public void sendChat(String chatInput) {
		//send input to server for broadcast
		slave.sendTextMessage(chatInput);
	}

	@Override
	public void addGameChat(String output) {
		bottomPanel.addGameChat(output);

	}

	@Override
	public void addToInventory(Item item) {
		bottomPanel.addToInventory(item);
	}

	@Override
	public void setStat(int id, int value) {
		bottomPanel.setStat(id, value);
	}

	@Override
	public void setRoom(int number) {
		infoBar.setRoom(number);
	}

	@Override
	public void updateGold(int amount) {
		infoBar.updateGold(amount);
	}

	@Override
	public void displayItemOptions(Entity entity, int x, int y) {
		optionsPane.displayAndDrawList(x, y, entity);
	}

	@Override
	public void performActionOnEntity(int itemId, int actionId) {
		// TODO Auto-generated method stub
	}
	

	/*
	 * Methods for implementing key listener for game movement.
	 */

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("A key was pressed!");
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
		Character player = this.slave.getGame().getPlayer();
		System.out.println(String.format("Facing: %s, x: %d, y: %d", player.facing(), player.xPos(), player.yPos()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void reconnect() {
		if (this.slave != null && this.slave.connected()) {
			return;
		}
		this.slave = new Slave(this);
		this.slave.start();
	}

	public void accountResult(byte result) {
		String text = "";
		if (result == PackageCode.Codes.LOGIN_SUCCESS.value()) {
			this.enterGame = true;
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
			this.enterGame = true;
			text = "Account created.";
		}
		else if (result == PackageCode.Codes.NEW_USER_NAME_TAKEN.value()) {
			text = "That name is unavailable.";
		}
		addGameChat(text);
	}

	/*
	 * Once a user has successfully logged in, load them into game.
	 */
	
	private void enterGame() {
		while (this.game == null) {
			this.game = this.slave.getGame();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		
		if (this.display != null) {
			this.display.setVisible(false);
			this.remove(display);		
		}
		//load player stats 
		Character player = null;
		while (player == null) {
			player = this.game.getPlayer();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		
		//player has loaded
		bottomPanel.loadPlayerStats(player);
		updateGold(player.getGold());
		setRoom(this.game.getRoom().depth());
		bottomPanel.loadInventory(player);
		//Load graphics panel
		this.display = new GraphicsPanel(this.game.getPlayer());
		GraphicsPanel gfx = (GraphicsPanel) display;
		gfx.setGraphicsClickListener(new GuiGraphicsClickListener(this));
		add(gfx,BorderLayout.CENTER);
		gfx.setVisible(true);
		gfx.revalidate();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable(){
			public void run(){
				gfx.repaint();
			}
		}, 0, 33, TimeUnit.MILLISECONDS);
		
	}

	public void setSlave(Slave slave) {
		this.slave = slave;
	}
	
	public void waitForGame() {
		while (!this.enterGame) {
			//wait for user to login
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		enterGame();
	}

	public static void main(String[] args){
		MainWindow main = new MainWindow();
		Slave slave = new Slave(main);
		slave.start();
		main.setSlave(slave);
		main.initComponents();
		main.waitForGame();
	}
}
