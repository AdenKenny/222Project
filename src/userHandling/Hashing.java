package userHandling;

import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.management.RuntimeErrorException;
import javax.xml.bind.DatatypeConverter;

//https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java

/**
 * A class to handle the hashing of a users password. Returns the hash and salt of the
 * password.
 *
 * @author Aden
 */

public class Hashing {

	/*These values can be increased for potentially more secure hashes and decreased
	 * performance or decreased for potentially less secure hashes and increased
	 * performance.
	*/

	private static final int SALT_BYTE_SIZE = 32;
	public static final int HASH_BYTE_SIZE = 32;
	public static final int PBKDF2_ITERATIONS = 64000;
	
	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	private Hashing() { //This should never be initialised.

	}

	/**
	 * Method that takes a char array of a user's password and creates hash and salt of
	 * the password. Uses pbfkf2 with 64000 iterations and salt and hash sizes of 32 bytes.
	 *
	 * @param password The users password.
	 * @return The hash.
	 */

	static String createHash(char[] password) {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt); //Create salt.

		byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

		password = null; //Let password get garbage collected.

		return toBase64(salt) + ":" + toBase64(hash); //Convert hash and salt to one string.
		//The colon separates the salt and hash for password checking.
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		}

		catch (Exception e) {
			throw new RuntimeErrorException(new Error("Hashing failed"));
		}
	}

	/**
	 * Slow equals rather than .equals(). .equals would stop comparing on the first wrong
	 * character whereas this method will keep comparing even if a character is wrong.
	 *
	 * @param a The hash from the password.
	 * @param b The correct hash for the user.
	 * @return Equality of hashes.
	 */

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length; //Make sure hashes are same length.
        for(int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i]; //XOR between bytes at all positions in hash.
        }

        return diff == 0; //Is there a difference in the hashes?
    }

    /**
     * Calls overloaded verify method with a char array rather than a string.
     *
     * @param password The entered password which will hashed and tested versus the correct hash.
     * @param correctHash The correct stored hash for the user account.
     * @return The equality of the two hashes.
     */

    public static boolean verifyPassword(String password, String correctHash) {
    	return verifyPassword(password.toCharArray(), correctHash); //Call overloaded method.
    }

    /**
     * Takes the entered password from the user and hashes it and compares to
     * the correct hash that is stored in the user's details.
     *
     * Splits password into salt and hash and then hashes the entered password
     * using the correct hashes salt and the length of the correct hash. The two
     * hashes are then tested for equality to check if the entered password is
     * correct.
     *
     * @param password The user entered password.
     * @param correctHash The correct hash for the user account.
     * @return The equality of the two hashes.
     */

    public static boolean verifyPassword(char[] password, String correctHash) {

        String[] params = correctHash.split(":"); //Split the hash and salt.

        byte[] salt = fromBase64(params[0]); //Convert to byte arrays.
        byte[] hash = fromBase64(params[1]);

        byte[] testHash = pbkdf2(password, salt, PBKDF2_ITERATIONS, hash.length); //Hash pw.

        return slowEquals(hash, testHash); //Return equality of the hashes.
    }

    /**
     * Returns a String from a byte array.
     *
     * @param array The array which will be converted.
     * @return The String value of the array.
     */

	private static String toBase64(byte[] array) {
		return DatatypeConverter.printBase64Binary(array);
	}

	/**
	 * Returns a byte array from a String representing a hex value.
	 *
	 * @param hex The hex value.
	 * @return The byte array.
	 */

	private static byte[] fromBase64(String hex) {
		return DatatypeConverter.parseBase64Binary(hex);
	}
}
