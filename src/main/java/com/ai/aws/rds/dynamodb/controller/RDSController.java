package com.ai.aws.rds.dynamodb.controller;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ai.aws.rds.dynamodb.repository.RDSRepository;

public class RDSController {

	private final RDSRepository repo = new RDSRepository();

	public void showRDSData() {
		
		JFrame frame = new JFrame("Amazon RDS Data");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 400);

		String[] columnNames = repo.getColumnNames();
		List<String[]> data = repo.getAllRDSData();

		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		for (String[] row : data) {
			model.addRow(row);
		}

		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setVisible(true);
	}
}
