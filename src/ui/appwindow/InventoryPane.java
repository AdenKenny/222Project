package ui.appwindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Graphics.ImageCache;
import gameWorld.item.Item;

public class InventoryPane extends JPanel{
	public static final float WIDTH_RATIO = 0.34f;
	static int ROWS;
	static int COLS;
	private MainWindow client;

	private Item[][] items;
	private ImageCache icons;
	int colWidth;
	int rowHeight;

	public InventoryPane(MainWindow client){
		ROWS=2;
		COLS=4;
		this.client = client;
		this.items = new Item[ROWS][COLS];
		this.icons = new ImageCache();
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				processItemClick(e.getX(), e.getY(), e.getButton());
			}
		});
	}

	/*
	 * Calculates what grid space was clicked and displays
	 * the options if an item is present in that space.
	 */
	protected void processItemClick(int x, int y, int button) {
		for(int i=1; i<=ROWS; i++){
			for(int j=1; j<=COLS; j++){
				if(y<i*rowHeight && x<j*colWidth){
					System.out.println("Item: " + (i-1) + " " + (j-1) + " Selected");
					showOptions(items[i-1][j-1], x+getX(), y+getParent().getY()); //TODO: remove this
					if(items[i-1][j-1] !=null){
						//calculate position according to parent components and window bounds
						showOptions(items[i-1][j-1], x+getX(), y+getParent().getY());
					}
					return;
				}
			}
		}
	}

	/*
	 * Uses the item clicked on to call for an options list to be displayed.
	 */
	private void showOptions(Item item, int x, int y) {
		client.displayItemOptions(null, x, y); //TODO: fix to deal with Item instead of entity
	}

	/*
	 * Add an item to the players inventory.
	 * Fills the first free space starting from top left to bottom right, moving left to right.
	 * If no free spaces, no item will be added, although this method should not be called in that case.
	 */
	public void addItem(Item item){
		for(int i=0; i<ROWS; i++ ){
			for(int j=0; j<COLS; j++){
				if(items[i][j] == null){
					items[i][j] = item;
					this.repaint();
					return;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		//background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());

		//Draw boundary lines
		g.setColor(Color.black);
		colWidth = getWidth()/COLS;
		for(int i=1; i<COLS;i++){
			g.drawLine(i*colWidth, 0, i*colWidth, getHeight());
		}
		rowHeight = getHeight()/ROWS;
		for(int i=1; i<ROWS;i++){
			g.drawLine(0, i*rowHeight, getWidth(), i*rowHeight);
		}

		//draw item icons of inventory
		for(int i=0; i<ROWS; i++ ){
			for(int j=0; j<COLS; j++){
				if(items[i][j] != null){
					//draw the item based on its name
					try {
						Image icon = icons.getImage(items[i][j].getName());
						g.drawImage(icon, colWidth*j, rowHeight*i, colWidth, rowHeight, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		//Draw borders
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.setColor(new Color(23, 69, 40));
		g2.drawRect(0,0,getWidth(), getHeight());
		g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5));
		g.drawRect(0,0,getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
