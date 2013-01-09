package nl.sense_os.objects;

import com.google.gson.JsonObject;

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
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		if (id != -1) {
			json.addProperty("id", id);
		}
		if (null != name) {
			json.addProperty("name", name);
		}
		if (null != description) {
			json.addProperty("description", description);
		}
		if (null != publicity) {
			json.addProperty("public", publicity);
		}
		return json;
	}

}
