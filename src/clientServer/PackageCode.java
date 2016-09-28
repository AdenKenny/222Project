package clientServer;

public final class PackageCode {

	private PackageCode() {
		throw new AssertionError();
	}

	public static enum Codes {
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
		DISCONNECT((byte) 13),
		TEXT_MESSAGE((byte) 14),
		GAME_ROOM_UPDATE((byte) 15),
		GAME_ROOM_ENTRY((byte) 16),
		GAME_POSITION_UPDATE((byte) 17),
		USER_INPUT((byte) 18);

		public final byte value;

		Codes(byte value) {
			this.value = value;
		}
	}
}
