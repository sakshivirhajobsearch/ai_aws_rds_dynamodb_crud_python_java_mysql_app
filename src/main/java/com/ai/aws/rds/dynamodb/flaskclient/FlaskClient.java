package com.ai.aws.rds.dynamodb.flaskclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlaskClient {

	public static void main(String[] args) {

		try {
			// Set the endpoint URL
			URL url = new URL("http://localhost:5000/predict");

			// Open connection
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			// JSON body
			String jsonInputString = "{\"input\": [5.1, 3.5, 1.4, 0.2]}";

			// Write JSON to output stream
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			// Check response
			int status = con.getResponseCode();
			System.out.println("HTTP Status Code: " + status);

			if (status == 200) {
				// Read response
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null) {
					response.append(line.trim());
				}
				in.close();
				System.out.println("✅ Prediction Response: " + response.toString());
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
				StringBuilder error = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null) {
					error.append(line.trim());
				}
				in.close();
				System.out.println("❌ Error Response: " + error.toString());
			}

			con.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}