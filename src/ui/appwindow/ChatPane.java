package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

public class ChatPane extends JPanel{
	public static float WIDTH_RATIO = 0.3f;
	protected JTextPane textArea;
	protected JScrollPane scroll;
	
	private SpringLayout layout;
	
	public ChatPane(){
		layout = new SpringLayout();
		setLayout(layout);
	}
	
	public void initComponents(){
		System.out.println(getWidth());
		
		this.textArea = new JTextPane();
		this.scroll = new JScrollPane(textArea);
		calculateLayoutConstraints();
		textArea.setEditable(false);
		textArea.setBounds( 0, 0, 200, 200 );
		textArea.setPreferredSize(new Dimension(getWidth()-20, getHeight()-50));
		textArea.setText("Welcome to RoomScape!\n\n\n");
		add(scroll);
		scroll.setVisible(true);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setVisible(true);
		revalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,getWidth(),getHeight());

		if(scroll!=null ){
			calculateLayoutConstraints();
			textArea.repaint();
			scroll.repaint();
		}
	}
	
	private void calculateLayoutConstraints() {
		layout.putConstraint(SpringLayout.WEST, scroll, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, scroll, 5, SpringLayout.NORTH, this);	
	}

	/*
	 * Makes the JScrollPane scroll down to the bottom of the text area so 
	 * that the latest input is shown
	 */
	public void scrollToEnd() {
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}
	
	public void addGameChat(String output){
		textArea.setText("output");
	}
}
