package unitTests;

import org.junit.Test;

import clientServer.Server;
import clientServer.Slave;

public class ClientServerTests {

	@Test
	public void textMessage() {
		Server server = new Server();
		Slave slave = new Slave();
		slave.sendTextMessage("hi");
		slave.sendTextMessage("how are you?");
		slave.sendTextMessage("/ff");
	}

}
