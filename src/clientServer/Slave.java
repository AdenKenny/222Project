package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import userHandling.Hashing;

public class Slave extends Thread {

	private static final int BROADCAST_CLOCK = 5;

	private Socket socket;
	private DataOutputStream output;

	private Slave() {
		try {
			this.socket = new Socket("127.0.0.1", 5000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO placeholder: will be moved to class where slave is held
		start();
	}

	public void run() {
		try {
			this.output = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());

			//TODO testing login, will be done from another class
			String user = "Simon";
			String password = Hashing.createHash("hunter2".toCharArray());
			byte[] toSend = new byte[user.length() + password.length() + 2];
			int i = 0;
			for (char c : user.toCharArray()) {
				toSend[i++] = (byte)c;
			}
			toSend[i++] = 0;
			for (char c : password.toCharArray()) {
				toSend[i++] = (byte)c;
			}
			toSend[i] = 0;
			send(toSend);

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
			System.out.println("Disconnected from server.");
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void send(byte[] toSend) {
		try {
			while(this.output.size() != 0) {
				//wait for any other sending to occur
			}
			this.output.writeInt(toSend.length);
			this.output.write(toSend);
			this.output.flush();
		} catch (IOException e) {
			System.out.println("Sending error");
		}
	}

	public static void main(String[] args) {
		new Slave();
	}

}