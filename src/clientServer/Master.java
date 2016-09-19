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

	public Master(Socket socket, long uid, Game game) {
		this.socket = socket;
		this.uid = uid;
		this.game = game;
	}

	public void run() {
		System.out.println("User " + this.uid + " connected.");
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

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
						if (this.game.login(this.uid, received)) {
							inGame = true;
							//TODO send confirmation
						}
						else {
							//TODO send failure
						}
					}
				}
				if (inGame) {
					//send game information
					byte[] data = this.game.toByteArray(this.uid);
					output.writeInt(data.length);
					output.write(data);
					output.flush();
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

}
