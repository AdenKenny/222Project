package ui.appwindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

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

	private int health= 70;
	private int maxHealth= 100;
	private int exp=20;
	private int expForLevel=100;
	private int level;

	private boolean showStats = false;

	public StatsPane(){
		setVisible(true);

	}

	protected void initComponents() {
		showStats = true;

		revalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0,0,getWidth(), getHeight());
		if(showStats){
			//Health bar
			g.setColor(Color.black);
			g.drawString("Health:", 50, 100);
			g.setColor(Color.red);
			g.fillRect(getWidth()/4, 90, getWidth()/2, 10); //MaxHealth
			g.setColor(Color.green);
			g.fillRect(getWidth()/4, 90, (getWidth()/2)*(health+1)/(maxHealth+1), 10); //currentHealth
			System.out.println("Drawing stats, Health: " +health);
			//Exp bar
			g.setColor(Color.black);
			g.drawString("Exp:", 50, 150);
			g.setColor(Color.orange);
			g.fillRect(getWidth()/4, 140, getWidth()/2, 10);
			g.setColor(Color.yellow);
			g.fillRect(getWidth()/4, 140,getWidth()/2*(exp+1)/(expForLevel+1), 10);


			g.setColor(Color.black);
			g.setFont(new Font("Level "+level, Font.BOLD, 20));
			g.drawString("Level "+level, getWidth()/2-40, 30);


            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
            g2.setColor(new Color(23, 69, 40));
			g2.drawRect(0,0,getWidth(), getHeight());
			g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(5));
			g.drawRect(0,0,getWidth(), getHeight());
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

	protected void setShowStats(boolean showStats) {
		this.showStats = showStats;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
}
