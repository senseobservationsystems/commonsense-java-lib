package nl.sense_os.objects;

public class User {

	private int id = -1;
	private String email = null;
	private String username = null;
	private String name = null;
	private String surname = null;
	private String mobile = null;
	private String address = null;
	private String zipcode = null;
	private String country = null;
	private String UUID = null;
	private String openid = null;
	private int databaseUserId = -1;

	public User(int id, String email, String username, String name, String surname, String mobile, String address, String zipcode, String country, String uUID, String openid, int databaseUserId) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.mobile = mobile;
		this.address = address;
		this.zipcode = zipcode;
		this.country = country;
		UUID = uUID;
		this.openid = openid;
		this.databaseUserId = databaseUserId;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getMobile() {
		return mobile;
	}

	public String getAddress() {
		return address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getCountry() {
		return country;
	}

	public String getUUID() {
		return UUID;
	}

	public String getOpenid() {
		return openid;
	}

	public int getDatabaseUserId() {
		return databaseUserId;
	}

}
