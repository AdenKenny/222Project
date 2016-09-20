package clientServer;

public abstract interface PackageCode {
	
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
	

	
	/*public static final byte BREAK = 0;
	public static final byte LOGIN_ATTEMPT = 1;
	public static final byte LOGIN_RESULT = 2;
	public static final byte LOGIN_SUCCESS = 3;
	public static final byte LOGIN_INCORRECT_USER = 4;
	public static final byte LOGIN_INCORRECT_PASSWORD = 5;
	public static final byte LOGIN_ALREADY_CONNECTED = 6;
	public static final byte NEW_USER_ATTEMPT = 7;
	public static final byte NEW_USER_RESULT = 8;
	public static final byte NEW_USER_SUCCESS = 9;
	public static final byte NEW_USER_NAME_TAKEN = 10;*/
}
