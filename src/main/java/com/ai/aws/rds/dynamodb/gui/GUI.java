package com.ai.aws.rds.dynamodb.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.aws.rds.dynamodb.repository.RDSRepository;

public class GUI {

	private final RDSRepository rdsRepo = new RDSRepository();
	private JTable rdsTable;

	public void createAndShowGUI() {
		JFrame frame = new JFrame("AI + AWS RDS CRUD Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 600);
		frame.setLayout(new BorderLayout());

		// --- RDS Tab ---
		JPanel rdsPanel = new JPanel(new BorderLayout());
		rdsTable = new JTable();
		rdsTable.setAutoCreateRowSorter(true);
		JScrollPane rdsScroll = new JScrollPane(rdsTable);

		JPanel rdsButtonPanel = new JPanel(new FlowLayout());
		JButton refreshRDSButton = new JButton("Refresh");
		JButton addRDSButton = new JButton("Add");
		JButton updateRDSButton = new JButton("Update");
		JButton deleteRDSButton = new JButton("Delete");

		refreshRDSButton.addActionListener(e -> fetchRDSData());
		addRDSButton.addActionListener(e -> addRDSRow());
		updateRDSButton.addActionListener(e -> updateRDSRow());
		deleteRDSButton.addActionListener(e -> deleteRDSRow());

		rdsButtonPanel.add(refreshRDSButton);
		rdsButtonPanel.add(addRDSButton);
		rdsButtonPanel.add(updateRDSButton);
		rdsButtonPanel.add(deleteRDSButton);

		rdsPanel.add(rdsButtonPanel, BorderLayout.NORTH);
		rdsPanel.add(rdsScroll, BorderLayout.CENTER);

		frame.add(rdsPanel, BorderLayout.CENTER);
		frame.setVisible(true);

		fetchRDSData();
	}

	// ---------------- RDS CRUD ----------------
	private void fetchRDSData() {
		new Thread(() -> {
			try {
				String[] columnNames = rdsRepo.getColumnNames();
				List<String[]> data = rdsRepo.getAllRDSData();
				SwingUtilities.invokeLater(() -> updateTable(rdsTable, columnNames, data));
			} catch (Exception e) {
				e.printStackTrace();
				showError("Error fetching RDS data: " + e.getMessage());
			}
		}).start();
	}

	private void addRDSRow() {
		try {
			String[] columns = rdsRepo.getColumnNames();
			String[] newRow = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				newRow[i] = JOptionPane.showInputDialog("Enter value for " + columns[i]);
			}
			rdsRepo.insertRow(newRow);
			fetchRDSData();
		} catch (Exception e) {
			e.printStackTrace();
			showError("Error adding row: " + e.getMessage());
		}
	}

	private void updateRDSRow() {
		int selectedRow = rdsTable.getSelectedRow();
		if (selectedRow == -1) {
			showError("Select a row to update.");
			return;
		}
		try {
			DefaultTableModel model = (DefaultTableModel) rdsTable.getModel();
			String[] updatedRow = new String[model.getColumnCount()];
			for (int i = 0; i < model.getColumnCount(); i++) {
				String current = (String) model.getValueAt(selectedRow, i);
				String value = JOptionPane.showInputDialog("Update " + model.getColumnName(i), current);
				updatedRow[i] = value != null ? value : current;
			}
			rdsRepo.updateRow(updatedRow);
			fetchRDSData();
		} catch (Exception e) {
			e.printStackTrace();
			showError("Error updating row: " + e.getMessage());
		}
	}

	private void deleteRDSRow() {
		int selectedRow = rdsTable.getSelectedRow();
		if (selectedRow == -1) {
			showError("Select a row to delete.");
			return;
		}
		try {
			DefaultTableModel model = (DefaultTableModel) rdsTable.getModel();
			String id = (String) model.getValueAt(selectedRow, 0);
			rdsRepo.deleteRow(id);
			fetchRDSData();
		} catch (Exception e) {
			e.printStackTrace();
			showError("Error deleting row: " + e.getMessage());
		}
	}

	private void updateTable(JTable table, String[] columnNames, List<String[]> data) {
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		for (String[] row : data) {
			model.addRow(row);
		}
		table.setModel(model);
		table.setAutoCreateRowSorter(true);
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GUI().createAndShowGUI());
	}
}
