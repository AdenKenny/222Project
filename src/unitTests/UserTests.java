package unitTests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;

import org.junit.Test;

import userHandling.Hashing;
import userHandling.Register;
import userHandling.User;
import userHandling.Verification;

/**
 * Tests for the userHandling package i.e. the database and user registration.
 *
 * @author Aden
 */

public class UserTests {

	File file = new File("tests/testDB.txt"); // Mock DB file.

	/**
	 * Test to create a mock user.
	 */

	@Test
	public void createMockUser() {

		User user = createUser();

		if (Register.userExists(user.getUsername())) {
			deleteFile(); // We can delete mock db.
		}

		else {
			System.out.println("createMockUser failed: The user was not found in mock db.");
			fail(); // The user was not found in the database.
		}

	}

	/**
	 * A test to see if a user with the correct password can login.
	 */

	@Test
	public void testVerification() {
		String mockUsername = "Paul";
		String mockPassword = "hunter2";

		User user = createUser();

		if (Verification.login(mockUsername, mockPassword) != null) {
			deleteFile();
		}

		else {
			System.out.println("testVerification failed: login failed.");
			deleteFile();
			fail();
		}

	}

	/**
	 * A test to see if a user with the correct password can login.
	 */

	@Test
	public void testWrongVerification() {
		String mockUsername = "Paul";
		String mockPassword = "dsadsadsa"; // Wrong password.

		User user = createUser();

		if (Verification.login(mockUsername, mockPassword) != null) {
			System.out.println("testVerification failed: user logged in with wrong password.");
			deleteFile();
			fail();
		}

		else {
			deleteFile();
		}
	}

	/**
	 * Tests to make sure Hashing.verifyPassword works correctly. The generated
	 * hash and the next generated hash should be equal.
	 */

	@Test
	public void testHashing() {

		String hash = Hashing.createHash("abcDef12#".toCharArray());

		if (!(Hashing.verifyPassword("abcDef12#", hash))) {
			System.out.println("testHashing failed: Passwords were not equal.");
			fail(); // Passwords were not equal?
		}
	}

	/**
	 * Tests to make sure a user cannot register with an already taken name.
	 */

	@Test
	public void testNoReRegister() {
		User user = createUser();
		user = createUser();

		if(user != null) { //User should be null as failed login returns null.
			deleteFile();
			fail();
		}

		deleteFile(); //User was null, test passed.
	}

	/**
	 * Tests to make sure the slow equals method in Hashing works correctly.
	 */

	@Test
	public void testSlowEquals() {
		try {
			Class<?> hashClass = Class.forName("userHandling.Hashing");
			Method[] methods = hashClass.getDeclaredMethods();

			String testPassword = "NortheastSouthwest!";

			for (Method method : methods) {
				if (method.getName().equals("slowEquals")) {
					String hashStr1 = Hashing.createHash(testPassword.toCharArray());

					String[] params1 = hashStr1.split(":");

					byte[] salt1 = null;
					byte[] hash1 = null;

					for (Method from64 : methods) {

						if (from64.getName().equals("fromBase64")) {
							from64.setAccessible(true);

							salt1 = (byte[]) from64.invoke(hashClass, params1[3]);
						}
					}

					byte[] byteHash1 = null;

					for (Method pbkdf2 : methods) {
						if (pbkdf2.getName().equals("pbkdf2")) {
							pbkdf2.setAccessible(true);

							byteHash1 = (byte[]) pbkdf2.invoke(hashClass, testPassword.toCharArray(), salt1, 64000, 32);
						}
					}

					method.setAccessible(true);

					if ((boolean) method.invoke(hashClass, byteHash1, byteHash1)){

					}

					else {
						System.out.println("testSlowEquals failed: hashes were not equal.");
						fail();
					}
				}
			}

		}

		catch (ClassNotFoundException e) {
			fail(); // Class was not found.
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a mock user in a new database We will need to use reflection to
	 * modify private fields in the register class as we do not want to user our
	 * actual database.
	 *
	 * @return The mock user created in our new mock database.
	 */

	private User createUser() {

		try {
			Class<?> regClass = Class.forName("userHandling.Register");

			try {
				Field field = regClass.getDeclaredField("DB_FILE");

				field.setAccessible(true); // Make private field accessible.

				Field modifiers = field.getClass().getDeclaredField("modifiers");
				modifiers.setAccessible(true);
				modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				field.set(regClass, this.file);

				User user = Register.createUser("Paul", "hunter2");

				return user;

			}

			catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}

			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void deleteFile() {
		try {
			Files.delete(this.file.toPath());
		}

		catch (IOException e) {
			e.printStackTrace(); // Don't really care. Test still passed.
		}
	}
}
