package clientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
					if (data[0] == PackageCode.Codes.LOGIN_RESULT.value) {
						if (data[1] == PackageCode.Codes.LOGIN_SUCCESS.value) {
							System.out.println("Login successful.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.Codes.LOGIN_INCORRECT_USER.value) {
							System.out.println("Incorrect username.");
						}
						else if (data[1] == PackageCode.Codes.LOGIN_INCORRECT_PASSWORD.value) {
							System.out.println("Incorrect password.");
						}
						else if (data[1] == PackageCode.Codes.LOGIN_ALREADY_CONNECTED.value) {
							System.out.println("That character is already online.");
						}
					}
					else if (data[0] == PackageCode.Codes.NEW_USER_RESULT.value) {
						if (data[1] == PackageCode.Codes.NEW_USER_SUCCESS.value) {
							System.out.println("Account created.");
							this.inGame = true;
						}
						else if (data[1] == PackageCode.Codes.NEW_USER_NAME_TAKEN.value) {
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
		toSend[0] = PackageCode.Codes.LOGIN_ATTEMPT.value;
		int i = 1;
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		toSend[i++] = PackageCode.Codes.BREAK.value;
		for (char c : password.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		send(toSend);
	}

	public void newUser(String username, String password) {
		byte[] toSend = new byte[username.length() + password.length() + 2];
		toSend[0] = PackageCode.Codes.NEW_USER_ATTEMPT.value;
		int i = 1;
		
		for (char c : username.toCharArray()) {
			toSend[i++] = (byte) c;
		}
		
		toSend[i++] = PackageCode.Codes.BREAK.value;
		
		for (char c : password.toCharArray()) {
			toSend[i++] = (byte) c;
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
		} 
		
		catch (IOException e) {
			System.out.println("Sending error");
		}
	}

	public static void main(String[] args) {
		new Slave();
	}

}