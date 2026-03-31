package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.GanttEntry;
import model.Process;
import model.ScheduleResult;

public class Exporter {

    public static void exportResultsToCSV(String filePath, ScheduleResult result) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("CPU Scheduling Simulation Results\n");
            writer.write("Algorithm: " + result.getAlgorithmName() + "\n");
            writer.write("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("\n");

            // Write metrics
            writer.write("Performance Metrics\n");
            writer.write("Average Waiting Time," + String.format("%.2f", result.getAvgWaitingTime()) + "\n");
            writer.write("Average Turnaround Time," + String.format("%.2f", result.getAvgTurnaroundTime()) + "\n");
            writer.write("Average Response Time," + String.format("%.2f", result.getAvgResponseTime()) + "\n");
            writer.write("Total Time," + result.getTotalTime() + "\n");
            writer.write("\n");

            // Write process details
            writer.write("Process Details\n");
            writer.write("Process ID,Arrival Time,Burst Time,Waiting Time,Turnaround Time,Response Time\n");
            for (Process p : result.getProcesses()) {
                writer.write(String.format("%s,%d,%d,%d,%d,%d\n",
                        p.getId(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getWaitingTime(),
                        p.getTurnaroundTime(),
                        p.getResponseTime()));
            }
            writer.write("\n");

            // Write Gantt chart
            writer.write("Gantt Chart\n");
            writer.write("Process ID,Start Time,End Time\n");
            for (GanttEntry entry : result.getGanttChart()) {
                writer.write(String.format("%s,%d,%d\n",
                        entry.getProcessId(),
                        entry.getStart(),
                        entry.getEnd()));
            }
        }
    }

    public static void exportComparisonToCSV(String filePath, List<ScheduleResult> results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Algorithm Comparison Report\n");
            writer.write("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("\n");

            // Write comparison table
            writer.write("Algorithm,Avg Waiting Time,Avg Turnaround Time,Avg Response Time,Total Time\n");
            for (ScheduleResult result : results) {
                writer.write(String.format("%s,%.2f,%.2f,%.2f,%d\n",
                        result.getAlgorithmName(),
                        result.getAvgWaitingTime(),
                        result.getAvgTurnaroundTime(),
                        result.getAvgResponseTime(),
                        result.getTotalTime()));
            }
        }
    }

    public static void exportToJSON(String filePath, ScheduleResult result) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("{\n");
            writer.write("  \"algorithm\": \"" + result.getAlgorithmName() + "\",\n");
            writer.write("  \"metrics\": {\n");
            writer.write("    \"avgWaitingTime\": " + String.format("%.2f", result.getAvgWaitingTime()) + ",\n");
            writer.write("    \"avgTurnaroundTime\": " + String.format("%.2f", result.getAvgTurnaroundTime()) + ",\n");
            writer.write("    \"avgResponseTime\": " + String.format("%.2f", result.getAvgResponseTime()) + ",\n");
            writer.write("    \"totalTime\": " + result.getTotalTime() + "\n");
            writer.write("  },\n");
            writer.write("  \"processes\": [\n");

            List<Process> processes = result.getProcesses();
            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                writer.write("    {\n");
                writer.write("      \"id\": \"" + p.getId() + "\",\n");
                writer.write("      \"arrivalTime\": " + p.getArrivalTime() + ",\n");
                writer.write("      \"burstTime\": " + p.getBurstTime() + ",\n");
                writer.write("      \"waitingTime\": " + p.getWaitingTime() + ",\n");
                writer.write("      \"turnaroundTime\": " + p.getTurnaroundTime() + ",\n");
                writer.write("      \"responseTime\": " + p.getResponseTime() + "\n");
                writer.write("    }");
                if (i < processes.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("  ],\n");
            writer.write("  \"ganttChart\": [\n");

            List<GanttEntry> gantt = result.getGanttChart();
            for (int i = 0; i < gantt.size(); i++) {
                GanttEntry entry = gantt.get(i);
                writer.write("    {\n");
                writer.write("      \"processId\": \"" + entry.getProcessId() + "\",\n");
                writer.write("      \"start\": " + entry.getStart() + ",\n");
                writer.write("      \"end\": " + entry.getEnd() + "\n");
                writer.write("    }");
                if (i < gantt.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");
        }
    }

    public static boolean createDirectory(String dirPath) {
        File dir = new File(dirPath);
        return dir.mkdirs() || dir.exists();
    }
}
