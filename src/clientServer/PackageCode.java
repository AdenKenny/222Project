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
		GAME_SENDABLE_UPDATE,
		GAME_SENDABLE_CREATE,
		GAME_SENDABLE_REQUEST,
		GAME_NEW_ROOM,
		KEY_PRESS_W,
		KEY_PRESS_A,
		KEY_PRESS_S,
		KEY_PRESS_D,
		KEY_PRESS_Q,
		KEY_PRESS_E;

		public byte value() {
			return (byte) ordinal();
		}
	}
}
