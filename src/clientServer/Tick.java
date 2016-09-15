package clientServer;

public class Tick extends Thread {
	
	private static final int delay = 500;
	
	private final Game game;

	public Tick(Game game) {
		this.game = game;
	}
	
	public void run() {
		while(1 == 1) {
			// Loop forever			
			try {
				Thread.sleep(delay);
				game.tick();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
}
