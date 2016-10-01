package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.AttributedCharacterIterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/**
 * Displays players information
 *
 */
public class StatsPane extends JPanel{
	public static final float WIDTH_RATIO = 0.33f;
	//Fields for specifying stat to update
	public static final int HEALTH = 1;
	public static final int MAXHEALTH = 2;
	public static final int EXP = 3;
	public static final int LEVEL = 4;
	public static final int EXP_FOR_LEVEL = 5;

	private String username;
	private int health= 70;
	private int maxHealth= 100;
	private int exp=20;
	private int expForLevel=100;
	private int level; 
	
	private SpringLayout layout;
	private boolean showStats = false;
	
	private JLabel name;
	
	public StatsPane(){
		this.layout = new SpringLayout();
		setLayout(layout);
		initComponents();
		this.username = "Clinton";
		this.setVisible(true);
	}

	protected void initComponents() {
		showStats = true;
		this.name = new JLabel(username);
		add(name);
		revalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0,0,getWidth(), getHeight());
		System.out.println("Stats: " + getWidth() + "x" + getHeight());
		if(showStats){
			//Health bar
			g.setColor(Color.red);
			g.fillRect(getWidth()/4, 40, getWidth()/2, 10); //MaxHealth
			g.setColor(Color.green);
			g.fillRect(getWidth()/4, 40, (getWidth()/2)*(health+1)/(maxHealth+1), 10); //currentHealth
			
			//Exp bar
			g.setColor(Color.orange);
			g.fillRect(getWidth()/4, 90, getWidth()/2, 10);
			g.setColor(Color.yellow);
			g.fillRect(getWidth()/4, 90,getWidth()/2*(exp+1)/(expForLevel+1), 10);
			
		}
		

	}
	
	protected void setStat(int id, int value){
		switch (id) {
		case HEALTH:
			setHealth(value);
			break;
		case MAXHEALTH:
			setMaxHealth(value);
			break;
		case EXP:
			setExp(value);
			break;
		case LEVEL:
			setLevel(value);
			break;
		case EXP_FOR_LEVEL:
			setTotalExp(value);
			break;
		default:
			break;
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
		this.expForLevel = totalExp;
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
