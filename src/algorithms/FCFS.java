package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.GanttEntry;
import model.Process;
import model.ScheduleResult;
import simulation.MetricsCalculator;

public class FCFS implements Scheduler {
    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public ScheduleResult schedule(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            throw new IllegalArgumentException("Processes must not be empty");
        }

        List<Process> ordered = new ArrayList<>(processes);
        ordered.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getId));

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, Integer> firstStart = new HashMap<>();
        Map<String, Integer> completion = new HashMap<>();

        int time = 0;
        for (Process p : ordered) {
            if (time < p.getArrivalTime()) {
                appendSegment(gantt, "IDLE", time, p.getArrivalTime());
                time = p.getArrivalTime();
            }

            firstStart.putIfAbsent(p.getId(), time);
            int end = time + p.getBurstTime();
            appendSegment(gantt, p.getId(), time, end);
            completion.put(p.getId(), end);
            time = end;
        }

        return MetricsCalculator.buildResult(getName(), ordered, firstStart, completion, gantt);
    }

    private static void appendSegment(List<GanttEntry> gantt, String pid, int start, int end) {
        if (end <= start) {
            return;
        }
        if (!gantt.isEmpty()) {
            GanttEntry last = gantt.get(gantt.size() - 1);
            if (last.getProcessId().equals(pid) && last.getEndTime() == start) {
                gantt.set(gantt.size() - 1, new GanttEntry(pid, last.getStartTime(), end));
                return;
            }
        }
        gantt.add(new GanttEntry(pid, start, end));
    }
}