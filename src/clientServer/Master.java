package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import userHandling.Register;
import userHandling.User;
import userHandling.Verification;
import util.Logging;

public class Master extends Thread {

	private static final int BROADCAST_CLOCK = 5;
	private static final int PING_TIMER = 400;
	private static final int TIMEOUT = 800;

	private final Socket socket;
	private final long uid;
	private final Game game;
	private boolean inGame;
	private DataOutputStream output;

	public Master(Socket socket, long uid, Game game) {
		this.socket = socket;
		this.uid = uid;
		this.game = game;
		try {
			this.output = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, "Output stream not created.");
		}
	}

	@Override
	public void run() {
		Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "User " + this.uid + " connected.");
		int noResponse = 0;
		try {
			DataInputStream input = new DataInputStream(this.socket.getInputStream());

			boolean exit = false;
			while (!exit) {
				//if data has not been received
				if (input.available() == 0) {
					noResponse++;
					if (noResponse >= TIMEOUT) {
						System.out.println("timeout");
					}
					else if (noResponse >= PING_TIMER) {
						byte[] ping = new byte[1];
						ping[0] = PackageCode.Codes.PING.value;
						send(ping);
					}
				}
				else {
					noResponse = 0;
					//read the amount sent
					int amount = input.readInt();
					//create array and fill with received data
					byte[] received = new byte[amount];
					input.readFully(received);

					if (this.inGame) {
						this.game.readInput(this.uid, received);
					}
					else {
						if (received[0] == PackageCode.Codes.LOGIN_ATTEMPT.value) {
							int i = 1;
							byte b;

							StringBuilder usernameBuilder = new StringBuilder();
							//iterate through the bytes until the break byte is encountered
							while ((b = received[i++]) != PackageCode.Codes.BREAK.value) {
								//convert the byte into a char and add it to the string builder
								usernameBuilder.append((char) b);
							}

							StringBuilder passwordBuilder = new StringBuilder();
							//iterate through the bytes from the break until the end
							while ((b = received[i++]) < received.length) {
								//convert the byte into a char and add it to the string builder
								passwordBuilder.append((char) b);
							}

							/*
							 * create a packet with two byte values
							 * the first value is to say that the packet is the login result
							 * the second is to say the result
							 */
							byte[] loginResult = new byte[2];
							loginResult[0] = PackageCode.Codes.LOGIN_RESULT.value;

							String username = usernameBuilder.toString();

							//if no users have that username
							if (!Register.userExists(username)) {
								loginResult[1] = PackageCode.Codes.LOGIN_INCORRECT_USER.value;
							}

							else {
								//check if the password is correct
								User user = Verification.login(username, passwordBuilder.toString());

								//if password is wrong
								if (user == null) {
									loginResult[1] = PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value;
								}

								else {
									//check if that user is already online
									if (this.game.userOnline(user)) {
										loginResult[1] = PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value;
									}

									else {
										//associate this connection with that user
										this.game.registerConnection(this.uid, user);
										this.inGame = true;
										loginResult[1] = PackageCode.Codes.LOGIN_SUCCESS.value;
									}
								}
							}
							//send the login result packet to the client
							send(loginResult);
						}

						else if (received[0] == PackageCode.Codes.NEW_USER_ATTEMPT.value) {
							String username = "";
							String password = "";
							int i = 1;
							byte b;

							StringBuilder usernameBuilder = new StringBuilder();

							//iterate through the bytes until the break byte is encountered
							while ((b = received[i++]) != PackageCode.Codes.BREAK.value) {
								usernameBuilder.append((char) b);
							}

							StringBuilder passwordBuilder = new StringBuilder();

							//iterate through the bytes from the break until the end
							while ((b = received[i++]) < received.length) {
								passwordBuilder.append((char) b);
							}
							/*
							 * create a packet with two byte values
							 * the first value is to say that the packet is the new user result
							 * the second is to say the result
							 */
							byte[] newUserResult = new byte[2];
							newUserResult[0] = PackageCode.Codes.NEW_USER_RESULT.value;

							//create a user
							User user = Register.createUser(usernameBuilder.toString(), passwordBuilder.toString());

							passwordBuilder = null;

							//if the username is taken
							if (user == null) {
								newUserResult[1] = PackageCode.Codes.NEW_USER_NAME_TAKEN.value;
							}

							else {
								//associate user with this connection
								this.game.registerConnection(this.uid, user);
								this.inGame = true;
								newUserResult[1] = PackageCode.Codes.NEW_USER_SUCCESS.value;
							}

							send(newUserResult);
						}
					}
				}
				if (this.inGame) {
					//send game information
					send(this.game.toByteArray(this.uid));
				}
				//sleep
				Thread.sleep(BROADCAST_CLOCK);
			}
			this.socket.close();
		} catch(IOException e) {
			Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, "User " + this.uid + " disconnected unexpectedly.");
		} catch (InterruptedException e) {
			Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, e.getMessage());
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
			Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, "Sending error.");
		}
	}

}
