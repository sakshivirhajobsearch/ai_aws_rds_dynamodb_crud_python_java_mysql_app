package com.ai.aws.rds.dynamodb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RDSRepository {

	private final String jdbcUrl = "aiawsrdsdynamodb.cnquiqqoi741.ap-south-1.rds.amazonaws.com";
	private final String username = "root";
	private final String password = "Anurag#123";

	// Fetch all rows
	public List<String[]> getAllRDSData() throws SQLException {
		List<String[]> data = new ArrayList<>();
		String query = "SELECT * FROM my_table"; // Replace with your table name
		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			int columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] row = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getString(i + 1);
				}
				data.add(row);
			}
		}
		return data;
	}

	// Get column names
	public String[] getColumnNames() throws SQLException {
		String query = "SELECT * FROM my_table LIMIT 1"; // Replace with your table
		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			String[] columnNames = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = meta.getColumnName(i + 1);
			}
			return columnNames;
		}
	}

	// Insert row
	public void insertRow(String[] values) throws SQLException {
		String[] columns = getColumnNames();
		String placeholders = String.join(",", java.util.Collections.nCopies(columns.length, "?"));
		String query = "INSERT INTO my_table VALUES (" + placeholders + ")";

		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement ps = conn.prepareStatement(query)) {

			for (int i = 0; i < values.length; i++) {
				ps.setString(i + 1, values[i]);
			}
			ps.executeUpdate();
		}
	}

	// Update row (first column as PK)
	public void updateRow(String[] values) throws SQLException {
		String[] columns = getColumnNames();
		StringBuilder setClause = new StringBuilder();
		for (int i = 1; i < columns.length; i++) {
			setClause.append(columns[i]).append("=?");
			if (i < columns.length - 1)
				setClause.append(",");
		}
		String query = "UPDATE my_table SET " + setClause + " WHERE " + columns[0] + "=?";

		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement ps = conn.prepareStatement(query)) {

			for (int i = 1; i < values.length; i++) {
				ps.setString(i, values[i]);
			}
			ps.setString(values.length, values[0]);
			ps.executeUpdate();
		}
	}

	// Delete row
	public void deleteRow(String primaryKey) throws SQLException {
		String[] columns = getColumnNames();
		String query = "DELETE FROM my_table WHERE " + columns[0] + "=?";

		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, primaryKey);
			ps.executeUpdate();
		}
	}
}
