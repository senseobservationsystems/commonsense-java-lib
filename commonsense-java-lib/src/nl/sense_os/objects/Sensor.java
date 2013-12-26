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
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public void setData_type_id(int data_type_id) {
		this.data_type_id = data_type_id;
	}

	public void setPager_type(String pager_type) {
		this.pager_type = pager_type;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public void setData_structure(String data_structure) {
		this.data_structure = data_structure;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public void setUse_data_storage(boolean use_data_storage) {
		this.use_data_storage = use_data_storage;
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
	
	/**
	 * This method will compare current object to the parameter
	 * @param other
	 * @param ignoreNull if true, it will skip property of current object that is null value
	 * @return
	 */
	public boolean equals(Sensor other, boolean ignoreNull) {
		if (id != -1) {
			if (id != other.id) return false;
		} else if (!ignoreNull && other.id != id) {
			return false;
		}
		
		if (name != null) {
			if (!name.equals(other.name)) return false;
		} else if (!ignoreNull) {
			if (other.name != null) return false;
		}
		
		if (type != -1) {
			if (type != other.type) return false;
		} else if (!ignoreNull) {
			if (type != other.type) return false;
		}
		
		if (device_type != null) {
			if (!device_type.equals(other.device_type)) return false;
		} else if (!ignoreNull) {
			if (other.device_type != null) return false;
		}
		
		if (data_type_id != -1) {
			if (data_type_id != other.data_type_id) return false;
		}  else if (!ignoreNull) {
			if (data_type_id != other.data_type_id) return false;
		}
		
		if (pager_type != null) {
			if (!pager_type.equals(other.pager_type)) return false;
		} else if (!ignoreNull) {
			if (other.pager_type != null) return false;
		}
		
		if (display_name != null) {
			if (!display_name.equals(other.display_name)) return false;
		} else if (!ignoreNull) {
			if (other.display_name != null) return false;
		}
	
		if (data_type != null) {
			if (!data_type.equals(other.data_type)) return false;
		} else if (!ignoreNull) {
			if (other.data_type != null) return false;
		}
	
		if (data_structure != null) {
			if (!data_structure.equals(other.data_structure)) return false;
		} else if (!ignoreNull) {
			if (other.data_structure != null) return false;
		}
		
		if (device != null) {
			if (!device.equals(other.device)) return false;
		} else if (!ignoreNull) {
			if (other.device != null) return false;
		}

		return true;
	}
}
