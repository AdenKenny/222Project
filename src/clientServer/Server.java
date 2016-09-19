package clientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import dataStorage.CreateXML;
import dataStorage.GetData;

public class Server {

	private static long uid = 0;

	private Socket socket;
	
	private Game game;

	private Server() {
		//Create a window which replaces System.out
		ConsoleWindow window = new ConsoleWindow();
		window.setVisible(true);

		//Start the game
		this.game = new Game();

		//Start the game tick
		Tick tick = new Tick(this.game);
		tick.start();
		
		new CreateXML();

		//A list of all the connections to clients
		ArrayList<Master> connections = new ArrayList<>();

		try {
			//Connect to port 5000
			ServerSocket ss = new ServerSocket(5000);
			//loop indefinitely
			while(true) {
				try {
					//wait until a new client connects
					Socket s = ss.accept();
					//create and run a master for that client
					Master m = new Master(s, uid++, this.game);
					connections.add(m);
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
		GetData d = new GetData(this.game);
	}

	public static void main(String[] args) {
		new Server();
	}

}
