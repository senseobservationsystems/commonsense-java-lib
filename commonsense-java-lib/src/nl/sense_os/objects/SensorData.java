package nl.sense_os.objects;

import com.google.gson.JsonObject;

public class SensorData {

	private String id = null;
	private int sensor_id = -1;
	private String value = null;
	private double date = -1;
	private int week = -1;
	private int month = -1;
	private int year = -1;

	/**
	 * Constructor.
	 * 
	 * @param value
	 * @param date
	 */
	public SensorData(String value, double date) {
		this.value = value;
		this.date = date;
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param sensor_id
	 * @param value
	 * @param date
	 * @param week
	 * @param month
	 * @param year
	 */
	public SensorData(String id, int sensor_id, String value, double date, int week, int month, int year) {
		this.id = id;
		this.sensor_id = sensor_id;
		this.value = value;
		this.date = date;
		this.week = week;
		this.month = month;
		this.year = year;
	}

	public String getId() {
		return id;
	}

	public int getSensor_id() {
		return sensor_id;
	}

	public String getValue() {
		return value;
	}

	public double getDate() {
		return date;
	}

	public int getWeek() {
		return week;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	/**
	 * This method returns a JsonObject with the contents of this SensorData point.
	 * 
	 * @return
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("sensor_id", sensor_id);
		json.addProperty("value", value);
		json.addProperty("date", date);
		json.addProperty("week", week);
		json.addProperty("month", month);
		json.addProperty("year", year);
		return json;
	}
}
