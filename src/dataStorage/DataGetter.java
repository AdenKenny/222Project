package dataStorage;

import java.util.Set;

import clientServer.Game;
import userHandling.User;

public final class DataGetter {

	static Set<User> users;

	public DataGetter(Game game) {
		DataGetter.users = game.getAllUsers();
	}

	protected static Set<User> getUsers() {
		return users;
	}

}
