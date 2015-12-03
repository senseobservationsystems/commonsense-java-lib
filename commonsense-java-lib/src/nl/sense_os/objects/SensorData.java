package nl.sense_os.objects;

import java.util.Date;

import org.json.simple.JSONObject;

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
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("sensor_id", sensor_id);

		if (value instanceof Number) {
			json.put("value", (Number) value);
		} else {
			json.put("value", value.toString());
		}

		json.put("date", date);
		json.put("week", week);
		json.put("month", month);
		json.put("year", year);
		return json;
	}
}
