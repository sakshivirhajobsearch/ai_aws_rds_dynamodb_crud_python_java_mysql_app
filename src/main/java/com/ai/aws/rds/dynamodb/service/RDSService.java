package com.ai.aws.rds.dynamodb.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RDSService {
	public static List<String> getDataFromRDS() {
		return getData("http://localhost:5000/rds");
	}

	private static List<String> getData(String urlStr) {
		List<String> dataList = new ArrayList<>();
		try {
			URL url = new URL(urlStr);
			System.out.println("Connecting to: " + urlStr);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				dataList.add(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			dataList.add("Error: " + e.getMessage());
		}
		return dataList;
	}
}
