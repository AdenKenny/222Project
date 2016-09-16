package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Master extends Thread {

	private final Socket socket;
	private final long uid;

	public Master(Socket socket, long uid) {
		this.socket = socket;
		this.uid = uid;
	}

	public void run() {
		System.out.println("User " + this.uid + " connected.");
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("User " + this.uid + " disconnected.");
		}
	}

}
