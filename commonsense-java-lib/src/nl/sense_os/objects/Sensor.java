package nl.sense_os.objects;

import com.google.gson.JsonObject;

public class Sensor {

	private int id = -1;
	private String name = null;
	private int type = -1;
	private String device_type = null;
	private int data_type_id = -1;
	private String pager_type = null;
	private String display_name = null;
	private String data_type = null;
	private String data_structure = null;
	private Device device = null;
	private boolean use_data_storage = false;

	/**
	 * Constructor. This constructor can be used to create a new sensor at CommonSense. These are the minimum fields required to create a new sensor in CommonSense.
	 * 
	 * @param name
	 * @param device_type
	 * @param display_name
	 * @param data_type
	 * @param data_structure
	 */
	public Sensor(String name, String device_type, String display_name, String data_type, String data_structure) {
		this.name = name;
		this.device_type = device_type;
		this.display_name = display_name;
		this.data_type = data_type;
		this.data_structure = data_structure;
	}

	/**
	 * Constructor. This constructor is used by the API to read sensor data from CommonSense.
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param device_type
	 * @param data_type_id
	 * @param pager_type
	 * @param display_name
	 * @param data_type
	 * @param data_structure
	 * @param device
	 * @param use_data_storage
	 */
	public Sensor(int id, String name, int type, String device_type, int data_type_id, String pager_type, String display_name, String data_type, String data_structure, Device device,
			boolean use_data_storage) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.device_type = device_type;
		this.data_type_id = data_type_id;
		this.pager_type = pager_type;
		this.display_name = display_name;
		this.data_type = data_type;
		this.data_structure = data_structure;
		this.device = device;
		this.use_data_storage = use_data_storage;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public String getDevice_type() {
		return device_type;
	}

	public int getData_type_id() {
		return data_type_id;
	}

	public String getPager_type() {
		return pager_type;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public String getData_type() {
		return data_type;
	}

	public String getData_structure() {
		return data_structure;
	}

	public Device getDevice() {
		return device;
	}

	public boolean isUse_data_storage() {
		return use_data_storage;
	}

	/**
	 * This method returns a JsonObject with the description of this Sensor. Please note that only the values which are not null are added to the JsonObject.
	 * 
	 * @return
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		JsonObject sensorProperties = new JsonObject();

		if (id != -1) {
			sensorProperties.addProperty("id", id);
		}
		if (name != null) {
			sensorProperties.addProperty("name", name);
		}
		if (type != -1) {
			sensorProperties.addProperty("type", type);
		}
		if (device_type != null) {
			sensorProperties.addProperty("device_type", device_type);
		}
		if (data_type_id != -1) {
			sensorProperties.addProperty("data_type_id", data_type_id);
		}
		if (pager_type != null) {
			sensorProperties.addProperty("pager_type", pager_type);
		}
		if (display_name != null) {
			sensorProperties.addProperty("display_name", display_name);
		}
		if (data_type != null) {
			sensorProperties.addProperty("data_type", data_type);
		}
		if (data_structure != null) {
			sensorProperties.addProperty("data_structure", data_structure);
		}
		if (device != null) {
			sensorProperties.addProperty("device", device.toString());
		}
		if (use_data_storage) {
			sensorProperties.addProperty("use_data_storage", 1);
		}

		json.add("sensor", sensorProperties);
		return json;
	}
}
