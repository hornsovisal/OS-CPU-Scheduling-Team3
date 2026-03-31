package ui;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Process;
import model.ScheduleResult;
import simulation.Simulator;
import utils.Exporter;

public class MainWindow extends JFrame {
	private Simulator simulator;
	private ProcessInputPanel processInputPanel;
	private AlgorithmSelectionPanel algorithmPanel;
	private GanttChartView ganttChartView;
	private ResultTable resultTable;
	private JPanel metricsPanel;
	private List<ScheduleResult> currentResult;

	public MainWindow() {
		this.simulator = new Simulator();
		this.currentResult = new ArrayList<>();
		initialize();
	}

	private void initialize() {
		setTitle("CPU Scheduling Algorithm Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1600, 1000);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));
		getContentPane().setBackground(new Color(35, 35, 35));

		// Top panel: Input + Algorithm + Controls
		JPanel topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);

		// Middle: Gantt Chart
		JPanel ganttPanel = createGanttPanel();
		add(ganttPanel, BorderLayout.CENTER);

		// Bottom: Results + Metrics
		JPanel bottomPanel = createBottomPanel();
		add(bottomPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(new Color(35, 35, 35));

		// Left: Process Input
		processInputPanel = new ProcessInputPanel();
		panel.add(processInputPanel, BorderLayout.WEST);

		// Center: Algorithm Selection
		algorithmPanel = new AlgorithmSelectionPanel();
		panel.add(algorithmPanel, BorderLayout.CENTER);

		// Right: Control Buttons
		JPanel controlPanel = createControlPanel();
		panel.add(controlPanel, BorderLayout.EAST);

		return panel;
	}

	private JPanel createControlPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Simulation Controls"));
		panel.setBackground(new Color(30, 30, 30));
		panel.setForeground(new Color(240, 240, 240));
		panel.setPreferredSize(new Dimension(150, 200));

		JButton runButton = new JButton("Run Simulation");
		runButton.setFont(new Font("Arial", Font.BOLD, 11));
		runButton.setBackground(new Color(76, 175, 80));
		runButton.setForeground(Color.WHITE);
		runButton.setFocusPainted(false);
		runButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		runButton.addActionListener(e -> runSimulation());
		panel.add(runButton);

		JButton clearButton = new JButton("Clear Processes");
		clearButton.setFont(new Font("Arial", Font.BOLD, 11));
		clearButton.setBackground(new Color(244, 67, 54));
		clearButton.setForeground(Color.WHITE);
		clearButton.setFocusPainted(false);
		clearButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		clearButton.addActionListener(e -> clearProcesses());
		panel.add(clearButton);

		JButton loadButton = new JButton("Load File");
		loadButton.setFont(new Font("Arial", Font.BOLD, 11));
		loadButton.setBackground(new Color(33, 150, 243));
		loadButton.setForeground(Color.WHITE);
		loadButton.setFocusPainted(false);
		loadButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		loadButton.addActionListener(e -> processInputPanel.loadFromFile());
		panel.add(loadButton);

		JButton exportButton = new JButton("Export Results");
		exportButton.setFont(new Font("Arial", Font.BOLD, 11));
		exportButton.setBackground(new Color(156, 39, 176));
		exportButton.setForeground(Color.WHITE);
		exportButton.setFocusPainted(false);
		exportButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		exportButton.addActionListener(e -> exportResults());
		panel.add(exportButton);

		return panel;
	}

	private JPanel createGanttPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Gantt Chart Display"));
		panel.setBackground(new Color(30, 30, 30));
		panel.setForeground(new Color(240, 240, 240));
		panel.setPreferredSize(new Dimension(1000, 250));

		ganttChartView = new GanttChartView();
		ganttChartView.setMinimumSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(ganttChartView);
		scrollPane.setBackground(new Color(40, 40, 40));
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		panel.setBackground(new Color(35, 35, 35));
		panel.setPreferredSize(new Dimension(1600, 300));

		// Results Table (left)
		resultTable = new ResultTable();
		resultTable.setPreferredSize(new Dimension(1000, 300));
		panel.add(resultTable, BorderLayout.CENTER);

		// Metrics Panel (right)
		metricsPanel = createMetricsPanel();
		metricsPanel.setPreferredSize(new Dimension(250, 300));
		panel.add(metricsPanel, BorderLayout.EAST);

		return panel;
	}

	private JPanel createMetricsPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Average Metrics"));
		panel.setBackground(new Color(30, 30, 30));
		panel.setForeground(new Color(240, 240, 240));
		panel.setPreferredSize(new Dimension(250, 100));

		JLabel waitLabel = new JLabel("Avg Waiting Time: --");
		waitLabel.setFont(new Font("Arial", Font.BOLD, 11));
		waitLabel.setForeground(new Color(240, 240, 240));
		waitLabel.setName("wait");
		panel.add(waitLabel);

		JLabel turnLabel = new JLabel("Avg Turnaround Time: --");
		turnLabel.setFont(new Font("Arial", Font.BOLD, 11));
		turnLabel.setForeground(new Color(240, 240, 240));
		turnLabel.setName("turn");
		panel.add(turnLabel);

		JLabel respLabel = new JLabel("Avg Response Time: --");
		respLabel.setFont(new Font("Arial", Font.BOLD, 11));
		respLabel.setForeground(new Color(240, 240, 240));
		respLabel.setName("resp");
		panel.add(respLabel);

		return panel;
	}

	private void runSimulation() {
		List<Process> processes = processInputPanel.getProcesses();

		if (processes.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please add at least one process", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		simulator.clearProcesses();
		simulator.addProcesses(processes);

		try {
			String algo = algorithmPanel.getSelectedAlgorithm();
			ScheduleResult result = null;

			switch (algo) {
				case "FCFS":
					result = simulator.simulateFCFS();
					break;
				case "SJF":
					result = simulator.simulateSJF();
					break;
				case "SRT":
					result = simulator.simulateSRT();
					break;
				case "Round Robin":
					result = simulator.simulateRoundRobin(algorithmPanel.getQuantum());
					break;
				case "MLFQ":
					result = simulator.simulateMLFQ();
					break;
			}

			if (result != null) {
				displayResult(result);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Simulation error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void displayResult(ScheduleResult result) {
		currentResult.clear();
		currentResult.add(result);
		ganttChartView.setScheduleResult(result);
		ganttChartView.revalidate();
		ganttChartView.repaint();
		resultTable.displayResult(result);
		updateMetrics(result);
	}

	private void updateMetrics(ScheduleResult result) {
		for (Component comp : metricsPanel.getComponents()) {
			if (comp instanceof JLabel) {
				JLabel label = (JLabel) comp;
				switch (label.getName()) {
					case "wait":
						label.setText(String.format("Avg Waiting Time: %.2f", result.getAvgWaitingTime()));
						break;
					case "turn":
						label.setText(String.format("Avg Turnaround Time: %.2f", result.getAvgTurnaroundTime()));
						break;
					case "resp":
						label.setText(String.format("Avg Response Time: %.2f", result.getAvgResponseTime()));
						break;
				}
			}
		}
	}

	private void clearProcesses() {
		int choice = JOptionPane.showConfirmDialog(this, "Clear all processes?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			processInputPanel.clearAll();
			simulator.clearProcesses();
			ganttChartView.clear();
			resultTable.clearDisplay();
			clearMetricsDisplay();
		}
	}

	private void clearMetricsDisplay() {
		for (Component comp : metricsPanel.getComponents()) {
			if (comp instanceof JLabel) {
				JLabel label = (JLabel) comp;
				label.setText(label.getText().split(":")[0] + ": --");
			}
		}
	}

	private void exportResults() {
		if (currentResult.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Run a simulation first", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
		int result = fileChooser.showSaveDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filePath.endsWith(".csv")) {
					filePath += ".csv";
				}
				Exporter.exportResultsToCSV(filePath, currentResult.get(0));
				JOptionPane.showMessageDialog(this, "Exported successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Export error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
