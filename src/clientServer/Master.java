package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Master extends Thread {

	private static final int BROADCAST_CLOCK = 5;

	private final Socket socket;
	private final long uid;
	private final Game game;
	private boolean inGame;
	private DataOutputStream output;

	public Master(Socket socket, long uid, Game game) {
		this.socket = socket;
		this.uid = uid;
		this.game = game;
	}

	public void run() {
		System.out.println("User " + this.uid + " connected.");
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());

			boolean exit = false;
			while (!exit) {
				//if data has been received
				if (input.available() != 0) {
					//read the amount sent
					int amount = input.readInt();
					//create array and fill with received data
					byte[] received = new byte[amount];
					input.readFully(received);

					if (inGame) {
						this.game.readInput(this.uid, received);
					}
					else {
						byte[] loginResult = new byte[1];
						if (this.game.login(this.uid, received)) {
							this.inGame = true;
							loginResult[0] = 1;
						}
						else {
							loginResult[0] = 0;
						}
						send(loginResult);
					}
				}
				if (inGame) {
					//send game information
					send(this.game.toByteArray(this.uid));
				}
				//sleep
				Thread.sleep(BROADCAST_CLOCK);
			}
			this.socket.close();
		} catch(IOException e) {
			System.out.println("User " + this.uid + " disconnected.");
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

}
