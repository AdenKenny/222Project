package gameWorld;

import java.nio.ByteBuffer;

/**
 * An interface which allows Objects to be translated into byte arrays which can
 * be sent along a network, then parsed back from the bytes.
 * 
 * @author Simon
 */
public interface Sendable {

	/**
	 * An enumeration which represents the different types of Characters which
	 * may be sent across the network.
	 * 
	 * @author Simon & Louis
	 */
	public static enum Types {
		MONSTER, VENDOR, PLAYER;

		public byte value() {
			return (byte) ordinal();
		}
	}

	/**
	 * Translates an integer value into a byte array.
	 * 
	 * @param i
	 *            The integer to translate
	 * @return The translated byte array
	 */
	static byte[] intToBytes(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	/**
	 * Translates an arbitrary number of integer values into a byte array.
	 * 
	 * @param is
	 *            The integers to translate
	 * @return The translated byte array
	 */
	static byte[] intsToBytes(int... is) {
		byte[] bytes = new byte[is.length * 4];

		int count = 0;

		for (int i = 0; i < is.length; i++) {
			for (byte b : intToBytes(is[i])) {
				bytes[count++] = b;
			}
		}

		return bytes;
	}

	/**
	 * Parses a section of an array of bytes into an integer value.
	 * 
	 * @param bytes
	 *            The array to parse
	 * @param start
	 *            The starting point where to parse from
	 * @return The parsed integer value
	 */
	static int bytesToInt(byte[] bytes, int start) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = bytes[start + i];
		}
		return ByteBuffer.wrap(bs).getInt();
	}

	/**
	 * Returns a packet with all the information required for the client to be
	 * updated.
	 *
	 * @return A byte packet.
	 */
	public byte[] toSend();
}
