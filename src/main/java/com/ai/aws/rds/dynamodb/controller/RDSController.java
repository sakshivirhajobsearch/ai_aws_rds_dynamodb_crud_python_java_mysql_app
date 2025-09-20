package com.ai.aws.rds.dynamodb.controller;

import java.util.List;
import java.util.Map;

import com.ai.aws.rds.dynamodb.repository.RDSRepository;

public class RDSController {

	private final RDSRepository repo;

	public RDSController() {
		// Replace with your actual RDS credentials
		String host = "your-rds-endpoint";
		int port = 3306;
		String database = "your_database_name";
		String username = "your_rds_username";
		String password = "your_rds_password";

		repo = new RDSRepository(host, port, database, username, password);
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

	// GET ALL USERS
	public List<Map<String, Object>> getAllUsers() {
		return repo.getAllUsers();
	}

	// GET COLUMN NAMES
	public List<String> getColumnNames() {
		return repo.getColumnNames();
	}
}
