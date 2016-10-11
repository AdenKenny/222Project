package ui.appwindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	public static final float WIDTH_RATIO = 0.33f; //fraction of display width used
	private BottomPanel parent;
	protected JTextPane textArea;
	protected JScrollPane scroll;
	protected JTextArea inputBar;

	private SpringLayout layout;

	public ChatPane(BottomPanel parent){
		this.parent = parent;
		this.layout = new SpringLayout();
		setLayout(this.layout);
		setVisible(true);
	}

	protected void initComponents(){
		this.textArea = new JTextPane();
		this.scroll = new JScrollPane(this.textArea);
		this.inputBar = new JTextArea();
		this.layout.putConstraint(SpringLayout.WEST, this.scroll, 25, SpringLayout.WEST, this);
		this.layout.putConstraint(SpringLayout.NORTH, this.scroll, 15, SpringLayout.NORTH, this);
		this.layout.putConstraint(SpringLayout.NORTH, this.inputBar, 3, SpringLayout.SOUTH, this.scroll);
		this.layout.putConstraint(SpringLayout.WEST, this.inputBar, 25, SpringLayout.WEST, this);
		this.textArea.setEditable(false);
		this.inputBar.setEditable(true);
		this.inputBar.setLineWrap(true);
		this.scroll.setPreferredSize(new Dimension(550, 150));
		this.inputBar.setPreferredSize(new Dimension(550, 20));
		this.textArea.setText("Welcome to RoomScape!\n");
		this.inputBar.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					sendChat(ChatPane.this.inputBar.getText().trim());
					ChatPane.this.inputBar.setText("");
					ChatPane.this.inputBar.setCaretPosition(0);
					parent.parent.requestFocus();//restore key input to game movement

				}
			}
		});
		add(this.scroll);
		add(this.inputBar);
		this.scroll.setVisible(true);
		this.inputBar.setVisible(true);
		this.textArea.setBackground(Color.BLACK);
		this.textArea.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		this.scroll.setPreferredSize(new Dimension(getWidth()-80, getHeight()-60));
		this.inputBar.setPreferredSize(new Dimension(getWidth()-80, 20));
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0,0,getWidth(),getHeight());
		if(this.scroll!=null ){
			this.textArea.repaint();
			this.scroll.repaint();
			this.inputBar.repaint();
		}
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.setColor(new Color(23, 69, 40));
		g2.drawRect(0,0,getWidth(), getHeight());
		g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5));
		g.drawRect(0,0,getWidth(), getHeight());
	}

	/*
	 * Makes the JScrollPane scroll down to the bottom of the text area so
	 * that the latest input is shown
	 */
	public void scrollToEnd() {
		this.scroll.getVerticalScrollBar().setValue(this.scroll.getVerticalScrollBar().getMaximum());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (getParent().getWidth()*WIDTH_RATIO), (int) (getParent().getHeight()));
	}

	void sendChat(String chatInput){
		this.parent.sendChat(chatInput);
	}

	public void addGameChat(String output){
		StyledDocument current = this.textArea.getStyledDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.RED);
		try{
			this.textArea.getStyledDocument().insertString(current.getLength(), "\n"+output, style);
		}
		catch(Exception e){
			System.out.println(e);
		}
		scrollToEnd();
	}

	public void addChat(String text) {
		StyledDocument current = this.textArea.getStyledDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setItalic(style, true);
		try{
			this.textArea.getStyledDocument().insertString(current.getLength(), "\n"+text, style);
		}
		catch(Exception e){
			System.out.println(e);
		}
		scrollToEnd();
	}
}
