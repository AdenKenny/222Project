package ui.appwindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import gameWorld.rooms.Room;

/**
 * Container for display Game information along top of window.
 *
 * @author Clinton
 *
 */
public class InfoPane extends JPanel {
	public static final int HEIGHT = 40;

	private JButton logoutButton;
	private JLabel floorLabel;
	private JLabel goldLabel;

	private SpringLayout layout;

	public InfoPane() {
		layout = new SpringLayout();
		setLayout(layout);
		setVisible(true);
	}

	public void initComponents() {
		logoutButton = new JButton("Logout");
		logoutButton.setOpaque(true);
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setBackground(Color.DARK_GRAY);
		logoutButton.setFocusPainted(false);

		floorLabel = new JLabel("");
		floorLabel.setOpaque(true);
		floorLabel.setForeground(Color.WHITE);
		floorLabel.setBackground(Color.DARK_GRAY);
		floorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		goldLabel = new JLabel("");
		goldLabel.setOpaque(true);
		goldLabel.setForeground(new Color(245, 225, 7));
		goldLabel.setBackground(Color.DARK_GRAY);

		add(logoutButton);
		add(floorLabel);
		add(goldLabel);

		logoutButton.setVisible(false);
		floorLabel.setVisible(true);
		goldLabel.setVisible(true);
		revalidate();
		repaint();
	}

	/*
	 * re-do calculations for relative positions of the components
	 */
	private void calculateLayoutConstraints() {
		layout.putConstraint(SpringLayout.WEST, logoutButton, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, logoutButton, 5, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, floorLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, floorLabel, 10, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.EAST, goldLabel, -30, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, goldLabel, 10, SpringLayout.NORTH, this);

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (logoutButton != null && floorLabel != null && goldLabel != null) {
			calculateLayoutConstraints();
			logoutButton.repaint();
			floorLabel.repaint();
			goldLabel.repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), HEIGHT);
	}

	public void setRoom(int floor, Room room) {
		floorLabel.setText("Floor " + floor + ", Room (" + room.xPos() + ", " + room.yPos() + ")");
	}

	public void updateGold(int amount) {
		goldLabel.setText("Gold: " + amount);
		this.repaint();
	}

	public void showLogout() {
		logoutButton.setVisible(true);
	}

	public void hideLogout() {
		logoutButton.setVisible(false);
	}
}
