package nl.sense_os.api;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.sense_os.constants.SenseUrls;
import nl.sense_os.constants.Settings;
import nl.sense_os.objects.Credentials;
import nl.sense_os.objects.Device;
import nl.sense_os.objects.Group;
import nl.sense_os.objects.Params;
import nl.sense_os.objects.Sensor;
import nl.sense_os.objects.SensorData;
import nl.sense_os.objects.User;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class Api {
	
	/**
	 * This method returns an ArrayList of devices to which the current user has
	 * access.
	 * 
	 * @access public
	 * @return ArrayList of devices
	 * @throws IOException
	 */
	public static ArrayList<Device> listDevices() {
		final String urlString = Params.General.devMode ? SenseUrls.DEV_DEVICES
				: SenseUrls.DEVICES;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, (JSONObject) null,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of sensors
		ArrayList<Device> devices = new ArrayList<Device>(); // ArrayList of
																// devices

		String content = response.get("content"); // String format of devices

		JSONObject json = (JSONObject) JSONValue.parse(content); // JSON container
																// of devices

		int numSensors = ((JSONArray)json.get("devices")).size();

		for (int i = 0; i < numSensors; i++) {
			JSONObject device = (JSONObject) ((JSONArray)json.get("devices")).get(i);
			Device d = new Device(Integer.parseInt(((String)device.get("id"))), (String) device.get("type"), (String) device.get("uuid"));
			devices.add(d);
		}

		return devices;
	}

	/**
	 * This method returns a list of sensors to which the current user has
	 * access.
	 * 
	 * @access public
	 * @return sensor array
	 * @throws IOException
	 */
	public static ArrayList<Sensor> listAllSensors() {
		final String urlStringBase = Params.General.devMode ? SenseUrls.DEV_ALL_SENSORS
				: SenseUrls.ALL_SENSORS;

		// create ArrayList of sensors
		ArrayList<Sensor> sensors = new ArrayList<Sensor>(); // ArrayList of
																// sensors

		int page = 0;
		int numSensors;

		do {
			numSensors = 0;
			String urlString = urlStringBase + "&page=" + page;

			// print request
			if (Params.General.verbosity) {
				printRequest(urlString);
			}

			Map<String, String> response = new HashMap<String, String>();

			try {
				response = Connector.request(urlString, (JSONObject) null,
						Settings.getSession_id());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// print response
			if (Params.General.verbosity) {
				printResponse(response);
			}

			String content = response.get("content"); // String format of sensors
			JSONObject json = (JSONObject) JSONValue.parse(content); // JSON container
																	// of sensors

			numSensors = ((JSONArray)json.get("sensors")).size();

			for (int i = 0; i < numSensors; i++) {
				JSONObject element = (JSONObject) ((JSONArray)json.get("sensors")).get(i);
				Sensor s = new Sensor((String) element.get("name"),
										(String) element.get("device_type"),
										(String) element.get("display_name"),
										(String) element.get("data_type"),
										(String) element.get("data_structure"));
				s.setId(Integer.parseInt((String) element.get("id")));
				sensors.add(s);
			}

			page += 1;
		} while (numSensors >= 1000);

		return sensors;
	}

	/**
	 * This method returns a list of sensors to which the current user has
	 * access. Parameters for this function can be changed in Params.Sensors.
	 * 
	 * @access public
	 * @return sensor array
	 * @throws IOException
	 */
	public static ArrayList<Sensor> listSensors() {
		String urlString = Params.General.devMode ? SenseUrls.DEV_BASE
				: SenseUrls.BASE;

		// parse parameters
		urlString += "sensors" + SenseUrls.FORMAT;
		urlString += "?page=" + Params.Sensors.page;
		urlString += "&per_page=" + Params.Sensors.per_page;
		urlString += "&shared=" + (Params.Sensors.shared ? "1" : "0");
		urlString += "&owned=" + (Params.Sensors.owned ? "1" : "0");
		urlString += "&physical=" + (Params.Sensors.physical ? "1" : "0");
		urlString += "&details=" + (Params.Sensors.details ? "full" : "no");

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, (JSONObject) null,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of sensors
		ArrayList<Sensor> sensors = new ArrayList<Sensor>(); // ArrayList of
																// sensors
		String content = response.get("content"); // String format of sensors
		JSONObject json = (JSONObject) JSONValue.parse(content); // JSON container
																// of sensors

		int numSensors = ((JSONArray)json.get("sensors")).size();

		for (int i = 0; i < numSensors; i++) {
			JSONObject element = (JSONObject) ((JSONArray)json.get("sensors")).get(i);
			Sensor s = new Sensor((String) element.get("name"),
					(String) element.get("device_type"),
					(String) element.get("display_name"),
					(String) element.get("data_type"),
					(String) element.get("data_structure"));
			sensors.add(s);
		}

		return sensors;
	}

	/**
	 * This method returns a list of sensor data constrained by the parameters
	 * below
	 * 
	 * @param sensorId
	 *            Unique id of the sensor (use for instance
	 *            <code>listAllSensors()</code> to fetch sensor id).
	 * @param page
	 *            This parameter specifies which page of the results must be
	 *            retrieved. The page offset starts at 0.
	 * @param per_page
	 *            This parameter specifies the amount of items that must be
	 *            received at once. The maximum amount is 1000 items and the
	 *            default amount is 100 items.
	 * @param start_date
	 *            The start date in UNIX time format (double seconds). Used as
	 *            date to start the item search from.
	 * @param end_date
	 *            The end date in UNIX time format (double seconds). The date
	 *            until which the items will be searched.
	 * @return An ArrayList of sensor data: <code>ArrayList&lt;Data&gt;</code>.
	 */
	public static ArrayList<SensorData> getSensorData(int sensorId) {
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA
				: SenseUrls.SENSOR_DATA;

		// parse parameters
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));
		urlString += "?page=" + String.valueOf(Params.Data.page);
		urlString += "&per_page=" + String.valueOf(Params.Data.per_page);
		urlString += "&start_date=" + String.valueOf(Params.Data.start_date);
		urlString += "&end_date=" + String.valueOf(Params.Data.end_date);
		urlString = Params.Data.date < 0 ? urlString : urlString + "&date="
				+ Params.Data.date;
		urlString = Params.Data.last ? urlString + "&last=1" : urlString;
		urlString += "&sort=" + Params.Data.sort;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, (JSONObject) null,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		if (null != response) {

			// create ArrayList of sensors
			ArrayList<SensorData> data = new ArrayList<SensorData>(); // ArrayList
																		// of
																		// sensors
			String content = response.get("content"); // String format of
														// sensors
			// System.out.println(content);
			JSONObject json = (JSONObject) JSONValue.parse(content); // JSON
																	// container
																	// of
																	// sensors
			// System.out.println(json);
			int numSensors = ((JSONArray)json.get("data")).size();

			for (int i = 0; i < numSensors; i++) {
				JSONObject element = (JSONObject) ((JSONArray)json.get("data")).get(i);
				System.out.println("element" + element);
				String id = (String) element.get("id");
				Integer sensor_id = ((Long) element.get("sensor_id")).intValue();
				String value = (String) element.get("value");
				Double datadata = (Double) element.get("date");
				Integer week = 0;
				if(element.get("week") != null)
					week = ((Long) element.get("week")).intValue();
				Integer month = ((Long) element.get("month")).intValue();
				Integer year = ((Long) element.get("year")).intValue();
				
				SensorData d = new SensorData(id, sensor_id, value, datadata, week, month, year);
//				SensorData d = new SensorData((String) element.get("id"),
//							  ((Long) element.get("sensor_id")).intValue(),
//							  (String) element.get("value"),
//							  (Double) element.get("date"),
//							  ((Long) element.get("week",)).intValue(),
//							  ((Long) element.get("month")).intValue(),
//							  ((Long) element.get("year")).intValue());
				// System.out.println("data" + d);
				data.add(d);
			}

			return data;
		} else {
			System.err.println("Failed to fetch CommonSense data");
			System.exit(1);
		}
		return null;

	}

	/**
	 * This method returns a list of groups the current user is a member of
	 * 
	 * @return list of groups
	 */
	public static ArrayList<Group> listGroups() {
		String url = SenseUrls.GROUPS;

		url += "?page=" + Params.Groups.page;
		url += "&per_page=" + Params.Groups.per_page;
		url += "&total=" + (Params.Groups.total ? "1" : "0");
		url += "&public=" + Params.Groups.publicity;

		// print request
		if (Params.General.verbosity) {
			printRequest(url);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(url, (JSONObject) null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of groups
		ArrayList<Group> groupList = new ArrayList<Group>(); // ArrayList of
																// groups
		String groups = response.get("content"); // String format of groups
		JSONObject json = (JSONObject) JSONValue.parse(groups); // JSON container
																// of groups
                int numGroups = ((JSONArray)json.get("groups")).size();

		for (int i = 0; i < numGroups; i++) {
			JSONObject element = (JSONObject) ((JSONArray)json.get("groups")).get(i);
			Group g = new Group(Integer.parseInt(((String)element.get("id"))),
											(String) element.get("name"),
											(String) element.get("description"),
											(String) element.get("publicity"));

			groupList.add(g);
		}

		return groupList;
	}

	/**
	 * This methods returns the members of the group as a list of users. Only
	 * group members can perform this action.
	 * 
	 * @param int groupId
	 * @return User Array
	 */
	public static ArrayList<User> listUsersOfGroup(int groupId) {
		// TODO
		return null;
	}

	/**
	 * This method list the sensors which are connected to this environment.
	 * 
	 * @param int id
	 * @return mixed
	 */
	public static ArrayList<Sensor> listEnvironmentSensors(int environmentId) {
		// TODO
		return null;
	}

	/**
	 * This method returns the details of the device to witch the sensor is
	 * connected.
	 * 
	 * @param int sensorId
	 * @return mixed
	 */
	public static Device readParentDevice(int sensorId) {

		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_BASE
				: SenseUrls.BASE;
		urlString += "sensors/" + sensorId + "/device" + SenseUrls.FORMAT;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, (JSONObject) null,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		String content = response.get("content");
		JSONObject json = (JSONObject) JSONValue.parse(content);
		if (null != json.get("error")) {
			System.out.println(json.get("error"));
			return null;
		}

		return new Device(Integer.parseInt(((String)json.get("id"))), (String) json.get("type"), (String) json.get("uuid"));
	}

	/**
	 * This method will list the users that have access to the sensor.
	 * 
	 * @param int sensorId
	 * @return
	 */
	public static ArrayList<User> sharedUsers(int sensorId) {
		// TODO
		return null;
	}

	/**
	 * This method will create a new sensor at CommonSense.
	 */
	public static boolean createSensor(Sensor sensor, boolean checkForDuplicates) {
		final JSONObject json = sensor.toJson();

		// See if sensor with same name already exists
		if (checkForDuplicates) {
			boolean alreadyExists = false;
			int id = 0;

			ArrayList<Sensor> sensors = Api.listAllSensors();
			for (int n = 0; n < sensors.size(); n++) {
				String name = sensors.get(n).getName();
				if (name.contains(sensor.getName())) {
					alreadyExists = true;
					id = sensors.get(n).getId();
				}
			}

			if (alreadyExists) {
				System.out.println("Sensor already exists with id: " + id);
				return false;
			}
		}

		// request URL
		final String urlString = Params.General.devMode ? SenseUrls.DEV_CREATE_SENSOR
				: SenseUrls.CREATE_SENSOR;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, json,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean created = response.get("http response code").equals("201") ? true
				: false;

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
			System.out.println(created ? "New '" + sensor.getName()
					+ "' sensor created." : "Failed creating new '"
					+ sensor.getName() + "' sensor.");
		}

		// TODO: change reading through location header
		// update sensor id
		String content = response.get("content");
		JSONObject jobject = (JSONObject) JSONValue.parse(content);
		JSONObject sensorObject = (JSONObject) jobject.get("sensor");
		sensor.setId(Integer.parseInt((String) sensorObject.get("id")));

		// return response.get("http response code");
		return created;
	}

	public static Sensor findOrCreateSensor(Sensor sensor) {
		if (sensor.getId() > 0) {
			return sensor;
		}

		// check if sensor exists
		ArrayList<Sensor> sensors = Api.listAllSensors();
		for (int n = 0; n < sensors.size(); n++) {
			if (sensor.equals(sensors.get(n), true)) {
				return sensors.get(n);
			}
		}

		// not exists create one
		createSensor(sensor, false);

		return sensor;
	}

	/**
	 * This method will delete the sensor with id <code>sensorId</code> from
	 * CommonSense.
	 * 
	 * @param sensorId
	 */
	public static boolean deleteSensor(int sensorId) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_DELETE_SENSOR
				: SenseUrls.DELETE_SENSOR;
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.requestDeletion(urlString,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		return response.get("http response code").equals("200") ? true : false;
	}

	/**
	 * This method will call a POST request to post the sensor data
	 * <code>data</code> to the sensor <code>sensorId</code> at CommonSense.
	 * 
	 * @param sensorId
	 *            sensorId that will be used to POST the data to.
	 * @param data
	 *            sensor data to send to CommonSense.
	 * @return true if the data was successfully posted to CommonSense, false
	 *         otherwise.
	 */
	public static boolean postSensorData(int sensorId, JSONObject json) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA
				: SenseUrls.SENSOR_DATA;
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, json,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		return response.get("http response code").equals("201") ? true : false;
		// return response.get("http response code");
	}

	public static boolean postSensorData(SensorData point) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA
				: SenseUrls.SENSOR_DATA;
		urlString = urlString.replaceAll("\\<id\\>",
				String.valueOf(point.getSensor_id()));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		// build JSON string
		JSONObject pointJson = new JSONObject();
		pointJson.put("date", point.getDate());

		if (point.getValue() instanceof Number) {
			pointJson.put("value", (Number) point.getValue());
		} else {
			pointJson.put("value", point.getValue().toString());
		}

		JSONArray dataArray = new JSONArray();
		dataArray.add(pointJson);

		JSONObject body = new JSONObject();
		body.put("data", dataArray);

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, body,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		return response.get("http response code").equals("201") ? true : false;
		// return response.get("http response code");
	}

	public static boolean postSensorMultiData(int sensorId,
			List<SensorData> points) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA
				: SenseUrls.SENSOR_DATA;
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		// build JSON string
		JSONArray dataArray = new JSONArray();

		for (SensorData point : points) {
			JSONObject pointJson = new JSONObject();
			pointJson.put("date", point.getDate());

			if (point.getValue() instanceof Number) {
				pointJson.put("value", (Number) point.getValue());
			} else {
				pointJson.put("value", point.getValue().toString());
			}

			dataArray.add(pointJson);
		}

		JSONObject body = new JSONObject();
		body.put("data", dataArray);

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, body,
					Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		return response.get("http response code").equals("201") ? true : false;
		// return response.get("http response code");
	}

	/**
	 * This method tries to log the user in to CommonSense using the provided
	 * file with user credentials. If the x-session_id in the provided
	 * credentials file was created less than a week ago, the x-session_id will
	 * be reused. If x-session_id was fetched more than a week ago, this method
	 * will try to login to CommonSense and fetch a new x-session_id.
	 * 
	 * @param pathToCredentials
	 *            The credential file should contain a JsonObject with at least
	 *            the fields <code>username</code> and <code>password</code>.
	 *            (Optional) <code>x-session_id</code>. (Optional)
	 *            <code>date_created</code> which holds the date at which the
	 *            <code>x-session_id</code> was retrieved from CommonSense. If
	 *            missing, the optional fields will be created by this method.
	 */
	public static void login(String pathToCredentials) {
		// long now = (System.currentTimeMillis() / 1000L);
		// int week = 604800;

		try {
			JSONObject element = (JSONObject) JSONValue
					.parse(new FileReader(pathToCredentials));
			Credentials credentials = new Credentials((String) element.get("username"), (String) element.get("password"), "", 0);

			// if (credentials.getDate_created() > (now - week)) {
			// // session out of date
			// System.out.println("x-session_id outdated.");
			Connector.login(credentials);

			// } else {
			// // reuse x-session_id
			// System.out.println("reusing x-session_id.");
			// }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int login(String username, String password) {
		try {
			return Connector.login(username, Connector.hashPassword(password));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * This method prints the CommonSense server response.
	 * 
	 * @param response
	 */
	private static void printResponse(Map<String, String> response) {
		System.out
				.println("------------------------------RESPONSE-----------------------------");
		for (Map.Entry<String, String> entry : response.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + ": \t" + value);
		}
		System.out
				.println("-------------------------------------------------------------------");
		System.out.println();
	}

	/**
	 * This method prints the CommonSense server request.
	 * 
	 * @param REQ
	 */
	private static void printRequest(String REQ) {
		System.out
				.println("------------------------------REQUEST------------------------------");
		System.out.println(REQ);
		System.out
				.println("-------------------------------------------------------------------");
		System.out.println();
	}

	// private constructor to prevent instantiation

	private Api() {
	}
}
