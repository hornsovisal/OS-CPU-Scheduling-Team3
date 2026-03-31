package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Process;

public class ProcessInputPanel extends JPanel {
    private JTextField pidField;
    private JTextField arrivalField;
    private JTextField burstField;
    private JTextField priorityField;
    private JButton addButton;
    private JList<String> processList;
    private DefaultListModel<String> listModel;
    private List<Process> processes;

    public ProcessInputPanel() {
        this.processes = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Process Input"));
        setBackground(new Color(30, 30, 30));
        setForeground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(350, 200));

        // Input row: PID | Arrival | Burst | Priority | Add (compact)
        JPanel inputRow = new JPanel(new GridLayout(1, 10, 3, 0));
        inputRow.setBackground(new Color(30, 30, 30));
        inputRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel pidLabel = new JLabel("PID:");
        pidLabel.setForeground(new Color(240, 240, 240));
        pidLabel.setFont(new Font("Arial", Font.BOLD, 10));
        inputRow.add(pidLabel);
        pidField = new JTextField();
        pidField.setBackground(new Color(50, 50, 50));
        pidField.setForeground(new Color(240, 240, 240));
        pidField.setCaretColor(new Color(240, 240, 240));
        pidField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        inputRow.add(pidField);

        JLabel arrivalLabel = new JLabel("Arr:");
        arrivalLabel.setForeground(new Color(240, 240, 240));
        arrivalLabel.setFont(new Font("Arial", Font.BOLD, 10));
        inputRow.add(arrivalLabel);
        arrivalField = new JTextField();
        arrivalField.setBackground(new Color(50, 50, 50));
        arrivalField.setForeground(new Color(240, 240, 240));
        arrivalField.setCaretColor(new Color(240, 240, 240));
        arrivalField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        inputRow.add(arrivalField);

        JLabel burstLabel = new JLabel("Burst:");
        burstLabel.setForeground(new Color(240, 240, 240));
        burstLabel.setFont(new Font("Arial", Font.BOLD, 10));
        inputRow.add(burstLabel);
        burstField = new JTextField();
        burstField.setBackground(new Color(50, 50, 50));
        burstField.setForeground(new Color(240, 240, 240));
        burstField.setCaretColor(new Color(240, 240, 240));
        burstField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        inputRow.add(burstField);

        JLabel priorityLabel = new JLabel("Pri:");
        priorityLabel.setForeground(new Color(240, 240, 240));
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 10));
        inputRow.add(priorityLabel);
        priorityField = new JTextField();
        priorityField.setText("0");
        priorityField.setBackground(new Color(50, 50, 50));
        priorityField.setForeground(new Color(240, 240, 240));
        priorityField.setCaretColor(new Color(240, 240, 240));
        priorityField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        inputRow.add(priorityField);

        addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 10));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        addButton.addActionListener(e -> addProcess());
        inputRow.add(addButton);

        add(inputRow, BorderLayout.NORTH);

        // Process list
        listModel = new DefaultListModel<>();
        processList = new JList<>(listModel);
        processList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processList.setFont(new Font("Monospaced", Font.PLAIN, 10));
        processList.setFixedCellHeight(18);
        processList.setBackground(new Color(50, 50, 50));
        processList.setForeground(new Color(240, 240, 240));
        processList.setSelectionBackground(new Color(70, 130, 180));
        processList.setSelectionForeground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(processList);
        add(scrollPane, BorderLayout.CENTER);

        // Control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        controlPanel.setBackground(new Color(30, 30, 30));

        JButton removeButton = new JButton("Remove");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 10));
        removeButton.setBackground(new Color(200, 100, 100));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeSelected());
        controlPanel.add(removeButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void addProcess() {
        try {
            String id = pidField.getText().trim();
            int arrival = Integer.parseInt(arrivalField.getText().trim());
            int burst = Integer.parseInt(burstField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Process ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (arrival < 0 || burst <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid arrival or burst time", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Process process = new Process(id, arrival, burst, priority);
            processes.add(process);
            updateListDisplay();
            clearInputFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateListDisplay() {
        listModel.clear();
        for (Process p : processes) {
            String display = String.format("%-8s | Arrival: %2d | Burst: %2d | Priority: %d",
                    p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority());
            listModel.addElement(display);
        }
    }

    private void clearInputFields() {
        pidField.setText("");
        arrivalField.setText("");
        burstField.setText("");
        priorityField.setText("0");
        pidField.requestFocus();
    }

    private void removeSelected() {
        int selectedIndex = processList.getSelectedIndex();
        if (selectedIndex >= 0) {
            processes.remove(selectedIndex);
            updateListDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Select a process to remove", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                List<Process> loadedProcesses = utils.FileLoader.loadFromCSV(filePath);
                processes.addAll(loadedProcesses);
                updateListDisplay();
                JOptionPane.showMessageDialog(this, "Loaded " + loadedProcesses.size() + " processes", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public List<Process> getProcesses() {
        return new ArrayList<>(processes);
    }

    public void clearAll() {
        processes.clear();
        listModel.clear();
        clearInputFields();
    }
}
