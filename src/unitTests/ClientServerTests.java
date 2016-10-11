package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clientServer.ClientSideGame;
import clientServer.Slave;
import gameWorld.Sendable;
import ui.appwindow.MainWindow;
import gameWorld.characters.Character;

public class ClientServerTests {

	@Test
	public void intToBytesToInt() {
		int num = 70000;
		byte[] bytes = Sendable.intToBytes(num);
		int back = Sendable.bytesToInt(bytes, 0);
		assertEquals(num, back);
	}

	@Test
	public void connect() {
		MainWindow main = new MainWindow();
		Slave slave = new Slave(main, null);
		main.setSlave(slave);
		slave.start();
		slave.login("Simon", "hunter2".toCharArray());
		ClientSideGame game = null;
		while (game == null) {
			game = slave.getGame();
		}
		Character player = null;
		while (player == null) {
			player = game.getPlayer();
			System.out.println("getting player");
		}
		//System.out.println(player);
	}

}
