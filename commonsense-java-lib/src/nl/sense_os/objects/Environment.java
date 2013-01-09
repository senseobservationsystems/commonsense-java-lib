package nl.sense_os.objects;

public class Environment {

	private int id;
	private String name;
	private int floors;
	private String gps_outline;
	private String position;
	private double date;

	public Environment(int id, String name, int floors, String gps_outline, String position, double date) {
		this.id = id;
		this.name = name;
		this.floors = floors;
		this.gps_outline = gps_outline;
		this.position = position;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getFloors() {
		return floors;
	}

	public String getGps_outline() {
		return gps_outline;
	}

	public String getPosition() {
		return position;
	}

	public double getDate() {
		return date;
	}
}
