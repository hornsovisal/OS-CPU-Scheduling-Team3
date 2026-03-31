package model;

import java.util.ArrayList;
import java.util.List;

public class ScheduleResult {
    private List<Process> processes;
    private List<GanttEntry> ganttChart;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private double avgResponseTime;
    private String algorithmName;
    private int totalTime;

    public ScheduleResult() {
        this.processes = new ArrayList<>();
        this.ganttChart = new ArrayList<>();
        this.algorithmName = "";
    }

    public ScheduleResult(String algorithmName) {
        this();
        this.algorithmName = algorithmName;
    }

    // Getters and Setters
    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public List<GanttEntry> getGanttChart() {
        return ganttChart;
    }

    public void setGanttChart(List<GanttEntry> ganttChart) {
        this.ganttChart = ganttChart;
    }

    public void addGanttEntry(GanttEntry entry) {
        this.ganttChart.add(entry);
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(double avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public double getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    public void setAvgTurnaroundTime(double avgTurnaroundTime) {
        this.avgTurnaroundTime = avgTurnaroundTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "ScheduleResult{" +
                "algorithmName='" + algorithmName + '\'' +
                ", avgWaitingTime=" + String.format("%.2f", avgWaitingTime) +
                ", avgTurnaroundTime=" + String.format("%.2f", avgTurnaroundTime) +
                ", avgResponseTime=" + String.format("%.2f", avgResponseTime) +
                ", totalTime=" + totalTime +
                '}';
    }
}
