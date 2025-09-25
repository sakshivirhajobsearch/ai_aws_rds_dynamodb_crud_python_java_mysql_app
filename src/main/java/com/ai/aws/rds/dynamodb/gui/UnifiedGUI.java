package com.ai.aws.rds.dynamodb.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.aws.rds.dynamodb.repository.DynamoDBRepository;
import com.ai.aws.rds.dynamodb.repository.RDSRepository;

public class UnifiedGUI {
	
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JTable table;
	private DefaultTableModel tableModel;

	// AWS & RDS credentials → replace with actual values
	private final String awsAccessKey = "";
	private final String awsSecretKey = "";
	private final String awsRegion = "";

	private static final String rdsHost = "";
	private final int rdsPort = 3306;
	private static final String rdsDatabase = "";
	private static final String rdsUser = "";
	private static final String rdsPassword = "";

	private DynamoDBRepository dynamoRepo;
	private RDSRepository rdsRepo;

	public UnifiedGUI() {
		dynamoRepo = new DynamoDBRepository(awsAccessKey, awsSecretKey, awsRegion);
		rdsRepo = new RDSRepository(rdsHost, rdsPort, rdsDatabase, rdsUser, rdsPassword);
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Unified AWS GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setLayout(new BorderLayout());

		// Table
		tableModel = new DefaultTableModel(new Object[] { "userId", "name", "email" }, 0);
		table = new JTable(tableModel);
		frame.add(new JScrollPane(table), BorderLayout.CENTER);

		// Input panel
		JPanel panel = new JPanel(new FlowLayout());
		JTextField userIdField = new JTextField(10);
		JTextField nameField = new JTextField(10);
		JTextField emailField = new JTextField(10);
		JButton insertButton = new JButton("Insert");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");
		JButton refreshButton = new JButton("Refresh");

		panel.add(new JLabel("UserID:"));
		panel.add(userIdField);
		panel.add(new JLabel("Name:"));
		panel.add(nameField);
		panel.add(new JLabel("Email:"));
		panel.add(emailField);
		panel.add(insertButton);
		panel.add(updateButton);
		panel.add(deleteButton);
		panel.add(refreshButton);

		frame.add(panel, BorderLayout.NORTH);

		// Button actions
		insertButton.addActionListener(e -> {
			String userId = userIdField.getText();
			String name = nameField.getText();
			String email = emailField.getText();
			dynamoRepo.insertUser(userId, name, email);
			rdsRepo.insertUser(userId, name, email);
			refreshTable();
		});

		updateButton.addActionListener(e -> {
			String userId = userIdField.getText();
			String name = nameField.getText();
			String email = emailField.getText();
			dynamoRepo.updateUser(userId, name, email);
			rdsRepo.updateUser(userId, name, email);
			refreshTable();
		});

		deleteButton.addActionListener(e -> {
			String userId = userIdField.getText();
			dynamoRepo.deleteUser(userId);
			rdsRepo.deleteUser(userId);
			refreshTable();
		});

		refreshButton.addActionListener(e -> refreshTable());

		frame.setVisible(true);
		refreshTable();
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		List<Map<String, Object>> rdsUsers = rdsRepo.getAllUsers();
		for (Map<String, Object> user : rdsUsers) {
			tableModel.addRow(new Object[] { user.get("userId"), user.get("name"), user.get("email") });
		}
		System.out.println("✅ GUI: Table refreshed.");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UnifiedGUI::new);
	}
}
