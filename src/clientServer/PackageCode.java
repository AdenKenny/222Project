package clientServer;

/**
 * A class holding enums that are used for identifying packages and their contents.
 *
 * @author kennyaden - 300334300
 */

public final class PackageCode {

	private PackageCode() { //This shouldn't be initialised.
		throw new AssertionError();
	}

	public enum Codes {
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
		GAME_SENDABLE_END,
		GAME_SENDABLE,
		GAME_NEW_ROOM,
		KEY_PRESS_W,
		KEY_PRESS_A,
		KEY_PRESS_S,
		KEY_PRESS_D,
		KEY_PRESS_Q,
		KEY_PRESS_E,
		PERFORM_ACTION_ENTITY,
		PERFORM_ACTION_ITEM,
		GAME_WON;

		public byte value() {
			return (byte) ordinal();
		}
	}
}
