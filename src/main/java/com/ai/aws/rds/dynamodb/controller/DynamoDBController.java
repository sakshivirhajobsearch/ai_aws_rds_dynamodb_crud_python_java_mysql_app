package com.ai.aws.rds.dynamodb.controller;

import java.util.List;
import java.util.Map;

import com.ai.aws.rds.dynamodb.repository.DynamoDBRepository;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoDBController {

	private final DynamoDBRepository repo;

	public DynamoDBController() {
		// Replace with your actual AWS credentials
		String accessKey = "YOUR_AWS_ACCESS_KEY";
		String secretKey = "YOUR_AWS_SECRET_KEY";
		String region = "us-east-1";

		repo = new DynamoDBRepository(accessKey, secretKey, region);
	}

	// INSERT
	public void insertUser(String userId, String name, String email) {
		repo.insertUser(userId, name, email);
	}

	// UPDATE
	public void updateUser(String userId, String name, String email) {
		repo.updateUser(userId, name, email);
	}

	// DELETE
	public void deleteUser(String userId) {
		repo.deleteUser(userId);
	}

	// SCAN USERS
	public List<Map<String, AttributeValue>> scanUsers() {
		return repo.scanUsers();
	}

	// GET COLUMN NAMES
	public List<String> getColumnNames() {
		return repo.getColumnNames();
	}
}
