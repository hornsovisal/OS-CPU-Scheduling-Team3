package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.GanttEntry;
import model.Process;
import model.ScheduleResult;

public class FCFS implements Scheduler {
    @Override
    public ScheduleResult schedule(List<Process> processes) {
        ScheduleResult result = new ScheduleResult("FCFS");

        if (processes == null || processes.isEmpty()) {
            result.setAvgWaitingTime(0.0);
            result.setAvgTurnaroundTime(0.0);
            result.setAvgResponseTime(0.0);
            result.setTotalTime(0);
            return result;
        }

        List<Process> ordered = new ArrayList<>();
        for (Process p : processes) {
            ordered.add(p.clone());
        }

        ordered.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getId));

        int currentTime = 0;
        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;

        for (Process p : ordered) {
            if (currentTime < p.getArrivalTime()) {
                result.addGanttEntry(new GanttEntry("IDLE", currentTime, p.getArrivalTime()));
                currentTime = p.getArrivalTime();
            }

            int start = currentTime;
            int end = start + p.getBurstTime();

            int response = start - p.getArrivalTime();
            int turnaround = end - p.getArrivalTime();
            int waiting = turnaround - p.getBurstTime();

            p.setStartTime(start);
            p.setEndTime(end);
            p.setResponseTime(response);
            p.setTurnaroundTime(turnaround);
            p.setWaitingTime(waiting);
            p.setRemainingTime(0);

            result.addProcess(p);
            result.addGanttEntry(new GanttEntry(p.getId(), start, end));

            totalResponse += response;
            totalWaiting += waiting;
            totalTurnaround += turnaround;
            currentTime = end;
        }

        int n = ordered.size();
        result.setAvgResponseTime(totalResponse / n);
        result.setAvgWaitingTime(totalWaiting / n);
        result.setAvgTurnaroundTime(totalTurnaround / n);
        result.setTotalTime(currentTime);
        return result;
    }
}
