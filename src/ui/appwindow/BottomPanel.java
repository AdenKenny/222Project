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

public class BottomPanel extends JPanel {
	public static final float HEIGHT_RATIO = 0.2f; // height as proportion of window.
	protected MainWindow parent;

	private ChatPane chatPane;
	private StatsPane statPane;
	private InventoryPane inventoryPane;

	public BottomPanel(MainWindow parent) {
		this.parent = parent;
		setLayout(new BorderLayout());
		setVisible(true);
	}


	protected void initComponents() {
		this.chatPane = new ChatPane(this);
		chatPane.initComponents();
		this.statPane = new StatsPane();

		this.inventoryPane = new InventoryPane(parent);

		add(chatPane, BorderLayout.WEST);
		add(statPane, BorderLayout.CENTER);
		add(inventoryPane, BorderLayout.EAST);
		revalidate();
	}

	protected void loadPlayerStats(Character currentPlayer){
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
		return new Dimension(getParent().getWidth(), (int) (getParent().getHeight() * HEIGHT_RATIO));
	}

	protected void addGameChat(String output) {
		while (chatPane == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		chatPane.addGameChat(output);
	}

	protected void addChat(String text) {
		chatPane.addChat(text);

	}

	protected void sendChat(String chatInput) {
		parent.sendChat(chatInput);

	}

	protected void setStat(int id, int value) {
		statPane.setStat(id, value);

	}

	/**
	 * Adds an item to the player inventory.
	 * @param item
	 */

	public void addToInventory(Item item) {
		inventoryPane.addItem(item);

	}

	/**
	 * Loads all the items in a player inventory into gui display.
	 * @param player
	 */
	public void loadInventory(Character player) {
		int[] itemIDs = player.getItems();
		//List<Integer> equippedIdx = player.getEquips();
		for (Integer id : itemIDs) {
			System.out.println("Adding Item");
			inventoryPane.addItem(Game.mapOfItems.get(id));
		}
	}

	/**
	 * Updates the stats pane to reflect any changes to the specified
	 * Character's statistics.
	 *
	 * @param player
	 *            The Character whose stats are being displayed
	 */
	public void updateStats(Character player) {
		this.statPane.updateStats(player);
	}
}
