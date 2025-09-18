package com.ai.aws.rds.dynamodb.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ai.aws.rds.dynamodb.service.DynamoDBService;
import com.ai.aws.rds.dynamodb.service.RDSService;

public class GUIApp {
	
	public void display() {
		
		JFrame frame = new JFrame("AWS RDS + DynamoDB Viewer");
		JTextArea area = new JTextArea(20, 50);

		List<String> rdsData = RDSService.getDataFromRDS();
		List<String> dynamoData = DynamoDBService.getDataFromDynamoDB();

		area.append("RDS Data:\n" + String.join("\n", rdsData));
		area.append("\n\nDynamoDB Data:\n" + String.join("\n", dynamoData));

		frame.add(new JScrollPane(area), BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}