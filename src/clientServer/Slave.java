package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import util.Logging;
import util.Logging.Levels;

public class Slave extends Thread {

	private static final int BROADCAST_CLOCK = 5;

	private Socket socket;
	private DataOutputStream output;
	private boolean connected;
	private boolean inGame;

	public Slave() {
		try {
			this.socket = new Socket("127.0.0.1", 5000);
			this.output = new DataOutputStream(socket.getOutputStream());
			this.connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Unable to connect to server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());

			boolean exit = false;
			while (!exit) {
				// the size of the packet received
				int amount = input.readInt();
				// create array and fill with received data
				byte[] data = new byte[amount];
				input.readFully(data);
				if (data[0] == PackageCode.Codes.PING.value()) {
					byte[] pong = new byte[1];
					pong[0] = PackageCode.Codes.PONG.value();
					send(pong);
				}
				if (this.inGame) {
					if (data[0] == PackageCode.Codes.GAME_POSITION_UPDATE.value()) {
						//TODO send to thing to deal with
					}
					else if (data[0] == PackageCode.Codes.GAME_ROOM_UPDATE.value()) {
						//TODO send to thing to deal with
					}
					else if (data[0] == PackageCode.Codes.GAME_ROOM_ENTRY.value()) {
						//TODO send to thing to deal with
					}
					else if (data[0] == PackageCode.Codes.TEXT_MESSAGE.value()) {
						StringBuilder message = new StringBuilder();
						for (int i = 1; i < data.length; i++) {
							message.append((char)data[i]);
						}
						//TODO send the received message to relevant class
						System.out.println(message.toString());
					}
				}
				else {
					if (data[0] == PackageCode.Codes.LOGIN_RESULT.value()) {
						if (data[1] == PackageCode.Codes.LOGIN_SUCCESS.value()) {
							System.out.println("Login successful.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.Codes.LOGIN_INCORRECT_USER.value()) {
							System.out.println("Incorrect username.");
						}
						else if (data[1] == PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value()) {
							System.out.println("Incorrect password.");
						}
						else if (data[1] == PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value()) {
							System.out.println("That character is already online.");
						}
					}
					else if (data[0] == PackageCode.Codes.NEW_USER_RESULT.value()) {
						if (data[1] == PackageCode.Codes.NEW_USER_SUCCESS.value()) {
							System.out.println("Account created.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.Codes.NEW_USER_NAME_TAKEN.value()) {
							System.out.println("That name is unavailable.");
						}
					}
				}
				Thread.sleep(BROADCAST_CLOCK);
			}
			this.socket.close();
		} catch (IOException e) {
			System.out.println("Disconnected from server.");
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void login(String username, String password) {
		byte[] toSend = new byte[username.length() + password.length() + 2];
		toSend[0] = PackageCode.Codes.LOGIN_ATTEMPT.value();
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		toSend[i++] = PackageCode.Codes.BREAK.value();
		for (char c : password.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		send(toSend);
	}

	public void newUser(String username, String password) {
		byte[] toSend = new byte[username.length() + password.length() + 2];
		toSend[0] = PackageCode.Codes.NEW_USER_ATTEMPT.value();
		int i = 1;

		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}

		toSend[i++] = PackageCode.Codes.BREAK.value();

		for (char c : password.toCharArray()) {
			toSend[i++] = (byte) c;
		}

		send(toSend);
	}

	public void send(byte[] toSend) {
		try {
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

		catch (IOException e) {
			System.out.println("Sending error");
		}
	}

	public void sendTextMessage(String message) {
		byte[] toSend = new byte[message.length() + 1];
		toSend[0] = PackageCode.Codes.TEXT_MESSAGE.value();
		int i = 1;
		for (char c : message.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		send(toSend);
	}

	public void sendUserInput(byte input) {
		byte[] toSend = new byte[2];
		toSend[0] = PackageCode.Codes.USER_INPUT.value();
		toSend[1] = input;
		send(toSend);
	}

	public boolean connected() {
		return connected;
	}

	public void close() {
		try {
			byte[] disconnect = new byte[1];
			disconnect[0] = PackageCode.Codes.DISCONNECT.value();
			send(disconnect);
			this.socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		new Slave();
	}

}