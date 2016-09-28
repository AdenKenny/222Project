package IDGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Aden Kenny and Simon Pope
 *
 */
public class MessageDialog extends JDialog implements ActionListener {
	public MessageDialog(JFrame parent, String message) {
	    super(parent, "", true);

	    JPanel messagePane = new JPanel();
	    messagePane.add(new JLabel(message));
	    getContentPane().add(messagePane);

	    JPanel buttonPane = new JPanel();
	    JButton button = new JButton("OK");
	    buttonPane.add(button);
	    button.addActionListener(this);
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);

	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();

	    Dimension parentSize = parent.getSize();
	    Point p = parent.getLocation();
	    Dimension thisSize = getSize();
	    setLocation(p.x + parentSize.width / 2 - thisSize.width / 2, p.y + parentSize.height / 2 - thisSize.height / 2);

	    setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    setVisible(false);
	    dispose();
	}
}
