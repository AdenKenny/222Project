package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Container for display Game information along top of window.
 * 
 * @author Clinton
 * 
 */
public class InfoPane extends JPanel{
	public static int HEIGHT = 40;
	
	public JButton logoutButton;
	public JLabel floorLabel;
	public JLabel goldLabel;
	
	public InfoPane() {
		setLayout(new BorderLayout());
		
		logoutButton = new JButton("Logout");
		logoutButton.setOpaque(true);
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setBackground(Color.DARK_GRAY);
		logoutButton.setFocusPainted(false);
		
		floorLabel = new JLabel("Floor: ");
		floorLabel.setOpaque(true);
		floorLabel.setForeground(Color.WHITE);
		floorLabel.setBackground(Color.DARK_GRAY);
		
		goldLabel = new JLabel("Gold:       ");
		goldLabel.setOpaque(true);
		goldLabel.setForeground(Color.WHITE);
		goldLabel.setBackground(Color.DARK_GRAY);
		
		add(logoutButton, BorderLayout.LINE_START);
		add(floorLabel);
		add(goldLabel, BorderLayout.LINE_END);
		logoutButton.setVisible(true);
		floorLabel.setVisible(true);
		goldLabel.setVisible(true);

	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(),getHeight());
		logoutButton.repaint();
		floorLabel.repaint();
		goldLabel.repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(),HEIGHT);
	}
	
	public void showLogout(){
		logoutButton.setVisible(true);
	}
	
	public void hideLogout(){
		logoutButton.setVisible(false);
	}
}
