package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import userHandling.Hashing;
import userHandling.Register;
import userHandling.User;
import userHandling.Verification;

public class Master extends Thread {

	private static final int BROADCAST_CLOCK = 5;
	private static final int TIMEOUT = 10;

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

	@Override
	public void run() {
		System.out.println("User " + this.uid + " connected.");
		int noResponse = 0;
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());

			boolean exit = false;
			while (!exit) {
				//if data has not been received
				if (input.available() == 0) {
					if (++noResponse >= TIMEOUT) {
						System.out.println("timeout?");
					}
				}
				else {
					noResponse = 0;
					//read the amount sent
					int amount = input.readInt();
					//create array and fill with received data
					byte[] received = new byte[amount];
					input.readFully(received);

					if (inGame) {
						this.game.readInput(this.uid, received);
					}
					else {
						if (received[0] == PackageCode.LOGIN_ATTEMPT) {
							String username = "";
							String password = "";
							int i = 1;
							byte b;
							while ((b = received[i++]) != PackageCode.BREAK) {
								username += (char)b;
							}
							while ((b = received[i++]) < received.length) {
								password += (char)b;
							}
							byte[] loginResult = new byte[2];
							loginResult[0] = PackageCode.LOGIN_RESULT;
							if (!Register.userExists(username)) {
								loginResult[1] = PackageCode.LOGIN_INCORRECT_USER;
							}
							else {
								User user = Verification.login(username, password);
								if (user == null) {
									loginResult[1] = PackageCode.LOGIN_INCORRECT_PASSWORD;
								}
								else {
									if (this.game.userOnline(user)) {
										loginResult[1] = PackageCode.LOGIN_ALREADY_CONNECTED;
									}
									else {
										this.game.registerConnection(uid, user);
										this.inGame = true;
										loginResult[1] = PackageCode.LOGIN_SUCCESS;
									}
								}
							}
							send(loginResult);
						}
						else if (received[0] == PackageCode.NEW_USER_ATTEMPT) {
							String username = "";
							String password = "";
							int i = 1;
							byte b;
							while ((b = received[i++]) != PackageCode.BREAK) {
								username += (char)b;
							}
							while ((b = received[i++]) < received.length) {
								password += (char)b;
							}
							byte[] newUserResult = new byte[2];
							newUserResult[0] = PackageCode.NEW_USER_RESULT;
							User user = Register.createUser(username, password);
							if (user == null) {
								newUserResult[1] = PackageCode.NEW_USER_NAME_TAKEN;
							}
							else {
								this.game.registerConnection(uid, user);
								this.inGame = true;
								newUserResult[1] = PackageCode.NEW_USER_SUCCESS;
							}
							send(newUserResult);
						}
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
