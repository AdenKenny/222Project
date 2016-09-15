package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Master extends Thread {

	private final Socket socket;
	
	public Master(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
