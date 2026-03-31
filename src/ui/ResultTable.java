package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Process;
import model.ScheduleResult;

public class ResultTable extends JPanel {
    private JTable processTable;
    private JTable metricsTable;
    private DefaultTableModel processModel;
    private DefaultTableModel metricsModel;
    private JLabel algorithmLabel;

    public ResultTable() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(35, 35, 35));

        // Algorithm info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(35, 35, 35));
        algorithmLabel = new JLabel("Algorithm: -");
        algorithmLabel.setFont(new Font("Arial", Font.BOLD, 14));
        algorithmLabel.setForeground(new Color(240, 240, 240));
        infoPanel.add(algorithmLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.setBackground(new Color(35, 35, 35));

        // Metrics
        contentPanel.add(createMetricsPanel());

        // Process table
        contentPanel.add(createProcessTablePanel());

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Performance Metrics"));
        panel.setBackground(new Color(30, 30, 30));
        panel.setForeground(new Color(240, 240, 240));

        String[] columnNames = {"Metric", "Value"};
        metricsModel = new DefaultTableModel(columnNames, 0);
        metricsTable = new JTable(metricsModel);
        metricsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        metricsTable.setFont(new Font("Arial", Font.PLAIN, 11));
        metricsTable.setRowHeight(25);
        metricsTable.setBackground(new Color(50, 50, 50));
        metricsTable.setForeground(new Color(240, 240, 240));
        metricsTable.setTableHeader(null);  // Hide column headers
        
        // Set column widths
        metricsTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        metricsTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        // Style metrics table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        metricsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(metricsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProcessTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Process Details"));
        panel.setBackground(new Color(30, 30, 30));
        panel.setForeground(new Color(240, 240, 240));

        String[] columnNames = {"Process ID", "Arrival Time", "Burst Time", 
                "Waiting Time", "Turnaround Time", "Response Time"};
        processModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(processModel);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processTable.setFont(new Font("Arial", Font.PLAIN, 11));
        processTable.setRowHeight(22);
        processTable.setBackground(new Color(50, 50, 50));
        processTable.setForeground(new Color(240, 240, 240));
        processTable.getTableHeader().setBackground(new Color(70, 70, 70));
        processTable.getTableHeader().setForeground(new Color(240, 240, 240));

        // Center align numbers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < processTable.getColumnCount(); i++) {
            processTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(processTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void displayResult(ScheduleResult result) {
        if (result == null) {
            clearDisplay();
            return;
        }

        // Update algorithm label
        algorithmLabel.setText("Algorithm: " + result.getAlgorithmName());

        // Clear existing data
        processModel.setRowCount(0);
        metricsModel.setRowCount(0);

        // Add metrics
        metricsModel.addRow(new Object[]{"Average Waiting Time", 
                String.format("%.2f", result.getAvgWaitingTime())});
        metricsModel.addRow(new Object[]{"Average Turnaround Time", 
                String.format("%.2f", result.getAvgTurnaroundTime())});
        metricsModel.addRow(new Object[]{"Average Response Time", 
                String.format("%.2f", result.getAvgResponseTime())});
        metricsModel.addRow(new Object[]{"Total Execution Time", 
                String.valueOf(result.getTotalTime())});

        // Add process details to process table
        processModel.setRowCount(0);
        for (Process process : result.getProcesses()) {
            processModel.addRow(new Object[]{
                    process.getId(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime(),
                    process.getResponseTime()
            });
        }
    }

    public void displayComparison(java.util.List<ScheduleResult> results) {
        if (results == null || results.isEmpty()) {
            clearDisplay();
            return;
        }

        algorithmLabel.setText("Algorithm Comparison");

        // Clear and set up metrics table for comparison
        processModel.setRowCount(0);
        metricsModel.setRowCount(0);

        metricsModel.setColumnCount(5);
        metricsModel.setColumnIdentifiers(new String[]{"Algorithm", "Avg Wait", "Avg Turnaround", "Avg Response", "Total Time"});

        for (ScheduleResult result : results) {
            metricsModel.addRow(new Object[]{
                    result.getAlgorithmName(),
                    String.format("%.2f", result.getAvgWaitingTime()),
                    String.format("%.2f", result.getAvgTurnaroundTime()),
                    String.format("%.2f", result.getAvgResponseTime()),
                    String.valueOf(result.getTotalTime())
            });
        }
    }

    public void clearDisplay() {
        algorithmLabel.setText("Algorithm: -");
        processModel.setRowCount(0);
        metricsModel.setRowCount(0);
    }
}
