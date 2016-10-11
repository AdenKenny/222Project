package ui.appwindow;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

/**
 * Mouse listener for selecting the correct list item
 * and hiding on mouse exit of the OptionsPane list.
 * An instance is created each time the OptionsPane is displayed.
 *
 * @author normanclin
 *
 */

public class OptionsListener implements MouseInputListener{

	private OptionsPane pane;

	public OptionsListener(OptionsPane pane){
		this.pane = pane;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		pane.selectOption(e.getY());
		pane.setVisible(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pane.setVisible(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}


}
