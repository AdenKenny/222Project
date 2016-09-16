package clientServer;

import java.util.HashMap;
import java.util.Map;

public class Game {

	private final Map<Long, Long> connectedCharacters;

	public Game() {
		connectedCharacters = new HashMap();
	}

	public synchronized void tick() {

	}

	public void registerConnection(long uid, long cid) {
		connectedCharacters.put(uid, cid);
	}

	public byte[] toByteArray(long uid) {
		//get the character of the user
		long cid = connectedCharacters.get(uid);
		//TODO placeholder
		byte[] data = new byte[0];
		return data;
	}
}
