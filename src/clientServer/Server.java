package clientServer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Logging;

public class Server {

	private ServerSideGame game;

	public Server() {

		Logging.checkFile();

		//Start the game
		this.game = new ServerSideGame();

	}

	public void run() {

		//Connect to port 5000
		try(ServerSocket ss = new ServerSocket(5000)) {
			Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "The server was started.");

			//start the game tick
			new Tick(this.game).start();
			Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "The game tick has begun.");

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
		}
		catch (BindException e) {
			System.out.println("A server is already running on this computer.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//Create a window which replaces System.out
		new ConsoleWindow();
	}

}