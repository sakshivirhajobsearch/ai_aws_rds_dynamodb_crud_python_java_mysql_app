package com.ai.aws.rds.dynamodb.flaskclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlaskClient {

	public static String callAI(double[] input) {
		
		try {
			URL url = new URL("http://localhost:5000/predict");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			StringBuilder json = new StringBuilder("{\"input\": [");
			for (int i = 0; i < input.length; i++) {
				json.append(input[i]);
				if (i < input.length - 1)
					json.append(", ");
			}
			json.append("]}");

			try (OutputStream os = con.getOutputStream()) {
				byte[] inputBytes = json.toString().getBytes("utf-8");
				os.write(inputBytes, 0, inputBytes.length);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			con.disconnect();
			return response.toString();
		} catch (Exception e) {
			return "❌ Error calling AI: " + e.getMessage();
		}
	}

	public static void main(String[] args) {
		double[] example = { 1.0, 1.0 };
		System.out.println("AI Response: " + callAI(example));
	}
}
