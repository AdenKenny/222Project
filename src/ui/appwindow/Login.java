package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Container for displaying Login screen.
 * 
 * @author Clinton
 */

public class Login extends JPanel{
	public Login() {
		setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0,0,getWidth(), getHeight());
		System.out.println("Display: " + getWidth() + "x" + getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), (int) (getParent().getHeight()*0.5));
	}
}
