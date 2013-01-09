package nl.sense_os.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import nl.sense_os.constants.SenseUrls;
import nl.sense_os.constants.Settings;
import nl.sense_os.objects.Credentials;
import nl.sense_os.objects.Params;

import org.apache.http.conn.ssl.SSLSocketFactory;

import com.google.gson.JsonObject;

public class Connector {

	/**
	 * Tries to login using the username and password from the private preferences and updates the {@link #isLoggedIn} status accordingly. Can also be called from Activities that are bound to the
	 * service.
	 * 
	 * @return 0 if login completed successfully, -2 if login was forbidden, and -1 for any other errors.
	 */
	public static int login(Credentials c) {
		System.out.println("Try to log in ... ");

		// try to log in
		int result = -1;
		if ((c.getUsername() != null) && (c.getPassword() != null)) {
			try {
				result = Connector.login(c.getUsername(), c.getPassword());
			} catch (Exception e) {
				System.out.println("Exception during login! " + e + ": '" + e.getMessage() + "'");
				// handle result below
			}
		} else {
			System.out.println("Cannot login: username or password unavailable");
		}

		// handle the result
		switch (result) {
		case 0: // logged in successfully
			break;
		case -1: // error
			System.out.println("Login failed!");
			break;
		case -2: // forbidden
			System.out.println("Login forbidden!");
			break;
		default:
			System.out.println("Unexpected login result: " + result);
		}
		return result;
	}

	/**
	 * Tries to log in at CommonSense using the supplied username and password. After login, the cookie containing the session ID is stored in the preferences.
	 * 
	 * @param context
	 *            Context for getting preferences
	 * @param username
	 *            Username for authentication
	 * @param password
	 *            Hashed password for authentication
	 * @return 0 if login completed successfully, -2 if login was forbidden, and -1 for any other errors.
	 * @throws JSONException
	 *             In case of unparseable response from CommonSense
	 * @throws IOException
	 *             In case of communication failure to CommonSense
	 */
	public static int login(String username, String password) throws IOException {

		final String url = Params.General.devMode ? SenseUrls.DEV_LOGIN : SenseUrls.LOGIN;
		// System.out.println(url);

		final JsonObject user = new JsonObject();
		user.addProperty("username", username);
		user.addProperty("password", password);

		// perform actual request
		Map<String, String> response = request(url, user, null);

		// if response code is not 200 (OK), the login was incorrect
		String responseCode = response.get("http response code");
		int result = -1;
		if ("403".equalsIgnoreCase(responseCode)) {
			System.out.println("CommonSense login refused! Response: forbidden!");
			result = -2;
		} else if (!"200".equalsIgnoreCase(responseCode)) {
			System.out.println("CommonSense login failed! Response: " + responseCode);
			result = -1;
		} else {
			// received 200 response
			result = 0;
		}

		// get the cookie from the response
		String session_id = response.get("x-session_id");
		// HttpSession session = response.getSession();

		Settings.setSession_id(session_id);

		System.out.println("----------------ACHIEVEMENT UNLOCKED, NEW ITEM ACQUIRED---------------------");
		System.out.println("X-SESSION_ID: " + session_id);
		System.out.println("----------------------------------------------------------------------------");

		if (result == 0 && response.get("set-cookie") == null) {
			// something went horribly wrong
			System.out.println("CommonSense login failed: no cookie received?!");
			result = -1;
		}

		// handle result
		// Editor authEditor = authPrefs.edit();
		switch (result) {
		case 0: // logged in
			System.out.println("Logged in.");
			break;
		case -1: // error
			System.out.println("Error");
			break;
		case -2: // unauthorized
			System.out.println("Unauthorized");
			session_id = "";
			break;
		default:
			System.out.println("Unexpected login result: " + result);
		}
		return result;
	}

	/**
	 * @param hashMe
	 *            "clear" password String to be hashed before sending it to CommonSense
	 * @return Hashed String
	 */
	public static String hashPassword(String hashMe) {
		final byte[] unhashedBytes = hashMe.getBytes();
		try {
			final MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(unhashedBytes);
			final byte[] hashedBytes = algorithm.digest();

			final StringBuffer hexString = new StringBuffer();
			for (final byte element : hashedBytes) {
				final String hex = Integer.toHexString(0xFF & element);
				if (hex.length() == 1) {
					hexString.append(0);
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Performs request at CommonSense API. Returns the response code, content, and headers.
	 * 
	 * @param context
	 *            Application context, used to read preferences.
	 * @param urlString
	 *            Complete URL to perform request to.
	 * @param content
	 *            (Optional) Content for the request. If the content is not null, the request method is automatically POST. The default method is GET.
	 * @param cookie
	 *            (Optional) Cookie header for the request.
	 * @return Map with "content" and "http response code" fields, plus fields for all response headers.
	 * @throws IOException
	 */
	public static Map<String, String> request(String urlString, JsonObject content, String cookie) throws IOException {

		HttpURLConnection urlConnection = null;
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			final boolean compress = true;

			// open new URL connection channel.
			URL url = new URL(urlString);
			// System.out.println("URL: " + url.toString());

			if ("https".equals(url.getProtocol().toLowerCase())) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				urlConnection = https;
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}

			// some parameters
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(false);

			// set cookie (if available)
			if (null != cookie) {
				urlConnection.setRequestProperty("x-session_id", cookie);
			}

			// send content (if available)
			if (null != content) {
				urlConnection.setDoOutput(true);
				urlConnection.setRequestProperty("Content-Type", "application/json");

				// send content
				DataOutputStream printout;
				if (compress) {
					// do not set content size
					urlConnection.setRequestProperty("Transfer-Encoding", "chunked");
					urlConnection.setRequestProperty("Content-Encoding", "gzip");
					GZIPOutputStream zipStream = new GZIPOutputStream(urlConnection.getOutputStream());
					printout = new DataOutputStream(zipStream);
				} else {
					// set content size
					urlConnection.setFixedLengthStreamingMode(content.toString().length());
					urlConnection.setRequestProperty("Content-Length", "" + content.toString().length());
					printout = new DataOutputStream(urlConnection.getOutputStream());
				}
				printout.writeBytes(content.toString());
				printout.flush();
				printout.close();
			}

			// get response, or read error message
			InputStream inputStream;
			try {
				inputStream = urlConnection.getInputStream();
			} catch (IOException e) {
				inputStream = urlConnection.getErrorStream();
			}
			if (null == inputStream) {
				throw new IOException("could not get InputStream");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024);
			String line;
			StringBuffer responseContent = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
				responseContent.append('\r');
			}
			result.put("content", responseContent.toString());
			result.put("http response code", "" + urlConnection.getResponseCode());

			// clean up
			reader.close();
			reader = null;
			inputStream.close();
			inputStream = null;

			// get headers
			Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
			String key, valueString;
			List<String> value;
			for (Entry<String, List<String>> entry : headerFields.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				if (null != key && null != value) {
					key = key.toLowerCase();
					valueString = value.toString();
					valueString = valueString.substring(1, valueString.length() - 1);
					// Log.d(TAG, "Header field '" + key + "': '" + valueString + "'");
					result.put(key, valueString);
				} else {
					// Log.d(TAG, "Skipped header field '" + key + "': '" + value + "'");
				}
			}

			return result;

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	/**
	 * Performs DELETE request at CommonSense API. Returns the response code, content and headers.
	 * 
	 * @param urlString
	 *            Complete URL to perform request to.
	 * @param cookie
	 *            (Optional) Cookie header for the request.
	 * @return Map with "content" and "http response code" fields, plus fields for all response headers.
	 * @throws IOException
	 */
	public static Map<String, String> requestDeletion(String urlString, String cookie) throws IOException {
		HttpURLConnection urlConnection = null;
		HashMap<String, String> result = new HashMap<String, String>();

		try {
			// open new URL connection channel.
			URL url = new URL(urlString);

			if ("https".equals(url.getProtocol().toLowerCase())) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				urlConnection = https;
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}

			// some parameters
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(false);

			// set cookie (if available)
			if (null != cookie) {
				urlConnection.setRequestProperty("x-session_id", cookie);
			}

			urlConnection.setDoOutput(false);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestMethod("DELETE");

			// get response, or read error message
			InputStream inputStream;
			try {
				inputStream = urlConnection.getInputStream();
			} catch (IOException e) {
				inputStream = urlConnection.getErrorStream();
			}
			if (null == inputStream) {
				throw new IOException("could not get InputStream");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024);
			String line;
			StringBuffer responseContent = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
				responseContent.append('\r');
			}
			result.put("content", responseContent.toString());
			result.put("http response code", "" + urlConnection.getResponseCode());

			// clean up
			reader.close();
			reader = null;
			inputStream.close();
			inputStream = null;

			// get headers
			Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
			String key, valueString;
			List<String> value;
			for (Entry<String, List<String>> entry : headerFields.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				if (null != key && null != value) {
					key = key.toLowerCase();
					valueString = value.toString();
					valueString = valueString.substring(1, valueString.length() - 1);
					// Log.d(TAG, "Header field '" + key + "': '" + valueString + "'");
					result.put(key, valueString);
				} else {
					// Log.d(TAG, "Skipped header field '" + key + "': '" + value + "'");
				}
			}

			return result;

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	/**
	 * Trust every server - do not check for any certificate
	 */
	// TODO Solve issue with security certificate for HTTPS.
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
