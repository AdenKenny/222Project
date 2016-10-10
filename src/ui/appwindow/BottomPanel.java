package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import gameWorld.characters.Character;
import javax.swing.JPanel;

import clientServer.Game;
import gameWorld.item.Item;

/**
 * Container for holding bottom Items(chat/info/inventory).
 * 
 * @author Clinton
 * 
 */
public class BottomPanel extends JPanel{
	public static float HEIGHT_RATIO = 0.2f; //height as proportion of window.
	private MainWindow parent;
	private ChatPane chatPane;
	private StatsPane statPane;
	private InventoryPane inventoryPane;
	
	public BottomPanel(MainWindow parent) {
		this.parent = parent;
		setLayout(new BorderLayout());
		setVisible(true);
	}
	
	public void initComponents(){
		this.chatPane = new ChatPane(this);
		chatPane.initComponents();
		this.statPane = new StatsPane();

		this.inventoryPane = new InventoryPane(parent);
		
		add(chatPane, BorderLayout.WEST);
		add(statPane, BorderLayout.CENTER);
		add(inventoryPane, BorderLayout.EAST);
		revalidate();
	}
	
	public void loadPlayerStats(Character currentPlayer){
		
		setStat(StatsPane.HEALTH, currentPlayer.getHealth());
		setStat(StatsPane.MAXHEALTH, currentPlayer.getMaxHealth());
		setStat(StatsPane.EXP, currentPlayer.getXp());
		setStat(StatsPane.HEALTH, currentPlayer.getXpForLevel());
		setStat(StatsPane.LEVEL, currentPlayer.getLevel());
		setStat(StatsPane.DAMAGE, currentPlayer.getDamage());
		statPane.initComponents();
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

	public void addChat(String text) {
		chatPane.addChat(text);
		
	}

	public void sendChat(String chatInput) {
		parent.sendChat(chatInput);
		
	}

	protected void setStat(int id, int value) {
		statPane.setStat(id, value);
		
	}

	public void addToInventory(Item item) {
		inventoryPane.addItem(item);
		
	}
	
	public void loadInventory(Character player) {
		List<Integer> itemIDs = player.getItems();
		for(Integer id : itemIDs){
			inventoryPane.addItem(Game.mapOfItems.get(id));
		}
	}
}
