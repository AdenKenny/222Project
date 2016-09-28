package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Graphics.GraphicsPanel;
import gameWorld.Entity;

public class MainWindow extends JFrame implements ClientUI, KeyListener {
	private InfoPane infoBar;
	private JPanel display; //Login to begin with, then display
	private BottomPanel bottomPanel;

	public MainWindow(){
		super("RoomScape");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Overridden
		JFrame frame = this;
		addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(frame, "Quit game?") == JOptionPane.YES_OPTION){
                    //TODO: Send a disconnect from server request here

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
		setSize((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()-100,
				(int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);



		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setResizable(true);


	}

	public void initComponents(){
		//Add next level of components
		infoBar = new InfoPane();
		infoBar.initComponents();
		display = new Login();
		bottomPanel = new BottomPanel(this);
		bottomPanel.initComponents();

		add(infoBar, BorderLayout.PAGE_START);
		add(display, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		addGameChat("Testing game chat");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addChat("Text from another player");
		addGameChat("Gone?");
		revalidate();
		setVisible(true);
		repaint();
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

	public void addToInventory(int itemId) {
		// TODO Auto-generated method stub

	}

	public void setStat(int id, int value) {
		// TODO Auto-generated method stub

	}

	public void setFloor(int number) {
		infoBar.setFloor(number);
	}

	public void updateGold(int amount) {
		infoBar.updateGold(amount);
	}

	public void displayItemOptions(String[] options) {
		// TODO Auto-generated method stub

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

	public static void main(String[] args){
		MainWindow main = new MainWindow();
		main.initComponents();
		main.setDisplay(new GraphicsPanel());
	}

}
