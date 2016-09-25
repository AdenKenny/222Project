package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Container for holding bottom Items(chat/info/inventory).
 * 
 * @author Clinton
 * 
 */
public class BottomPanel extends JPanel{
	private JPanel chatPane;
	private JPanel infoPane;
	private JPanel inventoryPane;
	
	public BottomPanel() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0,0,getWidth(),getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), (int) (getParent().getHeight()*0.2));
	}
}
