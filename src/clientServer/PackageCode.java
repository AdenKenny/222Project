package clientServer;

public final class PackageCode {

	private PackageCode() {
		throw new AssertionError();
	}

	public static enum Codes {
		BREAK,
		PING,
		PONG,
		LOGIN_ATTEMPT,
		LOGIN_RESULT,
		LOGIN_SUCCESS,
		LOGIN_INCORRECT_USER,
		LOGIN_INCORRECT_PASSWORD,
		LOGIN_ALREADY_CONNECTED,
		NEW_USER_ATTEMPT,
		NEW_USER_RESULT,
		NEW_USER_SUCCESS,
		NEW_USER_NAME_TAKEN,
		DISCONNECT,
		TEXT_MESSAGE,
		GAME_ROOM_UPDATE,
		GAME_ROOM_ENTRY,
		GAME_POSITION_UPDATE,
		USER_INPUT;

		public byte value() {
			return (byte) ordinal();
		}
	}
}
