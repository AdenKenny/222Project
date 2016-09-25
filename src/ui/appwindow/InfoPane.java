package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Container for display Game information along top of window.
 * 
 * @author Clinton
 * 
 */
public class InfoPane extends JPanel{
	
	public InfoPane() {
	
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(),getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), 40);
	}
}
