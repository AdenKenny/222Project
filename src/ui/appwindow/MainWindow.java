package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Graphics.GraphicsPanel;
import clientServer.ClientSideGame;
import clientServer.PackageCode;
import clientServer.Slave;
import gameWorld.Action;
import gameWorld.Entity;
import gameWorld.item.Item;

public class MainWindow extends JFrame implements ClientUI, KeyListener {
	private Slave slave;
	private ClientSideGame game;
	private InfoPane infoBar;
	private JPanel display; //Login to begin with, then display
	private BottomPanel bottomPanel;
	private OptionsGlassPane glassPane;

	public MainWindow(){
		super("RoomScape");
		this.glassPane = new OptionsGlassPane(this);
		//setGlassPane(glassPane);
		glassPane.setVisible(true);
		//reconnect();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Overridden
		JFrame frame = this;
		addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(frame, "Quit game?") == JOptionPane.YES_OPTION){
                	try{
                		slave.close();
                	}
                	catch(Exception ex){
                		
                	}
                    frame.setVisible(false);
                    frame.dispose();
                }
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
	}

	public void initComponents(){
		//Add next level of components
		infoBar = new InfoPane();
		display = new Login(this, slave);
		bottomPanel = new BottomPanel(this);
		add(infoBar, BorderLayout.PAGE_START);
		add(display, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		infoBar.initComponents();

		Login login = (Login) display;
		login.initComponents();

		bottomPanel.initComponents();

		addGameChat("Testing game chat");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addGameChat("Chat from the game");
		setStat(StatsPane.HEALTH, 50);
		setStat(StatsPane.EXP, 20);
		setStat(StatsPane.LEVEL, 99);
		revalidate();
		setVisible(true);
		displayItemOptions(null, 200, 200);
	}

	protected void setDisplay(JPanel display){
		this.display = display;
	}

	public void addChat(String text) {
		bottomPanel.addChat(text);
	}

	public void sendChat(String chatInput) {
		//send input to server for broadcast
		addChat(chatInput); //TODO: Remove so user sending message gets back from broadcast
	}

	public void addGameChat(String output) {
		bottomPanel.addGameChat(output);

	}

	public void addToInventory(Item item) {
		bottomPanel.addToInventory(item);
			
		

	}

	public void setStat(int id, int value) {
		bottomPanel.setStat(id, value);

	}

	public void setFloor(int number) {
		infoBar.setFloor(number);
	}

	public void updateGold(int amount) {
		infoBar.updateGold(amount);
	}

	public void displayItemOptions(List<Action> options, int x, int y) {
		glassPane.displayAndDrawList(x, y, options);
	}

	public void performActionOnItem(int itemId, int actionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public Entity getEntity(int x, int y){

		return null;
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

	public void threadedMessage(String string) {
		// TODO Auto-generated method stub
		
	}
	
	private void enterGame() {
		//TODO: setup graphics
		display.setVisible(false);
		this.display = new GraphicsPanel(null, null);
		GraphicsPanel gfx = (GraphicsPanel) display;
		gfx.setGraphicsClickListener(new GuiGraphicsClickListener(this));
		this.revalidate();
		this.repaint();
	}
	public static void main(String[] args){
		MainWindow main = new MainWindow();
		main.initComponents();
		Slave slave = new Slave(main);
		main.game = slave.getGame();
		main.slave = slave;
	}
}
