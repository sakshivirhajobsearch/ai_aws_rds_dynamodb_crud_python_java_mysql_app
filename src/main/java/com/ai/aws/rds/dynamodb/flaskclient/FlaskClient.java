package com.ai.aws.rds.dynamodb.flaskclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FlaskClient {

	public static void main(String[] args) {
		try {
			// 🔗 Flask Endpoint
			URL url = new URL("http://localhost:5000/predict");

			// 🌐 Open HTTP connection
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(5000); // 5 seconds connect timeout
			con.setReadTimeout(5000); // 5 seconds read timeout
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setDoOutput(true);

			// 🧾 JSON Payload (adjust input as needed)
			String jsonInputString = "{\"input\": [5.1, 3.5, 1.4, 0.2]}";
			byte[] inputBytes = jsonInputString.getBytes(StandardCharsets.UTF_8);

			// Set Content-Length (optional but good practice)
			con.setRequestProperty("Content-Length", String.valueOf(inputBytes.length));

			// 🚀 Send POST request
			try (OutputStream os = con.getOutputStream()) {
				os.write(inputBytes);
				os.flush();
			}

			// ✅ Response Handling
			int status = con.getResponseCode();
			System.out.println("HTTP Status Code: " + status);

			BufferedReader reader;
			if (status >= 200 && status < 300) {
				reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
			} else {
				reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
				System.out.println("⚠️ Error response from server:");
			}

			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line.trim());
			}
			reader.close();

			System.out.println("🔁 Server Response: " + response.toString());

			// 🔚 Disconnect
			con.disconnect();

		} catch (java.net.ConnectException ce) {
			System.out.println("🚫 Unable to connect to Flask server at http://localhost:5000");
			System.out.println("👉 Make sure the Flask server is running and reachable.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
