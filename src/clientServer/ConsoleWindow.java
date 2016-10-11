package clientServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataStorage.SaveGame;
import gameWorld.characters.Character;
import util.Logging;

/**
 * The frame the server runs in
 *
 * @author popesimo
 *
 */
public class ConsoleWindow extends JFrame {

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

	    setVisible(true);

	    //closing the window will stop the server
	    addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "Stopping the server...");
	        	SaveGame saver = new SaveGame();

	        	for (Character player : ServerSideGame.getAllPlayers().values()) {
	        		saver.savePlayer(player);
	        	}
	        	saver.saveFile();
	        	Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "The server has stopped.");
	            System.exit(0);
	        }
	    });

	    new Server().run();
	}
}