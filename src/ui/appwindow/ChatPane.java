package ui.appwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class ChatPane extends JPanel{
	public static float WIDTH_RATIO = 0.3f;
	protected JTextPane textArea;
	protected JScrollPane scroll;
	
	public ChatPane(){
		this.textArea = new JTextPane();
		this.scroll = new JScrollPane(textArea);
		textArea.setEditable(false);
		textArea.setText("Welcome to RoomScape!\n\n\n");
		add(scroll, BorderLayout.NORTH);
		scroll.setVisible(true);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setVisible(true);
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
