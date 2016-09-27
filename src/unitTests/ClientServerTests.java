package unitTests;

import org.junit.Test;
import clientServer.Slave;

public class ClientServerTests {

	@Test
	public void textMessage() {
		Slave slave = new Slave();
		slave.start();
		slave.login("Simon", "hunter2");
		slave.sendTextMessage("1");
		slave.sendTextMessage("2");
		slave.sendTextMessage("3");
		while (true) {}
	}

}
