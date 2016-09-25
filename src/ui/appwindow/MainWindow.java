package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainWindow extends JFrame implements ClientUI {
	private JPanel infoBar;
	private JPanel display; //Login to begin with, then display
	private JPanel bottomPanel;
	
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
		
		//Add next level of components
		infoBar = new InfoPane();
		display = new Login();
		bottomPanel = new BottomPanel();
		add(infoBar, BorderLayout.PAGE_START);
		add(display, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END); 
		
		setVisible(true);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setResizable(true);
		
	}

	protected void setDisplay(JPanel display){
		
	}

	public void addChat(String text) {}
	
	public void sendChat(String input) {
		// TODO Auto-generated method stub
		
	}

	public void addGameChat(String output) {
		// TODO Auto-generated method stub
		
	}

	public void addToInventory(int itemId) {
		// TODO Auto-generated method stub
		
	}

	public void setStat(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	public void setFloor(int number) {
		// TODO Auto-generated method stub
		
	}

	public void displayItemOptions(String[] options) {
		// TODO Auto-generated method stub
		
	}

	public void performActionOnItem(int itemId, int actionId) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args){
		new MainWindow();
	}

}
