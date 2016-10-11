package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import gameWorld.Entity;
import gameWorld.Sendable;
import gameWorld.characters.Character;
import ui.appwindow.MainWindow;

public class Slave extends Thread {
	private static final String DEFAULT_ADDRESS = "127.0.0.1";
	private static final int BROADCAST_CLOCK = 50;

	private Socket socket;
	private DataOutputStream output;
	private boolean connected;
	private ClientSideGame game;
	private MainWindow mainWindow;
	private String username;

	public Slave(MainWindow mainWindow, String address) {
		this.mainWindow = mainWindow;
		try {
			if (address == null) {
				this.socket = new Socket(DEFAULT_ADDRESS, Server.PORT);
			}
			else {
				this.socket = new Socket(address, Server.PORT);
			}
			this.output = new DataOutputStream(this.socket.getOutputStream());
			this.connected = true;
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (ConnectException e) {
			this.connected = false;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (!this.connected) {
			this.mainWindow.addGameChat("Unable to connect to server.");
			return;
		}
		try(DataInputStream input = new DataInputStream(this.socket.getInputStream())) {
			while (true) {
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
					if (data[0] == PackageCode.Codes.GAME_NEW_ROOM.value()) {
						this.game.newRoom(data);
						this.mainWindow.setRoom(this.game.getFloor(), this.game.getRoom());
					}

					else if (data[0] == PackageCode.Codes.GAME_SENDABLE_END.value()) {
						this.game.endSendables();
					}

					else if (data[0] == PackageCode.Codes.GAME_SENDABLE.value()) {
						this.game.updateSendable(data);
						if (data[1] == Character.Type.PLAYER.ordinal()) {
							this.mainWindow.updateStats(this.game.getPlayer());
						}
					}

					else if (data[0] == PackageCode.Codes.TEXT_MESSAGE.value()) {
						StringBuilder message = new StringBuilder();
						for (int i = 1; i < data.length; i++) {
							message.append((char)data[i]);
						}
						this.mainWindow.addChat(message.toString());
					}
				}

				else {
					if (data[0] == PackageCode.Codes.LOGIN_RESULT.value()) {
						if (data[1] == PackageCode.Codes.LOGIN_SUCCESS.value()) {
							startGame();
						}
						this.mainWindow.accountResult(data[1]);
					}
					else if (data[0] == PackageCode.Codes.NEW_USER_RESULT.value()) {
						if (data[1] == PackageCode.Codes.NEW_USER_SUCCESS.value()) {
							startGame();
						}
						this.mainWindow.accountResult(data[1]);
					}
				}

				Thread.sleep(BROADCAST_CLOCK);
			}
		} catch(SocketException e) {
			this.mainWindow.addGameChat("Disconnected from server.");
			this.connected = false;
		} catch (IOException e) {
			this.mainWindow.addGameChat("Disconnected from server.");
			this.connected = false;
		} catch (InterruptedException e) {
			System.out.println(e);
			this.connected = false;
		}
	}

	public void login(String username, char[] password) {
		if (!this.connected) {
			this.mainWindow.addGameChat("Unable to connect to server.");
			return;
		}
		byte[] toSend = new byte[username.length() + password.length + 2];
		toSend[0] = PackageCode.Codes.LOGIN_ATTEMPT.value();
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		toSend[i++] = PackageCode.Codes.BREAK.value();
		for (char c : password) {
			toSend[i++] = (byte) c;
		}
		this.username = username;
		send(toSend);
	}

	public void newUser(String username, char[] password) {
		if (!this.connected) {
			this.mainWindow.addGameChat("Unable to connect to server.");
			return;
		}
		byte[] toSend = new byte[username.length() + password.length + 2];
		toSend[0] = PackageCode.Codes.NEW_USER_ATTEMPT.value();
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		toSend[i++] = PackageCode.Codes.BREAK.value();
		for (char c : password) {
			toSend[i++] = (byte) c;
		}
		this.username = username;
		send(toSend);
	}

	public synchronized void send(byte[] toSend) {
		try {
			if (this.output == null) {
				return;
			}
			this.output.writeInt(toSend.length);
			this.output.write(toSend);
		}
		catch (SocketException e) {
			this.mainWindow.addGameChat("Disconnected from server.");
			this.connected = false;
		}
		catch (IOException e) {
			System.out.println("Sending error");
			e.printStackTrace();
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
		this.game = new ClientSideGame(this.username);
		new Tick(this.game).start();
	}

	public synchronized ClientSideGame getGame() {
		return this.game;
	}

	public boolean connected() {
		return this.connected;
	}

	public boolean inGame() {
		return this.game != null;
	}

	public void close() {
		try {
			byte[] disconnect = new byte[1];
			disconnect[0] = PackageCode.Codes.DISCONNECT.value();
			send(disconnect);
			if (this.socket == null) {
				return;
			}
			this.socket.close();
		}
		catch (SocketException e) {
			// nothing needs to be done, as the server connection is closed already
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	public String getUsername() {
		if (this.game == null) {
			return null;
		}
		else {
			return this.username;
		}
	}

	public void performActionOnEntity(Entity entity, String actionName) {
		if (!(entity instanceof Sendable)) {
			return;
		}

		byte[] toSend = new byte[actionName.length() + 5];
		toSend[0] = PackageCode.Codes.PERFORM_ACTION.value();

		byte[] entityID = Sendable.intToBytes(entity.ID());
		for (int i = 0; i < 4; ++i) {
			toSend[i+1] = entityID[i];
		}

		int i = 5;
		for (char c : actionName.toCharArray()) {
			toSend[i++] = (byte) c;
		}

		send(toSend);
	}
}