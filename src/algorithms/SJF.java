package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.GanttEntry;
import model.Process;
import model.ScheduleResult;

public class SJF implements Scheduler {
    @Override
    public ScheduleResult schedule(List<Process> processes) {
        ScheduleResult result = new ScheduleResult("SJF");

        if (processes == null || processes.isEmpty()) {
            result.setAvgWaitingTime(0.0);
            result.setAvgTurnaroundTime(0.0);
            result.setAvgResponseTime(0.0);
            result.setTotalTime(0);
            return result;
        }

        List<Process> remaining = new ArrayList<>();
        for (Process p : processes) {
            remaining.add(p.clone());
        }

        remaining.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getId));

        int currentTime = 0;
        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;

        while (!remaining.isEmpty()) {
            Process selected = null;

            for (Process p : remaining) {
                if (p.getArrivalTime() > currentTime) {
                    continue;
                }

                if (selected == null
                        || p.getBurstTime() < selected.getBurstTime()
                        || (p.getBurstTime() == selected.getBurstTime() && p.getArrivalTime() < selected.getArrivalTime())
                        || (p.getBurstTime() == selected.getBurstTime() && p.getArrivalTime() == selected.getArrivalTime() && p.getId().compareTo(selected.getId()) < 0)) {
                    selected = p;
                }
            }

            if (selected == null) {
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : remaining) {
                    if (p.getArrivalTime() < nextArrival) {
                        nextArrival = p.getArrivalTime();
                    }
                }
                result.addGanttEntry(new GanttEntry("IDLE", currentTime, nextArrival));
                currentTime = nextArrival;
                continue;
            }

            int start = currentTime;
            int end = start + selected.getBurstTime();

            int response = start - selected.getArrivalTime();
            int turnaround = end - selected.getArrivalTime();
            int waiting = turnaround - selected.getBurstTime();

            selected.setStartTime(start);
            selected.setEndTime(end);
            selected.setResponseTime(response);
            selected.setTurnaroundTime(turnaround);
            selected.setWaitingTime(waiting);
            selected.setRemainingTime(0);

            result.addProcess(selected);
            result.addGanttEntry(new GanttEntry(selected.getId(), start, end));

            totalResponse += response;
            totalWaiting += waiting;
            totalTurnaround += turnaround;
            currentTime = end;

            remaining.remove(selected);
        }

        int n = processes.size();
        result.setAvgResponseTime(totalResponse / n);
        result.setAvgWaitingTime(totalWaiting / n);
        result.setAvgTurnaroundTime(totalTurnaround / n);
        result.setTotalTime(currentTime);
        return result;
    }
}
