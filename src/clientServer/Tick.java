package clientServer;

public class Tick extends Thread {
	
	private static final int delay = 500;
	
	private final ServerSideGame game;

	public Tick(ServerSideGame game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		while(true) {
			// Loop forever			
			try {
				Thread.sleep(delay);
				this.game.tick();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
}
