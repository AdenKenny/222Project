package clientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Logging;

public class Server {

	private ServerSideGame game;
	private Tick tick;

	public Server() {

		//Start the game
		this.game = new ServerSideGame();
		Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "The server was started");

		//Start the game tick
		this.tick = new Tick(this.game);

	}

	public void run() {
		this.tick.start();

		//Connect to port 5000
		try(ServerSocket ss = new ServerSocket(5000)) {
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

	public static void main(String[] args) {
		//Create a window which replaces System.out
		ConsoleWindow window = new ConsoleWindow();
	}

}