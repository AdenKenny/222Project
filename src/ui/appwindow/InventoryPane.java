package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class InventoryPane extends JPanel{
	public static float WIDTH_RATIO = 0.34f;
	public InventoryPane(){
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
