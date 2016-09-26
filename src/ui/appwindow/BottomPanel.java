package ui.appwindow;

import java.awt.BorderLayout;
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
	public static float HEIGHT_RATIO = 0.2f; //height as proportion of window.
	
	private ChatPane chatPane;
	private StatsPane statPane;
	private InventoryPane inventoryPane;
	
	public BottomPanel() {
		setLayout(new BorderLayout());
		setVisible(true);
	}
	
	public void initComponents(){
		this.chatPane = new ChatPane();
		chatPane.initComponents();
		this.statPane = new StatsPane();
		this.inventoryPane = new InventoryPane();
		add(chatPane, BorderLayout.WEST);
		add(statPane, BorderLayout.CENTER);
		add(inventoryPane, BorderLayout.EAST);
//		
//		chatPane.setVisible(true);
//		statPane.setVisible(true);
//		inventoryPane.setVisible(true);
		
		revalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		chatPane.repaint();
		statPane.repaint();
		inventoryPane.repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getWidth(), (int) (getParent().getHeight()*HEIGHT_RATIO));
	}
	
	public void addGameChat(String output){
		chatPane.addGameChat(output);
	}
}
