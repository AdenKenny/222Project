package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import clientServer.Slave;
import userHandling.Verification;

/**
 * Container for displaying Login screen.
 *
 * @author Clinton
 */

public class Login extends JPanel{
	private JLabel userLabel;
	private JLabel passLabel;
	private JPasswordField passField;
	private JTextArea userField;
	private JButton loginButton;
	private JButton registerButton;
	private static Image background;

	private SpringLayout layout;

	private MainWindow client;
	private Slave slave;

	public Login(MainWindow client, Slave slave) {
		this.client = client;
		this.slave = slave;
		try {
			background = ImageIO.read(new File("resources/ui/LoginImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		layout = new SpringLayout();
		setLayout(layout);
		setVisible(true);
		setBackground(null);
		setOpaque(false);
	}

	public void initComponents(){
		userLabel = new JLabel("Username:");
		passLabel = new JLabel("Password:");
		passField = new JPasswordField();
		passField.setBounds(getWidth()/2-50, getHeight()/2+30, 50, 10);
		userField = new JTextArea();
		loginButton = new JButton("Login");
		registerButton = new JButton("Register");
		setLoginAction();
		setRegisterAction();
		userLabel.setForeground(Color.GRAY);
		passLabel.setForeground(Color.GRAY);
		add(userLabel);
		add(passField);
		add(passLabel);
		add(userField);
		add(loginButton);
		add(registerButton);
		revalidate();
		repaint();
	}

	private void setRegisterAction() {
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Run registration for user
				slave.newUser(userField.getText(), new String(passField.getPassword()));
				passField.setText("");
			}
		});

	}

	private void setLoginAction() {
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Attempt Login
				slave.login(userField.getText(), new String(passField.getPassword()));
				passField.setText("");
			}
		});

	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

		//username
		userField.setSize(new Dimension(200, 20));
		layout.putConstraint(SpringLayout.WEST, userLabel, getWidth()/2-150, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, userLabel, 250, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, userField, 20, SpringLayout.EAST, userLabel);
		layout.putConstraint(SpringLayout.NORTH, userField, 250, SpringLayout.NORTH, this);

		//password
		passField.setSize(new Dimension(200, 20));
		layout.putConstraint(SpringLayout.WEST, passLabel, getWidth()/2-150, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, passLabel, 290, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, passField, 20, SpringLayout.EAST, passLabel);
		layout.putConstraint(SpringLayout.NORTH, passField, 290, SpringLayout.NORTH, this);

		//buttons
		loginButton.setSize(90, 30);
		registerButton.setSize(90, 30);
		layout.putConstraint(SpringLayout.NORTH, loginButton, 20, SpringLayout.SOUTH, passField);
		layout.putConstraint(SpringLayout.WEST, loginButton, 0, SpringLayout.WEST, passField);
		layout.putConstraint(SpringLayout.NORTH, registerButton, 20, SpringLayout.SOUTH, passField);
		layout.putConstraint(SpringLayout.WEST, registerButton, 50, SpringLayout.EAST, loginButton);
		super.paint(g);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), (int) (getParent().getHeight()*0.5));
	}
}
