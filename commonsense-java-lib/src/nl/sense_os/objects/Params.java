package nl.sense_os.objects;

public class Params {

	/**
	 * General parameters for API.
	 */
	public static class General {

		/**
		 * This parameters enables developer mode.
		 */
		public static boolean devMode = false;

		/**
		 * This parameters determines the verbosity. Set true to enable writing server request and output to command line.
		 */
		public static boolean verbosity = false;
	}

	/**
	 * Parameters for fetching sensors
	 */
	public static class Sensors {
		/**
		 * This parameter specifies which page of the results must be retrieved. The page offset starts at 0.
		 */
		public static int page = 0;

		/**
		 * This parameter specifies the amount of items that must be received at once. The maximum amount is 1000 items and the default amount is 100 items.
		 */
		public static int per_page = 1000;

		/**
		 * With this parameter set to 1, only sensors which the user shares with others are displayed. The sensor object will contain an array named users with the ids of users that have access to the
		 * sensor.
		 */
		public static boolean shared = false;

		/**
		 * With this parameter set to 1, only sensors which the user owns are displayed. When it's set to 0 only sensors which the user does not own are displayed. The sensor object will contain an
		 * user object named owner with the id, name, surname, email and username of the owner of the sensor.
		 */
		public static boolean owned = true;

		/**
		 * With this parameter set to 1, only physical sensors will be returned. Also information of the sensor's parent device will be given in the fields device_device_type and device_device_uuid
		 * unless the parameter owned is used.
		 */
		public static boolean physical = true;

		/**
		 * To get all the related data as device, environment and owner the parameter details=full can be used. If only a list of sensor id's is needed then details=no can be used.
		 */
		public static boolean details = false;

	}

	/**
	 * Parameters for fetching sensor data
	 */
	public static class Data {
		/**
		 * This parameter specifies which page of the results must be retrieved. The page offset starts at 0.
		 */
		public static int page = 0;

		/**
		 * This parameter specifies the amount of items that must be received at once. The maximum amount is 1000 items and the default amount is 100 items.
		 */
		public static int per_page = 100;

		/**
		 * The start date in UNIX time format (double seconds). Used as date to start the item search from.
		 */
		public static double start_date = 0.0;

		/**
		 * The end date in UNIX time format (double seconds). The date until which the items will be searched.
		 */
		public static double end_date = 95617584000.0;

		/**
		 * The date in UNIX time format at which the items will be searched.
		 */
		public static double date = -1.0;

		/**
		 * With this parameter the last item will be returned based on the item date.
		 */
		public static boolean last = false;

		/**
		 * This parameter can be used to sort the results ascending (ASC) or descending (DESC).
		 */
		public static String sort = "ASC";
	}

	/**
	 * Parameters for fetching user data
	 */
	public static class Users {

		/**
		 * This parameter specifies which page of the results must be retrieved. The page offset starts at 0.
		 */
		public static int page = 0;

		/**
		 * This parameter specifies the amount of items that must be received at once. The maximum amount is 1000 items and the default amount is 100 items.
		 */
		public static int per_page = 100;
	}

	/**
	 * Parameters for fetching group data
	 */
	public static class Groups {

		/**
		 * This parameter specifies which page of the results must be retrieved. The page offset starts at 0.
		 */
		public static int page = 0;

		/**
		 * This parameter specifies the amount of items that must be received at once. The maximum amount is 1000 items and the default amount is 100 items.
		 */
		public static int per_page = 100;

		/**
		 * By adding this parameter a total item count will be added to the result.
		 */
		public static boolean total = false;

		/**
		 * With this parameter set to 1, only public groups are returned and when it is set to 0 only private groups are returned.
		 */
		public static byte publicity = 0;
	}

	// private constructor to prevent instantiation
	private Params() {
	}
}
