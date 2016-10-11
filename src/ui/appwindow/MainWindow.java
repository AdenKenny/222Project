package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Graphics.AudioHandler;
import Graphics.GraphicsPanel;
import clientServer.ClientSideGame;
import clientServer.PackageCode;
import clientServer.Slave;
import gameWorld.Entity;
import gameWorld.characters.Character;
import gameWorld.item.Item;
import gameWorld.rooms.Room;

public class MainWindow extends JFrame implements ClientUI, KeyListener {

	private static final float MOVE_SPEED = 2f;
	private long moveTimer;

	private Slave slave;
	private ClientSideGame game;
	private InfoPane infoBar;
	private JPanel display; // Login to begin with, then display
	private BottomPanel bottomPanel;
	private OptionsPane optionsPane;
	private boolean enterGame;
	private Compass compass;
	private AudioHandler audioHandler;

	public MainWindow() {
		super("RoomScape");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Overridden
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
		// set size for initial restore down
		int width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize(width - 100, height - 100);
		setPreferredSize(new Dimension(width, height));
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		setFocusable(true);
	}

	private void initComponents() {
		// Add next level of components
		this.compass = new Compass(this);
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

		getLayeredPane().add(optionsPane, new Integer(300)); // Pop-up layer
		getLayeredPane().add(compass, new Integer(200));
		this.audioHandler = new AudioHandler();
	
		audioHandler.playMusic("login");
	}

	protected void setDisplay(JPanel display) {
		this.display = display;
	}

	@Override
	public void addChat(String text) {
		bottomPanel.addChat(text);
	}

	@Override
	public void sendChat(String chatInput) {
		// send input to server for broadcast
		slave.sendTextMessage(chatInput);
	}

	@Override
	public void addGameChat(String output) {
		while (bottomPanel == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	/**
	 * Updates the stats pane to reflect any changes to the specified
	 * Character's statistics.
	 *
	 * @param player
	 *            The Character whose stats are being displayed
	 */
	public void updateStats(Character player) {
		this.bottomPanel.updateStats(player);
		this.updateGold(player.getGold());
	}

	@Override
	public void setRoom(int floor, Room room) {
		infoBar.setRoom(floor, room);
	}

	@Override
	public void updateGold(int amount) {
		infoBar.updateGold(amount);
	}

	@Override
	public void displayEntityOptions(Entity entity, int x, int y) {
		optionsPane.displayAndDrawEntityList(x, y, entity);
	}

	@Override
	public void displayItemOptions(Item item, int x, int y) {
		optionsPane.displayAndDrawItemList(x, y, item);
	}

	@Override
	public void performActionOnEntity(Entity entity, String actionName) {
		this.slave.performActionOnEntity(entity, actionName);
	}

	@Override
	public void performActionOnItem(Item clickedItem, String name) {
		// this.slave.performActionOnItem(clickedItem, name); //TODO: implement
	}

	/*
	 * Methods for implementing key listener for game movement.
	 */

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (System.currentTimeMillis() < this.moveTimer + MOVE_SPEED) {
			/*
			 * for(long s = 0; s < 1000000000; s++) {
			 *
			 * }
			 */
		}

		int code = e.getKeyCode();
		boolean moved = false;

		if (code == KeyEvent.VK_W) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_W.value());
			moved = true;
		} else if (code == KeyEvent.VK_A) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_A.value());
			moved = true;
		} else if (code == KeyEvent.VK_S) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_S.value());
			moved = true;
		} else if (code == KeyEvent.VK_D) {
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_D.value());
			moved = true;
		} else if (code == KeyEvent.VK_Q) {
			compass.rotateLeft();
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_Q.value());
			moved = true;
		} else if (code == KeyEvent.VK_E) {
			compass.rotateRight();
			this.slave.sendKeyPress(PackageCode.Codes.KEY_PRESS_E.value());
			moved = true;
		}

		if (moved) {
			this.moveTimer = System.currentTimeMillis();
		}
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
		} else if (result == PackageCode.Codes.LOGIN_INCORRECT_USER.value()) {
			text = "Incorrect username.";
		} else if (result == PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value()) {
			text = "Incorrect password.";
		} else if (result == PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value()) {
			text = "That character is already online.";
		} else if (result == PackageCode.Codes.NEW_USER_SUCCESS.value()) {
			this.enterGame = true;
			text = "Account created.";
		} else if (result == PackageCode.Codes.NEW_USER_NAME_TAKEN.value()) {
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
			} catch (InterruptedException e) {
			}
		}

		if (this.display != null) {
			this.display.setVisible(false);
			this.remove(display);
		}
		// load player stats
		Character player = null;
		while (player == null) {
			player = this.game.getPlayer();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		
		player.addListener(audioHandler);
		
		bottomPanel.loadPlayerStats(player);
		updateGold(this.game.getPlayer().getGold());
		setRoom(this.game.getFloor(), this.game.getRoom());
		bottomPanel.loadInventory(player);
		compass.setVisible(true);
		// Load graphics panel
		this.display = new GraphicsPanel(this.game.getPlayer());
		GraphicsPanel gfx = (GraphicsPanel) display;
		gfx.setGraphicsClickListener(new GuiGraphicsClickListener(this));
		add(gfx, BorderLayout.CENTER);
		gfx.setVisible(true);
		gfx.revalidate();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				gfx.repaint();
			}
		}, 0, 33, TimeUnit.MILLISECONDS);
		//Start playing music to make the player feel that they are alone in a dark place in a different world, and they may never return.
		audioHandler.playMusic("main");

	}
	/**
	 * Sets the slave for connection to the server.
	 * Slave must be initialized using this this instance of MainWindow.
	 *
	 * @param slave
	 */
	public void setSlave(Slave slave) {
		this.slave = slave;
	}

	/**
	 * Get the graphics panel of this Window if it has been set.
	 * @return graphics panel
	 */
	public GraphicsPanel getGraphicsPanel() {
		if (this.display instanceof GraphicsPanel) {
			return (GraphicsPanel) this.display;
		}
		return null;
	}

	public void waitForGame() {
		while (!this.enterGame) {
			// wait for user to login
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		enterGame();
	}

	public static void main(String[] args) {
		MainWindow main = new MainWindow();
		Slave slave = new Slave(main);
		slave.start();
		main.setSlave(slave);
		main.initComponents();
		main.waitForGame();
	}

	@Override
	public void playMusic(String inMusicName) {
		// TODO Auto-generated method stub
		
	}

}
