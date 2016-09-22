package clientServer;

public interface PackageCode {

	public enum Codes {
		BREAK((byte) 0),
		PING((byte) 1),
		PONG((byte) 2),
		LOGIN_ATTEMPT((byte) 3),
		LOGIN_RESULT((byte) 4),
		LOGIN_SUCCESS((byte) 5),
		LOGIN_INCORRECT_USER((byte) 6),
		LOGIN_INCORRECT_PASSWORD((byte) 7),
		LOGIN_ALREADY_CONNECTED((byte) 8),
		NEW_USER_ATTEMPT((byte) 9),
		NEW_USER_RESULT((byte) 10),
		NEW_USER_SUCCESS((byte) 11),
		NEW_USER_NAME_TAKEN((byte) 12),
		DISCONNECT((byte) 13);

		public final byte value;

		Codes(byte value) {
			this.value = value;
		}
	}
}
