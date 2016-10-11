package ui.appwindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Displays the direction the current player is facing.
 * Used by MainWindow by adding to LayeredPane.
 *
 * @author normanclin
 *
 */
public class Compass extends JPanel {
	private Image compass;
	private String direction = "N";
	private MainWindow parent;

	public Compass(MainWindow parent) {
		this.parent = parent;
		this.setVisible(false);
		try {
			compass = ImageIO.read(new File("resources/ui/Compass.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		setBounds(new Rectangle(parent.getWidth()-55, (int) (parent.getHeight()*0.7), 50, 50));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(compass, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.black);
		g.setFont(new Font(direction, Font.BOLD, 15));
		if(direction.equals("W")){
			g.drawString(direction, getWidth()/2-8, getHeight()/2-5);
		}
		else{
			g.drawString(direction, getWidth()/2-5, getHeight()/2-5);
		}
	}

	protected void rotateRight(){
		if(direction.equals("N")) direction = "E";
		else if(direction.equals("E")) direction = "S";
		else if(direction.equals("S")) direction = "W";
		else if(direction.equals("W")) direction = "N";
		repaint();
	}

	protected void rotateLeft(){
		if(direction.equals("N")) direction = "W";
		else if(direction.equals("E")) direction = "N";
		else if(direction.equals("S")) direction = "E";
		else if(direction.equals("W")) direction = "S";
		repaint();
	}

}
