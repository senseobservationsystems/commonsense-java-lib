package nl.sense_os.objects;

public class Credentials {

	private String username;
	private String password;
	private String session_id;
	private long date_created;

	public Credentials(String username, String password, String session_id, long date_created) {
		this.username = username;
		this.password = password;
		this.session_id = session_id;
		this.date_created = date_created;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSession_id() {
		return session_id;
	}

	public long getDate_created() {
		return date_created;
	}
}
