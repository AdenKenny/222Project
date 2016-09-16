package userHandling;

/**
 * A class dealing with a verifying a user's login details.
 * 
 * @author Aden
 */

public class Verification {

	private Verification() {
		
	}
	
	public static boolean login(String username, String password) { 
		
		if(Register.userExists(username)) {
			User user = Register.getUser(username);
			
			if(Hashing.verifyPassword(password, user.getHash())) { //Correct password.
				password = null;
				return true;
			}
			
			else { //Passwords do not match.
				//TODO Error message for wrong password.
			}
			
			password = null;
		}
		
		else { //No user registered with this username.
			System.out.println("This isn't a user");//TODO Error message to server.
		}
 		
		return false;
	}
	
	
}
