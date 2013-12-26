package nl.sense_os.objects;

import java.util.Date;

import com.google.gson.JsonObject;

public class SensorData {

	private String id = null;
	private int sensor_id = -1;
	private Object value = null;
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
	public SensorData(Object value, double date) {
		this.value = value;
		this.date = date;
	}

	public SensorData(int sensor_id, Object value, Date date) {
		this.value = value;
		this.sensor_id = sensor_id;
		this.date = date.getTime() / 1000;
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
	public SensorData(String id, int sensor_id, String value, double date,
			int week, int month, int year) {
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

	public Object getValue() {
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

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * This method returns a JsonObject with the contents of this SensorData
	 * point.
	 * 
	 * @return
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("sensor_id", sensor_id);

		if (value instanceof Number) {
			json.addProperty("value", (Number) value);
		} else {
			json.addProperty("value", value.toString());
		}

		json.addProperty("date", date);
		json.addProperty("week", week);
		json.addProperty("month", month);
		json.addProperty("year", year);
		return json;
	}
}
