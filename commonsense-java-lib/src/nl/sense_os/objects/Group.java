package nl.sense_os.objects;

import org.json.simple.JSONObject;

public class Group {

	private int id = -1;
	private String name = null;
	private String description = null;
	private String publicity = null;

	public Group(String name, String description, String publicity) {
		this.name = name;
		this.description = description;
		this.publicity = publicity;
	}
	
	public Group(int id, String name, String description, String publicity) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.publicity = publicity;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPublicity() {
		return publicity;
	}

	/**
	 * This method returns a JsonObject with the contents of this Group object. Please note that only the values that are not null are added to the JsonObject.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		if (id != -1) {
			json.put("id", id);
		}
		if (null != name) {
			json.put("name", name);
		}
		if (null != description) {
			json.put("description", description);
		}
		if (null != publicity) {
			json.put("public", publicity);
		}
		return json;
	}

}
