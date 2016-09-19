package dataStorage;

import java.util.Set;

import clientServer.Game;
import userHandling.User;

public class GetData {

	public GetData(Game game) {
		Set<User> users = game.getUsers();
		
		for(User user : users) {
			System.out.println(user.getUsername());
		}
	}
	
}
