package clientServer;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public ConsoleWindow() {
		super("Server");

		setSize(600, 600);

	    final JTextArea textArea = new JTextArea();
	    textArea.setEditable(false);

	    //custom output stream
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				//add text to end
				textArea.append(String.valueOf((char)b));
				//set next insertion point to end
		        textArea.setCaretPosition(textArea.getDocument().getLength());
			}
	    };
	    //create a new
	    PrintStream printStream = new PrintStream(out);

	    // re-assigns standard output stream and error output stream
	    System.setOut(printStream);
	    System.setErr(printStream);

	    //Scrollpane that holds the output stream
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    //lefthand border of 3 pixels
	    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
	    add(scrollPane);

	    //closing the window will stop the server
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}