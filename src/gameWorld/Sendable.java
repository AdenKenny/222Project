package gameWorld;

import java.nio.ByteBuffer;

public interface Sendable {

	public static enum Types {
		MONSTER(),
		PLAYER(),
		VENDOR(),
		;

		public byte value() {
			return (byte) ordinal();
		}
	}

	static byte[] intToByte(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	default byte[] intsToBytes(int... is) {
		byte[] bytes = new byte[is.length*4];

		int count = 0;

		for (int i = 0; i < is.length; i++) {
			for (byte b : intToByte(is[i])) {
				bytes[count++] = b;
			}
		}

		return bytes;
	}

	/**
	 * Returns a packet with all the information required
	 * to create an instance of this Object on the client-
	 * side.
	 *
	 * @return
	 */
	public byte[] onEntry();

	/**
	 * Returns a packet containing all the information
	 * required to update the Object on the client-
	 * side to the same status as the server.
	 *
	 * @return
	 */
	public byte[] roomUpdate();
}
