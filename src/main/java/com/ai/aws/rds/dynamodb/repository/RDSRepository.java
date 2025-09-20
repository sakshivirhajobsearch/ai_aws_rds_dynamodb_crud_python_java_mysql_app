package com.ai.aws.rds.dynamodb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDSRepository {

	private final String jdbcUrl;
	private final String username;
	private final String password;

	public RDSRepository(String host, int port, String database, String username, String password) {
		this.jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
		this.username = username;
		this.password = password;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, username, password);
	}

	// INSERT
	public void insertUser(String id, String name, String email) {
		String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setString(3, email);
			ps.executeUpdate();
			System.out.println("✅ RDS: User inserted successfully.");
		} catch (SQLException e) {
			System.err.println("❌ RDS: Error inserting user → " + e.getMessage());
		}
	}

	// UPDATE
	public void updateUser(String id, String name, String email) {
		String sql = "UPDATE users SET name=?, email=? WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, id);
			ps.executeUpdate();
			System.out.println("✅ RDS: User updated successfully.");
		} catch (SQLException e) {
			System.err.println("❌ RDS: Error updating user → " + e.getMessage());
		}
	}

	// DELETE
	public void deleteUser(String id) {
		String sql = "DELETE FROM users WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.executeUpdate();
			System.out.println("✅ RDS: User deleted successfully.");
		} catch (SQLException e) {
			System.err.println("❌ RDS: Error deleting user → " + e.getMessage());
		}
	}

	// GET ALL USERS
	public List<Map<String, Object>> getAllUsers() {
		List<Map<String, Object>> users = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					row.put(metaData.getColumnName(i), rs.getObject(i));
				}
				users.add(row);
			}
		} catch (SQLException e) {
			System.err.println("❌ RDS: Error fetching users → " + e.getMessage());
		}
		return users;
	}

	// GET COLUMN NAMES
	public List<String> getColumnNames() {
		List<String> columns = new ArrayList<>();
		String sql = "SELECT * FROM users LIMIT 1";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				columns.add(metaData.getColumnName(i));
			}
		} catch (SQLException e) {
			System.err.println("❌ RDS: Error fetching column names → " + e.getMessage());
		}
		return columns;
	}
}
