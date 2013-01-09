package nl.sense_os.api;

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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Api {

	final static JsonParser parser = new JsonParser();

	/**
	 * This method returns an ArrayList of devices to which the current user has access.
	 * 
	 * @access public
	 * @return ArrayList of devices
	 * @throws IOException
	 */
	public static ArrayList<Device> listDevices() {
		final String urlString = Params.General.devMode ? SenseUrls.DEV_DEVICES : SenseUrls.DEVICES;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of sensors
		ArrayList<Device> devices = new ArrayList<Device>();	// ArrayList of devices

		String content = response.get("content");				// String format of devices

		JsonObject json = (JsonObject) parser.parse(content); 	// JSON container of devices

		int numSensors = json.get("devices").getAsJsonArray().size();

		for (int i = 0; i < numSensors; i++) {
			JsonElement element = json.get("devices").getAsJsonArray().get(i);
			Device d = new Gson().fromJson(element, Device.class);
			devices.add(d);
		}

		return devices;
	}

	/**
	 * This method returns a list of sensors to which the current user has access.
	 * 
	 * @access public
	 * @return sensor array
	 * @throws IOException
	 */
	public static ArrayList<Sensor> listAllSensors() {
		final String urlString = Params.General.devMode ? SenseUrls.DEV_ALL_SENSORS : SenseUrls.ALL_SENSORS;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of sensors
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();	// ArrayList of sensors
		String content = response.get("content");				// String format of sensors
		JsonObject json = (JsonObject) parser.parse(content); 	// JSON container of sensors

		int numSensors = json.get("sensors").getAsJsonArray().size();

		for (int i = 0; i < numSensors; i++) {
			JsonElement element = json.get("sensors").getAsJsonArray().get(i);
			Sensor s = new Gson().fromJson(element, Sensor.class);
			sensors.add(s);
		}

		return sensors;
	}

	/**
	 * This method returns a list of sensors to which the current user has access. Parameters for this function can be changed in Params.Sensors.
	 * 
	 * @access public
	 * @return sensor array
	 * @throws IOException
	 */
	public static ArrayList<Sensor> listSensors() {
		String urlString = Params.General.devMode ? SenseUrls.DEV_BASE : SenseUrls.BASE;

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
			response = Connector.request(urlString, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of sensors
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();	// ArrayList of sensors
		String content = response.get("content");				// String format of sensors
		JsonObject json = (JsonObject) parser.parse(content); 	// JSON container of sensors

		int numSensors = json.get("sensors").getAsJsonArray().size();

		for (int i = 0; i < numSensors; i++) {
			JsonElement element = json.get("sensors").getAsJsonArray().get(i);
			Sensor s = new Gson().fromJson(element, Sensor.class);
			sensors.add(s);
		}

		return sensors;
	}

	/**
	 * This method returns a list of sensor data constrained by the parameters below
	 * 
	 * @param sensorId
	 *            Unique id of the sensor (use for instance <code>listAllSensors()</code> to fetch sensor id).
	 * @param page
	 *            This parameter specifies which page of the results must be retrieved. The page offset starts at 0.
	 * @param per_page
	 *            This parameter specifies the amount of items that must be received at once. The maximum amount is 1000 items and the default amount is 100 items.
	 * @param start_date
	 *            The start date in UNIX time format (double seconds). Used as date to start the item search from.
	 * @param end_date
	 *            The end date in UNIX time format (double seconds). The date until which the items will be searched.
	 * @return An ArrayList of sensor data: <code>ArrayList&lt;Data&gt;</code>.
	 */
	public static ArrayList<SensorData> getSensorData(int sensorId) {
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA : SenseUrls.SENSOR_DATA;

		// parse parameters
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));
		urlString += "?page=" + String.valueOf(Params.Data.page);
		urlString += "&per_page=" + String.valueOf(Params.Data.per_page);
		urlString += "&start_date=" + String.valueOf(Params.Data.start_date);
		urlString += "&end_date=" + String.valueOf(Params.Data.end_date);
		urlString = Params.Data.date < 0 ? urlString : urlString + "&date=" + Params.Data.date;
		urlString = Params.Data.last ? urlString + "&last=1" : urlString;
		urlString += "&sort=" + Params.Data.sort;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		if (null != response) {

			// create ArrayList of sensors
			ArrayList<SensorData> data = new ArrayList<SensorData>();	// ArrayList of sensors
			String content = response.get("content");					// String format of sensors
			JsonObject json = (JsonObject) parser.parse(content); 		// JSON container of sensors

			System.out.println(content);
			int numSensors = json.get("data").getAsJsonArray().size();

			for (int i = 0; i < numSensors; i++) {
				JsonElement element = json.get("data").getAsJsonArray().get(i);
				SensorData d = new Gson().fromJson(element, SensorData.class);
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
			response = Connector.request(url, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		// create ArrayList of groups
		ArrayList<Group> groupList = new ArrayList<Group>();	// ArrayList of groups
		String groups = response.get("content");				// String format of groups
		JsonObject json = (JsonObject) parser.parse(groups); 	// JSON container of groups

		int numGroups = json.get("groups").getAsJsonArray().size();

		for (int i = 0; i < numGroups; i++) {
			JsonElement element = json.get("groups").getAsJsonArray().get(i);
			Group g = new Gson().fromJson(element, Group.class);
			groupList.add(g);
		}

		return groupList;
	}

	/**
	 * This methods returns the members of the group as a list of users. Only group members can perform this action.
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
	 * This method returns the details of the device to witch the sensor is connected.
	 * 
	 * @param int sensorId
	 * @return mixed
	 */
	public static Device readParentDevice(int sensorId) {

		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_BASE : SenseUrls.BASE;
		urlString += "sensors/" + sensorId + "/device" + SenseUrls.FORMAT;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, null, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
		}

		String content = response.get("content");
		JsonObject json = (JsonObject) parser.parse(content);
		if (null != json.get("error")) {
			System.out.println(json.get("error"));
			return null;
		}

		return new Gson().fromJson(json.get("device"), Device.class);
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
		final JsonObject json = sensor.toJson();

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
		final String urlString = Params.General.devMode ? SenseUrls.DEV_CREATE_SENSOR : SenseUrls.CREATE_SENSOR;

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, json, Settings.getSession_id());
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean created = response.get("http response code").equals("201") ? true : false;

		// print response
		if (Params.General.verbosity) {
			printResponse(response);
			System.out.println(created ? "New '" + sensor.getName() + "' sensor created." : "Failed creating new '" + sensor.getName() + "' sensor.");
		}
		// return response.get("http response code");
		return created;
	}

	/**
	 * This method will delete the sensor with id <code>sensorId</code> from CommonSense.
	 * 
	 * @param sensorId
	 */
	public static boolean deleteSensor(int sensorId) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_DELETE_SENSOR : SenseUrls.DELETE_SENSOR;
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.requestDeletion(urlString, Settings.getSession_id());
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
	 * This method will call a POST request to post the sensor data <code>data</code> to the sensor <code>sensorId</code> at CommonSense.
	 * 
	 * @param sensorId
	 *            sensorId that will be used to POST the data to.
	 * @param data
	 *            sensor data to send to CommonSense.
	 * @return true if the data was successfully posted to CommonSense, false otherwise.
	 */
	public static boolean postSensorData(int sensorId, JsonObject json) {
		// request URL
		String urlString = Params.General.devMode ? SenseUrls.DEV_SENSOR_DATA : SenseUrls.SENSOR_DATA;
		urlString = urlString.replaceAll("\\<id\\>", String.valueOf(sensorId));

		// print request
		if (Params.General.verbosity) {
			printRequest(urlString);
		}

		Map<String, String> response = new HashMap<String, String>();

		try {
			response = Connector.request(urlString, json, Settings.getSession_id());
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
	 * This method tries to log the user in to CommonSense using the provided file with user credentials. If the x-session_id in the provided credentials file was created less than a week ago, the
	 * x-session_id will be reused. If x-session_id was fetched more than a week ago, this method will try to login to CommonSense and fetch a new x-session_id.
	 * 
	 * @param pathToCredentials
	 *            The credential file should contain a JsonObject with at least the fields <code>username</code> and <code>password</code>. (Optional) <code>x-session_id</code>. (Optional)
	 *            <code>date_created</code> which holds the date at which the <code>x-session_id</code> was retrieved from CommonSense. If missing, the optional fields will be created by this method.
	 */
	public static void login(String pathToCredentials) {
		// long now = (System.currentTimeMillis() / 1000L);
		// int week = 604800;

		try {
			JsonElement element = parser.parse(new FileReader(pathToCredentials));
			Credentials credentials = new Gson().fromJson(element, Credentials.class);

			// if (credentials.getDate_created() > (now - week)) {
			// // session out of date
			// System.out.println("x-session_id outdated.");
			Connector.login(credentials);

			// } else {
			// // reuse x-session_id
			// System.out.println("reusing x-session_id.");
			// }

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method prints the CommonSense server response.
	 * 
	 * @param response
	 */
	private static void printResponse(Map<String, String> response) {
		System.out.println("------------------------------RESPONSE-----------------------------");
		for (Map.Entry<String, String> entry : response.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + ": \t" + value);
		}
		System.out.println("-------------------------------------------------------------------");
		System.out.println();
	}

	/**
	 * This method prints the CommonSense server request.
	 * 
	 * @param REQ
	 */
	private static void printRequest(String REQ) {
		System.out.println("------------------------------REQUEST------------------------------");
		System.out.println(REQ);
		System.out.println("-------------------------------------------------------------------");
		System.out.println();
	}

	// private constructor to prevent instantiation

	private Api() {
	}
}