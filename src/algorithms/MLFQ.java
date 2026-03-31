package algorithms;

import model.GanttEntry;
import model.Process;
import model.ScheduleResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MLFQ implements Scheduler {
    private static final int NUM_QUEUES = 3;
    private static final int[] QUANTUMS = {2, 4, Integer.MAX_VALUE}; // Queues 0, 1, 2
    private static final int AGING_THRESHOLD = 5; // Time units before aging

    @Override
    public ScheduleResult schedule(List<Process> processes) {
        ScheduleResult result = new ScheduleResult("MLFQ");

        if (processes == null || processes.isEmpty()) {
            return result;
        }

        // Clone processes
        List<Process> processesCopy = new ArrayList<>();
        for (Process p : processes) {
            processesCopy.add(p.clone());
        }

        // Create priority queues
        @SuppressWarnings("unchecked")
        Queue<Process>[] readyQueues = new Queue[NUM_QUEUES];
        for (int i = 0; i < NUM_QUEUES; i++) {
            readyQueues[i] = new LinkedList<>();
        }

        List<GanttEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int totalResponseTime = 0;
        List<Process> completed = new ArrayList<>();
        int processIndex = 0;

        // Track last execution time for aging
        int[] lastExecutionTime = new int[NUM_QUEUES];
        for (int i = 0; i < NUM_QUEUES; i++) {
            lastExecutionTime[i] = 0;
        }

        while (completed.size() < processesCopy.size()) {
            // Add all arrived processes to queue 0
            while (processIndex < processesCopy.size() && 
                   processesCopy.get(processIndex).getArrivalTime() <= currentTime) {
                Process p = processesCopy.get(processIndex);
                p.setQueue(0);
                readyQueues[0].offer(p);
                processIndex++;
            }

            // Aging: Promote processes that have waited too long
            for (int i = 1; i < NUM_QUEUES; i++) {
                if (currentTime - lastExecutionTime[i] >= AGING_THRESHOLD) {
                    List<Process> toPromote = new ArrayList<>(readyQueues[i]);
                    for (Process p : toPromote) {
                        readyQueues[i].remove(p);
                        p.setQueue(i - 1);
                        readyQueues[i - 1].offer(p);
                    }
                    if (!toPromote.isEmpty()) {
                        lastExecutionTime[i] = currentTime;
                    }
                }
            }

            // Find next process to execute
            Process current = null;
            int selectedQueue = -1;
            for (int i = 0; i < NUM_QUEUES; i++) {
                if (!readyQueues[i].isEmpty()) {
                    current = readyQueues[i].poll();
                    selectedQueue = i;
                    lastExecutionTime[i] = currentTime;
                    break;
                }
            }

            if (current == null) {
                // No process ready, advance time
                if (processIndex < processesCopy.size()) {
                    currentTime = processesCopy.get(processIndex).getArrivalTime();
                }
                continue;
            }

            if (current.getStartTime() == -1) {
                current.setStartTime(currentTime);
                current.setResponseTime(currentTime - current.getArrivalTime());
            }

            int quantum = QUANTUMS[selectedQueue];
            int executionTime = Math.min(quantum, current.getRemainingTime());
            int startTime = currentTime;
            currentTime += executionTime;

            current.setRemainingTime(current.getRemainingTime() - executionTime);
            ganttChart.add(new GanttEntry(current.getId(), startTime, currentTime));

            // Add newly arrived processes to queue 0
            while (processIndex < processesCopy.size() && 
                   processesCopy.get(processIndex).getArrivalTime() <= currentTime) {
                Process p = processesCopy.get(processIndex);
                p.setQueue(0);
                readyQueues[0].offer(p);
                processIndex++;
            }

            if (current.getRemainingTime() > 0) {
                // Process not finished, demote to next queue
                if (selectedQueue < NUM_QUEUES - 1) {
                    current.setQueue(selectedQueue + 1);
                    readyQueues[selectedQueue + 1].offer(current);
                } else {
                    // Already in lowest queue, stay there
                    readyQueues[selectedQueue].offer(current);
                }
            } else {
                // Process completed
                current.setEndTime(currentTime);
                int turnaroundTime = currentTime - current.getArrivalTime();
                int waitingTime = turnaroundTime - current.getBurstTime();

                current.setWaitingTime(waitingTime);
                current.setTurnaroundTime(turnaroundTime);

                totalWaitingTime += waitingTime;
                totalTurnaroundTime += turnaroundTime;
                totalResponseTime += current.getResponseTime();

                result.addProcess(current);
                completed.add(current);
            }
        }

        result.setGanttChart(ganttChart);
        result.setTotalTime(currentTime);
        result.setAvgWaitingTime((double) totalWaitingTime / processesCopy.size());
        result.setAvgTurnaroundTime((double) totalTurnaroundTime / processesCopy.size());
        result.setAvgResponseTime((double) totalResponseTime / processesCopy.size());

        return result;
    }
}
