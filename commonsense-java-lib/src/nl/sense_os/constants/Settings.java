package nl.sense_os.constants;

public class Settings {

	// credentials
	private static final String username = "";
	private static final String pass = "";
	private static String session_id = "";

	public static String getUsername() {
		return username;
	}

	public static String getPass() {
		return pass;
	}

	public static String getSession_id() {
		return session_id;
	}

	public static void setSession_id(String session_id) {
		Settings.session_id = session_id;
	}

	// private constructor to prevent instantiation
	private Settings() {
	}

}