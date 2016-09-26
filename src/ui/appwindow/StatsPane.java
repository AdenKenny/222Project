package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Displays players information
 * 
 */
public class StatsPane extends JPanel{
	public static float WIDTH_RATIO = 0.3f;
	public StatsPane(){

	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
