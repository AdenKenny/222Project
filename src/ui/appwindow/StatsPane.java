package ui.appwindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import gameWorld.characters.Character;

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
	public static final int DAMAGE = 6;

	private int health= 70;
	private int maxHealth= 100;
	private int exp=20;
	private int expForLevel=100;
	private int level;
	private int damage;

	private boolean showStats = false;  //Tells paint() that there is relevant data loaded from player class.

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
			//Exp bar
			g.setColor(Color.black);
			g.drawString("Exp:", 50, 150);
			g.setColor(Color.orange);
			g.fillRect(getWidth()/4, 140, getWidth()/2, 10);
			g.setColor(Color.yellow);
			g.fillRect(getWidth()/4, 140,getWidth()/2*(exp+1)/(expForLevel+1), 10);


			//Level and damage text
			g.setColor(Color.black);
			g.setFont(new Font("Combat Level: "+level, Font.BOLD, 20));
			g.drawString("Combat Level: "+level, getWidth()/2-100, 30);
			g.setFont(new Font("Damage: "+damage, Font.BOLD, 20));
			g.drawString("Damage: " + damage, getWidth()/2-80, 60);

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
		case DAMAGE:
			setDamage(value);
			break;
		default:
			break;
		}
	}

	private void setDamage(int value) {
		this.damage=value;

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

	/**
	 * Updates the stats pane to reflect any changes to the specified
	 * Character's statistics.
	 *
	 * @param player
	 *            The Character whose stats are being displayed
	 */
	public void updateStats(Character player) {
		this.setHealth(player.getHealth());
		this.setMaxHealth(player.getMaxHealth());
		this.setLevel(player.getLevel());
		this.setExp(player.getXp());
		this.setTotalExp(player.getXpForLevel());
		this.setDamage(player.getAttack());
		this.repaint();
	}
}
