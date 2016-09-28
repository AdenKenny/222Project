package ui.appwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatPane extends JPanel{
	public static float WIDTH_RATIO = 0.3f;
	private BottomPanel parent;
	protected JTextPane textArea;
	protected JScrollPane scroll;
	protected JTextArea inputBar;
	
	private SpringLayout layout;
	
	public ChatPane(BottomPanel parent){
		this.parent = parent;
		layout = new SpringLayout();
		setLayout(layout);
		setVisible(true);
	}
	
	public void initComponents(){
		this.textArea = new JTextPane();
		this.scroll = new JScrollPane(textArea);
		this.inputBar = new JTextArea();
		layout.putConstraint(SpringLayout.WEST, scroll, 15, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, scroll, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, inputBar, 3, SpringLayout.SOUTH, scroll);
		layout.putConstraint(SpringLayout.WEST, inputBar, 15, SpringLayout.WEST, this);
		textArea.setEditable(false);
		inputBar.setEditable(true);
		inputBar.setLineWrap(true);
		inputBar.setText("enter message:");
		scroll.setPreferredSize(new Dimension(450, 150));
		inputBar.setPreferredSize(new Dimension(450, 20));
		textArea.setText("Welcome to RoomScape!\n");
		inputBar.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					sendChat(inputBar.getText().trim());
					inputBar.setText("");
					inputBar.setCaretPosition(0);
				}
				
			}
		});
		add(scroll);
		add(inputBar);
		scroll.setVisible(true);
		inputBar.setVisible(true);
		textArea.setBackground(Color.BLACK);
		textArea.setVisible(true);
		revalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,getWidth(),getHeight());

		if(scroll!=null ){
			textArea.repaint();
			scroll.repaint();
			inputBar.repaint();
		}
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
	
	private void sendChat(String chatInput){
		parent.sendChat(chatInput);
	}
	
	public void addGameChat(String output){
		StyledDocument current = textArea.getStyledDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setItalic(style, true);
		try{
			textArea.getStyledDocument().insertString(current.getLength(), "\n"+output, style);
		}
		catch(Exception e){
			System.out.println(e);
		}
		scrollToEnd();
	}

	public void addChat(String text) {
		StyledDocument current = textArea.getStyledDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.RED);
		try{
			textArea.getStyledDocument().insertString(current.getLength(), "\n"+text, style);
		}
		catch(Exception e){
			System.out.println(e);
		}
		scrollToEnd();
	}
}
