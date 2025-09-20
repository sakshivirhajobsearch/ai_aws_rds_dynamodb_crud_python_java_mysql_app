package com.ai.aws.rds.dynamodb.token;

import java.net.URI;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class TemporaryAWSSecurityCredentials {

	public static void main(String[] args) {

		// Step 1: Create session credentials from your temporary keys
		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create("", // Access Key ID
				"", // Secret Access Key
				"" // (your full session token)
		);

		// Step 2: Use StaticCredentialsProvider with DynamoDB
		DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
				.credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
				.region(software.amazon.awssdk.regions.Region.AP_SOUTH_1)
				.endpointOverride(URI.create("https://dynamodb.ap-south-1.amazonaws.com")).build();

		// Step 3: Sample scan request (adjust table name)
		ScanRequest request = ScanRequest.builder().tableName("users").build();

		try {
			ScanResponse response = dynamoDbClient.scan(request);
			System.out.println("✅ DynamoDB Users count: " + response.count());
		} catch (Exception e) {
			System.err.println("❌ DynamoDB Error: " + e.getMessage());
		}
	}
}
