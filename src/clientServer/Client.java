package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

	private static final int BROADCAST_CLOCK = 5;
	
	private Socket socket;

	private Client() {
		try {
			this.socket = new Socket("127.0.0.1", 5000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	public void run() {
		try {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());

			boolean exit = false;
			while (!exit) {
				// the size of the packet received
				int amount = input.readInt();
				// create array and fill with received data
				byte[] data = new byte[amount];
				input.readFully(data);
				Thread.sleep(BROADCAST_CLOCK);
			}
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		new Client();
	}

}