package com.ai.aws.rds.dynamodb.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class DynamoDBRepository {

	private final DynamoDbClient dynamoDbClient;
	private final String tableName = "users";

	public DynamoDBRepository(String accessKey, String secretKey, String regionName) {
		dynamoDbClient = DynamoDbClient.builder()
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.region(Region.of(regionName)).build();
	}

	// INSERT
	public void insertUser(String id, String name, String email) {
		Map<String, AttributeValue> item = new HashMap<>();
		item.put("id", AttributeValue.builder().s(id).build()); // primary key must match table
		item.put("name", AttributeValue.builder().s(name).build());
		item.put("email", AttributeValue.builder().s(email).build());

		try {
			dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());
			System.out.println("✅ DynamoDB: User inserted successfully.");
		} catch (DynamoDbException e) {
			System.err.println("❌ DynamoDB: Error inserting user → " + e.getMessage());
		}
	}

	// UPDATE
	public void updateUser(String id, String name, String email) {
		try {
			Map<String, AttributeValue> key = Map.of("id", AttributeValue.builder().s(id).build());
			Map<String, AttributeValueUpdate> updates = Map.of("name",
					AttributeValueUpdate.builder().value(AttributeValue.builder().s(name).build())
							.action(AttributeAction.PUT).build(),
					"email", AttributeValueUpdate.builder().value(AttributeValue.builder().s(email).build())
							.action(AttributeAction.PUT).build());

			dynamoDbClient.updateItem(builder -> builder.tableName(tableName).key(key).attributeUpdates(updates));
			System.out.println("✅ DynamoDB: User updated successfully.");
		} catch (DynamoDbException e) {
			System.err.println("❌ DynamoDB: Error updating user → " + e.getMessage());
		}
	}

	// DELETE
	public void deleteUser(String id) {
		try {
			Map<String, AttributeValue> key = Map.of("id", AttributeValue.builder().s(id).build());
			dynamoDbClient.deleteItem(builder -> builder.tableName(tableName).key(key));
			System.out.println("✅ DynamoDB: User deleted successfully.");
		} catch (DynamoDbException e) {
			System.err.println("❌ DynamoDB: Error deleting user → " + e.getMessage());
		}
	}

	// SCAN USERS
	public List<Map<String, AttributeValue>> scanUsers() {
		try {
			ScanResponse response = dynamoDbClient.scan(builder -> builder.tableName(tableName));
			return response.items();
		} catch (DynamoDbException e) {
			System.err.println("❌ DynamoDB: Error scanning users → " + e.getMessage());
			return Collections.emptyList();
		}
	}

	// GET COLUMN NAMES
	public List<String> getColumnNames() {
		List<String> columns = new ArrayList<>();
		List<Map<String, AttributeValue>> items = scanUsers();
		if (!items.isEmpty()) {
			columns.addAll(items.get(0).keySet());
		}
		return columns;
	}
}
