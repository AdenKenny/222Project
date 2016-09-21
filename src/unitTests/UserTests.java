package unitTests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import userHandling.Register;
import userHandling.User;
import userHandling.Verification;

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
	 * Creates a mock user in a new database We will need to use reflection to modify
	 * private fields in the register class as we do not want to user our actual database.
	 *
	 * @return The mock user created in our new mock database.
	 */

	private User createUser() {

		try {
			Class<?> regClass = Class.forName("userHandling.Register");

			Field field;
			try {
				field = regClass.getDeclaredField("DB_FILE");

				field.setAccessible(true); // Make private field accessible.

				Field modifiers = field.getClass().getDeclaredField("modifiers"); // Get
																					// modifiers.
				modifiers.setAccessible(true);
				modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL); // Not
																					// final
																					// field.

				field.set(regClass, this.file);

				User user = Register.createUser("Paul", "hunter2");

				return user;
			} catch (NoSuchFieldException | SecurityException e) {
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
