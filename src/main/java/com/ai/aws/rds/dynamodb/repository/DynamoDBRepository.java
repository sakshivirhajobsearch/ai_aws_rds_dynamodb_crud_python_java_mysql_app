package com.ai.aws.rds.dynamodb.repository;

import java.util.HashMap;
import java.util.Map;

import com.ai.aws.rds.dynamodb.model.DataModel;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class DynamoDBRepository implements AutoCloseable {

	private final DynamoDbClient dynamoDb;
	private final String tableName = "users";

	public DynamoDBRepository() {
		this.dynamoDb = DynamoDbClient.builder().region(Region.AP_SOUTH_1)
				.credentialsProvider(DefaultCredentialsProvider.create()).build();
	}

	// CREATE item
	public boolean createItem(DataModel data) {
		try {
			Map<String, AttributeValue> item = new HashMap<>();
			item.put("id", AttributeValue.builder().s(data.getId()).build());
			item.put("name", AttributeValue.builder().s(data.getName()).build());
			item.put("value", AttributeValue.builder().s(data.getValue()).build());

			PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(item)
					.conditionExpression("attribute_not_exists(id)") // avoids duplicates
					.build();

			dynamoDb.putItem(request);
			return true;

		} catch (ConditionalCheckFailedException e) {
			System.out.println("Item already exists with ID: " + data.getId());
			return false;
		} catch (DynamoDbException e) {
			e.printStackTrace();
			return false;
		}
	}

	// READ item
	public DataModel getItem(String id) {
		try {
			Map<String, AttributeValue> key = new HashMap<>();
			key.put("id", AttributeValue.builder().s(id).build());

			GetItemRequest request = GetItemRequest.builder().tableName(tableName).key(key).build();

			GetItemResponse response = dynamoDb.getItem(request);
			if (response.hasItem()) {
				Map<String, AttributeValue> item = response.item();
				return new DataModel(item.get("id").s(), item.get("name").s(), item.get("value").s());
			}
			return null;

		} catch (DynamoDbException e) {
			e.printStackTrace();
			return null;
		}
	}

	// UPDATE item
	public boolean updateItem(DataModel data) {
		try {
			Map<String, AttributeValue> key = new HashMap<>();
			key.put("id", AttributeValue.builder().s(data.getId()).build());

			UpdateItemRequest request = UpdateItemRequest.builder().tableName(tableName).key(key)
					.updateExpression("SET #n = :name, #v = :value")
					.expressionAttributeNames(Map.of("#n", "name", "#v", "value"))
					.expressionAttributeValues(Map.of(":name", AttributeValue.builder().s(data.getName()).build(),
							":value", AttributeValue.builder().s(data.getValue()).build()))
					.conditionExpression("attribute_exists(id)") // only update existing
					.build();

			dynamoDb.updateItem(request);
			return true;

		} catch (ConditionalCheckFailedException e) {
			System.out.println("Item not found with ID: " + data.getId());
			return false;
		} catch (DynamoDbException e) {
			e.printStackTrace();
			return false;
		}
	}

	// DELETE item
	public boolean deleteItem(String id) {
		try {
			Map<String, AttributeValue> key = new HashMap<>();
			key.put("id", AttributeValue.builder().s(id).build());

			DeleteItemRequest request = DeleteItemRequest.builder().tableName(tableName).key(key).build();

			dynamoDb.deleteItem(request);
			return true;

		} catch (DynamoDbException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		dynamoDb.close();
	}
}
