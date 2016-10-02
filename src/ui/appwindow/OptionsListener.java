package ui.appwindow;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

public class OptionsListener implements MouseInputListener{

	private OptionsPane pane;
	private Rectangle listBounds;
	
	public OptionsListener(OptionsPane pane, Rectangle listBounds){
		this.pane = pane;
		this.listBounds = listBounds;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(listBounds.contains(new Point(e.getX(), e.getY()))){
			pane.selectOption(e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
