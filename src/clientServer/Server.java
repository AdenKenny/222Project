package clientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private Socket socket;
	
	private Server() {
		//Create a window which replaces System.out
		ConsoleWindow window = new ConsoleWindow();
		window.setVisible(true);
		
		//Start the game tick
		Tick tick = new Tick(new Game());
		tick.start();
		
		//A list of all the connections to clients
		ArrayList<Master> connections = new ArrayList<>();
		
		try {
			//Connect to port 5000
			ServerSocket ss = new ServerSocket(5000);
			//loop indefinitely
			while(1 == 1) {
				try {
					//wait until a new client connects
					Socket s = ss.accept();
					//create and run a master for that client
					Master m = new Master(s);
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

	public static void main(String[] args) {
		new Server();
	}

}
