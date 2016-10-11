package clientServer;

public class Tick extends Thread {

	private static final int delay = 500;

	private final Game game;

	public Tick(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		while(true) {
			// Loop forever
			try {
				long start = System.currentTimeMillis();
				this.game.tick();
				long end = System.currentTimeMillis();
				Thread.sleep(delay - (int)(end - start));
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
