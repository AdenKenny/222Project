package dataStorage;

import java.util.Set;

import clientServer.ServerSideGame;
import userHandling.User;

public final class DataGetter {

	static Set<User> users;

	public DataGetter(ServerSideGame game) {
		DataGetter.users = game.getAllUsers();
	}

	protected static Set<User> getUsers() {
		return users;
	}

}
