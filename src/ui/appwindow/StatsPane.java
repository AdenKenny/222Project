package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.AttributedCharacterIterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * Displays players information
 *
 */
public class StatsPane extends JPanel{
	public static float WIDTH_RATIO = 0.33f;
	//Fields for specifying stat to update
	public static int HEALTH = 1;
	public static int MAXHEALTH = 2;
	public static int EXP = 3;
	public static int LEVEL = 4;
	public static int EXP_FOR_LEVEL = 5;

	private String username;
	private int health;
	private int maxHealth;
	private int exp;
	private int totalExp;
	private int level; 
	
	private SpringLayout layout;
	private boolean showStats = false;
	
	public StatsPane(){
		this.layout = new SpringLayout();
		initComponents();
		this.username = "Clinton";
	}

	protected void initComponents() {
		showStats = true;
		revalidate();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0,0,getWidth(), getHeight());

		if(showStats){
			//Health bar
			g.setColor(Color.red);
			g.fillRect(getWidth()/4, 40, getWidth()/2, 10);
			g.setColor(Color.green);
			g.fillRect(getWidth()/4, 40, getWidth()/2-40, 10);
			
			//Exp bar
			g.setColor(Color.orange);
			g.fillRect(getWidth()/4, 90, getWidth()/2, 10);
			g.setColor(Color.yellow);
			g.fillRect(getWidth()/4, 90, getWidth()/2-70, 10);
			
		}
		

	}
	
	protected void setUsername(String username) {
		this.username = username;
	}

	protected void setHealth(int health) {
		this.health = health;
	}

	protected void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	protected void setExp(int exp) {
		this.exp = exp;
	}

	protected void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	protected void setLevel(int level) {
		this.level = level;
	}

	protected void setLayout(SpringLayout layout) {
		this.layout = layout;
	}

	protected void setShowStats(boolean showStats) {
		this.showStats = showStats;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
