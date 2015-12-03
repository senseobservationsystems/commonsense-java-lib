package nl.sense_os.example;

import java.util.ArrayList;

import nl.sense_os.api.Api;
import nl.sense_os.objects.Device;
import nl.sense_os.objects.Group;
import nl.sense_os.objects.Params;
import nl.sense_os.objects.Sensor;
import nl.sense_os.objects.SensorData;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 * Sample code for using <i>commonsense-java-lib</i>.
 * 
 * @param args
 */
public class ExampleAPI {

	public static void main(String args[]) {
		// login, credential file should contain a JsonObject with 'username' and 'password'.
		String credentials = "credentials.txt";
		Api.login(credentials);

		// verbosity
		Params.General.verbosity = true;

		// start API test
		System.out.println("Starting API TEST... \n");

		// some example Api calls
		listDevices();
		listGroups();
		listSensors();
		listAllSensors();
		getSensorData();
		createSensor();
		// postSensorData();
		deleteSensor();
	}

	private static void listDevices() {
		ArrayList<Device> devices = Api.listDevices();
		System.out.println("Test listDevices()... " + (devices.size() > 0 ? "Success!" : "Fail!"));
	}

	private static void listGroups() {
		ArrayList<Group> groupList = Api.listGroups();
		System.out.println("Test listGroups()... " + (groupList.size() > 0 ? "Success!" : "Fail!"));
		printGroups(groupList);
	}

	private static void listSensors() {
		ArrayList<Sensor> sensors = Api.listSensors();
		System.out.println("Test listSensors()... " + (sensors.size() > 0 ? "Success!" : "Fail!"));
	}

	private static void listAllSensors() {
		ArrayList<Sensor> allSensors = Api.listAllSensors();
		System.out.println("Test listAllSensors()... " + (allSensors.size() > 0 ? "Success!" : "Fail!"));
		System.out.println("Get " + allSensors.size() + " sensors");
		printSensorList(allSensors);
	}

	private static void getSensorData() {
		int sensorId = 108142;
		ArrayList<SensorData> sensorData = Api.getSensorData(sensorId);
		System.out.println("Test getSensorData(sensorId)... " + (sensorData.size() > 0 ? "Success!" : "Fail!"));
		printSensorData(sensorData);
	}

	private static void createSensor() {
		// settings for creating new sensor
		String name = "defaultSensor";
		String device_type = "smartphone";
		String data_type = "json";

		JSONObject json = new JSONObject();
		json.put("data", "JSONArray");
		String data_structure = json.toString();

		Sensor s = new Sensor(name, device_type, name, data_type, data_structure);

		boolean created = Api.createSensor(s, true);
		System.out.println("Test createSensor()... " + (created ? "Success!" : "Fail!"));
	}

	private static void postSensorData() {
		// settings for posting sensor data
		int sensorId = 108142;
		JSONObject json = new JSONObject();
		json.put("x_pos", 1.4);
		json.put("y_pos", 0.2);
		json.put("heading", Math.PI);
		String value = json.toString();

		JSONObject dataPoint = new JSONObject();
		dataPoint.put("value", json);
		dataPoint.put("date", System.currentTimeMillis() / 1000);

		JSONArray dataArray = new JSONArray();
		dataArray.add(dataPoint);
		dataArray.add(dataPoint);

		JSONObject jsonFinal = new JSONObject();
		jsonFinal.put("data", dataArray);

		boolean posted = Api.postSensorData(sensorId, jsonFinal);
		System.out.println("Test postSensorData()... " + (posted ? "Success!" : "Fail!"));
	}

	private static void deleteSensor() {
		// sensor to be deleted.
		String name = "defaultSensor";
		int sensorId;

		ArrayList<Sensor> allSensors = Api.listAllSensors();
		for (int i = 0; i < allSensors.size(); i++) {
			String sensorName = allSensors.get(i).getName();
			sensorId = allSensors.get(i).getId();

			// delete sensor (and all of its data)
			if (sensorName.contains(name)) {
				boolean sensorDeleted = Api.deleteSensor(sensorId);
				System.out.println("Test deleteSensor()... " + (sensorDeleted ? "Success!" : "Fail!"));
			}
		}
	}

	private static void printSensorData(ArrayList<SensorData> sensordata) {
		for (int i = 0; i < sensordata.size(); i++) {
			System.out.println("data:" + sensordata.get(i));
			System.out.println("date: " + sensordata.get(i).getDate());
			System.out.println("value: " + sensordata.get(i).getValue());
		}
		System.out.println();
	}

	private static void printSensorList(ArrayList<Sensor> sensors) {
		int numSensors = sensors.size();

		// print detailed sensors information
		for (int i = 0; i < numSensors; i++) {
			Sensor s = sensors.get(i);
			System.out.print(s.getId() + " ");
			System.out.print(s.getName() + " ");
			System.out.print(s.getType() + " ");
			System.out.print(s.getDevice_type() + " ");
			System.out.print(s.getData_type_id() + " ");
			System.out.print(s.getPager_type() + " ");
			System.out.print(s.getDisplay_name() + " ");
			System.out.print(s.getData_type() + " ");
			System.out.print(s.getData_structure() + " ");
			System.out.print((null != s.getDevice() ? s.getDevice().getUuid() : s.getDevice()) + " ");
			System.out.print(s.isUse_data_storage() + " ");
			System.out.println();
		}
		System.out.println();
	}

	private static void printGroups(ArrayList<Group> groupList) {
		for (int i = 0; i < groupList.size(); i++) {
			System.out.print(groupList.get(i).getId() + " ");
			System.out.print(groupList.get(i).getName() + " ");
			System.out.print(groupList.get(i).getDescription() + " ");
			System.out.print(groupList.get(i).getPublicity() + " ");
			System.out.println();
		}
		System.out.println();
	}
}
