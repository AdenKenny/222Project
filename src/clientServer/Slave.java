package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import IDGUI.Frame;

public class Slave extends Thread {

	private static final int BROADCAST_CLOCK = 5;

	private Socket socket;
	private DataOutputStream output;
	private boolean connected;
	private ClientSideGame game;
	private Frame frame;

	private String username;

	public Slave(Frame frame) {
		this.frame = frame;
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
		if (!this.connected) {
			return;
		}
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
				if (this.game != null) {
					if (data[0] == PackageCode.Codes.GAME_POSITION_UPDATE.value()) {
						this.game.updatePosition(data);
					}
					else if (data[0] == PackageCode.Codes.GAME_NEW_ROOM.value()) {
						this.game.newRoom(data);
					}
					else if (data[0] == PackageCode.Codes.GAME_SENDABLE_UPDATE.value()) {
						this.game.updateSendable(data);
					}
					else if (data[0] == PackageCode.Codes.GAME_SENDABLE_CREATE.value()) {
						this.game.addSendable(data);
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
							startGame();
						}
						this.frame.accountResult(data[1]);
					}
					else if (data[0] == PackageCode.Codes.NEW_USER_RESULT.value()) {
						if (data[1] == PackageCode.Codes.NEW_USER_SUCCESS.value()) {
							startGame();
						}
						this.frame.accountResult(data[1]);
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
		if (!this.connected) {
			System.out.println("Unable to connect to server.");
			return;
		}
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

		this.username = username;

		send(toSend);
	}

	public void newUser(String username, String password) {
		if (!this.connected) {
			System.out.println("Unable to connect to server.");
			return;
		}
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

		this.username = username;

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

	public void sendKeyPress(byte input) {
		byte[] toSend = new byte[1];
		toSend[0] = input;
		send(toSend);
	}

	private void startGame() {
		this.game = new ClientSideGame();
		new Tick(this.game).start();
	}

	public ClientSideGame getGame() {
		return this.game;
	}

	public boolean connected() {
		return connected;
	}

	public boolean inGame() {
		return this.game != null;
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

	public String getUsername() {
		if (this.game == null) {
			return null;
		} else {
			return this.username;
		}
	}

	public static void main(String[] args) {
		new Slave(null);
	}

}