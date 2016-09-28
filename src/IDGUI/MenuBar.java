package IDGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * A class representing the JMenuBar that is displayed at the top of the program.
 *
 * @author Aden Kenny and Simon Pope.
 */

public class MenuBar extends JFrame {

	Frame frame; //The base frame on which the game is displayed on.
	JMenuBar menuBar; // The menu bar on which everything will be displayed on.

	/**
	 * Constructor for our menu bar. Takes a frame input on which it will be
	 * displayed.
	 *
	 * @param frame The frame on which this menu bar will be added to.
	 */

	public MenuBar(Frame frame) {

		this.frame = frame;
		this.menuBar = new JMenuBar(); // Initialise bar.

		JMenu gameMenu = new JMenu("Game");

		JMenuItem login = new JMenuItem("Login"); //The buttons on the menu.
		login.setActionCommand("Login");

		JMenuItem createAccount = new JMenuItem("Create Account");
		createAccount.setActionCommand("Create Account");

		MenuItemListener menuItemListener = new MenuItemListener();

		login.addActionListener(menuItemListener); //Adds the custom listener to the buttons.
		createAccount.addActionListener(menuItemListener);

		gameMenu.add(login); //Adds the menu items to the menus.
		gameMenu.add(createAccount);

		this.menuBar.add(gameMenu); //Add the menu to the menu bar.

	}

	/**
	 * Returns the JMenuBar to be added to the frame.
	 *
	 * @return The completed
	 */

	public JMenuBar getBar() {
		return this.menuBar;
	}

	/**
	 * Inner class representing a custom action listener.
	 *
	 * @author Aden Kenny and Simon Pope.
	 *
	 */

	class MenuItemListener implements ActionListener {

		/**
		 * Takes the button and calls the function that equates to the menu buttons.
		 */

		@Override
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();

			if(action.equals("Login")) {
				MenuBar.this.frame.login();
			}

			else if(action.equals("Create Account")) {
				MenuBar.this.frame.newUser();
			}
		}
	}
}
