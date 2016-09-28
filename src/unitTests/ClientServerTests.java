package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clientServer.ClientSideGame;
import clientServer.Slave;
import gameWorld.Sendable;

public class ClientServerTests {

	/**
	 * Requires a server to be running.
	 */
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

	@Test
	public void intToBytesToInt() {
		Sendable basic = new Sendable() {
			public byte[] onEntry() {return null;}
			public byte[] roomUpdate() {return null;}
		};
		int num = 70000;
		byte[] bytes = basic.intToByte(num);
		int back = new ClientSideGame().bytesToInt(bytes, 0);
		assertEquals(num, back);
	}

}
