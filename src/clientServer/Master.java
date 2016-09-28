package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import userHandling.Register;
import userHandling.User;
import userHandling.Verification;
import util.Logging;

import gameWorld.characters.Character;

public class Master extends Thread {

	private static final int BROADCAST_CLOCK = 5;
	private static final int PING_TIMER = 1000;
	private static final int TIMEOUT = 2000;

	private final Socket socket;
	private final long uid;
	private final ServerSideGame game;
	private boolean inGame;
	private DataOutputStream output;
	private int messagesReceived;

	public Master(Socket socket, long uid, ServerSideGame game) {
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
					//if no response has been received for a certain time, disconnect the user
					if (noResponse == TIMEOUT) {
						this.game.disconnect(this.uid);
						Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "User " + this.uid + " timed out.");
						return;
					}
					//if no response has been received for a certain time, send a ping
					if (noResponse == PING_TIMER) {
						byte[] ping = new byte[1];
						ping[0] = PackageCode.Codes.PING.value();
						send(ping);
					}
				}

				else {
					//reset timeout, as data has been received
					noResponse = 0;
					//read the amount sent
					int amount = input.readInt();
					//create array and fill with received data
					byte[] received = new byte[amount];
					input.readFully(received);

					if (received[0] == PackageCode.Codes.DISCONNECT.value()) {
						this.game.disconnect(this.uid);
						Logging.logEvent(Server.class.getName(), Logging.Levels.EVENT, "User " + this.uid + " disconnected.");
						break;
					}

					if (this.inGame) {
						if (received[0] == PackageCode.Codes.TEXT_MESSAGE.value()) {
							textMessage(received);
						}
						else if (received[0] >= PackageCode.Codes.KEY_PRESS_W.value() && received[0] <= PackageCode.Codes.KEY_PRESS_E.value()) {
							this.game.keyPress(this.uid, received[0]);
						}
					}

					else {
						if (received[0] == PackageCode.Codes.LOGIN_ATTEMPT.value()) {
							login(received);
						}
						else if (received[0] == PackageCode.Codes.NEW_USER_ATTEMPT.value()) {
							newUser(received);
						}
					}
				}
				if (this.inGame) {
					//send game information
					byte[][] packets = this.game.toByteArray(this.uid);
					for (byte[] toSend : packets) {
						if (toSend != null) {
							send(toSend);
						}
					}
					getMessages();
				}
				//sleep
				Thread.sleep(BROADCAST_CLOCK);
			}
		} catch(IOException e) {
			this.game.disconnect(this.uid);
			System.out.println("User " + this.uid + " disconnected.");
		} catch (InterruptedException e) {
			this.game.disconnect(this.uid);
			Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, e.getMessage());
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				Logging.logEvent(Server.class.getName(), Logging.Levels.WARNING, e.getMessage());
			}
		}
	}

	private void textMessage(byte[] received) {
		StringBuilder message = new StringBuilder();
		for (int i = 1; i < received.length; i++) {
			message.append((char)received[i]);
		}
		this.game.textMessage(this.uid, message.toString());
	}

	private void getMessages() throws IOException {
		String[] messages = this.game.getMessages(this.messagesReceived);
		this.messagesReceived += messages.length;
		for (String message : messages) {
			byte[] toSend = new byte[message.length() + 1];
			toSend[0] = PackageCode.Codes.TEXT_MESSAGE.value();
			int i = 1;
			for (char c : message.toCharArray()) {
				toSend[i++] = (byte) c;
			}
			send(toSend);
		}
	}

	private void login(byte[] received) throws IOException {
		int i = 1;
		byte b;

		StringBuilder usernameBuilder = new StringBuilder();
		//iterate through the bytes until the break byte is encountered
		while ((b = received[i++]) != PackageCode.Codes.BREAK.value()) {
			//convert the byte into a char and add it to the string builder
			usernameBuilder.append((char) b);
		}

		StringBuilder passwordBuilder = new StringBuilder();
		//iterate through the bytes from the break until the end
		for (; i < received.length; i++) {
			//convert the byte into a char and add it to the string builder
			passwordBuilder.append((char)received[i]);
		}

		/*
		 * create a packet with two byte values
		 * the first value is to say that the packet is the login result
		 * the second is to say the result
		 */
		byte[] loginResult = new byte[2];
		loginResult[0] = PackageCode.Codes.LOGIN_RESULT.value();

		String username = usernameBuilder.toString();

		//if no users have that username
		if (!Register.userExists(username)) {
			loginResult[1] = PackageCode.Codes.LOGIN_INCORRECT_USER.value();
		}

		else {
			//check if the password is correct
			User user = Verification.login(username, passwordBuilder.toString());

			//if password is wrong
			if (user == null) {
				loginResult[1] = PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value();
			}

			else {
				//check if that user is already online
				if (this.game.userOnline(user)) {
					loginResult[1] = PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value();
				}

				else {
					//associate this connection with that user
					this.game.registerConnection(this.uid, user);
					this.inGame = true;
					loginResult[1] = PackageCode.Codes.LOGIN_SUCCESS.value();
					ServerSideGame.getAllPlayers().put(user.getUsername(), new Character(user.getUsername()));
				}
			}
		}
		//send the login result packet to the client
		send(loginResult);
	}

	private void newUser(byte[] received) throws IOException {
		int i = 1;
		byte b;

		StringBuilder usernameBuilder = new StringBuilder();

		//iterate through the bytes until the break byte is encountered
		while ((b = received[i++]) != PackageCode.Codes.BREAK.value()) {
			usernameBuilder.append((char) b);
		}

		StringBuilder passwordBuilder = new StringBuilder();

		//iterate through the bytes from the break until the end
		while (i < received.length) {
			passwordBuilder.append((char)received[i++]);
		}
		/*
		 * create a packet with two byte values
		 * the first value is to say that the packet is the new user result
		 * the second is to say the result
		 */
		byte[] newUserResult = new byte[2];
		newUserResult[0] = PackageCode.Codes.NEW_USER_RESULT.value();

		//create a user
		User user = Register.createUser(usernameBuilder.toString(), passwordBuilder.toString());

		passwordBuilder = null;

		//if the username is taken
		if (user == null) {
			newUserResult[1] = PackageCode.Codes.NEW_USER_NAME_TAKEN.value();
		}

		else {
			//associate user with this connection
			this.game.registerConnection(this.uid, user);
			this.inGame = true;
			newUserResult[1] = PackageCode.Codes.NEW_USER_SUCCESS.value();
		}

		send(newUserResult);
	}

	public void send(byte[] toSend) throws IOException {
		int time = 0;
		while(this.output.size() != 0) {
			//wait for any other sending to occur
			//if it is taking too long, flush the output
			if (time++ == 10000) {
				this.output.flush();
				break;
			}
		}
		this.output.writeInt(toSend.length);
		this.output.write(toSend);
		this.output.flush();
	}

}
