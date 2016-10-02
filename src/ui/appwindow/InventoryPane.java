package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameWorld.item.Item;

public class InventoryPane extends JPanel{
	public static float WIDTH_RATIO = 0.34f;
	public static final int ROWS = 2;
	public static final int COLS = 4;
	
	private Item[][] items;
	private static HashMap<String, Image> itemIcons;
	int colWidth;
	int rowHeight;
	
	public InventoryPane(){
		this.items = new Item[ROWS][COLS];
		this.itemIcons = new HashMap<>();
		try {
			//C:\Users\OEM\eclipse\workspace\RoomScape\resources\resources\graphics
			itemIcons.put("Diamond Short Sword", ImageIO.read(new File("resources/resources/graphics/diamondShortSword.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				processItemClick(e.getX(), e.getY(), e.getButton());
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	protected void processItemClick(int x, int y, int button) {
		for(int i=1; i<=ROWS; i++){
			for(int j=1; j<=COLS; j++){
				if(y<i*rowHeight && x<j*colWidth){
					System.out.println("Item: " + (i-1) + " " + (j-1) + "Selected");
					if(items[i][j] !=null){
						showOptions(items[i-1][j-1]);
					}
					return;
				}
			}
		}
		
	}

	private void showOptions(Item item) {
		// TODO Auto-generated method stub
	}

	public void addItem(Item item){
		for(int i=0; i<ROWS; i++ ){
			for(int j=0; j<COLS; j++){
				if(items[i][j] == null){
					items[i][j] = item;
					return;
				}
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		System.out.println("inventory: " + getWidth() + "x" + getHeight());
		g.setColor(Color.black);
		colWidth = getWidth()/COLS;
		for(int i=1; i<COLS;i++){
			g.drawLine(i*colWidth, 0, i*colWidth, getHeight());
		}
		rowHeight = getHeight()/ROWS;
		for(int i=1; i<ROWS;i++){
			g.drawLine(0, i*rowHeight, getWidth(), i*rowHeight);
		}
		
		//draw item icons
		for(int i=0; i<ROWS; i++ ){
			for(int j=0; j<COLS; j++){
				if(items[i][j] != null){
					//draw the item based on its name
				}
				g.drawImage(itemIcons.get("Diamond Short Sword"), colWidth*j, rowHeight*i, colWidth, rowHeight, null);
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
