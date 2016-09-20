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
	private boolean inGame;

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
			login("Simon", "hunter2");

			boolean exit = false;
			while (!exit) {
				// the size of the packet received
				int amount = input.readInt();
				// create array and fill with received data
				byte[] data = new byte[amount];
				input.readFully(data);
				if (this.inGame) {
					//TODO send to thing to deal with
				}
				else {
					if (data[0] == PackageCode.LOGIN_RESULT) {
						if (data[1] == PackageCode.LOGIN_SUCCESS) {
							System.out.println("Login successful.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.LOGIN_INCORRECT_USER) {
							System.out.println("Incorrect username.");
						}
						else if (data[1] == PackageCode.LOGIN_INCORRECT_PASSWORD) {
							System.out.println("Incorrect password.");
						}
						else if (data[1] == PackageCode.LOGIN_ALREADY_CONNECTED) {
							System.out.println("That character is already online.");
						}
					}
					else if (data[0] == PackageCode.NEW_USER_RESULT) {
						if (data[1] == PackageCode.NEW_USER_SUCCESS) {
							System.out.println("Account created.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.NEW_USER_NAME_TAKEN) {
							System.out.println("That name is unavailable.");
						}
					}
				}
				Thread.sleep(BROADCAST_CLOCK);
			}
			socket.close();
		} catch (IOException e) {
			System.out.println("Disconnected from server.");
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void login(String username, String password) {
		byte[] toSend = new byte[username.length() + password.length() + 2];
		toSend[0] = PackageCode.LOGIN_ATTEMPT;
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte)c;
		}
		toSend[i++] = PackageCode.BREAK;
		for (char c : password.toCharArray()) {
			toSend[i++] = (byte)c;
		}
		send(toSend);
	}

	public void newUser(String username, String password) {
		byte[] toSend = new byte[username.length() + password.length() + 2];
		toSend[0] = PackageCode.NEW_USER_ATTEMPT;
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte)c;
		}
		toSend[i++] = PackageCode.BREAK;
		for (char c : password.toCharArray()) {
			toSend[i++] = (byte)c;
		}
		send(toSend);
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