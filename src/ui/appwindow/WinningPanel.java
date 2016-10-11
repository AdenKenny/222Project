package ui.appwindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class WinningPanel extends JPanel{

	private MainWindow parent;
	private Image winningImage;
	public WinningPanel(MainWindow parent) {
		this.parent = parent;
		this.setVisible(false);
		try {
			winningImage = ImageIO.read(new File("resources/ui/winningImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBounds(new Rectangle(0, 40, parent.getWidth(), 600));
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(winningImage, 0, 0, getWidth(), getHeight(), null);
	}

}
