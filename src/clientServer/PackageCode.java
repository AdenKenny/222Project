package clientServer;

public interface PackageCode {

	public enum Codes {
		BREAK((byte) 0),
		LOGIN_ATTEMPT((byte) 1),
		LOGIN_RESULT((byte) 2),
		LOGIN_SUCCESS((byte) 3),
		LOGIN_INCORRECT_USER((byte) 4),
		LOGIN_INCORRECT_PASSWORD((byte) 5),
		LOGIN_ALREADY_CONNECTED((byte) 6),
		NEW_USER_ATTEMPT((byte) 7),
		NEW_USER_RESULT((byte) 8),
		NEW_USER_SUCCESS((byte) 9),
		NEW_USER_NAME_TAKEN((byte) 10);

		public final byte value;

		Codes(byte value) {
			this.value = value;
		}
	}
}
