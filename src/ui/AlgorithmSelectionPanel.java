package ui;

import java.awt.*;
import javax.swing.*;

public class AlgorithmSelectionPanel extends JPanel {
    private JRadioButton fcfsButton;
    private JRadioButton sjfButton;
    private JRadioButton srtButton;
    private JRadioButton rrButton;
    private JRadioButton mlfqButton;
    private JSpinner quantumSpinner;

    public AlgorithmSelectionPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Algorithm Selection"));
        setBackground(new Color(30, 30, 30));
        setForeground(new Color(240, 240, 240));

        // Create button group
        ButtonGroup group = new ButtonGroup();

        // FCFS
        fcfsButton = new JRadioButton("FCFS (First Come First Serve)");
        fcfsButton.setFont(new Font("Arial", Font.PLAIN, 11));
        fcfsButton.setForeground(new Color(240, 240, 240));
        fcfsButton.setBackground(new Color(30, 30, 30));
        group.add(fcfsButton);
        add(fcfsButton);

        // SJF
        sjfButton = new JRadioButton("SJF (Shortest Job First)");
        sjfButton.setFont(new Font("Arial", Font.PLAIN, 11));
        sjfButton.setForeground(new Color(240, 240, 240));
        sjfButton.setBackground(new Color(30, 30, 30));
        group.add(sjfButton);
        add(sjfButton);

        // SRT
        srtButton = new JRadioButton("SRT (Shortest Remaining Time)");
        srtButton.setFont(new Font("Arial", Font.PLAIN, 11));
        srtButton.setForeground(new Color(240, 240, 240));
        srtButton.setBackground(new Color(30, 30, 30));
        group.add(srtButton);
        add(srtButton);

        // Round Robin
        JPanel rrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rrPanel.setBackground(new Color(30, 30, 30));
        rrButton = new JRadioButton("Round Robin");
        rrButton.setFont(new Font("Arial", Font.PLAIN, 11));
        rrButton.setForeground(new Color(240, 240, 240));
        rrButton.setBackground(new Color(30, 30, 30));
        rrButton.addActionListener(e -> quantumSpinner.setEnabled(rrButton.isSelected()));
        group.add(rrButton);
        rrPanel.add(rrButton);

        JLabel quantumLabel = new JLabel("Quantum: ");
        quantumLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        quantumLabel.setForeground(new Color(240, 240, 240));
        rrPanel.add(quantumLabel);

        SpinnerModel spinnerModel = new SpinnerNumberModel(2, 1, 20, 1);
        quantumSpinner = new JSpinner(spinnerModel);
        quantumSpinner.setPreferredSize(new Dimension(60, 25));
        quantumSpinner.setEnabled(false);
        ((JSpinner.DefaultEditor) quantumSpinner.getEditor()).getTextField().setBackground(new Color(50, 50, 50));
        ((JSpinner.DefaultEditor) quantumSpinner.getEditor()).getTextField().setForeground(new Color(240, 240, 240));
        rrPanel.add(quantumSpinner);

        add(rrPanel);

        // MLFQ
        mlfqButton = new JRadioButton("MLFQ (Multilevel Feedback Queue)");
        mlfqButton.setFont(new Font("Arial", Font.PLAIN, 11));
        mlfqButton.setForeground(new Color(240, 240, 240));
        mlfqButton.setBackground(new Color(30, 30, 30));
        mlfqButton.setSelected(true); // Default selection
        group.add(mlfqButton);
        add(mlfqButton);

        add(Box.createVerticalGlue());
    }

    public String getSelectedAlgorithm() {
        if (fcfsButton.isSelected()) {
            return "FCFS";
        } else if (sjfButton.isSelected()) {
            return "SJF";
        } else if (srtButton.isSelected()) {
            return "SRT";
        } else if (rrButton.isSelected()) {
            return "Round Robin";
        } else {
            return "MLFQ";
        }
    }

    public int getQuantum() {
        return (int) quantumSpinner.getValue();
    }
}
