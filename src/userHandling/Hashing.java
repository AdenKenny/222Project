package userHandling;

import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.management.RuntimeErrorException;
import javax.xml.bind.DatatypeConverter;

public class Hashing {

	private static final int SALT_BYTE_SIZE = 32;
	public static final int HASH_BYTE_SIZE = 32;
	public static final int PBKDF2_ITERATIONS = 64000;

	private Hashing() {

	}

	static String createHash(char[] password) {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

		byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);

		password = null;

		return toBase64(salt) + ":" + toBase64(hash);
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		}

		catch (Exception e) {
			throw new RuntimeErrorException(new Error("Hashing failed"));
		}

	}

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }

        return diff == 0;
    }

    public static boolean verifyPassword(String password, String correctHash) {
    	return verifyPassword(password.toCharArray(), correctHash);
    }

    public static boolean verifyPassword(char[] password, String correctHash) {

        String[] params = correctHash.split(":");

        byte[] salt = fromBase64(params[0]);
        byte[] hash = fromBase64(params[1]);

        byte[] testHash = pbkdf2(password, salt, PBKDF2_ITERATIONS, hash.length);

        return slowEquals(hash, testHash);
    }

	private static String toBase64(byte[] array) {
		return DatatypeConverter.printBase64Binary(array);
	}

	private static byte[] fromBase64(String hex) throws IllegalArgumentException {
		return DatatypeConverter.parseBase64Binary(hex);
	}
}
