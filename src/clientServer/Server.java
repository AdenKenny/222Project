package clientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import dataStorage.CreateXML;
import dataStorage.DataGetter;
import dataStorage.ReadXML;
import userHandling.Register;
import util.Logging;

public class Server {

	private Game game;

	private Server() {

		//Start the game
		this.game = new Game();
		Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "The server was started");

		//Start the game tick
		Tick tick = new Tick(this.game);
		tick.start();

		new CreateXML(new DataGetter(this.game), "base");
		new ReadXML("base");

		try {
			//Connect to port 5000
			ServerSocket ss = new ServerSocket(5004);
			int uid = 0;
			//loop indefinitely
			while(true) {
				try {
					//wait until a new client connects
					Socket s = ss.accept();
					//create and run a master for that client
					Master m = new Master(s, uid++, this.game);
					m.start();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveGame() {
		new DataGetter(this.game);
	}

	public static void main(String[] args) {
		//Create a window which replaces System.out
				/*ConsoleWindow window = new ConsoleWindow();
				window.setVisible(true);*/
		new Server();
	}

}
