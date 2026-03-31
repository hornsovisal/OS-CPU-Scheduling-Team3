package simulation;

import java.util.List;
import model.Process;

public class MetricsCalculator {

    public static double calculateAverageWaitingTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0;
        }
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTime();
        }
        return (double) totalWaitingTime / processes.size();
    }

    public static double calculateAverageTurnaroundTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0;
        }
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        return (double) totalTurnaroundTime / processes.size();
    }

    public static double calculateAverageResponseTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0;
        }
        int totalResponseTime = 0;
        for (Process p : processes) {
            totalResponseTime += p.getResponseTime();
        }
        return (double) totalResponseTime / processes.size();
    }

    public static int calculateTotalTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0;
        }
        int maxTime = 0;
        for (Process p : processes) {
            if (p.getEndTime() > maxTime) {
                maxTime = p.getEndTime();
            }
        }
        return maxTime;
    }

    public static double calculateCPUUtilization(List<Process> processes, int totalTime) {
        if (totalTime == 0) {
            return 0;
        }
        int totalBurstTime = 0;
        for (Process p : processes) {
            totalBurstTime += p.getBurstTime();
        }
        return (double) totalBurstTime / totalTime * 100;
    }

    public static double calculateThroughput(List<Process> processes, int totalTime) {
        if (totalTime == 0) {
            return 0;
        }
        return (double) processes.size() / totalTime;
    }
}
