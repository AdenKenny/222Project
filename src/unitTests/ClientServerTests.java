package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clientServer.ClientSideGame;
import clientServer.Slave;
import gameWorld.Sendable;
import ui.appwindow.MainWindow;
import gameWorld.characters.Character;

public class ClientServerTests {

	/**
	 * Requires a server to be running.
	 */
	@Test
	public void textMessage() {
		Slave slave = new Slave(null);
		slave.start();
		slave.login("Simon", "hunter2");
		slave.sendTextMessage("1");
		slave.sendTextMessage("2");
		slave.sendTextMessage("3");
		while (true) {}
	}

	@Test
	public void intToBytesToInt() {
		int num = 70000;
		byte[] bytes = Sendable.intToBytes(num);
		int back = Sendable.bytesToInt(bytes, 0);
		assertEquals(num, back);
	}
	
	@Test
	public void connect() {
		Slave slave = new Slave(new MainWindow());
		slave.start();
		slave.login("Simon", "hunter2");
		ClientSideGame game = null;
		while (game == null) {
			game = slave.getGame();
		}
		Character player = null;
		while (player == null) {
			player = game.getPlayer();
		}
		System.out.println(player);
	}

}
