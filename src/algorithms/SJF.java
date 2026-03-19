package algorithms;
import java.util.*;

public class SJF {

    static class Process {
        String id;
        int arrivalTime;
        int burstTime;
        int startTime;
        int completionTime;
        int waitingTime;
        int turnaroundTime;
        int responseTime;

        Process(String id, int arrivalTime, int burstTime) {
            this.id          = id;
            this.arrivalTime = arrivalTime;
            this.burstTime   = burstTime;
        }
    }

    static List<Process> schedule(List<Process> input) {
        // Sort by arrival time first
        List<Process> remaining = new ArrayList<>(input);
        remaining.sort(Comparator.comparingInt(p -> p.arrivalTime));

        List<Process> completed = new ArrayList<>();
        int currentTime = 0;

        while (!remaining.isEmpty()) {
            final int time = currentTime;

            // Collect all processes that have arrived
            List<Process> ready = new ArrayList<>();
            for (Process p : remaining) {
                if (p.arrivalTime <= time) ready.add(p);
            }

            if (ready.isEmpty()) {
                // CPU idle — jump to next arrival
                currentTime = remaining.get(0).arrivalTime;
                continue;
            }

            // Pick the process with the shortest burst time
            Process chosen = ready.stream()
                    .min(Comparator.comparingInt((Process p) -> p.burstTime)
                                   .thenComparingInt(p -> p.arrivalTime))
                    .orElseThrow();

            chosen.startTime      = currentTime;
            chosen.completionTime = currentTime + chosen.burstTime;
            chosen.turnaroundTime = chosen.completionTime - chosen.arrivalTime;
            chosen.responseTime   = chosen.startTime - chosen.arrivalTime;
            chosen.waitingTime    = chosen.turnaroundTime - chosen.burstTime;
            currentTime           = chosen.completionTime;

            remaining.remove(chosen);
            completed.add(chosen);
        }

        return completed;
    }

    static void printResults(List<Process> processes) {
        System.out.println("=".repeat(85));
        System.out.println("     SHORTEST JOB FIRST  NON-PREEMPTIVE (SJF) SCHEDULING");
        System.out.println("=".repeat(85));
        System.out.printf("%-10s %-12s %-10s %-12s %-12s %-12s %-10s%n",
                "Process", "Arrival", "Burst", "Start", "Finish", "Response", "Waiting");
        System.out.println("-".repeat(85));

        double totalWaiting    = 0;
        double totalTurnaround = 0;
        double totalResponse   = 0;

        for (Process p : processes) {
            System.out.printf("%-10s %-12d %-10d %-12d %-12d %-12d %-10d%n",
                    p.id, p.arrivalTime, p.burstTime,
                    p.startTime, p.completionTime, p.responseTime, p.waitingTime);
            totalWaiting    += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
            totalResponse   += p.responseTime;
        }

        System.out.println("-".repeat(85));
        int n = processes.size();
        System.out.printf("Average Response Time   : %.2f ms%n", totalResponse    / n);
        System.out.printf("Average Waiting Time    : %.2f ms%n", totalWaiting    / n);
        System.out.printf("Average Turnaround Time : %.2f ms%n", totalTurnaround / n);
    }

    static void printGanttChart(List<Process> processes) {
        System.out.println("\nGantt Chart:");
        System.out.print("|");
        for (Process p : processes) {
            int width = Math.max(p.burstTime * 2, 6);
            System.out.print(center(p.id, width) + "|");
        }
        System.out.println();
        System.out.print(processes.get(0).startTime);
        for (Process p : processes) {
            int width = Math.max(p.burstTime * 2, 6) + 1;
            System.out.printf("%" + width + "d", p.completionTime);
        }
        System.out.println();
    }

    static String center(String text, int width) {
        int pad   = width - text.length();
        int left  = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>(Arrays.asList(
                new Process("P1", 0, 8),
                new Process("P2", 1, 4),
                new Process("P3", 2, 9),
                new Process("P4", 3, 5),
                new Process("P5", 4, 2)
        ));

        List<Process> result = schedule(processes);
        printResults(result);
        printGanttChart(result);
    }
}