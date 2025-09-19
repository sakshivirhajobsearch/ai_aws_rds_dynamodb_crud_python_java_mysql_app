package com.ai.aws.rds.dynamodb.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class RDSConnectionTest {
	
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://aiawsrdsdynamodb.cnquiqqoi741.ap-south-1.rds.amazonaws.com:3306/aiawsrdsdynamodb?useSSL=false";
		String user = "root";
		String pass = "Anurag#123";

		try (Connection conn = DriverManager.getConnection(url, user, pass)) {
			System.out.println("✅ Connected successfully to RDS!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}