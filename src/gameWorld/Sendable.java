package gameWorld;

import java.nio.ByteBuffer;

public interface Sendable {

	public static enum Types {
		MONSTER(),
		VENDOR(),
		PLAYER();

		public byte value() {
			return (byte) ordinal();
		}
	}

	static byte[] intToBytes(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	static byte[] intsToBytes(int... is) {
		byte[] bytes = new byte[is.length*4];

		int count = 0;

		for (int i = 0; i < is.length; i++) {
			for (byte b : intToBytes(is[i])) {
				bytes[count++] = b;
			}
		}

		return bytes;
	}

	static int bytesToInt(byte[] bytes, int start) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = bytes[start + i];
		}
		return ByteBuffer.wrap(bs).getInt();
	}

	/**
	 * Returns a packet with all the information required
	 * for the client to be updated.
	 *
	 * @return
	 */
	public byte[] toSend();
}
