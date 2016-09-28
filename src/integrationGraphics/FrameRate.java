package integrationGraphics;

import clientServer.Game;

public class FrameRate extends Thread {

	private static final int delay = 100;

	private final GraphicsPanel gfx;

	public FrameRate(GraphicsPanel gfx) {
		this.gfx = gfx;
	}

	@Override
	public void run() {
		while(true) {
			// Loop forever
			try {
				this.gfx.repaint();
				Thread.sleep(delay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
