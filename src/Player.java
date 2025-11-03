
public class Player { // Player object to hold the name of a player; extensibility provided to enable features like player's rating to be added
	private String name;
	private static String user=""; // Stores the username of the user. To be compared to when extracting games.



	public Player(String name) {
		this.name=name;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String username) {
		Player.user=username;
	}

	public String getName() {

		return name;
	}

}
