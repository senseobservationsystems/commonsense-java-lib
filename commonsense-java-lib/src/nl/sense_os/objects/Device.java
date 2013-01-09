package nl.sense_os.objects;

public class Device {

	private int id = -1;
	private String type = null;
	private String uuid = null;

	public Device(int id, String type, String uuid) {
		this.id = id;
		this.type = type;
		this.uuid = uuid;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getUuid() {
		return uuid;
	}

}
